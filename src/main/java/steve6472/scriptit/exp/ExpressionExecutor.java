package steve6472.scriptit.exp;

import java.util.function.BiFunction;
import java.util.function.Supplier;

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

	private Supplier<Long> delayStartSupplier = System::currentTimeMillis;
	private BiFunction<Long, Long, Boolean> shouldAdvance = (start, delay) -> System.currentTimeMillis() - start >= delay;

	public void setGetDelayStart(Supplier<Long> delayStartSupplier)
	{
		this.delayStartSupplier = delayStartSupplier;
	}

	public void setShouldAdvance(BiFunction<Long, Long, Boolean> shouldAdvance)
	{
		this.shouldAdvance = shouldAdvance;
	}

	ExpressionExecutor(MemoryStack memory)
	{
		this.memory = memory;
	}

	public ExpressionExecutor setExpression(Expression expression)
	{
		this.expression = expression;
		return this;
	}

	private void delay(long delay)
	{
		this.delay = delay;
		delayStart = delayStartSupplier.get();
	}

	public Result execute(Script script)
	{
		if (delayStart != -1)
		{
			boolean advance = shouldAdvance.apply(delayStart, delay);
			if (!advance)
				return Result.delay();
			delayStart = -1;
			return Result.pass();
		}

		Result result = expression.apply(script);

		if (result.isDelay() && !result.getValue().isNull() && result.getValue().getInt() > 0)
			delay(result.getValue().getInt());

		return result;
	}

	@Override
	public String toString()
	{
		return "ExpressionExecutor{" + "expression=" + expression + '}';
	}
}
