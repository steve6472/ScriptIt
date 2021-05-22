package steve6472.scriptit.exp;

import steve6472.scriptit.exp.functions.NewVec2;
import steve6472.scriptit.exp.libraries.Library;
import steve6472.scriptit.exp.libraries.MathLibrary;
import steve6472.scriptit.exp.libraries.SystemLibrary;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/21/2021
 * Project: ScriptIt
 *
 ***********************/
public class Main
{
	public static class Script
	{
		private final MyParser parser;
		MemoryStack memory;
		ExpressionExecutor[] lines;
		private int expressionIndex = 0;
		private int currentIndex = 0;

		public Script()
		{
			this.parser = new MyParser();
			this.memory = new MemoryStack(64);
		}

		public void addLibrary(Library library)
		{
			memory.addLibrary(library);
		}

		public void setExpressions(String... expressions)
		{
			lines = new ExpressionExecutor[expressions.length];
			for (int i = 0; i < expressions.length; i++)
			{
				lines[i] = new ExpressionExecutor(memory);
				lines[i].setExpression(parser.setExpression(expressions[i]).parse(memory));
			}
		}

		public Result execute()
		{
			for (currentIndex = expressionIndex; currentIndex < lines.length; currentIndex++)
			{
				expressionIndex = currentIndex;

				Result result = lines[currentIndex].execute(this);
				if (result.isDelay() || result.isReturnValue() || result.isReturn())
					return result;
			}

			return Result.return_();
		}

		public ExpressionExecutor currentExecutor()
		{
			return lines[currentIndex];
		}

		public MemoryStack getMemory()
		{
			return memory;
		}

		public MyParser getParser()
		{
			return parser;
		}
	}

	public static void main(String[] args)
	{
		// start, stop, t
		String[] lerp =
			{
				"a = 1.0 - t",
				"System.print(a)",
				"m = a * start",
				"System.print(m)",
				"delay(500)",
				"m_ = t * stop",
				"System.print(m_)",
				"return m + m_",
				"System.print(69)"
			};

		String[] expressions =
			{
				"x = 5.0",
				"delay(500)",
				"x = x * 10.0",
				"return x"
			};

		String[] delayTest =
			{
				"delay(500)"
			};

		String[] lerpTest =
			{
				"l = lerp(3.0, 5.0, 0.5) * 2.0",
				"System.print(-1)",
				"System.print(-l)",
				"delay(500)",
				"return l"
			};

/*
		String[] lerp =
			{
				"return (1 - t) * start + t * stop"
			};*/

		Script script = new Script();
		PrimitiveTypes.init(script);
		script.addLibrary(new MathLibrary());
		script.addLibrary(new SystemLibrary());

//		Function doubleMul = new Function("left", "right");
////		doubleMul.setExpressions(script, "temp = left + right", "temp = temp * right", "return temp - left");
//		doubleMul.setExpressions(script, "return left * right");
//		PrimitiveTypes.DOUBLE.addBinaryOperator(PrimitiveTypes.DOUBLE, Operator.MUL, doubleMul);

		// Constractor, TODO: move to PrimitiveTypes
		Function newVec2 = new NewVec2();
		script.memory.addFunction(FunctionParameters.constructor(PrimitiveTypes.VEC2).addType(PrimitiveTypes.DOUBLE).addType(PrimitiveTypes.DOUBLE).build(), newVec2);

		Function lerpFunction = new Function("start", "stop", "t");
		lerpFunction.setExpressions(script, lerp);
		script.memory.addFunction(FunctionParameters.function("lerp").addType(PrimitiveTypes.DOUBLE).addType(PrimitiveTypes.DOUBLE).addType(PrimitiveTypes.DOUBLE).build(), lerpFunction);

		Function del = new Function();
		del.setExpressions(script, "delay(1000)", "System.print(8)", "return 3.0");
		script.memory.addFunction(FunctionParameters.function("del").build(), del);

		script.setExpressions("return lerp(3.0, 5.0, 0.5) * lerp(0.0, 1.0, 0.5)");
//		script.setExpressions("v = vec2(3.02, 6.7)", "return v.normalize()");
//		script.setExpressions("return Math.sqrt(2.0)");

		runWithDelay(script);
	}

	private static void runWithDelay(Script script)
	{
		Result ret = Result.delay();

		while (ret.isDelay() && !ret.isReturnValue() && ! ret.isReturn())
		{
			ret = script.execute();
		}
		if (ret.isReturnValue())
			System.out.println("Returned: " + ret.getValue());
		else
			System.out.println("Last value: " + ret.getValue());
	}
}
