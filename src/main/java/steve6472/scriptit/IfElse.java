package steve6472.scriptit;

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
	Result ifResult = Result.delay();
	Result elseResult = Result.delay();

	public IfElse(If ifFunction, Expression elseFunction)
	{
		this.ifFunction = ifFunction;
		this.elseFunction = elseFunction;
	}

	@Override
	public Result apply(Script script)
	{
		if (ifResult.isDelay())
			ifResult = ifFunction.apply(script);

		if (ifResult.isDelay())
			return ifResult;

		if (ifResult.isIfFalse())
		{
			if (elseResult.isDelay())
				elseResult = elseFunction.apply(script);

			if (elseResult.isDelay())
				return elseResult;

			Result elseResult = this.elseResult;
			ifResult = Result.delay();
			this.elseResult = Result.delay();

			if (elseResult.getStatus() == ResultStatus.VALUE)
				return Result.returnValue(elseResult.getValue());

			return elseResult;
		} else
		{
			Result leftResult = this.ifResult;
			this.ifResult = Result.delay();
			elseResult = Result.delay();
			return leftResult;
		}
	}

	@Override
	public String showCode(int a)
	{
		return ifFunction.showCode(a) + Highlighter.ELSE + " else \n" + depth(a - 1) + Highlighter.BRACET + "{" + Highlighter.RESET + "\n" + elseFunction.showCode(a) + Highlighter.BRACET + depth(a - 1) + "}" + Highlighter.RESET;
	}
}
