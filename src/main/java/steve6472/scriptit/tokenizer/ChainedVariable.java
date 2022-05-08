package steve6472.scriptit.tokenizer;

import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.expressions.Expression;

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
