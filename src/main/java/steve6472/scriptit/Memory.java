package steve6472.scriptit;

import steve6472.scriptit.libraries.Library;

import java.util.Arrays;
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
	Map<String, Library> libraries;
	Map<FunctionParameters, Function> functions;
	Map<String, Value> variables;

	public Memory()
	{
		this.libraries = new HashMap<>();
		this.functions = new HashMap<>();
		this.variables = new HashMap<>();
	}

	public boolean isLibrary(String name)
	{
		return libraries.containsKey(name);
	}

	public void addLibrary(Library library)
	{
		this.libraries.put(library.getLibraryName(), library);
	}

	public void addType(Type type)
	{
		type.constructors.forEach((p, f) -> functions.put(p, f));
	}

	public void addVariable(String name, Value value)
	{
		if (name.equals("true") || name.equals("false"))
			throw new RuntimeException("true or false can not be used as variable names");

		/*
		 * Fixes a problem in this code:
		 *
		 * a = 0;
		 *
		 * function changeA()
		 * {
		 *     a = 1;
		 * }
		 * changeA();
		 * System.println(a); // prints 0
		 *
		 * Printing 0 is incorrect, value of 'a' was changed only in the function.
		 * That is because the 'a' in function is assigned new Value, after the memory pops the value is deleted
		 * For this code to work as expected the Value must stay the same, only it's fields can be changed.
		 * That is what this code does.
		 */
		if (variables.containsKey(name))
		{
			Value value1 = variables.get(name);
			if (value == value1)
				return;
			if (value1.type != value.type)
				throw new RuntimeException("Type mismatch, " + value.type + " != " + value1.type);
			value1.values.clear();
			value.values.forEach(value1::setValue);
		} else
		{
			variables.put(name, value);
		}
	}

	public Value getVariable(String name)
	{
		Value val = variables.get(name);
		if (val == null)
			throw new IllegalArgumentException("Variable with name '" + name + "' not found");
		return val;
	}

	public void addFunction(FunctionParameters parameters, Function function)
	{
		functions.put(parameters, function);
	}

	public Function getFunction(String name, Type[] types)
	{
		Function func = null;
		main: for (Map.Entry<FunctionParameters, Function> e : functions.entrySet())
		{
			FunctionParameters parameters = e.getKey();
			Function function = e.getValue();

			if (!parameters.getName().equals(name))
				continue;
			if (parameters.getTypes().length != types.length)
				continue;
			for (int i = 0; i < types.length; i++)
			{
				if (parameters.getTypes()[i] != types[i])
				{
					continue main;
				}
			}
			func = function;
			break;
		}

		if (func == null)
			throw new RuntimeException("Function '" + name + "' with argument types " + Arrays.toString(types) + " not found!");

		return func;
	}

	/*
	 * Stack
	 */

	public void set(Memory other)
	{
		libraries.clear();
		functions.clear();
		variables.clear();
		other.libraries.forEach((k, l) -> libraries.put(k, l));
		other.functions.forEach((k, m) -> functions.put(k, m));
		other.variables.forEach((k, v) -> variables.put(k, v));
	}

	/*
	 * Debug
	 */

	public void dumpVariables()
	{
		variables.forEach((k, v) ->
		{
			if (!k.equals("true") && !k.equals("false"))
				System.out.println(k + " = " + v);
		});
	}

	public void dumpFunctions()
	{
		functions.forEach((k, m) -> System.out.println(k + " " + m));
	}
}
