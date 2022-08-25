package steve6472.scriptit.expressions;

import steve6472.scriptit.Log;
import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.exceptions.TypeMismatchException;
import steve6472.scriptit.tokenizer.ChainedVariable;
import steve6472.scriptit.type.Type;
import steve6472.scriptit.executor.Executor;
import steve6472.scriptit.tokenizer.Operator;
import steve6472.scriptit.type.PrimitiveTypes;
import steve6472.scriptit.value.DoubleValue;
import steve6472.scriptit.value.PrimitiveValue;
import steve6472.scriptit.value.Value;

import java.util.ArrayList;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 * <br>
 * This should probably be done with separate classes
 * Especially the dot operator assignment
 *
 ***********************/
public class Assignment extends Expression
{
	public final Expression valuePath;
	public final Executor valuePathExecutor;

	public Expression expression;
	public Executor expressionExecutor;

	public final boolean isDeclaration;

	private Type type;
	private String variableName;
	private boolean isArray;

	public static Assignment newFancyStyle(Expression path, Expression expression)
	{
		return new Assignment(path, expression);
	}

	private Assignment(Expression path, Expression expression)
	{
		this.expression = expression;
		this.valuePath = path;

		this.valuePathExecutor = new Executor(valuePath);

		validatePath(valuePath);

		if (expression != null)
		{
			this.expressionExecutor = new Executor(expression);
		} else
		{
			this.expressionExecutor = null;
		}
		this.isDeclaration = expressionExecutor == null || valuePath instanceof ChainedVariable;
	}

	private void validatePath(Expression path)
	{
		// TODO: maybe add a setting to allow this idk
		if (path instanceof FunctionCall)
			throw new RuntimeException("Can not assign to value returned from function!");

		if (path instanceof BinaryOperator bo)
		{
			if (bo.operator != Operator.INDEX)
				throw new RuntimeException("Binary Operator assignment accepts only arrays (Index) -> []");
		}

		/*
		 * Allowed
		 */

		if (path instanceof DotOperator) return;
		if (path instanceof Variable) return;
		if (path instanceof ChainedVariable) return;
	}

	/**
	 * @param script script
	 * @return null if path requires a delay, value otherwise
	 */
	public Value resolvePathValue(Script script)
	{
		if (valuePath instanceof Variable var)
		{
			variableName = var.variableName;
			return var.getValue(script);
		}

		if (valuePath instanceof FunctionCall || valuePath instanceof BinaryOperator || valuePath instanceof DotOperator)
		{
			if (valuePathExecutor.executeWhatYouCan(script).isDelay())
				return null;

			Value value = valuePathExecutor.getLastResult().getValue();
			if (value != null)
			{
				return value;
			} else
			{
				throw new RuntimeException("BinaryOperator did not return a value ?!");
			}
		}

		return null;
	}

	public record DeclarationData(String variableName, Type variableType) {}

	public DeclarationData resolveDeclarationType(Script script, Expression valuePath)
	{
		if (valuePath instanceof ChainedVariable cv)
		{
			String name;

			if (cv.exp2 instanceof Variable var_)
			{
				name = var_.variableName;
			} else
			{
				throw new RuntimeException("Declaration does not have a name! or something idk what to type here");
			}

			if (cv.exp1 instanceof Variable var)
			{
				Type type = script.getMemory().getType(var.variableName);
				return new DeclarationData(name, type);
			}
		}

		throw new RuntimeException("Unknown declaration for " + valuePath);
	}

	public Value declareUninitValue(DeclarationData data)
	{
		if (data.variableType.isArray())
		{
			return DoubleValue.newValue(data.variableType, null, Type.getArraySuperType(data.variableType));
		} else
		{
			return data.variableType.uninitValue();
		}
	}

	private void transformExpressionIntoArray(Type type)
	{
		// Transform int[5] to 5
		if (expression instanceof BinaryOperator bo && bo.operator == Operator.INDEX)
		{
			expression = bo.right;
			expressionExecutor = new Executor(expression);
			isArray = true;
		}
	}

