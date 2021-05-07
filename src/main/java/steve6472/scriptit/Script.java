package steve6472.scriptit;

import steve6472.scriptit.expression.*;

import java.util.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/2/2021
 * Project: ScriptIt
 *
 ***********************/
public class Script
{
	private final HashMap<String, Type> typeMap = new HashMap<>();
	private final HashMap<String, Value> valueMap = new HashMap<>();
	private final Map<FunctionParameters, Constructor> constructorMap = new HashMap<>();

	public List<Instruction> instructions;

	private Script parent;

	public Script()
	{
		instructions = new ArrayList<>();

		// Force true and false booleans into namespace
		addValue("true", TypeDeclarations.TRUE);
		addValue("false", TypeDeclarations.FALSE);
	}

	@SuppressWarnings("IncompleteCopyConstructor")
	public Script(Script parent)
	{
		instructions = new ArrayList<>();
		this.parent = parent;
	}

	public void print()
	{
		System.out.println(typeMap);
		System.out.println(valueMap);
		System.out.println(constructorMap);
	}

	public Map<FunctionParameters, Constructor> getConstructorMap()
	{
		return constructorMap;
	}

	public Value run()
	{
		for (Instruction c : instructions)
		{
			Value execute = c.execute(this);
			if (execute != null)
				return execute;
		}

		return null;
	}

	public Value runDebug()
	{
		for (Instruction c : instructions)
		{
			System.out.println(c.toString());
			Value execute = c.execute(this);
			System.out.println("\nNamespace: ");
			print();
			if (execute != null)
				return execute;
		}

		return null;
	}

	public void importTypesToScript(Script dest)
	{
		typeMap.forEach((name, type) -> dest.importType(type));
		if (parent != null)
			parent.importTypesToScript(dest);
	}

	public List<Type> getImportedTypes()
	{
		List<Type> types = new ArrayList<>(typeMap.values());
		if (parent != null)
			types.addAll(parent.getImportedTypes());
		return types;
	}

	public void printCode()
	{
		for (Instruction c : instructions)
		{
			System.out.println(c.toString());
		}
	}

	public void importType(Type type)
	{
		typeMap.put(type.getKeyword(), type);
	}

	public Type getType(String name)
	{
		Type type = typeMap.get(name);
		if (type == null && parent != null)
			return parent.getType(name);
		return type;
	}

	public void addValue(String name, Value value)
	{
		valueMap.put(name, value);
	}

	public Value getValue(String name)
	{
		Value value = valueMap.get(name);
		if (value == null && parent != null)
			return parent.getValue(name);
		return value;
	}

	public void addConstructor(FunctionParameters parameters, Constructor constructor)
	{
		constructorMap.put(parameters, constructor);
	}

	public void addProcedure(FunctionParameters parameters, ClassProcedure procedure)
	{
		constructorMap.put(parameters, a -> {
			procedure.apply(a);
			return null;
		});
	}

	public boolean hasValue(String name)
	{
		boolean b = valueMap.containsKey(name);
		if (!b && parent != null)
		{
			return parent.hasValue(name);
		}
		return b;
	}

	public void printConstructors()
	{
		if (parent != null)
			parent.printConstructors();
		System.out.println("");

		constructorMap.forEach((params, cons) ->
		{
			System.out.println(params);
		});
	}

	private Constructor findFunction(String name, Type[] types)
	{
		Constructor con = null;
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
			if (ExpressionParser.EVAL_DEBUG)
				System.out.println("Found function " + parameters + " " + constructor);
			con = constructor;
			break;
		}

		if (con == null && parent != null)
			return parent.findFunction(name, types);
		return con;
	}

	public Constructor getFunction(String name, Type[] types)
	{
		if (ExpressionParser.EVAL_DEBUG)
		{
			System.out.println("Looking for function with name '" + name + "' and types " + Arrays.toString(types));
			printConstructors();
		}

		//		System.out.println("\n\n\n");
		//		System.out.println("Looking for function with name '" + name + "' and types " + Arrays.toString(types));
		//		System.out.println("\n\n\n");

		Constructor c = findFunction(name, types);
		if (c != null)
			return c;

		Type type = getType(name);

		if (type == null)
			throw new RuntimeException("Type with name '" + name + "' not found!");

		if (ExpressionParser.EVAL_DEBUG)
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
			if (ExpressionParser.EVAL_DEBUG)
				System.out.println("Found function " + k + " " + v);
			return v;
		}

		if (parent != null)
		{
			return parent.getFunction(name, types);
		}

		throw new RuntimeException("Function with name " + name + " and types " + Arrays.toString(types) + " not found!");
	}
}
