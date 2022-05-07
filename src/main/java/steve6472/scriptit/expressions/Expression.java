package steve6472.scriptit.expressions;

import steve6472.scriptit.Result;
import steve6472.scriptit.Script;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
public abstract class Expression
{
	public abstract Result apply(Script script);

	public abstract String showCode(int a);

	protected String depth(int a)
	{
		return "\t".repeat(Math.max(0, a));
	}

	//TODO: add this from Function: protected Value typeFunction = null;
}
