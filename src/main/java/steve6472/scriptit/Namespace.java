package steve6472.scriptit;

import steve6472.scriptit.expression.Type;
import steve6472.scriptit.expression.Value;

import java.util.HashMap;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/2/2021
 * Project: ScriptIt
 *
 ***********************/
public class Namespace
{
	public HashMap<String, Type> typeMap = new HashMap<>();
	public HashMap<String, Value> valueMap = new HashMap<>();

	public void importType(Type type)
	{
		typeMap.put(type.getKeyword(), type);
	}

	public boolean isType(String name)
	{
		return typeMap.containsKey(name);
	}

	public void addValue(String name, Value value)
	{
		valueMap.put(name, value);
	}

	public Value getValue(String name)
	{
		return valueMap.get(name);
	}
}
