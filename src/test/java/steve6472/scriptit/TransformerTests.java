package steve6472.scriptit;

import steve6472.scriptit.Highlighter;
import steve6472.scriptit.Script;
import steve6472.scriptit.ScriptItSettings;
import steve6472.scriptit.Workspace;
import steve6472.scriptit.libraries.LogLibrary;
import steve6472.scriptit.libraries.TestLibrary;

import java.io.File;

/**
 * Created by steve6472
 * Date: 9/2/2022
 * Project: ScriptIt
 */
public class TransformerTests
{
	private void setDebugs()
	{
		boolean debug = !Boolean.parseBoolean(System.getenv("disable_debug"));
		debug = false;

		ScriptItSettings.DELAY_DEBUG = debug;
		ScriptItSettings.PARSER_DEBUG = debug;

		ScriptItSettings.STACK_TRACE = true;
	}

	private Script testScript(String name)
	{
		return testScript(name, new Workspace());
	}

	private Script testScript(String name, Workspace workspace)
	{
		setDebugs();

		workspace.addLibrary(new TestLibrary());
		workspace.addLibrary(new LogLibrary());
		Script script = Script.create(workspace, new File("!tests/" + name + ".scriptit"));
		Highlighter.basicHighlight();
		System.out.println(script.showCode() + "\n");
		return script;
	}
}
