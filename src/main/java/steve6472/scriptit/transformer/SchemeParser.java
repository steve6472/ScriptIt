package steve6472.scriptit.transformer;

import steve6472.scriptit.newtokenizer.IToken;
import steve6472.scriptit.newtokenizer.MainTokens;
import steve6472.scriptit.newtokenizer.TokenParser;
import steve6472.scriptit.newtokenizer.TokenStorage;
import steve6472.scriptit.transformer.parser.config.AliasConfig;
import steve6472.scriptit.transformer.parser.config.ClassConfig;
import steve6472.scriptit.transformer.parser.*;
import steve6472.scriptit.transformer.parser.config.Config;

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
	public static final MethodParslet METHOD_PARSLET = new MethodParslet();
	public static final FieldParslet FIELD_PARSLET = new FieldParslet();

	public static void main(String[] args) throws IOException
	{
		TokenStorage tokenStorage = new TokenStorage();
		tokenStorage.addTokens(Token.class);

		TokenParser<Config, Data> parser = new TokenParser<>(tokenStorage);

		parser.tokenize(new Data(), Files.readString(new File("!tests/transformer/scheme.txt").toPath()));

		parser.prefixParslet(Token.ALIAS, new AliasParslet());
		parser.prefixParslet(MainTokens.NAME, new PathParslet());
		parser.prefixParslet(Token.CLASS, new ClassParslet());
		parser.prefixParslet(Token.TYPE, new TypeParslet());
		parser.prefixParslet(Token.OBJECT, new TypeParslet());
		parser.prefixParslet(Token.METHODS, new MethodsParslet());
		parser.prefixParslet(Token.FIELDS, new FieldsParslet());


		parser.prefixParslet(Token.COMMENT, new CommentParslet());
		parser.prefixParslet(Token.MULTI_COMMENT_START, new MultiCommentParslet());

		List<Config> configs = parser.parseAll();

		Map<String, String> pathMap = new HashMap<>();

		for (Config config : configs)
		{
			if (config instanceof AliasConfig ac)
			{
				pathMap.put(ac.getAlias(), ac.getJavaPath());
			}

			if (config instanceof ClassConfig cc)
			{
				cc.path = pathMap.getOrDefault(cc.path, cc.path);
			}
		}
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
