package steve6472.scriptit;

import java.util.ArrayList;
import java.util.List;

import static steve6472.scriptit.OperatorType.*;

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

	/*
	 * Breaks
	 * a = Random.randomDouble(-1.0, 2.0)
	 * because of secondChars check
	 */
//	PRE_INC("++", UNARY),
//	PRE_DEC("--", UNARY),

	COMMA(",", BINARY),
	ASSIGN("=", BINARY),
	DOT(".", BINARY);

	public static final List<Character> secondChars = new ArrayList<>();

	static
	{
		for (Operator op : values())
		{
			if (op.operator.length() > 1)
			{
				secondChars.add(op.operator.charAt(1));
			}
		}
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
