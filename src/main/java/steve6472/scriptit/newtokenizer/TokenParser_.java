package steve6472.scriptit.newtokenizer;

import steve6472.scriptit.Log;
import steve6472.scriptit.ScriptItSettings;
import steve6472.scriptit.Stack;
import steve6472.scriptit.expressions.Attributes;
import steve6472.scriptit.expressions.ClassDeclaration;
import steve6472.scriptit.expressions.Expression;

import java.util.*;

/**********************
 * Created by steve6472
 * Date: 9/3/2022
 * Project: ScriptIt
 * <p>
 * <a href="https://en.cppreference.com/w/cpp/language/operator_precedence">...</a>
 *
 ***********************/
public class TokenParser_<R, D>
{
	public final Map<IToken, PrefixParselet<R, D>> prefixParslets = new HashMap<>();
	public final Map<IToken, InfixParslet<R, D>> infixParslets = new HashMap<>();

	public final Set<Class<?>> ignoredExpressions = new HashSet<>();

	private final Stack<List<Expression>> functionParameters = new Stack<>(64);
	public Tokenizer tokenizer;
	public Set<String> importedTypes;
	public D data;
	public Stack<ClassDeclaration> classStack;
	public Attributes lastAttributes;
	public TokenStorage tokenStorage;

	public TokenParser_(TokenStorage tokenStorage)
	{
		this.tokenStorage = tokenStorage;
	}

	public void prefixParslet(IToken token, PrefixParselet<R, D> parselet)
	{
		prefixParslets.put(token, parselet);
	}

	public void infixParslet(IToken token, InfixParslet<R, D> parselet)
	{
		infixParslets.put(token, parselet);
	}

	public TokenParser_<R, D> setExpression(D data, String expression)
	{
		this.data = data;
		functionParameters.clear();
		tokenizer = new Tokenizer(tokenStorage, expression);
		importedTypes = new HashSet<>();
		classStack = new Stack<>(16);

		return this;
	}

	public List<R> parseArguments(boolean matchBracket)
	{
		List<R> args = new ArrayList<>();
		if (!matchBracket || tokenizer.matchToken(Tokens.BRACKET_LEFT, true))
		{
			if (!tokenizer.matchToken(Tokens.BRACKET_RIGHT, true))
			{
				do
				{
					args.add(parse(Precedence.ANYTHING));
				} while (tokenizer.matchToken(Tokens.COMMA, true));
				tokenizer.consumeToken(Tokens.BRACKET_RIGHT);
			}
		}

		return args;
	}

	public R parse(IPrecedence precedence)
	{
		depth++;
		Tokenizer.Token token = tokenizer.consumeToken(null);

		if (token.type() == MainTokens.EOF)
			return null;

		PrefixParselet<R, D> parslet = prefixParslets.get(token.type());
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
		R result = parslet.parse(tokenizer, null, data);
		print(Log.BRIGHT_GREEN + "Parslet " + Log.BRIGHT_YELLOW + "Result: " + Log.RESET + result);

		if (ignoredExpressions.contains(result.getClass()))
		{
			depth--;
			return parse(precedence);
		}

		R finalResult = parseInfixExpression(result, precedence);
		depth--;
		return finalResult;
	}

	public R parseInfixExpression(R left, IPrecedence precedence)
	{
		Tokenizer.Token token;

		print(Log.BRIGHT_RED + "Infix Start (" + precedence.ordinal() + " < " + getPrecedence().ordinal() + Log.WHITE + " => " + Log.YELLOW + (precedence.ordinal() < getPrecedence().ordinal()) + Log.BRIGHT_RED + ")" + Log.RESET);
		depth++;
		while (precedence.ordinal() < getPrecedence().ordinal())
		{
			token = tokenizer.consumeToken(null);
			InfixParslet<R, D> infixParselet = infixParslets.get(token.type());
			print(Log.BRIGHT_MAGENTA + "---Infix--- (Precedence: " + precedence.ordinal() + ")");
			print("Infix: " + Log.RESET + infixParselet.getClass().getSimpleName());
			left = infixParselet.parse(tokenizer, left, data);
			print(Log.BRIGHT_MAGENTA + "Infix " + Log.BRIGHT_YELLOW + "Result: " + Log.RESET + left);
		}
		depth--;
		print(Log.BRIGHT_RED + "Infix End, " + Log.BRIGHT_YELLOW + "Result: " + left + Log.RESET);
		return left;
	}

	private IPrecedence getPrecedence()
	{
		Tokenizer.Token token = tokenizer.peekToken();

		if (token != null)
		{
			InfixParslet<R, D> parselet = infixParslets.get(token.type());

			if (parselet != null)
			{
				return parselet.getPrecedence();
			}
		}

		return Precedence.ANYTHING;
	}

	public List<R> parseAll()
	{
		depth = 0;
		List<R> expressions = new ArrayList<>();
		boolean lastIgnored = false;
		do
		{
			depth++;
			print(Log.BRIGHT_CYAN + "---Next Expression---" + Log.RESET);
			R next = parse(Precedence.ANYTHING);

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
		} while (tokenizer.matchToken(Tokens.SEMICOLON, true) || lastIgnored);
		return expressions;
	}

	public List<R> parseClass()
	{
		List<R> expressions = new ArrayList<>();
		boolean lastIgnored = false;
		do
		{
			depth++;
			print(Log.BRIGHT_CYAN + "---Next Expression---" + Log.RESET);
			if (tokenizer.matchToken(Tokens.BRACKET_CURLY_RIGHT, true))
			{
				depth--;
				break;
			}
			R next = parse(Precedence.ANYTHING);
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
		} while (tokenizer.matchToken(Tokens.SEMICOLON, true) || lastIgnored);

		return expressions;
	}

	protected void addExpression(R next, List<R> expressions)
	{
//		if (lastAttributes != null)
//		{
//			expressions.add(next);
//			expressions.add(lastAttributes);
//			lastAttributes.setNextExpr(workspace, next);
//			lastAttributes = null;
//		} else
//		{
//			if (next instanceof Attributes atr)
//			{
//				lastAttributes = atr;
//			} else
//			{
				expressions.add(next);
//			}
//		}
	}

	/*
	 * Debug
	 */

	public static int depth = 0;

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
}
