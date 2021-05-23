package steve6472.scriptit;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
public class ReturnThis extends Expression
{
	public ReturnThis()
	{
	}

	@Override
	public Result apply(Script script)
	{
		return Result.returnThis();
	}

	@Override
	public String toString()
	{
		return "ReturnThis{}";
	}
}
