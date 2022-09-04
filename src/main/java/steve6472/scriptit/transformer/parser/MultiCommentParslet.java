package steve6472.scriptit.transformer.parser;

import steve6472.scriptit.newtokenizer.PrefixParselet;
import steve6472.scriptit.newtokenizer.TokenParser;
import steve6472.scriptit.newtokenizer.Tokenizer;
import steve6472.scriptit.transformer.SchemeParser;
import steve6472.scriptit.transformer.parser.config.CommentInformation;
import steve6472.scriptit.transformer.parser.config.Config;

/**
 * Created by steve6472
 * Date: 9/3/2022
 * Project: ScriptIt
 */
public class MultiCommentParslet implements PrefixParselet<Config, SchemeParser.Data>
{
	@Override
	public Config parse(Tokenizer tokenizer, TokenParser<Config, SchemeParser.Data> parser, SchemeParser.Data data)
	{
		while (!tokenizer.matchToken(SchemeParser.Token.MULTI_COMMENT_END, true))
		{
			tokenizer.nextToken();
		}

		return new CommentInformation();
	}
}
