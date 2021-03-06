package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.expressions.Expression;
import steve6472.scriptit.expressions.ForLoop;
import steve6472.scriptit.tokenizer.*;

/**********************
 * Created by steve6472
 * On date: 1/10/2022
 * Project: ScriptIt
 *
 ***********************/
public class ForParslet implements PrefixParselet
{
	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token)
	{
		parser.tokenizer.consumeToken(Operator.BRACKET_LEFT);
		Expression init = parser.parse(Precedence.ANYTHING);
		parser.tokenizer.consumeToken(Operator.SEMICOLON);
		Expression condition = parser.parse(Precedence.ANYTHING);
		parser.tokenizer.consumeToken(Operator.SEMICOLON);
		Expression update = parser.parse(Precedence.ANYTHING);
		parser.tokenizer.consumeToken(Operator.BRACKET_RIGHT);
		Expression body = parser.parse(Precedence.ANYTHING);
		return new ForLoop(init, condition, update, body);
	}
}
