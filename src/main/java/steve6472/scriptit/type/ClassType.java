package steve6472.scriptit.type;

import steve6472.scriptit.expressions.Function;
import steve6472.scriptit.expressions.FunctionParameters;
import steve6472.scriptit.transformer.JavaValue;
import steve6472.scriptit.value.Value;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by steve6472
 * Date: 8/25/2022
 * Project: ScriptIt
 */
public class ClassType extends Type
{
	public final Class<?> clazz;

	public ClassType(String keyword, Class<?> clazz)
	{
		super(keyword);
		this.clazz = clazz;
	}

	public ClassType(String keyword, Class<?> clazz, Supplier<Value> uninitValue)
	{
		super(keyword, uninitValue);
		this.clazz = clazz;
	}

	/*
	 * Hack in inheritance
	 */
	@Override
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
				if (types[i] instanceof ClassType ct && parameters.getTypes()[i] instanceof ClassType ctp)
				{
					if (ctp.clazz.isAssignableFrom(ct.clazz))
						continue;
				}
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

	@Override
	public String toString()
	{
		return "ClassType{" + "clazz=" + clazz + ", keyword='" + getKeyword() + '\'' + '}';
	}
}
