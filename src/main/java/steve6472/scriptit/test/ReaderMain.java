package steve6472.scriptit.test;

import steve6472.scriptit.*;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/23/2021
 * Project: ScriptIt
 *
 ***********************/
public class ReaderMain
{
	public static void main(String[] args)
	{
//		MyParser.DEBUG = true;
//		ScriptReader.DEBUG = true;
		Workspace workspace = new Workspace();

		Highlighter.RESET = Log.RESET;
		Highlighter.IMPORT = "\u001b[38;5;166m";
		Highlighter.IMPORT_TYPE = "\u001b[38;5;166m";
		Highlighter.IMPORT_NAME = "\u001b[38;5;250m";
		Highlighter.VAR = "\u001b[38;5;250m";
		Highlighter.SYMBOL = "\u001b[38;5;250m";
		Highlighter.IF = "\u001b[38;5;166m";
		Highlighter.ELSE = "\u001b[38;5;166m";
		Highlighter.BRACET = "\u001b[38;5;250m";
		Highlighter.CONST_NUM = "\u001b[38;5;61m";
		Highlighter.CONST_STR = "\u001b[38;5;23m";
		Highlighter.CONST_CHAR = "\u001b[38;5;23m";
		Highlighter.FUNCTION_NAME = "\u001b[38;5;220m";
		Highlighter.WHILE = "\u001b[38;5;166m";
		Highlighter.LOOP_CONTROL = "\u001b[38;5;166m";
		Highlighter.COMMENT = "\u001b[38;5;243m";

		Script script = ScriptReader.readScript(new File("scripts/password.txt"), workspace, true);

		System.out.println(script.showCode());

//		script.runWithDelay();
	}
}
