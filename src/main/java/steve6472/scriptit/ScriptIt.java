package steve6472.scriptit;

import steve6472.scriptit.expression.Constructor;
import steve6472.scriptit.expression.FunctionParameters;
import steve6472.scriptit.expression.Type;
import steve6472.scriptit.expression.Value;
import steve6472.scriptit.instructions.*;
import steve6472.scriptit.instructions.type.*;

import java.io.*;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 4/30/2021
 * Project: ScriptIt
 *
 ***********************/
public class ScriptIt
{
	public static final boolean DEBUG = false;

	public static final Map<Pattern, TriFunction<Workspace, Script, String, Instruction>> mainCommandMap = new LinkedHashMap<>();
	public static final Map<Pattern, TriFunction<Workspace, Script, String, Instruction>> typeCommandMap = new LinkedHashMap<>();

	static
	{
		mainCommandMap.put(Regexes.IMPORT_FUNCTIONS, ImportFunctions::new);
		mainCommandMap.put(Regexes.IMPORT, ImportType::new);
		mainCommandMap.put(Regexes.WHILE, WhileLoop::new);
		mainCommandMap.put(Regexes.FOR, ForLoop::new);
		mainCommandMap.put(Regexes.IF, If::new);
		mainCommandMap.put(Regexes.BREAK, (w, s, l) -> new Break());
		mainCommandMap.put(Regexes.CONTINUE, (w, s, l) -> new Continue());
		mainCommandMap.put(Regexes.THIS_ASSIGN, (workspace, script, line) -> new ThisAssignValue(script, line));
		mainCommandMap.put(Regexes.DECLARE_TYPE, DeclareType::new);
		mainCommandMap.put(Regexes.RETURN_THIS, (workspace, script, line) -> new ReturnTypeThisValue());
		mainCommandMap.put(Regexes.RETURN, (workspace, script, line) -> new ReturnValue(line));
		mainCommandMap.put(Regexes.VALUE_DECLARATION, (workspace, script, line) -> new DeclareValue(script, line));
		mainCommandMap.put(Regexes.VALUE_ASSIGN, (workspace, script, line) -> new AssignValue(line));
		mainCommandMap.put(Regexes.VALUE_DECLARATION_ASSIGN, (workspace, script, line) -> new DeclareAssignValue(line));
		mainCommandMap.put(Regexes.DECLARE_FUNCTION, DeclareFunction::new);
		mainCommandMap.put(Regexes.DELAY, (workspace, script, line) -> new Delay(line));

		typeCommandMap.put(Regexes.IMPORT_FUNCTIONS, ImportFunctions::new);
		typeCommandMap.put(Regexes.DECLARE_FUNCTION, (workspace, script, line) -> new DeclareTypeFunction(script, line));
		typeCommandMap.put(Regexes.IMPORT, ImportType::new);
		typeCommandMap.put(Regexes.OPERATOR_OVERLOAD_FUNCTION, (workspace, script, line) -> new DeclareOperatorOverload(script, line));
		typeCommandMap.put(Regexes.TYPE_VALUE_DECLARATION, (workspace, script, line) -> new DeclareTypeValue(script, line));
		typeCommandMap.put(Regexes.DECLARE_CONSTRUCTOR, (workspace, script, line) -> new DeclareTypeConstructor(script, line));
	}

	public static String readFromFile(File file)
	{
		if (!file.exists())
			throw new IllegalArgumentException("File not found");
		if (file.isDirectory())
			throw new IllegalArgumentException("File is a directory");

		StringBuilder bobTheBuilder = new StringBuilder();

		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String s;
			while ((s = reader.readLine()) != null)
			{
				bobTheBuilder.append(s).append("\n");
			}

		} catch (IOException e)
		{
			e.printStackTrace();
		}

