package steve6472.scriptit.exp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
class Variable extends Expression
{
	public VariableSource source;

	public Variable(VariableSource source)
	{
		this.source = source;
	}

	@Override
	public Result apply(Main.Script script)
	{
		if (source.sourceType == VariableSourceType.MEMORY)
			return Result.value(script.currentExecutor().memory.getVariable(source.variableName));
		else
			return Result.value(source.value.getValue(source.variableName));
	}

	@Override
	public String toString()
	{
		return "Variable{" + "source=" + source + '}';
	}
}
