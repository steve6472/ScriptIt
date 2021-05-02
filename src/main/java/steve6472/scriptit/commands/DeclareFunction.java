package steve6472.scriptit.commands;

import steve6472.scriptit.Command;
import steve6472.scriptit.Main;
import steve6472.scriptit.Script;
import steve6472.scriptit.expression.FunctionParameters;
import steve6472.scriptit.expression.Type;
import steve6472.scriptit.expression.Value;

import java.util.Arrays;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/2/2021
 * Project: ScriptIt
 *
 ***********************/
public class DeclareFunction extends Command
{
	Script script;

	public DeclareFunction(Script parentScript, String line)
	{
		super(line);

		String[] mainSplit = line.split("\\)", 2);
		String functionDeclaration = mainSplit[0];
		String body = mainSplit[1].trim();
		body = body.substring(1, body.length() - 1); // remove first '{' and last '}'

		String[] split = functionDeclaration.split("\s+", 3);
		String returnType = split[1];
		String signature = split[2];

		String[] nameSplit = signature.split("\\(", 2);
		String name = nameSplit[0];
		String[] parameters = nameSplit[1].split("\s*,\s*");

		if (parameters.length == 1)
			if (parameters[0].isBlank())
				parameters = new String[0];

		if (Main.DEBUG)
		{
			System.out.println("split = " + Arrays.toString(split));
			System.out.println("returnType = " + returnType);
			System.out.println("signature = " + signature);

			System.out.println("nameSplit = " + Arrays.toString(nameSplit));
			System.out.println("name = " + name);
			System.out.println("parameters = " + Arrays.toString(parameters));
			System.out.println(parameters.length);

			System.out.println("\nFunction");

			System.out.println("functionDeclaration = " + functionDeclaration);
			System.out.println("body = " + body);
		}

//		Type[] paramTypes = new Type[parameters.length];
		String[] paramNames = new String[parameters.length];

		FunctionParameters.FunctionParametersBuilder paramsBuilder = FunctionParameters.function(name);
		for (int i = 0; i < parameters.length; i++)
		{
			String param = parameters[i];
			String[] paramSplit = param.split("\s+");
			String paramType = paramSplit[0];
			String paramName = paramSplit[1];
			Type type = parentScript.namespace.getType(paramType);
			if (type == null)
				throw new RuntimeException("Type '" + paramType + "' not found!");
//			paramTypes[i] = type;
			paramNames[i] = paramName;
			paramsBuilder = paramsBuilder.addType(type);
		}

		if (Main.DEBUG)
		{
			System.out.println("paramNames = " + Arrays.toString(paramNames));

			System.out.println("\n".repeat(3));
			System.out.println("Function script: ");
		}
		script = Main.createScript(parentScript, body);
		if (Main.DEBUG)
		{
			System.out.println("Function script end");
			System.out.println("\n".repeat(3));
		}

		FunctionParameters functionParameters = paramsBuilder.build();

		if (Main.DEBUG)
			System.out.println("functionParameters = " + functionParameters);

		parentScript.namespace.addConstructor(functionParameters, (args) ->
		{
			for (int i = 0; i < args.length; i++)
			{
				script.namespace.addValue(paramNames[i], args[i]);
			}

			return script.run();
		});
	}

	@Override
	public Value execute(Script script)
	{
		return null;
	}

	@Override
	public String toString()
	{
		System.out.println("Function\n");
		script.printCode();
		return "\nDeclareFunction{" + "script=printed above}";
	}
}
