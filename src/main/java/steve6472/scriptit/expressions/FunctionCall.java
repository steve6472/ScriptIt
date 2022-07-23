package steve6472.scriptit.expressions;

import steve6472.scriptit.*;
import steve6472.scriptit.executor.Executor;

import java.util.Arrays;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
public class FunctionCall extends Expression
{
	Executor argumentsExecutor, functionExecutor;
	private final Value[] args;

	public Function function = null;
	public FunctionSource source;
	public Expression[] arguments;

	public FunctionCall(FunctionSource source, Expression... arguments)
	{
		this.source = source;
		argumentsExecutor = new Executor(arguments);
		args = new Value[arguments.length];
		this.arguments = arguments;
	}

	public FunctionSource getSource()
	{
		return source;
	}

	@Override
	public Result apply(Script script)
	{
		stackTrace("Function call name: '" + source.functionName + "', type: " + source.sourceType);

		Executor.ExecutorResult argumentsResult = argumentsExecutor.executeWhatYouCan(script);
		if (argumentsResult.isDelay())
		{
			return Result.delay();
		} else
		{
			for (int i = 0; i < args.length; i++)
			{
				Value a = argumentsExecutor.getResult(i).getValue();
				args[i] = a;
			}

			if (function == null)
			{
				Type[] types = new Type[args.length];

				for (int i = 0; i < args.length; i++)
				{
					types[i] = args[i].type;
				}
				function = switch (source.sourceType)
					{
						case FUNCTION -> script.getMemory().getFunction(source.functionName, types);
						case STATIC -> source.library.getFunction(source.functionName, types);
						case VALUE -> source.value.type.getFunction(source.functionName, types);
					};
			}

			if (source.sourceType == FunctionSourceType.VALUE)
			{
				function.setReturnThisHelper(source.value);
			}

			function.setArguments(args);
		}

		if (functionExecutor == null)
		{
			functionExecutor = new Executor(function);
		}

		if (functionExecutor.executeWhatYouCan(script).isDelay())
			return Result.delay();

		Result result = functionExecutor.getLastResult();
		argumentsExecutor.reset();
		functionExecutor.reset();

		return result;
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
			s.append(argumentsExecutor.getExpressions().get(i).showCode(0));
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
		return "FunctionCall{" + "arguments=" + Arrays.toString(arguments)+ ", source=" + source + '}';
	}
}
