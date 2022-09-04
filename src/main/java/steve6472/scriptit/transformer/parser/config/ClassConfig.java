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

	public Setting type;
	public Setting objectSetting;

	public List<MethodConfig> methods = new ArrayList<>();
	public List<FieldConfig> fields = new ArrayList<>();
}
