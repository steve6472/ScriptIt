package steve6472.scriptit;

import steve6472.scriptit.expression.Value;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 4/30/2021
 * Project: ScriptIt
 *
 ***********************/
public abstract class Command
{
	public Command(String line)
	{

	}

	public abstract Value execute(Script script);
}
