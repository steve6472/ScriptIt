package steve6472.scriptit.libraries;

import steve6472.scriptit.Log;
import steve6472.scriptit.Value;

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

		addFunction("black", () -> {Log.black(); return Value.NULL;});
		addFunction("red", () -> {Log.red(); return Value.NULL;});
		addFunction("green", () -> {Log.green(); return Value.NULL;});
		addFunction("yellow", () -> {Log.yellow(); return Value.NULL;});
		addFunction("blue", () -> {Log.blue(); return Value.NULL;});
		addFunction("magenta", () -> {Log.magenta(); return Value.NULL;});
		addFunction("cyan", () -> {Log.cyan(); return Value.NULL;});
		addFunction("white", () -> {Log.white(); return Value.NULL;});
		addFunction("reset", () -> {Log.reset(); return Value.NULL;});

		addFunction("brightBlack", () -> {Log.brightBlack(); return Value.NULL;});
		addFunction("brightRed", () -> {Log.brightRed(); return Value.NULL;});
		addFunction("brightGreen", () -> {Log.brightGreen(); return Value.NULL;});
		addFunction("brightYellow", () -> {Log.brightYellow(); return Value.NULL;});
		addFunction("brightBlue", () -> {Log.brightBlue(); return Value.NULL;});
		addFunction("brightMagenta", () -> {Log.brightMagenta(); return Value.NULL;});
		addFunction("brightCyan", () -> {Log.brightCyan(); return Value.NULL;});
		addFunction("brightWhite", () -> {Log.brightWhite(); return Value.NULL;});
	}
}
