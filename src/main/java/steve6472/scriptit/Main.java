package steve6472.scriptit;

import steve6472.scriptit.commands.AssignValue;
import steve6472.scriptit.commands.DeclareValue;
import steve6472.scriptit.commands.ImportType;

import java.util.HashMap;
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

	string adr = vec2((8 + func(2, 3)) * 3, 2).toString();
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
//a = c * (b + 3);
a = 5 * int(double(3.2)*6.2);

""";

	private static Map<Pattern, BiFunction<Script, String, Command>> commandMap = new HashMap<>();

	static
	{
		commandMap.put(Stuff.IMPORT, (script, line) -> new ImportType(line));
		commandMap.put(Stuff.VALUE_DECLARATION, (script, line) -> new DeclareValue(line));
		commandMap.put(Stuff.VALUE_ASSIGN, (script, line) -> new AssignValue(line));
	}

	public static void main(String[] args)
	{
		Script script = new Script();
		int i = 0;
		while (i < source.length())
		{
			Object[] arr = nextLine(i);
			String line = (String) arr[0];
			commandMap.forEach(((pattern, command) ->
			{
				Matcher matcher = Stuff.VALUE_DECLARATION.matcher(line);
				if (matcher.matches())
				{
					script.commands.add(command.apply(script, line));
				}
			}));
			System.out.println(line);
			i = (int) arr[1];
		}
	}

	private static Object[] nextLine(int start)
	{
		StringBuilder bobThe = new StringBuilder();

		int i;

		boolean insideString = false;
		boolean escaped = false;

		for (i = start; i < source.length(); i++)
		{
			char c = source.charAt(i);
			// Skip single line comments
			if (c == '/' && source.charAt(i + 1) == '/')
			{
				while (c != '\n')
				{
					c = source.charAt(i);
					i++;
				}
			}

			if (c == ';' && !insideString)
			{
				i++;
				break;
			}

			if (c == '\\')
			{
				escaped = true;
			}

			if (c == '\n' && !escaped)
			{
				continue;
			}

			if (c == '"' && !escaped)
			{
				insideString = !insideString;
			}

			if (c == '"' && escaped)
			{
				escaped = false;
			}

			bobThe.append(c);
		}

		return new Object[] {bobThe.toString(), i};
	}
}
