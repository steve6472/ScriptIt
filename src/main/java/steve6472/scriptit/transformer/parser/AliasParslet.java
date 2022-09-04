package steve6472.scriptit.transformer.parser;

import steve6472.scriptit.newtokenizer.MainTokens;
import steve6472.scriptit.newtokenizer.PrefixParselet;
import steve6472.scriptit.newtokenizer.TokenParser;
import steve6472.scriptit.newtokenizer.Tokenizer;
import steve6472.scriptit.transformer.SchemeParser;
import steve6472.scriptit.transformer.parser.config.AliasConfig;
import steve6472.scriptit.transformer.parser.config.Config;
import steve6472.scriptit.transformer.parser.config.PathConfig;

/**
 * Created by steve6472
 * Date: 9/3/2022
 * Project: ScriptIt
 */
public class AliasParslet implements PrefixParselet<Config, SchemeParser.Data>
{
	@Override
	public Config parse(Tokenizer tokenizer, TokenParser<Config, SchemeParser.Data> parser, SchemeParser.Data data)
	{
		PathConfig path = parser.parse(PathConfig.class);
		tokenizer.matchToken(SchemeParser.Token.OP, true);
		tokenizer.matchToken(MainTokens.NAME, true);

		System.out.println("Alias path = " + path.getPath() + ": " + tokenizer.getCurrentToken().sval());
		return new AliasConfig(path.getPath(), tokenizer.getCurrentToken().sval());
	}
}
