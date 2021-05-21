package steve6472.scriptit.exp.functions;

import steve6472.scriptit.exp.Function;
import steve6472.scriptit.exp.Main;
import steve6472.scriptit.exp.Result;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/21/2021
 * Project: ScriptIt
 *
 ***********************/
public class PrintFunction extends Function
{
	public PrintFunction()
	{
		super((String) null);
	}

	@Override
	public Result apply(Main.Script script)
	{
		System.out.println("Print: " + arguments[0]);
		return Result.pass();
	}
}
