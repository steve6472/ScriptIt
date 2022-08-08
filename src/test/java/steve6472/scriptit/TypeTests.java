package steve6472.scriptit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import steve6472.scriptit.libraries.LogLibrary;
import steve6472.scriptit.libraries.TestLibrary;
import steve6472.scriptit.type.complex.HashMapType;
import steve6472.scriptit.value.Value;

import java.io.File;

/**********************
 * Created by steve6472
 * On date: 2/2/2022
 * Project: ScriptIt
 *
 ***********************/
public class TypeTests
{
	private void setDebugs()
	{
		boolean debug = !Boolean.parseBoolean(System.getenv("disable_debug"));
		debug = false;

		ScriptItSettings.DELAY_DEBUG = debug;
		ScriptItSettings.PARSER_DEBUG = debug;
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

	@Nested
	@DisplayName("string")
	class String_
	{
		@Test
		public void substring()
		{
			Script script = testScript("types/string/substring");
			Value value = script.runWithDelay();
			Assertions.assertEquals(value.asPrimitive().getString(), "ello World");
		}
	}

	@Nested
	@DisplayName("HashMap")
	class HashMap_
	{
		@Test
		public void generic()
		{
			Workspace workspace = new Workspace();
			HashMapType.init();
			workspace.addType(HashMapType.HASH_MAP);

			Script script = testScript("types/hashmap/generic", workspace);
			Value value = script.runWithDelay();
			Assertions.assertEquals(value.asPrimitive().getInt(), 41);
		}
	}
}
