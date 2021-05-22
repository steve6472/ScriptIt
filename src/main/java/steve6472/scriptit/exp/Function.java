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

	public void setExpressions(Main.Script script, String... expressions)
	{
		lines = new ExpressionExecutor[expressions.length];
		for (int i = 0; i < expressions.length; i++)
		{
			lines[i] = new ExpressionExecutor(script.getMemory());
			lines[i].setExpression(script.getParser().setExpression(expressions[i]).parse(script.getMemory()));
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
	public Result apply(Main.Script script)
	{
		if (!isDelayed)
			script.getMemory().push();

		for (int i = 0; i < arguments.length; i++)
		{
			script.getMemory().addVariable(this.argumentNames[i], arguments[i]);
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

		for (int i = expressionIndex; i < lines.length; i++)
		{
			expressionIndex = i;
			Result result = lines[i].execute(script);

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
			}
		}

		if (!isDelayed)
			script.getMemory().pop();
		return Result.pass();
	}
}
