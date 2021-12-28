package steve6472.scriptit;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/23/2021
 * Project: ScriptIt
 *
 ***********************/
public class ScriptReader
{
	public static boolean DEBUG = false;
	public static final String EMPTY_LINE = "%EMPTY_LINE%";
	public static final String NEW_LINE = "%NEW_LINE%";
	public static final String COMMENT_START = "%COMMENT_START%";
	public static final String COMMENT_END = "%COMMENT_END%";

	public static String COLOR_WHILE = Log.RED;
	public static String COLOR_IF_ELSE = Log.GREEN;
	public static String COLOR_IF = Log.YELLOW;
	public static String COLOR_FUNCTION = Log.BLUE;
	public static String COLOR_EXPRESSION = Log.MAGENTA;
	public static String COLOR_FUNCTION_DECLARATION = Log.CYAN;
/*
	private static Expression createExpression(Script script, String line, boolean includeEmptyLines)
	{
		if (line.startsWith("/*"))
		{
			depth++;
			if (DEBUG)
			{
				System.out.println(tree() + "Multiline Comment " + line.replace("\n", "\\n") + Log.RESET);
			}
			depth--;
			if (includeEmptyLines)
				return new Comment(Highlighter.COMMENT + line + Highlighter.RESET);
			else
				return null;
		}
		else if (line.startsWith("//"))
		{
			depth++;
			if (DEBUG)
			{
				System.out.println(tree() + "Comment " + line + Log.RESET);
			}
			depth--;
			if (includeEmptyLines)
				return new Comment(Highlighter.COMMENT + line + Highlighter.RESET);
			else
				return null;
		}
		else if (line.equals(EMPTY_LINE))
		{
			depth++;
			if (DEBUG)
			{
				System.out.println(tree() + "Empty Line" + Log.RESET);
			}
			depth--;
			if (includeEmptyLines)
				return new Comment(Highlighter.EMPTY_LINE);
			else
				return null;
		}
		else if (line.startsWith("function"))
		{
			depth++;
			if (DEBUG)
			{
				System.out.print(COLOR_FUNCTION_DECLARATION);
				System.out.print(tree() + "Function Declaration: ");
				Log.reset();
				System.out.println(line);
			}
			line = line.substring(8).trim();
			String name = line.substring(0, line.indexOf('('));
			String arguments = line.substring(name.length() + 1, line.indexOf(')'));
			String function = line.substring(line.indexOf('{'), line.lastIndexOf('}')).trim();

			String[] args = arguments.split(",");
			int i1 = arguments.isBlank() ? 0 : args.length;
			Type[] types = new Type[i1];
			String[] names = new String[i1];

			if (!arguments.isBlank())
			{
				for (int i = 0; i < args.length; i++)
				{
					String[] arg = args[i].trim().split("\s+");
					types[i] = script.getWorkspace().getType(arg[0]);
					names[i] = arg[1];
				}
			}

			if (DEBUG)
			{
				System.out.println(tree() + Log.BRIGHT_CYAN + "Line: " + Log.RESET + line);
				System.out.println(tree() + Log.BRIGHT_CYAN + "Name: " + Log.RESET + name);
				System.out.println(tree() + Log.BRIGHT_CYAN + "Arguments: " + Log.RESET + (arguments.isEmpty() ? "NONE" : arguments));
			}

			Function body = (Function) createExpression(script, function, includeEmptyLines);
			depth--;
			return new DeclareFunction(name, body, names, types);
		}
		else if (Pattern.compile("while\s*\\(.*\\).*").matcher(line).matches())
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

			String cond = s.substring(0, s.lastIndexOf(')')).trim();
			if (DEBUG)
			{
				Log.brightRed();
				System.out.print(tree() + "Condition: ");
				Log.reset();
				System.out.println(cond);
			}
			Expression condition = script.getParser().setExpression(cond).parse();
			String trim = '{' + split[1].trim();
			trim = trim.substring(0, trim.length() - 1);
			If anIf = new If(condition, createExpression(script, trim, includeEmptyLines));
			depth--;
			return new While(anIf);
		}
		else if (Pattern.compile("if\s*\\(.+\\).*\\{.*\\}\s*(%NEW_LINE%)?\s*else\s*(%NEW_LINE%)?\s*\\{.*\\}").matcher(line).matches() && !isIf(line))
		{
			depth++;
			if (DEBUG)
			{
				System.out.print(COLOR_IF_ELSE);
				System.out.print(tree() + "If Else: ");
				Log.reset();
				System.out.println(line);
			}

			String[] split = splitElse(line);
			split[0] = split[0].trim();
			split[1] = split[1].trim();
			split[1] = split[1].substring(split[1].indexOf('{'), split[1].lastIndexOf('}')).trim();

			If anIf = (If) createExpression(script, split[0], includeEmptyLines);

			if (DEBUG)
			{
				Log.brightGreen();
				System.out.print(tree() + "Else: ");
				Log.reset();
			}

			if (Pattern.compile("if\s*\\(.+\\).*\\{.*\\}\s*else\s*\\{.*\\}").matcher(split[1]).matches())
			{
				split[1] = split[1].substring(1, split[1].length() - 1).trim();
			}

			Expression expression = createExpression(script, split[1], includeEmptyLines);
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
			String trim1 = s.substring(0, s.lastIndexOf(')')).trim();
			if (DEBUG)
			{
				Log.brightYellow();
				System.out.print(tree() + "Condition: ");
				Log.reset();
				System.out.println(trim1);
			}
			Expression condition = script.getParser().setExpression(trim1).parse();
			String body = '{' + split[1].trim();
			body = body.substring(0, body.length() - 1);
			if (DEBUG)
			{
				Log.brightYellow();
				System.out.print(tree() + "Body: ");
				Log.reset();
				System.out.println(body);
			}
			Expression expression = createExpression(script, body, includeEmptyLines);
			depth--;
			return new If(condition, expression);
		}
		else if (line.startsWith("{"))
		{
			depth++;
			if (DEBUG)
			{
				System.out.print(COLOR_FUNCTION);
				System.out.print(tree() + "Function: ");
				Log.reset();
				System.out.println(line);
			}
			String substring = line.substring(1).trim();
			substring = substring.replace(COMMENT_START, "").replace(COMMENT_END, "");
			substring = substring.replace(NEW_LINE, "\n").replace(EMPTY_LINE, "\n");
			if (substring.startsWith("\n"))
				substring = substring.substring(1);
			List<String> split = split(substring);
			List<Expression> exps = new ArrayList<>();
			for (String s : split)
			{
				Expression expression = createExpression(script, s.trim(), includeEmptyLines);
				if (expression != null)
					exps.add(expression);
			}
			Function func = new Function();
			func.setExpressions(script, exps.toArray(new Expression[0]));
			depth--;
			return func;
		}
		else
		{
			depth++;
			if (DEBUG)
			{
				System.out.print(COLOR_EXPRESSION);
				System.out.print(tree() + "Expression: ");
				Log.reset();
				System.out.println(line);
			}
			line = line.replaceAll("\\\\n", "\n");
			depth--;
			return script.getParser().setExpression(line).parse();
		}
	}*/
}
