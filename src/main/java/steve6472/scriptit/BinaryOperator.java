package steve6472.scriptit;

import java.util.HashMap;

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
	Function operatorFunction = null;
	Value leftValue, rightValue;

	public BinaryOperator(Operator operator, Expression left, Expression right)
	{
		this.operator = operator;
		this.left = left;
		this.right = right;
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

		if (operatorFunction == null)
		{
			HashMap<Operator, Function> operatorFunctionHashMap = leftValue.type.binary.get(rightValue.type);
			if (operatorFunctionHashMap == null)
			{
				throw new RuntimeException("Type '" + leftValue.type.getKeyword() + "' does not have binary operation with type '" + rightValue.type.getKeyword() + "'");
			}
			operatorFunction = operatorFunctionHashMap.get(operator);

			if (operatorFunction == null)
			{
				throw new RuntimeException("No operator found for type '" + leftValue.type.getKeyword() + "' with right type '" + rightValue.type.getKeyword() + "', operator: " + operator + " (" + operator.getOperator() + ")");
			}
		}

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

	@Override
	public String showCode(int a)
	{
		return left.showCode(0) + " " + operator.getOperator() + " " + right.showCode(0);
	}
}