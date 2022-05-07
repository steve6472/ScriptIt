package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.expressions.Expression;
import steve6472.scriptit.simple.ReturnIf;
import steve6472.scriptit.tokenizer.*;

/**********************
 * Created by steve6472
 * On date: 12/28/2021
 * Project: ScriptIt
 *
 ***********************/
public class ReturnIfParslet implements PrefixParselet
{
	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token)
	{
		parser.tokenizer.consumeToken(Operator.BRACKET_LEFT);
		Expression condition = parser.parse(Precedence.ANYTHING);
		parser.tokenizer.consumeToken(Operator.BRACKET_RIGHT);
		return new ReturnIf(condition);
	}
}
