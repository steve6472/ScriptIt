package steve6472.scriptit.exp;

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
	public static Script readScript(File file, Workspace workspace)
	{
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
			String[] split = line.split("\\{", 2);
			String s = split[0].split("\\(", 2)[1].trim();
			Expression condition = script.getParser().setExpression(s.substring(0, s.length() - 1).trim()).parse();
			String trim = split[1].trim();
			If anIf = new If(condition, (Function) createExpression(script, trim));
			return new While(anIf);
		}
		else if (Pattern.compile("if\s*\\(.+\\).*\\{.*\\}\s*else\s*\\{.*\\}").matcher(line).matches())
		{
			String[] split = line.split("\\}\s*else\s*\\{");

			If anIf = (If) createExpression(script, split[0] + "}");

			return new IfElse(anIf, (Function) createExpression(script, split[1]));
		}
		else if (Pattern.compile("if\s*\\(.*\\).*").matcher(line).matches())
		{
			String[] split = line.split("\\{", 2);
			String s = split[0].split("\\(", 2)[1].trim();
			Expression condition = script.getParser().setExpression(s.substring(0, s.length() - 1).trim()).parse();
			String trim = split[1].trim();
			return new If(condition, (Function) createExpression(script, trim));
		}
		else if (line.endsWith("}"))
		{
			String substring = line.substring(0, line.length() - 1).trim();
			List<String> split = split(substring);
			List<Expression> exps = new ArrayList<>();
			for (String s : split)
			{
				exps.add(createExpression(script, s));
			}
			Function func = new Function();
			func.setExpressions(script, exps.toArray(new Expression[0]));
			return func;
		}
		else
		{
			return script.getParser().setExpression(line).parse();
		}
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
				escaped = true;
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
