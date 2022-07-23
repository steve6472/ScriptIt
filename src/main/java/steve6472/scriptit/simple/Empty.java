package steve6472.scriptit.simple;

import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.expressions.Expression;

/**********************
 * Created by steve6472
 * On date: 1/23/2022
 * Project: ScriptIt
 *
 ***********************/
public class Empty extends Expression
{
	public static final Empty INSTANCE = new Empty();

	private Empty()
	{

	}

	public static Empty getInstance()
	{
		return INSTANCE;
	}

	@Override
	public Result apply(Script script)
	{
		return Result.pass();
	}

	@Override
	public String showCode(int a)
	{
		return "";
	}
}
