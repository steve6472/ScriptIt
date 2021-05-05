package steve6472.scriptit.instructions.type;

import steve6472.scriptit.Script;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/2/2021
 * Project: ScriptIt
 *
 ***********************/
public class DeclareTypeValue extends UncallableInstruction
{
	public final String name;
	public final String typeKeyword;

	public DeclareTypeValue(Script script, String line)
	{
		super(line);

		String[] split = line.split("\s+");
		typeKeyword = split[0];
		name = split[1];
	}
}
