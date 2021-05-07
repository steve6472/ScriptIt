package steve6472.scriptit;

import steve6472.scriptit.expression.ClassProcedure;
import steve6472.scriptit.expression.Constructor;
import steve6472.scriptit.expression.FunctionParameters;
import steve6472.scriptit.expression.Type;

import java.util.HashMap;
import java.util.Map;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/7/2021
 * Project: ScriptIt
 *
 ***********************/
public class Workspace
{
	private final Map<String, Type> types;
	private final Map<String, Map<FunctionParameters, Constructor>> functions;

	public Workspace()
	{
		types = new HashMap<>();
		functions = new HashMap<>();
	}

	public void addType(Type type)
	{
		types.put(type.getKeyword(), type);
	}

	public Type getType(String keyword)
	{
		return types.get(keyword);
	}

	public void addProcedure(String libName, FunctionParameters parameters, ClassProcedure procedure)
	{
		Map<FunctionParameters, Constructor> map = functions.get(libName);
		if (map == null)
			map = new HashMap<>();

		map.put(parameters, a ->
		{
			procedure.apply(a);
			return null;
		});
		functions.put(libName, map);
	}

	public void addConstructor(String libName, FunctionParameters parameters, Constructor constructor)
	{
		Map<FunctionParameters, Constructor> map = functions.get(libName);
		if (map == null)
			map = new HashMap<>();

		map.put(parameters, constructor);

		functions.put(libName, map);
	}

	public Map<FunctionParameters, Constructor> getFunctions(String name)
	{
		return functions.get(name);
	}
}
