package steve6472.scriptit.simple;

import steve6472.scriptit.expressions.Expression;
import steve6472.scriptit.Highlighter;
import steve6472.scriptit.Result;
import steve6472.scriptit.Script;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
public class Return extends Expression
{
	Expression returnValue;

	public Return(Expression expression)
	{
		this.returnValue = expression;
	}

	@Override
	public Result apply(Script script)
	{
		Result result = returnValue.apply(script);
		if (result.isDelay())
		{
			return result;
		}

		return Result.returnValue(result.getValue());
	}

	@Override
	public String showCode(int a)
	{
		return Highlighter.RETURN + "return " + Highlighter.RESET + returnValue.showCode(a);
	}

	@Override
	public String toString()
	{
		return "Return{" + "returnValue=" + returnValue + '}';
	}
}
