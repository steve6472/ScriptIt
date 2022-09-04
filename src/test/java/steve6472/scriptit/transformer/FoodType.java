package steve6472.scriptit.transformer;

/**
 * Created by steve6472
 * Date: 9/2/2022
 * Project: ScriptIt
 */
public enum FoodType
{
	APPLE(69), MEAT(999), FRUIT(2), BANANA(3);

	private final int nutrients;

	FoodType(int nutrients)
	{
		this.nutrients = nutrients;
	}

	public int getNutrients()
	{
		return nutrients;
	}
}
