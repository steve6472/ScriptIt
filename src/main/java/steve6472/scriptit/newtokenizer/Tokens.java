package steve6472.scriptit.newtokenizer;

/**
 * Created by steve6472
 * Date: 9/3/2022
 * Project: ScriptIt
 */
public enum Tokens implements IToken
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
	ASSIGN_BIT_AND("&=", true),
	ASSIGN_BIT_OR("|=", true),
	ASSIGN_BIT_XOR("^=", true),

	SINGLE_LINE_COMMENT("//", true),
	MULTI_LINE_COMMENT_BEGIN("/*", true),
	MULTI_LINE_COMMENT_END("*/", true),

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

	// Ternary
	TERNARY("?"),
	TERNARY_SPLIT(":"),

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
	INDEX("[]", true),

	// Keywords
	IMPORT("import"),
	FOR("for"),
	IF("if"),
	RETURN("return"),
	FUNCTION("function"),
	FUNC("func"),
	ELSE("else"),
	WHILE("while"),
	RETURN_IF("returnif"),
	CONTINUE("continue"),
	BREAK("break"),
	CLASS("class"),
	EXTENDS("extends"),
	INSTANCEOF("instanceof"),
	OVERLOAD("overload"),
	THIS("this"),
	AUTO("auto"),

	;

	private final String symbol;
	private final boolean isMerge;

	Tokens()
	{
		this("", false);
	}

	Tokens(String symbol)
	{
		this(symbol, false);
	}

	Tokens(String symbol, boolean isMerge)
	{
		this.symbol = symbol;
		this.isMerge = isMerge;
	}

	@Override
	public String getSymbol()
	{
		return symbol;
	}

	@Override
	public boolean isMerge()
	{
		return isMerge;
	}
}
