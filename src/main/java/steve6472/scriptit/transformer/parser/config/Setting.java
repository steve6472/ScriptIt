package steve6472.scriptit.transformer.parser.config;

/**
 * Created by steve6472
 * Date: 9/4/2022
 * Project: ScriptIt
 */
public enum Setting
{
	DENY("deny"),
	ALLOW("allow"),

	ALLOW_METHODS("allow_methods"),
	ALLOW_FIELDS("allow_fields")

	;

	final String label;

	Setting(String label)
	{
		this.label = label;
	}

	public String getLabel()
	{
		return label;
	}
}
