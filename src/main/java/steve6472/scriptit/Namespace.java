package steve6472.scriptit;

import steve6472.scriptit.expression.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static steve6472.scriptit.TypeDeclarations.BOOL;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/2/2021
 * Project: ScriptIt
 *
 ***********************/
public class Namespace
{
	public HashMap<String, Type> typeMap = new HashMap<>();
	public HashMap<String, Value> valueMap = new HashMap<>();

	public Map<FunctionParameters, Constructor> constructorMap = new HashMap<>();

	public Namespace()
	{
		// Force true and false booleans into namespace
		addValue("true", new Value(BOOL, true));
		addValue("false", new Value(BOOL, false));
	}

	public void print()
	{
		System.out.println(typeMap);
		System.out.println(valueMap);
		System.out.println(constructorMap);
	}

	public void importType(Type type)
	{
		typeMap.put(type.getKeyword(), type);
	}

	public boolean isType(String name)
	{
		return typeMap.containsKey(name);
	}

	public Type getType(String name)
	{
		return typeMap.get(name);
	}

	public void addValue(String name, Value value)
	{
		valueMap.put(name, value);
	}

	public Value getValue(String name)
	{
		return valueMap.get(name);
	}

	public void addConstructor(FunctionParameters parameters, Constructor constructor)
	{
		constructorMap.put(parameters, constructor);
	}

	public Constructor getFunction(String name, Type[] types)
	{
		if (ExpressionParser.DEBUG)
			System.out.println("Looking for function with name '" + name + "' and types " + Arrays.toString(types));

//		System.out.println("\n\n\n");
//		System.out.println("Looking for function with name '" + name + "' and types " + Arrays.toString(types));
//		System.out.println(constructorMap);
//		System.out.println("\n\n\n");

		main: for (Map.Entry<FunctionParameters, Constructor> e : constructorMap.entrySet())
		{
			FunctionParameters parameters = e.getKey();
			Constructor constructor = e.getValue();

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
			if (ExpressionParser.DEBUG)
				System.out.println("Found function " + parameters + " " + constructor);
			return constructor;
		}

		Type type = typeMap.get(name);

		if (type == null)
			throw new RuntimeException("Type with name '" + name + "' not found!");

		if (ExpressionParser.DEBUG)
			System.out.println("Found type " + type);

		main: for (Map.Entry<FunctionParameters, Constructor> entry : type.getConstructors().entrySet())
		{
			FunctionParameters k = entry.getKey();
			Constructor v = entry.getValue();
			if (!k.getName().equals(name))
				continue;
			if (k.getTypes().length != types.length)
				continue;
			for (int i = 0; i < types.length; i++)
			{
				if (k.getTypes()[i] != types[i])
				{
					continue main;
				}
			}
			if (ExpressionParser.DEBUG)
				System.out.println("Found function " + k + " " + v);
			return v;
		}
		throw new RuntimeException("Function with name " + name + " and types " + Arrays.toString(types) + " not found!");
	}
}
