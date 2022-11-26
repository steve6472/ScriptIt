package steve6472.scriptit.transformer.parser;

import steve6472.scriptit.newtokenizer.PrefixParselet;
import steve6472.scriptit.newtokenizer.TokenParser;
import steve6472.scriptit.newtokenizer.Tokenizer;
import steve6472.scriptit.transformer.SchemeParser;
import steve6472.scriptit.transformer.parser.config.Config;
import steve6472.scriptit.transformer.parser.config.Setting;
import steve6472.scriptit.transformer.parser.config.SettingType;
import steve6472.scriptit.transformer.parser.config.TypeConfig;

/**
 * Created by steve6472
 * Date: 9/3/2022
 * Project: ScriptIt
 */
public class TypeParslet implements PrefixParselet<Config, SchemeParser.Data>
{
	@Override
	public Config parse(Tokenizer tokenizer, TokenParser<Config, SchemeParser.Data> parser, SchemeParser.Data data)
	{
		if (tokenizer.getCurrentToken().type() == SchemeParser.Token.TYPE)
		{
			tokenizer.matchToken(SchemeParser.Token.OP, true);
			Setting setting = Setting.valueOf(tokenizer.nextToken().sval().toUpperCase());
			return new TypeConfig(setting, SettingType.CLASS);
		}
		else if (tokenizer.getCurrentToken().type() == SchemeParser.Token.OBJECT)
		{
			tokenizer.matchToken(SchemeParser.Token.OP, true);
			Setting setting = Setting.valueOf(tokenizer.nextToken().sval().toUpperCase());
			return new TypeConfig(setting, SettingType.OBJECT);
		}
		else
		{
			throw new RuntimeException("" + tokenizer.getCurrentToken());
		}
	}
}
