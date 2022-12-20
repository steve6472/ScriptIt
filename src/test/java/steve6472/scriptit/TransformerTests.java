package steve6472.scriptit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import steve6472.scriptit.Highlighter;
import steve6472.scriptit.Script;
import steve6472.scriptit.ScriptItSettings;
import steve6472.scriptit.Workspace;
import steve6472.scriptit.expressions.Function;
import steve6472.scriptit.expressions.FunctionParameters;
import steve6472.scriptit.libraries.Library;
import steve6472.scriptit.libraries.LogLibrary;
import steve6472.scriptit.libraries.TestLibrary;
import steve6472.scriptit.transformer.Cat;
import steve6472.scriptit.transformer.JavaTransformer;
import steve6472.scriptit.transformer.SchemeParser;
import steve6472.scriptit.type.PrimitiveTypes;

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
		ScriptItSettings.CLASS_TRANSFORMER_DEBUG = true;
		ScriptItSettings.CLASS_TRANSFORMER_IGNORED_DEBUG = true;

		ScriptItSettings.STACK_TRACE = true;
	}

	private Script testScript(String name)
	{
		setDebugs();
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
		JavaTransformer.FUNCTION_GENERATED.clear();
		JavaTransformer.CACHE.clear();
		JavaTransformer.initCache();

		ScriptItSettings.ALLOW_UNSAFE_CUSTOM_TRANSFORMER = true;
		Script script = testScript("transformer/animals_unsafe");
		script.runWithDelay();
		ScriptItSettings.ALLOW_UNSAFE_CUSTOM_TRANSFORMER = false;
	}

	@Test
	public void animalSafe()
	{
		JavaTransformer.FUNCTION_GENERATED.clear();
		JavaTransformer.CACHE.clear();
		JavaTransformer.initCache();

		ScriptItSettings.ALLOW_UNSAFE_CUSTOM_TRANSFORMER = false;
		Workspace workspace = new Workspace();
		workspace.addTransformer("farm", new SchemeParser().createConfigs(new File("!tests/transformer/scheme.txt")));

		Script script = testScript("transformer/animals_safe", workspace);
		script.runWithDelay();
	}

	@Test
	public void meowInterface()
	{
		JavaTransformer.FUNCTION_GENERATED.clear();
		JavaTransformer.CACHE.clear();
		JavaTransformer.initCache();

		ScriptItSettings.ALLOW_UNSAFE_CUSTOM_TRANSFORMER = false;
		Workspace workspace = new Workspace();
		workspace.addTransformer("meow", new SchemeParser().createConfigs(new File("!tests/transformer/scheme_interface.txt")));
		workspace.addLibrary(new Library("Cats")
		{{
			addFunction(FunctionParameters.function("createCat").build(), new Function()
			{
				@Override
				public Result apply(Script script)
				{
					try
					{
						return Result.value(JavaTransformer.transformObject(new Cat("Import the Cat"), script));
					} catch (IllegalAccessException e)
					{
						throw new RuntimeException(e);
					}
				}
			});
		}});

		Script script = testScript("transformer/meow_interface", workspace);
		script.runWithDelay();
	}

	@Test
	public void meowBool()
	{
		JavaTransformer.FUNCTION_GENERATED.clear();
		JavaTransformer.CACHE.clear();
		JavaTransformer.initCache();

		ScriptItSettings.ALLOW_UNSAFE_CUSTOM_TRANSFORMER = false;
		Workspace workspace = new Workspace();
		workspace.addTransformer("meow", new SchemeParser().createConfigs(new File("!tests/transformer/scheme_interface.txt")));
		workspace.addLibrary(new Library("Cats")
		{{
			addFunction(FunctionParameters.function("createCat").build(), new Function()
			{
				@Override
				public Result apply(Script script)
				{
					try
					{
						return Result.value(JavaTransformer.transformObject(new Cat("Import the Cat"), script));
					} catch (IllegalAccessException e)
					{
						throw new RuntimeException(e);
					}
				}
			});
		}});

		Script script = testScript("transformer/meow_bool", workspace);
		Assertions.assertThrows(RuntimeException.class, script::runWithDelay);
	}

	@Test
	public void interfaceHell()
	{
		JavaTransformer.FUNCTION_GENERATED.clear();
		JavaTransformer.CACHE.clear();
		JavaTransformer.initCache();

		ScriptItSettings.ALLOW_UNSAFE_CUSTOM_TRANSFORMER = false;
		Workspace workspace = new Workspace();
		workspace.addTransformer("hell", new SchemeParser().createConfigs(new File("!tests/transformer/interface_hell.txt")));

		Script script = testScript("transformer/interface_hell", workspace);
		script.runWithDelay();
	}
}
