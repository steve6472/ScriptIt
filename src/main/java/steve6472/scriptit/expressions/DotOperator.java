package steve6472.scriptit.expressions;

import steve6472.scriptit.Highlighter;
import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.executor.Executor;
import steve6472.scriptit.libraries.Library;
import steve6472.scriptit.value.Value;

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
			case LIB_FUNC ->
			{
				((FunctionCall) right).source = FunctionSource.staticFunction(((FunctionCall) right).source.functionName, library);
				return onlyRight(script);
			}
			case VAR_FUNC, DOT_FUNC, CONST_FUNC ->
			{
				if (leftExe.executeWhatYouCan(script).isDelay())
					return Result.delay();

				Result leftResult = leftExe.getLastResult();

				((FunctionCall) right).source = FunctionSource.dot(((FunctionCall) right).source.functionName, leftResult.getValue());

				return onlyRight(script);
			}
			case VAR_VAR, DOT_VAR ->
			{
				Result apply = left.apply(script);
				Value value = apply.getValue();
				Object o = value.getValueByName(((Variable) right).variableName);
				if (o instanceof Value val)
				{
					return Result.value(val);
				} else
				{
					throw new RuntimeException("No value '" + ((Variable) right).variableName + "' in variable '" + ((Variable) left).variableName + "' or other error lol");
				}
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
			return DotType.CONST_FUNC;
		}

		// Math.PI();
		if (left instanceof Variable va)
		{
			if (script.getMemory().isLibrary(va.variableName))
			{
				if (right instanceof FunctionCall fc)
				{
					library = script.getMemory().libraries.get(va.variableName);
					fc.source = FunctionSource.staticFunction(fc.source.functionName, library);
					return DotType.LIB_FUNC;
				}
			}
		}

		// stringVariable.charAt(3);
		if (left instanceof Variable va)
		{
			if (script.getMemory().hasVariable(va.variableName))
			{
				if (right instanceof FunctionCall)
				{
					return DotType.VAR_FUNC;
				}
			}
		}

		if (left instanceof Variable va && right instanceof FunctionCall)
		{
			throw new RuntimeException("Variable - FunctionCall: Variable nor Library not found '" + va.variableName + "'");
		}

		/*
		 * something.getText().charAt(3);
		 *
		 *  -->  .getText().charAt(3)  <--
		 */
		if (left instanceof DotOperator && right instanceof FunctionCall)
		{
			return DotType.DOT_FUNC;
		}

		/*
		 * a.b = 3;
		 * -->  a.b  <--
		 */
		if (left instanceof Variable && right instanceof Variable)
		{
			return DotType.VAR_VAR;
		}

		/*
		 * a.b.c = 4;
		 * --> .b.c <--
		 */
		if (left instanceof DotOperator && right instanceof Variable)
		{
			return DotType.DOT_VAR;
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
		return Highlighter.VAR + left.showCode(0) + Highlighter.SYMBOL + "." + Highlighter.VAR + right.showCode(0) + Highlighter.RESET;
	}

	private enum DotType
	{
		LIB_FUNC, VAR_FUNC, DOT_FUNC, CONST_FUNC, VAR_VAR, DOT_VAR
	}
}