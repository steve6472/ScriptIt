package steve6472.scriptit.transformer.parser;

import steve6472.scriptit.newtokenizer.PrefixParselet;
import steve6472.scriptit.newtokenizer.TokenParser;
import steve6472.scriptit.newtokenizer.Tokenizer;
import steve6472.scriptit.transformer.SchemeParser;
import steve6472.scriptit.transformer.parser.config.Config;
import steve6472.scriptit.transformer.parser.config.MethodConfig;
import steve6472.scriptit.transformer.parser.config.Setting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve6472
 * Date: 9/3/2022
 * Project: ScriptIt
 */
public class MethodParslet implements PrefixParselet<Config, SchemeParser.Data>
{
	@Override
	public Config parse(Tokenizer tokenizer, TokenParser<Config, SchemeParser.Data> parser, SchemeParser.Data data)
	{
		String returnType = tokenizer.nextToken().sval();
		String name = tokenizer.nextToken().sval();
		tokenizer.matchToken(SchemeParser.Token.PARAMETERS_START, true);
		MethodConfig methodConfig = new MethodConfig();
		methodConfig.returnType = returnType;
		methodConfig.name = name;

		if (!tokenizer.matchToken(SchemeParser.Token.PARAMETERS_END, true))
		{
			while (!tokenizer.matchToken(SchemeParser.Token.PARAMETERS_END, true))
			{
				methodConfig.arguments.add(tokenizer.nextToken().sval());
			}
		}
		tokenizer.matchToken(SchemeParser.Token.OP, true);
		methodConfig.setting = Setting.valueOf(tokenizer.nextToken().sval().toUpperCase());

		return methodConfig;
	}
}
