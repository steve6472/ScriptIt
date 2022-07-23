package steve6472.scriptit.executor;

import steve6472.scriptit.expressions.Expression;
import steve6472.scriptit.Result;
import steve6472.scriptit.Script;

import java.util.List;

/**********************
 * Created by steve6472
 * On date: 1/22/2022
 * Project: ScriptIt
 *
 ***********************/
public class MainExecutor
{
	private final List<Expression> expressions;
	private int lastExecuted = -1;
	private boolean wasLastDelay = false;
	public long nextAllowedExecute = -1;

	public MainExecutor(Expression... expressions)
	{
		this.expressions = List.of(expressions);
	}

	public boolean canExecuteMore()
	{
		return lastExecuted <= expressions.size() - 2 || wasLastDelay;
	}

	public Expression getLastExecuted()
	{
		return expressions.get(lastExecuted);
	}

	public boolean isWasLastDelay()
	{
		return wasLastDelay;
	}

	public Result executeSingle(Script script)
	{
		if (nextAllowedExecute != -1 && script.delayStartSupplier.get() < nextAllowedExecute)
			return Result.wait_();

		if (!wasLastDelay)
			lastExecuted++;

		wasLastDelay = false;

		if (lastExecuted >= expressions.size())
			return Result.return_();

		Expression expression = getLastExecuted();
		Result result = expression.apply(script);

		if (result.isDelay())
		{
			wasLastDelay = true;
			if (Result.isDelaySkip(result))
				lastExecuted++;
		}

		return result;
	}

	public void reset()
	{
		lastExecuted = -1;
		wasLastDelay = false;
		nextAllowedExecute = -1;
	}

	public List<Expression> getExpressions()
	{
		return expressions;
	}
}
