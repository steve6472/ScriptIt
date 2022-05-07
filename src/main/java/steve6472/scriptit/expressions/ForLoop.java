package steve6472.scriptit.expressions;

import steve6472.scriptit.Highlighter;
import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.executor.Executor;

/**********************
 * Created by steve6472
 * On date: 1/10/2022
 * Project: ScriptIt
 *
 ***********************/
public class ForLoop extends Expression
{
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

	/* [1]
	 * for ( [2] int i = 1; [3] i < 5; [5] i++)
	 * {
	 *      [4]
	 * }
	 * [6]
	 */

	@Override
	public Result apply(Script script)
	{
		if (first)
		{
			script.getMemory().push(); // [1]
			first = false;
		}

		if (initExecutor.executeWhatYouCan(script).isDelay())
			return Result.delay();

		while (true)
		{
			if (conditionExecutor.executeWhatYouCan(script).isDelay()) // [2]
				return Result.delay();

			if (conditionExecutor.getLastResult().getValue().getBoolean()) // [3]
			{
				if (bodyExecutor.executeWhatYouCan(script).isDelay()) // [4]
					return Result.delay();

				Result lastResult = bodyExecutor.getLastResult();

				if (lastResult.isBreak())
				{
					fullReset(script, true);
					return Result.pass();
				}

				if (updateExecutor.executeWhatYouCan(script).isDelay()) // [5]
					return Result.delay();

				fullReset(script, false);
			} else
			{
				fullReset(script, true); // [6]
				return Result.pass();
			}
		}
	}

	@Override
	public String showCode(int a)
	{
		return Highlighter.WHILE + "for " + Highlighter.BRACET + "(" + init.showCode(a) + Highlighter.SYMBOL + "; " + condition.showCode(a) + Highlighter.SYMBOL + "; " + update.showCode(a) + Highlighter.BRACET + ") \n{" + Highlighter.RESET + body.showCode(a) + Highlighter.BRACET + "}" + Highlighter.RESET;
	}

	@Override
	public String toString()
	{
		return "ForLoop{" + "init=" + init + ", condition=" + condition + ", update=" + update + ", body=" + body + ", initExecutor=" + initExecutor + ", conditionExecutor=" + conditionExecutor + ", bodyExecutor=" + bodyExecutor + ", updateExecutor=" + updateExecutor + ", first=" + first + '}';
	}
}
