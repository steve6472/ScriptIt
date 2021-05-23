package steve6472.scriptit.exp.types;

import steve6472.scriptit.exp.*;

import static steve6472.scriptit.exp.Value.newValue;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/21/2021
 * Project: ScriptIt
 *
 ***********************/
public class PrimitiveTypes extends TypesInit
{
	public static final Type DOUBLE = new Type("double");
	public static final Type INT = new Type("int");
	public static final Type BOOL = new Type("bool");
	public static final Type STRING = new Type("string");
	public static final Type NULL = new Type("null");

	public static final Value TRUE = newValue(BOOL, true);
	public static final Value FALSE = newValue(BOOL, false);

	static
	{
		DOUBLE.addConstructor(FunctionParameters.create(DOUBLE, DOUBLE), new Constructor(args -> newValue(DOUBLE, args[0].getDouble())));
		DOUBLE.addConstructor(FunctionParameters.create(DOUBLE, INT), new Constructor(args -> newValue(DOUBLE, (double) args[0].getInt())));
		DOUBLE.addConstructor(FunctionParameters.create(DOUBLE), new Constructor(args -> newValue(DOUBLE, 0.0)));

		DOUBLE.addBinaryOperator(DOUBLE, Operator.ADD, new BinaryOperatorOverload((left, right) -> newValue(DOUBLE, left.getDouble() + right.getDouble())));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.SUB, new BinaryOperatorOverload((left, right) -> newValue(DOUBLE, left.getDouble() - right.getDouble())));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.MUL, new BinaryOperatorOverload((left, right) -> newValue(DOUBLE, left.getDouble() * right.getDouble())));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.DIV, new BinaryOperatorOverload((left, right) -> newValue(DOUBLE, left.getDouble() / right.getDouble())));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.MOD, new BinaryOperatorOverload((left, right) -> newValue(DOUBLE, left.getDouble() % right.getDouble())));

		DOUBLE.addBinaryOperator(DOUBLE, Operator.EQUAL, new BinaryOperatorOverload((left, right) -> left.getDouble() == right.getDouble() ? TRUE : FALSE));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.NOT_EQUAL, new BinaryOperatorOverload((left, right) -> left.getDouble() != right.getDouble() ? TRUE : FALSE));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.LESS_THAN, new BinaryOperatorOverload((left, right) -> left.getDouble() < right.getDouble() ? TRUE : FALSE));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.GREATER_THAN, new BinaryOperatorOverload((left, right) -> left.getDouble() > right.getDouble() ? TRUE : FALSE));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.LESS_THAN_EQUAL, new BinaryOperatorOverload((left, right) -> left.getDouble() <= right.getDouble() ? TRUE : FALSE));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.GREATER_THAN_EQUAL, new BinaryOperatorOverload((left, right) -> left.getDouble() >= right.getDouble() ? TRUE : FALSE));

		DOUBLE.addUnaryOperator(Operator.ADD, new UnaryOperatorOverload(right -> newValue(DOUBLE, +right.getDouble())));
		DOUBLE.addUnaryOperator(Operator.SUB, new UnaryOperatorOverload(right -> newValue(DOUBLE, -right.getDouble())));


		INT.addConstructor(FunctionParameters.create(INT, DOUBLE), new Constructor(args -> newValue(INT, (int) args[0].getDouble())));
		INT.addConstructor(FunctionParameters.create(INT, INT), new Constructor(args -> newValue(INT, args[0].getInt())));
		INT.addConstructor(FunctionParameters.create(INT), new Constructor(args -> newValue(INT, 0)));

		INT.addBinaryOperator(INT, Operator.ADD, new BinaryOperatorOverload((left, right) -> newValue(INT, left.getInt() + right.getInt())));
		INT.addBinaryOperator(INT, Operator.SUB, new BinaryOperatorOverload((left, right) -> newValue(INT, left.getInt() - right.getInt())));
		INT.addBinaryOperator(INT, Operator.MUL, new BinaryOperatorOverload((left, right) -> newValue(INT, left.getInt() * right.getInt())));
		INT.addBinaryOperator(INT, Operator.DIV, new BinaryOperatorOverload((left, right) -> newValue(INT, left.getInt() / right.getInt())));
		INT.addBinaryOperator(INT, Operator.MOD, new BinaryOperatorOverload((left, right) -> newValue(INT, left.getInt() % right.getInt())));

		INT.addBinaryOperator(INT, Operator.EQUAL, new BinaryOperatorOverload((left, right) -> left.getInt() == right.getInt() ? TRUE : FALSE));
		INT.addBinaryOperator(INT, Operator.NOT_EQUAL, new BinaryOperatorOverload((left, right) -> left.getInt() != right.getInt() ? TRUE : FALSE));
		INT.addBinaryOperator(INT, Operator.LESS_THAN, new BinaryOperatorOverload((left, right) -> left.getInt() < right.getInt() ? TRUE : FALSE));
		INT.addBinaryOperator(INT, Operator.GREATER_THAN, new BinaryOperatorOverload((left, right) -> left.getInt() > right.getInt() ? TRUE : FALSE));
		INT.addBinaryOperator(INT, Operator.LESS_THAN_EQUAL, new BinaryOperatorOverload((left, right) -> left.getInt() <= right.getInt() ? TRUE : FALSE));
		INT.addBinaryOperator(INT, Operator.GREATER_THAN_EQUAL, new BinaryOperatorOverload((left, right) -> left.getInt() >= right.getInt() ? TRUE : FALSE));

		INT.addBinaryOperator(INT, Operator.BIT_OR, new BinaryOperatorOverload((left, right) -> newValue(INT, left.getInt() | right.getInt())));
		INT.addBinaryOperator(INT, Operator.BIT_AND, new BinaryOperatorOverload((left, right) -> newValue(INT, left.getInt() & right.getInt())));
		INT.addBinaryOperator(INT, Operator.BIT_XOR, new BinaryOperatorOverload((left, right) -> newValue(INT, left.getInt() ^ right.getInt())));
		INT.addBinaryOperator(INT, Operator.LSH, new BinaryOperatorOverload((left, right) -> newValue(INT, left.getInt() << right.getInt())));
		INT.addBinaryOperator(INT, Operator.RSH, new BinaryOperatorOverload((left, right) -> newValue(INT, left.getInt() >> right.getInt())));

		INT.addUnaryOperator(Operator.ADD, new UnaryOperatorOverload(right -> newValue(INT, +right.getInt())));
		INT.addUnaryOperator(Operator.SUB, new UnaryOperatorOverload(right -> newValue(INT, -right.getInt())));
		INT.addUnaryOperator(Operator.NEG, new UnaryOperatorOverload(right -> newValue(INT, ~right.getInt())));


		BOOL.addConstructor(FunctionParameters.create(BOOL, STRING), new Constructor(args -> newValue(BOOL, Boolean.parseBoolean(args[0].getString()))));
		BOOL.addConstructor(FunctionParameters.create(BOOL, INT), new Constructor(args -> newValue(BOOL, args[0].getInt() > 0)));
		BOOL.addConstructor(FunctionParameters.create(BOOL), new Constructor(args -> newValue(BOOL, false)));

		BOOL.addBinaryOperator(BOOL, Operator.EQUAL, new BinaryOperatorOverload((left, right) -> left.getBoolean() == right.getBoolean() ? TRUE : FALSE));
		BOOL.addBinaryOperator(BOOL, Operator.NOT_EQUAL, new BinaryOperatorOverload((left, right) -> left.getBoolean() != right.getBoolean() ? TRUE : FALSE));

		BOOL.addUnaryOperator(Operator.NOT, new UnaryOperatorOverload(right -> right.getBoolean() ? FALSE : TRUE));
	}

	public static boolean isPrimitive(Type type)
	{
		return type == DOUBLE || type == INT || type == NULL || type == BOOL || type == STRING /*|| type == CHAR*/;
	}
}
