package steve6472.scriptit;

import steve6472.scriptit.types.PrimitiveTypes;

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
	public Result apply(Script script)
	{
		return Result.value(Value.newValue(type, constant));
	}

	@Override
	public String showCode(int a)
	{
		if (type == PrimitiveTypes.STRING)
			return Highlighter.CONST_STR + '"' + constant.toString().replace("\n", "\\n") + '"' + Highlighter.RESET;
		else if (type == PrimitiveTypes.CHAR)
			return Highlighter.CONST_CHAR + "'" + constant.toString() + "'" + Highlighter.RESET;
		else if (type == PrimitiveTypes.INT || type == PrimitiveTypes.DOUBLE)
			return Highlighter.CONST_NUM + constant.toString() + Highlighter.RESET;
		else if (type == PrimitiveTypes.BOOL)
			return Highlighter.CONST_BOOL + constant.toString() + Highlighter.RESET;
		return constant.toString();
	}

	@Override
	public String toString()
	{
		return "Constant{" + "constant=" + constant + ", type=" + type + '}';
	}
}
