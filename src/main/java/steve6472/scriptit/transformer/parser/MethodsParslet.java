package steve6472.scriptit.transformer.parser;

import steve6472.scriptit.newtokenizer.PrefixParselet;
import steve6472.scriptit.newtokenizer.TokenParser;
import steve6472.scriptit.newtokenizer.Tokenizer;
import steve6472.scriptit.transformer.SchemeParser;
import steve6472.scriptit.transformer.parser.config.Config;
import steve6472.scriptit.transformer.parser.config.MethodConfig;
import steve6472.scriptit.transformer.parser.config.MethodsConfig;

import java.util.function.Supplier;

/**
 * Created by steve6472
 * Date: 9/3/2022
 * Project: ScriptIt
 */
public class MethodsParslet implements PrefixParselet<Config, SchemeParser.Data>
{
	@Override
	public Config parse(Tokenizer tokenizer, TokenParser<Config, SchemeParser.Data> parser, SchemeParser.Data data)
	{
		tokenizer.matchToken(SchemeParser.Token.OP, true);
		tokenizer.matchToken(SchemeParser.Token.START, true);

		MethodsConfig methodsConfig = new MethodsConfig();

		Supplier<Boolean> iteratingCondition = parser.iteratingCondition;
		parser.setIteratingCondition(() -> !tokenizer.matchToken(SchemeParser.Token.END, true));

		do
		{
			MethodConfig parse = (MethodConfig) parser.parse(TokenParser.MIN_PRECEDENCE, SchemeParser.METHOD_PARSLET);
			methodsConfig.configs.add(parse);

		} while (parser.iteratingCondition.get());

		parser.setIteratingCondition(iteratingCondition);

		return methodsConfig;
	}
}
