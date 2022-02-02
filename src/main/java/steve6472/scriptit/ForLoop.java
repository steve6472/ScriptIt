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

	Executor initExecutor, conditionBodyExecutor, updateExecutor;
	boolean first = true;

	public ForLoop(Expression init, Expression condition, Expression update, Expression body)
	{
		this.init = init;
		this.condition = condition;
		this.update = update;
		this.body = body;

		initExecutor = new Executor(init);
		If anIf = new If(condition, body);
		conditionBodyExecutor = new Executor(anIf);
		System.out.println("Cond and body: " + Integer.toHexString(hashCode()));
		updateExecutor = new Executor(update);
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
			System.out.println("\nNEW LOOP");
			Executor.ExecutorResult condBodyExeRes = conditionBodyExecutor.executeWhatYouCan(script);
			System.out.println(condBodyExeRes);
			if (condBodyExeRes.isDelay())
				return Result.delay();


			if (!condBodyExeRes.isSkipped())
			{
				System.out.println("Running cond and body");
				Result bodyResult = conditionBodyExecutor.getLastResult();
//				System.out.println("----------------");
//
//				for (int i = 0; i < conditionBodyExecutor.getExpressions().size(); i++)
//				{
//					System.out.println(conditionBodyExecutor.getResult(i));
//				}
//
//				System.out.println("----------------\n\n");

				if (bodyResult.isBreak() || bodyResult.isIfFalse())
				{
					script.getMemory().pop();
					first = true;
					return Result.pass();
				}

				if (bodyResult.isReturnValue())
				{
					script.getMemory().pop();
					first = true;
					return bodyResult;
				}
			} else
			{
				if (updateExecutor.executeWhatYouCan(script).isDelay())
					return Result.delay();
				System.out.println("Inced");

				conditionBodyExecutor.reset();
				updateExecutor.reset();
			}
		}
	}

	@Override
	public String showCode(int a)
	{
		return Highlighter.WHILE + "for " + Highlighter.BRACET + "(" + init.showCode(a) + Highlighter.SYMBOL + "; " + condition.showCode(a) + Highlighter.SYMBOL + "; " + update.showCode(a) + Highlighter.BRACET + ") \n{" + Highlighter.RESET + body.showCode(a) + Highlighter.BRACET + "}" + Highlighter.RESET;
	}
}
