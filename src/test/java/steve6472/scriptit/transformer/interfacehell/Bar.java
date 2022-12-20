package steve6472.scriptit.transformer.interfacehell;

public interface Bar extends Foo, Fee
{
	void d();
	
	@Override
	default void b()
	{
		System.out.println("Bee");
	}
}