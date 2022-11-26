package steve6472.scriptit;

import org.junit.jupiter.api.Test;
import steve6472.scriptit.Highlighter;
import steve6472.scriptit.Script;
import steve6472.scriptit.ScriptItSettings;
import steve6472.scriptit.Workspace;
import steve6472.scriptit.libraries.LogLibrary;
import steve6472.scriptit.libraries.TestLibrary;
import steve6472.scriptit.transformer.SchemeParser;

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
		ScriptItSettings.CLASS_TRANSFORMER_DEBUG = false;
		ScriptItSettings.CLASS_TRANSFORMER_IGNORED_DEBUG = false;

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

	@Test
	public void animal()
	{
		ScriptItSettings.ALLOW_UNSAFE_CUSTOM_TRANSFORMER = true;
		Script script = testScript("transformer/animals_unsafe");
		script.runWithDelay();
		ScriptItSettings.ALLOW_UNSAFE_CUSTOM_TRANSFORMER = false;
	}

	@Test
	public void animalSafe()
	{
		ScriptItSettings.ALLOW_UNSAFE_CUSTOM_TRANSFORMER = false;
		Workspace workspace = new Workspace();
		workspace.addTransformer("farm", new SchemeParser().createConfigs(new File("!tests/transformer/scheme.txt")));

		Script script = testScript("transformer/animals_safe", workspace);
		script.runWithDelay();
	}
}
