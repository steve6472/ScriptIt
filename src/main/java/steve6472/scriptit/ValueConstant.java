package steve6472.scriptit;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
class ValueConstant extends Expression
{
	public Value constant;

	public ValueConstant(Value constant)
	{
		this.constant = constant;
	}

	@Override
	public Result apply(Script script)
	{
		return Result.value(constant);
	}

	@Override
	public String showCode(int a)
	{
		return constant.toString();
	}

	@Override
	public String toString()
	{
		return "ValueConstant{" + "constant=" + constant + '}';
	}
}
