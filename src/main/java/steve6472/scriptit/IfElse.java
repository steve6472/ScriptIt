package steve6472.scriptit;

import steve6472.scriptit.executor.Executor;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/22/2021
 * Project: ScriptIt
 *
 ***********************/
public class IfElse extends Expression
{
	If ifFunction;
	Expression elseFunction;
	Executor ifExecutor, elseExecutor;

	public IfElse(If ifFunction, Expression elseFunction)
	{
		this.ifFunction = ifFunction;
		this.elseFunction = elseFunction;
		if (elseFunction instanceof Function f)
			f.setBody(true);
		this.elseExecutor = new Executor(elseFunction);
		this.ifExecutor = new Executor(ifFunction);
	}

	@Override
	public Result apply(Script script)
	{
		if (ifExecutor.executeWhatYouCan(script).isDelay())
			return Result.delay();

		Result ifResult = ifExecutor.getLastResult();
		if (ifResult.isIfFalse())
		{
			if (elseExecutor.executeWhatYouCan(script).isDelay())
				return Result.delay();
			Result elseResult = elseExecutor.getLastResult();

			ifExecutor.reset();
			elseExecutor.reset();

			return elseResult;
		} else
		{
			ifExecutor.reset();
			elseExecutor.reset();

			return ifResult;
		}
	}

	@Override
	public String showCode(int a)
	{
		return ifFunction.showCode(a) + Highlighter.ELSE + " else \n" + depth(a - 1) + Highlighter.BRACET + "{" + Highlighter.RESET + "\n" + elseFunction.showCode(a) + Highlighter.BRACET + depth(a - 1) + "}" + Highlighter.RESET;
	}
}
