package steve6472.scriptit.transformer.parser.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve6472
 * Date: 9/4/2022
 * Project: ScriptIt
 */
public class MethodConfig extends Config
{
	public String returnType;
	public String name;
	public Setting setting = Setting.DENY;

	public List<String> arguments = new ArrayList<>();

	@Override
	public String toString()
	{
		return "MethodConfig{" + "returnType='" + returnType + '\'' + ", name='" + name + '\'' + ", setting=" + setting + ", arguments=" + arguments + '}';
	}
}
