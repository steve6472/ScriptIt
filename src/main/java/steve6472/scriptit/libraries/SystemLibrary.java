package steve6472.scriptit.libraries;

import steve6472.scriptit.*;
import steve6472.scriptit.types.PrimitiveTypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/22/2021
 * Project: ScriptIt
 *
 ***********************/
public class SystemLibrary extends Library
{
	public SystemLibrary()
	{
		super("System");

		addFunction(FunctionParameters.create("print", PrimitiveTypes.NULL), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				System.out.print(arguments[0]);
				return Result.pass();
			}
		});

		addFunction(FunctionParameters.create("println", PrimitiveTypes.NULL), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				System.out.println(arguments[0]);
				return Result.pass();
			}
		});

		addFunction(FunctionParameters.function("printDetail").addType(PrimitiveTypes.NULL).build(), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				System.out.println(arguments[0] + ", hash: " + arguments[0].hashCode());
				return Result.pass();
			}
		});

		addFunction(FunctionParameters.function("input").build(), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

				String value = null;
				try
				{
					value = reader.readLine();
				} catch (IOException e)
				{
					e.printStackTrace();
				}

				if (value != null)
					return Result.value(Value.newValue(PrimitiveTypes.STRING, value));
				else
					return Result.value(Value.NULL);
			}
		});

		addFunction(FunctionParameters.create("createChar", PrimitiveTypes.INT), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				return Result.value(Value.newValue(PrimitiveTypes.CHAR, (char) arguments[0].getInt()));
			}
		});

		addFunction(FunctionParameters.create("dumpVariables"), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				script.getMemory().dumpVariables();
				return Result.pass();
			}
		});

		addFunction(FunctionParameters.create("dumpFunctions"), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				script.getMemory().dumpFunctions();
				return Result.pass();
			}
		});

		addFunction("timeMs", () -> Value.newValue(PrimitiveTypes.INT, (int) System.currentTimeMillis()));

		addFunction(FunctionParameters.create("acceptEvents"), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				script.runQueuedFunctions();
				return Result.pass();
			}
		});

		addFunction(FunctionParameters.create("waitForEvents"), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				return Result.waitForEvents();
			}
		});
	}
}
