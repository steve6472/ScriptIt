package steve6472.scriptit.type;

import steve6472.scriptit.*;
import steve6472.scriptit.expressions.Function;
import steve6472.scriptit.expressions.FunctionParameters;
import steve6472.scriptit.tokenizer.Operator;
import steve6472.scriptit.value.PrimitiveValue;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/21/2021
 * Project: ScriptIt
 *
 ***********************/
public class PrimitiveTypes extends TypesInit
{
	public static PrimitiveValue<Boolean> TRUE()
	{
		return newPrimitive(BOOL, true);
	}

	public static PrimitiveValue<Boolean> FALSE()
	{
		return newPrimitive(BOOL, false);
	}

	static
	{
		/*
		 * Double
		 */
		{
			DOUBLE.addConstructor(FunctionParameters.create(DOUBLE, DOUBLE), new Constructor(args -> newPrimitive(DOUBLE, args[0].asPrimitive().getDouble())));
			DOUBLE.addConstructor(FunctionParameters.create(DOUBLE, INT), new Constructor(args -> newPrimitive(DOUBLE, (double) args[0].asPrimitive().getInt())));
			DOUBLE.addConstructor(FunctionParameters.create(DOUBLE), new Constructor(args -> newPrimitive(DOUBLE, 0.0)));

			DOUBLE.addBinaryOperator(DOUBLE, Operator.ADD, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(DOUBLE, left.get() + right.get())));
			DOUBLE.addBinaryOperator(DOUBLE, Operator.SUB, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(DOUBLE, left.get() - right.get())));
			DOUBLE.addBinaryOperator(DOUBLE, Operator.MUL, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(DOUBLE, left.get() * right.get())));
			DOUBLE.addBinaryOperator(DOUBLE, Operator.DIV, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(DOUBLE, left.get() / right.get())));
			DOUBLE.addBinaryOperator(DOUBLE, Operator.MOD, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(DOUBLE, left.get() % right.get())));

			DOUBLE.addBinaryOperator(INT, Operator.ADD, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(DOUBLE, left.get() + right.get())));
			DOUBLE.addBinaryOperator(INT, Operator.SUB, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(DOUBLE, left.get() - right.get())));
			DOUBLE.addBinaryOperator(INT, Operator.MUL, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(DOUBLE, left.get() * right.get())));
			DOUBLE.addBinaryOperator(INT, Operator.DIV, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(DOUBLE, left.get() / right.get())));
			DOUBLE.addBinaryOperator(INT, Operator.MOD, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(DOUBLE, left.get() % right.get())));

			DOUBLE.addBinaryOperator(DOUBLE, Operator.ASSIGN_ADD, new PBinaryOperatorOverload<>((itself, right) -> itself.asPrimitive().set(itself.get() + right.get())));
			DOUBLE.addBinaryOperator(DOUBLE, Operator.ASSIGN_SUB, new PBinaryOperatorOverload<>((itself, right) -> itself.asPrimitive().set(itself.get() - right.get())));
			DOUBLE.addBinaryOperator(DOUBLE, Operator.ASSIGN_MUL, new PBinaryOperatorOverload<>((itself, right) -> itself.asPrimitive().set(itself.get() * right.get())));
			DOUBLE.addBinaryOperator(DOUBLE, Operator.ASSIGN_DIV, new PBinaryOperatorOverload<>((itself, right) -> itself.asPrimitive().set(itself.get() / right.get())));
			DOUBLE.addBinaryOperator(DOUBLE, Operator.ASSIGN_MOD, new PBinaryOperatorOverload<>((itself, right) -> itself.asPrimitive().set(itself.get() % right.get())));

			DOUBLE.addBinaryOperator(INT, Operator.ASSIGN_ADD, new PBinaryOperatorOverload<>((itself, right) -> itself.set(itself.get() + right.get())));
			DOUBLE.addBinaryOperator(INT, Operator.ASSIGN_SUB, new PBinaryOperatorOverload<>((itself, right) -> itself.set(itself.get() - right.get())));
			DOUBLE.addBinaryOperator(INT, Operator.ASSIGN_MUL, new PBinaryOperatorOverload<>((itself, right) -> itself.set(itself.get() * right.get())));
			DOUBLE.addBinaryOperator(INT, Operator.ASSIGN_DIV, new PBinaryOperatorOverload<>((itself, right) -> itself.set(itself.get() / right.get())));
			DOUBLE.addBinaryOperator(INT, Operator.ASSIGN_MOD, new PBinaryOperatorOverload<>((itself, right) -> itself.set(itself.get() % right.get())));

			DOUBLE.addBinaryOperator(DOUBLE, Operator.EQUAL, new PBinaryOperatorOverload<>((left, right) -> left.getDouble() == right.getDouble() ? TRUE() : FALSE()));
			DOUBLE.addBinaryOperator(DOUBLE, Operator.NOT_EQUAL, new PBinaryOperatorOverload<>((left, right) -> left.getDouble() != right.getDouble() ? TRUE() : FALSE()));
			DOUBLE.addBinaryOperator(DOUBLE, Operator.LESS_THAN, new PBinaryOperatorOverload<>((left, right) -> left.get() < right.get() ? TRUE() : FALSE()));
			DOUBLE.addBinaryOperator(DOUBLE, Operator.GREATER_THAN, new PBinaryOperatorOverload<>((left, right) -> left.get() > right.get() ? TRUE() : FALSE()));
			DOUBLE.addBinaryOperator(DOUBLE, Operator.LESS_THAN_EQUAL, new PBinaryOperatorOverload<>((left, right) -> left.get() <= right.get() ? TRUE() : FALSE()));
			DOUBLE.addBinaryOperator(DOUBLE, Operator.GREATER_THAN_EQUAL, new PBinaryOperatorOverload<>((left, right) -> left.get() >= right.get() ? TRUE() : FALSE()));

			DOUBLE.addBinaryOperator(INT, Operator.EQUAL, new PBinaryOperatorOverload<>((left, right) -> left.getDouble() == right.getInt() ? TRUE() : FALSE()));
			DOUBLE.addBinaryOperator(INT, Operator.NOT_EQUAL, new PBinaryOperatorOverload<>((left, right) -> left.getDouble() != right.getInt() ? TRUE() : FALSE()));
			DOUBLE.addBinaryOperator(INT, Operator.LESS_THAN, new PBinaryOperatorOverload<>((left, right) -> left.get() < right.get() ? TRUE() : FALSE()));
			DOUBLE.addBinaryOperator(INT, Operator.GREATER_THAN, new PBinaryOperatorOverload<>((left, right) -> left.get() > right.get() ? TRUE() : FALSE()));
			DOUBLE.addBinaryOperator(INT, Operator.LESS_THAN_EQUAL, new PBinaryOperatorOverload<>((left, right) -> left.get() <= right.get() ? TRUE() : FALSE()));
			DOUBLE.addBinaryOperator(INT, Operator.GREATER_THAN_EQUAL, new PBinaryOperatorOverload<>((left, right) -> left.get() >= right.get() ? TRUE() : FALSE()));

			DOUBLE.addUnaryOperator(Operator.ADD, new PUnaryOperatorOverload<>(right -> newPrimitive(DOUBLE, +right.get())));
			DOUBLE.addUnaryOperator(Operator.SUB, new PUnaryOperatorOverload<>(right -> newPrimitive(DOUBLE, -right.get())));
		}

		/*
		 * Int
		 */

		INT.addConstructor(FunctionParameters.create(INT, DOUBLE), new Constructor(args -> newPrimitive(INT, (int) args[0].asPrimitive().getDouble())));
		INT.addConstructor(FunctionParameters.create(INT, STRING), new Constructor(args -> newPrimitive(INT, Integer.parseInt(args[0].asPrimitive().getString()))));
		INT.addConstructor(FunctionParameters.create(INT, INT), new Constructor(args -> newPrimitive(INT, args[0].asPrimitive().getInt())));
		INT.addConstructor(FunctionParameters.create(INT), new Constructor(args -> newPrimitive(INT, 0)));

		INT.addBinaryOperator(INT, Operator.ADD, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(INT, left.get() + right.get())));

		INT.addBinaryOperator(INT, Operator.SUB, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(INT, left.get() - right.get())));
		INT.addBinaryOperator(INT, Operator.MUL, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(INT, left.get() * right.get())));
		INT.addBinaryOperator(INT, Operator.DIV, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(INT, left.get() / right.get())));
		INT.addBinaryOperator(INT, Operator.MOD, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(INT, left.get() % right.get())));

		INT.addBinaryOperator(DOUBLE, Operator.ADD, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(DOUBLE, left.get() + right.get())));
		INT.addBinaryOperator(DOUBLE, Operator.SUB, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(DOUBLE, left.get() - right.get())));
		INT.addBinaryOperator(DOUBLE, Operator.MUL, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(DOUBLE, left.get() * right.get())));
		INT.addBinaryOperator(DOUBLE, Operator.DIV, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(DOUBLE, left.get() / right.get())));
		INT.addBinaryOperator(DOUBLE, Operator.MOD, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(DOUBLE, left.get() % right.get())));


		INT.addBinaryOperator(INT, Operator.ASSIGN_ADD, new PBinaryOperatorOverload<>((itself, right) -> itself.set(itself.get() + right.get())));
		INT.addBinaryOperator(INT, Operator.ASSIGN_SUB, new PBinaryOperatorOverload<>((itself, right) -> itself.set(itself.get() - right.get())));
		INT.addBinaryOperator(INT, Operator.ASSIGN_MUL, new PBinaryOperatorOverload<>((itself, right) -> itself.set(itself.get() * right.get())));
		INT.addBinaryOperator(INT, Operator.ASSIGN_DIV, new PBinaryOperatorOverload<>((itself, right) -> itself.set(itself.get() / right.get())));
		INT.addBinaryOperator(INT, Operator.ASSIGN_MOD, new PBinaryOperatorOverload<>((itself, right) -> itself.set(itself.get() % right.get())));

		INT.addBinaryOperator(INT, Operator.ASSIGN_BIT_AND, new PBinaryOperatorOverload<>((itself, right) -> itself.set(itself.get() & right.get())));
		INT.addBinaryOperator(INT, Operator.ASSIGN_BIT_OR, new PBinaryOperatorOverload<>((itself, right) -> itself.set(itself.get() | right.get())));
		INT.addBinaryOperator(INT, Operator.ASSIGN_BIT_XOR, new PBinaryOperatorOverload<>((itself, right) -> itself.set(itself.get() ^ right.get())));

		INT.addBinaryOperator(DOUBLE, Operator.ASSIGN_ADD, new PBinaryOperatorOverload<>((itself, right) -> itself.set((int) (itself.get() + right.get()))));
		INT.addBinaryOperator(DOUBLE, Operator.ASSIGN_SUB, new PBinaryOperatorOverload<>((itself, right) -> itself.set((int) (itself.get() - right.get()))));
		INT.addBinaryOperator(DOUBLE, Operator.ASSIGN_MUL, new PBinaryOperatorOverload<>((itself, right) -> itself.set((int) (itself.get() * right.get()))));
		INT.addBinaryOperator(DOUBLE, Operator.ASSIGN_DIV, new PBinaryOperatorOverload<>((itself, right) -> itself.set((int) (itself.get() / right.get()))));
		INT.addBinaryOperator(DOUBLE, Operator.ASSIGN_MOD, new PBinaryOperatorOverload<>((itself, right) -> itself.set((int) (itself.get() % right.get()))));

		INT.addBinaryOperator(INT, Operator.EQUAL, new PBinaryOperatorOverload<>((left, right) -> left.getInt() == right.getInt() ? TRUE() : FALSE()));
		INT.addBinaryOperator(INT, Operator.NOT_EQUAL, new PBinaryOperatorOverload<>((left, right) -> left.getInt() != right.getInt() ? TRUE() : FALSE()));
		INT.addBinaryOperator(INT, Operator.LESS_THAN, new PBinaryOperatorOverload<>((left, right) -> left.get() < right.get() ? TRUE() : FALSE()));
		INT.addBinaryOperator(INT, Operator.GREATER_THAN, new PBinaryOperatorOverload<>((left, right) -> left.get() > right.get() ? TRUE() : FALSE()));
		INT.addBinaryOperator(INT, Operator.LESS_THAN_EQUAL, new PBinaryOperatorOverload<>((left, right) -> left.get() <= right.get() ? TRUE() : FALSE()));
		INT.addBinaryOperator(INT, Operator.GREATER_THAN_EQUAL, new PBinaryOperatorOverload<>((left, right) -> left.get() >= right.get() ? TRUE() : FALSE()));

		INT.addBinaryOperator(DOUBLE, Operator.EQUAL, new PBinaryOperatorOverload<>((left, right) -> left.getInt() == right.getDouble() ? TRUE() : FALSE()));
		INT.addBinaryOperator(DOUBLE, Operator.NOT_EQUAL, new PBinaryOperatorOverload<>((left, right) -> left.getInt() != right.getDouble() ? TRUE() : FALSE()));
		INT.addBinaryOperator(DOUBLE, Operator.LESS_THAN, new PBinaryOperatorOverload<>((left, right) -> left.get() < right.get() ? TRUE() : FALSE()));
		INT.addBinaryOperator(DOUBLE, Operator.GREATER_THAN, new PBinaryOperatorOverload<>((left, right) -> left.get() > right.get() ? TRUE() : FALSE()));
		INT.addBinaryOperator(DOUBLE, Operator.LESS_THAN_EQUAL, new PBinaryOperatorOverload<>((left, right) -> left.get() <= right.get() ? TRUE() : FALSE()));
		INT.addBinaryOperator(DOUBLE, Operator.GREATER_THAN_EQUAL, new PBinaryOperatorOverload<>((left, right) -> left.get() >= right.get() ? TRUE() : FALSE()));

		INT.addBinaryOperator(INT, Operator.BIT_OR, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(INT, left.get() | right.get())));
		INT.addBinaryOperator(INT, Operator.BIT_AND, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(INT, left.get() & right.get())));
		INT.addBinaryOperator(INT, Operator.BIT_XOR, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(INT, left.get() ^ right.get())));
		INT.addBinaryOperator(INT, Operator.LSH, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(INT, left.get() << right.get())));
		INT.addBinaryOperator(INT, Operator.RSH, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(INT, left.get() >> right.get())));

		INT.addUnaryOperator(Operator.ADD, new PUnaryOperatorOverload<>(right -> newPrimitive(INT, +right.get())));
		INT.addUnaryOperator(Operator.SUB, new PUnaryOperatorOverload<>(right -> newPrimitive(INT, -right.get())));
		INT.addUnaryOperator(Operator.PRE_INC, new PUnaryOperatorOverload<>(right -> right.set(right.get() + 1)));
		INT.addUnaryOperator(Operator.PRE_DEC, new PUnaryOperatorOverload<>(right -> right.set(right.get() - 1)));
		INT.addUnaryOperator(Operator.NEG, new PUnaryOperatorOverload<>(right -> newPrimitive(INT, ~right.get())));

		/*
		 * Bool
		 */

		BOOL.addConstructor(FunctionParameters.create(BOOL, STRING), new Constructor(args -> newPrimitive(BOOL, Boolean.parseBoolean(args[0].asPrimitive().getString()))));
		BOOL.addConstructor(FunctionParameters.create(BOOL, INT), new Constructor(args -> newPrimitive(BOOL, args[0].asPrimitive().getInt() > 0)));
		BOOL.addConstructor(FunctionParameters.create(BOOL), new Constructor(args -> newPrimitive(BOOL, false)));

		BOOL.addBinaryOperator(BOOL, Operator.EQUAL, new PBinaryOperatorOverload<>((left, right) -> left.get() == right.get() ? TRUE() : FALSE()));
		BOOL.addBinaryOperator(BOOL, Operator.NOT_EQUAL, new PBinaryOperatorOverload<>((left, right) -> left.get() != right.get() ? TRUE() : FALSE()));
		BOOL.addBinaryOperator(BOOL, Operator.OR, new PBinaryOperatorOverload<>((left, right) -> left.get() || right.get() ? TRUE() : FALSE()));
		BOOL.addBinaryOperator(BOOL, Operator.AND, new PBinaryOperatorOverload<>((left, right) -> left.get() && right.get() ? TRUE() : FALSE()));

		BOOL.addUnaryOperator(Operator.NOT, new PUnaryOperatorOverload<>(right -> right.get() ? FALSE() : TRUE()));

		/*
		 * Char
		 */

		CHAR.addConstructor(FunctionParameters.create(CHAR, CHAR), new Constructor(args -> newPrimitive(CHAR, args[0].asPrimitive().getChar())));
		CHAR.addConstructor(FunctionParameters.create(CHAR), new Constructor(args -> newPrimitive(CHAR, Character.MIN_VALUE)));

		CHAR.addBinaryOperator(CHAR, Operator.EQUAL, new PBinaryOperatorOverload<>((left, right) -> left.get() == right.get() ? TRUE() : FALSE()));
		CHAR.addBinaryOperator(CHAR, Operator.NOT_EQUAL, new PBinaryOperatorOverload<>((left, right) -> left.get() != right.get() ? TRUE() : FALSE()));
		CHAR.addBinaryOperator(CHAR, Operator.ADD, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(STRING, left.get() + "" + right.get())));

		/*
		 * String
		 */

		STRING.addConstructor(FunctionParameters.create(STRING, STRING), new Constructor(args -> newPrimitive(STRING, args[0].asPrimitive().getString())));
		STRING.addConstructor(FunctionParameters.create(STRING, DOUBLE), new Constructor(args -> newPrimitive(STRING, String.valueOf(args[0].asPrimitive().getDouble()))));
		STRING.addConstructor(FunctionParameters.create(STRING, INT), new Constructor(args -> newPrimitive(STRING, String.valueOf(args[0].asPrimitive().getInt()))));
		STRING.addConstructor(FunctionParameters.create(STRING, BOOL), new Constructor(args -> newPrimitive(STRING, String.valueOf(args[0].asPrimitive().getBoolean()))));
		STRING.addConstructor(FunctionParameters.create(STRING), new Constructor(args -> newPrimitive(STRING, "")));

		STRING.addBinaryOperator(STRING, Operator.EQUAL, new PBinaryOperatorOverload<>((left, right) -> left.get().equals(right.get()) ? TRUE() : FALSE()));
		STRING.addBinaryOperator(STRING, Operator.NOT_EQUAL, new PBinaryOperatorOverload<>((left, right) -> !left.get().equals(right.get()) ? TRUE() : FALSE()));

		STRING.addBinaryOperator(STRING, Operator.ADD, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(STRING, left.get() + right.getString())));
		STRING.addBinaryOperator(DOUBLE, Operator.ADD, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(STRING, left.get() + right.getDouble())));
		STRING.addBinaryOperator(CHAR, Operator.ADD, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(STRING, left.get() + right.getChar())));
		STRING.addBinaryOperator(BOOL, Operator.ADD, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(STRING, left.get() + right.getBoolean())));
		STRING.addBinaryOperator(INT, Operator.ADD, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(STRING, left.get() + right.getInt())));
		STRING.addBinaryOperator(INT, Operator.SUB, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(STRING, left.get().substring(0, left.getString().length() - right.getInt())))); // remove n characters from back
		STRING.addBinaryOperator(INT, Operator.MUL, new PBinaryOperatorOverload<>((left, right) -> newPrimitive(STRING, left.get().repeat(right.getInt())))); // repeat the string n times

		STRING.addBinaryOperator(STRING, Operator.ASSIGN_ADD, new PBinaryOperatorOverload<>((itself, right) -> itself.set(itself.get() + right.getString())));
		STRING.addBinaryOperator(DOUBLE, Operator.ASSIGN_ADD, new PBinaryOperatorOverload<>((itself, right) -> itself.set(itself.get() + right.getDouble())));
		STRING.addBinaryOperator(CHAR, Operator.ASSIGN_ADD, new PBinaryOperatorOverload<>((itself, right) -> itself.set(itself.get() + right.getChar())));
		STRING.addBinaryOperator(BOOL, Operator.ASSIGN_ADD, new PBinaryOperatorOverload<>((itself, right) -> itself.set(itself.get() + right.getBoolean())));
		STRING.addBinaryOperator(INT, Operator.ASSIGN_ADD, new PBinaryOperatorOverload<>((itself, right) -> itself.set(itself.get() + right.getInt())));
		STRING.addBinaryOperator(INT, Operator.ASSIGN_SUB, new PBinaryOperatorOverload<>((itself, right) -> itself.set(itself.get().substring(0, itself.getString().length() - right.getInt())))); // remove n characters from back
		STRING.addBinaryOperator(INT, Operator.ASSIGN_MUL, new PBinaryOperatorOverload<>((itself, right) -> itself.set(itself.get().repeat(right.getInt())))); // repeat the string n times

		STRING.addUnaryOperator(Operator.SUB, new PUnaryOperatorOverload<>(right -> right.set(new StringBuilder(right.get()).reverse().toString()))); // reverse the string
		STRING.addFunction(FunctionParameters.function("len").build(), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				return Result.value(newPrimitive(INT, returnThisHelper.asPrimitive().getString().length()));
			}
		});
		STRING.addFunction(FunctionParameters.function("print").build(), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				System.out.println(returnThisHelper.asPrimitive().getString());
				return Result.value(returnThisHelper);
			}
		});
		STRING.addFunction(FunctionParameters.create("charAt", INT), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				return Result.value(newPrimitive(CHAR, returnThisHelper.asPrimitive().getString().charAt(arguments[0].asPrimitive().getInt())));
			}
		});
		STRING.addFunction(FunctionParameters.create("setChar", INT, CHAR), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				StringBuilder builder = new StringBuilder(returnThisHelper.asPrimitive().getString());
				builder.setCharAt(arguments[0].asPrimitive().getInt(), arguments[1].asPrimitive().getChar());
				return Result.value(newPrimitive(STRING, builder.toString()));
			}
		});
		STRING.addFunction(FunctionParameters.create("substring", INT), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				return Result.value(newPrimitive(STRING, returnThisHelper.asPrimitive().getString().substring(arguments[0].asPrimitive().getInt())));
			}
		});
		STRING.addFunction(FunctionParameters.create("substring", INT, INT), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				return Result.value(newPrimitive(STRING, returnThisHelper.asPrimitive().getString().substring(arguments[0].asPrimitive().getInt(), arguments[1].asPrimitive().getInt())));
			}
		});
	}
}
