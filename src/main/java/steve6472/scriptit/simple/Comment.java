package steve6472.scriptit.simple;

import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.expressions.Expression;

/**********************
 * Created by steve6472
 * On date: 7/23/2022
 * Project: ScriptIt
 *
 ***********************/
public class Comment extends Expression
{
	public static final Comment INSTANCE = new Comment();

	private Comment()
	{

	}

	public static Comment getInstance()
	{
		return INSTANCE;
	}

	@Override
	public Result apply(Script script)
	{
		return Result.pass();
	}

	@Override
	public String showCode(int a)
	{
		return "";
	}
}
