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
	Function function;
	double[] args;
	int index = 0;
	boolean isDelayed;

	public FunctionCall(Function function, Expression... arguments)
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
	public Result apply(Main.Script script)
	{
		if (!isDelayed)
		{
			for (int i = index; i < arguments.length; i++)
			{
				if (arguments[i].apply(script))
					return Result.delay();
				args[i] = arguments[i].val();
			}
			function.setArguments(args);
		}

		isDelayed = false;

		Result r = function.apply(script);
		if (r.isDelay())
			isDelayed = true;

		if (!isDelayed)
			index = 0;
		return r;
	}
}
