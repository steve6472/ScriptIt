package steve6472.scriptit.exp.libraries;

import steve6472.scriptit.exp.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/22/2021
 * Project: ScriptIt
 *
 ***********************/
public class Library
{
	private final String libraryName;
	private final Map<FunctionParameters, Function> functions = new HashMap<>();

	public Library(String libraryName)
	{
		this.libraryName = libraryName;
	}

	public String getLibraryName()
	{
		return libraryName;
	}

	public void addFunction(FunctionParameters parameters, Function function)
	{
		functions.put(parameters, function);
	}

	public Function getFunction(String name, Type[] types)
	{
		Function func = null;
		main: for (Map.Entry<FunctionParameters, Function> e : functions.entrySet())
		{
			FunctionParameters parameters = e.getKey();
			Function function = e.getValue();

			if (!parameters.getName().equals(name))
				continue;
			if (parameters.getTypes().length != types.length)
				continue;
			for (int i = 0; i < types.length; i++)
			{
				if (parameters.getTypes()[i] != types[i])
				{
					continue main;
				}
			}
			func = function;
			break;
		}

		if (func == null)
			throw new RuntimeException("Function '" + name + "' with argument types " + Arrays.toString(types) + " not found!");

		return func;
	}

	private static final class Func extends Function
	{
		MultiParamFunction func;

		public Func(MultiParamFunction func)
		{
			super((String[]) null);
			this.func = func;
		}

		@Override
		public Result apply(Main.Script script)
		{
			return Result.returnValue(func.apply(arguments));
		}
	}

	@FunctionalInterface
	protected interface NoParamFunction
	{
		Value apply();
	}

	@FunctionalInterface
	protected interface OneParamFunction
	{
		Value apply(Value param1);
	}

	@FunctionalInterface
	protected interface TwoParamFunction
	{
		Value apply(Value param1, Value param2);
	}

	@FunctionalInterface
	protected interface ThreeParamFunction
	{
		Value apply(Value param1, Value param2, Value param3);
	}

	@FunctionalInterface
	protected interface FourParamFunction
	{
		Value apply(Value param1, Value param2, Value param3, Value param4);
	}

	@FunctionalInterface
	protected interface FiveParamFunction
	{
		Value apply(Value param1, Value param2, Value param3, Value param4, Value param5);
	}

	@FunctionalInterface
	protected interface SixParamFunction
	{
		Value apply(Value param1, Value param2, Value param3, Value param4, Value param5, Value param6);
	}

	@FunctionalInterface
	protected interface SevenParamFunction
	{
		Value apply(Value param1, Value param2, Value param3, Value param4, Value param5, Value param6, Value param7);
	}

	@FunctionalInterface
	protected interface EightParamFunction
	{
		Value apply(Value param1, Value param2, Value param3, Value param4, Value param5, Value param6, Value param7, Value param8);
	}

	@FunctionalInterface
	protected interface NineParamFunction
	{
		Value apply(Value param1, Value param2, Value param3, Value param4, Value param5, Value param6, Value param7, Value param8, Value param9);
	}

	@FunctionalInterface
	protected interface MultiParamFunction
	{
		Value apply(Value... params);
	}

	public void addFunction(String name, NoParamFunction function, Type... types)
	{
		addFunction(FunctionParameters.create(name, types), new Func(args -> function.apply()));
	}

	public void addFunction(String name, OneParamFunction function, Type... types)
	{
		addFunction(FunctionParameters.create(name, types), new Func(args -> function.apply(args[0])));
	}

	public void addFunction(String name, TwoParamFunction function, Type... types)
	{
		addFunction(FunctionParameters.create(name, types), new Func(args -> function.apply(args[0], args[1])));
	}

	public void addFunction(String name, ThreeParamFunction function, Type... types)
	{
		addFunction(FunctionParameters.create(name, types), new Func(args -> function.apply(args[0], args[1], args[2])));
	}

	public void addFunction(String name, FourParamFunction function, Type... types)
	{
		addFunction(FunctionParameters.create(name, types), new Func(args -> function.apply(args[0], args[1], args[2], args[3])));
	}

	public void addFunction(String name, FiveParamFunction function, Type... types)
	{
		addFunction(FunctionParameters.create(name, types), new Func(args -> function.apply(args[0], args[1], args[2], args[3], args[4])));
	}

	public void addFunction(String name, SixParamFunction function, Type... types)
	{
		addFunction(FunctionParameters.create(name, types), new Func(args -> function.apply(args[0], args[1], args[2], args[3], args[4], args[5])));
	}

	public void addFunction(String name, SevenParamFunction function, Type... types)
	{
		addFunction(FunctionParameters.create(name, types), new Func(args -> function.apply(args[0], args[1], args[2], args[3], args[4], args[5], args[6])));
	}

	public void addFunction(String name, EightParamFunction function, Type... types)
	{
		addFunction(FunctionParameters.create(name, types), new Func(args -> function.apply(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7])));
	}

	public void addFunction(String name, NineParamFunction function, Type... types)
	{
		addFunction(FunctionParameters.create(name, types), new Func(args -> function.apply(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8])));
	}

	public void addFunctionM(String name, MultiParamFunction function, Type... types)
	{
		addFunction(FunctionParameters.create(name, types), new Func(function));
	}
}
