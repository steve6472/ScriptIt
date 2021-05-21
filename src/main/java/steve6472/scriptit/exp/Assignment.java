package steve6472.scriptit.exp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
class Assignment extends Expression
{
	private final String varName;
	private final DelayValue val;

	Assignment(String varName, Expression expression)
	{
		this.varName = varName;
		this.val = new DelayValue(expression);
	}

	@Override
	public Result apply(Main.Script script)
	{
		if (val.apply(script))
			return Result.delay();

		script.memory.addVariable(varName, val.val());
		return Result.pass();
	}
}
