package steve6472.scriptit.commands;

import steve6472.scriptit.Command;
import steve6472.scriptit.Script;
import steve6472.scriptit.TypeDeclarations;
import steve6472.scriptit.expression.Type;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/2/2021
 * Project: ScriptIt
 *
 ***********************/
public class ImportType extends Command
{
	Type typeToImport;

	public ImportType(String line)
	{
		super(line);

		String keyword = line.split("\s+")[1];

		for (Type t : TypeDeclarations.BASIC_TYPES)
		{
			if (t.getKeyword().equals(keyword))
			{
				typeToImport = t;
				break;
			}
		}
	}

	@Override
	public void execute(Script script)
	{
		script.namespace.importType(typeToImport);
	}
}
