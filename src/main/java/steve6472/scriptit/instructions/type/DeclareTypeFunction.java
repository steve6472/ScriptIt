package steve6472.scriptit.instructions.type;

import steve6472.scriptit.Script;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/2/2021
 * Project: ScriptIt
 *
 ***********************/
public class DeclareTypeFunction extends UncallableInstruction
{
	public final String functionName;
	public final String returnType;
	public final String[] names;
	public final String[] types;
	public final String body;

	public DeclareTypeFunction(Script parentScript, String line)
	{
		super(line);

		String[] mainSplit = line.split("\\)", 2);
		String functionDeclaration = mainSplit[0];
		String tempBody = mainSplit[1].trim();
		body = tempBody.substring(1, tempBody.length() - 1); // remove first '{' and last '}'

		String[] split = functionDeclaration.split("\s+", 3);
		returnType = split[1];
		String signature = split[2];

		String[] nameSplit = signature.split("\\(", 2);
		functionName = nameSplit[0];
		String[] parameters = nameSplit[1].split("\s*,\s*");

		if (parameters.length == 1)
			if (parameters[0].isBlank())
				parameters = new String[0];

		names = new String[parameters.length];
		types = new String[parameters.length];

		for (int i = 0; i < parameters.length; i++)
		{
			String param = parameters[i];
			String[] paramSplit = param.split("\\s+");
			names[i] = paramSplit[1];
			types[i] = paramSplit[0];
		}
	}
}
