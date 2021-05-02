package steve6472.scriptit;

import java.util.regex.Pattern;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/2/2021
 * Project: ScriptIt
 *
 ***********************/
public class Stuff
{
	public static final Pattern IS_PURE_NUMBER = Pattern.compile("([+-]?\\d*(\\.\\d+)?)+");

	/**
	 * string adr
	 */
	public static final Pattern VALUE_DECLARATION = Pattern.compile("^[a-zA-Z][a-zA-Z0-9]*\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*");
	/**
	 * adr = vec2((8 + function(2, 3)) * 3, 2).toString()
	 */
	public static final Pattern VALUE_ASSIGN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*\s*=\s*.+[^\\.]");
	/**
	 * string adr = vec2((8 + function(2, 3)) * 3, 2).toString()
	 */
	public static final Pattern VALUE_DECLARATION_ASSIGN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9]*\s+[a-zA-Z_][a-zA-Z0-9_]*\s*=\s*.+[^\\.]");
	/**
	 * import int
	 */
	public static final Pattern IMPORT = Pattern.compile("^import\s+[a-zA-Z][a-zA-Z0-9]*");
}



