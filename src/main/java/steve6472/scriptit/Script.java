package steve6472.scriptit;

import steve6472.scriptit.expression.Value;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/2/2021
 * Project: ScriptIt
 *
 ***********************/
public class Script
{
	public Namespace namespace;

	public List<Command> commands;

	private Script parent;

	public Script()
	{
		namespace = new Namespace();
		commands = new ArrayList<>();
	}

	@SuppressWarnings("IncompleteCopyConstructor")
	public Script(Script parent)
	{
		namespace = new Namespace();
		commands = new ArrayList<>();
		this.parent = parent;
	}

	public Value run()
	{
		for (Command c : commands)
		{
			Value execute = c.execute(this);
			if (execute != null)
				return execute;
		}

		return null;
	}

	public Value runDebug()
	{
		for (Command c : commands)
		{
			System.out.println(c.toString());
			Value execute = c.execute(this);
			System.out.println("\nNamespace: ");
			namespace.print();
			if (execute != null)
				return execute;
		}

		return null;
	}

	public void printCode()
	{
		for (Command c : commands)
		{
			System.out.println(c.toString());
		}
	}
}
