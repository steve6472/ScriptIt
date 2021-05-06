package steve6472.scriptit;

import steve6472.scriptit.expression.FunctionParameters;
import steve6472.scriptit.expression.Operator;
import steve6472.scriptit.expression.Type;
import steve6472.scriptit.expression.Value;

import static steve6472.scriptit.expression.Value.newValue;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/1/2021
 * Project: ScriptIt
 *
 ***********************/
public class TypeDeclarations
{
	public static final Type DOUBLE = new Type("double");
	public static final Type INT = new Type("int");
	public static final Type STRING = new Type("string");
	public static final Type CHAR = new Type("char");
	public static final Type BOOL = new Type("bool");
	public static final Type VEC2 = new Type("vec2");

	public static final Type[] BASIC_TYPES = {INT, DOUBLE, CHAR, BOOL, STRING, VEC2};

	private static double d(Object o)
	{
		return ((Value) o).getDouble();
	}

	private static String s(Object o)
	{
		return ((Value) o).getString();
	}

	private static char c(Object o)
	{
		return ((Value) o).getChar();
	}

	private static int i(Object o)
	{
		return ((Value) o).getInt();
	}

	private static boolean b(Object o)
	{
		return ((Value) o).getBoolean();
	}

	public static boolean isPrimitive(Type type)
	{
		return type == DOUBLE || type == INT || type == STRING || type == CHAR || type == BOOL;
	}

	static
	{
		DOUBLE.addConstructor(FunctionParameters.constructor(DOUBLE).addType(DOUBLE).build(), par -> newValue(DOUBLE, par[0].getDouble()));
		DOUBLE.addConstructor(FunctionParameters.constructor(DOUBLE).addType(INT).build(), par -> newValue(DOUBLE, par[0].getDouble()));
		DOUBLE.addConstructor(FunctionParameters.constructor(DOUBLE).build(), par -> newValue(DOUBLE, 0));

		DOUBLE.addBinaryOperator(DOUBLE, Operator.ADD, (left, right) -> newValue(DOUBLE, d(left) + d(right)));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.SUB, (left, right) -> newValue(DOUBLE, d(left) - d(right)));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.MUL, (left, right) -> newValue(DOUBLE, d(left) * d(right)));
		DOUBLE.addBinaryOperator(DOUBLE, Operator.DIV, (left, right) -> newValue(DOUBLE, d(left) / d(right)));
		DOUBLE.addUnaryOperator(Operator.SUB, (itself -> itself.setValue(-itself.getDouble())));
		DOUBLE.addUnaryOperator(Operator.ADD, (itself -> itself));
		DOUBLE.addProcedure(FunctionParameters.function("print").build(), (itself, args) -> System.out.println("double[val=" + d(itself) + "]"));
		DOUBLE.addFunction(FunctionParameters.function("toString").build(), (itself, args) -> newValue(STRING, itself.getDouble()));

		BOOL.addConstructor(FunctionParameters.constructor(BOOL).addType(STRING).build(), par -> newValue(BOOL, Boolean.parseBoolean(par[0].getString())));
		BOOL.addConstructor(FunctionParameters.constructor(BOOL).addType(INT).build(), par -> newValue(BOOL, par[0].getInt() > 0));
		BOOL.addConstructor(FunctionParameters.constructor(BOOL).build(), par -> newValue(BOOL, false));

