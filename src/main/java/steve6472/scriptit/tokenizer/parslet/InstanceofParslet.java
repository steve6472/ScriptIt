package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.expressions.Expression;
import steve6472.scriptit.expressions.Instanceof;
import steve6472.scriptit.tokenizer.*;

/**********************
 * Created by steve6472
 * On date: 12/18/2021
 * Project: ScriptIt
 *
 ***********************/
public class InstanceofParslet implements InfixParslet
{
	public InstanceofParslet()
	{
	}

	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token, Expression leftExpression)
	{
		if (parser.tokenizer.matchToken(Operator.NOT, true))
		{
			return new Instanceof(leftExpression, parser.tokenizer.nextToken().sval(), true);
		} else
		{
			return new Instanceof(leftExpression, parser.tokenizer.nextToken().sval(), false);
		}
	}

	@Override
	public Precedence getPrecedence()
	{
		return Precedence.ASSIGN;
	}
}
