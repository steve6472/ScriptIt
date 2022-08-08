package steve6472.scriptit.type.complex;

import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.expressions.Function;
import steve6472.scriptit.expressions.FunctionParameters;
import steve6472.scriptit.tokenizer.Operator;
import steve6472.scriptit.type.PrimitiveType;
import steve6472.scriptit.type.PrimitiveTypes;
import steve6472.scriptit.type.TypesInit;
import steve6472.scriptit.value.SingleValue;
import steve6472.scriptit.value.Value;

import java.util.HashMap;

import static steve6472.scriptit.type.PrimitiveTypes.INT;
import static steve6472.scriptit.type.PrimitiveTypes.STRING;

/**
 * Created by steve6472
 * Date: 8/8/2022
 * Project: ScriptIt
 */
public class HashMapType extends TypesInit
{
	public static final PrimitiveType<SingleValue<HashMap<Value, Value>>> HASH_MAP = new PrimitiveType<>("HashMap");

	public static void init()
	{
		//TODO: add something that creates a type references instead of variables.... so I can make a type-safe hashmap
		HASH_MAP.addConstructor(FunctionParameters.constructor(HASH_MAP).build(), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				return Result.value(SingleValue.newValue(HASH_MAP, new HashMap<>()));
			}
		});

		// TODO: add wildcard
		addProcedure(HASH_MAP, "put", (itself, k, v) -> itself.as(HASH_MAP).get().put(k, v), STRING, INT);
		addFunction(HASH_MAP, "get", (itself, k) -> itself.as(HASH_MAP).get().get(k), STRING);

		HASH_MAP.addBinaryOperator(STRING, Operator.INDEX, new PBinaryOperatorOverload<>((itself, right) -> itself.get().get(right)));
	}
}
