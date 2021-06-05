package steve6472.scriptit.test;

import steve6472.scriptit.*;
import steve6472.scriptit.libraries.GeometryLibrary;
import steve6472.scriptit.swingTest.SwingTypes;
import steve6472.scriptit.types.CustomTypes;
import steve6472.scriptit.types.PrimitiveTypes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import static steve6472.scriptit.Value.newValue;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/28/2021
 * Project: ScriptIt
 *
 ***********************/
public class EventTest
{
	private static Script script;

	public static void main(String[] args)
	{
		SwingTypes.init();

		Workspace workspace = new Workspace();
		workspace.addType(SwingTypes.WINDOW);
		workspace.addType(CustomTypes.COLOR);
		workspace.addType(CustomTypes.LIST);

		workspace.addLibrary(new GeometryLibrary());

		script = ScriptReader.readScript(new File("scripts/event_test_2.txt"), workspace);
		CustomTypes.init(script);
		createWindow("TicTacToe", 64 * 3 + 8, 64 * 3 + 8); // 3 times 64 pixel cell, 4 pixels border

		script.runWithDelay();
	}

	private static void createWindow(String name, int width, int height)
	{
		JFrame frame = new JFrame(name);
		frame.setVisible(true);
		frame.setSize(width + 16, height + 39);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
		SwingTypes.Canvas canvas = new SwingTypes.Canvas(image);
		frame.add(canvas);

		frame.repaint();
		SwingTypes.FrameKeyListener keyListener = new SwingTypes.FrameKeyListener();
		frame.addKeyListener(keyListener);

		frame.addMouseListener(new FrameMouseListener());

		script.getMemory().addVariable("window", newValue(SwingTypes.WINDOW).setValue("frame", frame).setValue("canvas", canvas).setValue("keyListener", keyListener));
	}

	public static class FrameMouseListener extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			Point point = e.getPoint();
			script.queueFunctionCall(true, "mouseClicked",
				Value.newValue(PrimitiveTypes.INT, e.getButton()),
				Value.newValue(PrimitiveTypes.INT, point.x - 8),
				Value.newValue(PrimitiveTypes.INT, point.y - 31));
		}
	}
}
