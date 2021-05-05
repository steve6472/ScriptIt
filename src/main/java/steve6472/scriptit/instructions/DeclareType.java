package steve6472.scriptit.instructions;

import steve6472.scriptit.*;
import steve6472.scriptit.expression.FunctionParameters;
import steve6472.scriptit.expression.Type;
import steve6472.scriptit.expression.Value;
import steve6472.scriptit.instructions.type.DeclareTypeConstructor;
import steve6472.scriptit.instructions.type.DeclareTypeValue;
import steve6472.scriptit.instructions.type.ThisAssignValue;

import java.util.ArrayList;
import java.util.List;

import static steve6472.scriptit.expression.Value.newValue;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/2/2021
 * Project: ScriptIt
 *
 ***********************/
public class DeclareType extends Instruction
{
	Script script;

	public DeclareType(Script parentScript, String line)
	{
		super(line);

		String[] split = line.split("\\s+|\\{{1}", 3);
		String keyword = split[1];
		String valueBody = line.substring(line.indexOf('{') + 1, line.length() - 2);

		if (Main.DEBUG)
		{
			System.out.println("keyword = " + keyword);
			System.out.println("valueBody = " + valueBody);
		}

		Type newType = new Type(keyword);

		Script tempScript = Main.createScript(valueBody, false, Main.typeCommandMap);
		parentScript.importTypesToScript(tempScript);



		List<Instruction> instructions = tempScript.instructions;

		List<Pair<String, Type>> valueMap = new ArrayList<>();


		for (Instruction ins : instructions)
		{
			if (ins instanceof DeclareTypeValue dec)
			{
				valueMap.add(new Pair<>(dec.name, tempScript.getType(dec.typeKeyword)));
			}

			if (ins instanceof DeclareTypeConstructor dec)
			{
				FunctionParameters.FunctionParametersBuilder paramsBuilder = FunctionParameters.constructor(newType);
				List<Pair<String, Type>> parameterMap = new ArrayList<>();

				for (int i = 0; i < dec.types.length; i++)
				{
					Type type = tempScript.getType(dec.types[i]);
					if (type == null)
					{
						throw new RuntimeException("Type '" + dec.types[i] + "' not found!");
					}
					parameterMap.add(new Pair<>(dec.names[i], type));
					paramsBuilder = paramsBuilder.addType(type);
				}

				Script constructorScript = Main.createScript(dec.body, true, Main.mainCommandMap, tempScript.getImportedTypes());

				newType.addConstructor(paramsBuilder.build(), args ->
				{
					if (args.length != dec.types.length)
					{
						throw new RuntimeException("Argument count is incorrect! Expected " + dec.types.length + ", got " + args.length);
					}

					for (int i = 0; i < args.length; i++)
					{
						Pair<String, Type> param = parameterMap.get(i);
						constructorScript.addValue(param.a(), newValue(param.b(), args[i]));
					}

					Value returnValue = newValue(newType);

					for (Instruction instruction : constructorScript.instructions)
					{
						if (instruction instanceof ThisAssignValue instruct)
						{
							instruct.thisValue = returnValue;
						}
					}

					constructorScript.run();

					return returnValue;
				});
			}
		}

		parentScript.importType(newType);
	}

	private Type findType(Script script, String keyword)
	{
		Type type = script.getType(keyword);

		if (type == null)
			throw new RuntimeException("Type '" + keyword + "' not found");

		return type;
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
