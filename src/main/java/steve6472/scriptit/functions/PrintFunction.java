package steve6472.scriptit.functions;

import steve6472.scriptit.Function;
import steve6472.scriptit.Result;
import steve6472.scriptit.Script;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/21/2021
 * Project: ScriptIt
 *
 ***********************/
public class PrintFunction extends Function
{
	boolean ln;

	public PrintFunction(boolean ln)
	{
		super((String) null);
		this.ln = ln;
	}

	@Override
	public Result apply(Script script)
	{
		if (ln)
			System.out.println(arguments[0]);
		else
			System.out.print(arguments[0]);
		return Result.pass();
	}
}
