package steve6472.scriptit.instructions;

import steve6472.scriptit.Instruction;
import steve6472.scriptit.Script;
import steve6472.scriptit.expression.Type;
import steve6472.scriptit.expression.Value;

import static steve6472.scriptit.expression.Value.newValue;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/2/2021
 * Project: ScriptIt
 *
 ***********************/
public class DeclareValue extends Instruction
{
	String name;
	Type type;

	public DeclareValue(Script script, String line)
	{
		super(line);

		String[] split = line.split("\s+");
		String keyword = split[0];
		name = split[1];

		type = script.getType(keyword);

		if (type == null)
			throw new RuntimeException("Type '" + keyword + "' not found");
	}

	@Override
	public Value execute(Script script)
	{
		script.addValue(name, newValue(type));
		return null;
	}

	@Override
	public String toString()
	{
		return "DeclareValue{" + "name='" + name + '\'' + ", type=" + type + '}';
	}
}
