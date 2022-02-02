package steve6472.scriptit.simple;

import steve6472.scriptit.Expression;
import steve6472.scriptit.Highlighter;
import steve6472.scriptit.Result;
import steve6472.scriptit.Script;

/**********************
 * Created by steve6472
 * On date: 1/23/2022
 * Project: ScriptIt
 *
 ***********************/
public class Continue extends Expression
{
	public static final Continue INSTANCE = new Continue();

	private Continue()
	{

	}

	public static Continue getInstance()
	{
		return INSTANCE;
	}

	@Override
	public Result apply(Script script)
	{
		return Result.continueLoop();
	}

	@Override
	public String showCode(int a)
	{
		return Highlighter.RETURN + "continue" + Highlighter.RESET;
	}
}
