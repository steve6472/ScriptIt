package steve6472.scriptit.functions;

import steve6472.scriptit.ScriptItSettings;
import steve6472.scriptit.expressions.Function;
import steve6472.scriptit.Result;
import steve6472.scriptit.Script;

/**********************
 * Created by steve6472
 * On date: 5/21/2021
 * Project: ScriptIt
 *
 ***********************/
public class DelayFunction extends Function
{
	public DelayFunction()
	{
		super((String) null);
	}

	@Override
	public Result apply(Script script)
	{
		if (ScriptItSettings.DELAY_DEBUG)
			System.out.println("Delay " + arguments[0]);
		int delay = arguments[0].asPrimitive().getInt();
		script.getMainExecutor().nextAllowedExecute = System.currentTimeMillis() + delay;
		return Result.delay(delay, true);
	}
}
