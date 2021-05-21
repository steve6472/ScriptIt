package steve6472.scriptit.exp;

import steve6472.scriptit.exp.functions.DelayFunction;
import steve6472.scriptit.exp.functions.PrintFunction;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
public class MemoryStack extends Memory
{
	private final Memory[] memories;

	private int curr = 0;

	public MemoryStack(int stackSize)
	{
		this.memories = new Memory[stackSize - 1];
		for (int i = 0; i < memories.length; i++)
		{
			this.memories[i] = new Memory();
		}

		// Force delay and print functions
		addFunction("delay", 1, new DelayFunction());
		addFunction("print", 1, new PrintFunction());
	}

	public MemoryStack push()
	{
		memories[curr++].set(this);
		return this;
	}

	public MemoryStack pop()
	{
		set(memories[--curr]);
		return this;
	}
}
