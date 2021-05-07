package steve6472.scriptit;

import java.util.regex.Pattern;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/2/2021
 * Project: ScriptIt
 *
 ***********************/
public class Regexes
{
	public static final Pattern IS_PURE_NUMBER = Pattern.compile("([+-]?\\d*(\\.\\d+)?)+");

	/**
	 * string adr
	 */
	public static final Pattern VALUE_DECLARATION = Pattern.compile("^[a-zA-Z][a-zA-Z0-9]*\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*");
	/**
	 * adr = vec2((8 + function(2, 3)) * 3, 2).toString()
	 */
	public static final Pattern VALUE_ASSIGN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*\\s*=\\s*.+[^\\.]");
	/**
	 * string adr = vec2((8 + function(2, 3)) * 3, 2).toString()
	 */
	public static final Pattern VALUE_DECLARATION_ASSIGN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9]*\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*=\\s*.+[^\\.]");
	/**
	 * import int
	 */
	public static final Pattern IMPORT = Pattern.compile("^import\\s+[a-zA-Z][a-zA-Z0-9]*");
	/**
	 * function int name(...
	 */
	public static final Pattern DECLARE_FUNCTION = Pattern.compile("^function\\s+[a-zA-Z][a-zA-Z0-9_]*\\s+[a-zA-Z][a-zA-Z0-9_]*\\(.+");
	/**
	 * constructor(...
	 */
	public static final Pattern DECLARE_CONSTRUCTOR = Pattern.compile("^constructor\\s*\\(.+");
	/**
	 * class vec3
	 */
	public static final Pattern DECLARE_TYPE = Pattern.compile("^class\\s+[a-zA-Z][a-zA-Z0-9_]*\\s*\\{?.*");
	/**
	 * operator* (...
	 * Allowed operators: +, -, *, /, %, ~, &, |, ^, !, <<, >>, &&, ||
	 *     (byte shift operators are not supported yet due to Expression Parser being able to read only one character and im too lazy to add more)
	 */
	public static final Pattern OPERATOR_OVERLOAD_FUNCTION = Pattern.compile("^operator\\s*([\\+\\-\\*\\/%~&\\|\\^!]|(<<)|(>>)|(&&)|(\\|\\|))\\s*\\(.*");
	/**
	 * return ...
	 */
	public static final Pattern RETURN = Pattern.compile("^return\\s+[a-zA-Z0-9].*");
	/**
	 * this.x = 5
	 */
	public static final Pattern THIS_ASSIGN = Pattern.compile("^this\\.[a-zA-Z_][a-zA-Z0-9_]*\\s*=\\s*.+[^\\.]");
	/**
	 * public string adr
	 */
	public static final Pattern TYPE_VALUE_DECLARATION = Pattern.compile("^((public)|(private))?\\s*[a-zA-Z][a-zA-Z0-9]*\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*");
	/**
	 * return this
	 */
	public static final Pattern RETURN_THIS = Pattern.compile("^return\\s+this");
	/**
	 * import functions log
	 */
	public static final Pattern IMPORT_FUNCTIONS = Pattern.compile("^import\\s+functions\\s+[a-zA-Z][a-zA-Z0-9]*");
	/**
	 * delay 7
	 */
	public static final Pattern DELAY = Pattern.compile("^delay\\s+[0-9]+");
}



