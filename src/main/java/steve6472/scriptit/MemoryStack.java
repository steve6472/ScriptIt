package steve6472.scriptit;

import steve6472.scriptit.expressions.FunctionParameters;
import steve6472.scriptit.functions.DelayFunction;
import steve6472.scriptit.type.PrimitiveTypes;

/**********************
 * Created by steve6472
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
		addFunction(FunctionParameters.function("delay").addType(PrimitiveTypes.INT).build(), new DelayFunction());
		variables.put("true", PrimitiveTypes.TRUE());
		variables.put("false", PrimitiveTypes.FALSE());
	}

	public MemoryStack push()
	{
		if (curr + 1 >= memories.length)
			throw new RuntimeException("Tried to push memory over the set maximum depth of " + memories.length);

		memories[curr++].set(this);
		return this;
	}

	public MemoryStack pop()
	{
		if (curr - 1 < 0)
			throw new RuntimeException("Tried to push memory under the depth of 0");

		set(memories[--curr]);
		return this;
	}

	public int getCurr()
	{
		return curr;
	}
}
