package steve6472.scriptit.tokenizer;

import steve6472.scriptit.Stack;
import steve6472.scriptit.*;
import steve6472.scriptit.expressions.Attributes;
import steve6472.scriptit.expressions.ClassDeclaration;
import steve6472.scriptit.expressions.Expression;
import steve6472.scriptit.simple.Comment;
import steve6472.scriptit.simple.Empty;
import steve6472.scriptit.tokenizer.parslet.*;

import java.util.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/13/2021
 * Project: ScriptIt
 * <p>
 * <a href="https://en.cppreference.com/w/cpp/language/operator_precedence">...</a>
 *
 ***********************/
public class TokenParser
{
	public static int depth = 0;

	public final Map<IOperator, PrefixParselet> prefixParslets = new HashMap<>();
	public final Map<IOperator, InfixParslet> infixParslets = new HashMap<>();

	public final Set<Class<?>> ignoredExpressions = new HashSet<>();

	public TokenParser()
	{
		prefixParslets.put(Operator.NAME, new NameParslet());
		prefixParslets.put(Operator.IMPORT, new ImportParslet());
		prefixParslets.put(Operator.NUMBER_INT, new NumberParslet(true));
		prefixParslets.put(Operator.NUMBER_DOUBLE, new NumberParslet(false));
		prefixParslets.put(Operator.BRACKET_LEFT, new GroupParslet());
		prefixParslets.put(Operator.SUB, new UnaryParselet());
		prefixParslets.put(Operator.ADD, new UnaryParselet());
		prefixParslets.put(Operator.NOT, new UnaryParselet());
		prefixParslets.put(Operator.NEG, new UnaryParselet());
		prefixParslets.put(Operator.STRING, new StringParslet());
		prefixParslets.put(Operator.CHAR, new CharParslet());
		prefixParslets.put(Operator.IF, new IfParslet());
		prefixParslets.put(Operator.BRACKET_CURLY_LEFT, new BodyParslet());
		prefixParslets.put(Operator.RETURN, new ReturnParslet());
		prefixParslets.put(Operator.FUNCTION, new FunctionParslet());
		prefixParslets.put(Operator.FUNC, new FunctionParslet());
		prefixParslets.put(Operator.WHILE, new WhileParslet());
		prefixParslets.put(Operator.RETURN_IF, new ReturnIfParslet());
		prefixParslets.put(Operator.FOR, new ForParslet());
		prefixParslets.put(Operator.CONTINUE, new ContinueParslet());
		prefixParslets.put(Operator.BREAK, new BreakParslet());
		prefixParslets.put(Operator.CLASS, new ClassParslet());
		prefixParslets.put(Operator.OVERLOAD, new OverloadParslet());
		prefixParslets.put(Operator.BRACKET_SQUARE_LEFT, new AttributeParslet());

		prefixParslets.put(Operator.SINGLE_LINE_COMMENT, new SingleLineCommentParslet());
		prefixParslets.put(Operator.MULTI_LINE_COMMENT_BEGIN, new MultiLineCommentParslet());



		infixParslets.put(Operator.TERNARY, new TernaryParslet());
		infixParslets.put(Operator.INSTANCEOF, new InstanceofParslet());
		infixParslets.put(Operator.NAME, new ChainedNameParslet());
		infixParslets.put(Operator.ASSIGN, new AssignParslet());
		infixParslets.put(Operator.DOT, new DotParslet());

		infixParslets.put(Operator.ADD, new BinaryParslet(Precedence.ADD_SUB));
		infixParslets.put(Operator.SUB, new BinaryParslet(Precedence.ADD_SUB));
		infixParslets.put(Operator.MUL, new BinaryParslet(Precedence.MUL_DIV_MOD));
		infixParslets.put(Operator.DIV, new BinaryParslet(Precedence.MUL_DIV_MOD));
		infixParslets.put(Operator.MOD, new BinaryParslet(Precedence.MUL_DIV_MOD));
		infixParslets.put(Operator.BIT_OR, new BinaryParslet(Precedence.BIT_OR));
		infixParslets.put(Operator.BIT_XOR, new BinaryParslet(Precedence.BIT_XOR));
		infixParslets.put(Operator.BIT_AND, new BinaryParslet(Precedence.BIT_AND));
		infixParslets.put(Operator.LSH, new BinaryParslet(Precedence.BIT_SHIFT));
		infixParslets.put(Operator.RSH, new BinaryParslet(Precedence.BIT_SHIFT));
		infixParslets.put(Operator.EQUAL, new BinaryParslet(Precedence.EQUALITY));
		infixParslets.put(Operator.NOT_EQUAL, new BinaryParslet(Precedence.EQUALITY));
		infixParslets.put(Operator.LESS_THAN_EQUAL, new BinaryParslet(Precedence.RELATION));
		infixParslets.put(Operator.GREATER_THAN_EQUAL, new BinaryParslet(Precedence.RELATION));
		infixParslets.put(Operator.LESS_THAN, new BinaryParslet(Precedence.RELATION));
		infixParslets.put(Operator.GREATER_THAN, new BinaryParslet(Precedence.RELATION));
		infixParslets.put(Operator.AND, new BinaryParslet(Precedence.AND));
		infixParslets.put(Operator.OR, new BinaryParslet(Precedence.OR));

		infixParslets.put(Operator.ASSIGN_ADD, new AssignParslet());
		infixParslets.put(Operator.ASSIGN_SUB, new AssignParslet());
		infixParslets.put(Operator.ASSIGN_MUL, new AssignParslet());
		infixParslets.put(Operator.ASSIGN_DIV, new AssignParslet());
		infixParslets.put(Operator.ASSIGN_MOD, new AssignParslet());
		infixParslets.put(Operator.ASSIGN_BIT_OR, new AssignParslet());
		infixParslets.put(Operator.ASSIGN_BIT_XOR, new AssignParslet());
		infixParslets.put(Operator.ASSIGN_BIT_AND, new AssignParslet());

		infixParslets.put(Operator.INDEX, new IndexParslet());
		infixParslets.put(Operator.BRACKET_LEFT, new FunctionCallInfix());
		infixParslets.put(Operator.BRACKET_SQUARE_LEFT, new IndexOperatorParslet());



		ignoredExpressions.add(Comment.class);
		ignoredExpressions.add(Empty.class);
	}


