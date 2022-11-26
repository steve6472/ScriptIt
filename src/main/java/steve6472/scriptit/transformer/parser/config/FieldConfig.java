package steve6472.scriptit.transformer.parser.config;

/**
 * Created by steve6472
 * Date: 9/4/2022
 * Project: ScriptIt
 */
public class FieldConfig extends Config
{
	public String type;
	public String name;
	public Setting setting = Setting.DENY;

	@Override
	public String toString()
	{
		return "FieldConfig{" + "type='" + type + '\'' + ", name='" + name + '\'' + ", setting=" + setting + '}';
	}
}
