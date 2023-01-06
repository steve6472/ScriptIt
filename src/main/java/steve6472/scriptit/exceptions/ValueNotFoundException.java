package steve6472.scriptit.exceptions;

/**********************
 * Created by steve6472
 * On date: 12/12/2021
 * Project: ScriptIt
 *
 ***********************/
public class ValueNotFoundException extends RuntimeException
{
	/**
	 * Constructs a new runtime exception with the specified detail message.
	 * The cause is not initialized, and may subsequently be initialized by a
	 * call to {@link #initCause}.
	 *
	 * @param message the detail message. The detail message is saved for
	 *                later retrieval by the {@link #getMessage()} method.
	 */
	public ValueNotFoundException(String message)
	{
		super(message);
	}
}
