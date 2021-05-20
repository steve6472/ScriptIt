package steve6472.scriptit.instructions.type;

import steve6472.scriptit.Script;
import steve6472.scriptit.exp.Operator;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/2/2021
 * Project: ScriptIt
 *
 ***********************/
public class DeclareOperatorOverload extends UncallableInstruction
{
	public final String[] names;
	public final String[] types;
	public final String body;
	public final Operator operator;

	public DeclareOperatorOverload(Script parentScript, String line)
	{
		super(line);

		String[] mainSplit = line.split("\\)", 2);
//		System.out.println("mainSplit = " + Arrays.toString(mainSplit));
		String parametersString = mainSplit[0].split("\\(", 2)[1];
//		System.out.println("parametersString = " + parametersString);
		String tempBody = mainSplit[1].trim();
		body = tempBody.substring(1, tempBody.length() - 1).trim(); // remove first '{' and last '}'
//		System.out.println("body = " + body);
		String operator = mainSplit[0].split("\\(", 2)[0].replace("operator", "").trim();
//		System.out.println("operator = " + operator);

		Operator foundOperator = null;

		for (Operator op : Operator.values())
		{
			if (op.getOperator().equals(operator))
			{
				foundOperator = op;
				break;
			}
		}

		if (foundOperator == null)
			throw new IllegalArgumentException("Operator '" + operator + "' is not supported!");

		this.operator = foundOperator;

		String[] parameters = parametersString.split("\\s*,\\s*");

		if (parameters.length == 1)
			if (parameters[0].isBlank())
				parameters = new String[0];

//		System.out.println("parameters = " + Arrays.toString(parameters));

		names = new String[parameters.length];
		types = new String[parameters.length];

		for (int i = 0; i < parameters.length; i++)
		{
			String param = parameters[i];
			String[] paramSplit = param.split("\\s+");
			names[i] = paramSplit[1];
			types[i] = paramSplit[0];
		}

//		System.out.println("names = " + Arrays.toString(names));
//		System.out.println("types = " + Arrays.toString(types));
	}
}
