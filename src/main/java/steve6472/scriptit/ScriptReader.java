package steve6472.scriptit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/23/2021
 * Project: ScriptIt
 *
 ***********************/
public class ScriptReader
{
	public static boolean DEBUG = true;

	public static String COLOR_WHILE = Log.RED;
	public static String COLOR_IF_ELSE = Log.GREEN;
	public static String COLOR_IF = Log.YELLOW;
	public static String COLOR_FUNCTION = Log.BLUE;
	public static String COLOR_EXPRESSION = Log.MAGENTA;

	private static int depth = 0;

	private static String tree()
	{
		return "    ".repeat(depth - 1);
	}

	public static Script readScript(File file, Workspace workspace)
	{
		depth = 0;
		Script script = new Script(workspace);

		List<String> split = split(readFromFile(file));
		List<Expression> expressions = new ArrayList<>();

		for (String s : split)
		{
			expressions.add(createExpression(script, s));
		}
		script.setExpressions(expressions.toArray(new Expression[0]));

		return script;
	}

	private static Expression createExpression(Script script, String line)
	{
		if (Pattern.compile("while\s*\\(.*\\).*").matcher(line).matches())
		{
			depth++;
			if (DEBUG)
			{
				System.out.print(COLOR_WHILE);
				System.out.print(tree() + "While: ");
				Log.reset();
				System.out.println(line);
			}
			String[] split = line.split("\\{", 2);
			String s = split[0].split("\\(", 2)[1].trim();

			String cond = s.substring(0, s.length() - 1).trim();
			if (DEBUG)
			{
				Log.brightRed();
				System.out.print(tree() + "Condition: ");
				Log.reset();
				System.out.println(cond);
			}
			Expression condition = script.getParser().setExpression(cond).parse();
			String trim = split[1].trim();
			If anIf = new If(condition, (Function) createExpression(script, trim));
			depth--;
			return new While(anIf);
		}
		else if (Pattern.compile("if\s*\\(.+\\).*\\{.*\\}\s*else\s*\\{.*\\}").matcher(line).matches() && !isIf(line))
		{
			depth++;
			if (DEBUG)
			{
				System.out.print(COLOR_IF_ELSE);
				System.out.print(tree() + "If Else: ");
				Log.reset();
				System.out.println(line);
			}

			// FIXME: Wrong split, ignores nested else
//			String[] split = line.split("\\}\s*else\s*\\{");
			String[] split = splitElse(line);
			split[0] = split[0].trim();
			split[1] = split[1].trim();
			split[1] = split[1].substring(split[1].indexOf('{') + 1).trim();

			If anIf = (If) createExpression(script, split[0]);

			if (DEBUG)
			{
				Log.brightGreen();
				System.out.println(tree() + "Else");
				Log.reset();
			}

			if (Pattern.compile("if\s*\\(.+\\).*\\{.*\\}\s*else\s*\\{.*\\}").matcher(split[1]).matches())
			{
				split[1] = split[1].substring(0, split[1].length() - 1).trim();
			}

			Expression expression = createExpression(script, split[1]);
			depth--;
			return new IfElse(anIf, expression);
		}
		else if (Pattern.compile("if\s*\\(.*\\).*").matcher(line).matches() && isIf(line))
		{
			depth++;
			if (DEBUG)
			{
				System.out.print(COLOR_IF);
				System.out.print(tree() + "If: ");
				Log.reset();
				System.out.println(line);
			}
			String[] split = line.split("\\{", 2);
			String s = split[0].split("\\(", 2)[1].trim();
			String trim1 = s.substring(0, s.length() - 1).trim();
			if (DEBUG)
			{
				Log.brightYellow();
				System.out.print(tree() + "Condition: ");
				Log.reset();
				System.out.println(trim1);
			}
			Expression condition = script.getParser().setExpression(trim1).parse();
			String trim = split[1].trim();
			Expression expression = createExpression(script, trim);
			depth--;
			return new If(condition, expression);
		}
		else if (line.endsWith("}"))
		{
			depth++;
			if (DEBUG)
			{
				System.out.print(COLOR_FUNCTION);
				System.out.print(tree() + "Function: ");
				Log.reset();
				System.out.println(line);
			}
			String substring = line.substring(0, line.length() - 1).trim();
			List<String> split = split(substring);
			List<Expression> exps = new ArrayList<>();
			for (String s : split)
			{
				exps.add(createExpression(script, s.trim()));
			}
			Function func = new Function();
			func.setExpressions(script, exps.toArray(new Expression[0]));
			depth--;
			return func;
		}
		else
		{
			depth++;
			line = line.replaceAll("\\\\n", "\n");
			if (DEBUG)
			{
				System.out.print(COLOR_EXPRESSION);
				System.out.print(tree() + "Expression: ");
				Log.reset();
				System.out.println(line);
			}
			depth--;
			return script.getParser().setExpression(line).parse();
		}
	}

