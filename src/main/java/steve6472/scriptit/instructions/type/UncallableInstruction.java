package steve6472.scriptit.instructions.type;

import steve6472.scriptit.Instruction;
import steve6472.scriptit.Script;
import steve6472.scriptit.expression.Value;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 4/30/2021
 * Project: ScriptIt
 *
 ***********************/
public abstract class UncallableInstruction extends Instruction
{
	public UncallableInstruction(String line)
	{
		super(line);
	}

	public Value execute(Script script)
	{
		throw new RuntimeException("This instruction should not be called!");
	}
}