//		BOOL.addAddFunction(DOUBLE, (left, right) -> new Value(DOUBLE, d(left) + d(right)));
//		BOOL.addSubFunction(DOUBLE, (left, right) -> new Value(DOUBLE, d(left) - d(right)));
//		BOOL.addMulFunction(DOUBLE, (left, right) -> new Value(DOUBLE, d(left) * d(right)));
//		BOOL.addDivFunction(DOUBLE, (left, right) -> new Value(DOUBLE, d(left) / d(right)));
//		BOOL.addPowFunction(DOUBLE, (left, right) -> new Value(DOUBLE, Math.pow(d(left), d(right))));
//		BOOL.unarySubFunctions.put(DOUBLE, (itself -> new Value(DOUBLE, -d(itself))));
//		BOOL.unaryAddFunctions.put(DOUBLE, (itself -> new Value(DOUBLE, d(itself))));
		BOOL.addUnaryOperator(Operator.NOT, (itself -> itself.setValue(itself.getBoolean())));
		BOOL.addProcedure(FunctionParameters.function("print").build(), (itself, args) -> System.out.println("bool[val=" + b(itself) + "]"));


		INT.addConstructor(FunctionParameters.constructor(INT).addType(INT).build(), par -> newValue(INT, par[0].getInt()));
		INT.addConstructor(FunctionParameters.constructor(INT).addType(DOUBLE).build(), par -> newValue(INT, (int) par[0].getDouble()));
		INT.addConstructor(FunctionParameters.constructor(INT).build(), par -> newValue(INT, 0));

		INT.addBinaryOperator(INT, Operator.ADD, (left, right) -> newValue(INT, i(left) + i(right)));
		INT.addBinaryOperator(INT, Operator.SUB, (left, right) -> newValue(INT, i(left) - i(right)));
		INT.addBinaryOperator(INT, Operator.MUL, (left, right) -> newValue(INT, i(left) * i(right)));
		INT.addBinaryOperator(INT, Operator.DIV, (left, right) -> newValue(INT, i(left) / i(right)));

		INT.addBinaryOperator(DOUBLE, Operator.ADD, (left, right) -> newValue(DOUBLE, i(left) + d(right)));
		INT.addBinaryOperator(DOUBLE, Operator.SUB, (left, right) -> newValue(DOUBLE, i(left) - d(right)));
		INT.addBinaryOperator(DOUBLE, Operator.MUL, (left, right) -> newValue(DOUBLE, i(left) * d(right)));
		INT.addBinaryOperator(DOUBLE, Operator.DIV, (left, right) -> newValue(DOUBLE, i(left) / d(right)));

		INT.addUnaryOperator(Operator.SUB, (itself -> itself.setValue(-itself.getInt())));
		INT.addUnaryOperator(Operator.ADD, (itself -> itself));
		INT.addUnaryOperator(Operator.NEG, (itself -> itself.setValue(~itself.getInt())));

		INT.addProcedure(FunctionParameters.function("print").build(), (itself, args) -> System.out.println("int[val=" + i(itself) + "]"));

		STRING.addConstructor(FunctionParameters.constructor(STRING).addType(STRING).build(), par -> newValue(STRING, par[0].getString()));
		STRING.addConstructor(FunctionParameters.constructor(STRING).addType(DOUBLE).build(), par -> newValue(STRING, String.valueOf(par[0].getDouble())));
		STRING.addConstructor(FunctionParameters.constructor(STRING).build(), par -> newValue(STRING, ""));

		STRING.addBinaryOperator(STRING, Operator.ADD, (left, right) -> newValue(STRING, s(left) + s(right)));
		STRING.addBinaryOperator(CHAR, Operator.ADD, (left, right) -> newValue(STRING, s(left) + c(right)));
		STRING.addBinaryOperator(INT, Operator.SUB, (left, right) -> newValue(STRING, s(left).substring(0, s(left).length() - i(right)))); // remove n characters from back
		STRING.addBinaryOperator(INT, Operator.MUL, (left, right) -> newValue(STRING, s(left).repeat(i(right)))); // repeat the string n times
//		STRING.addDivFunction(DOUBLE, (left, right) -> new Value(DOUBLE, d(left) / d(right)));
//		STRING.addPowFunction(DOUBLE, (left, right) -> new Value(DOUBLE, Math.pow(d(left), d(right))));
		STRING.addUnaryOperator(Operator.SUB, (itself -> itself.setValue(new StringBuilder(s(itself)).reverse().toString()))); // reverse the string
//		STRING.unaryAddFunctions.put(DOUBLE, (itself -> new Value(DOUBLE, d(itself))));
//		STRING.functions.put(FunctionParameters.function("length").build(), ((itself, args) -> new Value(DOUBLE, ((String) itself.value).length())));
		STRING.addFunction(FunctionParameters.function("len").build(), ((itself, args) -> newValue(INT, itself.getString().length())));
		STRING.addProcedure(FunctionParameters.function("print").build(), ((itself, args) -> System.out.println("string[val=" + s(itself) + "]")));
		STRING.addProcedure(FunctionParameters.function("printRaw").build(), ((itself, args) -> System.out.println(s(itself))));




		CHAR.addConstructor(FunctionParameters.constructor(CHAR).addType(CHAR).build(), par -> newValue(CHAR, par[0].getChar()));
		CHAR.addConstructor(FunctionParameters.constructor(CHAR).build(), par -> newValue(CHAR, Character.MIN_VALUE));

