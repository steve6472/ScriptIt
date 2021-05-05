package steve6472.scriptit;

import steve6472.scriptit.expression.Type;
import steve6472.scriptit.instructions.*;
import steve6472.scriptit.instructions.type.DeclareTypeConstructor;
import steve6472.scriptit.instructions.type.DeclareTypeValue;
import steve6472.scriptit.instructions.type.ThisAssignValue;

import java.util.Collection;
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

/*
int a;
//int b = 5;
//int c = 9 + 10 * 6;
a = int(double(3.2)*6.2 * 5.0);
a.print(); // 99
//b.print();
//c.print();


function void testExpressionParser()
{
	int(7).print();
	(-(6 * 7)).print();
	(int(6) * 7).print();
	("Hello" + " world!").print();
	(("Hello" + ' ' + "world" + '!').len() * 2).print();
	string("_1\\"3_").print();
	vec2(0, 9).-toString().print();
	string("Hello World").-len().print();
	("Hello World"-3).print();
	("#-" * 10 + '#').print();
	vec2(8 + stuff(3, int(6)) * 4, 2).toString().print(); // [92.0, 2.0]
	(vec2(6.9, 4.2) * 7.5).print(); //vec2[x=51.75,y=31.5]
}

testExpressionParser();


// This somehow works...
function void output(string text)
{
	text.print();
}

output("Hi World!");

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
*/

class vec3
{
	double x;
	double y;
	double z;

	constructor()
	{
		this.x = 0.0;
		this.y = 0.0;
		this.z = 0.0;
	}
	
	constructor(double X, double Y, double Z)
	{
		this.x = X;
		this.y = Y;
		this.z = Z;
	}
	
/*
	function void print()
	{
		("vec3[x=" + string(x) + ",y=" + string(y) + ",z=" + string(z) + "]).printRaw();
	}
	*/
}

class TestClass
{
	double x;
	
	constructor(double X)
	{
		this.x = X;
	}
	
	constructor()
	{
		this.x = 0.0;
	}
}

string helloStringTest = "Hello World!";
helloStringTest._printAllValues();
helloStringTest.printRaw();
" ".printRaw();

int intTest = 5;
intTest._printAllValues();
" ".printRaw();

double doubleTest = 5.2;
doubleTest._printAllValues();
" ".printRaw();

vec2 vec2Test = vec2(3.0, 6.0);
vec2Test._printAllValues();
vec2Test.print();
" ".printRaw();

vec3 test = vec3();
test._printAllValues();
" ".printRaw();

test = vec3(1.2, 3.4, 5.6);
test._printAllValues();
" ".printRaw();

TestClass testClass = TestClass(6.9);
testClass._printAllValues();
" ".printRaw();

testClass = TestClass();
testClass._printAllValues();
" ".printRaw();

""";

	public static final boolean DEBUG = true;

	public static final Map<Pattern, BiFunction<Script, String, Instruction>> mainCommandMap = new LinkedHashMap<>();
	public static final Map<Pattern, BiFunction<Script, String, Instruction>> typeCommandMap = new LinkedHashMap<>();

	static
	{
		mainCommandMap.put(Regexes.IMPORT, ImportType::new);
		mainCommandMap.put(Regexes.THIS_ASSIGN, ThisAssignValue::new);
		mainCommandMap.put(Regexes.DECLARE_TYPE, DeclareType::new);
		mainCommandMap.put(Regexes.VALUE_DECLARATION, DeclareValue::new);
		mainCommandMap.put(Regexes.VALUE_ASSIGN, (script, line) -> new AssignValue(line));
		mainCommandMap.put(Regexes.VALUE_DECLARATION_ASSIGN, (script, line) -> new DeclareAssignValue(line));
		mainCommandMap.put(Regexes.RETURN, (script, line) -> new ReturnValue(line));
		mainCommandMap.put(Regexes.DECLARE_FUNCTION, DeclareFunction::new);

		typeCommandMap.put(Regexes.IMPORT, ImportType::new);
		typeCommandMap.put(Regexes.VALUE_DECLARATION, DeclareTypeValue::new);
		typeCommandMap.put(Regexes.DECLARE_CONSTRUCTOR, DeclareTypeConstructor::new);
	}

	public static void main(String[] args)
	{
		Script script = createScript(source, true, mainCommandMap);

//		script.printCode();
		script.run();
	}

	public static Script createScript(String code, boolean evalFallback, Map<Pattern, BiFunction<Script, String, Instruction>> commandMap)
	{
		return createScript(null, code, evalFallback, commandMap, null);
	}

	public static Script createScript(Script parent, String code, boolean evalFallback, Map<Pattern, BiFunction<Script, String, Instruction>> commandMap)
	{
		return createScript(parent, code, evalFallback, commandMap, null);
	}

	public static Script createScript(String code, boolean evalFallback, Map<Pattern, BiFunction<Script, String, Instruction>> commandMap, Collection<Type> preImportedTypes)
	{
		return createScript(null, code, evalFallback, commandMap, preImportedTypes);
	}

	public static Script createScript(Script parentScript, String code, boolean evalFallback, Map<Pattern, BiFunction<Script, String, Instruction>> commandMap, Collection<Type> preImportedTypes)
	{
		Script script = new Script(parentScript);

		if (preImportedTypes != null)
		{
			for (Type preImportedType : preImportedTypes)
			{
				script.importType(preImportedType);
			}
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
			for (Map.Entry<Pattern, BiFunction<Script, String, Instruction>> entry : commandMap.entrySet())
			{
				Pattern pattern = entry.getKey();
				if (DEBUG)
					System.out.println("Trying " + Log.BRIGHT_MAGENTA + pattern.pattern() + Log.RESET);
				BiFunction<Script, String, Instruction> command = entry.getValue();
				Matcher matcher = pattern.matcher(line);
				if (matcher.matches())
				{
					if (DEBUG)
						System.out.println(line + " matches " + Log.MAGENTA + pattern.pattern() + Log.RESET);
					try
					{
						script.instructions.add(command.apply(script, line));
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
