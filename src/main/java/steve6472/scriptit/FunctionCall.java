package steve6472.scriptit;

import java.util.Arrays;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
public class FunctionCall extends Expression
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

	public DelayValue[] getArguments()
	{
		return arguments;
	}

	public FunctionSource getSource()
	{
		return source;
	}

	@Override
	public Result apply(Script script)
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

	@Override
	public String showCode(int a)
	{
		StringBuilder s = new StringBuilder();
		if (source.sourceType == FunctionSourceType.FUNCTION)
		{
			s.append(Highlighter.FUNCTION_NAME).append(source.functionName);
		} else if (source.sourceType == FunctionSourceType.VALUE)
		{
			s.append(Highlighter.IMPORT_NAME).append(source.value.toString()).append(Highlighter.RESET).append(".").append(Highlighter.FUNCTION_NAME).append(source.functionName);
		} else if (source.sourceType == FunctionSourceType.STATIC)
		{
			s.append(Highlighter.IMPORT_NAME).append(source.library.getLibraryName()).append(Highlighter.RESET).append('.').append(Highlighter.FUNCTION_NAME).append(source.functionName);
		}
		s.append(Highlighter.BRACET);
		s.append("(");
		for (int i = 0; i < args.length; i++)
		{
			s.append(arguments[i].expression.showCode(0));
			if (i < args.length - 1)
				s.append(Highlighter.SYMBOL).append(", ");
		}
		s.append(Highlighter.BRACET);
		s.append(")").append(Highlighter.RESET);
		return s.toString();
	}

	@Override
	public String toString()
	{
		return "FunctionCall{" + "arguments=" + Arrays.toString(arguments) + ", isDelayed=" + isDelayed + ", lastIndex=" + lastIndex + ", source=" + source + '}';
	}
}
