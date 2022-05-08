package steve6472.scriptit.exceptions;

import steve6472.scriptit.Type;
import steve6472.scriptit.Value;

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

	public NoBinaryOperationFoundException(Value left, Value right)
	{
		super("Type '" + left.type.getKeyword() + "' does not have binary operation with type '" + right.type.getKeyword() + "' ('" + left + "', '" + right + "')");
	}
}