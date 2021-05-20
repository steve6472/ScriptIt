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
	Expression fullExpression;
	Memory memory;
	public long delay, delayStart;

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

	ExpressionExecutor(Memory memory, Expression ex)
	{
		this.memory = memory;
		this.fullExpression = ex;
	}

	public void delay(long delay)
	{
		this.delay = delay;
		delayStart = delayStartSupplier.get();
	}

	public double execute()
	{
		if (delayStart != -1 && !shouldAdvance.apply(delayStart, delay))
			return Double.NaN;

		delayStart = -1;
		return fullExpression.apply(this);
	}
}
