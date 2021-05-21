package steve6472.scriptit.exp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
public class Constant extends Expression
{
	Object constant;
	Type type;

	public Constant(Type type, Object constant)
	{
		this.type = type;
		this.constant = constant;
	}

	@Override
	public Result apply(Main.Script script)
	{
		return Result.value(Value.newValue(type, constant));
	}

	@Override
	public String toString()
	{
		return "Constant{" + "constant=" + constant + ", type=" + type + '}';
	}
}
