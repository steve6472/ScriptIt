package steve6472.scriptit.expressions;

import steve6472.scriptit.*;
import steve6472.scriptit.value.Value;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
public class Variable extends Expression
{
	public String variableName;

	public Variable(String variableName)
	{
		this.variableName = variableName;
	}

	public Value getValue(Script script)
	{
		return script.memory.getVariable(variableName);
	}

	@Override
	public Result apply(Script script)
	{
		return Result.value(getValue(script));
	}

	@Override
	public String toString()
	{
		return "Variable{" + "source=" + variableName + '}';
	}

	@Override
	public String showCode(int a)
	{
		return Highlighter.VAR + variableName + Highlighter.RESET;
	}
}
