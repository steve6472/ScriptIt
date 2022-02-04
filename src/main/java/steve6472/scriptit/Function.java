package steve6472.scriptit;

import steve6472.scriptit.executor.Executor;
import steve6472.scriptit.tokenizer.Precedence;

import java.util.Arrays;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/21/2021
 * Project: ScriptIt
 *
 ***********************/
public class Function extends Expression
{
	Executor bodyExecutor;
	String[] argumentNames;
	protected Value[] arguments;
	protected Value returnThisHelper = null;
	protected boolean isDelayed;
	protected boolean isBody;

	/**
	 * @param argumentNames arg names
	 */
	public Function(String... argumentNames)
	{
		this.argumentNames = argumentNames;
	}

	public void setExpressions(Script script, String... expressions)
	{
		Expression[] lines = new Expression[expressions.length];
		for (int i = 0; i < expressions.length; i++)
		{
			lines[i] = script.getParser().setExpression(script, expressions[i]).parse(Precedence.ANYTHING);
		}
		bodyExecutor = new Executor(lines);
	}

	public void setExpressions(Expression... expressions)
	{
		bodyExecutor = new Executor(expressions);
	}

	public void setArguments(Value[] arguments)
	{
		this.arguments = arguments;
	}

	/**
	 * @param body false: will transform 'return 1' to just '1' 'cause functions return values
	 *             true: will retain the functionality of 'return 1'
	 *             default: false
	 */
	public void setBody(boolean body)
	{
		isBody = body;
	}

	/**
	 * The dot signalizes that the type is VALUE and sets the typeFunction
	 * "string".getThatString()
	 *
	 * function getThatString()
	 * {
	 *     return this; <--- HERE
	 * }
	 * @param returnThisHelper type
	 */
	public void setReturnThisHelper(Value returnThisHelper)
	{
		this.returnThisHelper = returnThisHelper;
	}

	@Override
	public Result apply(Script script)
	{
		if (!isDelayed)
		{
			script.getMemory().push();

			if (arguments != null)
			{
				for (int i = 0; i < arguments.length; i++)
				{
					script.getMemory().addVariable(this.argumentNames[i], arguments[i]);
				}
			}

			if (returnThisHelper != null)
			{
				returnThisHelper.values.forEach((k, v) ->
				{
					if (!returnThisHelper.isPrimitive())
					{
						script.getMemory().addVariable(k, (Value) v);
					}
				});

				returnThisHelper.type.functions.forEach((p, f) -> script.getMemory().addFunction(p, f));
			}
		}

		if (bodyExecutor.executeWhatYouCan(script).isDelay())
		{
			isDelayed = true;
			return Result.delay();
		}
		isDelayed = false;

		script.getMemory().pop();

		Result lastResult = bodyExecutor.getLastResult();
		bodyExecutor.reset();
//		System.out.println("Code: '" + showCode(0) + "'");

		Result result;

		if (lastResult.isLoopControl())
		{
			return lastResult;
		}

		if (lastResult.isReturnThis())
		{
			result = Result.value(returnThisHelper);
		}
		// Transform 'return 1' to '1' only if the function is not a body of if, for...
		else if (!isBody && lastResult.isReturnValue())
		{
			result = Result.value(lastResult.getValue());
		}
		else if (lastResult.isReturn())
		{
			result = Result.pass();
		} else
		{
			result = lastResult;
		}

		return result;
	}

	@Override
	public String showCode(int a)
	{
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < bodyExecutor.getExpressions().size(); i++)
		{
			Expression expression = bodyExecutor.getExpressions().get(i);
			s.append(depth(a));
			s.append(expression.showCode(a + 1));
			if (!(expression instanceof If || expression instanceof While || expression instanceof IfElse || expression instanceof Comment))
				s.append(Highlighter.END);
			if (i < bodyExecutor.getExpressions().size() + 1)
				s.append("\n");
		}
		return s.toString();
	}

	@Override
	public String toString()
	{
		return "Function{" + "lines=" + (bodyExecutor != null ? bodyExecutor.getExpressions() : null) + ", argumentNames=" + Arrays.toString(argumentNames) + ", expressionIndex=" + ", isDelayed=" + isDelayed + ", arguments=" + Arrays
			.toString(arguments) + ", typeFunction=" + returnThisHelper + '}';
	}
}
