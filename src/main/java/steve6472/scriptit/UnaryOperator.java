package steve6472.scriptit;

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
	Function operatorFunction = null;
	Result leftResult = Result.delay();
	Value leftValue;

	public UnaryOperator(Operator operator, Expression left)
	{
		this.operator = operator;
		this.left = left;
	}

	@Override
	public Result apply(Script script)
	{
		if (leftResult.isDelay())
			leftResult = left.apply(script);

		if (leftResult.isDelay())
			return leftResult;

		leftValue = leftResult.getValue();

		if (operatorFunction == null)
		{
			operatorFunction = leftValue.type.unary.get(operator);

			if (operatorFunction == null)
			{
				throw new RuntimeException("No operator found for type '" + leftValue.type.getKeyword() + "' with operator: " + operator + " (" + operator.getOperator() + ")");
			}
		}

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

	@Override
	public String showCode(int a)
	{
		return operator.getOperator() + left.showCode(0);
	}
}