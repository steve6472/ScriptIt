package steve6472.scriptit;

import steve6472.scriptit.expression.Constructor;
import steve6472.scriptit.expression.FunctionParameters;
import steve6472.scriptit.expression.Type;
import steve6472.scriptit.expression.Value;

import java.util.HashMap;
import java.util.Map;

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

	public Map<FunctionParameters, Constructor> constructorMap = new HashMap<>();

	public Namespace()
	{
	}

	public void print()
	{
		System.out.println(typeMap);
		System.out.println(valueMap);
		System.out.println(constructorMap);
	}
}
