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
	public Result apply(Main.Script script)
	{
		if (left.apply(script))
			return Result.delay();

		if (right.apply(script))
			return Result.delay();

		return switch (operator)
			{
				case MUL -> Result.value(left.val() * right.val());
				case DIV -> Result.value(left.val() / right.val());
				case MOD -> Result.value(left.val() % right.val());
				case ADD -> Result.value(left.val() + right.val());
				case SUB -> Result.value(left.val() - right.val());
				default -> throw new IllegalStateException("Unexpected value: " + operator);
			};
	}

	@Override
	public String toString()
	{
		return "BinaryOperator{" + "operator=" + operator + '}';
	}
}
