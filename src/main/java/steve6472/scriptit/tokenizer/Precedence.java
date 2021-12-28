package steve6472.scriptit.tokenizer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 12/19/2021
 * Project: ScriptIt
 *
 ***********************/
public enum Precedence
{
	ANYTHING(0),
	ASSIGN(1), NAME_CHAIN(1), TERNARY(1),
	OR(2),
	AND(3),
	BIT_OR(4),
	BIT_XOR(5),
	BIT_AND(6),
	EQUALITY(7),
	RELATION(8),
	BIT_SHIFT(9),
	ADD_SUB(10),
	MUL_DIV_MOD(11),
	UNARY(12),
	FUNCTION_CALL(13),



	
	;

	int predecence;

	Precedence(int predecence)
	{
		this.predecence = predecence;
	}

	public int getPredecence()
	{
		return predecence;
	}
}
