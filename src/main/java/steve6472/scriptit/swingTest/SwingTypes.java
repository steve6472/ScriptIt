package steve6472.scriptit.swingTest;

import steve6472.scriptit.FunctionParameters;
import steve6472.scriptit.Type;
import steve6472.scriptit.types.CustomTypes;
import steve6472.scriptit.types.TypesInit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import static steve6472.scriptit.Value.newValue;
import static steve6472.scriptit.types.PrimitiveTypes.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/28/2021
 * Project: ScriptIt
 *
 ***********************/
public class SwingTypes extends TypesInit
{
	public static final Type WINDOW = new Type("Window");
	public static final Type CANVAS = new Type("Canvas");

	public static void init()
	{
		WINDOW.addConstructor(FunctionParameters.create(WINDOW, STRING, INT, INT), new Constructor(args ->
		{
			JFrame frame = new JFrame(args[0].getString());
			frame.setVisible(true);
			frame.setSize(args[1].getInt() + 16, args[2].getInt() + 39);
			frame.setResizable(false);
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


			BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
			Canvas canvas = new Canvas(image);
			frame.add(canvas);

			frame.repaint();
			FrameKeyListener keyListener = new FrameKeyListener();
			frame.addKeyListener(keyListener);

			return newValue(WINDOW).setValue("frame", frame).setValue("canvas", canvas).setValue("keyListener", keyListener);
		}));

		addProcedure(WINDOW, "close", (itself) -> {
			JFrame frame = (JFrame) itself.get("frame");
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		});

		addProcedure(WINDOW, "center", (itself) -> {
			JFrame frame = (JFrame) itself.get("frame");
			frame.setLocationRelativeTo(null);
		});

		addProcedure(WINDOW, "setAlwaysOnTop", (itself, top) -> {
			JFrame frame = (JFrame) itself.get("frame");
			frame.setAlwaysOnTop(top.getBoolean());
		}, BOOL);

		addProcedure(WINDOW, "setResizable", (itself, resizable) -> {
			JFrame frame = (JFrame) itself.get("frame");
			frame.setResizable(resizable.getBoolean());
		}, BOOL);

		addProcedure(WINDOW, "setLocation", (itself, x, y) -> {
			JFrame frame = (JFrame) itself.get("frame");
			frame.setLocation(x.getInt(), y.getInt());
		}, INT, INT);

		addFunction(WINDOW, "getCanvas", itself -> newValue(CANVAS).setValue("canvas", itself.get("canvas")));

		addFunction(WINDOW, "getMouseX", itself ->
		{
			Point point = ((Canvas) itself.get("canvas")).getMousePosition();
			return newValue(INT).setValue(point == null ? 0 : point.x);
		});
		addFunction(WINDOW, "getMouseY", itself ->
		{
			Point point = ((Canvas) itself.get("canvas")).getMousePosition();
			return newValue(INT).setValue(point == null ? 0 : point.y);
		});
		addFunction(WINDOW, "isMouseInWindow", itself ->
		{
			Point point = ((Canvas) itself.get("canvas")).getMousePosition();
			return point == null ? FALSE() : TRUE();
		});
		addFunction(WINDOW, "isKeyPressed", (itself, key) ->
		{
			FrameKeyListener keyListener = (FrameKeyListener) itself.get("keyListener");
			//			System.out.println(keyListener.pressed);
			return keyListener.pressed.contains(key.getChar()) ? TRUE() : FALSE();
		}, CHAR);
		addProcedure(WINDOW, "printPressedKeys", itself ->
		{
			FrameKeyListener keyListener = (FrameKeyListener) itself.get("keyListener");
			System.out.println("Pressed keys: " + keyListener.pressed);
		});

		addProcedure(CANVAS, "repaint", itself -> ((Canvas) itself.get("canvas")).repaint());

		addProcedure(CANVAS, "setColor", (itself, r, g, b) -> ((Canvas) itself.get("canvas")).graphics2D.setColor(new Color(r.getInt(), g.getInt(), b.getInt())), INT, INT, INT);
		addProcedure(CANVAS, "setColor", (itself, rgb) -> ((Canvas) itself.get("canvas")).graphics2D.setColor(new Color((rgb.getInt() >> 16) & 0xff, (rgb.getInt() >> 8) & 0xff, rgb.getInt() & 0xff)), INT);
		addProcedure(CANVAS, "setColor", (itself, color) -> ((Canvas) itself.get("canvas")).graphics2D.setColor(new Color(color.getIntValue("r"), color.getIntValue("g"), color.getIntValue("b"))), CustomTypes.COLOR);

		addProcedure(CANVAS, "drawString", (itself, text, x, y) -> ((Canvas) itself.get("canvas")).graphics2D.drawString(text.getString(), x.getInt(), y.getInt()), STRING, INT, INT);

		addProcedure(CANVAS, "fillRect", (itself, x, y, w, h) -> ((Canvas) itself.get("canvas")).graphics2D.fillRect(x.getInt(), y.getInt(), w.getInt(), h.getInt()), INT, INT, INT, INT);
		addProcedure(CANVAS, "drawRect", (itself, x, y, w, h) -> ((Canvas) itself.get("canvas")).graphics2D.drawRect(x.getInt(), y.getInt(), w.getInt(), h.getInt()), INT, INT, INT, INT);

		addProcedure(CANVAS, "drawLine", (itself, x1, y1, x2, y2) -> ((Canvas) itself.get("canvas")).graphics2D.drawLine(x1.getInt(), y1.getInt(), x2.getInt(), y2.getInt()), INT, INT, INT, INT);

		addProcedure(CANVAS, "drawPoint", (itself, x, y) -> ((Canvas) itself.get("canvas")).graphics2D.drawLine(x.getInt(), y.getInt(), x.getInt(), y.getInt()), INT, INT);
	}

	private static class Canvas extends JComponent
	{
		private final BufferedImage image;
		private final Graphics2D graphics2D;

		public Canvas(BufferedImage image)
		{
			this.image = image;
			this.graphics2D = image.createGraphics();
		}

		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			g.drawImage(image, 0, 0, null);
		}
	}

	private static class FrameKeyListener extends KeyAdapter
	{
		public final Set<Character> pressed = new HashSet<>();

		@Override
		public void keyPressed(KeyEvent e)
		{
			super.keyPressed(e);
			pressed.add(e.getKeyChar());
		}

		@Override
		public void keyReleased(KeyEvent e)
		{
			super.keyReleased(e);
			pressed.remove(e.getKeyChar());
		}
	}
}
