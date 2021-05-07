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
public class BasicFunctions
{
	public static void addMathFunctions(Workspace workspace)
	{
		String libName = "math";

		workspace.addConstructor(libName, FunctionParameters.function("sqrt").addType(DOUBLE).build(), a -> newValue(DOUBLE, Math.sqrt(a[0].getDouble())));
		workspace.addConstructor(libName, FunctionParameters.function("sin").addType(DOUBLE).build(), a -> newValue(DOUBLE, Math.sin(a[0].getDouble())));
		workspace.addConstructor(libName, FunctionParameters.function("cos").addType(DOUBLE).build(), a -> newValue(DOUBLE, Math.cos(a[0].getDouble())));
		workspace.addConstructor(libName, FunctionParameters.function("tan").addType(DOUBLE).build(), a -> newValue(DOUBLE, Math.tan(a[0].getDouble())));
		workspace.addConstructor(libName, FunctionParameters.function("tan").addType(DOUBLE).build(), a -> newValue(DOUBLE, Math.tan(a[0].getDouble())));

		workspace.addConstructor(libName, FunctionParameters.function("pi").build(), a -> newValue(DOUBLE, Math.PI));
	}

	public static void addPrintFunctions(Workspace workspace)
	{
		String libName = "print";

		workspace.addProcedure(libName, FunctionParameters.function("print").addType(STRING).build(), a -> System.out.println(a[0].getString()));
		workspace.addProcedure(libName, FunctionParameters.function("print").addType(DOUBLE).build(), a -> System.out.println(a[0].getDouble()));
		workspace.addProcedure(libName, FunctionParameters.function("print").addType(INT).build(), a -> System.out.println(a[0].getInt()));
		workspace.addProcedure(libName, FunctionParameters.function("print").addType(CHAR).build(), a -> System.out.println(a[0].getChar()));
		workspace.addProcedure(libName, FunctionParameters.function("print").addType(BOOL).build(), a -> System.out.println(a[0].getBoolean()));
	}

	public static void addColorPrint(Workspace workspace)
	{
		String libName = "log";

		workspace.addProcedure(libName, FunctionParameters.function("logBlack").build(), a -> Log.black());
		workspace.addProcedure(libName, FunctionParameters.function("logRed").build(), a -> Log.red());
		workspace.addProcedure(libName, FunctionParameters.function("logGreen").build(), a -> Log.green());
		workspace.addProcedure(libName, FunctionParameters.function("logYellow").build(), a -> Log.yellow());
		workspace.addProcedure(libName, FunctionParameters.function("logBlue").build(), a -> Log.blue());
		workspace.addProcedure(libName, FunctionParameters.function("logMagenta").build(), a -> Log.magenta());
		workspace.addProcedure(libName, FunctionParameters.function("logCyan").build(), a -> Log.cyan());
		workspace.addProcedure(libName, FunctionParameters.function("logWhite").build(), a -> Log.white());
		workspace.addProcedure(libName, FunctionParameters.function("logReset").build(), a -> Log.reset());

		workspace.addProcedure(libName, FunctionParameters.function("logBrightBlack").build(), a -> Log.brightBlack());
		workspace.addProcedure(libName, FunctionParameters.function("logBrightRed").build(), a -> Log.brightRed());
		workspace.addProcedure(libName, FunctionParameters.function("logBrightGreen").build(), a -> Log.brightGreen());
		workspace.addProcedure(libName, FunctionParameters.function("logBrightYellow").build(), a -> Log.brightYellow());
		workspace.addProcedure(libName, FunctionParameters.function("logBrightBlue").build(), a -> Log.brightBlue());
		workspace.addProcedure(libName, FunctionParameters.function("logBrightMagenta").build(), a -> Log.brightMagenta());
		workspace.addProcedure(libName, FunctionParameters.function("logBrightCyan").build(), a -> Log.brightCyan());
		workspace.addProcedure(libName, FunctionParameters.function("logBrightWhite").build(), a -> Log.brightWhite());
	}
}