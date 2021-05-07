package steve6472.scriptit.instructions;

import steve6472.scriptit.Instruction;
import steve6472.scriptit.Script;
import steve6472.scriptit.Workspace;
import steve6472.scriptit.expression.Value;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/2/2021
 * Project: ScriptIt
 *
 ***********************/
public class ImportFunctions extends Instruction
{
	public ImportFunctions(Workspace workspace, Script script, String line)
	{
		super(line);

		String functions = line.split("\s+")[2];

		workspace.getFunctions(functions).forEach(script::addConstructor);
	}

	@Override
	public Value execute(Script script)
	{
		return null;
	}
}
