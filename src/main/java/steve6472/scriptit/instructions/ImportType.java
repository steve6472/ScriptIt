package steve6472.scriptit.instructions;

import steve6472.scriptit.Instruction;
import steve6472.scriptit.Script;
import steve6472.scriptit.TypeDeclarations;
import steve6472.scriptit.expression.Type;
import steve6472.scriptit.expression.Value;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/2/2021
 * Project: ScriptIt
 *
 ***********************/
public class ImportType extends Instruction
{
	Type typeToImport;

	public ImportType(Script script, String line)
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

		if (typeToImport != null)
			script.importType(typeToImport);
	}

	@Override
	public Value execute(Script script)
	{
		return null;
	}

	@Override
	public String toString()
	{
		return "ImportType{" + "typeToImport=" + typeToImport + '}';
	}
}
