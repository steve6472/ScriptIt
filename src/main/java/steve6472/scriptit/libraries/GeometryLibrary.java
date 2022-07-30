package steve6472.scriptit.libraries;

import steve6472.scriptit.type.PrimitiveTypes;

import static steve6472.scriptit.type.PrimitiveTypes.DOUBLE;
import static steve6472.scriptit.type.PrimitiveTypes.INT;
import static steve6472.scriptit.type.TypesInit.newPrimitive;

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

		addFunction("isPointInsideRectangle", (x, y, w, h, px, py) -> newPrimitive(PrimitiveTypes.BOOL, (px.asPrimitive().getDouble() >= x.asPrimitive().getDouble() && px.asPrimitive().getDouble() <= w.asPrimitive().getDouble() + x.asPrimitive().getDouble()) && (py.asPrimitive().getDouble() >= y.asPrimitive().getDouble() && py.asPrimitive().getDouble() <= h.asPrimitive().getDouble() + y.asPrimitive().getDouble())), DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE);
		addFunction("isPointInsideRectangle", (x, y, w, h, px, py) -> newPrimitive(PrimitiveTypes.BOOL, (px.asPrimitive().getInt() >= x.asPrimitive().getInt() && px.asPrimitive().getInt() <= w.asPrimitive().getInt() + x.asPrimitive().getInt()) && (py.asPrimitive().getInt() >= y.asPrimitive().getInt() && py.asPrimitive().getInt() <= h.asPrimitive().getInt() + y.asPrimitive().getInt())), INT, INT, INT, INT, INT, INT);

		/*
		 * Removed 'cause Vec4 does not really exist
		addFunction("circleCircleIntersection", (x0, y0, r0, x1, y1, r1) ->
		{
			double a, dx, dy, d, h ,rx, ry;
			double x2, y2;

			dx = x1.asPrimitive().getDouble() - x0.asPrimitive().getDouble();
			dy = y1.asPrimitive().getDouble() - y0.asPrimitive().getDouble();

			d = Math.hypot(dx, dy);

			if (d > (r0.asPrimitive().getDouble() + r1.asPrimitive().getDouble()))
			{
//				System.out.println("No Intersection");
				return CustomTypes.createVec4(0, 0, 0, 0);
			}

			if (d < Math.abs(r0.asPrimitive().getDouble() - r1.asPrimitive().getDouble()))
			{
//				System.out.println("Inside");
				return CustomTypes.createVec4(0, 0, 0, 0);
			}

			a = ((r0.asPrimitive().getDouble()*r0.asPrimitive().getDouble())-(r1.asPrimitive().getDouble()*r1.asPrimitive().getDouble())+(d*d))/(2.0*d);
			x2 = x0.asPrimitive().getDouble()+(dx*a/d);
			y2 = y0.asPrimitive().getDouble()+(dy*a/d);
			h = Math.sqrt((r0.asPrimitive().getDouble()*r0.asPrimitive().getDouble())-(a*a));
			rx = -dy * (h/d);
			ry = dx*(h/d);

			return CustomTypes.createVec4(x2 + rx, y2 + ry, x2 - rx, y2 - ry);
		}, DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE);*/
	}
}
