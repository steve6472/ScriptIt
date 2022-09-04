package steve6472.scriptit.transformer.parser;

import steve6472.scriptit.newtokenizer.MainTokens;
import steve6472.scriptit.newtokenizer.PrefixParselet;
import steve6472.scriptit.newtokenizer.TokenParser;
import steve6472.scriptit.newtokenizer.Tokenizer;
import steve6472.scriptit.transformer.SchemeParser;
import steve6472.scriptit.transformer.parser.config.Config;
import steve6472.scriptit.transformer.parser.config.PathConfig;

/**
 * Created by steve6472
 * Date: 9/3/2022
 * Project: ScriptIt
 */
public class PathParslet implements PrefixParselet<Config, SchemeParser.Data>
{
	@Override
	public Config parse(Tokenizer tokenizer, TokenParser<Config, SchemeParser.Data> parser, SchemeParser.Data data)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(tokenizer.getCurrentToken().sval());

		boolean expectName = false;

		while (true)
		{
			if (expectName)
			{
				if (tokenizer.matchToken(MainTokens.NAME, true))
				{
					sb.append(tokenizer.getCurrentToken().sval());
				}
				else
				{
					return new PathConfig(sb.toString());
				}
			}

			if (!expectName)
			{
				if (tokenizer.matchToken(SchemeParser.Token.PATH_JOIN, true))
				{
					sb.append(".");
				}
				else
				{
					return new PathConfig(sb.toString());
				}
			}

			expectName = !expectName;
		}
	}
}
