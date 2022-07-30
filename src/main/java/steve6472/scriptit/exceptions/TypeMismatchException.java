package steve6472.scriptit.exceptions;

import steve6472.scriptit.type.Type;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 12/12/2021
 * Project: ScriptIt
 *
 ***********************/
public class TypeMismatchException extends RuntimeException
{
	public TypeMismatchException(Type expectedType, Type actualType)
	{
		super("Expected: " + expectedType + ", got: " + actualType);
	}

	public TypeMismatchException(String message, Type expectedType, Type actualType)
	{
		super(message.formatted(expectedType, actualType));
	}
}
