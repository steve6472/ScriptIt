package steve6472.scriptit;

import steve6472.scriptit.commands.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 4/30/2021
 * Project: ScriptIt
 *
 ***********************/
public class Main
{
	/*
	 * Multi
	 * Line
	 * Comment
	 */

	/*
	class vec2
	{
		int x;
		int y;

		constructor vec2(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
	}

	function int func(int a, int b)
	{
		return a + b * a;
	}

	*/
	private static final String source =
"""
// Imports
import int;
import double;
import string;
import bool;

import char;
import vec2;

int a;
//int b = 5;
//int c = 9 + 10 * 6;
a = int(double(3.2)*6.2 * 5.0);
a.print(); // 99
//b.print();
//c.print();

function int stuff(int varA, int varB)
{
	function void prnt()
	{
		"inside function thingie".print();
	}
	int temp = varA * varB;
	temp.print();
	prnt();
	a.print();
	return temp + varA;
}

int i = stuff(2, 3); // inside function thingie
i.print(); // 8

"before adr".print(); // before adr

string adr = vec2(8 + stuff(3, 6) * 3, 2).toString();
adr.print();

//class vec3
//{
//	double x;
//	double y;
//	double z;
//
//	constructor()
//	{
//		this.x = 0;
//		this.y = 0;
//		this.z = 0;
//	}
//
//	constructor(double x, double y, double z)
//	{
//		this.x = x;
//		this.y = y;
//		this.z = z;
//	}
//}

""";

	public static final boolean DEBUG = false;

	private static final Map<Pattern, BiFunction<Script, String, Command>> commandMap = new LinkedHashMap<>();

	static
	{
		commandMap.put(Regexes.IMPORT, ImportType::new);
		commandMap.put(Regexes.VALUE_DECLARATION, (script, line) -> new DeclareValue(line));
		commandMap.put(Regexes.VALUE_ASSIGN, (script, line) -> new AssignValue(line));
		commandMap.put(Regexes.VALUE_DECLARATION_ASSIGN, (script, line) -> new DeclareAssignValue(line));
		commandMap.put(Regexes.RETURN, (script, line) -> new ReturnValue(line));
		commandMap.put(Regexes.DECLARE_FUNCTION, DeclareFunction::new);
	}

	public static void main(String[] args)
	{
		Script script = createScript(source);

//		script.printCode();
		script.run();
	}

	public static Script createScript(String code)
	{
		return createScript(null, code);
	}

	public static Script createScript(Script parentScript, String code)
	{
		Script script = new Script(parentScript);
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
				System.out.println("Processing line '" + line + "'");
			boolean foundCommand = false;
			for (Map.Entry<Pattern, BiFunction<Script, String, Command>> entry : commandMap.entrySet())
			{
				Pattern pattern = entry.getKey();
				if (DEBUG)
					System.out.println("Trying " + pattern.pattern());
				BiFunction<Script, String, Command> command = entry.getValue();
				Matcher matcher = pattern.matcher(line);
				if (matcher.matches())
				{
					if (DEBUG)
						System.out.println(line + " matches " + pattern.pattern());
					try
					{
						script.commands.add(command.apply(script, line));
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
				if (DEBUG)
					System.out.println("No Pattern matched, defaulting to expression");
				script.commands.add(new EvalExpression(line));
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

		return new Object[] {bobThe.toString().trim(), i};
	}
}
