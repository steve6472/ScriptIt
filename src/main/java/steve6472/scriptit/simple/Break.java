package steve6472.scriptit.simple;

import steve6472.scriptit.expressions.Expression;
import steve6472.scriptit.Highlighter;
import steve6472.scriptit.Result;
import steve6472.scriptit.Script;

/**********************
 * Created by steve6472
 * On date: 1/23/2022
 * Project: ScriptIt
 *
 ***********************/
public class Break extends Expression
{
	public static final Break INSTANCE = new Break();

	private Break()
	{

	}

	public static Break getInstance()
	{
		return INSTANCE;
	}

	@Override
	public Result apply(Script script)
	{
		return Result.breakLoop();
	}

	@Override
	public String showCode(int a)
	{
		return Highlighter.RETURN + "break" + Highlighter.RESET;
	}
}
