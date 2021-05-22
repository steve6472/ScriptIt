package steve6472.scriptit.exp.functions;

import steve6472.scriptit.exp.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/22/2021
 * Project: ScriptIt
 *
 ***********************/
public class NewVec2 extends Function
{
	public NewVec2()
	{
		super((String[]) null);
	}

	@Override
	public Result apply(Main.Script script)
	{
		return Result.returnValue(Value.newValue(PrimitiveTypes.VEC2).setValue("x", arguments[0]).setValue("y", arguments[1]));
	}
}
