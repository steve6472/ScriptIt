package steve6472.scriptit.expressions;

import steve6472.scriptit.type.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/1/2021
 * Project: ScriptIt
 *
 ***********************/
public class FunctionParameters
{
	private final String name;
	private final Type[] types;

	private FunctionParameters(String name, Type... types)
	{
		this.name = name;
		this.types = types;
	}

	public String getName()
	{
		return name;
	}

	public Type[] getTypes()
	{
		return types;
	}

	/**
	 * Creates a function with name of Type that should return value with Type
	 * @param type type
	 * @return constructor
	 */
	public static FunctionParametersBuilder constructor(Type type)
	{
		return new FunctionParametersBuilder(type.getKeyword());
	}

	public static FunctionParametersBuilder function(String name)
	{
		return new FunctionParametersBuilder(name);
	}

	public static FunctionParameters create(String name, Type... types)
	{
		return new FunctionParameters(name, types);
	}

	public static FunctionParameters create(Type type, Type... types)
	{
		return new FunctionParameters(type.getKeyword(), types);
	}

	@Override
	public String toString()
	{
		return "FunctionParameters{" + "name='" + name + '\'' + ", types=" + Arrays.toString(types) + '}';
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		FunctionParameters that = (FunctionParameters) o;
		return Objects.equals(name, that.name) && Arrays.equals(types, that.types);
	}

	@Override
	public int hashCode()
	{
		int result = Objects.hash(name);
		result = 31 * result + Arrays.hashCode(types);
		return result;
	}

	public static class FunctionParametersBuilder
	{
		private final List<Type> typeList;
		private final String name;

		private FunctionParametersBuilder(String name)
		{
			this.name = name;
			this.typeList = new ArrayList<>();
		}

		public FunctionParametersBuilder addType(Type type)
		{
			typeList.add(type);
			return this;
		}

		public FunctionParameters build()
		{
			return new FunctionParameters(name, typeList.toArray(new Type[0]));
		}
	}
}
