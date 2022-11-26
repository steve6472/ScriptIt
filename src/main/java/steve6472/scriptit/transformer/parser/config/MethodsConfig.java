package steve6472.scriptit.transformer.parser.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve6472
 * Date: 9/4/2022
 * Project: ScriptIt
 */
public class MethodsConfig extends Config
{
	public List<MethodConfig> configs = new ArrayList<>();

	@Override
	public String toString()
	{
		return "MethodsConfig{" + "configs=" + configs + '}';
	}
}
