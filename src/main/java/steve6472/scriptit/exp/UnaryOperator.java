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
	public Result apply(Main.Script script)
	{
		return switch (operator)
			{
				case ADD -> Result.value(+left.apply(script).getValue());
				case SUB -> Result.value(-left.apply(script).getValue());
				default -> throw new IllegalStateException("Unexpected value: " + operator);
			};
	}

	@Override
	public String toString()
	{
		return "UnaryOperator{" + "operator=" + operator + '}';
	}
}
