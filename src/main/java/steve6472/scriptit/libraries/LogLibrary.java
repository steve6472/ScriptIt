package steve6472.scriptit.libraries;

import steve6472.scriptit.Log;
import steve6472.scriptit.Value;
import steve6472.scriptit.types.PrimitiveTypes;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/28/2021
 * Project: ScriptIt
 *
 ***********************/
public class LogLibrary extends Library
{
	public LogLibrary()
	{
		super("Log");

		addFunction("black", () -> {Log.black(); return Value.newValue(PrimitiveTypes.STRING, Log.black());});
		addFunction("red", () -> {Log.red(); return Value.newValue(PrimitiveTypes.STRING, Log.red());});
		addFunction("green", () -> {Log.green(); return Value.newValue(PrimitiveTypes.STRING, Log.green());});
		addFunction("yellow", () -> {Log.yellow(); return Value.newValue(PrimitiveTypes.STRING, Log.yellow());});
		addFunction("blue", () -> {Log.blue(); return Value.newValue(PrimitiveTypes.STRING, Log.blue());});
		addFunction("magenta", () -> {Log.magenta(); return Value.newValue(PrimitiveTypes.STRING, Log.magenta());});
		addFunction("cyan", () -> {Log.cyan(); return Value.newValue(PrimitiveTypes.STRING, Log.cyan());});
		addFunction("white", () -> {Log.white(); return Value.newValue(PrimitiveTypes.STRING, Log.white());});
		addFunction("reset", () -> {Log.reset(); return Value.newValue(PrimitiveTypes.STRING, Log.reset());});

		addFunction("brightBlack", () -> {Log.brightBlack(); return Value.newValue(PrimitiveTypes.STRING, Log.brightBlack());});
		addFunction("brightRed", () -> {Log.brightRed(); return Value.newValue(PrimitiveTypes.STRING, Log.brightRed());});
		addFunction("brightGreen", () -> {Log.brightGreen(); return Value.newValue(PrimitiveTypes.STRING, Log.brightGreen());});
		addFunction("brightYellow", () -> {Log.brightYellow(); return Value.newValue(PrimitiveTypes.STRING, Log.brightYellow());});
		addFunction("brightBlue", () -> {Log.brightBlue(); return Value.newValue(PrimitiveTypes.STRING, Log.brightBlue());});
		addFunction("brightMagenta", () -> {Log.brightMagenta(); return Value.newValue(PrimitiveTypes.STRING, Log.brightMagenta());});
		addFunction("brightCyan", () -> {Log.brightCyan(); return Value.newValue(PrimitiveTypes.STRING, Log.brightCyan());});
		addFunction("brightWhite", () -> {Log.brightWhite(); return Value.newValue(PrimitiveTypes.STRING, Log.brightWhite());});
	}
}
