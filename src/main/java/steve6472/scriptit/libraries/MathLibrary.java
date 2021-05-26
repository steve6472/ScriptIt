package steve6472.scriptit.libraries;

import steve6472.scriptit.types.PrimitiveTypes;
import steve6472.scriptit.Value;

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

		addFunction("clamp", (number, min, max) -> Value.newValue(PrimitiveTypes.DOUBLE, Math.min(Math.max(number.getDouble(), min.getDouble()), max.getDouble())), PrimitiveTypes.DOUBLE, PrimitiveTypes.DOUBLE, PrimitiveTypes.DOUBLE);
		addFunction("clamp", (number, min, max) -> Value.newValue(PrimitiveTypes.INT, Math.min(Math.max(number.getInt(), min.getInt()), max.getInt())), PrimitiveTypes.INT, PrimitiveTypes.INT, PrimitiveTypes.INT);

		addFunction("lerp", (start, end, value) -> Value.newValue(PrimitiveTypes.DOUBLE, start.getDouble() + value.getDouble() * (end.getDouble() - start.getDouble())), PrimitiveTypes.DOUBLE, PrimitiveTypes.DOUBLE, PrimitiveTypes.DOUBLE);

		addFunction("isEven", number -> Value.newValue(PrimitiveTypes.DOUBLE, number.getDouble() % 2.0 == 0 ? PrimitiveTypes.TRUE : PrimitiveTypes.FALSE), PrimitiveTypes.DOUBLE);
		addFunction("isEven", number -> Value.newValue(PrimitiveTypes.INT, number.getInt() % 2 == 0 ? PrimitiveTypes.TRUE : PrimitiveTypes.FALSE), PrimitiveTypes.INT);

	}
}
