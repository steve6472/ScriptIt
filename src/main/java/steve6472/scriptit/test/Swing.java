package steve6472.scriptit.test;

import steve6472.scriptit.Script;
import steve6472.scriptit.Workspace;
import steve6472.scriptit.libraries.GeometryLibrary;
import steve6472.scriptit.libraries.LogLibrary;
import steve6472.scriptit.swingTest.SwingTypes;
import steve6472.scriptit.types.CustomTypes;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/28/2021
 * Project: ScriptIt
 *
 ***********************/
public class Swing
{
	public static void main(String[] args)
	{
//		TokenParser.DEBUG = true;

		SwingTypes.init();
		CustomTypes.init();

		Workspace workspace = new Workspace();
		workspace.addType(SwingTypes.WINDOW);
		workspace.addType(SwingTypes.CANVAS);
		workspace.addType(CustomTypes.COLOR);
		workspace.addType(CustomTypes.VEC4);

		workspace.addLibrary(new GeometryLibrary());
		workspace.addLibrary(new LogLibrary());


		Script script = Script.create(workspace, new File("scripts/rlc.txt"));
		script.runWithDelay();

//		Script script = ScriptReader.readScript(new File("scripts/pong.txt"), workspace);
//		CustomTypes.init(script);
//
//		script.runWithDelay();
	}
}
