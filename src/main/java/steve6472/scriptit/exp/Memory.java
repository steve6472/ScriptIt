package steve6472.scriptit.exp;

import java.util.HashMap;
import java.util.Map;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
public class Memory
{
	Map<String, HashMap<Integer, IFunction>> functions;
	Map<String, Double> variables;

	public Memory()
	{
		this.functions = new HashMap<>();
		this.variables = new HashMap<>();
	}

	public void addVariable(String name, double value)
	{
		variables.put(name, value);
	}

	public double getVariable(String name)
	{
		Double val = variables.get(name);
		if (val == null)
			throw new IllegalArgumentException("Variable with name '" + name + "' not found");
		return val;
	}

	public void addFunction(String name, int argumentCount, IFunction function)
	{
		HashMap<Integer, IFunction> funcs = functions.computeIfAbsent(name, k -> new HashMap<>());

		funcs.put(argumentCount, function);
	}

	public IFunction getFunction(String name, int argumentCount)
	{
		HashMap<Integer, IFunction> funcs = functions.get(name);
		if (funcs == null)
			throw new IllegalArgumentException("Function with name '" + name + "' not found");

		IFunction iFunction = funcs.get(argumentCount);

		if (iFunction == null)
			throw new IllegalArgumentException("Function with name '" + name + "' and " + argumentCount + " arguments not found");

		return iFunction;
	}
}
