package steve6472.scriptit.exp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/21/2021
 * Project: ScriptIt
 *
 ***********************/
public class Function
{
	ExpressionExecutor[] lines;
	String[] arguments;
	private int expressionIndex = 0;
	private boolean isDelayed = false;

	public Function(String... argumentNames)
	{
		this.arguments = argumentNames;
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

	public Result execute(Main.Script script, double[] arguments)
	{
		if (!isDelayed)
			script.getMemory().push();

		for (int i = 0; i < arguments.length; i++)
		{
			script.getMemory().addVariable(this.arguments[i], arguments[i]);
		}

		for (int i = expressionIndex; i < lines.length; i++)
		{
			expressionIndex = i;
			Result result = lines[i].execute(script);

			if (result.isDelay())
			{
				isDelayed = true;
				return result;
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
