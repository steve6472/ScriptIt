package steve6472.scriptit.expressions;

import steve6472.scriptit.*;
import steve6472.scriptit.exceptions.NoBinaryOperationFoundException;
import steve6472.scriptit.exceptions.NoOperatorFoundException;
import steve6472.scriptit.executor.Executor;
import steve6472.scriptit.tokenizer.IOperator;

import java.util.HashMap;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
public class BinaryOperator extends Expression
{
	/**
	 * Probably a performance improvement
	 */
	private final Value[] operatorArguments;

	IOperator operator;
	Executor operandExecutor, operationExecutor;
	Expression left, right;
	Function operatorFunction = null;

	public BinaryOperator(IOperator operator, Expression left, Expression right)
	{
		this.operator = operator;
		this.left = left;
		this.right = right;
		this.operandExecutor = new Executor(left, right);
		operatorArguments = new Value[2];
	}

	@Override
	public String toString()
	{
		return "BinaryOperator{" + "operator=" + operator + ", left=" + left + ", right=" + right + '}';
	}

	@Override
	public Result apply(Script script)
	{
		if (operandExecutor.executeWhatYouCan(script).isDelay())
			return Result.delay();

		Result left = operandExecutor.getResult(0);
		Result right = operandExecutor.getResult(1);

		operandExecutor.reset();

		if (operatorFunction == null)
		{
			HashMap<IOperator, Function> operatorFunctionHashMap = left.getValue().type.binary.get(right.getValue().type);
			if (operatorFunctionHashMap == null)
			{
				throw new NoBinaryOperationFoundException(left.getValue(), right.getValue());
			}
			operatorFunction = operatorFunctionHashMap.get(operator);

			if (operatorFunction == null)
			{
				throw new NoOperatorFoundException(operator, left.getValue().type, right.getValue().type);
			}
		}

		operatorArguments[0] = left.getValue();
		operatorArguments[1] = right.getValue();

		operatorFunction.setArguments(operatorArguments);

		if (operationExecutor == null)
		{
			operationExecutor = new Executor(operatorFunction);
		}
		operationExecutor.reset();

		if (operationExecutor.executeWhatYouCan(script).isDelay())
			return Result.delay();

		return operationExecutor.getLastResult();
	}

	@Override
	public String showCode(int a)
	{
		return '(' + left.showCode(0) + " " + operator.getSymbol() + " " + right.showCode(0) + ')';
	}
}