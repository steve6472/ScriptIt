package steve6472.scriptit;

import steve6472.scriptit.expression.FunctionParameters;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

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
	private static final Map<String, Consumer<Script>> importMap = new HashMap<>();

	static
	{
		importMap.put("mathFunctionsRad", ImportableFunctions::importMathFunctionsRad);
		importMap.put("printFunctions", ImportableFunctions::importPrintFunctions);
		importMap.put("log", ImportableFunctions::importColorPrint);
	}

	public static void importFunctions(Script script, String functions)
	{
		importMap.get(functions).accept(script);
	}

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

	public static void importColorPrint(Script script)
	{
		script.addProcedure(FunctionParameters.function("logBlack").build(), a -> Log.black());
		script.addProcedure(FunctionParameters.function("logRed").build(), a -> Log.red());
		script.addProcedure(FunctionParameters.function("logGreen").build(), a -> Log.green());
		script.addProcedure(FunctionParameters.function("logYellow").build(), a -> Log.yellow());
		script.addProcedure(FunctionParameters.function("logBlue").build(), a -> Log.blue());
		script.addProcedure(FunctionParameters.function("logMagenta").build(), a -> Log.magenta());
		script.addProcedure(FunctionParameters.function("logCyan").build(), a -> Log.cyan());
		script.addProcedure(FunctionParameters.function("logWhite").build(), a -> Log.white());
		script.addProcedure(FunctionParameters.function("logReset").build(), a -> Log.reset());

		script.addProcedure(FunctionParameters.function("logBrightBlack").build(), a -> Log.brightBlack());
		script.addProcedure(FunctionParameters.function("logBrightRed").build(), a -> Log.brightRed());
		script.addProcedure(FunctionParameters.function("logBrightGreen").build(), a -> Log.brightGreen());
		script.addProcedure(FunctionParameters.function("logBrightYellow").build(), a -> Log.brightYellow());
		script.addProcedure(FunctionParameters.function("logBrightBlue").build(), a -> Log.brightBlue());
		script.addProcedure(FunctionParameters.function("logBrightMagenta").build(), a -> Log.brightMagenta());
		script.addProcedure(FunctionParameters.function("logBrightCyan").build(), a -> Log.brightCyan());
		script.addProcedure(FunctionParameters.function("logBrightWhite").build(), a -> Log.brightWhite());
	}

	public static void definePi(Script script)
	{
		script.addValue("pi", newValue(DOUBLE, Math.PI));
	}
}
