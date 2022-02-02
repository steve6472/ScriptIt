package steve6472.scriptit.exceptions;

import steve6472.scriptit.Type;
import steve6472.scriptit.tokenizer.Operator;

/**********************
 * Created by steve6472
 * On date: 1/23/2022
 * Project: ScriptIt
 *
 ***********************/
public class NoOperatorFoundException extends RuntimeException
{
	public NoOperatorFoundException(Operator operator, Type left, Type right)
	{
		super("No operator found for type '" + left.getKeyword() + "' with right type '" + right.getKeyword() + "', operator: " + operator + " (" + operator.getSymbol() + ")");
	}
}