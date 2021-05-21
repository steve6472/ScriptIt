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
	Function operatorFunction;
	Result leftResult = Result.delay();
	Value leftValue;

	public UnaryOperator(Operator operator, Expression left)
	{
		this.operator = operator;
		this.left = left;
		this.operatorFunction = PrimitiveTypes.DOUBLE.unary.get(operator);
	}

	@Override
	public Result apply(Main.Script script)
	{
		if (leftResult.isDelay())
			leftResult = left.apply(script);

		if (leftResult.isDelay())
			return leftResult;

		leftValue = leftResult.getValue();

		operatorFunction.setArguments(new Value[] {leftValue});
		Result apply = operatorFunction.apply(script);

		if (apply.isDelay())
			return apply;

		leftResult = Result.delay();

		return apply;
	}

	@Override
	public String toString()
	{
		return "UnaryOperator{" + "operator=" + operator + '}';
	}
}