package steve6472.scriptit.type;

import steve6472.scriptit.Result;
import steve6472.scriptit.expressions.Function;
import steve6472.scriptit.expressions.FunctionParameters;
import steve6472.scriptit.tokenizer.IOperator;
import steve6472.scriptit.tokenizer.Operator;
import steve6472.scriptit.value.DoubleValue;
import steve6472.scriptit.value.UniversalValue;
import steve6472.scriptit.value.Value;

import java.sql.Array;
import java.util.*;
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

	private Type arraySubtype;

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

	public Type createArraySubtype()
	{
		if (arraySubtype == null)
			this.arraySubtype = new ArraySubType(this);
		return this;
	}

	public Type getArraySubtype()
	{
		return arraySubtype;
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

	public final boolean isArray()
	{
		return this instanceof ArraySubType;
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

	public static Type getArraySuperType(Type type)
	{
		if (type instanceof ArraySubType ast)
			return ast.getSuperType();

		return null;
	}

	public Value createArray(List<Value> values)
	{
		Type arrayType = this.isArray() ? this : this.getArraySubtype();
		if (arrayType == null)
			throw new RuntimeException("Array type not valid!");

		return DoubleValue.newValue(arrayType, values, this);
	}

	private static final class ArraySubType extends PrimitiveType<DoubleValue<List<Value>, Type>>
	{
		Type superType;

		public ArraySubType(Type type)
		{
			super(type.getKeyword() + Operator.INDEX.getSymbol());

			superType = type;

			addBinaryOperator(PrimitiveTypes.INT, Operator.INDEX, new TypesInit.PBinaryOperatorOverload<>((arr, i) -> arr.getFirst().get(i.getInt())));
			addConstructor(FunctionParameters.constructor(this).build(), new TypesInit.Constructor((c) -> {throw new RuntimeException("no array constructor for you");}));

			TypesInit.addFunction(this, "getTypeKeyword", (itself) -> TypesInit.newPrimitive(PrimitiveTypes.STRING, superType.getKeyword()));
			TypesInit.addFunction(this, "len", (itself) -> TypesInit.newPrimitive(PrimitiveTypes.INT, itself.as(this).getFirst().size()));


			/*
			addFunction(this, "getType", (itself) ->
			{
				// return new type -> PrimitiveType<SingleValue<Type>> TYPE
				// TYPE.constructor(STRING, text -> memory.findType(text));
			});*/
		}

		public Type getSuperType()
		{
			return superType;
		}

		@Override
		public Type createArraySubtype()
		{
			throw new RuntimeException("Can not create multidimensional arrays! (I am too lazy to add that)");
		}
	}
}
