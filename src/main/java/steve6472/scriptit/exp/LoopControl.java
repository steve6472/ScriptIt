package steve6472.scriptit.exp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/22/2021
 * Project: ScriptIt
 *
 ***********************/
public class LoopControl extends Expression
{
	Result result;

	public LoopControl(Result result)
	{
		this.result = result;
	}

	@Override
	public Result apply(Main.Script script)
	{
		return result;
	}
}
