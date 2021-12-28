package steve6472.scriptit;

import steve6472.scriptit.tokenizer.MyStreamTokenizer;
import steve6472.scriptit.tokenizer.Operator;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Pattern;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 12/18/2021
 * Project: ScriptIt
 *
 ***********************/
public class Tokenizer
{
	private final List<Token> tokens;
	private int currentToken = -1;
	public boolean debug = false;

	private static final Pattern IS_DECIMAL = Pattern.compile("([+-]?\\d*(\\.\\d+)?)+");
	private static final Pattern IS_INTEGER = Pattern.compile("([+-]?\\d)+");

	public static boolean isInteger(String text)
	{
		return IS_INTEGER.matcher(text).matches();
	}

	public static boolean isDecimal(String text)
	{
		return IS_DECIMAL.matcher(text).matches();
	}

	public Tokenizer(String string)
	{
		MiniTokenizer miniTokenizer = new MiniTokenizer(string);

		tokens = new ArrayList<>();

		while (miniTokenizer.hasMoreTokens())
		{
			MiniTokenizer.MiniToken token = miniTokenizer.nextToken();

			if (token.token == StreamTokenizer.TT_NUMBER)
			{
				if (isInteger(token.sval))
					tokens.add(new Token(Operator.NUMBER_INT, token.sval));
				else
					tokens.add(new Token(Operator.NUMBER_DOUBLE, token.sval));
			} else if (token.token == StreamTokenizer.TT_WORD)
			{
				Operator type = Operator.fromSymbol(token.sval);
				tokens.add(new Token(Objects.requireNonNullElse(type, Operator.NAME), token.sval));
			} else if (token.token == '"') // String
			{
				tokens.add(new Token(Operator.STRING, token.sval));
			} else if (token.token == '\'') // Char
			{
				tokens.add(new Token(Operator.CHAR, token.sval));
			} else
			{
				Operator type = Operator.fromSymbol(token.sval);
//				System.out.println(token.sval + " -> " + type);
				if (type == null)
					throw new RuntimeException("Unknown symbol '" + token + "'");
				else
					tokens.add(new Token(type, token.sval));
			}
		}

		tokens.add(new Token(Operator.EOF, ""));

//		tokens.forEach(System.out::println);
//		System.out.println("\n");
	}

	public Token nextToken()
	{
		currentToken++;
		if (debug)
			TokenParser.print(Log.WHITE + currentToken + Log.RESET + " Next token: " + getCurrentToken());
		return getCurrentToken();
	}

	public boolean hasMoreTokens()
	{
		return currentToken < tokens.size() - 1;
	}

	public Token getCurrentToken()
	{
		return tokens.get(currentToken);
	}

	public Token peekToken(int peek)
	{
		if (debug)
			TokenParser.print("Peeking " + peek);
		if (currentToken + peek >= tokens.size())
			return null;
		return tokens.get(currentToken + peek);
	}

	public Token peekToken()
	{
		return peekToken(1);
	}

	public boolean matchToken(Operator expectedtype, boolean consume)
	{
		if (debug)
			TokenParser.print("Matching " + expectedtype);
		Token token = peekToken();
		if (token == null || token.type != expectedtype)
		{
			return false;
		} else
		{
			if (consume)
				consumeToken(expectedtype);
			return true;
		}
	}

	public Token consumeToken(Operator expectedType)
	{
		if (debug)
			TokenParser.print("Consuming token " + expectedType);
		nextToken();
		Token token = getCurrentToken();
		if (expectedType == null)
			return token;
		if (expectedType != token.type)
			throw new RuntimeException("Expected token type '" + expectedType + "' got '" + token.type + "'");
		return null;
	}

	public void skip(int skip)
	{
		currentToken += skip;
	}

	public record Token(Operator type, String sval)
	{
		@Override
		public String toString()
		{
			return "Token{" + "type=" + type + ", sval='" + sval + '\'' + '}';
		}
	}

	private static final Set<Operator> MERGABLE_OPERATORS;

	static
	{
		MERGABLE_OPERATORS = new HashSet<>();

		for (Operator value : Operator.getValues())
		{
			if (value.isMerge())
			{
				MERGABLE_OPERATORS.add(value);
			}
		}
	}

	private static class MiniTokenizer
	{
		List<MiniToken> tokens;
		private int index = -1;

		MiniTokenizer(String string)
		{
			string = string.replace('/', 'ˇ');
			StringReader r = new StringReader(string);
			MyStreamTokenizer tokenizer = new MyStreamTokenizer(r);
			tokenizer.ordinaryChar('.');
			tokenizer.commentChar('§');
			tokenizer.slashSlashComments(false);
			tokenizer.slashStarComments(false);

			tokens = new ArrayList<>();

			while (true)
			{
				try
				{
					int token = tokenizer.nextToken();
					if (token == StreamTokenizer.TT_EOF)
						break;

					MiniToken e;
					if (token == '"' || token == '\'')
					{
						e = new MiniToken(token, tokenizer.sval);
					}
					else if (token != StreamTokenizer.TT_WORD && token != StreamTokenizer.TT_NUMBER)
					{
						e = new MiniToken(token, "" + (char) token);
					} else
					{
						e = new MiniToken(token, tokenizer.sval.replace('ˇ', '/'));
					}
					tokens.add(e);

				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}

			mergeTokens();

//			System.out.println("\n\n-----------------------------");
//			System.out.println("--------Mini Tokenizer-------");
//			System.out.println("-----------------------------");
//
//			tokens.forEach(System.out::println);
//
//			System.out.println("-----------------------------");
//			System.out.println("--------Mini Tokenizer-------");
//			System.out.println("-----------------------------\n\n");
		}

		private void mergeTokens()
		{
			List<MiniToken> newTokens = new ArrayList<>();
			m: for (int i = 0; i < tokens.size() - 1; i++)
			{
				MiniToken token = tokens.get(i);
				MiniToken peek = tokens.get(i + 1);

				for (Operator mergableOperator : MERGABLE_OPERATORS)
				{
					if (token.sval != null && peek.sval != null)
					{
						if (mergableOperator.getSymbol().charAt(0) == token.sval().charAt(0) && mergableOperator.getSymbol().charAt(1) == peek.sval.charAt(0))
						{
							newTokens.add(new MiniToken(-mergableOperator.ordinal(), mergableOperator.getSymbol()));
							i++;
							continue m;
						}
					}
				}

				newTokens.add(token);
			}

			newTokens.add(tokens.get(tokens.size() - 1));

			this.tokens = newTokens;
		}

		public MiniToken nextToken()
		{
			index++;
			return tokens.get(index);
		}

		public int getIndex()
		{
			return index;
		}

		public MiniToken getToken(int index)
		{
			return tokens.get(index);
		}

		public boolean hasMoreTokens()
		{
			return index < tokens.size() - 1;
		}

		private record MiniToken(int token, String sval)
		{
			@Override
			public String toString()
			{
				return "MiniToken{" + "token=" + token + ", sval='" + sval + '\'' + '}';
			}
		}
	}
}
