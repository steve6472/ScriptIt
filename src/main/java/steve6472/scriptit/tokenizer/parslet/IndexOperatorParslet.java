package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.expressions.BinaryOperator;
import steve6472.scriptit.expressions.Expression;
import steve6472.scriptit.tokenizer.*;

/**********************
 * Created by steve6472
 * On date: 5/20/2022
 * Project: PixelIt
 *
 ***********************/
public class IndexOperatorParslet implements InfixParslet
{
	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token, Expression leftExpression)
	{
		Expression rightExpr = parser.parse(Precedence.ANYTHING);
		parser.tokenizer.consumeToken(Operator.BRACKET_SQUARE_RIGHT);
		return new BinaryOperator(Operator.INDEX, leftExpression, rightExpr);
	}

	@Override
	public Precedence getPrecedence()
	{
		return Precedence.FUNCTION_CALL;
	}
}
