package steve6472.scriptit.newtokenizer;

/**********************
 * Created by steve6472
 * Date: 9/3/2022
 * Project: ScriptIt
 *
 ***********************/
public enum Precedence implements IPrecedence
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
}
