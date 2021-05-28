package steve6472.scriptit;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/28/2021
 * Project: ScriptIt
 *
 ***********************/
public class DeclareFunction extends Expression
{
	Function function;
	FunctionParameters params;

	public DeclareFunction(String name, Function function, String[] argumentNames, Type[] argumentTypes)
	{
		this.function = function;
		this.function.argumentNames = argumentNames;
		this.params = FunctionParameters.create(name, argumentTypes);
	}

	@Override
	public Result apply(Script script)
	{
		script.getMemory().addFunction(params, function);

		return Result.pass();
	}
}
