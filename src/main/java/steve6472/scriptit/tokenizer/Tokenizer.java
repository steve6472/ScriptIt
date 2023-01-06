package steve6472.scriptit.tokenizer;

import steve6472.scriptit.Log;
import steve6472.scriptit.ScriptItSettings;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Pattern;

/**********************
 * Created by steve6472
 * On date: 12/18/2021
 * Project: ScriptIt
 *
 ***********************/
public class Tokenizer
{
	private final List<Token> tokens;
	private final Set<Integer> newLines;
	private int currentToken = -1;

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
		newLines = new HashSet<>();

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
				IOperator type = IOperator.fromSymbol(token.sval);
				tokens.add(new Token(Objects.requireNonNullElse(type, Operator.NAME), token.sval));
			} else if (token.token == '"') // String
			{
				tokens.add(new Token(Operator.STRING, token.sval));
			} else if (token.token == '\'') // Char
			{
				tokens.add(new Token(Operator.CHAR, token.sval));
			} else if (token.token == '\n')
			{
				newLines.add(tokens.size());
			}
			else
			{
				IOperator type = IOperator.fromSymbol(token.sval);
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

	public boolean isNextTokenNewLine()
	{
		return newLines.contains(currentToken + 1);
	}

	public Token nextToken()
	{
		currentToken++;
		if (ScriptItSettings.TOKENIZER_DEBUG)
			TokenParser.print(Log.WHITE + currentToken + Log.RESET + " Next token: " + getCurrentToken()
				+ (getCurrentToken() != null ? (" '" + Log.WHITE + getCurrentToken().type.getSymbol() + Log.RESET + "'") : ""));
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

	public int getCurrentTokenIndex()
	{
		return currentToken;
	}

	public List<Token> getTokens()
	{
		return tokens;
	}

	public Token peekToken(int peek)
	{
		if (ScriptItSettings.TOKENIZER_DEBUG)
			TokenParser.print("Peeking " + peek + (currentToken + peek < tokens.size() ? " (" + tokens.get(currentToken + peek) + ")" : ""));
		if (currentToken + peek >= tokens.size())
			return null;
		return tokens.get(currentToken + peek);
	}

	public Token peekToken()
	{
		return peekToken(1);
	}

	public boolean matchToken(IOperator expectedtype, boolean consume)
	{
		if (ScriptItSettings.TOKENIZER_DEBUG)
			TokenParser.print("Matching " + expectedtype + (expectedtype != null ? (" '" + Log.WHITE + expectedtype.getSymbol() + Log.RESET + "'") : ""));
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

	public Token consumeToken(IOperator expectedType)
	{
		if (ScriptItSettings.TOKENIZER_DEBUG)
			TokenParser.print("Consuming token " + expectedType);
		nextToken();
		Token token = getCurrentToken();
		if (expectedType == null)
			return token;
		if (expectedType != token.type)
		{
			StringBuilder sb = new StringBuilder();
			for (int i = Math.max(0, currentToken - 10); i < Math.min(currentToken + 10, tokens.size() - 1); i++)
			{
				sb.append(tokens.get(i).sval());
			}

			throw new RuntimeException("Expected token type '" + expectedType + "' got '" + token.type + "' around ... " + sb + " ...");
		}
		return null;
	}

	public void skip(int skip)
	{
		currentToken += skip;
	}

	public record Token(IOperator type, String sval)
	{
		@Override
		public String toString()
		{
			return "Token{" + "type=" + type + ", sval='" + sval + '\'' + '}';
		}
	}

	private static final Set<IOperator> MERGABLE_OPERATORS = new HashSet<>();

	public static void updateMergableOperators()
	{
		MERGABLE_OPERATORS.clear();

		for (IOperator value : IOperator.getAllOperators())
		{
			if (value.isMerge())
			{
				MERGABLE_OPERATORS.add(value);
			}
		}
	}

	public static boolean MINI_TOKENIZER_DEBUG = false;

	private static class MiniTokenizer
	{
		List<MiniToken> tokens;
		private int index = -1;

		MiniTokenizer(String string)
		{
			StringReader r = new StringReader(string);
			MyStreamTokenizer tokenizer = new MyStreamTokenizer(r);
			tokenizer.ordinaryChar('.');
			tokenizer.wordChars('_', '_');
			tokenizer.eolIsSignificant(true);

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
						e = new MiniToken(token, tokenizer.sval);
					}
					tokens.add(e);

				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}

			if (tokens.size() < 1)
				return;

			mergeTokens();

			if (MINI_TOKENIZER_DEBUG)
			{
				System.out.println("\n\n-----------------------------");
				System.out.println("--------Mini Tokenizer-------");
				System.out.println("-----------------------------");

				tokens.forEach(System.out::println);

				System.out.println("-----------------------------");
				System.out.println("--------Mini Tokenizer-------");
				System.out.println("-----------------------------\n\n");
			}
		}

		private void mergeTokens()
		{
			List<MiniToken> newTokens = new ArrayList<>();
			m: for (int i = 0; i < tokens.size() - 1; i++)
			{
				MiniToken token = tokens.get(i);
				MiniToken peek = tokens.get(i + 1);

				for (IOperator mergableOperator : MERGABLE_OPERATORS)
				{
					if (token.sval != null && peek.sval != null && !token.sval.isBlank() && !peek.sval.isBlank())
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
