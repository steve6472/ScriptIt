package steve6472.scriptit.libraries;

import static steve6472.scriptit.Value.newValue;
import static steve6472.scriptit.types.PrimitiveTypes.*;

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

		addFunction("PI", () -> newValue(DOUBLE, Math.PI));

		addFunction("sqrt", d -> newValue(DOUBLE, Math.sqrt(d.getDouble())), DOUBLE);
		addFunction("sin", d -> newValue(DOUBLE, Math.sin(d.getDouble())), DOUBLE);
		addFunction("cos", d -> newValue(DOUBLE, Math.cos(d.getDouble())), DOUBLE);

		addFunction("clamp", (number, min, max) -> newValue(DOUBLE, Math.min(Math.max(number.getDouble(), min.getDouble()), max.getDouble())), DOUBLE, DOUBLE, DOUBLE);
		addFunction("clamp", (number, min, max) -> newValue(INT, Math.min(Math.max(number.getInt(), min.getInt()), max.getInt())), INT, INT, INT);

		addFunction("lerp", (start, end, value) -> newValue(DOUBLE, start.getDouble() + value.getDouble() * (end.getDouble() - start.getDouble())), DOUBLE, DOUBLE, DOUBLE);

		addFunction("isEven", number -> newValue(DOUBLE, number.getDouble() % 2.0 == 0 ? TRUE() : FALSE()), DOUBLE);
		addFunction("isEven", number -> newValue(INT, number.getInt() % 2 == 0 ? TRUE() : FALSE()), INT);

	}
}
