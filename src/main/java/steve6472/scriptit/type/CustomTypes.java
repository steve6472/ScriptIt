package steve6472.scriptit.type;

import steve6472.scriptit.expressions.FunctionParameters;
import steve6472.scriptit.tokenizer.Operator;
import steve6472.scriptit.value.UniversalValue;
import steve6472.scriptit.value.Value;

import java.util.ArrayList;
import java.util.List;

import static steve6472.scriptit.type.PrimitiveTypes.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/23/2021
 * Project: ScriptIt
 *
 ***********************/
public class CustomTypes extends TypesInit
{
	public static final Type COLOR = new Type("Color");
	public static final Type LIST = new Type("List");

	public static void init()
	{
		COLOR.addConstructor(FunctionParameters.create(COLOR), new Constructor((args) -> newUniversal(COLOR).setValue("r", newPrimitive(INT, 0)).setValue("g", newPrimitive(INT, 0)).setValue("b", newPrimitive(INT, 0))));
		COLOR.addConstructor(FunctionParameters.create(COLOR, INT, INT, INT), new Constructor((args) -> newUniversal(COLOR).setValue("r", newPrimitive(INT, args[0].asPrimitive().getInt())).setValue("g", newPrimitive(INT, args[1].asPrimitive().getInt())).setValue("b", newPrimitive(INT, args[2].asPrimitive().getInt()))));

		COLOR.addBinaryOperator(COLOR, Operator.ADD, new BinaryOperatorOverload((left, right) -> newUniversal(COLOR)
			.setValue("r", newPrimitive(INT, Math.min(255, left.asUniversal().getIntValue("r") + right.asUniversal().getIntValue("r"))))
			.setValue("g", newPrimitive(INT, Math.min(255, left.asUniversal().getIntValue("g") + right.asUniversal().getIntValue("g"))))
			.setValue("b", newPrimitive(INT, Math.min(255, left.asUniversal().getIntValue("b") + right.asUniversal().getIntValue("b"))))));

		COLOR.addBinaryOperator(COLOR, Operator.SUB, new BinaryOperatorOverload((left, right) -> newUniversal(COLOR)
			.setValue("r", newPrimitive(INT, Math.max(0, left.asUniversal().getIntValue("r") - right.asUniversal().getIntValue("r"))))
			.setValue("g", newPrimitive(INT, Math.max(0, left.asUniversal().getIntValue("g") - right.asUniversal().getIntValue("g"))))
			.setValue("b", newPrimitive(INT, Math.max(0, left.asUniversal().getIntValue("b") - right.asUniversal().getIntValue("b"))))));

		addFunction(COLOR, "getRed", itself -> newPrimitive(INT, itself.asUniversal().getIntValue("r")));
		addFunction(COLOR, "getGreen", itself -> newPrimitive(INT, itself.asUniversal().getIntValue("g")));
		addFunction(COLOR, "getBlue", itself -> newPrimitive(INT, itself.asUniversal().getIntValue("b")));

		addFunction(COLOR, "r", itself -> newPrimitive(INT, itself.asUniversal().getIntValue("r")));
		addFunction(COLOR, "g", itself -> newPrimitive(INT, itself.asUniversal().getIntValue("g")));
		addFunction(COLOR, "b", itself -> newPrimitive(INT, itself.asUniversal().getIntValue("b")));

		LIST.addConstructor(FunctionParameters.create(LIST), new Constructor((args) -> newUniversal(LIST, new ArrayList<UniversalValue>())));
		LIST.addConstructor(FunctionParameters.create(LIST, INT), new Constructor((args) -> newUniversal(LIST, new ArrayList<UniversalValue>(args[0].asPrimitive().getInt()))));

		addProcedure(LIST, "add", (itself, value) -> ((List<Value>) itself.asUniversal().getSingle()).add(value), NULL);
		addProcedure(LIST, "set", (itself, index, value) -> ((List<Value>) itself.asUniversal().getSingle()).set(index.asPrimitive().getInt(), value), INT, NULL);
		addFunction(LIST, "get", (itself, index) -> ((List<Value>) itself.asUniversal().getSingle()).get(index.asPrimitive().getInt()), INT);
		addFunction(LIST, "remove", (itself, index) -> ((List<Value>) itself.asUniversal().getSingle()).remove(index.asPrimitive().getInt()), INT);
		addFunction(LIST, "size", itself -> newPrimitive(INT, ((List<Value>) itself.asUniversal().getSingle()).size()));

		STRING.addBinaryOperator(COLOR, Operator.ADD, new BinaryOperatorOverload((left, right) -> newPrimitive(STRING, left.asPrimitive().getString() + right.toString())));
	}

	public static UniversalValue createColor(int r, int g, int b)
	{
		return newUniversal(COLOR).setValue("r", r).setValue("g", g).setValue("b", b);
	}
}
