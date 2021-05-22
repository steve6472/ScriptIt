package steve6472.scriptit.exp;

import steve6472.scriptit.exp.libraries.Library;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
class DotOperator extends Expression
{
	Expression left, right;
	Result leftResult = Result.delay();
	Result rightResult = Result.delay();
	Value leftValue, rightValue;
	boolean isLeftLibrary = false;
	Library library = null;

	public DotOperator(Expression left, Expression right)
	{
		this.left = left;
		this.right = right;

		if (right instanceof UnaryOperator unary)
		{
			if (!(unary.left instanceof FunctionCall) && !(unary.left instanceof Variable))
			{
				throw new IllegalStateException("Expression in unary operator must be either a FunctionCall or a Variable, is " + right.getClass().getCanonicalName());
			}
		}
		else if (!(right instanceof FunctionCall) && !(right instanceof Variable))
		{
			throw new IllegalStateException("Right expression of dot operator must be either a FunctionCall or a Variable, is " + right.getClass().getCanonicalName());
		}
	}

	@Override
	public Result apply(Main.Script script)
	{
		if (left instanceof Variable va)
		{
			if (script.getMemory().isLibrary(va.source.variableName))
			{
				isLeftLibrary = true;
				library = script.getMemory().libraries.get(va.source.variableName);
			} else
			{
				if (leftResult.isDelay())
					leftResult = left.apply(script);

				if (leftResult.isDelay())
					return leftResult;

				leftValue = leftResult.getValue();
			}
		} else
		{
			if (leftResult.isDelay())
				leftResult = left.apply(script);

			if (leftResult.isDelay())
				return leftResult;

			leftValue = leftResult.getValue();
		}

		if (right instanceof FunctionCall fc)
		{
			if (isLeftLibrary)
			{
				fc.source = FunctionSource.staticFunction(fc.source.functionName, library);
			} else
			{
				fc.source = FunctionSource.dot(fc.source.functionName, leftValue);
			}
		} else if (right instanceof UnaryOperator un)
		{
			if (isLeftLibrary)
			{
				((FunctionCall) un.left).source = FunctionSource.staticFunction(((FunctionCall) un.left).source.functionName, library);
			} else
			{
				((FunctionCall) un.left).source = FunctionSource.dot(((FunctionCall) un.left).source.functionName, leftValue);
			}
		}
		else if (right instanceof Variable va)
		{
			va.source.setValue(leftValue);
		}


		if (rightResult.isDelay())
			rightResult = right.apply(script);

		if (rightResult.isDelay())
			return rightResult;

		rightValue = rightResult.getValue();

		Result rightResult = this.rightResult;

		leftResult = Result.delay();
		this.rightResult = Result.delay();

		return rightResult;
	}

	@Override
	public String toString()
	{
		return "DotOperator{}";
	}
}