package steve6472.scriptit.expressions;

import steve6472.scriptit.Highlighter;
import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.type.Type;
import steve6472.scriptit.executor.Executor;
import steve6472.scriptit.tokenizer.IOperator;
import steve6472.scriptit.tokenizer.Operator;
import steve6472.scriptit.type.PrimitiveTypes;
import steve6472.scriptit.value.Value;

import java.util.HashSet;
import java.util.Set;

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
	/**
	 * Used for pretty print in debug
	 */
	public static final Set<IOperator> COMPOUND_ASSINGMENT = new HashSet<>();

	static
	{
		COMPOUND_ASSINGMENT.add(Operator.ASSIGN_ADD);
		COMPOUND_ASSINGMENT.add(Operator.ASSIGN_SUB);
		COMPOUND_ASSINGMENT.add(Operator.ASSIGN_MUL);
		COMPOUND_ASSINGMENT.add(Operator.ASSIGN_DIV);
		COMPOUND_ASSINGMENT.add(Operator.ASSIGN_MOD);
		COMPOUND_ASSINGMENT.add(Operator.ASSIGN_BIT_AND);
		COMPOUND_ASSINGMENT.add(Operator.ASSIGN_BIT_OR);
		COMPOUND_ASSINGMENT.add(Operator.ASSIGN_BIT_XOR);
	}

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
			stackTrace("Assignment-Dot");
		} else {
			stackTrace("Assignment '" + varName + "' (" + typeName + ")");
		}

		if (dotOperator != null)
		{
			assert expressionExecutor != null;

			if (expressionExecutor.executeWhatYouCan(script).isDelay())
				return Result.delay();

			Result expressionResult = expressionExecutor.getLastResult();
			Value value = expressionResult.getValue();

			Result apply = dotOperator.apply(script);
			Value dotValue = apply.getValue();
			dotValue.clear();
			dotValue.setFrom(value);

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
			Value value = type.uninitValue();
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

		return Result.value(value);
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
				if (expression instanceof BinaryOperator bo && COMPOUND_ASSINGMENT.contains(bo.operator))
				{
					return Highlighter.VAR + varName + Highlighter.SYMBOL + " " + bo.operator.getSymbol() + " " + bo.right.showCode(0) + Highlighter.RESET;
				} else
				{
					return Highlighter.VAR + varName + Highlighter.SYMBOL + " = " + expression.showCode(0) + Highlighter.RESET;
				}
			}
		}
	}

	@Override
	public String toString()
	{
		return "Assignment{" + "typeName='" + typeName + '\'' + ", varName='" + varName + '\'' + ", expression=" + expression + ", type=" + type + '}';
	}
}