		return bobTheBuilder.toString();
	}

	public static void main(String[] args)
	{
		Workspace workspace = new Workspace();
		workspace.addType(TypeDeclarations.INT);
		workspace.addType(TypeDeclarations.DOUBLE);
		workspace.addType(TypeDeclarations.STRING);
		workspace.addType(TypeDeclarations.CHAR);
		workspace.addType(TypeDeclarations.BOOL);
		workspace.addType(TypeDeclarations.ARRAY);
		workspace.addType(TypeDeclarations.VEC2);

		BasicFunctions.addColorPrint(workspace);
		BasicFunctions.addMathFunctions(workspace);
		BasicFunctions.addPrintFunctions(workspace);

		Script script = createScript(workspace, readFromFile(new File("scripts/while.scriptit")), true, mainCommandMap);

		script.run();
//		basicDelayedScriptLoop(script);
	}

	public static void basicDelayedScriptLoop(Script script)
	{
		boolean loop = true;
		while (loop)
		{
			Value value = script.run();
			if (value != null)
				loop = !value.getBoolean();
		}
	}

	public static Script createScript(Workspace workspace, String code, boolean evalFallback, Map<Pattern, TriFunction<Workspace, Script, String, Instruction>> commandMap)
	{
		return createScript(workspace, null, code, evalFallback, commandMap, null, null);
	}

	public static Script createScript(Workspace workspace, Script parent, String code, boolean evalFallback, Map<Pattern, TriFunction<Workspace, Script, String, Instruction>> commandMap)
	{
		return createScript(workspace, parent, code, evalFallback, commandMap, null, null);
	}

	public static Script createScript(Workspace workspace, String code, boolean evalFallback, Map<Pattern, TriFunction<Workspace, Script, String, Instruction>> commandMap, Collection<Type> preImportedTypes, Map<FunctionParameters, Constructor> preImportedFunctions)
	{
		return createScript(workspace, null, code, evalFallback, commandMap, preImportedTypes, preImportedFunctions);
	}

	public static Script createScript(Workspace workspace, Script parentScript, String code, boolean evalFallback, Map<Pattern, TriFunction<Workspace, Script, String, Instruction>> commandMap, Collection<Type> preImportedTypes, Map<FunctionParameters, Constructor> preImportedFunctions)
	{
		Script script = new Script(parentScript);

		if (preImportedTypes != null)
		{
			for (Type preImportedType : preImportedTypes)
			{
				script.importType(preImportedType);
			}
		}

		if (preImportedFunctions != null)
		{
			preImportedFunctions.forEach(script::addConstructor);
		}

		int i = 0;
		while (i < code.length())
		{
			Object[] arr = nextLine(code, i);
			String line = (String) arr[0];
			if (line.isBlank())
			{
				if ((int) arr[1] >= code.length())
					break;
				continue;
			}
			if (DEBUG)
				System.out.println("Processing line '" + Log.GREEN + line + Log.RESET + "'");
			boolean foundCommand = false;
			for (Map.Entry<Pattern, TriFunction<Workspace, Script, String, Instruction>> entry : commandMap.entrySet())
			{
				Pattern pattern = entry.getKey();
				if (DEBUG)
					System.out.println("Trying " + Log.BRIGHT_MAGENTA + pattern.pattern() + Log.RESET);
				TriFunction<Workspace, Script, String, Instruction> command = entry.getValue();
				Matcher matcher = pattern.matcher(line);
				if (matcher.matches())
				{
					if (DEBUG)
						System.out.println(line + " matches " + Log.MAGENTA + pattern.pattern() + Log.RESET + " " + command.apply(workspace, script, line).getClass().getCanonicalName());
					try
					{
						script.instructions.add(command.apply(workspace, script, line));
						foundCommand = true;
						break;
					} catch (Exception ex)
					{
						System.err.println(line);
						ex.printStackTrace();
						System.exit(1);
					}
				}
			}
			if (!foundCommand)
			{
				if (evalFallback)
				{
					if (DEBUG)
						System.out.println("No Pattern matched, defaulting to expression");
					script.instructions.add(new EvalExpression(line));
				} else
				{
					throw new IllegalArgumentException("Instruction not found for line\n" + line);
				}
			}
			i = (int) arr[1];
		}

		return script;
	}

	public static Object[] nextLine(String text, int start)
	{
		StringBuilder bobThe = new StringBuilder();

		int i;

		boolean insideString = false;
		boolean escaped = false;

		int bCounter = 0;

		for (i = start; i < text.length(); i++)
		{
			char ch = text.charAt(i);
			// Skip single line comments
			if (!insideString && ch == '/' && text.charAt(i + 1) == '/')
			{
				while (ch != '\n')
				{
					i++;
					ch = text.charAt(i);
				}
			}

			if (!insideString && ch == '/' && text.charAt(i + 1) == '*')
			{
				i += 2;
				ch = text.charAt(i);
				while (ch != '*' || text.charAt(i + 1) != '/')
				{
					i++;
					ch = text.charAt(i);
				}
				i += 2;
				ch = text.charAt(i);
			}

			if (ch == '{')
			{
				bCounter++;
			}

			if (ch == '}')
			{
				bCounter--;
				if (bCounter == 0)
				{
					i++;
					bobThe.append(ch);
					if (DEBUG)
					{
						Log.blue();
						System.out.println(bobThe.toString().trim());
						Log.reset();
					}
					return new Object[] {bobThe.toString().trim(), i};
				}
			}

			if (ch == ';' && !insideString && bCounter == 0)
			{
				i++;
				break;
			}

			if (ch == '\\')
			{
				escaped = true;
			}

			if (ch == '\n' && !escaped)
			{
				continue;
			}

			if (ch == '"' && !escaped)
			{
				insideString = !insideString;
			}

			if (ch == '"' && escaped)
			{
				escaped = false;
			}

			bobThe.append(ch);
		}

		if (bCounter == 1 && i == text.length())
		{
			bobThe.append('}');
			bCounter--;
		}

		if (DEBUG)
		{
			Log.green();
			System.out.println(bobThe.toString().trim());
			Log.yellow();
			System.out.println("Index: " + i + ", TextLenght: " + text.length() + ", bCounter: " + bCounter);
			Log.reset();
		}

		return new Object[] {bobThe.toString().trim(), i};
	}
}
