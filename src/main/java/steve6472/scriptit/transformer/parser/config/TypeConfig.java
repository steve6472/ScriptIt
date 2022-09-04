package steve6472.scriptit.transformer.parser.config;

/**
 * Created by steve6472
 * Date: 9/4/2022
 * Project: ScriptIt
 */
public class TypeConfig extends Config
{
	final Setting setting;
	final SettingType type;

	public TypeConfig(Setting setting, SettingType type)
	{
		this.setting = setting;
		this.type = type;
	}

	public Setting getSetting()
	{
		return setting;
	}

	public SettingType getType()
	{
		return type;
	}
}
