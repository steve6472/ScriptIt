package steve6472.scriptit;

import steve6472.scriptit.type.PrimitiveTypes;
import steve6472.scriptit.value.PrimitiveValue;
import steve6472.scriptit.value.Value;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/21/2021
 * Project: ScriptIt
 *
 ***********************/
public class Result
{
	private static final Result PASS = new Result(Value.NULL, ResultStatus.PASS);
	private static final Result DELAY = new Result(Value.NULL, ResultStatus.DELAY);
	private static final Result WAIT = new Result(Value.NULL, ResultStatus.WAIT);
	private static final Result BREAK = new Result(Value.NULL, ResultStatus.BREAK);
	private static final Result CONTINUE = new Result(Value.NULL, ResultStatus.CONTINUE);
	private static final Result RETURN_THIS = new Result(Value.NULL, ResultStatus.RETURN_THIS);
	private static final Result WAIT_FOR_EVENT = new Result(Value.NULL, ResultStatus.WAIT_FOR_EVENTS);

	private final Value value;
	private final ResultStatus status;

	private Result(Value value, ResultStatus status)
	{
		this.value = value;
		this.status = status;
	}

	public static Result value(Value value)
	{
		return new Result(value, ResultStatus.VALUE);
	}

	public static Result delay(int delay, boolean skip)
	{
		return new Result(PrimitiveValue.newValue(PrimitiveTypes.INT, delay * (skip ? -1 : 1)), ResultStatus.DELAY);
	}

	public static Result delay()
	{
		return delay(0, true);
	}

	public static boolean isDelaySkip(Result result)
	{
		return result.isDelay() && result.getPrimitiveValue().getInt() < 0;
	}

	public static Result wait_() { return WAIT; }

	public static Result pass()
	{
		return PASS;
	}

	public static Result breakLoop()
	{
		return BREAK;
	}

	public static Result continueLoop()
	{
		return CONTINUE;
	}

	public static Result returnValue(Value value)
	{
		return new Result(value, ResultStatus.RETURN_VALUE);
	}

	public static Result return_()
	{
		return new Result(Value.NULL, ResultStatus.RETURN);
	}

	public static Result returnThis()
	{
		return RETURN_THIS;
	}

	public static Result waitForEvents()
	{
		return WAIT_FOR_EVENT;
	}

	public Value getValue()
	{
		return value;
	}

	public PrimitiveValue<?> getPrimitiveValue()
	{
		return (PrimitiveValue<?>) value;
	}

	public ResultStatus getStatus()
	{
		return status;
	}

	public boolean isDelay()
	{
		return getStatus() == ResultStatus.DELAY;
	}

	public boolean isReturnValue()
	{
		return getStatus() == ResultStatus.RETURN_VALUE;
	}

	public boolean isReturn()
	{
		return getStatus() == ResultStatus.RETURN;
	}

	public boolean isBreak()
	{
		return getStatus() == ResultStatus.BREAK;
	}

	public boolean isContinue()
	{
		return getStatus() == ResultStatus.CONTINUE;
	}

	public boolean isPass()
	{
		return getStatus() == ResultStatus.PASS;
	}

	public boolean isReturnThis()
	{
		return getStatus() == ResultStatus.RETURN_THIS;
	}

	public boolean isWaitForEvents()
	{
		return getStatus() == ResultStatus.WAIT_FOR_EVENTS;
	}

	public boolean isLoopControl()
	{
		return switch (getStatus())
			{
				case BREAK, CONTINUE -> true;
				default -> false;
			};
	}

	public boolean doesReturn()
	{
		return switch (getStatus())
			{
				case RETURN, RETURN_VALUE, RETURN_THIS -> true;
				default -> false;
			};
	}

	@Override
	public String toString()
	{
		return "Result{" + "value=" + value + ", status=" + status + '}';
	}
}
