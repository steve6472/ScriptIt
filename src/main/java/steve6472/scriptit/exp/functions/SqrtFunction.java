package steve6472.scriptit.exp.functions;

import steve6472.scriptit.exp.*;
import steve6472.scriptit.exp.types.PrimitiveTypes;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/21/2021
 * Project: ScriptIt
 *
 ***********************/
public class SqrtFunction extends Function
{
	public SqrtFunction()
	{
		super((String) null);
	}

	@Override
	public Result apply(Script script)
	{
		return Result.returnValue(Value.newValue(PrimitiveTypes.DOUBLE, Math.sqrt(arguments[0].getDouble())));
	}
}
