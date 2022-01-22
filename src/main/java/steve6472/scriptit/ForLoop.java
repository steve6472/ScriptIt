package steve6472.scriptit;

/**********************
 * Created by steve6472
 * On date: 1/10/2022
 * Project: ScriptIt
 *
 ***********************/
public class ForLoop extends Expression
{
	DelayValue initDelayed, updateDelayed;
	Expression init, condition, update, body;
	If anIf;
	boolean first = true;

	public ForLoop(Expression init, Expression condition, Expression update, Expression body)
	{
		this.init = init;
		this.condition = condition;
		this.update = update;
		this.body = body;

		initDelayed = new DelayValue(init);
		updateDelayed = new DelayValue(update);

		anIf = new If(condition, body);
	}

	@Override
	public Result apply(Script script)
	{
		if (first)
		{
			script.getMemory().push();
			first = false;
		}

		if (initDelayed.apply(script))
		{
			return Result.delay();
		}

		while (true)
		{
			script.getMemory().dumpVariables();

			Result ifResult = anIf.apply(script);

			if (ifResult.isBreak() || ifResult.isIfFalse())
			{
				script.getMemory().pop();
				first = true;
				return Result.pass();
			}

			if (ifResult.isDelay())
			{
				return ifResult;
			}

			if (ifResult.isReturnValue() || ifResult.isReturnValue())
			{
				script.getMemory().pop();
				first = true;
				return ifResult;
			}

			if (updateDelayed.apply(script))
			{
				return Result.delay();
			}
		}
	}

	@Override
	public String showCode(int a)
	{
		return Highlighter.WHILE + "for " + Highlighter.BRACET + "(" + init.showCode(a) + Highlighter.SYMBOL + "; " + condition.showCode(a) + Highlighter.SYMBOL + "; " + update.showCode(a) + Highlighter.BRACET + ") \n{" + Highlighter.RESET + body.showCode(a) + Highlighter.BRACET + "}" + Highlighter.RESET;
	}
}
