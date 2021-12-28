package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.*;
import steve6472.scriptit.tokenizer.Operator;
import steve6472.scriptit.tokenizer.Precedence;
import steve6472.scriptit.tokenizer.PrefixParselet;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472
 * On date: 12/26/2021
 * Project: ScriptIt
 *
 ***********************/
public class BodyParslet implements PrefixParselet
{
	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token)
	{
		List<Expression> expressions = new ArrayList<>();
		if (!parser.tokenizer.matchToken(Operator.BRACKET_CURLY_LEFT, true))
		{
			do
			{
				if (parser.tokenizer.matchToken(Operator.BRACKET_CURLY_RIGHT, false))
					break;

				TokenParser.depth++;
				TokenParser.print(Log.CYAN + "---Body Next Expression---" + Log.RESET);
				Expression parse = parser.parse(Precedence.ANYTHING);
				expressions.add(parse);
				TokenParser.depth--;
			} while(parser.tokenizer.matchToken(Operator.SEMICOLON, true));
		}

		parser.tokenizer.consumeToken(Operator.BRACKET_CURLY_RIGHT);

		TokenParser.depth++;
		TokenParser.print(Log.CYAN + "Body: " + Log.RESET);
		TokenParser.depth++;
		expressions.forEach(c -> TokenParser.print(c.toString()));
		TokenParser.depth--;
		TokenParser.depth--;

		Function function = new Function();
		function.setExpressions(parser.script, expressions.toArray(Expression[]::new));
		return function;
	}
}
