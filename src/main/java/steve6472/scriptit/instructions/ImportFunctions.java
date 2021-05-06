package steve6472.scriptit.instructions;

import steve6472.scriptit.ImportableFunctions;
import steve6472.scriptit.Instruction;
import steve6472.scriptit.Script;
import steve6472.scriptit.expression.Type;
import steve6472.scriptit.expression.Value;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/2/2021
 * Project: ScriptIt
 *
 ***********************/
public class ImportFunctions extends Instruction
{
	Type typeToImport;

	public ImportFunctions(Script script, String line)
	{
		super(line);

		String functions = line.split("\s+")[2];

		ImportableFunctions.importFunctions(script, functions);
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
