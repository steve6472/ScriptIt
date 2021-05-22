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
	Function function = null;
	Value[] args;
	boolean isDelayed;
	private int lastIndex = 0;
	FunctionSource source;

	public FunctionCall(FunctionSource source, Expression... arguments)
	{
		this.source = source;
		this.arguments = new DelayValue[arguments.length];
		for (int i = 0; i < arguments.length; i++)
		{
			this.arguments[i] = new DelayValue(arguments[i]);
		}
		args = new Value[arguments.length];
	}

	@Override
	public Result apply(Main.Script script)
	{
		if (!isDelayed)
		{
			for (int currentIndex = lastIndex; currentIndex < arguments.length; currentIndex++)
			{
				lastIndex = currentIndex;

				if (arguments[currentIndex].apply(script))
					return Result.delay();
				args[currentIndex] = arguments[currentIndex].val();
			}

			if (function == null)
			{
				Type[] types = new Type[args.length];
				for (int i = 0; i < args.length; i++)
				{
					Value a = args[i];
					types[i] = a.type;
				}
				if (source.sourceType == FunctionSourceType.FUNCTION)
				{
					function = script.getMemory().getFunction(source.functionName, types);
				}
				else if (source.sourceType == FunctionSourceType.VALUE)
				{
					function = source.value.type.getFunction(source.functionName, types);
				}
				else if (source.sourceType == FunctionSourceType.STATIC)
				{
					function = source.library.getFunction(source.functionName, types);
				}
				else
				{
					throw new IllegalArgumentException("SourceType " + source.sourceType + " not implemented!");
				}
			}
			function.setArguments(args);

			if (source.sourceType == FunctionSourceType.VALUE)
			{
				function.setTypeFunction(source.value);
			}
		}

		isDelayed = false;

		Result r = function.apply(script);
		if (r.isDelay())
			isDelayed = true;

		if (!isDelayed)
			lastIndex = 0;

		return r;
	}
}
