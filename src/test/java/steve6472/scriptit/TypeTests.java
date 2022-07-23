package steve6472.scriptit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import steve6472.scriptit.libraries.TestLibrary;

import java.io.File;

/**********************
 * Created by steve6472
 * On date: 2/2/2022
 * Project: ScriptIt
 *
 ***********************/
public class TypeTests
{
	private Script testScript(String name)
	{
		boolean debug = !Boolean.parseBoolean(System.getenv("disable_debug"));
		debug = false;

		ScriptItSettings.DELAY_DEBUG = debug;
		ScriptItSettings.PARSER_DEBUG = debug;
		Workspace workspace = new Workspace();
		workspace.addLibrary(new TestLibrary());
		Script script = Script.create(workspace, new File("!tests/" + name + ".txt"));
		Highlighter.basicHighlight();
		System.out.println(script.showCode());
		return script;
	}

	@Nested
	@DisplayName("string")
	class String_
	{
		@Test
		public void substring()
		{
			Script script = testScript("types/string/substring");
			Value value = script.runWithDelay();
			Assertions.assertEquals(value.getString(), "ello World");
		}
	}
}
