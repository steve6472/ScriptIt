package steve6472.scriptit.exp;

import static steve6472.scriptit.exp.Value.newValue;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/21/2021
 * Project: ScriptIt
 *
 ***********************/
public class PrimitiveTypes
{
	public static final Type DOUBLE = new Type("double");
	public static final Type INT = new Type("int");
	public static final Type BOOL = new Type("bool");
	public static final Type NULL = new Type("null");

	public static final Value TRUE = newValue(BOOL, true);
	public static final Value FALSE = newValue(BOOL, false);

	public static final Type VEC2 = new Type("vec2");

	public static void init(Main.Script script)
	{
		DOUBLE.addBinaryOperator(DOUBLE, Operator.ADD, new BinaryOperatorOverload((left, right) -> Result.value(newValue(DOUBLE, left.getDouble() + right.getDouble()))));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.SUB, new BinaryOperatorOverload((left, right) -> Result.value(newValue(DOUBLE, left.getDouble() - right.getDouble()))));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.MUL, new BinaryOperatorOverload((left, right) -> Result.value(newValue(DOUBLE, left.getDouble() * right.getDouble()))));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.DIV, new BinaryOperatorOverload((left, right) -> Result.value(newValue(DOUBLE, left.getDouble() / right.getDouble()))));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.MOD, new BinaryOperatorOverload((left, right) -> Result.value(newValue(DOUBLE, left.getDouble() % right.getDouble()))));


		DOUBLE.addBinaryOperator(DOUBLE, Operator.EQUAL, new BinaryOperatorOverload((left, right) -> Result.value(left.getDouble() == right.getDouble() ? TRUE : FALSE)));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.NOT_EQUAL, new BinaryOperatorOverload((left, right) -> Result.value(left.getDouble() != right.getDouble() ? TRUE : FALSE)));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.LESS_THAN, new BinaryOperatorOverload((left, right) -> Result.value(left.getDouble() < right.getDouble() ? TRUE : FALSE)));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.GREATER_THAN, new BinaryOperatorOverload((left, right) -> Result.value(left.getDouble() > right.getDouble() ? TRUE : FALSE)));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.LESS_THAN_EQUAL, new BinaryOperatorOverload((left, right) -> Result.value(left.getDouble() <= right.getDouble() ? TRUE : FALSE)));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.GREATER_THAN_EQUAL, new BinaryOperatorOverload((left, right) -> Result.value(left.getDouble() >= right.getDouble() ? TRUE : FALSE)));

		DOUBLE.addUnaryOperator(Operator.ADD, new UnaryOperatorOverload(left -> Result.value(newValue(DOUBLE, +left.getDouble()))));
		DOUBLE.addUnaryOperator(Operator.SUB, new UnaryOperatorOverload(left -> Result.value(newValue(DOUBLE, -left.getDouble()))));

