package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.Expression;
import steve6472.scriptit.tokenizer.TokenParser;
import steve6472.scriptit.tokenizer.Tokenizer;
import steve6472.scriptit.UnaryOperator;
import steve6472.scriptit.tokenizer.Precedence;
import steve6472.scriptit.tokenizer.PrefixParselet;

/**********************
 * Created by steve6472
 * On date: 12/26/2021
 * Project: ScriptIt
 *
 ***********************/
public class UnaryParselet implements PrefixParselet
{
	public UnaryParselet()
	{

	}

	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token)
	{
		return new UnaryOperator(token.type(), parser.parse(Precedence.UNARY));
	}
}
