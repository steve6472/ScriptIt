package steve6472.scriptit.exp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
class DelayValue
{
	Expression expression;
	double value;
	boolean isSet;

	DelayValue(Expression expression)
	{
		this.expression = expression;
	}

	double val()
	{
		return value;
	}

	void print(int i)
	{
		System.out.println("Value: " + value);
		System.out.println("IsSet: " + isSet);
		expression.print(i);
	}

	boolean apply(ExpressionExecutor executor)
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
