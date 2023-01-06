package steve6472.scriptit.expressions;

import steve6472.scriptit.Highlighter;
import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.executor.Executor;

/**********************
 * Created by steve6472
 * On date: 5/22/2021
 * Project: ScriptIt
 *
 ***********************/
public class While extends Expression
{
	Expression condition, body;
	Executor conditionExecutor, bodyExecutor;

	public While(Expression condition, Expression body)
	{
		this.condition = condition;
		this.body = body;
		this.conditionExecutor = new Executor(condition);

		if (body instanceof Function f)
			f.setBody(true);

		this.bodyExecutor = new Executor(body);
	}

	@Override
	public Result apply(Script script)
	{
		stackTrace("While");

		while (true)
		{
			if (conditionExecutor.executeWhatYouCan(script).isDelay())
				return Result.delay();

			if (conditionExecutor.getLastResult().getValue().asPrimitive().getBoolean())
			{
				if (bodyExecutor.executeWhatYouCan(script).isDelay())
					return Result.delay();

				Result lastResult = bodyExecutor.getLastResult();

				if (lastResult.isBreak())
				{
					conditionExecutor.reset();
					bodyExecutor.reset();
					return Result.pass();
				}

				conditionExecutor.reset();
				bodyExecutor.reset();
			} else
			{
				conditionExecutor.reset();
				bodyExecutor.reset();
				return Result.pass();
			}
		}
	}

	@Override
	public String showCode(int a)
	{
		if (body instanceof Function)
			return Highlighter.WHILE + "while " + Highlighter.BRACET + "(" + Highlighter.RESET + condition.showCode(0) + Highlighter.BRACET + ")" + Highlighter.RESET + "\n" + Highlighter.BRACET + depth(a) + "{" + Highlighter.RESET + "\n" + body.showCode(a + 1) + depth(a) + Highlighter.BRACET + "}" + Highlighter.RESET;
		else
			return Highlighter.WHILE + "while " + Highlighter.BRACET + "(" + Highlighter.RESET + condition.showCode(0) + Highlighter.BRACET + ")" + Highlighter.RESET + "\n" + body.showCode(a + 1) + depth(a) + Highlighter.RESET;
	}
}




