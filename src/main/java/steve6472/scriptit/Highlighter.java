package steve6472.scriptit;

/**********************
 * Created by steve6472
 * On date: 6/19/2021
 * Project: ScriptIt
 *
 ***********************/
public class Highlighter
{
	public static String RESET = "%RESET%";
	public static String VAR = "%VAR%";
	public static String CONST_NUM = "%CONST_NUM%";
	public static String CONST_STR = "%CONST_STR%";
	public static String CONST_CHAR = "%CONST_CHAR%";
	public static String CONST_BOOL = "%CONST_BOOL%";
	public static String SYMBOL = "%SYMBOL%";
	public static String BRACET = "%BRACET%";
	public static String IMPORT = "%IMPORT%";
	public static String IMPORT_TYPE = "%IMPORT_TYPE%";
	public static String IMPORT_NAME = "%IMPORT_NAME%";
	public static String IF = "%IF%";
	public static String ELSE = "%ELSE%";
	public static String FUNCTION_NAME = "%FUNC_NAME%";
	public static String WHILE = "%WHILE%";
	public static String LOOP_CONTROL = "%LOOP_CONTROL%";
	public static String END = ";";
	public static String EMPTY_LINE = "";
	public static String COMMENT = "%COMMENT%";
	public static String RETURN = "%RETURN%";
	public static String INSTANCEOF = "%INSTANCEOF%";

	public static void reset()
	{
		RESET = "%RESET%";
		VAR = "%VAR%";
		CONST_NUM = "%CONST_NUM%";
		CONST_STR = "%CONST_STR%";
		CONST_CHAR = "%CONST_CHAR%";
		CONST_BOOL = "%CONST_BOOL%";
		SYMBOL = "%SYMBOL%";
		BRACET = "%BRACET%";
		IMPORT = "%IMPORT%";
		IMPORT_TYPE = "%IMPORT_TYPE%";
		IMPORT_NAME = "%IMPORT_NAME%";
		IF = "%IF%";
		ELSE = "%ELSE%";
		FUNCTION_NAME = "%FUNC_NAME%";
		WHILE = "%WHILE%";
		LOOP_CONTROL = "%LOOP_CONTROL%";
		END = ";";
		EMPTY_LINE = "";
		COMMENT = "%COMMENT%";
		RETURN = "%RETURN%";
		INSTANCEOF = "%INSTANCEOF%";
	}

	public static void basicHighlight()
	{
		RESET = Log.RESET;
		IMPORT = "\u001b[38;5;166m";
		IMPORT_TYPE = "\u001b[38;5;166m";
		IMPORT_NAME = "\u001b[38;5;250m";
		VAR = "\u001b[38;5;250m";
		SYMBOL = "\u001b[38;5;250m";
		IF = "\u001b[38;5;166m";
		ELSE = "\u001b[38;5;166m";
		BRACET = "\u001b[38;5;250m";
		CONST_NUM = "\u001b[38;5;61m";
		CONST_STR = "\u001b[38;5;23m";
		CONST_CHAR = "\u001b[38;5;23m";
		FUNCTION_NAME = "\u001b[38;5;220m";
		WHILE = "\u001b[38;5;166m";
		LOOP_CONTROL = "\u001b[38;5;166m";
		COMMENT = "\u001b[38;5;243m";
		RETURN = "\u001b[38;5;166m";
		INSTANCEOF = "\u001b[38;5;166m";
	}
}
