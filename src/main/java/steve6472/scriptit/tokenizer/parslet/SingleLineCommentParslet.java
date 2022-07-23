package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.expressions.Expression;
import steve6472.scriptit.simple.Comment;
import steve6472.scriptit.tokenizer.PrefixParselet;
import steve6472.scriptit.tokenizer.TokenParser;
import steve6472.scriptit.tokenizer.Tokenizer;

/**********************
 * Created by steve6472
 * On date: 7/23/2022
 * Project: ScriptIt
 *
 ***********************/
public class SingleLineCommentParslet implements PrefixParselet
{
	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token)
	{
		while (!parser.tokenizer.isNextTokenNewLine())
		{
			parser.tokenizer.nextToken();
		}

		return Comment.getInstance();
	}
}
