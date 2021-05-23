package steve6472.scriptit.exp;

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
		Workspace workspace = new Workspace();

		Script script = ScriptReader.readScript(new File("scripts/while.txt"), workspace);
		script.runWithDelay();
	}
}