	private Value createArray(Script script, Type type)
	{
		if (expressionExecutor.executeWhatYouCan(script).isDelay())
			return null;

		Value value = expressionExecutor.getLastResult().getValue();

		if (value.type.isArray())
		{
			return value;
		}

		if (value.type != PrimitiveTypes.INT)
		{
			throw new RuntimeException("Can not create array, size is not an int");
		}

		int capacity = value.asPrimitive().getInt();
		ArrayList<Value> list = new ArrayList<>(capacity);

		for (int i = 0; i < capacity; i++)
		{
			list.add(Type.getArraySuperType(type).uninitValue());
		}

		return DoubleValue.newValue(type, list, Type.getArraySuperType(type));
	}

	public Value declareInitValue(Script script, DeclarationData data)
	{
		if (data.variableType.isArray())
		{
			transformExpressionIntoArray(data.variableType);

			return createArray(script, data.variableType);
		} else
		{
			if (expressionExecutor.executeWhatYouCan(script).isDelay())
				return null;

			Value value = expressionExecutor.getLastResult().getValue();

			if (value.isPrimitive())
			{
				Value value1 = value.type.uninitValue();
				value1.setFrom(value);
				value = value1;
			}

			if (value.type != data.variableType)
				throw new TypeMismatchException("Can not assign", data.variableType, value.type);

			return value;
		}
	}

	@Override
	public Result apply(Script script)
	{
		if (isDeclaration)
		{
			DeclarationData data = resolveDeclarationType(script, valuePath);

			if (type == null)
			{
				type = data.variableType;
				variableName = data.variableName;
			}

			Value value;

			if (expression == null)
			{
				value = declareUninitValue(data);
			} else
			{
				value = declareInitValue(script, data);
			}

			if (value == null)
				return Result.delay();

			if (expression != null)
			{
				expressionExecutor.reset();
			}

			script.memory.addVariable(data.variableName, value);
			return Result.value(value);
		} else
		{
			Value value = resolvePathValue(script);

			if (value == null)
				return Result.delay();

			Result lastResult;

			if (value.type.isArray())
			{
				Type type = (Type) value.as(DoubleValue.class).getSecond();
				transformExpressionIntoArray(type);

				lastResult = Result.value(createArray(script, value.type));
			} else
			{
				if (expressionExecutor.executeWhatYouCan(script).isDelay())
					return Result.delay();

				lastResult = expressionExecutor.getLastResult();
			}

			valuePathExecutor.reset();
			expressionExecutor.reset();

			if (value.type != lastResult.getValue().type)
				throw new TypeMismatchException(value.type, lastResult.getValue().type);

			// Makes everything pass by value
			value.setFrom(lastResult.getValue());
			return lastResult;
		}
	}

	public Type getType()
	{
		return type;
	}

	public String getVariableName()
	{
		return variableName;
	}

	public boolean isArray()
	{
		return isArray;
	}

	@Override
	public String showCode(int a)
	{
		if (isDeclaration)
		{
			if (type != null)
			{
				if (isArray)
				{
					return type.getKeyword() + " " + variableName + " = " + type.getKeyword() + "[" + expression.showCode(a) + "]";
				} else
				{
					return type.getKeyword() + " " + variableName + " = " + expression.showCode(a);
				}
			} else
			{
				if (expression == null)
				{
					return valuePath.showCode(a);
				} else
				{
					return valuePath.showCode(a) + " = " + expression.showCode(a);
				}
			}
		} else
		{
			if (type != null)
			{
				if (isArray)
				{
					return variableName + " = " + type.getKeyword() + "[" + expression.showCode(a) + "]";
				} else
				{
					return variableName + " = " + expression.showCode(a);
				}
			} else
			{
				if (expression == null)
				{
					return valuePath.showCode(a);
				} else
				{
					return valuePath.showCode(a) + " = " + expression.showCode(a);
				}
			}
		}
	}

	@Override
	public String toString()
	{
		return "Assignment{" + "valuePath=" + valuePath + ", expression=" + expression  + ", isDeclaration=" + isDeclaration + '}';
	}
}
