package steve6472.scriptit.exp;

import steve6472.scriptit.exp.types.CustomTypes;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/21/2021
 * Project: ScriptIt
 *
 ***********************/
public class Main
{

	public static void main(String[] args) throws InterruptedException
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
				"System.print(69.0)"
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

		Workspace workspace = new Workspace();
		workspace.addType(CustomTypes.VEC2);

		Script script = new Script(workspace);
		CustomTypes.init(script);

//		Function doubleMul = new Function("left", "right");
////		doubleMul.setExpressions(script, "temp = left + right", "temp = temp * right", "return temp - left");
//		doubleMul.setExpressions(script, "return left * right");
//		PrimitiveTypes.DOUBLE.addBinaryOperator(PrimitiveTypes.DOUBLE, Operator.MUL, doubleMul);
/*
		Function lerpFunction = new Function("start", "stop", "t");
		lerpFunction.setExpressions(script, lerp);
		script.memory.addFunction(FunctionParameters.function("lerp").addType(PrimitiveTypes.DOUBLE).addType(PrimitiveTypes.DOUBLE).addType(PrimitiveTypes.DOUBLE).build(), lerpFunction);

		Function ifBody = new Function();
		ifBody.setExpressions(script, "System.print(true)", "delay(1000)", "return 1");
		Function elseBody = new Function();
		elseBody.setExpressions(script, "System.print(false)", "delay(2000)", "return 0");

		Function whileBody = new Function();
		whileBody.setExpressions(script, "System.print(var)", "delay(200)", "var = var + 1.0");

		Function literallyJustBreak = new Function();
		literallyJustBreak.setExpressions(script, "break");

		Function printAndBreak = new Function();
		printAndBreak.setExpressions(script, "System.print(888.888)", "break");

		Function literallyJustContinue = new Function();
		literallyJustContinue.setExpressions(script, "continue");

		Function whileBody_ = new Function();
		whileBody_.lines = new ExpressionExecutor[]
			{
				new ExpressionExecutor(script.memory).setExpression(script.getParser().setExpression("System.print(var)").parse()),
				new ExpressionExecutor(script.memory).setExpression(script.getParser().setExpression("delay(200)").parse()),
				new ExpressionExecutor(script.memory).setExpression(script.getParser().setExpression("var = var + 1.0").parse()),
				new ExpressionExecutor(script.memory).setExpression(new If(script.getParser().setExpression("var > 4.0").parse(), literallyJustBreak))
			};

		Function whileBody__ = new Function();
		whileBody__.lines = new ExpressionExecutor[]
			{
				new ExpressionExecutor(script.memory).setExpression(script.getParser().setExpression("System.print(var)").parse()),
				new ExpressionExecutor(script.memory).setExpression(script.getParser().setExpression("delay(200)").parse()),
				new ExpressionExecutor(script.memory).setExpression(script.getParser().setExpression("var = var + 1.0").parse()),
				new ExpressionExecutor(script.memory).setExpression(new IfElse(new If(script.getParser().setExpression("var < 3.0").parse(), literallyJustContinue), printAndBreak)),
				new ExpressionExecutor(script.memory).setExpression(script.getParser().setExpression("System.print(666.666)").parse())
			};

		Function del = new Function();
		del.setExpressions(script, "delay(1000)", "System.print(8)", "return 3.0");
		script.getMemory().addFunction(FunctionParameters.function("del").build(), del);*/

		script.setExpressions(
			"import type vec2",
			"import type double",
			"import type int",

			"import library Math",
			"import library System",

			"temp = vec2(3.02, double(6.7)).normalize()",
			"System.printDetail(temp)",
			"var = temp.makeZero()",
			"System.printDetail(temp)",
			"System.printDetail(var)",
			"System.print(false == true)", // false
			"System.print(\"Hello world\")",
			"System.print(5 & 3)", // 1
			"System.print(1 << 3)", // 8
			"return temp"
		);
//		script.setExpressions("return lerp(3.0, 5.0, 0.5) * lerp(0.0, 1.0, 0.5)");
//		script.setExpressions("return lerp(3.0, 5.0, lerp(0.0, 1.0, 0.5)) * lerp(-1.0, 2.0, 0.5)");
//		script.setExpressions("v = vec2(3.02, 6.7)", "return v.normalize()");
//		script.setExpressions("return Math.sqrt(2.0)");

		// if / if-else
/*
		script.lines = new ExpressionExecutor[]
			{
				new ExpressionExecutor(script.memory).setExpression(script.parser.setExpression("var = 2.0").parse()),
//				new ExpressionExecutor(script.memory).setExpression(new If(new BinaryOperator(Operator.LESS_THAN, new Variable(VariableSource.memory("var")), new Constant(PrimitiveTypes.DOUBLE, 3.0)), ifBody)) // return nothing if false
				new ExpressionExecutor(script.memory).setExpression(new IfElse(new If(new BinaryOperator(Operator.LESS_THAN, new Variable(VariableSource.memory("var")), new Constant(PrimitiveTypes.DOUBLE, 3.0)), ifBody), elseBody))
			};*/

		// while loops
/*
		script.lines = new ExpressionExecutor[]
			{
				new ExpressionExecutor(script.memory).setExpression(new Assignment("var", new Constant(PrimitiveTypes.DOUBLE, 0.0))),
				new ExpressionExecutor(script.memory).setExpression(new While(new If(script.getParser().setExpression("var < 5.0").parse(), whileBody))),
				new ExpressionExecutor(script.memory).setExpression(script.parser.setExpression("return true").parse())
			};*/

		/*
		script.lines = new ExpressionExecutor[]
			{
				new ExpressionExecutor(script.memory).setExpression(new Assignment("var", new Constant(PrimitiveTypes.DOUBLE, 0.0))),
				new ExpressionExecutor(script.memory).setExpression(new While(new If(script.parser.setExpression("true").parse(), whileBody_))),
				new ExpressionExecutor(script.memory).setExpression(script.parser.setExpression("return true").parse())
			};*/
/*
		script.lines = new ExpressionExecutor[]
			{
				new ExpressionExecutor(script.memory).setExpression(script.getParser().setExpression("import library System").parse()),
				new ExpressionExecutor(script.memory).setExpression(new Assignment("var", new Constant(PrimitiveTypes.DOUBLE, 0.0))),
				new ExpressionExecutor(script.memory).setExpression(script.getParser().setExpression("delay(500)").parse()),
				new ExpressionExecutor(script.memory).setExpression(new While(new If(script.getParser().setExpression("var < 7.0").parse(), whileBody__))),
				new ExpressionExecutor(script.memory).setExpression(script.getParser().setExpression("return true").parse())
			};*/

		runWithDelay(script);
//		System.out.println("-".repeat(64));
//		runWithDelay(script);
	}

	private static void runWithDelay(Script script) throws InterruptedException
	{
		Result ret = Result.delay();

		while (ret.isDelay() && !ret.isReturnValue() && !ret.isReturn())
		{
//			Thread.sleep(100);
			ret = script.execute();
		}
		if (ret.isReturnValue())
			System.out.println("Returned: " + ret.getValue());
		else
			System.out.println("Last value: " + ret.getValue());
	}
}
