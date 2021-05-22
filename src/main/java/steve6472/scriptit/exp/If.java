package steve6472.scriptit.exp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/22/2021
 * Project: ScriptIt
 *
 ***********************/
class If extends Expression
{
	Expression condition;
	Function body;
	Result condResult = Result.delay();
	boolean isBodyDelayed;
	Value insideValue;

	public If(Expression condition, Function body)
	{
		this.condition = condition;
		this.body = body;
	}

	@Override
	public Result apply(Main.Script script)
	{
		if (!isBodyDelayed)
		{
			if (condResult.isDelay())
				condResult = condition.apply(script);

			if (condResult.isDelay())
				return condResult;

			Value condValue = condResult.getValue();

			// Condition is false -> do not continue
			if (!condValue.getBoolean())
				return Result.passIfFalse();

			if (insideValue != Value.NULL)
			{
				body.setTypeFunction(insideValue);
			}
		}

		isBodyDelayed = false;

		Result r = body.apply(script);
		if (r.isDelay())
			isBodyDelayed = true;
		else if (r.getStatus() == ResultStatus.VALUE)
			return Result.returnValue(r.getValue());

		return r;
	}
}
