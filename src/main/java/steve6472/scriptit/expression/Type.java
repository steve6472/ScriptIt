package steve6472.scriptit.expression;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Type
{
	private final String keyword;

	public HashMap<Type, OperatorOverloadFunction> addFunctions, subFunctions, mulFunctions, divFunctions, powFunctions;
	public HashMap<Type, UnaryOperatorOverloadFunction> unaryAddFunctions, unarySubFunctions, unaryMulFunctions, unaryDivFunctions, unaryPowFunctions, unaryNegFunctions, unaryNotFunctions;

	private HashMap<FunctionParameters, Constructor> constructors;
	private HashMap<FunctionParameters, Function> functions;

	public Type(String keyword)
	{
		this.keyword = keyword;

		addFunctions = new HashMap<>();
		subFunctions = new HashMap<>();
		mulFunctions = new HashMap<>();
		divFunctions = new HashMap<>();
		powFunctions = new HashMap<>();

		unaryAddFunctions = new HashMap<>();
		unarySubFunctions = new HashMap<>();
		unaryMulFunctions = new HashMap<>();
		unaryDivFunctions = new HashMap<>();
		unaryPowFunctions = new HashMap<>();
		unaryNegFunctions = new HashMap<>();
		unaryNotFunctions = new HashMap<>();

		functions = new HashMap<>();
		constructors = new HashMap<>();
	}

//	public void importIntoParser(ExpressionParser parser)
//	{
//		constructors.forEach(parser::addConstructor);
//	}

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
		if (ExpressionParser.DEBUG)
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

	public void addAddFunction(Type rightOperand, OperatorOverloadFunction function)
	{
		addFunctions.put(rightOperand, function);
	}

	public void addSubFunction(Type rightOperand, OperatorOverloadFunction function)
	{
		subFunctions.put(rightOperand, function);
	}

	public void addMulFunction(Type rightOperand, OperatorOverloadFunction function)
	{
		mulFunctions.put(rightOperand, function);
	}

	public void addDivFunction(Type rightOperand, OperatorOverloadFunction function)
	{
		divFunctions.put(rightOperand, function);
	}

	public void addPowFunction(Type rightOperand, OperatorOverloadFunction function)
	{
		powFunctions.put(rightOperand, function);
	}

	@Override
	public String toString()
	{
		return "Type{" + "keyword='" + keyword + '\'' + '}';
	}
}