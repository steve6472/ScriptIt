package steve6472.scriptit.libraries;

import steve6472.scriptit.Log;
import steve6472.scriptit.type.PrimitiveTypes;
import steve6472.scriptit.value.PrimitiveValue;

/**********************
 * Created by steve6472
 * On date: 5/28/2021
 * Project: ScriptIt
 *
 ***********************/
public class LogLibrary extends Library
{
	public LogLibrary()
	{
		super("Log");

		addFunction("black", () -> {Log.black(); return PrimitiveValue.newValue(PrimitiveTypes.STRING, Log.black());});
		addFunction("red", () -> {Log.red(); return PrimitiveValue.newValue(PrimitiveTypes.STRING, Log.red());});
		addFunction("green", () -> {Log.green(); return PrimitiveValue.newValue(PrimitiveTypes.STRING, Log.green());});
		addFunction("yellow", () -> {Log.yellow(); return PrimitiveValue.newValue(PrimitiveTypes.STRING, Log.yellow());});
		addFunction("blue", () -> {Log.blue(); return PrimitiveValue.newValue(PrimitiveTypes.STRING, Log.blue());});
		addFunction("magenta", () -> {Log.magenta(); return PrimitiveValue.newValue(PrimitiveTypes.STRING, Log.magenta());});
		addFunction("cyan", () -> {Log.cyan(); return PrimitiveValue.newValue(PrimitiveTypes.STRING, Log.cyan());});
		addFunction("white", () -> {Log.white(); return PrimitiveValue.newValue(PrimitiveTypes.STRING, Log.white());});
		addFunction("reset", () -> {Log.reset(); return PrimitiveValue.newValue(PrimitiveTypes.STRING, Log.reset());});

		addFunction("brightBlack", () -> {Log.brightBlack(); return PrimitiveValue.newValue(PrimitiveTypes.STRING, Log.brightBlack());});
		addFunction("brightRed", () -> {Log.brightRed(); return PrimitiveValue.newValue(PrimitiveTypes.STRING, Log.brightRed());});
		addFunction("brightGreen", () -> {Log.brightGreen(); return PrimitiveValue.newValue(PrimitiveTypes.STRING, Log.brightGreen());});
		addFunction("brightYellow", () -> {Log.brightYellow(); return PrimitiveValue.newValue(PrimitiveTypes.STRING, Log.brightYellow());});
		addFunction("brightBlue", () -> {Log.brightBlue(); return PrimitiveValue.newValue(PrimitiveTypes.STRING, Log.brightBlue());});
		addFunction("brightMagenta", () -> {Log.brightMagenta(); return PrimitiveValue.newValue(PrimitiveTypes.STRING, Log.brightMagenta());});
		addFunction("brightCyan", () -> {Log.brightCyan(); return PrimitiveValue.newValue(PrimitiveTypes.STRING, Log.brightCyan());});
		addFunction("brightWhite", () -> {Log.brightWhite(); return PrimitiveValue.newValue(PrimitiveTypes.STRING, Log.brightWhite());});
	}
}
