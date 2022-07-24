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
		addFunction("asin", d -> newValue(DOUBLE, Math.asin(d.getDouble())), DOUBLE);
		addFunction("acos", d -> newValue(DOUBLE, Math.acos(d.getDouble())), DOUBLE);
		addFunction("toRad", d -> newValue(DOUBLE, Math.toRadians(d.getDouble())), DOUBLE);
		addFunction("toDeg", d -> newValue(DOUBLE, Math.toDegrees(d.getDouble())), DOUBLE);
		addFunction("atan_", (a, b) -> newValue(DOUBLE, Math.atan2(a.getDouble(), b.getDouble())), DOUBLE, DOUBLE);
		addFunction("abs", d -> newValue(DOUBLE, Math.abs(d.getDouble())), DOUBLE);
		addFunction("hypot", (a, b) -> newValue(DOUBLE, Math.hypot(a.getDouble(), b.getDouble())), DOUBLE, DOUBLE);

		addFunction("clamp", (number, min, max) -> newValue(DOUBLE, Math.min(Math.max(number.getDouble(), min.getDouble()), max.getDouble())), DOUBLE, DOUBLE, DOUBLE);
		addFunction("clamp", (number, min, max) -> newValue(INT, Math.min(Math.max(number.getInt(), min.getInt()), max.getInt())), INT, INT, INT);

		addFunction("min", (a, b) -> newValue(INT, Math.min(a.getInt(), b.getInt())), INT, INT);
		addFunction("max", (a, b) -> newValue(INT, Math.max(a.getInt(), b.getInt())), INT, INT);

		addFunction("min", (a, b) -> newValue(DOUBLE, Math.min(a.getDouble(), b.getDouble())), DOUBLE, DOUBLE);
		addFunction("max", (a, b) -> newValue(DOUBLE, Math.max(a.getDouble(), b.getDouble())), DOUBLE, DOUBLE);

		addFunction("lerp", (start, end, value) -> newValue(DOUBLE, start.getDouble() + value.getDouble() * (end.getDouble() - start.getDouble())), DOUBLE, DOUBLE, DOUBLE);

		addFunction("isEven", number -> newValue(DOUBLE, number.getDouble() % 2.0 == 0 ? TRUE() : FALSE()), DOUBLE);
		addFunction("isEven", number -> newValue(INT, number.getInt() % 2 == 0 ? TRUE() : FALSE()), INT);

		addFunction("floor", number -> newValue(DOUBLE, Math.floor(number.getDouble())), DOUBLE);
		addFunction("floor", number -> newValue(INT, Math.floor(number.getInt())), INT);

		addFunction("ceil", number -> newValue(DOUBLE, Math.ceil(number.getDouble())), DOUBLE);
		addFunction("ceil", number -> newValue(INT, Math.ceil(number.getInt())), INT);

		addFunction("floorMod", (x, y) -> newValue(INT, Math.floorMod(x.getInt(), y.getInt())), INT, INT);
	}
}
