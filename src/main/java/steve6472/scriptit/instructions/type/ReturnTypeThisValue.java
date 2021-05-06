package steve6472.scriptit.instructions.type;

import steve6472.scriptit.Script;
import steve6472.scriptit.expression.Value;
import steve6472.scriptit.instructions.ReturnValue;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/2/2021
 * Project: ScriptIt
 *
 ***********************/
public class ReturnTypeThisValue extends ReturnValue
{
	public Value thisValue;

	public ReturnTypeThisValue()
	{
		super();
	}

	@Override
	public Value execute(Script script)
	{
		return thisValue;
	}
}
