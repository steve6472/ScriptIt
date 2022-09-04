package steve6472.scriptit.transformer.parser.config;

/**
 * Created by steve6472
 * Date: 9/3/2022
 * Project: ScriptIt
 */
public class AliasConfig extends Config
{
	private final String javaPath;
	private final String alias;

	public AliasConfig(String javaPath, String alias)
	{
		this.javaPath = javaPath;
		this.alias = alias;
	}

	public String getJavaPath()
	{
		return javaPath;
	}

	public String getAlias()
	{
		return alias;
	}
}
