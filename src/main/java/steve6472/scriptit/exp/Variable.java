package steve6472.scriptit.exp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
class Variable extends Expression
{
	String name;

	public Variable(String name)
	{
		this.name = name;
	}

	@Override
	public double apply(ExpressionExecutor executor)
	{
		return executor.memory.getVariable(name);
	}

	@Override
	public void print(int i)
	{
		System.out.println(i + " Variable " + name);
	}

	@Override
	public String toString()
	{
		return "Variable{" + "name=" + name + '}';
	}
}
