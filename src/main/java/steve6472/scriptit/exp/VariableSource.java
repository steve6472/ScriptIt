package steve6472.scriptit.exp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/22/2021
 * Project: ScriptIt
 *
 ***********************/
public class VariableSource
{
	public final VariableSourceType sourceType;
	public final String variableName;
	public Value value;

	private VariableSource(VariableSourceType sourceType, String variableName, Value value)
	{
		this.sourceType = sourceType;
		this.variableName = variableName;
		this.value = value;
	}

	public static VariableSource memory(String name)
	{
		return new VariableSource(VariableSourceType.MEMORY, name, Value.NULL);
	}

	public static VariableSource value(String name, Value value)
	{
		return new VariableSource(VariableSourceType.VALUE, name, value);
	}

	public void setValue(Value value)
	{
		this.value = value;
	}
}
