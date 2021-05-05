package steve6472.scriptit.instructions;

import steve6472.scriptit.Instruction;
import steve6472.scriptit.Script;
import steve6472.scriptit.expression.Value;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/1/2021
 * Project: ScriptIt
 *
 ***********************/
public class DecreseScope extends Instruction
{
	public DecreseScope(String line)
	{
		super(line);
	}

	@Override
	public Value execute(Script script)
	{
		return null;
	}
}
