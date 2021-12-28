package steve6472.scriptit.tokenizer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 12/18/2021
 * Project: ScriptIt
 *
 ***********************/
public enum Operator
{
	// Mathematics / Binary
	ADD("+"),
	SUB("-"),
	MUL("*"),
	DIV("/"),
	MOD("%"),
	NEG("~"),
	BIT_AND("&"),
	BIT_OR("|"),
	BIT_XOR("^"),
	LSH("<<", true),
	RSH(">>", true),

	ASSIGN_ADD("+=", true),
	ASSIGN_SUB("-=", true),
	ASSIGN_MUL("*=", true),
	ASSIGN_DIV("/=", true),
	ASSIGN_MOD("%=", true),
	ASSIGN_NEG("~=", true),
	ASSIGN_BIT_AND("&=", true),
	ASSIGN_BIT_OR("|=", true),
	ASSIGN_BIT_XOR("^=", true),

	/* FIXME
	 * The merging in MiniTokenizer/TokenParser would die cause it checks only the first TWO characters
	 * and as you can see these SHITS have THREE characters
	 * AAAAAAAAAAAAAAA
	 *
	 * I don't wanna deal with this
	 * It's just... not gonna get implemented for a while
	 */
	//ASSIGN_LSH("<<=", true),
	//ASSIGN_RSH(">>=", true),

	// Equality
	EQUAL("==", true),
	NOT_EQUAL("!=", true),

	// Relativity
	LESS_THAN_EQUAL("<=", true),
	GREATER_THAN_EQUAL(">=", true),
	LESS_THAN("<"),
	GREATER_THAN(">"),

	// Boolean
	NOT("!"),
	AND("&&", true),
	OR("||", true),

	PRE_INC("++", true),
	PRE_DEC("--", true),

	// Special
	COMMA(","),
	ASSIGN("="),
	DOT("."),
	SEMICOLON(";"),

	// Brackets
	BRACKET_LEFT("("),
	BRACKET_RIGHT(")"),
	BRACKET_CURLY_LEFT("{"),
	BRACKET_CURLY_RIGHT("}"),
	BRACKET_SQUARE_LEFT("["),
	BRACKET_SQUARE_RIGHT("]"),

	// Keywords
	IMPORT("import"),
	FOR("for"),
	IF("if"),
	RETURN("return"),
	FUNCTION("function"),
	ELSE("else"),
	WHILE("while"),

	// Other special
	NUMBER_INT(""),
	NUMBER_DOUBLE(""),
	NAME(""),
	STRING(""),
	CHAR(""),
	TYPE_NAME(""),
	EOF(""),
	;

	String symbol;
	boolean merge;

	Operator(String symbol, boolean merge)
	{
		this.symbol = symbol;
		this.merge = merge;
	}

	Operator(String symbol)
	{
		this.symbol = symbol;
	}

	public String getSymbol()
	{
		return symbol;
	}

	public boolean isMerge()
	{
		return merge;
	}

	private static final Operator[] VALUES = values();

	public static Operator[] getValues()
	{
		return VALUES;
	}

	public static Operator fromSymbol(String symbol)
	{
		for (Operator tokenType : VALUES)
		{
			if (tokenType.symbol.equals(symbol))
			{
				return tokenType;
			}
		}

		return null;
	}
}