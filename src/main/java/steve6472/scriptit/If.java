package steve6472.scriptit;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/22/2021
 * Project: ScriptIt
 *
 ***********************/
public class If extends Expression
{
	Expression condition;
	Expression body;
	Result condResult = Result.delay();
	boolean isBodyDelayed;
	Value insideValue;

	public If(Expression condition, Expression body)
	{
		this.condition = condition;
		this.body = body;
	}

	@Override
	public Result apply(Script script)
	{
		if (!isBodyDelayed)
		{
			if (condResult.isDelay())
				condResult = condition.apply(script);

			if (condResult.isDelay())
				return condResult;

			Value condValue = condResult.getValue();
			condResult = Result.delay();

			// Condition is false -> do not continue
			if (!condValue.getBoolean())
				return Result.passIfFalse();

			if (insideValue != Value.NULL && body instanceof Function)
			{
				Function f = (Function) body;

				f.setTypeFunction(insideValue);
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

	@Override
	public String showCode(int a)
	{
		return Highlighter.IF + "if " + Highlighter.BRACET + "(" + condition.showCode(0) + Highlighter.BRACET + ")\n" + Highlighter.BRACET + depth(a - 1) + "{" + Highlighter.RESET + "\n" + body.showCode(a) + Highlighter.BRACET + depth(a - 1) + "}" + Highlighter.RESET;
	}
}
