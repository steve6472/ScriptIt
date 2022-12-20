package steve6472.scriptit;

import steve6472.scriptit.exceptions.TypeMismatchException;
import steve6472.scriptit.exceptions.ValueNotFoundException;
import steve6472.scriptit.expressions.Function;
import steve6472.scriptit.expressions.FunctionParameters;
import steve6472.scriptit.libraries.Library;
import steve6472.scriptit.type.ClassType;
import steve6472.scriptit.type.PrimitiveTypes;
import steve6472.scriptit.type.Type;
import steve6472.scriptit.value.Value;

import java.util.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
public class Memory
{
	public final Map<String, Library> libraries;
	public final Map<FunctionParameters, Function> functions;
	public final Map<String, Value> variables;
	public final Map<String, Type> types;

	public Memory()
	{
		this.libraries = new HashMap<>();
		this.functions = new HashMap<>();
		this.variables = new HashMap<>();
		this.types = new HashMap<>();
	}

	public boolean isLibrary(String name)
	{
		return libraries.containsKey(name);
	}

	public void addLibrary(Library library)
	{
		if (libraries.containsKey(library.getLibraryName())) return;

		this.libraries.put(library.getLibraryName(), library);
	}

	public void addType(Type type)
	{
		if (types.containsKey(type.getKeyword())) return;

		type.constructors.forEach(functions::put);
		types.put(type.getKeyword(), type);

		if (type.getArraySubtype() != null)
			addType(type.getArraySubtype());
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
			Value existing = variables.get(name);
			if (value == existing)
			{
				return;
			}
			if (existing.type != value.type)
			{
				throw new TypeMismatchException(existing.type, value.type);
			}
			existing.clear();
			existing.setFrom(value);
		} else
		{
			variables.put(name, value);
		}
	}

	public boolean hasVariable(String name)
	{
		return variables.containsKey(name);
	}

	public Value getVariable(String name)
	{
		Value val = variables.get(name);
		if (val == null)
			throw new ValueNotFoundException("Variable with name '" + name + "' not found");
		return val;
	}

	public Type getType(String name)
	{
		return types.get(name);
	}

	public void addFunction(FunctionParameters parameters, Function function)
	{
		functions.put(parameters, function);
		function.parameters = parameters;
	}

	public Function getFunction(String name, Type[] types)
	{
		record Funcs(int fitness, Function function) {}

		List<Funcs> foundFunctions = new ArrayList<>();
		Function exactMatch = null;

		main: for (Map.Entry<FunctionParameters, Function> e : functions.entrySet())
		{
			FunctionParameters functionParams = e.getKey();
			Function function = e.getValue();

			// Continue searching if function name does not match
			if (!functionParams.getName().equals(name))
				continue;

			// Continue searching if argument length is incorrect
			// TODO: Make FunctionParameters hold a bool about if function accepts infinite amount of parameters and test it
			if (functionParams.getTypes().length != types.length)
				continue;

			boolean foundExactMatch = true;
			int fitness = 0;

			// Try to match parameter types
			for (int i = 0; i < types.length; i++)
			{
				// Accepts any type, try to match other types
				if (functionParams.getTypes()[i] == PrimitiveTypes.ANY_TYPE)
				{
					fitness += 1;
					foundExactMatch = false;
					continue;
				}

				// Automatic type conversion
				// int -> double
				// double -> float
				if (ScriptItSettings.ALLOW_AUTOMATIC_CONVERSION)
				{
					if (functionParams.getTypes()[i] == PrimitiveTypes.DOUBLE && types[i] == PrimitiveTypes.INT)
					{
						fitness += 2;
						foundExactMatch = false;
						continue;
					} else if (functionParams.getTypes()[i] == PrimitiveTypes.DOUBLE && types[i] == PrimitiveTypes.FLOAT)
					{
						fitness += 2;
						foundExactMatch = false;
						continue;
					}
				}

				if (ScriptItSettings.ALLOW_CLASS_TYPE_CONVERSION)
				{
					if (functionParams.getTypes()[i] instanceof ClassType pct && types[i] instanceof ClassType ct)
					{
						if (pct.clazz.isAssignableFrom(ct.clazz))
						{
							fitness += 3;
							continue;
						}
					}
				}

				if (functionParams.getTypes()[i] != types[i])
				{
					continue main;
				}

				fitness += 3;
			}

			if (foundExactMatch)
			{
				exactMatch = function;
				break;
			} else
			{
				foundFunctions.add(new Funcs(fitness, function));
			}
		}

		if (exactMatch != null)
			return exactMatch;

		if (foundFunctions.isEmpty())
			throw new RuntimeException("Function '" + name + "' with argument types " + Arrays.toString(types) + " not found!");

		return foundFunctions
			.stream()
			.min(Comparator.comparingInt(a -> a.fitness))
			.orElseThrow(() -> new RuntimeException("Function '" + name + "' with argument types " + Arrays.toString(types) + " not found!")).function;
	}

	public boolean hasFunction(String name, Type[] types)
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
				if (parameters.getTypes()[i] != PrimitiveTypes.NULL && parameters.getTypes()[i] != types[i])
				{
					continue main;
				}
			}
			func = function;
			break;
		}

		return func != null;
	}

	/*
	 * Stack
	 */

	public void set(Memory other)
	{
		clear();
		other.libraries.forEach(libraries::put);
		other.functions.forEach(functions::put);
		other.variables.forEach(variables::put);
	}

	public void clear()
	{
		libraries.clear();
		functions.clear();
		variables.clear();
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

	public void dumpTypes()
	{
		types.forEach((k, v) ->
		{
			System.out.println(k + " = " + v);
		});
	}

	public void dumpFunctions()
	{
		functions.forEach((k, m) -> System.out.println(k + " " + m));
	}
}
