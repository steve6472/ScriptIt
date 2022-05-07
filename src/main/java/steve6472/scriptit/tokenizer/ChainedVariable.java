package steve6472.scriptit.tokenizer;

import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.Type;
import steve6472.scriptit.Value;
import steve6472.scriptit.expressions.Expression;
import steve6472.scriptit.expressions.Variable;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 12/19/2021
 * Project: ScriptIt
 *
 ***********************/
public class ChainedVariable extends Expression
{
	public Expression exp1, exp2;

	public ChainedVariable(Expression exp1, Expression exp2)
	{
		this.exp1 = exp1;
		this.exp2 = exp2;
	}

	@Override
	public Result apply(Script script)
	{
		// Try to create un-initialized variable if possible
		if (exp1 instanceof Variable var1 && exp2 instanceof Variable var2)
		{
			Type type = script.memory.getType(var1.source.variableName);
			String variableName = var2.source.variableName;

			if (type != null)
			{
				script.memory.addVariable(variableName, Value.newValue(type));
			}
		}
		return Result.pass();
	}

	@Override
	public String showCode(int a)
	{
		return exp1.showCode(a) + " " + exp2.showCode(a);
	}

	@Override
	public String toString()
	{
		return "ChainedVariable{" + "exp1=" + exp1 + ", exp2=" + exp2 + '}';
	}
}
