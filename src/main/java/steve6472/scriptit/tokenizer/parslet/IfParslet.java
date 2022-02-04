package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.*;
import steve6472.scriptit.tokenizer.*;

/**********************
 * Created by steve6472
 * On date: 12/26/2021
 * Project: ScriptIt
 *
 ***********************/
public class IfParslet implements PrefixParselet
{
	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token)
	{
		parser.tokenizer.consumeToken(Operator.BRACKET_LEFT);
		Expression condition = parser.parse(Precedence.ANYTHING);
		parser.tokenizer.consumeToken(Operator.BRACKET_RIGHT);
		Expression parse = parser.parse(Precedence.ANYTHING);

		if (parser.tokenizer.peekToken().type() == Operator.SEMICOLON && parser.tokenizer.peekToken(2).type() == Operator.ELSE)
		{
			parser.tokenizer.consumeToken(Operator.SEMICOLON);
			parser.tokenizer.consumeToken(Operator.ELSE);
			return new IfElse(condition, parse, parser.parse(Precedence.ANYTHING));
		}

		if (parser.tokenizer.matchToken(Operator.ELSE, true))
		{
			return new IfElse(condition, parse, parser.parse(Precedence.ANYTHING));
		} else
		{
			return new If(condition, parse);
		}
	}
}
