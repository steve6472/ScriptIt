package steve6472.scriptit.exceptions;

import steve6472.scriptit.Type;

/**********************
 * Created by steve6472
 * On date: 1/23/2022
 * Project: ScriptIt
 *
 ***********************/
public class NoBinaryOperationFoundException extends RuntimeException
{
	public NoBinaryOperationFoundException(Type left, Type right)
	{
		super("Type '" + left.getKeyword() + "' does not have binary operation with type '" + right.getKeyword() + "'");
	}
}