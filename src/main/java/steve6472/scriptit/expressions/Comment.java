package steve6472.scriptit.expressions;

import steve6472.scriptit.Result;
import steve6472.scriptit.Script;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 6/20/2021
 * Project: ScriptIt
 *
 ***********************/
public class Comment extends Expression
{
	private final String comment;

	public Comment(String comment)
	{
		this.comment = comment;
	}

	@Override
	public Result apply(Script script)
	{
		return Result.pass();
	}

	@Override
	public String showCode(int a)
	{
		return comment;
	}
}
