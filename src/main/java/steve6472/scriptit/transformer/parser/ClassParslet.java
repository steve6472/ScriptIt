package steve6472.scriptit.transformer.parser;

import steve6472.scriptit.Log;
import steve6472.scriptit.ScriptItSettings;
import steve6472.scriptit.newtokenizer.PrefixParselet;
import steve6472.scriptit.newtokenizer.TokenParser;
import steve6472.scriptit.newtokenizer.Tokenizer;
import steve6472.scriptit.transformer.SchemeParser;
import steve6472.scriptit.transformer.parser.config.*;

import java.util.List;
import java.util.function.Supplier;

/**
 * Created by steve6472
 * Date: 9/3/2022
 * Project: ScriptIt
 */
public class ClassParslet implements PrefixParselet<Config, SchemeParser.Data>
{
	@Override
	public Config parse(Tokenizer tokenizer, TokenParser<Config, SchemeParser.Data> parser, SchemeParser.Data data)
	{
		PathConfig path = parser.parse(PathConfig.class);
		tokenizer.matchToken(SchemeParser.Token.OP, true);
		tokenizer.matchToken(SchemeParser.Token.START, true);

		ClassConfig classConfig = new ClassConfig();
		classConfig.path = path.getPath();

		Supplier<Boolean> iteratingCondition = parser.iteratingCondition;
		parser.setIteratingCondition(() -> !tokenizer.matchToken(SchemeParser.Token.END, true));
		List<Config> configs = parser.parseAll();
		parser.setIteratingCondition(iteratingCondition);

		configs.forEach(c ->
		{
			if (c instanceof TypeConfig tc)
			{
				if (tc.getType() == SettingType.CLASS)
					classConfig.type = tc.getSetting();
				else if (tc.getType() == SettingType.OBJECT)
					classConfig.objectSetting = tc.getSetting();
			}
			else if (c instanceof MethodsConfig mc)
			{
				classConfig.methods = mc.configs;
			}
			else if (c instanceof FieldsConfig fc)
			{
				classConfig.fields = fc.configs;
			}
			else
			{
				throw new RuntimeException("Config not allowed " + c.getClass().getSimpleName());
			}
		});

//		Log.scriptDebug(ScriptItSettings.SCHEME_PARSER_DEBUG, "ClassConfig: " + classConfig);

		return classConfig;
	}
}
