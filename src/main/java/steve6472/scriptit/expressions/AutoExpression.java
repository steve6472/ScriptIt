package steve6472.scriptit.expressions;

import steve6472.scriptit.Result;
import steve6472.scriptit.Script;

/**
 * Created by steve6472
 * Date: 8/25/2022
 * Project: ScriptIt
 */
public class AutoExpression extends Expression
{
	@Override
	public Result apply(Script script)
	{
		return Result.pass();
	}

	@Override
	public String showCode(int a)
	{
		return "auto";
	}
}
