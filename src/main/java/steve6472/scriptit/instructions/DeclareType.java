package steve6472.scriptit.instructions;

import steve6472.scriptit.*;
import steve6472.scriptit.expression.FunctionParameters;
import steve6472.scriptit.expression.Type;
import steve6472.scriptit.expression.Value;
import steve6472.scriptit.instructions.type.*;

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

	public DeclareType(Workspace workspace, Script parentScript, String line)
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

		Script tempScript = Main.createScript(workspace, valueBody, false, Main.typeCommandMap);
		parentScript.importTypesToScript(tempScript);
		tempScript.importType(newType);

		for (Instruction ins : tempScript.instructions)
		{
			if (ins instanceof DeclareTypeValue dec)
			{
				Type type = tempScript.getType(dec.typeKeyword);

				if (dec.accessModifier == AccessModifier.PUBLIC)
				{
					newType.addProcedure(FunctionParameters
						.function("set" + Character.toUpperCase(dec.name.charAt(0)) + dec.name.substring(1))
						.addType(type)
						.build(), (itself, args) -> itself.setValue(dec.name, args[0]));
					newType.addFunction(FunctionParameters
						.function("get" + Character.toUpperCase(dec.name.charAt(0)) + dec.name.substring(1))
						.build(), (itself, args) -> itself.getValue(dec.name));
				}
			}

			if (ins instanceof DeclareOperatorOverload dec)
			{
				Type rightOperand = tempScript.getType(dec.types[0]);
				String rightOperandName = dec.names[0];

				Script functionScript = Main.createScript(workspace, dec.body, true, Main.mainCommandMap, tempScript.getImportedTypes(), parentScript.getConstructorMap());

				newType.addBinaryOperator(rightOperand, dec.operator, (left, right) ->
				{
					// load values of itself into the script
					left.values.forEach((k, v) -> functionScript.addValue(k, (Value) v));
					functionScript.addValue(rightOperandName, right);

					editInstructions(functionScript.instructions, left);

					Value result = functionScript.run();
					if (result == null)
						throw new RuntimeException("Function didn't return anything!");
					if (result.type != newType)
						throw new RuntimeException("Function return incorrect type!");

					return result;
				});
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

				Script constructorScript = Main.createScript(workspace, dec.body, true, Main.mainCommandMap, tempScript.getImportedTypes(), parentScript.getConstructorMap());

				newType.addConstructor(paramsBuilder.build(), args ->
				{
					if (args.length != dec.types.length)
					{
						throw new RuntimeException("Argument count is incorrect! Expected " + dec.types.length + ", got " + args.length);
					}

					for (int i = 0; i < args.length; i++)
					{
						Pair<String, Type> param = parameterMap.get(i);
						constructorScript.addValue(param.a(), args[i]);
					}

					Value returnValue = newValue(newType);

					editInstructions(constructorScript.instructions, returnValue);

					constructorScript.run();

					return returnValue;
				});
			}

			if (ins instanceof DeclareTypeFunction dec)
			{
				FunctionParameters.FunctionParametersBuilder paramsBuilder = FunctionParameters.function(dec.functionName);
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

				Type returnType;

				Script functionScript = Main.createScript(workspace, dec.body, true, Main.mainCommandMap, tempScript.getImportedTypes(), parentScript.getConstructorMap());

				if (dec.returnType.equals("void"))
				{
					newType.addProcedure(paramsBuilder.build(), (itself, args) ->
					{
						// load values of itself into the script
						itself.values.forEach((k, v) -> functionScript.addValue(k, (Value) v));

						if (args.length != dec.types.length)
						{
							throw new RuntimeException("Argument count is incorrect! Expected " + dec.types.length + ", got " + args.length);
						}

						for (int i = 0; i < args.length; i++)
						{
							Pair<String, Type> param = parameterMap.get(i);
							functionScript.addValue(param.a(), args[i]);
						}

						editInstructions(functionScript.instructions, itself);

						functionScript.run();
					});
				} else
				{
					returnType = tempScript.getType(dec.returnType);

					newType.addFunction(paramsBuilder.build(), (itself, args) ->
					{
						// load values of itself into the script
						itself.values.forEach((k, v) -> functionScript.addValue(k, (Value) v));

						if (args.length != dec.types.length)
						{
							throw new RuntimeException("Argument count is incorrect! Expected " + dec.types.length + ", got " + args.length);
						}

						for (int i = 0; i < args.length; i++)
						{
							Pair<String, Type> param = parameterMap.get(i);
							functionScript.addValue(param.a(), args[i]);
						}

						editInstructions(functionScript.instructions, itself);

						Value result = functionScript.run();
						if (result == null)
							throw new RuntimeException("Function didn't return anything!");
						if (result.type != returnType)
							throw new RuntimeException("Function return incorrect type!");

						return result;
					});
				}
			}
		}

		parentScript.importType(newType);
	}

	private void editInstructions(List<Instruction> instructions, Value itself)
	{
		for (Instruction instruction : instructions)
		{
			if (instruction instanceof ThisAssignValue instruct)
			{
				instruct.thisValue = itself;
			}
			if (instruction instanceof ReturnTypeThisValue instruct)
			{
				instruct.thisValue = itself;
			}
		}
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
