package steve6472.scriptit;

import steve6472.scriptit.libraries.Library;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/23/2021
 * Project: ScriptIt
 *
 ***********************/
public class Script
{
	private final Workspace workspace;

	private final MyParser parser;
	MemoryStack memory;
	ExpressionExecutor[] lines;
	private int lastIndex = 0;
	private int currentIndex = 0;

	public Script(Workspace workspace)
	{
		this.workspace = workspace;
		this.parser = new MyParser();
		this.memory = new MemoryStack(64);
	}

	public void addLibrary(Library library)
	{
		memory.addLibrary(library);
	}

	public void setExpressions(String... expressions)
	{
		lines = new ExpressionExecutor[expressions.length];
		for (int i = 0; i < expressions.length; i++)
		{
			lines[i] = new ExpressionExecutor(memory);
			lines[i].setExpression(parser.setExpression(expressions[i]).parse());
		}
	}

	public void setExpressions(Expression... expressions)
	{
		lines = new ExpressionExecutor[expressions.length];
		for (int i = 0; i < expressions.length; i++)
		{
			lines[i] = new ExpressionExecutor(memory);
			lines[i].setExpression(expressions[i]);
		}
	}

	public Result execute()
	{
		currentIndex = lastIndex;
		while (currentIndex < lines.length)
		{
			lastIndex = currentIndex;

			Result result = lines[currentIndex].execute(this);
			if (result.isReturnValue() || result.isReturn())
			{
				lastIndex = 0;
				return result;
			}
			if (result.isDelay())
				return result;
			currentIndex++;
		}

		return Result.return_();
	}

	public Value runWithDelay()
	{
		Result ret = Result.delay();

		while (ret.isDelay() && !ret.isReturnValue() && !ret.isReturn())
		{
			ret = execute();
		}
		if (ret.isReturnValue())
			return ret.getValue();
		else
			return Value.NULL;
	}

	public ExpressionExecutor currentExecutor()
	{
		return lines[currentIndex];
	}

	public MemoryStack getMemory()
	{
		return memory;
	}

	public MyParser getParser()
	{
		return parser;
	}

	public Workspace getWorkspace()
	{
		return workspace;
	}
}
