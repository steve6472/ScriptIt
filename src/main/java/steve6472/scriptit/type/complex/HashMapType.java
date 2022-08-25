package steve6472.scriptit.type.complex;

import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.expressions.Function;
import steve6472.scriptit.expressions.FunctionParameters;
import steve6472.scriptit.tokenizer.Operator;
import steve6472.scriptit.type.PrimitiveType;
import steve6472.scriptit.type.Type;
import steve6472.scriptit.type.TypesInit;
import steve6472.scriptit.value.SingleValue;
import steve6472.scriptit.value.Value;

import java.util.*;

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
		addProcedure(HASH_MAP, "put", (itself, k, v) -> itself.as(HASH_MAP).get().put(k, v), ANY_TYPE, ANY_TYPE);
		addFunction(HASH_MAP, "get", (itself, k) -> Optional.of(itself.as(HASH_MAP).get().get(k)).orElse(Value.NULL), ANY_TYPE);

		addFunction(HASH_MAP, "values", (itself) -> {
			HashMap<Value, Value> map = itself.as(HASH_MAP).get();
			Collection<Value> values = map.values();
			Optional<Value> any = values.stream().findAny();
			if (any.isPresent())
			{
				Type type = any.get().type;
				return type.createArray(new ArrayList<>(values));
			} else
			{
				return Value.NULL;
			}
		});

		addFunction(HASH_MAP, "keySet", (itself) -> {
			HashMap<Value, Value> map = itself.as(HASH_MAP).get();
			Collection<Value> values = map.keySet();
			Optional<Value> any = values.stream().findAny();
			if (any.isPresent())
			{
				Type type = any.get().type;
				return type.createArray(new ArrayList<>(values));
			} else
			{
				return Value.NULL;
			}
		});

		HASH_MAP.addBinaryOperator(ANY_TYPE, Operator.INDEX, new PBinaryOperatorOverload<>((itself, right) -> itself.get().get(right)));
	}
}
