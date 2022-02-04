package steve6472.scriptit;

import org.junit.jupiter.api.*;
import steve6472.scriptit.exceptions.ValueNotFoundException;
import steve6472.scriptit.functions.DelayFunction;
import steve6472.scriptit.libraries.LogLibrary;
import steve6472.scriptit.libraries.TestLibrary;
import steve6472.scriptit.tokenizer.TokenParser;
import steve6472.scriptit.types.PrimitiveTypes;

import java.io.File;
import java.time.Duration;

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
		boolean debug = !Boolean.parseBoolean(System.getenv("disable_debug"));
		debug = false;

		DelayFunction.DEBUG = debug;
		TokenParser.DEBUG = debug;
		Workspace workspace = new Workspace();
		workspace.addLibrary(new TestLibrary());
		workspace.addLibrary(new LogLibrary());
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

	/*
	TODO: do this test but with Vec4 or array
	@Test
	public void functionIntersection()
	{
		Script script = testScript("function_intersection");
		Value value = script.runWithDelay();
	}*/

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

	/*@Test
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
	public void dots()
	{
		Script script = testScript("dots");
		Value value = script.runWithDelay();
		Assertions.assertEquals('W', value.getChar());
	}

	@Test
	public void dotBinary()
	{
		Script script = testScript("dot_bin");
		Value value = script.runWithDelay();
		Assertions.assertEquals(Math.PI * 2f, value.getDouble());
	}

	@Test
	public void delay()
	{
		Script script = testScript("delay");
		long start = System.currentTimeMillis();
		script.runWithDelay();
		Assertions.assertTrue(System.currentTimeMillis() - start >= 500);
	}

	@Test
	public void valueChanged()
	{
		Script script = testScript("value_changed");
		Value value = script.runWithDelay();
		Assertions.assertEquals(2, value.getInt());
	}

	@Nested
	@DisplayName("return")
	class Return_
	{
		@Test
		@DisplayName(value = "return if")
		public void returnIf()
		{
			Script script = testScript("return/return_if");
			Value value = script.runWithDelay();
			Assertions.assertTrue(value.getBoolean());
		}

		@Test
		@DisplayName(value = "returnif")
		public void returnif()
		{
			Script script = testScript("return/returnif_true");
			Value value = script.runWithDelay();
			Assertions.assertEquals(value, Value.NULL);

			script = testScript("return/returnif_false");
			value = script.runWithDelay();
			Assertions.assertFalse(value.getBoolean());
		}

		@Test
		public void returnOne()
		{
			Script script = testScript("return/return");
			Value value = script.runWithDelay();
			Assertions.assertEquals(1, value.getInt());
		}
	}

	@Nested
	@DisplayName("flow")
	class Flow_
	{
		@Test
		public void ternary()
		{
			Script script = testScript("flow/ternary");
			Value value = script.runWithDelay();
			Assertions.assertTrue(value.getBoolean());
		}

		@Nested
		@DisplayName("if")
		class If_
		{
			@Test
			public void ifNoBody()
			{
				Script script = testScript("flow/if/if_no_body");
				Value value = script.runWithDelay();
				Assertions.assertTrue(value.getBoolean());
			}

			@Test
			public void ifElseNoBody()
			{
				Script script = testScript("flow/if/if_else_no_body");
				Value value = script.runWithDelay();
				Assertions.assertFalse(value.getBoolean());
			}

			@Test
			public void ifTrueReturnOne()
			{
				Script script = testScript("flow/if/if_true_return_one");
				Value value = script.runWithDelay();
				Assertions.assertEquals(1, value.getInt());
			}

			@Test
			@DisplayName(value = "Decision (if, else)")
			public void decision()
			{
				Script script = testScript("flow/if/decision");
				script.getMemory().addVariable("num", new Value(true, PrimitiveTypes.INT, 0));
				Value value = script.runWithDelay();
				Assertions.assertEquals("ZERO", value.getString());

				script.getMainExecutor().reset();
				script.getMemory().addVariable("num", new Value(true, PrimitiveTypes.INT, 1));
				value = script.runWithDelay();
				Assertions.assertEquals("ONE", value.getString());
			}
		}

		@Nested
		@DisplayName("while")
		class While
		{
			@Test
			@DisplayName(value = "normal")
			public void normalWhile()
			{
				Script script = testScript("flow/loop/while/while");
				script.getMemory().addVariable("input", new Value(true, PrimitiveTypes.INT, 5));

				final Value[] value = new Value[1];
				Assertions.assertTimeoutPreemptively(Duration.ofMillis(200), () -> value[0] = script.runWithDelay());
				Assertions.assertEquals(120, value[0].getInt());
			}

			@Test
			@DisplayName(value = "break")
			public void whileBreak()
			{
				Script script = testScript("flow/loop/while/while_break");

				final Value[] value = new Value[1];
				Assertions.assertTimeoutPreemptively(Duration.ofMillis(200), () -> value[0] = script.runWithDelay());
				Assertions.assertEquals(4, value[0].getInt());
			}

			@Test
			@DisplayName(value = "continue")
			public void whileContinue()
			{
				Script script = testScript("flow/loop/while/while_continue");

				final Value[] value = new Value[1];
				Assertions.assertTimeoutPreemptively(Duration.ofMillis(200), () -> value[0] = script.runWithDelay());
				Assertions.assertEquals(5, value[0].getInt());
			}
		}

		@Nested
		@DisplayName("for")
		class For_
		{
			@Test
			@DisplayName(value = "normal")
			public void normalFor()
			{
				Script script = testScript("flow/loop/for/for");

				final Value[] value = new Value[1];
				Assertions.assertTimeoutPreemptively(Duration.ofMillis(200), () -> value[0] = script.runWithDelay());
				Assertions.assertEquals(24, value[0].getInt());
			}

			@Test
			@DisplayName(value = "continue")
			public void forContinue()
			{
				Script script = testScript("flow/loop/for/for_continue");

				final Value[] value = new Value[1];
				Assertions.assertTimeoutPreemptively(Duration.ofMillis(200), () -> value[0] = script.runWithDelay());
				Assertions.assertEquals(945, value[0].getInt());
			}

			@Test
			@DisplayName(value = "break")
			public void forBreak()
			{
				Script script = testScript("flow/loop/for/for_break");

				final Value[] value = new Value[1];
				Assertions.assertTimeoutPreemptively(Duration.ofMillis(200), () -> value[0] = script.runWithDelay());
				Assertions.assertEquals(3, value[0].getInt());
			}

			@Test
			@DisplayName(value = "var not found")
			public void varNotFound()
			{
				Script script = testScript("flow/loop/for/for_var_not_found");

				Assertions.assertTimeoutPreemptively(Duration.ofMillis(200), () -> Assertions.assertThrows(ValueNotFoundException.class, script::runWithDelay));
			}
		}
	}
}
