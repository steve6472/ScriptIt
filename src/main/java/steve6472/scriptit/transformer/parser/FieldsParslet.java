package steve6472.scriptit.transformer.parser;

import steve6472.scriptit.newtokenizer.PrefixParselet;
import steve6472.scriptit.newtokenizer.TokenParser;
import steve6472.scriptit.newtokenizer.Tokenizer;
import steve6472.scriptit.transformer.SchemeParser;
import steve6472.scriptit.transformer.config.*;
import steve6472.scriptit.transformer.parser.config.Config;
import steve6472.scriptit.transformer.parser.config.FieldConfig;
import steve6472.scriptit.transformer.parser.config.FieldsConfig;

import java.util.function.Supplier;

/**
 * Created by steve6472
 * Date: 9/3/2022
 * Project: ScriptIt
 */
public class FieldsParslet implements PrefixParselet<Config, SchemeParser.Data>
{
	@Override
	public Config parse(Tokenizer tokenizer, TokenParser<Config, SchemeParser.Data> parser, SchemeParser.Data data)
	{
		tokenizer.matchToken(SchemeParser.Token.OP, true);
		tokenizer.matchToken(SchemeParser.Token.START, true);

		FieldsConfig fieldsConfig = new FieldsConfig();

		Supplier<Boolean> iteratingCondition = parser.iteratingCondition;
		parser.setIteratingCondition(() -> !tokenizer.matchToken(SchemeParser.Token.END, true));

		do
		{
			FieldConfig parse = (FieldConfig) parser.parse(TokenParser.MIN_PRECEDENCE, SchemeParser.FIELD_PARSLET);
			fieldsConfig.configs.add(parse);

		} while (parser.iteratingCondition.get());

		parser.setIteratingCondition(iteratingCondition);

		return fieldsConfig;
	}
}