//		VEC2.addConstructor(FunctionParameters.constructor(VEC2).addType(DOUBLE).addType(DOUBLE).build(), par -> newValue(VEC2).setValue("x", newValue(DOUBLE, par[0].getDouble())).setValue("y", newValue(DOUBLE, par[1].getDouble())));
//		VEC2.addConstructor(FunctionParameters.constructor(VEC2).addType(INT).addType(INT).build(), par -> newValue(VEC2).setValue("x", newValue(DOUBLE, (double) par[0].getInt())).setValue("y", newValue(DOUBLE, (double) par[1].getInt())));
//		VEC2.addConstructor(FunctionParameters.constructor(VEC2).addType(DOUBLE).build(), par -> newValue(VEC2).setValue("x", newValue(DOUBLE, par[0].getDouble())).setValue("y", newValue(DOUBLE, par[0].getDouble())));
//		VEC2.addConstructor(FunctionParameters.constructor(VEC2).addType(VEC2).build(), par -> newValue(VEC2).setValue("x", par[0].getValue("x").getDouble()).setValue("y", par[0].getValue("y").getDouble()));
//		VEC2.addConstructor(FunctionParameters.constructor(VEC2).build(), par -> newValue(VEC2).setValue("x", newValue(DOUBLE, 0)).setValue("y", newValue(DOUBLE, 0)));

		VEC2.addBinaryOperator(VEC2, Operator.ADD, new BinaryOperatorOverload((left, right) -> Result.value(newValue(VEC2).setValue("x", left.getValue("x").getDouble() + right.getValue("x").getDouble()).setValue("y", left.getValue("y").getDouble() + right.getValue("y").getDouble()))));
		VEC2.addBinaryOperator(VEC2, Operator.SUB, new BinaryOperatorOverload((left, right) -> Result.value(newValue(VEC2).setValue("x", left.getValue("x").getDouble() - right.getValue("x").getDouble()).setValue("y", left.getValue("y").getDouble() - right.getValue("y").getDouble()))));
		VEC2.addBinaryOperator(VEC2, Operator.MUL, new BinaryOperatorOverload((left, right) -> Result.value(newValue(VEC2).setValue("x", left.getDoubleValue("x") * right.getDoubleValue("x")).setValue("y", left.getDoubleValue("y") * right.getDoubleValue("y")))));
		VEC2.addBinaryOperator(VEC2, Operator.DIV, new BinaryOperatorOverload((left, right) -> Result.value(newValue(VEC2).setValue("x", left.getValue("x").getDouble() / right.getValue("x").getDouble()).setValue("y", left.getValue("y").getDouble() / right.getValue("y").getDouble()))));

		VEC2.addBinaryOperator(DOUBLE, Operator.ADD, new BinaryOperatorOverload((left, right) -> Result.value(newValue(VEC2).setValue("x", newValue(DOUBLE, left.getValue("x").getDouble() + right.getDouble())).setValue("y", newValue(DOUBLE, left.getValue("y").getDouble() + right.getDouble())))));
		VEC2.addBinaryOperator(DOUBLE, Operator.SUB, new BinaryOperatorOverload((left, right) -> Result.value(newValue(VEC2).setValue("x", newValue(DOUBLE, left.getValue("x").getDouble() - right.getDouble())).setValue("y", newValue(DOUBLE, left.getValue("y").getDouble() - right.getDouble())))));
		VEC2.addBinaryOperator(DOUBLE, Operator.MUL, new BinaryOperatorOverload((left, right) -> Result.value(newValue(VEC2).setValue("x", newValue(DOUBLE, left.getValue("x").getDouble() * right.getDouble())).setValue("y", newValue(DOUBLE, left.getValue("y").getDouble() * right.getDouble())))));
		VEC2.addBinaryOperator(DOUBLE, Operator.DIV, new BinaryOperatorOverload((left, right) -> Result.value(newValue(VEC2).setValue("x", newValue(DOUBLE, left.getValue("x").getDouble() / right.getDouble())).setValue("y", newValue(DOUBLE, left.getValue("y").getDouble() / right.getDouble())))));

		VEC2.addUnaryOperator(Operator.SUB, new UnaryOperatorOverload(left -> Result.value(left.setValue("x", -left.getValue("x").getDouble()).setValue("y", -left.getValue("y").getDouble()))));
		VEC2.addUnaryOperator(Operator.ADD, new UnaryOperatorOverload(Result::value));

		Function normalizeVec2 = new Function();
		normalizeVec2.setExpressions(script, "len = 1.0 / len()", "return vec2(x * len, y * len)");
		VEC2.addFunction(FunctionParameters.function("normalize").build(), normalizeVec2);

		Function lenVec2 = new Function();
		lenVec2.setExpressions(script, "return Math.sqrt(x * x + y * y)");
		VEC2.addFunction(FunctionParameters.function("len").build(), lenVec2);

//		VEC2.addFunction(FunctionParameters.function("normalize").build(), (itself, args) -> {
//			double x = itself.getDouble("x");
//			double y = itself.getDouble("y");
//			double len = 1.0 / Math.sqrt(x * x + y * y);
//
//			return newValue(VEC2, new double[] {x * len, y * len});
//		});
//		VEC2.addFunction(FunctionParameters.function("len").build(), (itself, args) -> {
//			double x = itself.getDouble("x");
//			double y = itself.getDouble("y");
//			double len = Math.sqrt(x * x + y * y);
//
//			return newValue(DOUBLE, len);
//		});
//		VEC2.addFunction(FunctionParameters.function("toString").build(), (itself, args) -> newValue(STRING, "[x=" + itself.getValue("x").getDouble() + ",y=" + itself.getValue("y").getDouble() + "]"));
//		VEC2.addFunction(FunctionParameters.function("print").build(), (itself, args) -> System.out.println("vec2[x=" + itself.getValue("x").getDouble() + ",y=" + itself.getValue("y").getDouble() + "]"));

//		VEC2.addFunction(FunctionParameters.function("getX").build(), ((itself, args) -> itself.getValue("x")));
//		VEC2.addFunction(FunctionParameters.function("getY").build(), ((itself, args) -> itself.getValue("y")));
	}

	public static boolean isPrimitive(Type type)
	{
		return type == NULL || type == DOUBLE|| type == INT /* || type == STRING || type == CHAR || type == BOOL || type == ARRAY*/;
	}

	@FunctionalInterface
	private interface BinaryOperator
	{
		Result apply(Value left, Value right);
	}

	@FunctionalInterface
	private interface UnaryOperator
	{
		Result apply(Value left);
	}

	private static class BinaryOperatorOverload extends Function
	{
		private final BinaryOperator func;

		BinaryOperatorOverload(BinaryOperator func)
		{
			this.func = func;
			this.argumentNames = new String[] {"left", "right"};
		}

		@Override
		public Result apply(Main.Script script)
		{
			return func.apply(arguments[0], arguments[1]);
		}
	}

	private static class UnaryOperatorOverload extends Function
	{
		private final UnaryOperator func;

		UnaryOperatorOverload(UnaryOperator func)
		{
			this.func = func;
			this.argumentNames = new String[] {"left"};
		}

		@Override
		public Result apply(Main.Script script)
		{
			return func.apply(arguments[0]);
		}
	}
}
