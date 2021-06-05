package steve6472.scriptit;

import steve6472.scriptit.libraries.Library;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/23/2021
 * Project: ScriptIt
 *
 ***********************/
public class Script
{
	private static class QueuedFunctionCall<A, B>
	{
		private final A executor;
		private final B mustExist;

		QueuedFunctionCall(A executor, B mustExist)
		{
			this.executor = executor;
			this.mustExist = mustExist;
		}

		public A executor()
		{
			return executor;
		}

		public B mustExist()
		{
			return mustExist;
		}
	}

	private final Workspace workspace;

	private final MyParser parser;
	MemoryStack memory;
	ExpressionExecutor[] lines;
	private int lastIndex = 0;
	private int currentIndex = 0;

	private List<QueuedFunctionCall<ExpressionExecutor, Boolean>> queuedFunctionCalls;
	QueuedFunctionCall<ExpressionExecutor, Boolean> currentFunction = null;
	private boolean waitingForQueuedFunction = false;

	public Script(Workspace workspace)
	{
		this.workspace = workspace;
		this.parser = new MyParser();
		this.memory = new MemoryStack(64);
		this.queuedFunctionCalls = new ArrayList<>();
	}

	/**
	 * Calls function inside script
	 * If the script is currently delayed it waits for the delay to end and calls the function immediately
	 *
	 * @param mustExist if true and function does not exist in script throwns an error
	 * @param name name of function
	 * @param types parameters
	 */
	public void queueFunctionCall(boolean mustExist, String name, Value... types)
	{
		ExpressionExecutor exe = new ExpressionExecutor(getMemory());

		Expression[] expressions = new Expression[types.length];
		for (int i = 0; i < types.length; i++)
		{
			expressions[i] = new ValueConstant(types[i]);
		}

		exe.setExpression(new FunctionCall(FunctionSource.function(name), expressions));
		queuedFunctionCalls.add(new QueuedFunctionCall<>(exe, mustExist));
	}

	public void addVariable(String name, Value value)
	{
		memory.addVariable(name, value);
	}

	public void addLibrary(Library library)
	{
		memory.addLibrary(library);
	}

	public void setExpressions(String... expressions)
	{
		lines = new ExpressionExecutor[expressions.length];
		for (int i = 0; i < expressions.length; i++)
		{
			lines[i] = new ExpressionExecutor(memory);
			lines[i].setExpression(parser.setExpression(expressions[i]).parse());
		}
	}

	public void setExpressions(Expression... expressions)
	{
		lines = new ExpressionExecutor[expressions.length];
		for (int i = 0; i < expressions.length; i++)
		{
			lines[i] = new ExpressionExecutor(memory);
			lines[i].setExpression(expressions[i]);
		}
	}

	public void runQueuedFunctions()
	{
		currentFunction = queuedFunctionCalls.isEmpty() ? null : queuedFunctionCalls.remove(0);
	}

	private Result execute_()
	{
		if (waitingForQueuedFunction)
		{
			runQueuedFunctions();
			if (currentFunction == null)
				return Result.delay(1);
		}

		if (currentFunction != null)
		{
			if (waitingForQueuedFunction)
			{
				waitingForQueuedFunction = false;
			}
			if (!currentFunction.mustExist())
			{
				FunctionCall functionCall = (FunctionCall) currentFunction.executor().getExpression();
				DelayValue[] args = functionCall.arguments;
				Type[] types = new Type[args.length];
				for (int i = 0; i < args.length; i++)
				{
					Value a = ((ValueConstant) args[i].expression).constant;
					types[i] = a.type;
				}

				if (getMemory().hasFunction(functionCall.source.functionName, types))
				{
					Result result = currentFunction.executor().execute(this);
					if (result.isDelay())
						return result;
					runQueuedFunctions();
				}
			} else
			{
				Result result = currentFunction.executor().execute(this);
				if (result.isDelay())
					return result;
				runQueuedFunctions();
			}
		}

		currentIndex = lastIndex;
		while (currentIndex < lines.length)
		{
			lastIndex = currentIndex;

			Result result = lines[currentIndex].execute(this);
			if (result.isReturnValue() || result.isReturn())
			{
				lastIndex = 0;
				return result;
			}
			if (result.isDelay())
				return result;
			if (result.isWaitForEvents())
			{
				waitingForQueuedFunction = true;
				return Result.delay();
			}

			currentIndex++;
		}

		return Result.return_();
	}

	public Result execute()
	{
		try
		{
			return execute_();
		} catch (Exception ex)
		{
			List<StackTraceElement> stackTraceElements = new ArrayList<>(Arrays.asList(ex.getStackTrace()));
			Collections.reverse(stackTraceElements);

			boolean inWhile = false;
			boolean isBody = false;
			boolean wasInScript = false;

			for (int i = 0; i < stackTraceElements.size(); i++)
			{
				StackTraceElement e = stackTraceElements.get(i);
				String errorLine = e.toString();
				String expression = errorLine.replace("steve6472.scriptit.", "");
				if (expression.startsWith("Script.") || expression.startsWith("ExpressionExecutor.execute"))
				{
					wasInScript = true;
					continue;
				}

				if (!wasInScript)
					continue;

				if (expression.startsWith("While.apply"))
				{
					System.out.print(ScriptReader.COLOR_WHILE + "While" + Log.RESET + " -> ");
					inWhile = true;
					isBody = true;
				} else if (expression.startsWith("If.apply"))
				{
					if (inWhile)
					{
						System.out.print(ScriptReader.COLOR_IF + "Condition" + Log.RESET + " -> ");
						inWhile = false;
					} else
					{
						System.out.print(ScriptReader.COLOR_IF + "If" + Log.RESET + " -> ");
					}
					isBody = true;
				} else if (expression.startsWith("Function.apply"))
				{
					if (isBody)
					{
						System.out.print(ScriptReader.COLOR_FUNCTION + "Body" + Log.RESET + " -> ");
						isBody = false;
					} else
					{
						System.out.print(ScriptReader.COLOR_FUNCTION + "Function" + Log.RESET + " -> ");
					}
					isBody = false;
				} else if (expression.startsWith("IfElse.apply"))
				{
					System.out.print(ScriptReader.COLOR_IF_ELSE + "IfElse" + Log.RESET + " -> ");
					isBody = true;
				} else
				{
					System.out.print(expression + " -> ");
				}
			}
			System.out.println("\n");
			ex.printStackTrace();

			System.exit(1);
			return null;
		}
	}

	public Value runWithDelay()
	{
		Result ret = Result.delay();

		while (ret.isDelay() && !ret.isReturnValue() && !ret.isReturn())
		{
			ret = execute();
		}
		if (ret.isReturnValue())
			return ret.getValue();
		else
			return Value.NULL;
	}

	public ExpressionExecutor currentExecutor()
	{
		return lines[currentIndex];
	}

	public MemoryStack getMemory()
	{
		return memory;
	}

	public MyParser getParser()
	{
		return parser;
	}

	public Workspace getWorkspace()
	{
		return workspace;
	}
}
