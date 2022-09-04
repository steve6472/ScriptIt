package steve6472.scriptit.newtokenizer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by steve6472
 * Date: 9/3/2022
 * Project: ScriptIt
 */
public class TokenStorage
{
	private final Set<IToken> tokenList;

	public TokenStorage()
	{
		tokenList = new HashSet<>();
	}

	public void addTokens(IToken[] operators)
	{
		Collections.addAll(tokenList, operators);
	}

	public void addTokens(Class<? extends IToken> tokens)
	{
		if (!tokens.isEnum())
		{
			throw new RuntimeException(tokens.getSimpleName() + " is not an enum! Use addTokens(IToken[]) instead!");
		}

		Collections.addAll(tokenList, tokens.getEnumConstants());
	}

	public IToken fromSymbol(String symbol)
	{
		return tokenList
			.stream()
			.filter(s -> s.getSymbol().equals(symbol))
			.findFirst()
			.orElse(null);
	}

	public Set<IToken> getTokenList()
	{
		return tokenList;
	}
}
