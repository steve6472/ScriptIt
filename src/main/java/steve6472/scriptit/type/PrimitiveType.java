package steve6472.scriptit.type;

import steve6472.scriptit.expressions.Function;
import steve6472.scriptit.tokenizer.IOperator;
import steve6472.scriptit.value.PrimitiveValue;
import steve6472.scriptit.value.Value;

import java.util.HashMap;
import java.util.function.Supplier;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/21/2021
 * Project: ScriptIt
 *
 ***********************/
public class PrimitiveType<A extends Value> extends Type
{
	public PrimitiveType(String keyword)
	{
		super(keyword);
		setUninitValue(() -> PrimitiveValue.newEmptyValue(this));
	}

	public PrimitiveType(String keyword, Supplier<Value> uninitValue)
	{
		super(keyword);
		setUninitValue(uninitValue);
	}

	public <T extends Value> void addBinaryOperator(PrimitiveType<T> rightOperandType, IOperator operator, TypesInit.PBinaryOperatorOverload<A, T> function)
	{
		HashMap<IOperator, Function> map = binary.get(rightOperandType);
		if (map == null)
		{
			map = new HashMap<>();
		}
		map.put(operator, function);
		binary.put(rightOperandType, map);
	}

	public void addUnaryOperator(IOperator operator, TypesInit.PUnaryOperatorOverload<A> function)
	{
		unary.put(operator, function);
	}

	@FunctionalInterface
	public interface BinaryOperator<Left extends Value, Right extends Value>
	{
		Value apply(Left left, Right right);
	}

	@FunctionalInterface
	public interface UnaryOperator<Right extends Value>
	{
		Value apply(Right right);
	}
}
