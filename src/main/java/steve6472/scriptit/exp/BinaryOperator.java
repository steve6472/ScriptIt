package steve6472.scriptit.exp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
class BinaryOperator extends Expression
{
	Operator operator;
	DelayValue left, right;

	public BinaryOperator(Operator operator, Expression left, Expression right)
	{
		this.operator = operator;
		this.left = new DelayValue(left);
		this.right = new DelayValue(right);
	}

	@Override
	public double apply(ExpressionExecutor executor)
	{
		if (left.apply(executor))
			return Double.NaN;

		if (right.apply(executor))
			return Double.NaN;

		return switch (operator)
			{
				case MUL -> left.val() * right.val();
				case DIV -> left.val() / right.val();
				case MOD -> left.val() % right.val();
				case ADD -> left.val() + right.val();
				case SUB -> left.val() - right.val();
				default -> throw new IllegalStateException("Unexpected value: " + operator);
			};
	}

	@Override
	public void print(int i)
	{
		left.print(i + 1);
		System.out.println(i + " Operator " + operator);
		right.print(i + 1);
	}

	@Override
	public String toString()
	{
		return "BinaryOperator{" + "operator=" + operator + '}';
	}
}
