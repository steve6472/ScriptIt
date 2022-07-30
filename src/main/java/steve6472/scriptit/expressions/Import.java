package steve6472.scriptit.expressions;

import steve6472.scriptit.Highlighter;
import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.type.Type;
import steve6472.scriptit.libraries.Library;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
public class Import extends Expression
{
	private final ImportType type;
	private final String name;

	public Import(ImportType type, String name)
	{
		this.type = type;
		this.name = name;
	}

	@Override
	public Result apply(Script script)
	{
		if (type == ImportType.TYPE)
		{
			Type type = script.getWorkspace().getType(name);
			if (type == null)
			{
				throw new RuntimeException("Type '" + name + "' not found!");
			}
			script.getMemory().addType(type);
		} else
		{
			Library library = script.getWorkspace().getLibrary(name);
			if (type == null)
			{
				throw new RuntimeException("Library '" + name + "' not found!");
			}
			script.getMemory().addLibrary(library);
		}

		return Result.pass();
	}

	@Override
	public String toString()
	{
		return "Import{" + "type=" + type + ", name='" + name + '\'' + '}';
	}

	@Override
	public String showCode(int a)
	{
		return Highlighter.IMPORT + "import " + Highlighter.IMPORT_TYPE + type.name().toLowerCase() + " " + Highlighter.IMPORT_NAME + name + Highlighter.RESET;
	}
}
