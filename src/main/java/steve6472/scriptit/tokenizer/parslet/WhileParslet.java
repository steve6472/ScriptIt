package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.*;
import steve6472.scriptit.tokenizer.*;

/**********************
 * Created by steve6472
 * On date: 12/26/2021
 * Project: ScriptIt
 *
 ***********************/
public class WhileParslet implements PrefixParselet
{
	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token)
	{
		parser.tokenizer.consumeToken(Operator.BRACKET_LEFT);
		Expression condition = parser.parse(Precedence.ANYTHING);
		parser.tokenizer.consumeToken(Operator.BRACKET_RIGHT);
		Expression parse = parser.parse(Precedence.ANYTHING);
		return new While(condition, parse);
	}
}
