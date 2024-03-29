package steve6472.scriptit;

import steve6472.scriptit.attributes.Attribute;
import steve6472.scriptit.libraries.Library;
import steve6472.scriptit.libraries.MathLibrary;
import steve6472.scriptit.libraries.RandomLibrary;
import steve6472.scriptit.libraries.SystemLibrary;
import steve6472.scriptit.transformer.parser.config.Config;
import steve6472.scriptit.type.PrimitiveTypes;
import steve6472.scriptit.type.Type;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**********************
 * Created by steve6472
 * On date: 5/23/2021
 * Project: ScriptIt
 *
 ***********************/
public class Workspace
{
	private final Map<String, Library> libraries = new HashMap<>();
	private final Map<String, Type> types = new HashMap<>();
	private final Map<String, Attribute> attributes = new HashMap<>();
	private final Map<String, List<Config>> transformers = new HashMap<>();

	public Workspace()
	{
		addLibrary(new MathLibrary());
		addLibrary(new SystemLibrary());
		addLibrary(new RandomLibrary());

		// Make fucking sure the static initializer runs
		// Arrays break without this
		PrimitiveTypes.TRUE();

		addType(PrimitiveTypes.DOUBLE);
		addType(PrimitiveTypes.FLOAT);
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

		if (type.getArraySubtype() != null)
			this.types.put(type.getArraySubtype().getKeyword(), type.getArraySubtype());
	}

	public Collection<Type> getTypes()
	{
		return types.values();
	}

	public void addTransformer(String name, List<Config> loadedConfig)
	{
		transformers.put(name, loadedConfig);
	}

	public List<Config> getTransformer(String name)
	{
		return transformers.get(name);
	}

	public void removeType(String name)
	{
		this.types.remove(name);
	}

	public Type getType(String name)
	{
		return this.types.get(name);
	}

	public void addAttribute(Attribute attribute)
	{
		this.attributes.put(attribute.getName(), attribute);
	}

	public Attribute getAttribute(String name)
	{
		return attributes.get(name);
	}
}
