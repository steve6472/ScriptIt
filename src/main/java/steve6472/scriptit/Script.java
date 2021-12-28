package steve6472.scriptit;

import steve6472.scriptit.libraries.Library;
import steve6472.scriptit.tokenizer.Precedence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/23/2021
 * Project: ScriptIt
 *
 ***********************/
public class Script
{
	private record QueuedFunctionCall<A, B>(A executor, B mustExist)
	{
	}

	private final Workspace workspace;

	public TokenParser parser;
	MemoryStack memory;
	ExpressionExecutor[] lines;
	private int lastIndex = 0;
	private int currentIndex = 0;
	boolean exitOnError = false;

	private List<QueuedFunctionCall<ExpressionExecutor, Boolean>> queuedFunctionCalls;
	QueuedFunctionCall<ExpressionExecutor, Boolean> currentFunction = null;
	private boolean waitingForQueuedFunction = false;

	Supplier<Long> delayStartSupplier = System::currentTimeMillis;
	BiFunction<Long, Long, Boolean> shouldAdvance = (start, delay) -> System.currentTimeMillis() - start >= delay;

	public void setGetDelayStart(Supplier<Long> delayStartSupplier)
	{
		this.delayStartSupplier = delayStartSupplier;
	}

	public void setShouldAdvance(BiFunction<Long, Long, Boolean> shouldAdvance)
	{
		this.shouldAdvance = shouldAdvance;
	}

	public static Script create(Workspace workspace, File source)
	{
		String code = readFromFile(source);

		Script script = new Script(workspace);
		script.parser = new TokenParser();

		script.parser.setExpression(script, code);
		List<Expression> parse = script.parser.parseAll();
		script.setExpressions(parse.toArray(Expression[]::new));
		return script;
	}

	public static Script createEmpty(Workspace workspace)
	{
		return new Script(workspace);
	}

	private Script(Workspace workspace)
	{
		this.workspace = workspace;
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
			lines[i].setExpression(parser.parse(Precedence.ANYTHING));
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

	public static String COLOR_WHILE = Log.RED;
	public static String COLOR_IF_ELSE = Log.GREEN;
	public static String COLOR_IF = Log.YELLOW;
	public static String COLOR_FUNCTION = Log.BLUE;

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

			for (StackTraceElement e : stackTraceElements)
			{
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
					System.out.print(COLOR_WHILE + "While" + Log.RESET + " -> ");
					inWhile = true;
					isBody = true;
				} else if (expression.startsWith("If.apply"))
				{
					if (inWhile)
					{
						System.out.print(COLOR_IF + "Condition" + Log.RESET + " -> ");
						inWhile = false;
					} else
					{
						System.out.print(COLOR_IF + "If" + Log.RESET + " -> ");
					}
					isBody = true;
				} else if (expression.startsWith("Function.apply"))
				{
					if (isBody)
					{
						System.out.print(COLOR_FUNCTION + "Body" + Log.RESET + " -> ");
						isBody = false;
					} else
					{
						System.out.print(COLOR_FUNCTION + "Function" + Log.RESET + " -> ");
					}
					isBody = false;
				} else if (expression.startsWith("IfElse.apply"))
				{
					System.out.print(COLOR_IF_ELSE + "IfElse" + Log.RESET + " -> ");
					isBody = true;
				} else
				{
					System.out.print(expression + " -> ");
				}
			}
			System.out.println("\n");
			ex.printStackTrace();

			if (exitOnError)
				System.exit(1);
			throw ex;
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

	public Workspace getWorkspace()
	{
		return workspace;
	}

	public ExpressionExecutor[] getLines()
	{
		return lines;
	}

	public TokenParser getParser()
	{
		return parser;
	}

	public String showCode()
	{
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < lines.length; i++)
		{
			ExpressionExecutor line = lines[i];
			s.append(line.getExpression().showCode(0));
			if (!(line.getExpression() instanceof If || line.getExpression() instanceof While || line.getExpression() instanceof IfElse || line.getExpression() instanceof Comment))
				s.append(Highlighter.END);
			if (i < lines.length - 1)
				s.append("\n");
		}
		return s.toString();
	}

	private static String readFromFile(File file)
	{
		if (!file.exists())
			throw new IllegalArgumentException("File not found");
		if (file.isDirectory())
			throw new IllegalArgumentException("File is a directory");

		StringBuilder bobTheBuilder = new StringBuilder();

		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String s;
			while ((s = reader.readLine()) != null)
			{
				bobTheBuilder.append(s).append("\n");
			}
			reader.close();

		} catch (IOException e)
		{
			e.printStackTrace();
		}

		return bobTheBuilder.toString();
	}
}