	private static boolean isIf(String s)
	{
		boolean inString = false;
		boolean escaped = false;

		int bCounter = 0;
		int i;

		for (i = 0; i < s.length(); i++)
		{
			char ch = s.charAt(i);

			if (!inString && ch == '/' && s.charAt(i + 1) == '/')
			{
				while (ch != '\n')
				{
					i++;
					ch = s.charAt(i);
				}
			}

			if (!inString && ch == '/' && s.charAt(i + 1) == '*')
			{
				i += 2;
				ch = s.charAt(i);
				while (ch != '*' || s.charAt(i + 1) != '/')
				{
					i++;
					ch = s.charAt(i);
				}
				i += 2;
				ch = s.charAt(i);
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
					if (!s.substring(i + 1).trim().startsWith("else"))
					{
						return true;
					} else
					{
						return false;
					}
				}
			}

			if (ch == ';' && !inString && bCounter == 0)
			{
				continue;
			}

			if (ch == '\\')
			{
				if (s.charAt(i + 1) == 'n')
				{
					i++;
					continue;
				} else
				{
					escaped = true;
				}
			}

			if (ch == '\n' && !escaped)
			{
				continue;
			}

			if (ch == '"' && !escaped)
			{
				inString = !inString;
			}

			if (ch == '"' && escaped)
			{
				escaped = false;
			}

		}

		throw new RuntimeException("Should not get here! missing '}' ?");
	}

	private static String[] splitElse(String s)
	{
		boolean inString = false;
		boolean escaped = false;

		int bCounter = 0;
		int i;

		for (i = 0; i < s.length(); i++)
		{
			char ch = s.charAt(i);

			if (!inString && ch == '/' && s.charAt(i + 1) == '/')
			{
				while (ch != '\n')
				{
					i++;
					ch = s.charAt(i);
				}
			}

			if (!inString && ch == '/' && s.charAt(i + 1) == '*')
			{
				i += 2;
				ch = s.charAt(i);
				while (ch != '*' || s.charAt(i + 1) != '/')
				{
					i++;
					ch = s.charAt(i);
				}
				i += 2;
				ch = s.charAt(i);
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
					if (s.substring(i + 1).trim().startsWith("else"))
					{
						return new String[] {s.substring(0, i + 1).trim(), s.substring(i + 1)};
					} else
					{
						throw new RuntimeException("No 'else' in ifElse ??");
					}
				}
			}

			if (ch == ';' && !inString && bCounter == 0)
			{
				continue;
			}

			if (ch == '\\')
			{
				if (s.charAt(i + 1) == 'n')
				{
					i++;
					continue;
				} else
				{
					escaped = true;
				}
			}

			if (ch == '\n' && !escaped)
			{
				continue;
			}

			if (ch == '"' && !escaped)
			{
				inString = !inString;
			}

			if (ch == '"' && escaped)
			{
				escaped = false;
			}

		}

		throw new RuntimeException("No 'else' in ifElse ?");
	}

	public static List<String> split(String script)
	{
		List<String> lines = new ArrayList<>();
		StringBuilder builder = new StringBuilder();

		boolean inString = false;
		boolean escaped = false;

		int bCounter = 0;
		int i;

		for (i = 0; i < script.length(); i++)
		{
			char ch = script.charAt(i);

			if (!inString && ch == '/' && script.charAt(i + 1) == '/')
			{
				while (ch != '\n')
				{
					i++;
					ch = script.charAt(i);
				}
			}

			if (!inString && ch == '/' && script.charAt(i + 1) == '*')
			{
				i += 2;
				ch = script.charAt(i);
				while (ch != '*' || script.charAt(i + 1) != '/')
				{
					i++;
					ch = script.charAt(i);
				}
				i += 2;
				ch = script.charAt(i);
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
					if (script.substring(i + 1).trim().startsWith("else"))
					{
						i++;
						builder.append(ch);
						continue;
					}
					i++;
					builder.append(ch);
					lines.add(builder.toString().trim());
					builder.setLength(0);
					continue;
				}
			}

			if (ch == ';' && !inString && bCounter == 0)
			{
				lines.add(builder.toString().trim());
				builder.setLength(0);
				continue;
			}

			if (ch == '\\')
			{
				if (script.charAt(i + 1) == 'n')
				{
					i++;
					builder.append("\\n");
					continue;
				} else
				{
					escaped = true;
				}
			}

			if (ch == '\n' && !escaped)
			{
				continue;
			}

			if (ch == '"' && !escaped)
			{
				inString = !inString;
			}

			if (ch == '"' && escaped)
			{
				escaped = false;
			}

			builder.append(ch);
		}

//		if (bCounter == 1 && i == script.length())
//		{
//			lines.set(lines.size() - 1, lines.get(lines.size() - 1) + "}");
//		}

		return lines;
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
}
