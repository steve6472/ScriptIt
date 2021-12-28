package steve6472.scriptit;

import steve6472.scriptit.types.PrimitiveTypes;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
public class Assignment extends Expression
{
	private final String typeName;
	private final String varName;
	private final Expression expression;
	private Type type;
	Result res = Result.delay();
	Value value;

	public Assignment(String type, String varName, Expression expression)
	{
		this.typeName = type;
		this.varName = varName;
		this.expression = expression;
	}

	public Assignment(String varName, Expression expression)
	{
		this.typeName = "null";
		this.type = PrimitiveTypes.NULL;
		this.varName = varName;
		this.expression = expression;
	}

	@Override
	public Result apply(Script script)
	{
		if (res.isDelay())
			res = expression.apply(script);

		if (res.isDelay())
			return res;

		value = res.getValue();

		if (type == null)
		{
			type = script.getMemory().getType(typeName);
			if (type == null)
				throw new RuntimeException("Type '" + typeName + "' not found in memory!");
		}

		if (type != PrimitiveTypes.NULL && value.type != type)
			throw new IllegalStateException("Returned type does not match (" + type + " != " + value.type);

		script.memory.addVariable(varName, res.getValue());

		res = Result.delay();
		return Result.pass();
	}

	@Override
	public String showCode(int a)
	{
		if (!typeName.equals("null"))
			return Highlighter.FUNCTION_NAME + typeName + " " + Highlighter.VAR + varName + Highlighter.SYMBOL + " = " + expression.showCode(0) + Highlighter.RESET;
			else
		return Highlighter.VAR + varName + Highlighter.SYMBOL + " = " + expression.showCode(0) + Highlighter.RESET;
	}

	@Override
	public String toString()
	{
		return "Assignment{" + "typeName='" + typeName + '\'' + ", varName='" + varName + '\'' + ", expression=" + expression + ", type=" + type + ", value=" + value + '}';
	}
}
