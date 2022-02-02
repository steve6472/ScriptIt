package steve6472.scriptit.libraries;

import steve6472.scriptit.Value;
import steve6472.scriptit.types.CustomTypes;
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

		addFunction("circleCircleIntersection", (x0, y0, r0, x1, y1, r1) ->
		{
			double a, dx, dy, d, h ,rx, ry;
			double x2, y2;

			dx = x1.getDouble() - x0.getDouble();
			dy = y1.getDouble() - y0.getDouble();

			d = Math.hypot(dx, dy);

			if (d > (r0.getDouble() + r1.getDouble()))
			{
				System.out.println("No Intersection");
				return CustomTypes.createVec4(0, 0, 0, 0);
			}

			if (d < Math.abs(r0.getDouble() - r1.getDouble()))
			{
				System.out.println("Inside");
				return CustomTypes.createVec4(0, 0, 0, 0);
			}

			a = ((r0.getDouble()*r0.getDouble())-(r1.getDouble()*r1.getDouble())+(d*d))/(2.0*d);
			x2 = x0.getDouble()+(dx*a/d);
			y2 = y0.getDouble()+(dy*a/d);
			h = Math.sqrt((r0.getDouble()*r0.getDouble())-(a*a));
			rx = -dy * (h/d);
			ry = dx*(h/d);

			return CustomTypes.createVec4(x2 + rx, y2 + ry, x2 - rx, y2 - ry);
		}, DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE);

/*
		addFunction("stuff", (canvas, x, y, text, w) ->
		{
			Graphics2D graphics2D = ((SwingTypes.Canvas) canvas.get("canvas")).graphics2D;
			graphics2D.setColor(new Color(40, 40, 40));
			graphics2D.fillRect((int) x.getDouble(), (int) y.getDouble(), w.getInt(), 15);
			graphics2D.setColor(Color.WHITE);
			graphics2D.drawString(text.getString(), (int) x.getDouble(), (int) y.getDouble() + 12);
			((SwingTypes.Canvas) canvas.get("canvas")).repaint();

			return Value.newValue(INT, 0);
		}, SwingTypes.CANVAS, DOUBLE, DOUBLE, STRING, INT);

		addFunction("drawGrid", (canvas, spacing, count) ->
		{
			Graphics2D graphics2D = ((SwingTypes.Canvas) canvas.get("canvas")).graphics2D;
			for (int i = 0; i < count.getInt(); i++)
			{
				for (int j = 0; j < count.getInt(); j++)
				{
					graphics2D.drawLine(i, j * spacing.getInt(), i * 2000, j * spacing.getInt());
					graphics2D.drawLine(i * spacing.getInt(), j, i * spacing.getInt(), j * 2000);
				}
			}
			((SwingTypes.Canvas) canvas.get("canvas")).repaint();

			return Value.newValue(INT, 0);
		}, SwingTypes.CANVAS, INT, INT);*/
	}
}
