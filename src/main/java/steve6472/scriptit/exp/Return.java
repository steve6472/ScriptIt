package steve6472.scriptit.exp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
public class Return extends Expression
{
	DelayValue returnValue;

	public Return(Expression expression)
	{
		this.returnValue = new DelayValue(expression);
	}

	@Override
	public Result apply(Main.Script script)
	{
		if (returnValue.apply(script))
			return Result.delay();

		return Result.returnValue(returnValue.val());
	}

	@Override
	public void print(int i)
	{

	}

	@Override
	public String toString()
	{
		return "Return{" + "returnValue=" + returnValue + '}';
	}
}
