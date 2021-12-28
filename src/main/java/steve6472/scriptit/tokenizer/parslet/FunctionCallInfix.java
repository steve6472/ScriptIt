package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.*;
import steve6472.scriptit.tokenizer.InfixParslet;
import steve6472.scriptit.tokenizer.Precedence;

/**********************
 * Created by steve6472
 * On date: 12/28/2021
 * Project: ScriptIt
 *
 ***********************/
public class FunctionCallInfix implements InfixParslet
{
	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token, Expression leftExpression)
	{
		if (!(leftExpression instanceof Variable v))
			throw new RuntimeException("Not a name ???? (" + leftExpression + ")");
		return new FunctionCall(FunctionSource.function(v.source.variableName), parser.parseArguments(false));
	}

	@Override
	public Precedence getPrecedence()
	{
		return Precedence.FUNCTION_CALL;
	}
}
