package steve6472.scriptit.newtokenizer;

/**
 * Created by steve6472
 * Date: 9/2/2022
 * Project: ScriptIt
 */
public interface IToken
{
	String getSymbol();

	boolean isMerge();

	int ordinal();
}
