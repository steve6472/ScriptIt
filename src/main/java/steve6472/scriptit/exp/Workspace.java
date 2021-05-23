package steve6472.scriptit.exp;

import steve6472.scriptit.exp.libraries.Library;
import steve6472.scriptit.exp.libraries.MathLibrary;
import steve6472.scriptit.exp.libraries.SystemLibrary;
import steve6472.scriptit.exp.types.PrimitiveTypes;

import java.util.HashMap;
import java.util.Map;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/23/2021
 * Project: ScriptIt
 *
 ***********************/
public class Workspace
{
	private final Map<String, Library> libraries = new HashMap<>();
	private final Map<String, Type> types = new HashMap<>();

	public Workspace()
	{
		addLibrary(new MathLibrary());
		addLibrary(new SystemLibrary());

		addType(PrimitiveTypes.DOUBLE);
		addType(PrimitiveTypes.INT);
		addType(PrimitiveTypes.BOOL);
		addType(PrimitiveTypes.NULL);
	}

	public void addLibrary(Library library)
	{
		this.libraries.put(library.getLibraryName(), library);
	}

	public Library getLibrary(String name)
	{
		return libraries.get(name);
	}

	public void addType(Type type)
	{
		this.types.put(type.getKeyword(), type);
	}

	public Type getType(String name)
	{
		return this.types.get(name);
	}
}
