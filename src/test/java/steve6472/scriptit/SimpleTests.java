package steve6472.scriptit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steve6472.scriptit.exceptions.ValueNotFoundException;
import steve6472.scriptit.libraries.TestLibrary;
import steve6472.scriptit.types.PrimitiveTypes;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 12/12/2021
 * Project: ScriptIt
 *
 ***********************/
public class SimpleTests
{
	private Script testScript(String name)
	{
		TokenParser.DEBUG = !Boolean.parseBoolean(System.getenv("disable_debug"));
		Workspace workspace = new Workspace();
		workspace.addLibrary(new TestLibrary());
		Script script = Script.create(workspace, new File("!tests/" + name + ".txt"));
		Highlighter.basicHighlight();
		System.out.println(script.showCode());
		return script;
	}

	@Test
	public void helloWorld()
	{
		Script script = testScript("hello_world");
		script.runWithDelay();
	}

	@Test
	public void math()
	{
		Script script = testScript("math");
		Value value = script.runWithDelay();
		Assertions.assertEquals(-24, value.getInt());
	}

	@Test
	@DisplayName(value = "Factorial (while)")
	public void factorial()
	{
		Script script = testScript("factorial");
		script.getMemory().addVariable("input", new Value(true, PrimitiveTypes.INT, 5));
		Value value = script.runWithDelay();

		Assertions.assertEquals(120, value.getInt());
	}

	@Test
	@DisplayName(value = "Decision (if, else)")
	public void decision()
	{
		Script script = testScript("decision");
		script.getMemory().addVariable("num", new Value(true, PrimitiveTypes.INT, 0));
		Value value = script.runWithDelay();
		Assertions.assertEquals("ZERO", value.getString());

		script.getMemory().addVariable("num", new Value(true, PrimitiveTypes.INT, 1));
		value = script.runWithDelay();
		Assertions.assertEquals("ONE", value.getString());
	}

	@Test
	@DisplayName(value = "Function (function)")
	public void function()
	{
		Script script = testScript("function");
		Value value = script.runWithDelay();
		Assertions.assertEquals(1, value.getInt());
	}

	@Test
	@DisplayName(value = "Function Params (function parameters)")
	public void functionParameters()
	{
		Script script = testScript("function_parameters");
		script.getMemory().addVariable("l", new Value(true, PrimitiveTypes.INT, 1));
		script.getMemory().addVariable("r", new Value(true, PrimitiveTypes.INT, 5));
		Value value = script.runWithDelay();
		Assertions.assertEquals(6, value.getInt());
	}

	@Test
	@DisplayName(value = "Value not found (val declared in function, can not be found outside function)")
	public void function_()
	{
		Script script = testScript("val_not_found");
		Assertions.assertThrows(ValueNotFoundException.class, script::runWithDelay);
	}
/*
	@Test
	@DisplayName(value = "Recursion (recursion)")
	public void recursion()
	{
		Script script = testScript("recursion");
		Assertions.assertTimeoutPreemptively(Duration.ofMillis(200), script::runWithDelay);
	}*/

	@Test
	@DisplayName(value = "Nested function (nested function)")
	public void nestedFunction()
	{
		Script script = testScript("nested_functions");
		Value value = script.runWithDelay();
		Assertions.assertEquals(15, value.getInt());
	}

	@Test
	@DisplayName(value = "Assign-add")
	public void assignAdd()
	{
		Script script = testScript("assign_plus");
		Value value = script.runWithDelay();
		Assertions.assertEquals(3, value.getInt());
	}

	@Test
	public void ifNoBody()
	{
		Script script = testScript("if_no_body");
		Value value = script.runWithDelay();
		Assertions.assertTrue(value.getBoolean());
	}

	@Test
	public void ifElseNoBody()
	{
		Script script = testScript("if_else_no_body");
		Value value = script.runWithDelay();
		Assertions.assertFalse(value.getBoolean());
	}

	@Test
	@DisplayName(value = "return if")
	public void returnIf()
	{
		Script script = testScript("return_if");
		Value value = script.runWithDelay();
		Assertions.assertTrue(value.getBoolean());
	}

	@Test
	@DisplayName(value = "returnif")
	public void returnif()
	{
		Script script = testScript("returnif_true");
		Value value = script.runWithDelay();
		Assertions.assertEquals(value, Value.NULL);

		script = testScript("returnif_false");
		value = script.runWithDelay();
		Assertions.assertFalse(value.getBoolean());
	}

	@Test
	public void dots()
	{
		Script script = testScript("dots");
		Value value = script.runWithDelay();
		Assertions.assertEquals('W', value.getChar());
	}
}
