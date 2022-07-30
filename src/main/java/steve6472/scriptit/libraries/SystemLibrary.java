package steve6472.scriptit.libraries;

import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.expressions.Function;
import steve6472.scriptit.expressions.FunctionParameters;
import steve6472.scriptit.type.PrimitiveTypes;
import steve6472.scriptit.value.PrimitiveValue;
import steve6472.scriptit.value.Value;

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

		addFunction(FunctionParameters.function("isPrimitive").addType(PrimitiveTypes.NULL).build(), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				return Result.value(PrimitiveValue.newValue(PrimitiveTypes.BOOL, arguments[0].isPrimitive()));
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
					return Result.value(PrimitiveValue.newValue(PrimitiveTypes.STRING, value));
				else
					return Result.value(Value.NULL);
			}
		});

		addFunction(FunctionParameters.create("createChar", PrimitiveTypes.INT), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				return Result.value(PrimitiveValue.newValue(PrimitiveTypes.CHAR, (char) arguments[0].asPrimitive().getInt()));
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

		addFunction("timeMs", () -> PrimitiveValue.newValue(PrimitiveTypes.INT, (int) System.currentTimeMillis()));
		addFunction("timeNs", () -> PrimitiveValue.newValue(PrimitiveTypes.INT, (int) System.nanoTime()));

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
