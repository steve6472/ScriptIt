package steve6472.scriptit.newtokenizer;

import steve6472.scriptit.Log;
import steve6472.scriptit.ScriptItSettings;
import steve6472.scriptit.tokenizer.MyStreamTokenizer;
import steve6472.scriptit.tokenizer.TokenParser;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Pattern;

/**********************
 * Created by steve6472
 * Date: 9/3/2022
 * Project: ScriptIt
 *
 ***********************/
public class Tokenizer
{
	protected final List<Token> tokens;
	protected final Set<Integer> newLines;
	protected int currentToken = -1;

	public int maxMergeDistance = 3;
	private final List<Token> mergeStack;

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

	public Tokenizer(TokenStorage tokenStorage, String string)
	{
		tokens = new ArrayList<>();
		newLines = new HashSet<>();
		mergeStack = new ArrayList<>();

		MyStreamTokenizer tokenizer = createTokenizer(string);
		tokenize(tokenizer, tokenStorage);
	}

	protected MyStreamTokenizer createTokenizer(String string)
	{
		StringReader r = new StringReader(string);
		MyStreamTokenizer tokenizer = new MyStreamTokenizer(r);
		tokenizer.ordinaryChar('.');
		tokenizer.wordChars('_', '_');
		tokenizer.eolIsSignificant(true);

		return tokenizer;
	}

	protected void tokenize(MyStreamTokenizer tokenizer, TokenStorage tokenStorage)
	{
		int lastLine = 1;
		int lastColumn = 1;

		w: while (true)
		{
			int token;
			String sval;

			int line = tokenizer.lineno();
			int column = -1;

			if (!mergeStack.isEmpty())
			{
				Token remove = mergeStack.remove(0);
				token = remove._token();
				sval = remove.sval();
				line = remove.line;
				lastColumn = remove.column;
			}
			else
			{
				try
				{
					token = tokenizer.nextToken();
					sval = tokenizer.sval;
				} catch (IOException e)
				{
					throw new RuntimeException(e);
				}
			}

			if (lastLine != line)
			{
				lastLine = line;
				lastColumn = 1;
				column = 1;
			} else
			{
				lastColumn++;
				column = lastColumn;
			}

			switch (token)
			{
				case StreamTokenizer.TT_EOF ->
				{
					break w;
				}
				case StreamTokenizer.TT_NUMBER -> tokens.add(new Token(isInteger(sval) ? MainTokens.NUMBER_INT : MainTokens.NUMBER_DOUBLE, sval, line, column));
				case StreamTokenizer.TT_WORD -> tokens.add(new Token(Objects.requireNonNullElse(tokenStorage.fromSymbol(sval), MainTokens.NAME), sval, line, column));
				case '"' -> tokens.add(new Token(MainTokens.STRING, sval, line, column)); // String
				case '\'' -> tokens.add(new Token(MainTokens.CHAR, sval, line, column)); // Char
				case '\n' -> newLines.add(tokens.size()); // New line
				default ->
				{
					if (sval == null)
					{
						IToken merged = mergeTokens(tokenizer, tokenStorage);

						if (merged == null)
							merged = tokenStorage.fromSymbol("" + (char) token);
						else
							mergeStack.clear();

						if (merged == null)
							throw new RuntimeException("Unknown symbol " + token + " '" + (char) token + ", line: " + line + ", column: " + column);
						else
							tokens.add(new Token(merged, merged.getSymbol(), line, column));
					} else
					{
						IToken type = tokenStorage.fromSymbol(sval);

						if (type == null)
							throw new RuntimeException("Unknown symbol " + token + " '" + (char) token + "', sval: " + sval + ", line: " + line + ", column: " + column);
						else
							tokens.add(new Token(type, sval, line, column));
					}
				}
			}
		}

		tokens.add(new Token(MainTokens.EOF, "", tokenizer.lineno(), 0));
	}

	protected IToken mergeTokens(MyStreamTokenizer tokenizer, TokenStorage tokenStorage)
	{
		if (maxMergeDistance <= 1)
			return null;

		StringBuilder symbol = new StringBuilder("" + (char) tokenizer.ttype);

		for (int i = 0; i < maxMergeDistance - 1; i++)
		{
			try
			{
				symbol.append((char) tokenizer.nextToken());
				mergeStack.add(new Token(null, tokenizer.sval, tokenizer.lineno(), -1, tokenizer.ttype));
			} catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		}

		for (int i = 0; i < maxMergeDistance - 1; i++)
		{
			IToken iToken = tokenStorage.fromSymbol(symbol.toString());
			if (iToken != null)
				return iToken;
			else
				symbol.setLength(symbol.length() - 1);
		}

		return null;
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

	public boolean matchToken(IToken expectedtype, boolean consume)
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

	public Token consumeToken(IToken expectedType)
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

	public record Token(IToken type, String sval, int line, int column, int _token)
	{
		public Token(IToken type, String sval, int line, int column)
		{
			this(type, sval, line, column, 0);
		}

		@Override
		public String toString()
		{
			return "Token{" + "type=" + type + ", sval='" + sval + '\'' + '}';
		}

		public String string()
		{
			return sval == null ? "" + (char) _token : sval;
		}
	}
}
