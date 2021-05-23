package steve6472.scriptit.exp;

import steve6472.scriptit.expression.OperatorType;

import static steve6472.scriptit.expression.OperatorType.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/3/2021
 * Project: ScriptIt
 *
 ***********************/
public enum Operator
{
	ADD("+", BOTH),
	SUB("-", BOTH),
	MUL("*", BINARY),
	DIV("/", BINARY),
	MOD("%", BINARY),
	NEG("~", UNARY),
	BIT_AND("&", BINARY),
	BIT_OR("|", BINARY),
	BIT_XOR("^", BINARY),
	LSH("<<", BINARY),
	RSH(">>", BINARY),

	EQUAL("==", BINARY),
	NOT_EQUAL("!=", BINARY),
	LESS_THAN_EQUAL("<=", BINARY),
	GREATER_THAN_EQUAL(">=", BINARY),
	LESS_THAN("<", BINARY),
	GREATER_THAN(">", BINARY),

	NOT("!", UNARY),
	AND("&&", BINARY),
	OR("||", BINARY),

	PRE_INC("++", UNARY),
	PRE_DEC("--", UNARY),

	COMMA(",", BINARY),
	ASSIGN("=", BINARY),
	DOT(".", BINARY);

	private static final Operator[] MAIN_OPS = {ADD, SUB, BIT_OR};
	private static final Operator[] UNARY_OPS = {ADD, SUB, NEG, NOT, PRE_INC, PRE_DEC};
	private static final Operator[] TERM_OPS = {MUL, DIV, MOD, AND, OR, BIT_AND, BIT_XOR, EQUAL, NOT_EQUAL, LSH, RSH, LESS_THAN_EQUAL, GREATER_THAN_EQUAL, LESS_THAN, GREATER_THAN};

	public static Operator[] getMainOps()
	{
		return MAIN_OPS;
	}

	public static Operator[] getUnaryOps()
	{
		return UNARY_OPS;
	}

	public static Operator[] getTermOps()
	{
		return TERM_OPS;
	}

	private final String operator;
	private final OperatorType type;

	Operator(String operator, OperatorType type)
	{
		this.operator = operator;
		this.type = type;
	}

	public String getOperator()
	{
		return operator;
	}

	public OperatorType getType()
	{
		return type;
	}
}
