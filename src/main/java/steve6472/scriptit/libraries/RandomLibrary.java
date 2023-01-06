package steve6472.scriptit.libraries;

import steve6472.scriptit.type.PrimitiveTypes;
import steve6472.scriptit.value.PrimitiveValue;

import java.util.Random;


/**********************
 * Created by steve6472
 * On date: 5/22/2021
 * Project: ScriptIt
 *
 ***********************/
public class RandomLibrary extends Library
{
	private static final Random random = new Random();

	public RandomLibrary()
	{
		super("Random");

		addFunction("nextInt", () -> PrimitiveValue.newValue(PrimitiveTypes.INT, random.nextInt()));
		addFunction("nextDouble", () -> PrimitiveValue.newValue(PrimitiveTypes.DOUBLE, random.nextDouble()));
		addFunction("randomInt", (min, max) -> PrimitiveValue.newValue(PrimitiveTypes.INT, random.nextInt((max.asPrimitive().getInt() - min.asPrimitive().getInt()) + 1) + min.asPrimitive().getInt()), PrimitiveTypes.INT, PrimitiveTypes.INT);
		addFunction("randomDouble", (min, max) -> PrimitiveValue.newValue(PrimitiveTypes.DOUBLE, min.asPrimitive().getDouble() + (max.asPrimitive().getDouble() - min.asPrimitive().getDouble()) * random.nextDouble()), PrimitiveTypes.DOUBLE, PrimitiveTypes.DOUBLE);
		addFunction("randomInt", (min, max, seed) ->
		{
			Random random = new Random(seed.asPrimitive().getInt());
			return PrimitiveValue.newValue(PrimitiveTypes.INT, random.nextInt((max.asPrimitive().getInt() - min.asPrimitive().getInt()) + 1) + min.asPrimitive().getInt());
		}, PrimitiveTypes.INT, PrimitiveTypes.INT, PrimitiveTypes.INT);
		addFunction("randomDouble", (min, max, seed) ->
		{
			Random random = new Random(seed.asPrimitive().getInt());
			return PrimitiveValue.newValue(PrimitiveTypes.DOUBLE, min.asPrimitive().getDouble() + (max.asPrimitive().getDouble() - min.asPrimitive().getDouble()) * random.nextDouble());
		}, PrimitiveTypes.DOUBLE, PrimitiveTypes.DOUBLE, PrimitiveTypes.INT);
	}
}
