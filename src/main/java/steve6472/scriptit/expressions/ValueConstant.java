package steve6472.scriptit.expressions;

import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.Value;
import steve6472.scriptit.types.PrimitiveTypes;

import java.util.function.Supplier;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
public class ValueConstant extends Expression
{
	public Supplier<Value> constantSuplier;

	public ValueConstant(Supplier<Value> constantSuplier)
	{
		this.constantSuplier = constantSuplier;
	}

	@Override
	public Result apply(Script script)
	{
		return Result.value(constantSuplier.get());
	}

	@Override
	public String showCode(int a)
	{
		Value value = constantSuplier.get();
		if (value.type == PrimitiveTypes.STRING)
			return '"' + value.toString().replaceAll("\\n", "\\\\n") + '"';
		else if (value.type == PrimitiveTypes.CHAR)
			return "'" + value + "'";
		return value.toString();
	}

	@Override
	public String toString()
	{
		return "ValueConstant{" + "constant=" + constantSuplier.get() + '}';
	}
}
