package steve6472.scriptit;

import steve6472.scriptit.executor.Executor;

/**********************
 * Created by steve6472
 * On date: 2/4/2022
 * Project: ScriptIt
 *
 ***********************/
public class TernaryExpression extends Expression
{
	Expression condition, ifTrue, ifFalse;
	Executor conditionExecutor, trueExecutor, falseExecutor;

	public TernaryExpression(Expression condition, Expression ifTrue, Expression ifFalse)
	{
		this.condition = condition;
		this.ifTrue = ifTrue;
		this.ifFalse = ifFalse;

		this.conditionExecutor = new Executor(condition);
		this.trueExecutor = new Executor(ifTrue);
		this.falseExecutor = new Executor(ifFalse);
	}

	@Override
	public Result apply(Script script)
	{
		if (conditionExecutor.executeWhatYouCan(script).isDelay())
			return Result.delay();

		if (conditionExecutor.getLastResult().getValue().getBoolean())
		{
			if (trueExecutor.executeWhatYouCan(script).isDelay())
				return Result.delay();

			Result lastResult = trueExecutor.getLastResult();
			conditionExecutor.reset();
			trueExecutor.reset();

			return lastResult;
		} else
		{
			if (falseExecutor.executeWhatYouCan(script).isDelay())
				return Result.delay();

			Result lastResult = falseExecutor.getLastResult();
			conditionExecutor.reset();
			falseExecutor.reset();

			return lastResult;
		}
	}

	@Override
	public String showCode(int a)
	{
		return condition.showCode(a) + " ? " + ifTrue.showCode(a) + " : " + ifFalse.showCode(a);
	}
}
