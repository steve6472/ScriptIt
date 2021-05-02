package steve6472.scriptit.commands;

import steve6472.scriptit.Command;
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
public class DeclareValue extends Command
{
	String name;
	Type type;

	public DeclareValue(String line)
	{
		super(line);

		String[] split = line.split("\s+");
		String keyword = split[0];
		name = split[1];

		for (Type t : TypeDeclarations.BASIC_TYPES)
		{
			if (t.getKeyword().equals(keyword))
			{
				type = t;
				break;
			}
		}
	}

	@Override
	public void execute(Script script)
	{
		script.namespace.addValue(name, new Value(type));
	}
}
