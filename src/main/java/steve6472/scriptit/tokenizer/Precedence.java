package steve6472.scriptit.tokenizer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 12/19/2021
 * Project: ScriptIt
 *
 ***********************/
public enum Precedence
{
	ANYTHING,
	ASSIGN,
	NAME_CHAIN,
	TERNARY,
	OR,
	AND,
	BIT_OR,
	BIT_XOR,
	BIT_AND,
	EQUALITY,
	RELATION,
	BIT_SHIFT,
	ADD_SUB,
	MUL_DIV_MOD,
	UNARY,
	DOT,
	FUNCTION_CALL,


	
	;

	public int getPredecence()
	{
		return ordinal();
	}
}
