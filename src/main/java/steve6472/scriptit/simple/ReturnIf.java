package steve6472.scriptit.simple;

import steve6472.scriptit.*;
import steve6472.scriptit.expressions.Expression;
import steve6472.scriptit.type.PrimitiveTypes;

/**********************
 * Created by steve6472
 * On date: 12/28/2021
 * Project: ScriptIt
 *
 ***********************/
public class ReturnIf extends Expression
{
	Expression condition;

	public ReturnIf(Expression condition)
	{
		this.condition = condition;
	}

	@Override
	public Result apply(Script script)
	{
		Result ifResult = condition.apply(script);

		if (ifResult.isDelay())
			return ifResult;

		if (ifResult.getStatus() == ResultStatus.VALUE && ifResult.getValue().type == PrimitiveTypes.BOOL)
			return ifResult.getValue().asPrimitive().getBoolean() ? Result.return_() : Result.pass();

		return null;
	}

	@Override
	public String showCode(int a)
	{
		return Highlighter.RETURN + "returnif " + Highlighter.RESET + condition.showCode(a);
	}

	@Override
	public String toString()
	{
		return "ReturnIf{" + "condition=" + condition + '}';
	}
}
