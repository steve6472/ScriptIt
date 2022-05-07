package steve6472.scriptit.expressions;

import steve6472.scriptit.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
public class Variable extends Expression
{
	public VariableSource source;

	public Variable(VariableSource source)
	{
		this.source = source;
	}

	@Override
	public Result apply(Script script)
	{
		if (source.sourceType == VariableSourceType.MEMORY)
			return Result.value(script.memory.getVariable(source.variableName));
		else
			return Result.value(source.value.getValue(source.variableName));
	}

	@Override
	public String toString()
	{
		return "Variable{" + "source=" + source + '}';
	}

	@Override
	public String showCode(int a)
	{
		return Highlighter.VAR + source.variableName + Highlighter.RESET;
	}
}
