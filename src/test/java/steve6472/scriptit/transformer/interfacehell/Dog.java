package steve6472.scriptit.transformer.interfacehell;

public class Dog implements Bar
{
	public Dog()
	{

	}

	@Override
	public void a()
	{
		System.out.println("A");
	}
	@Override
	public void c()
	{
		System.out.println("C");
	}
	@Override
	public void d()
	{
		System.out.println("D");
	}
}