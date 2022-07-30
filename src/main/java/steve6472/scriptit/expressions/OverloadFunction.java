package steve6472.scriptit.expressions;

import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.type.Type;
import steve6472.scriptit.tokenizer.IOperator;

import java.util.Arrays;

/**********************
 * Created by steve6472
 * On date: 5/10/2022
 * Project: ScriptIt
 *
 ***********************/
public class OverloadFunction extends Expression
{
	final IOperator operator;
	final Type[] types;
	final String[] strings;
	final Function body;
	final OverloadType type;

	public OverloadFunction(IOperator operator, Type[] types, String[] strings, Function body, OverloadType type)
	{
		this.operator = operator;
		this.types = types;
		this.strings = strings;
		this.body = body;
		this.type = type;
		body.argumentNames = strings;
	}

	@Override
	public Result apply(Script script)
	{
		return Result.pass();
	}

	@Override
	public String showCode(int a)
	{
		return "overload";
	}

	@Override
	public String toString()
	{
		return "OverloadFunction{" + "operator=" + operator + ", types=" + Arrays.toString(types) + ", strings=" + Arrays
			.toString(strings) + ", body=" + body + ", type=" + type + '}';
	}

	public enum OverloadType
	{
		UNARY, BINARY
	}
}
