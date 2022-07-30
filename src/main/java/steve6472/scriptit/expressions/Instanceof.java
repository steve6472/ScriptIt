package steve6472.scriptit.expressions;

import steve6472.scriptit.Highlighter;
import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.type.Type;
import steve6472.scriptit.executor.Executor;
import steve6472.scriptit.type.PrimitiveTypes;

/**********************
 * Created by steve6472
 * On date: 5/8/2022
 * Project: ScriptIt
 *
 ***********************/
public class Instanceof extends Expression
{
	Expression leftExp;
	Executor exe;
	String typeName;
	Type type;
	boolean not;

	public Instanceof(Expression leftExpression, String typeName, boolean not)
	{
		this.typeName = typeName;
		this.leftExp = leftExpression;
		this.exe = new Executor(leftExp);
		this.not = not;
	}

	@Override
	public Result apply(Script script)
	{
		if (type == null)
		{
			this.type = script.getMemory().getType(typeName);
		}

		if (exe.executeWhatYouCan(script).isDelay())
			return Result.delay();

		Result lastResult = exe.getLastResult();
		exe.reset();

		if (not)
		{
			if (lastResult.getValue().type.equals(type))
			{
				return Result.value(PrimitiveTypes.FALSE());
			} else
			{
				return Result.value(PrimitiveTypes.TRUE());
			}
		} else
		{
			if (lastResult.getValue().type.equals(type))
			{
				return Result.value(PrimitiveTypes.TRUE());
			} else
			{
				return Result.value(PrimitiveTypes.FALSE());
			}
		}
	}

	@Override
	public String showCode(int a)
	{
		if (not)
		{
			return leftExp.showCode(a) + Highlighter.INSTANCEOF + " instanceof" + Highlighter.SYMBOL + "! " + Highlighter.RESET + typeName;
		} else
		{
			return leftExp.showCode(a) + Highlighter.INSTANCEOF + " instanceof " + Highlighter.RESET + typeName;
		}
	}
}
