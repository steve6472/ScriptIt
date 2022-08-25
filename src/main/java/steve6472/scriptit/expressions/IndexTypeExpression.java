package steve6472.scriptit.expressions;

import steve6472.scriptit.Result;
import steve6472.scriptit.Script;

/**
 * Created by steve6472
 * Date: 8/22/2022
 * Project: ScriptIt
 */
public class IndexTypeExpression extends Expression
{
	public Variable left;

	public IndexTypeExpression(Variable left)
	{
		this.left = left;
	}

	@Override
	public Result apply(Script script)
	{
		return Result.pass();
	}

	@Override
	public String showCode(int a)
	{
		return left.showCode(a) + "[]";
	}

	@Override
	public String toString()
	{
		return "IndexTypeExpression{" + "left=" + left + '}';
	}
}
