package steve6472.scriptit.exp;

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
	public static final Type NULL = new Type("null");

	static
	{
		DOUBLE.addBinaryOperator(DOUBLE, Operator.ADD, new BinaryOperatorOverload((left, right) -> Result.value(Value.newValue(DOUBLE, left.getDouble() + right.getDouble()))));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.SUB, new BinaryOperatorOverload((left, right) -> Result.value(Value.newValue(DOUBLE, left.getDouble() - right.getDouble()))));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.MUL, new BinaryOperatorOverload((left, right) -> Result.value(Value.newValue(DOUBLE, left.getDouble() * right.getDouble()))));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.DIV, new BinaryOperatorOverload((left, right) -> Result.value(Value.newValue(DOUBLE, left.getDouble() / right.getDouble()))));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.MOD, new BinaryOperatorOverload((left, right) -> Result.value(Value.newValue(DOUBLE, left.getDouble() % right.getDouble()))));

		DOUBLE.addUnaryOperator(Operator.ADD, new UnaryOperatorOverload(left -> Result.value(Value.newValue(DOUBLE, +left.getDouble()))));
		DOUBLE.addUnaryOperator(Operator.SUB, new UnaryOperatorOverload(left -> Result.value(Value.newValue(DOUBLE, -left.getDouble()))));
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
