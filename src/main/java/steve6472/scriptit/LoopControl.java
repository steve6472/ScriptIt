package steve6472.scriptit;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/22/2021
 * Project: ScriptIt
 *
 ***********************/
class LoopControl extends Expression
{
	Result result;

	public LoopControl(Result result)
	{
		this.result = result;
	}

	@Override
	public Result apply(Script script)
	{
		return result;
	}
}
