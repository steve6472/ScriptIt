package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.expressions.Expression;
import steve6472.scriptit.expressions.FunctionCall;
import steve6472.scriptit.expressions.FunctionSource;
import steve6472.scriptit.expressions.Variable;
import steve6472.scriptit.tokenizer.InfixParslet;
import steve6472.scriptit.tokenizer.Precedence;
import steve6472.scriptit.tokenizer.TokenParser;
import steve6472.scriptit.tokenizer.Tokenizer;

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
