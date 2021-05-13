package steve6472.scriptit.instructions;

import steve6472.scriptit.Instruction;
import steve6472.scriptit.Script;
import steve6472.scriptit.expression.Value;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/7/2021
 * Project: ScriptIt
 *
 ***********************/
public class Break extends Instruction
{
	public Break()
	{
		super("");
	}

	@Override
	public Value execute(Script script)
	{
		return null;
	}
}
