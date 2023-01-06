package steve6472.scriptit.expressions;

import steve6472.scriptit.libraries.Library;
import steve6472.scriptit.value.Value;

/**********************
 * Created by steve6472
 * On date: 5/22/2021
 * Project: ScriptIt
 *
 ***********************/
public class FunctionSource
{
	public final FunctionSourceType sourceType;
	public final String functionName;
	public final Library library;
	public Value value;

	private FunctionSource(FunctionSourceType sourceType, String functionName, Library library, Value value)
	{
		this.sourceType = sourceType;
		this.functionName = functionName;
		this.library = library;
		this.value = value;
	}

	public static FunctionSource function(String functionName)
	{
		return new FunctionSource(FunctionSourceType.FUNCTION, functionName, null, Value.NULL);
	}

	public static FunctionSource staticFunction(String functionName, Library library)
	{
		return new FunctionSource(FunctionSourceType.STATIC, functionName, library, Value.NULL);
	}

	public static FunctionSource dot(String functionName, Value value)
	{
		return new FunctionSource(FunctionSourceType.VALUE, functionName, null, value);
	}

	public void setValue(Value value)
	{
		this.value = value;
	}

	@Override
	public String toString()
	{
		return "FunctionSource{" + "sourceType=" + sourceType + ", functionName='" + functionName + '\'' + ", library=" + library + ", value=" + value + '}';
	}
}
