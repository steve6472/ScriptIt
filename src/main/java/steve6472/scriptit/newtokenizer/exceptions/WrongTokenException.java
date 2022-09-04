package steve6472.scriptit.newtokenizer.exceptions;

import steve6472.scriptit.newtokenizer.IToken;
import steve6472.scriptit.newtokenizer.Tokenizer;

/**
 * Created by steve6472
 * Date: 9/3/2022
 * Project: ScriptIt
 */
public class WrongTokenException extends RuntimeException
{
	public WrongTokenException(IToken expected, IToken actual)
	{
		super("Expected token " + expected + ", got " + actual);
	}

	public WrongTokenException(IToken expected, Tokenizer.Token actual)
	{
		super("Expected token " + expected + ", got " + actual.type());
	}
}
