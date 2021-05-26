package steve6472.scriptit.libraries;

import steve6472.scriptit.types.PrimitiveTypes;

import java.util.Random;

import static steve6472.scriptit.Value.newValue;

/**********************
 * Created by steve6472 (Mirek Jozefek)
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

		addFunction("nextInt", () -> newValue(PrimitiveTypes.INT, random.nextInt()));
		addFunction("nextDouble", () -> newValue(PrimitiveTypes.DOUBLE, random.nextDouble()));
		addFunction("randomInt", (min, max) -> newValue(PrimitiveTypes.INT, random.nextInt((max.getInt() - min.getInt()) + 1) + min.getInt()), PrimitiveTypes.INT, PrimitiveTypes.INT);
		addFunction("randomDouble", (min, max) -> newValue(PrimitiveTypes.DOUBLE, min.getDouble() + (max.getDouble() - min.getDouble()) * random.nextDouble()), PrimitiveTypes.DOUBLE, PrimitiveTypes.DOUBLE);
		addFunction("randomInt", (min, max, seed) ->
		{
			Random random = new Random(seed.getInt());
			return newValue(PrimitiveTypes.INT, random.nextInt((max.getInt() - min.getInt()) + 1) + min.getInt());
		}, PrimitiveTypes.INT, PrimitiveTypes.INT, PrimitiveTypes.INT);
		addFunction("randomDouble", (min, max, seed) ->
		{
			Random random = new Random(seed.getInt());
			return newValue(PrimitiveTypes.DOUBLE, min.getDouble() + (max.getDouble() - min.getDouble()) * random.nextDouble());
		}, PrimitiveTypes.DOUBLE, PrimitiveTypes.DOUBLE, PrimitiveTypes.INT);
	}
}
