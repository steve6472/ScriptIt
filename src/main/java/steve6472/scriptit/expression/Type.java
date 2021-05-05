package steve6472.scriptit.expression;

import steve6472.scriptit.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Type
{
	private final String keyword;

	public HashMap<Type, HashMap<Operator, OperatorOverloadFunction>> binary;
	public HashMap<Type, HashMap<Operator, UnaryOperatorOverloadFunction>> unary;

	private final HashMap<FunctionParameters, Constructor> constructors;
	private final HashMap<FunctionParameters, Function> functions;

	public Type(String keyword)
	{
		this.keyword = keyword;

		binary = new HashMap<>();
		unary = new HashMap<>();

		functions = new HashMap<>();
		constructors = new HashMap<>();

		addProcedure(FunctionParameters.function("_printAllValues").build(), (itself, args) ->
		{
			System.out.println(Log.CYAN + itself + Log.RESET);
			itself.values.forEach((n, v) ->
			{
				if (n.equals(Value.SINGLE_VALUE))
				{
					System.out.println("VALUE=" + v);
				} else
				{
					System.out.println(n + "=" + v);
				}
			});
		});
	}

	public HashMap<FunctionParameters, Constructor> getConstructors()
	{
		return constructors;
	}

	public void addConstructor(FunctionParameters parameters, Constructor constructor)
	{
		constructors.put(parameters, constructor);
	}

	public void addFunction(FunctionParameters parameters, Function function)
	{
		functions.put(parameters, function);
	}

	/**
	 * Hack procedure into function meaning procedure returns itself
	 */
	public void addProcedure(FunctionParameters parameters, Procedure procedure)
	{
		addFunction(parameters, ((itself, args) -> {
			procedure.apply(itself, args);
			return itself;
		}));
	}

	public Function getFunction(String name, Type[] types)
	{
		if (ExpressionParser.PARSE_DEBUG)
			System.out.println("Looking for function in type '" + keyword + "' with name '" + name + "' and types " + Arrays.toString(types));

		main: for (Map.Entry<FunctionParameters, Function> entry : functions.entrySet())
		{
			FunctionParameters k = entry.getKey();
			// if map is constructors skip name check as all constructors have the same name
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
			return entry.getValue();
		}
		return null;
	}

	public String getKeyword()
	{
		return keyword;
	}

	public void addBinaryOperator(Type rightOperandType, Operator operator, OperatorOverloadFunction function)
	{
		HashMap<Operator, OperatorOverloadFunction> map = binary.get(rightOperandType);
		if (map == null)
		{
			map = new HashMap<>();
		}
		map.put(operator, function);
		binary.put(rightOperandType, map);
	}

	public void addUnaryOperator(Operator operator, UnaryOperatorOverloadFunction function)
	{
		HashMap<Operator, UnaryOperatorOverloadFunction> map = unary.get(this);
		if (map == null)
		{
			map = new HashMap<>();
		}
		map.put(operator, function);
		unary.put(this, map);
	}

	@Override
	public String toString()
	{
		return "Type{" + "keyword='" + keyword + '\'' + '}';
	}
}