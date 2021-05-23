package steve6472.scriptit.libraries;

import steve6472.scriptit.Function;
import steve6472.scriptit.FunctionParameters;
import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.functions.PrintFunction;
import steve6472.scriptit.types.CustomTypes;
import steve6472.scriptit.types.PrimitiveTypes;

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

		addFunction(FunctionParameters.function("print").addType(PrimitiveTypes.DOUBLE).build(), new PrintFunction());
		addFunction(FunctionParameters.function("print").addType(PrimitiveTypes.INT).build(), new PrintFunction());
		addFunction(FunctionParameters.function("print").addType(PrimitiveTypes.BOOL).build(), new PrintFunction());
		addFunction(FunctionParameters.function("print").addType(PrimitiveTypes.STRING).build(), new PrintFunction());
		addFunction(FunctionParameters.function("print").addType(PrimitiveTypes.NULL).build(), new PrintFunction());

		addFunction(FunctionParameters.function("printDetail").addType(CustomTypes.VEC2).build(), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				System.out.println(arguments[0] + ", hash: " + arguments[0].hashCode());
				return Result.pass();
			}
		});
	}
}
