package steve6472.scriptit.transformer.parser.config;

/**
 * Created by steve6472
 * Date: 9/3/2022
 * Project: ScriptIt
 */
public class PathConfig extends Config
{
	public final String path;

	public PathConfig(String path)
	{
		this.path = path;
	}

	public String getPath()
	{
		return path;
	}
}
