package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.expressions.Expression;
import steve6472.scriptit.expressions.IndexTypeExpression;
import steve6472.scriptit.expressions.Variable;
import steve6472.scriptit.tokenizer.*;

/**
 * Created by steve6472
 * Date: 8/22/2022
 * Project: ScriptIt
 */
public class IndexParslet implements InfixParslet
{
	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token, Expression leftExpression)
	{
		if (!(leftExpression instanceof Variable var))
		{
			throw new RuntimeException("Left expression is not a NameExpression (" + leftExpression + ")");
		}
		return new IndexTypeExpression(var);
	}

	@Override
	public Precedence getPrecedence()
	{
		return Precedence.ASSIGN;
	}
}