	private static String tree()
	{
		if (depth <= 0)
			return "#";
		return "    ".repeat(depth - 1);
	}

	public static void print(String s)
	{
		if (ScriptItSettings.PARSER_DEBUG)
			System.out.println(tree() + s);
	}

	private final Stack<List<Expression>> functionParameters = new Stack<>(64);
	public Tokenizer tokenizer;
	public Set<String> importedTypes;
	public Workspace workspace;
	public Script script;
	public Stack<ClassDeclaration> classStack;
	public Attributes lastAttributes;

	public TokenParser setExpression(Script script, String expression)
	{
		this.script = script;
		this.workspace = script.getWorkspace();
		functionParameters.clear();
		tokenizer = new Tokenizer(expression);
		ScriptItSettings.TOKENIZER_DEBUG = ScriptItSettings.PARSER_DEBUG;
		importedTypes = new HashSet<>();
		classStack = new Stack<>(16);

		return this;
	}

	public Expression[] parseArguments(boolean matchBracket)
	{
		List<Expression> args = new ArrayList<>();
		if (!matchBracket || tokenizer.matchToken(Operator.BRACKET_LEFT, true))
		{
			if (!tokenizer.matchToken(Operator.BRACKET_RIGHT, true))
			{
				do
				{
					args.add(parse(Precedence.ANYTHING));
				} while (tokenizer.matchToken(Operator.COMMA, true));
				tokenizer.consumeToken(Operator.BRACKET_RIGHT);
			}
		}
		return args.toArray(Expression[]::new);
	}

	public Expression parse(Precedence precedence)
	{
		depth++;
		Tokenizer.Token token = tokenizer.consumeToken(null);

		if (token.type() == Operator.EOF)
			return null;

		PrefixParselet parslet = prefixParslets.get(token.type());
		if (parslet == null)
		{
			StringBuilder sb = new StringBuilder();
			for (int i = Math.max(0, tokenizer.getCurrentTokenIndex() - 10); i < Math.min(tokenizer.getCurrentTokenIndex() + 10, tokenizer.getTokens().size() - 1); i++)
			{
				sb.append(tokenizer.getTokens().get(i).sval());
			}

			throw new RuntimeException("Parslet not found for '" + token.sval() + "' around ... " + sb + " ...");
		}
		print(Log.BRIGHT_GREEN + "---Parslet---");
		print("Prefix: " + Log.RESET + parslet.getClass().getSimpleName());
		Expression expression = parslet.parse(this, token);
		print(Log.BRIGHT_GREEN + "Parslet " + Log.BRIGHT_YELLOW + "Result: " + Log.RESET + expression);

		if (ignoredExpressions.contains(expression.getClass()))
		{
			depth--;
			return parse(precedence);
		}

		Expression expression1 = parseInfixExpression(expression, precedence);
		depth--;
		return expression1;
	}

