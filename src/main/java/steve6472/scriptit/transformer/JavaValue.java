package steve6472.scriptit.transformer;

import steve6472.scriptit.type.Type;
import steve6472.scriptit.value.UniversalValue;
import steve6472.scriptit.value.Value;

/**
 * Created by steve6472
 * Date: 9/2/2022
 * Project: ScriptIt
 */
public class JavaValue extends UniversalValue
{
	protected JavaValue(Type type)
	{
		super(type);
	}

	@Override
	public Value getValueByName(String name)
	{
		try
		{
			return JavaTransformer.transformObject(values.get(name));
		} catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}
}
