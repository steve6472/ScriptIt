package steve6472.scriptit.types;

import steve6472.scriptit.FunctionParameters;
import steve6472.scriptit.Type;
import steve6472.scriptit.Value;
import steve6472.scriptit.tokenizer.Operator;

import java.util.ArrayList;
import java.util.List;

import static steve6472.scriptit.Value.newValue;
import static steve6472.scriptit.types.PrimitiveTypes.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/23/2021
 * Project: ScriptIt
 *
 ***********************/
public class CustomTypes extends TypesInit
{
	public static final Type VEC2 = new Type("vec2");
	public static final Type VEC4 = new Type("vec4");
	public static final Type COLOR = new Type("Color");
	public static final Type LIST = new Type("List");

	public static void init()
	{
		addFunction(VEC4, "x", itself -> newValue(DOUBLE, itself.getDoubleValue("x")));
		addFunction(VEC4, "y", itself -> newValue(DOUBLE, itself.getDoubleValue("y")));
		addFunction(VEC4, "z", itself -> newValue(DOUBLE, itself.getDoubleValue("z")));
		addFunction(VEC4, "w", itself -> newValue(DOUBLE, itself.getDoubleValue("w")));
		/*VEC2.addConstructor(FunctionParameters
			.constructor(VEC2).addType(DOUBLE).addType(DOUBLE).build(), new TypesInit.Constructor((args) -> newValue(VEC2).setValue("x", args[0]).setValue("y", args[1])));
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
*/


		COLOR.addConstructor(FunctionParameters.create(COLOR), new Constructor((args) -> newValue(COLOR).setValue("r", newValue(INT, 0)).setValue("g", newValue(INT, 0)).setValue("b", newValue(INT, 0))));
		COLOR.addConstructor(FunctionParameters.create(COLOR, INT, INT, INT), new Constructor((args) -> newValue(COLOR).setValue("r", newValue(INT, args[0].getInt())).setValue("g", newValue(INT, args[1].getInt())).setValue("b", newValue(INT, args[2].getInt()))));

		COLOR.addBinaryOperator(COLOR, Operator.ADD, new BinaryOperatorOverload((left, right) -> newValue(COLOR)
			.setValue("r", newValue(INT, Math.min(255, left.getIntValue("r") + right.getIntValue("r"))))
			.setValue("g", newValue(INT, Math.min(255, left.getIntValue("g") + right.getIntValue("g"))))
			.setValue("b", newValue(INT, Math.min(255, left.getIntValue("b") + right.getIntValue("b"))))));

		COLOR.addBinaryOperator(COLOR, Operator.SUB, new BinaryOperatorOverload((left, right) -> newValue(COLOR)
			.setValue("r", newValue(INT, Math.max(0, left.getIntValue("r") - right.getIntValue("r"))))
			.setValue("g", newValue(INT, Math.max(0, left.getIntValue("g") - right.getIntValue("g"))))
			.setValue("b", newValue(INT, Math.max(0, left.getIntValue("b") - right.getIntValue("b"))))));

		addFunction(COLOR, "getRed", itself -> newValue(INT, itself.getIntValue("r")));
		addFunction(COLOR, "getGreen", itself -> newValue(INT, itself.getIntValue("g")));
		addFunction(COLOR, "getBlue", itself -> newValue(INT, itself.getIntValue("b")));

		addFunction(COLOR, "r", itself -> newValue(INT, itself.getIntValue("r")));
		addFunction(COLOR, "g", itself -> newValue(INT, itself.getIntValue("g")));
		addFunction(COLOR, "b", itself -> newValue(INT, itself.getIntValue("b")));

		LIST.addConstructor(FunctionParameters.create(LIST), new Constructor((args) -> newValue(LIST, new ArrayList<Value>())));
		LIST.addConstructor(FunctionParameters.create(LIST, INT), new Constructor((args) -> newValue(LIST, new ArrayList<Value>(args[0].getInt()))));

		addProcedure(LIST, "add", (itself, value) -> ((List<Value>) itself.get()).add(value), NULL);
		addProcedure(LIST, "set", (itself, index, value) -> ((List<Value>) itself.get()).set(index.getInt(), value), INT, NULL);
		addFunction(LIST, "get", (itself, index) -> ((List<Value>) itself.get()).get(index.getInt()), INT);
		addFunction(LIST, "remove", (itself, index) -> ((List<Value>) itself.get()).remove(index.getInt()), INT);
		addFunction(LIST, "size", itself -> newValue(INT, ((List<Value>) itself.get()).size()));

		STRING.addBinaryOperator(COLOR, Operator.ADD, new BinaryOperatorOverload((left, right) -> newValue(STRING, left.getString() + right.toString())));
	}

	public static Value createColor(int r, int g, int b)
	{
		return newValue(COLOR).setValue("r", r).setValue("g", g).setValue("b", b);
	}

	public static Value createVec4(double x, double y, double z, double w)
	{
		return newValue(VEC4).setValue("x", newValue(DOUBLE, x)).setValue("y", newValue(DOUBLE, y)).setValue("z", newValue(DOUBLE, z)).setValue("w", newValue(DOUBLE, w));
	}
}
