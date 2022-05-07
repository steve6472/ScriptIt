package steve6472.scriptit.expressions;

import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.Value;
import steve6472.scriptit.executor.Executor;
import steve6472.scriptit.tokenizer.IOperator;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
public class UnaryOperator extends Expression
{
	/**
	 * Probably a performance improvement
	 */
	private final Value[] operatorArguments;

	IOperator operator;
	public Expression left;
	Function operatorFunction = null;
	Executor argumentExecutor, operationExecutor;

	public UnaryOperator(IOperator operator, Expression left)
	{
		this.operator = operator;
		this.left = left;
		this.argumentExecutor = new Executor(left);
		operatorArguments = new Value[1];
	}

	@Override
	public Result apply(Script script)
	{
		if (argumentExecutor.executeWhatYouCan(script).isDelay())
			return Result.delay();

		if (operatorFunction == null)
		{
			operatorFunction = argumentExecutor.getLastResult().getValue().type.unary.get(operator);

			if (operatorFunction == null)
			{
				throw new RuntimeException("No operator found for type '" + argumentExecutor.getLastResult().getValue().type.getKeyword() + "' with operator: " + operator + " (" + operator.getSymbol() + ")");
			}
		}

		operatorArguments[0] = argumentExecutor.getLastResult().getValue();

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
	public String toString()
	{
		return "UnaryOperator{" + "operator=" + operator + '}';
	}

	@Override
	public String showCode(int a)
	{
		return operator.getSymbol() + left.showCode(0);
	}
}