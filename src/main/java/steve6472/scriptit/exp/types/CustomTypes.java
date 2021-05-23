package steve6472.scriptit.exp.types;

import steve6472.scriptit.exp.*;

import static steve6472.scriptit.exp.Value.newValue;
import static steve6472.scriptit.exp.types.PrimitiveTypes.DOUBLE;
import static steve6472.scriptit.exp.types.PrimitiveTypes.INT;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/23/2021
 * Project: ScriptIt
 *
 ***********************/
public class CustomTypes extends TypesInit
{
	public static final Type VEC2 = new Type("vec2");

	public static void init(Script script)
	{
		VEC2.addConstructor(FunctionParameters.constructor(VEC2).addType(DOUBLE).addType(DOUBLE).build(), new TypesInit.Constructor((args) -> newValue(VEC2).setValue("x", args[0]).setValue("y", args[1])));
		VEC2.addConstructor(FunctionParameters.constructor(VEC2).addType(INT).addType(INT).build(), new TypesInit.Constructor((args) -> newValue(VEC2).setValue("x", newValue(DOUBLE, (double) args[0].getInt())).setValue("y", newValue(DOUBLE, (double) args[1].getInt()))));
		VEC2.addConstructor(FunctionParameters.constructor(VEC2).addType(DOUBLE).build(), new TypesInit.Constructor((args) -> newValue(VEC2).setValue("x", newValue(DOUBLE, args[0].getDouble())).setValue("y", newValue(DOUBLE, args[0].getDouble()))));
		VEC2.addConstructor(FunctionParameters.constructor(VEC2).addType(VEC2).build(), new TypesInit.Constructor((args) -> newValue(VEC2).setValue("x", newValue(DOUBLE, args[0].getValue("x").getDouble())).setValue("y", newValue(DOUBLE, args[0].getValue("y").getDouble()))));
		VEC2.addConstructor(FunctionParameters.constructor(VEC2).addType(INT).build(), new TypesInit.Constructor((args) -> newValue(VEC2).setValue("x", newValue(DOUBLE, (double) args[0].getInt())).setValue("y", newValue(DOUBLE, (double) args[0].getInt()))));
		VEC2.addConstructor(FunctionParameters.constructor(VEC2).build(), new TypesInit.Constructor((args) -> newValue(VEC2).setValue("x", newValue(DOUBLE, 0.0)).setValue("y", newValue(DOUBLE, 0.0))));

		VEC2.addBinaryOperator(VEC2, Operator.ADD, new BinaryOperatorOverload((left, right) -> newValue(VEC2).setValue("x", left.getValue("x").getDouble() + right.getValue("x").getDouble()).setValue("y", left.getValue("y").getDouble() + right.getValue("y").getDouble())));
		VEC2.addBinaryOperator(VEC2, Operator.SUB, new BinaryOperatorOverload((left, right) -> newValue(VEC2).setValue("x", left.getValue("x").getDouble() - right.getValue("x").getDouble()).setValue("y", left.getValue("y").getDouble() - right.getValue("y").getDouble())));
		VEC2.addBinaryOperator(VEC2, Operator.MUL, new BinaryOperatorOverload((left, right) -> newValue(VEC2).setValue("x", left.getDoubleValue("x") * right.getDoubleValue("x")).setValue("y", left.getDoubleValue("y") * right.getDoubleValue("y"))));
		VEC2.addBinaryOperator(VEC2, Operator.DIV, new BinaryOperatorOverload((left, right) -> newValue(VEC2).setValue("x", left.getValue("x").getDouble() / right.getValue("x").getDouble()).setValue("y", left.getValue("y").getDouble() / right.getValue("y").getDouble())));

		VEC2.addBinaryOperator(DOUBLE, Operator.ADD, new BinaryOperatorOverload((left, right) -> newValue(VEC2).setValue("x", newValue(DOUBLE, left.getValue("x").getDouble() + right.getDouble())).setValue("y", newValue(DOUBLE, left.getValue("y").getDouble() + right.getDouble()))));
		VEC2.addBinaryOperator(DOUBLE, Operator.SUB, new BinaryOperatorOverload((left, right) -> newValue(VEC2).setValue("x", newValue(DOUBLE, left.getValue("x").getDouble() - right.getDouble())).setValue("y", newValue(DOUBLE, left.getValue("y").getDouble() - right.getDouble()))));
		VEC2.addBinaryOperator(DOUBLE, Operator.MUL, new BinaryOperatorOverload((left, right) -> newValue(VEC2).setValue("x", newValue(DOUBLE, left.getValue("x").getDouble() * right.getDouble())).setValue("y", newValue(DOUBLE, left.getValue("y").getDouble() * right.getDouble()))));
		VEC2.addBinaryOperator(DOUBLE, Operator.DIV, new BinaryOperatorOverload((left, right) -> newValue(VEC2).setValue("x", newValue(DOUBLE, left.getValue("x").getDouble() / right.getDouble())).setValue("y", newValue(DOUBLE, left.getValue("y").getDouble() / right.getDouble()))));

		VEC2.addUnaryOperator(Operator.SUB, new UnaryOperatorOverload(left -> left.setValue("x", -left.getValue("x").getDouble()).setValue("y", -left.getValue("y").getDouble())));
		VEC2.addUnaryOperator(Operator.ADD, new UnaryOperatorOverload(value -> value));

		Function normalizeVec2 = new Function();
		normalizeVec2.setExpressions(script, "len = 1.0 / len()", "return vec2(x * len, y * len)");
		VEC2.addFunction(FunctionParameters.function("normalize").build(), normalizeVec2);

		Function lenVec2 = new Function();
		lenVec2.setExpressions(script, "return Math.sqrt(x * x + y * y)");
		VEC2.addFunction(FunctionParameters.function("len").build(), lenVec2);

		Function returnThisTest = new Function();
		returnThisTest.setExpressions(script, "x = 0.0", "y = 0.0", "return this");
		VEC2.addFunction(FunctionParameters.function("makeZero").build(), returnThisTest);
	}
}
