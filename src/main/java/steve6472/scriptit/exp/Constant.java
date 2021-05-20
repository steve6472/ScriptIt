package steve6472.scriptit.exp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
class Constant extends Expression
{
	double constant;

	public Constant(double constant)
	{
		this.constant = constant;
	}

	@Override
	double apply(ExpressionExecutor executor)
	{
		return constant;
	}

	@Override
	void print(int i)
	{
		System.out.println(i + " Constant " + constant);
	}

	@Override
	public String toString()
	{
		return "Constant{" + "constant=" + constant + '}';
	}
}
