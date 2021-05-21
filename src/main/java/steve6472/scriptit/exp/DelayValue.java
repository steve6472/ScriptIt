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
		System.out.println("Value: " + value);
		System.out.println("IsSet: " + isSet);
		expression.print(i);
	}

	public boolean apply(ExpressionExecutor executor)
	{
		double temp;
		if (!isSet)
		{
			temp = expression.apply(executor);
		} else
		{
			temp = value;
		}

		if (Double.isNaN(temp))
		{
			isSet = false;
		} else
		{
			value = temp;
			isSet = true;
		}

		return !isSet;
	}
}
