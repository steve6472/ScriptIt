package steve6472.scriptit;

import steve6472.scriptit.executor.Executor;

/**********************
 * Created by steve6472
 * On date: 1/10/2022
 * Project: ScriptIt
 *
 ***********************/
public class ForLoop extends Expression
{
	//for (int i = 1; i < 5; i++) {}
	Expression init, condition, update, body;

	Executor initExecutor, conditionExecutor, bodyExecutor, updateExecutor;
	boolean first = true;

	public ForLoop(Expression init, Expression condition, Expression update, Expression body)
	{
		this.init = init;
		this.condition = condition;
		this.update = update;
		this.body = body;

		initExecutor = new Executor(init);
		conditionExecutor = new Executor(condition);
		bodyExecutor = new Executor(body);
		updateExecutor = new Executor(update);
	}

	private void fullReset(Script script, boolean popMemory)
	{
		conditionExecutor.reset();
		bodyExecutor.reset();
		initExecutor.reset();
		updateExecutor.reset();
		first = true;

		if (popMemory)
		{
			script.getMemory().pop();
		}
	}

	@Override
	public Result apply(Script script)
	{
		if (first)
		{
			script.getMemory().push();
			first = false;
		}

		if (initExecutor.executeWhatYouCan(script).isDelay())
			return Result.delay();

		while (true)
		{
			if (conditionExecutor.executeWhatYouCan(script).isDelay())
				return Result.delay();

			if (conditionExecutor.getLastResult().getValue().getBoolean())
			{
				if (bodyExecutor.executeWhatYouCan(script).isDelay())
					return Result.delay();

				Result lastResult = bodyExecutor.getLastResult();

				if (lastResult.isBreak())
				{
					fullReset(script, true);
					return Result.pass();
				}

				if (updateExecutor.executeWhatYouCan(script).isDelay())
					return Result.delay();

				fullReset(script, false);
			} else
			{
				fullReset(script, true);
				return Result.pass();
			}
		}
	}

	@Override
	public String showCode(int a)
	{
		return Highlighter.WHILE + "for " + Highlighter.BRACET + "(" + init.showCode(a) + Highlighter.SYMBOL + "; " + condition.showCode(a) + Highlighter.SYMBOL + "; " + update.showCode(a) + Highlighter.BRACET + ") \n{" + Highlighter.RESET + body.showCode(a) + Highlighter.BRACET + "}" + Highlighter.RESET;
	}
}
