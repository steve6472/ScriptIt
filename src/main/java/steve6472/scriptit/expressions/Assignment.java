package steve6472.scriptit.expressions;

import steve6472.scriptit.*;
import steve6472.scriptit.executor.Executor;
import steve6472.scriptit.types.PrimitiveTypes;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 * This should probably be done with separate classes
 * Especially the dot operator assignment
 *
 ***********************/
public class Assignment extends Expression
{
	public final String typeName;
	public final String varName;
	public final Expression expression;
	public final Executor expressionExecutor;
	public final DotOperator dotOperator;
	private Type type;

	public Assignment(String type, String varName, Expression expression)
	{
		this.typeName = type;
		this.varName = varName;
		this.expression = expression;
		if (expression != null)
		{
			this.expressionExecutor = new Executor(expression);
		} else
		{
			this.expressionExecutor = null;
		}
		this.dotOperator = null;
	}

	public Assignment(String varName, Expression expression)
	{
		this.typeName = "null";
		this.type = PrimitiveTypes.NULL;
		this.varName = varName;
		this.expression = expression;
		if (expression != null)
		{
			this.expressionExecutor = new Executor(expression);
		} else
		{
			this.expressionExecutor = null;
		}
		this.dotOperator = null;
		if (expression == null)
		{
			throw new RuntimeException("Can not create assignment of variable with no type nor any value!");
		}
	}

	public Assignment(DotOperator dop, Expression expression)
	{
		this.typeName = null;
		this.varName = null;
		this.expression = expression;
		this.expressionExecutor = new Executor(expression);
		this.dotOperator = dop;
		if (expression == null)
		{
			throw new RuntimeException("var.val name; is not allowed!");
		}
	}

	@Override
	public Result apply(Script script)
	{
		if (dotOperator != null)
		{
			assert expressionExecutor != null;

			if (expressionExecutor.executeWhatYouCan(script).isDelay())
				return Result.delay();

			Result expressionResult = expressionExecutor.getLastResult();
			Value value = expressionResult.getValue();

			Result apply = dotOperator.apply(script);
			Value dotValue = apply.getValue();
			dotValue.values.clear();
			value.values.forEach(dotValue.values::put);

			expressionExecutor.reset();

			return Result.pass();
		}

		assert varName != null;

		if (type == null)
		{
			type = script.getMemory().getType(typeName);
			if (type == null)
				throw new RuntimeException("Type '" + typeName + "' not found in memory!");
		}

		if (expression == null)
		{
			Value value = Value.newValue(type);
			script.memory.addVariable(varName, value);
			return Result.pass();
		}

		assert expressionExecutor != null;

		if (expressionExecutor.executeWhatYouCan(script).isDelay())
			return Result.delay();

		Result expressionResult = expressionExecutor.getLastResult();
		Value value = expressionResult.getValue();
		expressionExecutor.reset();

		if (type != PrimitiveTypes.NULL && value.type != type)
			throw new IllegalStateException("Returned type does not match (" + type + " != " + value.type);

		script.memory.addVariable(varName, value);

		return Result.pass();
	}

	@Override
	public String showCode(int a)
	{
		if (dotOperator != null)
		{
			return dotOperator.showCode(a) + Highlighter.SYMBOL + " = " + expression.showCode(a) + Highlighter.RESET;
		}

		assert typeName != null;
		if (expression == null)
		{
			if (!typeName.equals("null"))
			{
				return Highlighter.FUNCTION_NAME + typeName + " " + Highlighter.VAR + varName + Highlighter.RESET;
			} else
			{
				throw new RuntimeException("Uuuuhhhh bad. Bonk!");
			}
		} else
		{
			if (!typeName.equals("null"))
			{
				return Highlighter.FUNCTION_NAME + typeName + " " + Highlighter.VAR + varName + Highlighter.SYMBOL + " = " + expression.showCode(0) + Highlighter.RESET;
			} else
			{
				return Highlighter.VAR + varName + Highlighter.SYMBOL + " = " + expression.showCode(0) + Highlighter.RESET;
			}
		}
	}

	@Override
	public String toString()
	{
		return "Assignment{" + "typeName='" + typeName + '\'' + ", varName='" + varName + '\'' + ", expression=" + expression + ", type=" + type + '}';
	}
}
