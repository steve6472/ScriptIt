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
	Expression left, right;
	Result leftResult = Result.delay();
	Result rightResult = Result.delay();
	Function operatorFunction;
	Value leftValue, rightValue;

	public BinaryOperator(Operator operator, Expression left, Expression right)
	{
		this.operator = operator;
		this.left = left;
		this.right = right;
		this.operatorFunction = PrimitiveTypes.DOUBLE.binary.get(PrimitiveTypes.DOUBLE).get(operator);
	}

	@Override
	public Result apply(Script script)
	{
		if (leftResult.isDelay())
			leftResult = left.apply(script);

		if (leftResult.isDelay())
			return leftResult;

		leftValue = leftResult.getValue();

		if (rightResult.isDelay())
			rightResult = right.apply(script);

		if (rightResult.isDelay())
			return rightResult;

		rightValue = rightResult.getValue();

		operatorFunction.setArguments(new Value[] {leftValue, rightValue});

		Result result = operatorFunction.apply(script);
		if (result.isDelay())
			return result;

		leftResult = Result.delay();
		rightResult = Result.delay();

		return result;
	}

	@Override
	public String toString()
	{
		return "BinaryOperator{" + "operator=" + operator + '}';
	}
}