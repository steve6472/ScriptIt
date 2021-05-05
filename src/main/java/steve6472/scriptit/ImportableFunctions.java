package steve6472.scriptit;

import steve6472.scriptit.expression.FunctionParameters;

import static steve6472.scriptit.TypeDeclarations.*;
import static steve6472.scriptit.expression.Value.newValue;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/2/2021
 * Project: ScriptIt
 *
 ***********************/
public class ImportableFunctions
{
	public static void importMathFunctionsRad(Script script)
	{
		script.addConstructor(FunctionParameters.function("sqrt").addType(DOUBLE).build(), a -> newValue(DOUBLE, Math.sqrt(a[0].getDouble())));
		script.addConstructor(FunctionParameters.function("sin").addType(DOUBLE).build(), a -> newValue(DOUBLE, Math.sin(a[0].getDouble())));
		script.addConstructor(FunctionParameters.function("cos").addType(DOUBLE).build(), a -> newValue(DOUBLE, Math.cos(a[0].getDouble())));
		script.addConstructor(FunctionParameters.function("tan").addType(DOUBLE).build(), a -> newValue(DOUBLE, Math.tan(a[0].getDouble())));
	}

	public static void importPrintFunctions(Script script)
	{
		script.addProcedure(FunctionParameters.function("print").addType(STRING).build(), a -> System.out.println(a[0].getString()));
		script.addProcedure(FunctionParameters.function("print").addType(DOUBLE).build(), a -> System.out.println(a[0].getDouble()));
		script.addProcedure(FunctionParameters.function("print").addType(INT).build(), a -> System.out.println(a[0].getInt()));
		script.addProcedure(FunctionParameters.function("print").addType(CHAR).build(), a -> System.out.println(a[0].getChar()));
		script.addProcedure(FunctionParameters.function("print").addType(BOOL).build(), a -> System.out.println(a[0].getBoolean()));
	}

	public static void definePi(Script script)
	{
		script.addValue("pi", newValue(DOUBLE, Math.PI));
	}
}
