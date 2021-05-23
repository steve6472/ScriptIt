package steve6472.scriptit;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
class Assignment extends Expression
{
	private final String varName;
	private final Expression expression;
	Result res = Result.delay();
	Value value;

	Assignment(String varName, Expression expression)
	{
		this.varName = varName;
		this.expression = expression;
	}

	@Override
	public Result apply(Script script)
	{
		if (res.isDelay())
			res = expression.apply(script);

		if (res.isDelay())
			return res;

		value = res.getValue();

		script.memory.addVariable(varName, res.getValue());

		res = Result.delay();
		return Result.pass();
	}
}
