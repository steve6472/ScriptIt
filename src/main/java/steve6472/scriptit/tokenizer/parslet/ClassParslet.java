package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.expressions.Expression;
import steve6472.scriptit.tokenizer.Operator;
import steve6472.scriptit.tokenizer.PrefixParselet;
import steve6472.scriptit.tokenizer.TokenParser;
import steve6472.scriptit.tokenizer.Tokenizer;

/**********************
 * Created by steve6472
 * On date: 5/7/2022
 * Project: ScriptIt
 *
 ***********************/
public class ClassParslet implements PrefixParselet
{
	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token)
	{
		String name = parser.tokenizer.nextToken().sval();
		if (parser.tokenizer.peekToken().type() == Operator.EXTENDS)
		{
			throw new RuntimeException("Inheritance not yet implemented");
		}

		parser.tokenizer.consumeToken(Operator.BRACKET_CURLY_LEFT);

		return null;
	}
}
