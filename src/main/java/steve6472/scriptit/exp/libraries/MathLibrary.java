package steve6472.scriptit.exp.libraries;

import steve6472.scriptit.exp.PrimitiveTypes;
import steve6472.scriptit.exp.Value;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/22/2021
 * Project: ScriptIt
 *
 ***********************/
public class MathLibrary extends Library
{
	public MathLibrary()
	{
		super("Math");

		addFunction("sqrt", d -> Value.newValue(PrimitiveTypes.DOUBLE, Math.sqrt(d.getDouble())), PrimitiveTypes.DOUBLE);
	}
}
