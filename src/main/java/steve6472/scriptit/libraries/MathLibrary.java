package steve6472.scriptit.libraries;

import static steve6472.scriptit.type.PrimitiveTypes.*;

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

		addFunction("PI", () -> newPrimitive(DOUBLE, Math.PI));

		addFunction("sqrt", d -> newPrimitive(DOUBLE, Math.sqrt(d.asPrimitive().getDouble())), DOUBLE);
		addFunction("sin", d -> newPrimitive(DOUBLE, Math.sin(d.asPrimitive().getDouble())), DOUBLE);
		addFunction("cos", d -> newPrimitive(DOUBLE, Math.cos(d.asPrimitive().getDouble())), DOUBLE);
		addFunction("asin", d -> newPrimitive(DOUBLE, Math.asin(d.asPrimitive().getDouble())), DOUBLE);
		addFunction("acos", d -> newPrimitive(DOUBLE, Math.acos(d.asPrimitive().getDouble())), DOUBLE);
		addFunction("toRad", d -> newPrimitive(DOUBLE, Math.toRadians(d.asPrimitive().getDouble())), DOUBLE);
		addFunction("toDeg", d -> newPrimitive(DOUBLE, Math.toDegrees(d.asPrimitive().getDouble())), DOUBLE);
		addFunction("atan_", (a, b) -> newPrimitive(DOUBLE, Math.atan2(a.asPrimitive().getDouble(), b.asPrimitive().getDouble())), DOUBLE, DOUBLE);
		addFunction("abs", d -> newPrimitive(DOUBLE, Math.abs(d.asPrimitive().getDouble())), DOUBLE);
		addFunction("hypot", (a, b) -> newPrimitive(DOUBLE, Math.hypot(a.asPrimitive().getDouble(), b.asPrimitive().getDouble())), DOUBLE, DOUBLE);

		addFunction("clamp", (number, min, max) -> newPrimitive(DOUBLE, Math.min(Math.max(number.asPrimitive().getDouble(), min.asPrimitive().getDouble()), max.asPrimitive().getDouble())), DOUBLE, DOUBLE, DOUBLE);
		addFunction("clamp", (number, min, max) -> newPrimitive(INT, Math.min(Math.max(number.asPrimitive().getInt(), min.asPrimitive().getInt()), max.asPrimitive().getInt())), INT, INT, INT);

		addFunction("min", (a, b) -> newPrimitive(INT, Math.min(a.asPrimitive().getInt(), b.asPrimitive().getInt())), INT, INT);
		addFunction("max", (a, b) -> newPrimitive(INT, Math.max(a.asPrimitive().getInt(), b.asPrimitive().getInt())), INT, INT);

		addFunction("min", (a, b) -> newPrimitive(DOUBLE, Math.min(a.asPrimitive().getDouble(), b.asPrimitive().getDouble())), DOUBLE, DOUBLE);
		addFunction("max", (a, b) -> newPrimitive(DOUBLE, Math.max(a.asPrimitive().getDouble(), b.asPrimitive().getDouble())), DOUBLE, DOUBLE);

		addFunction("lerp", (start, end, value) -> newPrimitive(DOUBLE, start.asPrimitive().getDouble() + value.asPrimitive().getDouble() * (end.asPrimitive().getDouble() - start.asPrimitive().getDouble())), DOUBLE, DOUBLE, DOUBLE);

		addFunction("isEven", number -> newPrimitive(DOUBLE, number.asPrimitive().getDouble() % 2.0 == 0 ? TRUE() : FALSE()), DOUBLE);
		addFunction("isEven", number -> newPrimitive(INT, number.asPrimitive().getInt() % 2 == 0 ? TRUE() : FALSE()), INT);

		addFunction("floor", number -> newPrimitive(DOUBLE, Math.floor(number.asPrimitive().getDouble())), DOUBLE);
		addFunction("floor", number -> newPrimitive(INT, Math.floor(number.asPrimitive().getInt())), INT);

		addFunction("ceil", number -> newPrimitive(DOUBLE, Math.ceil(number.asPrimitive().getDouble())), DOUBLE);
		addFunction("ceil", number -> newPrimitive(INT, Math.ceil(number.asPrimitive().getInt())), INT);

		addFunction("floorMod", (x, y) -> newPrimitive(INT, Math.floorMod(x.asPrimitive().getInt(), y.asPrimitive().getInt())), INT, INT);
	}
}
