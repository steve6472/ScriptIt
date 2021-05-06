package steve6472.scriptit.instructions.type;

import steve6472.scriptit.AccessModifier;
import steve6472.scriptit.Script;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/2/2021
 * Project: ScriptIt
 *
 ***********************/
public class DeclareTypeValue extends UncallableInstruction
{
	public final AccessModifier accessModifier;
	public final String name;
	public final String typeKeyword;

	public DeclareTypeValue(Script script, String line)
	{
		super(line);

		String[] split = line.split("\s+");
		if (split.length == 3)
		{
			accessModifier = switch (split[0])
				{
					case "public" -> AccessModifier.PUBLIC;
					case "private" -> AccessModifier.PRIVATE;
					default -> throw new IllegalStateException("Unexpected value: " + split[0]);
				};
			typeKeyword = split[1];
			name = split[2];
		} else
		{
			typeKeyword = split[0];
			name = split[1];
			accessModifier = AccessModifier.PRIVATE;
		}
	}
}
