package steve6472.scriptit.type;

import steve6472.scriptit.value.Value;

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
}
