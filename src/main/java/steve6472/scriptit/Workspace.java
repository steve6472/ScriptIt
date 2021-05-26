package steve6472.scriptit;

import steve6472.scriptit.libraries.Library;
import steve6472.scriptit.libraries.MathLibrary;
import steve6472.scriptit.libraries.RandomLibrary;
import steve6472.scriptit.libraries.SystemLibrary;
import steve6472.scriptit.types.PrimitiveTypes;

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
		addLibrary(new RandomLibrary());

		addType(PrimitiveTypes.DOUBLE);
		addType(PrimitiveTypes.INT);
		addType(PrimitiveTypes.BOOL);
		addType(PrimitiveTypes.STRING);
		addType(PrimitiveTypes.CHAR);
		addType(PrimitiveTypes.NULL);
	}

	public void addLibrary(Library library)
	{
		this.libraries.put(library.getLibraryName(), library);
	}

	public void removeLibrary(String name)
	{
		this.libraries.remove(name);
	}

	public Library getLibrary(String name)
	{
		return libraries.get(name);
	}

	public void addType(Type type)
	{
		this.types.put(type.getKeyword(), type);
	}

	public void removeType(String name)
	{
		this.types.remove(name);
	}

	public Type getType(String name)
	{
		return this.types.get(name);
	}
}
