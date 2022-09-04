package steve6472.scriptit.transformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve6472
 * Date: 9/2/2022
 * Project: ScriptIt
 */
public class Farm
{
	List<Animal> animals;

	public Farm()
	{
		animals = new ArrayList<>();
	}

	public List<Animal> getAnimals()
	{
		return animals;
	}

	public void addAnimal(Animal animal)
	{
		animals.add(animal);
	}

	public int getAnimalCount()
	{
		return animals.size();
	}
}
