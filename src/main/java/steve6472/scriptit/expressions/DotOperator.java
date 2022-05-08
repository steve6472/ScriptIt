package steve6472.scriptit.expressions;

import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.executor.Executor;
import steve6472.scriptit.libraries.Library;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
public class DotOperator extends Expression
{
	Expression left, right;
	Executor leftExe, rightExe;
	Library library = null;

	DotType dotType;

	// TODO: Add unary support
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
		else if (!(right instanceof FunctionCall) && !(right instanceof Variable) && !(right instanceof DotOperator))
		{
			throw new IllegalStateException("Right expression of dot operator must be either a FunctionCall, Variable or DotOperator, is " + right.getClass().getCanonicalName());
		}
		leftExe = new Executor(left);
		rightExe = new Executor(right);
	}

	private Result onlyRight(Script script)
	{
		if (rightExe.executeWhatYouCan(script).isDelay())
			return Result.delay();

		Result rightResult = rightExe.getLastResult();

		leftExe.reset();
		rightExe.reset();

		return rightResult;
	}

	@Override
	public Result apply(Script script)
	{
		dotType = findSimpleType(script);

		switch (dotType)
		{
			case LIBRARY_FUNCTION ->
			{
				((FunctionCall) right).source = FunctionSource.staticFunction(((FunctionCall) right).source.functionName, library);
				return onlyRight(script);
			}
			case VARIABLE_FUNCTION, DOT_FUNCTION, CONSTANT_FUNCTION ->
			{
				if (leftExe.executeWhatYouCan(script).isDelay())
					return Result.delay();

				Result leftResult = leftExe.getLastResult();

				((FunctionCall) right).source = FunctionSource.dot(((FunctionCall) right).source.functionName, leftResult.getValue());

				return onlyRight(script);
			}
			default -> throw new IllegalStateException("Unexpected value: " + dotType);
		}
	}

	private DotType findSimpleType(Script script)
	{
		if (dotType != null)
			return dotType;

		// "text".substring(4);
		if (left instanceof ValueConstant && right instanceof FunctionCall)
		{
			return DotType.CONSTANT_FUNCTION;
		}

		// Math.PI();
		if (left instanceof Variable va)
		{
			if (script.getMemory().isLibrary(va.source.variableName))
			{
				if (right instanceof FunctionCall fc)
				{
					library = script.getMemory().libraries.get(va.source.variableName);
					fc.source = FunctionSource.staticFunction(fc.source.functionName, library);
					return DotType.LIBRARY_FUNCTION;
				}
			}
		}

		// stringVariable.charAt(3);
		if (left instanceof Variable va)
		{
			if (script.getMemory().hasVariable(va.source.variableName))
			{
				if (right instanceof FunctionCall)
				{
					return DotType.VARIABLE_FUNCTION;
				}
			}
		}

		/*
		 * something.getText().charAt(3);
		 *
		 *  -->  .getText().charAt(3)  <--
		 */
		if (left instanceof DotOperator)
		{
			if (right instanceof FunctionCall)
			{
				return DotType.DOT_FUNCTION;
			}
		}

		throw new RuntimeException("DotType not found for " + left.getClass().getSimpleName() + " - " + right
			.getClass()
			.getSimpleName());
	}

	@Override
	public String toString()
	{
		return "DotOperator{" + "left=" + left + ", right=" + right + '}';
	}

	@Override
	public String showCode(int a)
	{
		return left.showCode(0) + "." + right.showCode(0);
	}

	private enum DotType
	{
		LIBRARY_FUNCTION, VARIABLE_FUNCTION, DOT_FUNCTION, CONSTANT_FUNCTION, VARIABLE_CHAIN, VARIABLE_DOT
	}
}