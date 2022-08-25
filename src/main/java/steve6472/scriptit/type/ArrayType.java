package steve6472.scriptit.type;

import steve6472.scriptit.expressions.FunctionParameters;
import steve6472.scriptit.tokenizer.Operator;
import steve6472.scriptit.value.DoubleValue;
import steve6472.scriptit.value.Value;

import java.util.List;

/**
 * Created by steve6472
 * Date: 8/22/2022
 * Project: ScriptIt
 */
public class ArrayType extends TypesInit
{
	public static final PrimitiveType<DoubleValue<List<Value>, Type>> ARRAY = new PrimitiveType<>("ara ara-y", () -> {throw new RuntimeException("Can not create unitialized value for array! (No type specified)");});

	static
	{
		ARRAY.addConstructor(FunctionParameters.constructor(ARRAY).build(), new Constructor((c) -> {throw new RuntimeException("no array constructor for you");}));

		ARRAY.addBinaryOperator(INT, Operator.INDEX, new PBinaryOperatorOverload<>((arr, i) -> arr.getFirst().get(i.getInt())));

		/*
		addFunction(ARRAY, "getType", (itself) ->
		{
			// return new type -> PrimitiveType<SingleValue<Type>> TYPE
			// TYPE.constructor(STRING, text -> memory.findType(text));
		});*/
	}
}
