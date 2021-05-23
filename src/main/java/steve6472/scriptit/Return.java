package steve6472.scriptit;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
public class Return extends Expression
{
	Expression returnValue;

	public Return(Expression expression)
	{
		this.returnValue = expression;
	}

	@Override
	public Result apply(Script script)
	{
		Result result = returnValue.apply(script);
		if (result.isDelay())
		{
			return result;
		}

		return Result.returnValue(result.getValue());
	}

	@Override
	public String toString()
	{
		return "Return{" + "returnValue=" + returnValue + '}';
	}
}
