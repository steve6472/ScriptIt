package steve6472.scriptit.type;

import steve6472.scriptit.expressions.Function;
import steve6472.scriptit.expressions.FunctionParameters;
import steve6472.scriptit.tokenizer.IOperator;
import steve6472.scriptit.value.UniversalValue;
import steve6472.scriptit.value.Value;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/21/2021
 * Project: ScriptIt
 *
 ***********************/
public class Type
{
	private final String keyword;

	public HashMap<Type, HashMap<IOperator, Function>> binary;
	public HashMap<IOperator, Function> unary;

	public HashMap<FunctionParameters, Function> functions;
	public HashMap<FunctionParameters, Function> constructors;

	private Supplier<Value> uninitValue;

	public Type(String keyword)
	{
		this.keyword = keyword;
		this.binary = new HashMap<>();
		this.unary = new HashMap<>();
		this.functions = new HashMap<>();
		this.constructors = new HashMap<>();
		uninitValue = () -> UniversalValue.newValue(this);
	}

	/**
	 *
	 * @param keyword keyword
	 * @param uninitValue gets created when assignment does not supply any value : <code>int a;</code>
	 */
	public Type(String keyword, Supplier<Value> uninitValue)
	{
		this(keyword);
		this.uninitValue = uninitValue;
	}

	public void addBinaryOperator(Type rightOperandType, IOperator operator, Function function)
	{
		HashMap<IOperator, Function> map = binary.get(rightOperandType);
		if (map == null)
		{
			map = new HashMap<>();
		}
		map.put(operator, function);
		binary.put(rightOperandType, map);
	}

	public void addUnaryOperator(IOperator operator, Function function)
	{
		unary.put(operator, function);
	}

	public void addFunction(FunctionParameters parameters, Function function)
	{
		this.functions.put(parameters, function);
	}

	public void addConstructor(FunctionParameters parameters, Function function)
	{
		this.constructors.put(parameters, function);
	}

	public Type setUninitValue(Supplier<Value> uninitValue)
	{
		this.uninitValue = uninitValue;
		return this;
	}

	public Value uninitValue()
	{
		return uninitValue.get();
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
				if (parameters.getTypes()[i] != PrimitiveTypes.NULL && parameters.getTypes()[i] != types[i])
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

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Type type = (Type) o;
		return Objects.equals(keyword, type.keyword);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(keyword);
	}
}
