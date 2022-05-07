package steve6472.scriptit.functions;

import steve6472.scriptit.expressions.Function;
import steve6472.scriptit.Result;
import steve6472.scriptit.Script;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/21/2021
 * Project: ScriptIt
 *
 ***********************/
public class DelayFunction extends Function
{
	public static boolean DEBUG = false;

	public DelayFunction()
	{
		super((String) null);
	}

	@Override
	public Result apply(Script script)
	{
		if (DEBUG)
			System.out.println("Delay " + arguments[0]);
		int delay = arguments[0].getInt();
		script.getMainExecutor().nextAllowedExecute = System.currentTimeMillis() + delay;
		return Result.delay(delay, true);
	}
}
