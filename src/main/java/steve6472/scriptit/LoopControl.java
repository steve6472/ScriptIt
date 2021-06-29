package steve6472.scriptit;

import java.util.Locale;

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

	@Override
	public String showCode(int a)
	{
		return Highlighter.LOOP_CONTROL + result.getStatus().toString().toLowerCase(Locale.ROOT) + Highlighter.RESET;
	}
}
