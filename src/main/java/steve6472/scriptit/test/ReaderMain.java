package steve6472.scriptit.test;

import steve6472.scriptit.Script;
import steve6472.scriptit.ScriptReader;
import steve6472.scriptit.Workspace;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/23/2021
 * Project: ScriptIt
 *
 ***********************/
public class ReaderMain
{
	public static void main(String[] args)
	{
//		MyParser.DEBUG = true;
		ScriptReader.DEBUG = true;
		Workspace workspace = new Workspace();

		Script script = ScriptReader.readScript(new File("scripts/random.txt"), workspace);
		script.runWithDelay();
	}
}
