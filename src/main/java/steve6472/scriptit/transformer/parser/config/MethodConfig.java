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
	public Setting setting;

	public List<String> arguments = new ArrayList<>();
}
