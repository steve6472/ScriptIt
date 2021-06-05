package steve6472.scriptit.libraries;

import steve6472.scriptit.Value;
import steve6472.scriptit.types.PrimitiveTypes;

import static steve6472.scriptit.types.PrimitiveTypes.DOUBLE;
import static steve6472.scriptit.types.PrimitiveTypes.INT;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/29/2021
 * Project: ScriptIt
 *
 ***********************/
public class GeometryLibrary extends Library
{
	public GeometryLibrary()
	{
		super("Geometry");

		addFunction("isPointInsideRectangle", (x, y, w, h, px, py) -> Value.newValue(PrimitiveTypes.BOOL, (px.getDouble() >= x.getDouble() && px.getDouble() <= w.getDouble() + x.getDouble()) && (py.getDouble() >= y.getDouble() && py.getDouble() <= h.getDouble() + y.getDouble())), DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE);
		addFunction("isPointInsideRectangle", (x, y, w, h, px, py) -> Value.newValue(PrimitiveTypes.BOOL, (px.getInt() >= x.getInt() && px.getInt() <= w.getInt() + x.getInt()) && (py.getInt() >= y.getInt() && py.getInt() <= h.getInt() + y.getInt())), INT, INT, INT, INT, INT, INT);
	}
}
