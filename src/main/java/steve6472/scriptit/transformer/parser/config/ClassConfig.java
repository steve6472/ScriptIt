package steve6472.scriptit.transformer.parser.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve6472
 * Date: 9/4/2022
 * Project: ScriptIt
 */
public class ClassConfig extends Config
{
	public String path;

	public Setting type = Setting.DENY;
	public Setting objectSetting = Setting.DENY;

	public List<MethodConfig> methods = new ArrayList<>();
	public List<FieldConfig> fields = new ArrayList<>();

	public boolean isFullyDisabled()
	{
		if (type != Setting.DENY)
		{
			return false;
		} else
		{
			for (MethodConfig method : methods)
			{
				if (method.setting != Setting.DENY)
					return false;
			}
			for (FieldConfig field : fields)
			{
				if (field.setting != Setting.DENY)
					return false;
			}
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "ClassConfig{" + "path='" + path + '\'' + ", type=" + type + ", objectSetting=" + objectSetting + ", methods=" + methods + ", fields=" + fields + '}';
	}
}