	public Expression parseInfixExpression(Expression left, Precedence precedence)
	{
		Tokenizer.Token token;

		print(Log.BRIGHT_RED + "Infix Start (" + precedence.getPredecence() + " < " + getPrecedence().getPredecence() + Log.WHITE + " => " + Log.YELLOW + (precedence.getPredecence() < getPrecedence().getPredecence()) + Log.BRIGHT_RED + ")" + Log.RESET);
		depth++;
		while (precedence.getPredecence() < getPrecedence().getPredecence())
		{
			token = tokenizer.consumeToken(null);
			InfixParslet infixParselet = infixParslets.get(token.type());
			print(Log.BRIGHT_MAGENTA + "---Infix--- (Precedence: " + precedence.getPredecence() + ")");
			print("Infix: " + Log.RESET + infixParselet.getClass().getSimpleName());
			left = infixParselet.parse(this, token, left);
			print(Log.BRIGHT_MAGENTA + "Infix " + Log.BRIGHT_YELLOW + "Result: " + Log.RESET + left);
		}
		depth--;
		print(Log.BRIGHT_RED + "Infix End, " + Log.BRIGHT_YELLOW + "Result: " + left + Log.RESET);
		return left;
	}

	private Precedence getPrecedence()
	{
		Tokenizer.Token token = tokenizer.peekToken();

		if (token != null)
		{
			InfixParslet parselet = infixParslets.get(token.type());

			if (parselet != null)
			{
				return parselet.getPrecedence();
			}
		}

		return Precedence.ANYTHING;
	}

	public List<Expression> parseAll()
	{
		depth = 0;
		List<Expression> expressions = new ArrayList<>();
		boolean lastIgnored = false;
		do
		{
			depth++;
			print(Log.BRIGHT_CYAN + "---Next Expression---" + Log.RESET);
			Expression next = parse(Precedence.ANYTHING);

			if (next != null)
			{
				if (!ignoredExpressions.contains(next.getClass()))
				{
					addExpression(next, expressions);
				} else {
					lastIgnored = true;
				}
			} else
			{
				if (ScriptItSettings.PARSER_DEBUG)
					System.out.println(Log.BRIGHT_RED + "\n" +
						"-------------------------------\n" +
						"----------END OF FILE----------\n" +
						"-------------------------------\n" + Log.RESET);
				break;
			}
			if (ScriptItSettings.PARSER_DEBUG)
				System.out.println("\n");
			depth--;
		} while (tokenizer.matchToken(Operator.SEMICOLON, true) || lastIgnored);
		return expressions;
	}

	public List<Expression> parseClass()
	{
		List<Expression> expressions = new ArrayList<>();
		boolean lastIgnored = false;
		do
		{
			depth++;
			print(Log.BRIGHT_CYAN + "---Next Expression---" + Log.RESET);
			if (tokenizer.matchToken(Operator.BRACKET_CURLY_RIGHT, true))
			{
				depth--;
				break;
			}
			Expression next = parse(Precedence.ANYTHING);
			if (next != null)
			{
				if (!ignoredExpressions.contains(next.getClass()))
				{
					addExpression(next, expressions);
				} else {
					lastIgnored = true;
				}
			} else
			{
				if (ScriptItSettings.PARSER_DEBUG)
					System.out.println(Log.BRIGHT_RED + "\n" +
						"-------------------------------\n" +
						"----------END OF FILE----------\n" +
						"-------------------------------\n" + Log.RESET);
				break;
			}
			if (ScriptItSettings.PARSER_DEBUG)
				System.out.println("\n");
			depth--;
		} while (tokenizer.matchToken(Operator.SEMICOLON, true) || lastIgnored);

		return expressions;
	}

	protected void addExpression(Expression next, List<Expression> expressions)
	{
		if (lastAttributes != null)
		{
			expressions.add(next);
			expressions.add(lastAttributes);
			lastAttributes.setNextExpr(workspace, next);
			lastAttributes = null;
		} else
		{
			if (next instanceof Attributes atr)
			{
				lastAttributes = atr;
			} else
			{
				expressions.add(next);
			}
		}
	}
}
