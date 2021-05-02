package steve6472.scriptit.commands;

import steve6472.scriptit.Command;
import steve6472.scriptit.Script;
import steve6472.scriptit.expression.Value;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/1/2021
 * Project: ScriptIt
 *
 ***********************/
public class IncreseScope extends Command
{
	public IncreseScope(String line)
	{
		super(line);
	}

	@Override
	public Value execute(Script script)
	{
		return null;
	}
}