//		CHAR.addAddFunction(STRING, (left, right) -> new Value(STRING, s(left) + s(right)));
		//		CHAR.addSubFunction(DOUBLE, (left, right) -> new Value(DOUBLE, d(left) - d(right))); // remove n characters from back
		//		CHAR.addMulFunction(DOUBLE, (left, right) -> new Value(DOUBLE, d(left) * d(right))); // repeat the string n times
		//		CHAR.addDivFunction(DOUBLE, (left, right) -> new Value(DOUBLE, d(left) / d(right)));
		//		CHAR.addPowFunction(DOUBLE, (left, right) -> new Value(DOUBLE, Math.pow(d(left), d(right))));
		//		CHAR.unarySubFunctions.put(DOUBLE, (itself -> new Value(DOUBLE, -d(itself)))); // reverse the string
		//		CHAR.unaryAddFunctions.put(DOUBLE, (itself -> new Value(DOUBLE, d(itself))));
		//		CHAR.functions.put(FunctionParameters.function("length").build(), ((itself, args) -> new Value(DOUBLE, ((String) itself.value).length())));
		CHAR.addProcedure(FunctionParameters.function("prnt").build(), (itself, args) -> System.out.println("char[val='" + c(itself) + "']"));


		VEC2.addConstructor(FunctionParameters.constructor(VEC2).addType(DOUBLE).addType(DOUBLE).build(), par -> newValue(VEC2).setValue("x", newValue(DOUBLE, par[0].getDouble())).setValue("y", newValue(DOUBLE, par[1].getDouble())));
		VEC2.addConstructor(FunctionParameters.constructor(VEC2).addType(INT).addType(INT).build(), par -> newValue(VEC2).setValue("x", newValue(DOUBLE, (double) par[0].getInt())).setValue("y", newValue(DOUBLE, (double) par[1].getInt())));
		VEC2.addConstructor(FunctionParameters.constructor(VEC2).addType(DOUBLE).build(), par -> newValue(VEC2).setValue("x", newValue(DOUBLE, par[0].getDouble())).setValue("y", newValue(DOUBLE, par[0].getDouble())));
		VEC2.addConstructor(FunctionParameters.constructor(VEC2).addType(VEC2).build(), par -> newValue(VEC2).setValue("x", par[0].getValue("x").getDouble()).setValue("y", par[0].getValue("y").getDouble()));
		VEC2.addConstructor(FunctionParameters.constructor(VEC2).build(), par -> newValue(VEC2).setValue("x", newValue(DOUBLE, 0)).setValue("y", newValue(DOUBLE, 0)));

		VEC2.addBinaryOperator(VEC2, Operator.ADD, (left, right) -> newValue(VEC2).setValue("x", left.getValue("x").getDouble() + right.getValue("x").getDouble()).setValue("y", left.getValue("y").getDouble() + right.getValue("y").getDouble()));
		VEC2.addBinaryOperator(VEC2, Operator.SUB, (left, right) -> newValue(VEC2).setValue("x", left.getValue("x").getDouble() - right.getValue("x").getDouble()).setValue("y", left.getValue("y").getDouble() - right.getValue("y").getDouble()));
		VEC2.addBinaryOperator(VEC2, Operator.MUL, (left, right) -> newValue(VEC2).setValue("x", left.getDoubleValue("x") * right.getDoubleValue("x")).setValue("y", left.getDoubleValue("y") * right.getDoubleValue("y")));
		VEC2.addBinaryOperator(VEC2, Operator.DIV, (left, right) -> newValue(VEC2).setValue("x", left.getValue("x").getDouble() / right.getValue("x").getDouble()).setValue("y", left.getValue("y").getDouble() / right.getValue("y").getDouble()));

		VEC2.addBinaryOperator(DOUBLE, Operator.ADD, (left, right) -> newValue(VEC2).setValue("x", newValue(DOUBLE, left.getValue("x").getDouble() + right.getDouble())).setValue("y", newValue(DOUBLE, left.getValue("y").getDouble() + right.getDouble())));
		VEC2.addBinaryOperator(DOUBLE, Operator.SUB, (left, right) -> newValue(VEC2).setValue("x", newValue(DOUBLE, left.getValue("x").getDouble() - right.getDouble())).setValue("y", newValue(DOUBLE, left.getValue("y").getDouble() - right.getDouble())));
		VEC2.addBinaryOperator(DOUBLE, Operator.MUL, (left, right) -> newValue(VEC2).setValue("x", newValue(DOUBLE, left.getValue("x").getDouble() * right.getDouble())).setValue("y", newValue(DOUBLE, left.getValue("y").getDouble() * right.getDouble())));
		VEC2.addBinaryOperator(DOUBLE, Operator.DIV, (left, right) -> newValue(VEC2).setValue("x", newValue(DOUBLE, left.getValue("x").getDouble() / right.getDouble())).setValue("y", newValue(DOUBLE, left.getValue("y").getDouble() / right.getDouble())));

		VEC2.addUnaryOperator(Operator.SUB, itself -> itself.setValue("x", -itself.getValue("x").getDouble()).setValue("y", -itself.getValue("y").getDouble()));
		VEC2.addUnaryOperator(Operator.ADD, itself -> itself);

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
		VEC2.addFunction(FunctionParameters.function("toString").build(), (itself, args) -> newValue(STRING, "[x=" + itself.getValue("x").getDouble() + ",y=" + itself.getValue("y").getDouble() + "]"));
		VEC2.addProcedure(FunctionParameters.function("print").build(), (itself, args) -> System.out.println("vec2[x=" + itself.getValue("x").getDouble() + ",y=" + itself.getValue("y").getDouble() + "]"));
	}
}
