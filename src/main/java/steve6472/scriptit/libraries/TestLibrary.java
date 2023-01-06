package steve6472.scriptit.libraries;

/**********************
 * Created by steve6472
 * On date: 5/22/2021
 * Project: ScriptIt
 *
 ***********************/
public class TestLibrary extends Library
{
	public TestLibrary()
	{
		super("Test");
/*
		addFunction(FunctionParameters.create("print", PrimitiveTypes.NULL), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				System.out.print(arguments[0]);
				return Result.pass();
			}
		});*/
	}
}
