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
	public double apply(ExpressionExecutor executor)
	{
		if (returnValue.apply(executor))
			return Double.NaN;

		return returnValue.val();
	}

	@Override
	public void print(int i)
	{

	}
}
