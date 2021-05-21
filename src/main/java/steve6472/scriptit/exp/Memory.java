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

	/*
	 * Stack
	 */

	public void set(Memory other)
	{
		functions.clear();
		variables.clear();
		other.functions.forEach((k, m) -> {
			HashMap<Integer, IFunction> functionMap = new HashMap<>();
			m.forEach(functionMap::put);
			functions.put(k, functionMap);
		});
		other.variables.forEach((k, v) ->
		{
			variables.put(k, v);
		});
	}

	/*
	 * Debug
	 */

	public void dumpVariables()
	{
		variables.forEach((k, v) -> System.out.println(k + " = " + v));
	}

	public void dumpFunctions()
	{
		functions.forEach((k, m) -> {
			System.out.println("k\n");
			m.forEach((c, f) -> {
				System.out.println("\t" + c);
			});
		});
	}
}
