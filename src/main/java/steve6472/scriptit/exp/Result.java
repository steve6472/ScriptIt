package steve6472.scriptit.exp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/21/2021
 * Project: ScriptIt
 *
 ***********************/
public class Result
{
	private static final Result PASS = new Result(0, ResultStatus.PASS);
	private static final Result DELAY = new Result(0, ResultStatus.DELAY);

	private final double value;
	private final ResultStatus status;

	private Result(double value, ResultStatus status)
	{
		this.value = value;
		this.status = status;
	}

	public static Result value(double value)
	{
		return new Result(value, ResultStatus.VALUE);
	}

	public static Result delay(double delay)
	{
		return new Result(delay, ResultStatus.DELAY);
	}

	public static Result delay()
	{
		return DELAY;
	}

	public static Result pass()
	{
		return PASS;
	}

	public static Result returnValue(double value)
	{
		return new Result(value, ResultStatus.RETURN_VALUE);
	}

	public static Result return_()
	{
		return new Result(0, ResultStatus.RETURN);
	}

	public double getValue()
	{
		return value;
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

	@Override
	public String toString()
	{
		return "Result{" + "value=" + value + ", status=" + status + '}';
	}
}
