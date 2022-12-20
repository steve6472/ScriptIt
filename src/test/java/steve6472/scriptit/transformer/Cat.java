package steve6472.scriptit.transformer;

/**
 * Created by steve6472
 * Date: 9/2/2022
 * Project: ScriptIt
 */
public class Cat extends Feline implements Mewoable
{
	public String name;

	public Cat(String name)
	{
		this.name = name;
	}

	@Override
	public FoodType food()
	{
		return FoodType.MEAT;
	}

	public String getName()
	{
		return name;
	}

	@Override
	public void meow()
	{
		System.out.println("meow");
	}

	@Override
	public boolean isFemale()
	{
		return Math.random() >= 0.5;
	}
}
