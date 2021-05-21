package steve6472.scriptit.exp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
class UnaryOperator extends Expression
{
	Operator operator;
	Expression left;

	public UnaryOperator(Operator operator, Expression left)
	{
		this.operator = operator;
		this.left = left;
	}

	@Override
	public double apply(ExpressionExecutor executor)
	{
		return switch (operator)
			{
				case ADD -> +left.apply(executor);
				case SUB -> -left.apply(executor);
				default -> throw new IllegalStateException("Unexpected value: " + operator);
			};
	}

	@Override
	public void print(int i)
	{
		System.out.println(i + " Unary " + operator);
		left.print(i + 1);
	}

	@Override
	public String toString()
	{
		return "UnaryOperator{" + "operator=" + operator + '}';
	}
}
