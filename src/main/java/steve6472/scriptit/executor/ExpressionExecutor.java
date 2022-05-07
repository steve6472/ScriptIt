package steve6472.scriptit.executor;

import steve6472.scriptit.MemoryStack;
import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.expressions.Expression;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
public class ExpressionExecutor
{
	public final MemoryStack memory;
	private Expression expression;
	public long delay, delayStart = -1;

	public ExpressionExecutor(MemoryStack memory)
	{
		this.memory = memory;
	}

	public ExpressionExecutor setExpression(Expression expression)
	{
		this.expression = expression;
		return this;
	}

	private void delay(Script script, long delay)
	{
		this.delay = delay;
		delayStart = script.delayStartSupplier.get();
	}

	public boolean canAdvance(Script script)
	{
		if (delayStart != -1)
		{
			boolean advance = script.shouldAdvance.apply(delayStart, delay);
			if (!advance)
				return false;
			delayStart = -1;
			return true;
		}

		return true;
	}

	public Result execute(Script script)
	{
		if (delayStart != -1)
		{
			return canAdvance(script) ? Result.pass() : Result.delay();
		}

		Result result = expression.apply(script);

		if (result.isDelay() && !result.getValue().isNull() && result.getValue().getInt() > 0)
			delay(script, result.getValue().getInt());

		return result;
	}

	Expression getExpression()
	{
		return expression;
	}

	@Override
	public String toString()
	{
		return "ExpressionExecutor{" + "expression=" + expression + '}';
	}
}
