package steve6472.scriptit.expression;

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

	NOT("!", UNARY),
	AND("&&", BINARY),
	OR("||", BINARY),

	PRE_INC("++", UNARY),
	PRE_DEC("--", UNARY);

	private static final Operator[] UNARY_OPS = {ADD, SUB, NEG, NOT, PRE_INC, PRE_DEC};

	public static Operator[] getUnaryOps()
	{
		return UNARY_OPS;
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
