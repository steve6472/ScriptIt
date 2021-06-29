package steve6472.scriptit;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/22/2021
 * Project: ScriptIt
 *
 ***********************/
public class While extends Expression
{
	If anIf;

	public While(If anIf)
	{
		this.anIf = anIf;
	}

	@Override
	public Result apply(Script script)
	{
		while (true)
		{
			Result ifResult = anIf.apply(script);

			if (ifResult.isDelay())
				return ifResult;

			if (ifResult.isBreak())
				return Result.pass();

			if (ifResult.isReturn() || ifResult.isReturnValue())
				return ifResult;

			if (ifResult.isIfFalse())
				return Result.pass();
		}
	}

	@Override
	public String showCode(int a)
	{
		return Highlighter.WHILE + "while " + Highlighter.BRACET + "(" + Highlighter.RESET + anIf.condition.showCode(0) + Highlighter.BRACET + ")" + Highlighter.RESET + "\n" + Highlighter.BRACET + depth(a) + "{" + Highlighter.RESET + "\n" + anIf.body.showCode(a + 1) + depth(a) + Highlighter.BRACET + "}" + Highlighter.RESET;
	}
}




