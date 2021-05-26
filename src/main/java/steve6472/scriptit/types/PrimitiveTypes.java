package steve6472.scriptit.types;

import steve6472.scriptit.*;

import static steve6472.scriptit.Value.newValue;

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
	public static final Type CHAR = new Type("char");
	public static final Type NULL = new Type("null");

	public static final Value TRUE = newValue(BOOL, true);
	public static final Value FALSE = newValue(BOOL, false);

	static
	{
		/*
		 * Double
		 */
		DOUBLE.addConstructor(FunctionParameters.create(DOUBLE, DOUBLE), new Constructor(args -> newValue(DOUBLE, args[0].getDouble())));
		DOUBLE.addConstructor(FunctionParameters.create(DOUBLE, INT), new Constructor(args -> newValue(DOUBLE, (double) args[0].getInt())));
		DOUBLE.addConstructor(FunctionParameters.create(DOUBLE), new Constructor(args -> newValue(DOUBLE, 0.0)));

		DOUBLE.addBinaryOperator(DOUBLE, Operator.ADD, new BinaryOperatorOverload((left, right) -> newValue(DOUBLE, left.getDouble() + right.getDouble())));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.SUB, new BinaryOperatorOverload((left, right) -> newValue(DOUBLE, left.getDouble() - right.getDouble())));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.MUL, new BinaryOperatorOverload((left, right) -> newValue(DOUBLE, left.getDouble() * right.getDouble())));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.DIV, new BinaryOperatorOverload((left, right) -> newValue(DOUBLE, left.getDouble() / right.getDouble())));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.MOD, new BinaryOperatorOverload((left, right) -> newValue(DOUBLE, left.getDouble() % right.getDouble())));

		DOUBLE.addBinaryOperator(INT, Operator.ADD, new BinaryOperatorOverload((left, right) -> newValue(DOUBLE, left.getDouble() + right.getInt())));
		DOUBLE.addBinaryOperator(INT, Operator.SUB, new BinaryOperatorOverload((left, right) -> newValue(DOUBLE, left.getDouble() - right.getInt())));
		DOUBLE.addBinaryOperator(INT, Operator.MUL, new BinaryOperatorOverload((left, right) -> newValue(DOUBLE, left.getDouble() * right.getInt())));
		DOUBLE.addBinaryOperator(INT, Operator.DIV, new BinaryOperatorOverload((left, right) -> newValue(DOUBLE, left.getDouble() / right.getInt())));
		DOUBLE.addBinaryOperator(INT, Operator.MOD, new BinaryOperatorOverload((left, right) -> newValue(DOUBLE, left.getDouble() % right.getInt())));

		DOUBLE.addBinaryOperator(DOUBLE, Operator.EQUAL, new BinaryOperatorOverload((left, right) -> left.getDouble() == right.getDouble() ? TRUE : FALSE));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.NOT_EQUAL, new BinaryOperatorOverload((left, right) -> left.getDouble() != right.getDouble() ? TRUE : FALSE));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.LESS_THAN, new BinaryOperatorOverload((left, right) -> left.getDouble() < right.getDouble() ? TRUE : FALSE));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.GREATER_THAN, new BinaryOperatorOverload((left, right) -> left.getDouble() > right.getDouble() ? TRUE : FALSE));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.LESS_THAN_EQUAL, new BinaryOperatorOverload((left, right) -> left.getDouble() <= right.getDouble() ? TRUE : FALSE));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.GREATER_THAN_EQUAL, new BinaryOperatorOverload((left, right) -> left.getDouble() >= right.getDouble() ? TRUE : FALSE));

		DOUBLE.addBinaryOperator(INT, Operator.EQUAL, new BinaryOperatorOverload((left, right) -> left.getDouble() == right.getInt() ? TRUE : FALSE));
		DOUBLE.addBinaryOperator(INT, Operator.NOT_EQUAL, new BinaryOperatorOverload((left, right) -> left.getDouble() != right.getInt() ? TRUE : FALSE));
		DOUBLE.addBinaryOperator(INT, Operator.LESS_THAN, new BinaryOperatorOverload((left, right) -> left.getDouble() < right.getInt() ? TRUE : FALSE));
		DOUBLE.addBinaryOperator(INT, Operator.GREATER_THAN, new BinaryOperatorOverload((left, right) -> left.getDouble() > right.getInt() ? TRUE : FALSE));
		DOUBLE.addBinaryOperator(INT, Operator.LESS_THAN_EQUAL, new BinaryOperatorOverload((left, right) -> left.getDouble() <= right.getInt() ? TRUE : FALSE));
		DOUBLE.addBinaryOperator(INT, Operator.GREATER_THAN_EQUAL, new BinaryOperatorOverload((left, right) -> left.getDouble() >= right.getInt() ? TRUE : FALSE));

		DOUBLE.addUnaryOperator(Operator.ADD, new UnaryOperatorOverload(right -> newValue(DOUBLE, +right.getDouble())));
		DOUBLE.addUnaryOperator(Operator.SUB, new UnaryOperatorOverload(right -> newValue(DOUBLE, -right.getDouble())));

		/*
		 * Int
		 */

		INT.addConstructor(FunctionParameters.create(INT, DOUBLE), new Constructor(args -> newValue(INT, (int) args[0].getDouble())));
		INT.addConstructor(FunctionParameters.create(INT, STRING), new Constructor(args -> newValue(INT, Integer.parseInt(args[0].getString()))));
		INT.addConstructor(FunctionParameters.create(INT, INT), new Constructor(args -> newValue(INT, args[0].getInt())));
		INT.addConstructor(FunctionParameters.create(INT), new Constructor(args -> newValue(INT, 0)));

		INT.addBinaryOperator(INT, Operator.ADD, new BinaryOperatorOverload((left, right) -> newValue(INT, left.getInt() + right.getInt())));
		INT.addBinaryOperator(INT, Operator.SUB, new BinaryOperatorOverload((left, right) -> newValue(INT, left.getInt() - right.getInt())));
		INT.addBinaryOperator(INT, Operator.MUL, new BinaryOperatorOverload((left, right) -> newValue(INT, left.getInt() * right.getInt())));
		INT.addBinaryOperator(INT, Operator.DIV, new BinaryOperatorOverload((left, right) -> newValue(INT, left.getInt() / right.getInt())));
		INT.addBinaryOperator(INT, Operator.MOD, new BinaryOperatorOverload((left, right) -> newValue(INT, left.getInt() % right.getInt())));

		INT.addBinaryOperator(DOUBLE, Operator.ADD, new BinaryOperatorOverload((left, right) -> newValue(DOUBLE, left.getInt() + right.getDouble())));
		INT.addBinaryOperator(DOUBLE, Operator.SUB, new BinaryOperatorOverload((left, right) -> newValue(DOUBLE, left.getInt() - right.getDouble())));
		INT.addBinaryOperator(DOUBLE, Operator.MUL, new BinaryOperatorOverload((left, right) -> newValue(DOUBLE, left.getInt() * right.getDouble())));
		INT.addBinaryOperator(DOUBLE, Operator.DIV, new BinaryOperatorOverload((left, right) -> newValue(DOUBLE, left.getInt() / right.getDouble())));
		INT.addBinaryOperator(DOUBLE, Operator.MOD, new BinaryOperatorOverload((left, right) -> newValue(DOUBLE, left.getInt() % right.getDouble())));

		INT.addBinaryOperator(INT, Operator.EQUAL, new BinaryOperatorOverload((left, right) -> left.getInt() == right.getInt() ? TRUE : FALSE));
		INT.addBinaryOperator(INT, Operator.NOT_EQUAL, new BinaryOperatorOverload((left, right) -> left.getInt() != right.getInt() ? TRUE : FALSE));
		INT.addBinaryOperator(INT, Operator.LESS_THAN, new BinaryOperatorOverload((left, right) -> left.getInt() < right.getInt() ? TRUE : FALSE));
		INT.addBinaryOperator(INT, Operator.GREATER_THAN, new BinaryOperatorOverload((left, right) -> left.getInt() > right.getInt() ? TRUE : FALSE));
		INT.addBinaryOperator(INT, Operator.LESS_THAN_EQUAL, new BinaryOperatorOverload((left, right) -> left.getInt() <= right.getInt() ? TRUE : FALSE));
		INT.addBinaryOperator(INT, Operator.GREATER_THAN_EQUAL, new BinaryOperatorOverload((left, right) -> left.getInt() >= right.getInt() ? TRUE : FALSE));

		INT.addBinaryOperator(DOUBLE, Operator.EQUAL, new BinaryOperatorOverload((left, right) -> left.getInt() == right.getDouble() ? TRUE : FALSE));
		INT.addBinaryOperator(DOUBLE, Operator.NOT_EQUAL, new BinaryOperatorOverload((left, right) -> left.getInt() != right.getDouble() ? TRUE : FALSE));
		INT.addBinaryOperator(DOUBLE, Operator.LESS_THAN, new BinaryOperatorOverload((left, right) -> left.getInt() < right.getDouble() ? TRUE : FALSE));
		INT.addBinaryOperator(DOUBLE, Operator.GREATER_THAN, new BinaryOperatorOverload((left, right) -> left.getInt() > right.getDouble() ? TRUE : FALSE));
		INT.addBinaryOperator(DOUBLE, Operator.LESS_THAN_EQUAL, new BinaryOperatorOverload((left, right) -> left.getInt() <= right.getDouble() ? TRUE : FALSE));
		INT.addBinaryOperator(DOUBLE, Operator.GREATER_THAN_EQUAL, new BinaryOperatorOverload((left, right) -> left.getInt() >= right.getDouble() ? TRUE : FALSE));

		INT.addBinaryOperator(INT, Operator.BIT_OR, new BinaryOperatorOverload((left, right) -> newValue(INT, left.getInt() | right.getInt())));
		INT.addBinaryOperator(INT, Operator.BIT_AND, new BinaryOperatorOverload((left, right) -> newValue(INT, left.getInt() & right.getInt())));
		INT.addBinaryOperator(INT, Operator.BIT_XOR, new BinaryOperatorOverload((left, right) -> newValue(INT, left.getInt() ^ right.getInt())));
		INT.addBinaryOperator(INT, Operator.LSH, new BinaryOperatorOverload((left, right) -> newValue(INT, left.getInt() << right.getInt())));
		INT.addBinaryOperator(INT, Operator.RSH, new BinaryOperatorOverload((left, right) -> newValue(INT, left.getInt() >> right.getInt())));

		INT.addUnaryOperator(Operator.ADD, new UnaryOperatorOverload(right -> newValue(INT, +right.getInt())));
		INT.addUnaryOperator(Operator.SUB, new UnaryOperatorOverload(right -> newValue(INT, -right.getInt())));
		INT.addUnaryOperator(Operator.NEG, new UnaryOperatorOverload(right -> newValue(INT, ~right.getInt())));

		/*
		 * Bool
		 */

		BOOL.addConstructor(FunctionParameters.create(BOOL, STRING), new Constructor(args -> newValue(BOOL, Boolean.parseBoolean(args[0].getString()))));
		BOOL.addConstructor(FunctionParameters.create(BOOL, INT), new Constructor(args -> newValue(BOOL, args[0].getInt() > 0)));
		BOOL.addConstructor(FunctionParameters.create(BOOL), new Constructor(args -> newValue(BOOL, false)));

		BOOL.addBinaryOperator(BOOL, Operator.EQUAL, new BinaryOperatorOverload((left, right) -> left.getBoolean() == right.getBoolean() ? TRUE : FALSE));
		BOOL.addBinaryOperator(BOOL, Operator.NOT_EQUAL, new BinaryOperatorOverload((left, right) -> left.getBoolean() != right.getBoolean() ? TRUE : FALSE));
		BOOL.addBinaryOperator(BOOL, Operator.OR, new BinaryOperatorOverload((left, right) -> left.getBoolean() || right.getBoolean() ? TRUE : FALSE));
		BOOL.addBinaryOperator(BOOL, Operator.AND, new BinaryOperatorOverload((left, right) -> left.getBoolean() && right.getBoolean() ? TRUE : FALSE));

		BOOL.addUnaryOperator(Operator.NOT, new UnaryOperatorOverload(right -> right.getBoolean() ? FALSE : TRUE));

		/*
		 * Char
		 */

		CHAR.addConstructor(FunctionParameters.create(CHAR, CHAR), new Constructor(args -> newValue(CHAR, args[0].getChar())));
		CHAR.addConstructor(FunctionParameters.create(CHAR), new Constructor(args -> newValue(CHAR, Character.MIN_VALUE)));

		CHAR.addBinaryOperator(CHAR, Operator.EQUAL, new BinaryOperatorOverload((left, right) -> left.getChar() == right.getChar() ? TRUE : FALSE));
		CHAR.addBinaryOperator(CHAR, Operator.NOT_EQUAL, new BinaryOperatorOverload((left, right) -> left.getChar() != right.getChar() ? TRUE : FALSE));
		CHAR.addBinaryOperator(CHAR, Operator.ADD, new BinaryOperatorOverload((left, right) -> newValue(STRING, left.getChar() + "" + right.getChar())));

		/*
		 * String
		 */

		STRING.addConstructor(FunctionParameters.create(STRING, STRING), new Constructor(args -> newValue(STRING, args[0].getString())));
		STRING.addConstructor(FunctionParameters.create(STRING, DOUBLE), new Constructor(args -> newValue(STRING, String.valueOf(args[0].getDouble()))));
		STRING.addConstructor(FunctionParameters.create(STRING, INT), new Constructor(args -> newValue(STRING, String.valueOf(args[0].getInt()))));
		STRING.addConstructor(FunctionParameters.create(STRING), new Constructor(args -> newValue(STRING, "")));

		STRING.addBinaryOperator(STRING, Operator.EQUAL, new BinaryOperatorOverload((left, right) -> left.getString().equals(right.getString()) ? TRUE : FALSE));
		STRING.addBinaryOperator(STRING, Operator.NOT_EQUAL, new BinaryOperatorOverload((left, right) -> !left.getString().equals(right.getString()) ? TRUE : FALSE));

		STRING.addBinaryOperator(STRING, Operator.ADD, new BinaryOperatorOverload((left, right) -> newValue(STRING, left.getString() + right.getString())));
		STRING.addBinaryOperator(DOUBLE, Operator.ADD, new BinaryOperatorOverload((left, right) -> newValue(STRING, left.getString() + right.getDouble())));
		STRING.addBinaryOperator(CHAR, Operator.ADD, new BinaryOperatorOverload((left, right) -> newValue(STRING, left.getString() + right.getChar())));
		STRING.addBinaryOperator(INT, Operator.ADD, new BinaryOperatorOverload((left, right) -> newValue(STRING, left.getString() + right.getInt())));
		STRING.addBinaryOperator(INT, Operator.SUB, new BinaryOperatorOverload((left, right) -> newValue(STRING, left.getString().substring(0, left.getString().length() - right.getInt())))); // remove n characters from back
		STRING.addBinaryOperator(INT, Operator.MUL, new BinaryOperatorOverload((left, right) -> newValue(STRING, left.getString().repeat(right.getInt())))); // repeat the string n times
		STRING.addUnaryOperator(Operator.SUB, new UnaryOperatorOverload(right -> right.setValue(new StringBuilder(right.getString()).reverse().toString()))); // reverse the string
		STRING.addFunction(FunctionParameters.function("len").build(), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				return Result.value(newValue(INT, typeFunction.getString().length()));
			}
		});
		STRING.addFunction(FunctionParameters.create("charAt", INT), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				return Result.value(newValue(CHAR, typeFunction.getString().charAt(arguments[0].getInt())));
			}
		});
		STRING.addFunction(FunctionParameters.create("setChar", INT, CHAR), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				StringBuilder builder = new StringBuilder(typeFunction.getString());
				builder.setCharAt(arguments[0].getInt(), arguments[1].getChar());
				return Result.value(newValue(STRING, builder.toString()));
			}
		});
		STRING.addFunction(FunctionParameters.create("substring", INT), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				return Result.value(newValue(STRING, typeFunction.getString().substring(arguments[0].getInt())));
			}
		});
		STRING.addFunction(FunctionParameters.create("substring", INT, INT), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				return Result.value(newValue(STRING, typeFunction.getString().substring(arguments[0].getInt(), arguments[1].getInt())));
			}
		});
	}

	public static boolean isPrimitive(Type type)
	{
		return type == DOUBLE || type == INT || type == NULL || type == BOOL || type == STRING || type == CHAR;
	}
}
