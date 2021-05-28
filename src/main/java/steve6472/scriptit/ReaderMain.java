package steve6472.scriptit;

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

		Script script = ScriptReader.readScript(new File("scripts/jan_ken_pon.txt"), workspace);
		script.runWithDelay();
	}
}
