package steve6472.scriptit.newtokenizer;

/**
 * Created by steve6472
 * Date: 9/3/2022
 * Project: ScriptIt
 */
public enum MainTokens implements IToken
{
	NUMBER_INT,
	NUMBER_DOUBLE,
	NAME,
	STRING,
	CHAR,
	EOL,
	EOF;

	@Override
	public String getSymbol()
	{
		return "";
	}

	@Override
	public boolean isMerge()
	{
		return false;
	}
}
