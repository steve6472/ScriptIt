package steve6472.scriptit.expressions;

import steve6472.scriptit.Highlighter;
import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.ScriptItSettings;
import steve6472.scriptit.transformer.JavaTransformer;
import steve6472.scriptit.transformer.parser.config.Config;

import java.io.File;
import java.util.List;

/**
 * Created by steve6472
 * Date: 11/25/2022
 * Project: ScriptIt <br>
 */
public class TransformerUse extends Expression
{
	private final boolean isCustom;
	private final String name;

	public TransformerUse(String path, boolean isCustom)
	{
		this.name = path;
		this.isCustom = isCustom;
	}

	@Override
	public Result apply(Script script)
	{
		List<Config> configs;
		if (isCustom)
		{
			if (!ScriptItSettings.ALLOW_UNSAFE_CUSTOM_TRANSFORMER)
			{
				throw new RuntimeException("Unsafe operation detected! Allow unsafe or remove operation. (transformer)");
			} else
			{
				configs = script.schemeParser.createConfigs(new File(name));
			}
		} else
		{
			configs = script.getWorkspace().getTransformer(name);
		}

		script.transformerConfigs.addAll(configs);
		JavaTransformer.addClasses(script);

		return Result.pass();
	}

	@Override
	public String showCode(int a)
	{
		return Highlighter.IMPORT + "transformer " + Highlighter.CONST_STR + name + Highlighter.RESET;
	}
}
