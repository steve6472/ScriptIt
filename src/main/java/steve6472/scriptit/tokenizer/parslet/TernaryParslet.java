package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.Expression;
import steve6472.scriptit.TernaryExpression;
import steve6472.scriptit.tokenizer.*;

/**********************
 * Created by steve6472
 * On date: 2/4/2022
 * Project: ScriptIt
 *
 ***********************/
public class TernaryParslet implements InfixParslet
{
	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token, Expression leftExpression)
	{
		Expression ifTrue = parser.parse(getPrecedence());
		parser.tokenizer.consumeToken(Operator.TERNARY_SPLIT);
		Expression ifFalse = parser.parse(getPrecedence());
		return new TernaryExpression(leftExpression, ifTrue, ifFalse);
	}

	@Override
	public Precedence getPrecedence()
	{
		return Precedence.TERNARY;
	}
}
