package steve6472.scriptit.exp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
class FunctionCall extends Expression
{
	DelayValue[] arguments;
	IFunction function;
	double delayValue = Double.NaN;
	double[] args;

	public FunctionCall(IFunction function, Expression... arguments)
	{
		this.function = function;
		this.arguments = new DelayValue[arguments.length];
		for (int i = 0; i < arguments.length; i++)
		{
			this.arguments[i] = new DelayValue(arguments[i]);
		}
		args = new double[arguments.length];
	}

	@Override
	public Result apply(ExpressionExecutor executor)
	{
		for (int i = 0; i < arguments.length; i++)
		{
			if (arguments[i].apply(executor))
				return Result.delay();
			args[i] = arguments[i].val();
		}

		// If no delay -> get value from function
		if (Double.isNaN(delayValue))
		{
			delayValue = function.apply(executor, args);
			return Result.delay();
		}
		// If delay -> assing the already calculated value
		else
		{
			double temp = delayValue;
			delayValue = Double.NaN;
			return Result.value(temp);
		}
	}

	@Override
	public void print(int i)
	{
		System.out.println(i + " Function with " + arguments.length + " parameters:");
		for (DelayValue ex : arguments)
		{
			ex.print(i + 1);
		}
		System.out.println();
	}
}
