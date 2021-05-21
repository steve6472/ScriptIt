package steve6472.scriptit.exp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
public class DelayValue
{
	Expression expression;
	Result result = Result.delay(0);
	double value;
	boolean isSet;

	public DelayValue(Expression expression)
	{
		this.expression = expression;
	}

	public double val()
	{
		return value;
	}

	public void print(int i)
	{
		System.out.println("AbstractValue: " + value);
		System.out.println("IsSet: " + isSet);
		expression.print(i);
	}

	public boolean apply(Main.Script script)
	{
		Result temp;
		if (!isSet)
		{
			temp = expression.apply(script);
		} else
		{
			temp = result;
		}

		if (temp.isDelay())
		{
			isSet = false;
		} else
		{
			value = temp.getValue();
			isSet = true;
		}

		return !isSet;
	}

	@Override
	public String toString()
	{
		return "DelayValue{" + "expression=" + expression + '}';
	}
}
