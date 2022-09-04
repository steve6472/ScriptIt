package steve6472.scriptit.transformer.parser;

import steve6472.scriptit.newtokenizer.PrefixParselet;
import steve6472.scriptit.newtokenizer.TokenParser;
import steve6472.scriptit.newtokenizer.Tokenizer;
import steve6472.scriptit.transformer.SchemeParser;
import steve6472.scriptit.transformer.parser.config.Config;
import steve6472.scriptit.transformer.parser.config.FieldConfig;
import steve6472.scriptit.transformer.parser.config.Setting;

/**
 * Created by steve6472
 * Date: 9/3/2022
 * Project: ScriptIt
 */
public class FieldParslet implements PrefixParselet<Config, SchemeParser.Data>
{
	@Override
	public Config parse(Tokenizer tokenizer, TokenParser<Config, SchemeParser.Data> parser, SchemeParser.Data data)
	{
		String type = tokenizer.nextToken().sval();
		String name = tokenizer.nextToken().sval();
		tokenizer.matchToken(SchemeParser.Token.OP, true);
		Setting setting = Setting.valueOf(tokenizer.nextToken().sval().toUpperCase());

		FieldConfig fieldConfig = new FieldConfig();
		fieldConfig.type = type;
		fieldConfig.name = name;
		fieldConfig.setting = setting;
		return fieldConfig;
	}
}
