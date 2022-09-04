package steve6472.scriptit.transformer;

/**
 * Created by steve6472
 * Date: 9/2/2022
 * Project: ScriptIt
 */
public class Feline extends Animal
{
	@Override
	public String id()
	{
		return "Feline";
	}

	@Override
	public FoodType food()
	{
		return FoodType.APPLE;
	}
}
