package steve6472.scriptit.transformer;

import steve6472.scriptit.Log;
import steve6472.scriptit.ScriptItSettings;
import steve6472.scriptit.newtokenizer.IToken;
import steve6472.scriptit.newtokenizer.MainTokens;
import steve6472.scriptit.newtokenizer.TokenParser;
import steve6472.scriptit.newtokenizer.TokenStorage;
import steve6472.scriptit.transformer.parser.config.*;
import steve6472.scriptit.transformer.parser.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by steve6472
 * Date: 9/3/2022
 * Project: ScriptIt
 */
public class SchemeParser
{
	public static final Map<String, String> JAVA_ALIAS = Map.of(
		"String", String.class.getName(),
		"int", Integer.class.getName(),
		"bool", Boolean.class.getName(),
		"boolean", Boolean.class.getName(),
		"double", Double.class.getName(),
		"float", Float.class.getName(),
		"byte", Byte.class.getName(),
		"short", Short.class.getName(),
		"char", Character.class.getName(),
		"Object", Object.class.getName()
	);

	public static final MethodParslet METHOD_PARSLET = new MethodParslet();
	public static final FieldParslet FIELD_PARSLET = new FieldParslet();

	private final TokenStorage tokenStorage;
	private TokenParser<Config, Data> parser;

	public SchemeParser()
	{
		tokenStorage = new TokenStorage();
		tokenStorage.addTokens(Token.class);

		parser = new TokenParser<>(tokenStorage);

		parser.prefixParslet(Token.ALIAS, new AliasParslet());
		parser.prefixParslet(MainTokens.NAME, new PathParslet());
		parser.prefixParslet(Token.CLASS, new ClassParslet());
		parser.prefixParslet(Token.TYPE, new TypeParslet());
		parser.prefixParslet(Token.OBJECT, new TypeParslet());
		parser.prefixParslet(Token.METHODS, new MethodsParslet());
		parser.prefixParslet(Token.FIELDS, new FieldsParslet());


		parser.prefixParslet(Token.COMMENT, new CommentParslet());
		parser.prefixParslet(Token.MULTI_COMMENT_START, new MultiCommentParslet());
	}

	public List<Config> createConfigs(File schemeFile)
	{
		try
		{
			parser.tokenize(new Data(), Files.readString(schemeFile.toPath()));
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		}

		List<Config> configs = parser.parseAll();

		applyAlias(configs);

		// Clear useless configs
		configs.removeIf(c -> c instanceof CommentInformation);

		for (Config config : configs)
		{
			Log.scriptDebug(ScriptItSettings.SCHEME_PARSER_DEBUG, "" + config);
		}

		return configs;
	}

	private void applyAliasToConfig(Map<String, String> pathMap, Config config)
	{
		if (config instanceof AliasConfig ac) pathMap.put(ac.getAlias(), ac.getJavaPath());
		if (config instanceof MethodsConfig msc) msc.configs.forEach(mc -> applyAliasToConfig(pathMap, mc));
		if (config instanceof FieldConfig fc) fc.type = getAlias(pathMap, fc.type);
		if (config instanceof FieldsConfig fsc) fsc.configs.forEach(fc -> applyAliasToConfig(pathMap, fc));

		if (config instanceof MethodConfig mc)
		{
			mc.returnType = getAlias(pathMap, mc.returnType);
			mc.arguments.replaceAll(s -> getAlias(pathMap, s));
		}

		if (config instanceof ClassConfig cc)
		{
			cc.path = getAlias(pathMap, cc.path);
			cc.methods.forEach(mc -> applyAliasToConfig(pathMap, mc));
			cc.fields.forEach(fc -> applyAliasToConfig(pathMap, fc));
		}
	}

	public void applyAlias(List<Config> configs)
	{
		Map<String, String> pathMap = new HashMap<>();

		for (Config config : configs)
		{
			applyAliasToConfig(pathMap, config);
		}
	}

	private String getAlias(Map<String, String> pathMap, String original)
	{
		// Either use the defined alias, use Java alias or use provided path
		String s = pathMap.get(original);
		if (s != null)
		{
			return s;
		} else
		{
			return JAVA_ALIAS.getOrDefault(original, original);
		}
	}

	public static void main(String[] args) throws IOException
	{
		SchemeParser schemeParser = new SchemeParser();
		schemeParser.createConfigs(new File("!tests/transformer/scheme.txt"));
	}

	public record Data() {}

	public enum Token implements IToken
	{
		ALIAS("alias"),
		CLASS("class"),
		TYPE("type"),
		OBJECT("object"),
		METHODS("methods"),
		FIELDS("fields"),

		DENY("dely"),
		ALLOW("allow"),

		ALLOW_METHODS("allow_methods"),
		ALLOW_FIELDS("allow_fields"),

		PATH_JOIN("."),
		OP(":"),
		COMMENT("//", true),
		MULTI_COMMENT_START("/*", true),
		MULTI_COMMENT_END("*/", true),
		START("{"),
		END("}"),
		PARAMETERS_START("("),
		PARAMETERS_SEPARATOR(","),
		PARAMETERS_END(")"),

		;

		private final String symbol;
		private final boolean isMerge;

		Token()
		{
			this("", false);
		}

		Token(String symbol)
		{
			this(symbol, false);
		}

		Token(String symbol, boolean isMerge)
		{
			this.symbol = symbol;
			this.isMerge = isMerge;
		}

		@Override
		public String getSymbol()
		{
			return symbol;
		}

		@Override
		public boolean isMerge()
		{
			return isMerge;
		}
	}
}
