package steve6472.scriptit;

import steve6472.scriptit.types.PrimitiveTypes;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
public class ValueConstant extends Expression
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
		if (constant.type == PrimitiveTypes.STRING)
			return '"' + constant.toString() + '"';
		else if (constant.type == PrimitiveTypes.CHAR)
			return "'" + constant + "'";
		return constant.toString();
	}

	@Override
	public String toString()
	{
		return "ValueConstant{" + "constant=" + constant + '}';
	}
}
