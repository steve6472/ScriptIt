package steve6472.scriptit.expressions;

import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.executor.Executor;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/22/2021
 * Project: ScriptIt
 *
 ***********************/
public class IfElse extends Expression
{
	Expression condition, ifBody, elseBody;
	Executor conditionExecutor, ifBodyExecutor, elseBodyExecutor;

	public IfElse(Expression condition, Expression ifBody, Expression elseBody)
	{
		this.condition = condition;
		this.ifBody = ifBody;
		this.elseBody = elseBody;

		if (ifBody instanceof Function f)
			f.setBody(true);

		if (elseBody instanceof Function f)
			f.setBody(true);

		conditionExecutor = new Executor(condition);
		ifBodyExecutor = new Executor(ifBody);
		elseBodyExecutor = new Executor(elseBody);
	}

	private void fullReset()
	{
		conditionExecutor.reset();
		ifBodyExecutor.reset();
		elseBodyExecutor.reset();
	}

	@Override
	public Result apply(Script script)
	{
		if (conditionExecutor.executeWhatYouCan(script).isDelay())
			return Result.delay();

		if (conditionExecutor.getLastResult().getValue().getBoolean())
		{
			if (ifBodyExecutor.executeWhatYouCan(script).isDelay())
				return Result.delay();

			Result r = ifBodyExecutor.getLastResult();
			fullReset();

			return r;
		} else
		{
			if (elseBodyExecutor.executeWhatYouCan(script).isDelay())
				return Result.delay();

			Result r = elseBodyExecutor.getLastResult();
			fullReset();

			return r;
		}
	}

	@Override
	public String showCode(int a)
	{
		return "if (" + condition.showCode(a) + ")" + ifBody.showCode(a) + " else " + elseBody.showCode(a);
	}
}
