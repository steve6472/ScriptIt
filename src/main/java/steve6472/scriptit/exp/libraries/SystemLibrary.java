package steve6472.scriptit.exp.libraries;

import steve6472.scriptit.exp.FunctionParameters;
import steve6472.scriptit.exp.PrimitiveTypes;
import steve6472.scriptit.exp.functions.PrintFunction;

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
		addFunction(FunctionParameters.function("print").addType(PrimitiveTypes.NULL).build(), new PrintFunction());
	}
}
