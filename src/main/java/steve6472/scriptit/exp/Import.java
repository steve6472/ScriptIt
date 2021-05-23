package steve6472.scriptit.exp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
class Import extends Expression
{
	private final ImportType type;
	private final String name;

	public Import(ImportType type, String name)
	{
		this.type = type;
		this.name = name;
	}

	@Override
	public Result apply(Script script)
	{
		if (type == ImportType.TYPE)
			script.getMemory().addType(script.getWorkspace().getType(name));
		else
			script.getMemory().addLibrary(script.getWorkspace().getLibrary(name));

		return Result.pass();
	}
}
