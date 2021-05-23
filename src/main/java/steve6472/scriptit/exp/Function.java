package steve6472.scriptit.exp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/21/2021
 * Project: ScriptIt
 *
 ***********************/
public class Function extends Expression
{
	ExpressionExecutor[] lines;
	String[] argumentNames;
	private int expressionIndex = 0;
	private boolean isDelayed = false;
	protected Value[] arguments;
	protected Value typeFunction = null;

	public Function(String... argumentNames)
	{
		this.argumentNames = argumentNames;
	}

	public void setExpressions(Script script, String... expressions)
	{
		lines = new ExpressionExecutor[expressions.length];
		for (int i = 0; i < expressions.length; i++)
		{
			lines[i] = new ExpressionExecutor(script.getMemory());
			lines[i].setExpression(script.getParser().setExpression(expressions[i]).parse());
		}
	}

	public void setArguments(Value[] arguments)
	{
		this.arguments = arguments;
	}

	public void setTypeFunction(Value typeFunction)
	{
		this.typeFunction = typeFunction;
	}

	@Override
	public Result apply(Script script)
	{
		if (!isDelayed)
			script.getMemory().push();

		if (arguments != null)
		{
			for (int i = 0; i < arguments.length; i++)
			{
				script.getMemory().addVariable(this.argumentNames[i], arguments[i]);
			}
		}

		if (!isDelayed && typeFunction != null)
		{
			typeFunction.values.forEach((k, v) ->
			{
				if (!typeFunction.isPrimitive())
				{
					script.getMemory().addVariable(k, (Value) v);
				}
			});

			typeFunction.type.functions.forEach((p, f) -> script.getMemory().addFunction(p, f));
		}

		int i = expressionIndex;
		while (i < lines.length)
		{
			expressionIndex = i;
			Result result = lines[i].execute(script);

//			System.out.println(script.memory.getCurr() + " " + i + " Func result: " + result);

			if (result.isDelay())
			{
				isDelayed = true;
				return Result.delay();
			} else if (result.isReturnValue())
			{
				isDelayed = false;
				expressionIndex = 0;
				script.getMemory().pop();
				return Result.value(result.getValue());
			} else if (result.isReturn())
			{
				isDelayed = false;
				expressionIndex = 0;
				script.getMemory().pop();
				return Result.pass();
			} else if (result.isLoop())
			{
				System.out.println("I GOT CALLED! (Function:96)");
				continue;
			} else if (result.isBreak())
			{
				isDelayed = false;
				expressionIndex = 0;
				script.getMemory().pop();
				return Result.breakLoop();
			} else if (result.isContinue())
			{
				isDelayed = false;
				expressionIndex = 0;
				script.getMemory().pop();
				return Result.continueLoop();
			}

			i++;
		}

		if (!isDelayed)
			script.getMemory().pop();
		expressionIndex = 0;
		return Result.pass();
	}
}
