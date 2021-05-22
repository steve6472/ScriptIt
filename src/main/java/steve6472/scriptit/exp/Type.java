package steve6472.scriptit.exp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/21/2021
 * Project: ScriptIt
 *
 ***********************/
public class Type
{
	private final String keyword;

	public HashMap<Type, HashMap<Operator, Function>> binary;
	public HashMap<Operator, Function> unary;

	public HashMap<FunctionParameters, Function> functions;

	public Type(String keyword)
	{
		this.keyword = keyword;
		this.binary = new HashMap<>();
		this.unary = new HashMap<>();
		this.functions = new HashMap<>();
	}

	public void addBinaryOperator(Type rightOperandType, Operator operator, Function function)
	{
		HashMap<Operator, Function> map = binary.get(rightOperandType);
		if (map == null)
		{
			map = new HashMap<>();
		}
		map.put(operator, function);
		binary.put(rightOperandType, map);
	}

	public void addUnaryOperator(Operator operator, Function function)
	{
		unary.put(operator, function);
	}

	public void addFunction(FunctionParameters parameters, Function function)
	{
		this.functions.put(parameters, function);
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
			throw new RuntimeException("Function '" + name + "' with argument types " + Arrays.toString(types) + " in type " + this + " not found!");

		return func;
	}

	public String getKeyword()
	{
		return keyword;
	}

	@Override
	public String toString()
	{
		return "Type{" + "keyword='" + keyword + '\'' + '}';
	}
}
