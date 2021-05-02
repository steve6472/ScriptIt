package steve6472.scriptit;

import steve6472.scriptit.expression.FunctionParameters;
import steve6472.scriptit.expression.Type;
import steve6472.scriptit.expression.Value;

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

	static
	{
		DOUBLE.addConstructor(FunctionParameters.constructor(DOUBLE).addType(DOUBLE).build(), par -> new Value(DOUBLE, par[0].getDouble()));
		DOUBLE.addConstructor(FunctionParameters.constructor(DOUBLE).addType(INT).build(), par -> new Value(DOUBLE, par[0].getDouble()));
		DOUBLE.addConstructor(FunctionParameters.constructor(DOUBLE).build(), par -> new Value(DOUBLE, 0));

		DOUBLE.addAddFunction(DOUBLE, (left, right) -> new Value(DOUBLE, d(left) + d(right)));
		DOUBLE.addSubFunction(DOUBLE, (left, right) -> new Value(DOUBLE, d(left) - d(right)));
		DOUBLE.addMulFunction(DOUBLE, (left, right) -> new Value(DOUBLE, d(left) * d(right)));
		DOUBLE.addDivFunction(DOUBLE, (left, right) -> new Value(DOUBLE, d(left) / d(right)));
		DOUBLE.addPowFunction(DOUBLE, (left, right) -> new Value(DOUBLE, Math.pow(d(left), d(right))));
		DOUBLE.unarySubFunctions.put(DOUBLE, (itself -> new Value(DOUBLE, -d(itself))));
		DOUBLE.unaryAddFunctions.put(DOUBLE, (itself -> new Value(DOUBLE, d(itself))));
		DOUBLE.addProcedure(FunctionParameters.function("print").build(), (itself, args) -> System.out.println("double[val=" + d(itself) + "]"));

		BOOL.addConstructor(FunctionParameters.constructor(BOOL).addType(STRING).build(), par -> new Value(BOOL, Boolean.parseBoolean(par[0].getString())));
		BOOL.addConstructor(FunctionParameters.constructor(BOOL).addType(INT).build(), par -> new Value(BOOL, par[0].getInt() > 0));
		BOOL.addConstructor(FunctionParameters.constructor(BOOL).build(), par -> new Value(BOOL, false));

//		BOOL.addAddFunction(DOUBLE, (left, right) -> new Value(DOUBLE, d(left) + d(right)));
//		BOOL.addSubFunction(DOUBLE, (left, right) -> new Value(DOUBLE, d(left) - d(right)));
//		BOOL.addMulFunction(DOUBLE, (left, right) -> new Value(DOUBLE, d(left) * d(right)));
//		BOOL.addDivFunction(DOUBLE, (left, right) -> new Value(DOUBLE, d(left) / d(right)));
//		BOOL.addPowFunction(DOUBLE, (left, right) -> new Value(DOUBLE, Math.pow(d(left), d(right))));
//		BOOL.unarySubFunctions.put(DOUBLE, (itself -> new Value(DOUBLE, -d(itself))));
//		BOOL.unaryAddFunctions.put(DOUBLE, (itself -> new Value(DOUBLE, d(itself))));
		BOOL.unaryNegFunctions.put(BOOL, (itself -> new Value(BOOL, !b(itself))));
		BOOL.unaryNotFunctions.put(BOOL, (itself -> new Value(BOOL, !b(itself))));
		BOOL.addProcedure(FunctionParameters.function("print").build(), (itself, args) -> System.out.println("bool[val=" + b(itself) + "]"));


		INT.addConstructor(FunctionParameters.constructor(INT).addType(INT).build(), par -> new Value(INT, par[0].getInt()));
		INT.addConstructor(FunctionParameters.constructor(INT).build(), par -> new Value(INT, 0));

		INT.addAddFunction(INT, (left, right) -> new Value(INT, i(left) + i(right)));
		INT.addSubFunction(INT, (left, right) -> new Value(INT, i(left) - i(right)));
		INT.addMulFunction(INT, (left, right) -> new Value(INT, i(left) * i(right)));
		INT.addDivFunction(INT, (left, right) -> new Value(INT, i(left) / i(right)));

		INT.addAddFunction(DOUBLE, (left, right) -> new Value(DOUBLE, i(left) + d(right)));
		INT.addSubFunction(DOUBLE, (left, right) -> new Value(DOUBLE, i(left) - d(right)));
		INT.addMulFunction(DOUBLE, (left, right) -> new Value(DOUBLE, i(left) * d(right)));
		INT.addDivFunction(DOUBLE, (left, right) -> new Value(DOUBLE, i(left) / d(right)));

		INT.addPowFunction(INT, (left, right) -> new Value(INT, (int) Math.pow(i(left), i(right))));
		INT.unarySubFunctions.put(INT, (itself -> new Value(INT, -i(itself))));
		INT.unaryAddFunctions.put(INT, (itself -> new Value(INT, i(itself))));
		INT.unaryNegFunctions.put(INT, (itself -> new Value(INT, ~i(itself))));
		INT.addProcedure(FunctionParameters.function("print").build(), (itself, args) -> System.out.println("int[val=" + i(itself) + "]"));

		STRING.addConstructor(FunctionParameters.constructor(STRING).addType(STRING).build(), par -> new Value(STRING, par[0].getString()));
		STRING.addConstructor(FunctionParameters.constructor(STRING).build(), par -> new Value(STRING, ""));

		STRING.addAddFunction(STRING, (left, right) -> new Value(STRING, s(left) + s(right)));
		STRING.addAddFunction(CHAR, (left, right) -> new Value(STRING, s(left) + c(right)));
		STRING.addSubFunction(INT, (left, right) -> new Value(STRING, s(left).substring(0, s(left).length() - i(right)))); // remove n characters from back
		STRING.addMulFunction(INT, (left, right) -> new Value(STRING, s(left).repeat(i(right)))); // repeat the string n times
//		STRING.addDivFunction(DOUBLE, (left, right) -> new Value(DOUBLE, d(left) / d(right)));
//		STRING.addPowFunction(DOUBLE, (left, right) -> new Value(DOUBLE, Math.pow(d(left), d(right))));
		STRING.unarySubFunctions.put(STRING, (itself -> new Value(STRING, new StringBuilder(s(itself)).reverse().toString()))); // reverse the string
//		STRING.unaryAddFunctions.put(DOUBLE, (itself -> new Value(DOUBLE, d(itself))));
//		STRING.functions.put(FunctionParameters.function("length").build(), ((itself, args) -> new Value(DOUBLE, ((String) itself.value).length())));
		STRING.addFunction(FunctionParameters.function("len").build(), ((itself, args) -> new Value(INT, itself.getString().length())));
		STRING.addProcedure(FunctionParameters.function("print").build(), ((itself, args) -> System.out.println("string[val=" + s(itself) + "]")));




		CHAR.addConstructor(FunctionParameters.constructor(CHAR).addType(CHAR).build(), par -> new Value(CHAR, par[0].getChar()));
		CHAR.addConstructor(FunctionParameters.constructor(CHAR).build(), par -> new Value(CHAR, Character.MIN_VALUE));

//		CHAR.addAddFunction(STRING, (left, right) -> new Value(STRING, s(left) + s(right)));
		//		CHAR.addSubFunction(DOUBLE, (left, right) -> new Value(DOUBLE, d(left) - d(right))); // remove n characters from back
		//		CHAR.addMulFunction(DOUBLE, (left, right) -> new Value(DOUBLE, d(left) * d(right))); // repeat the string n times
		//		CHAR.addDivFunction(DOUBLE, (left, right) -> new Value(DOUBLE, d(left) / d(right)));
		//		CHAR.addPowFunction(DOUBLE, (left, right) -> new Value(DOUBLE, Math.pow(d(left), d(right))));
		//		CHAR.unarySubFunctions.put(DOUBLE, (itself -> new Value(DOUBLE, -d(itself)))); // reverse the string
		//		CHAR.unaryAddFunctions.put(DOUBLE, (itself -> new Value(DOUBLE, d(itself))));
		//		CHAR.functions.put(FunctionParameters.function("length").build(), ((itself, args) -> new Value(DOUBLE, ((String) itself.value).length())));
		CHAR.addProcedure(FunctionParameters.function("prnt").build(), (itself, args) -> System.out.println("char[val='" + c(itself) + "']"));


		VEC2.addConstructor(FunctionParameters.constructor(VEC2).addType(DOUBLE).addType(DOUBLE).build(), par -> new Value(VEC2).setValue("x", par[0].getDouble()).setValue("y", par[1].getDouble()));
		VEC2.addConstructor(FunctionParameters.constructor(VEC2).addType(INT).addType(INT).build(), par -> new Value(VEC2).setValue("x", (double) par[0].getInt()).setValue("y", (double) par[1].getInt()));
		VEC2.addConstructor(FunctionParameters.constructor(VEC2).addType(DOUBLE).build(), par -> new Value(VEC2).setValue("x", par[0].getDouble()).setValue("y", par[0].getDouble()));
		VEC2.addConstructor(FunctionParameters.constructor(VEC2).addType(VEC2).build(), par ->
		{
			Value val = par[0];
			return new Value(VEC2).setValue("x", val.getDouble("x")).setValue("y", val.getDouble("y"));
		});
		VEC2.addConstructor(FunctionParameters.constructor(VEC2).build(), par -> new Value(VEC2, new double[] {0, 0}));

		VEC2.addAddFunction(VEC2, (left, right) -> new Value(VEC2).setValue("x", left.getDouble("x") + right.getDouble("x")).setValue("y", left.getDouble("y") + right.getDouble("y")));
		VEC2.addSubFunction(VEC2, (left, right) -> new Value(VEC2).setValue("x", left.getDouble("x") - right.getDouble("x")).setValue("y", left.getDouble("y") - right.getDouble("y")));
		VEC2.addMulFunction(VEC2, (left, right) -> new Value(VEC2).setValue("x", left.getDouble("x") * right.getDouble("x")).setValue("y", left.getDouble("y") * right.getDouble("y")));
		VEC2.addDivFunction(VEC2, (left, right) -> new Value(VEC2).setValue("x", left.getDouble("x") / right.getDouble("x")).setValue("y", left.getDouble("y") / right.getDouble("y")));

		VEC2.addAddFunction(DOUBLE, (left, right) -> new Value(VEC2).setValue("x", left.getDouble("x") + right.getDouble()).setValue("y", left.getDouble("y") + right.getDouble()));
		VEC2.addSubFunction(DOUBLE, (left, right) -> new Value(VEC2).setValue("x", left.getDouble("x") - right.getDouble()).setValue("y", left.getDouble("y") - right.getDouble()));
		VEC2.addMulFunction(DOUBLE, (left, right) -> new Value(VEC2).setValue("x", left.getDouble("x") * right.getDouble()).setValue("y", left.getDouble("y") * right.getDouble()));
		VEC2.addDivFunction(DOUBLE, (left, right) -> new Value(VEC2).setValue("x", left.getDouble("x") / right.getDouble()).setValue("y", left.getDouble("y") / right.getDouble()));

		VEC2.unarySubFunctions.put(VEC2, (itself -> new Value(VEC2).setValue("x", -itself.getDouble("x")).setValue("y", -itself.getDouble("y"))));
		VEC2.unaryAddFunctions.put(VEC2, (itself -> new Value(VEC2).setValue("x", itself.getDouble("x")).setValue("y", itself.getDouble("y"))));
		VEC2.addFunction(FunctionParameters.function("normalize").build(), (itself, args) -> {
			double x = itself.getDouble("x");
			double y = itself.getDouble("y");
			double len = 1.0 / Math.sqrt(x * x + y * y);

			return new Value(VEC2, new double[] {x * len, y * len});
		});
		VEC2.addFunction(FunctionParameters.function("len").build(), (itself, args) -> {
			double x = itself.getDouble("x");
			double y = itself.getDouble("y");
			double len = Math.sqrt(x * x + y * y);

			return new Value(DOUBLE, len);
		});
		VEC2.addFunction(FunctionParameters.function("toString").build(), (itself, args) -> new Value(STRING, "[x=" + itself.getDouble("x") + ",y=" + itself.getDouble("y") + "]"));
		VEC2.addProcedure(FunctionParameters.function("print").build(), (itself, args) -> System.out.println("vec2[x=" + itself.getDouble("x") + ",y=" + itself.getDouble("y") + "]"));
	}
}
