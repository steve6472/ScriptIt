package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.BinaryOperator;
import steve6472.scriptit.Expression;
import steve6472.scriptit.tokenizer.TokenParser;
import steve6472.scriptit.tokenizer.Tokenizer;
import steve6472.scriptit.tokenizer.InfixParslet;
import steve6472.scriptit.tokenizer.Precedence;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 12/18/2021
 * Project: ScriptIt
 *
 ***********************/
public class BinaryParslet implements InfixParslet
{
	Precedence precedence;

	public BinaryParslet(Precedence precedence)
	{
		this.precedence = precedence;
	}

	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token, Expression leftExpression)
	{
		Expression rightExpr = parser.parse(getPrecedence());
		return new BinaryOperator(token.type(), leftExpression, rightExpr);
	}

	@Override
	public Precedence getPrecedence()
	{
		return precedence;
	}
}
