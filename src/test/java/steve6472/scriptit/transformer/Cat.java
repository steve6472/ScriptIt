package steve6472.scriptit.transformer;

/**
 * Created by steve6472
 * Date: 9/2/2022
 * Project: ScriptIt
 */
public class Cat extends Feline
{
	public String name;

	@Override
	public FoodType food()
	{
		return FoodType.MEAT;
	}

	public String getName()
	{
		return name;
	}
}
