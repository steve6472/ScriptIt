package steve6472.scriptit.exp;

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
	public Result apply(Main.Script script)
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
}
