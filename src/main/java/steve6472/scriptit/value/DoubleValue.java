package steve6472.scriptit.value;

import steve6472.scriptit.Memory;
import steve6472.scriptit.type.Type;

import java.util.Objects;

/**
 * Created by steve6472
 * Date: 7/30/2022
 * Project: ScriptIt
 */
public class DoubleValue<T, U> extends Value
{
	private T valueOne;
	private U valueTwo;

	protected DoubleValue(Type type, T valueOne, U valueTwo)
	{
		super(type);
		this.valueOne = valueOne;
		this.valueTwo = valueTwo;
	}

	public static <T, U> DoubleValue<T, U> newValue(Type type, T valueOne, U valueTwo)
	{
		return new DoubleValue<>(type, valueOne, valueTwo);
	}

	public static DoubleValue<?, ?> newEmptyValue(Type type)
	{
		return new DoubleValue<>(type, null, null);
	}

	public DoubleValue<T, U> set(T valueOne, U valueTwo)
	{
		this.valueOne = valueOne;
		this.valueTwo = valueTwo;
		return this;
	}

	public T getFirst()
	{
		return valueOne;
	}

	public U getSecond()
	{
		return valueTwo;
	}

	@Override
	public void clear()
	{
		valueOne = null;
		valueTwo = null;
	}

	@Override
	@SuppressWarnings(value = "unchecked") // Checked
	public void setFrom(Value other)
	{
		if (other instanceof DoubleValue<?, ?> v)
		{
			if (valueOne == null || v.valueOne.getClass().equals(valueOne.getClass()))
			{
				if ((valueTwo == null || v.valueTwo.getClass().equals(valueTwo.getClass())))
				{
					valueOne = (T) v.valueOne;
					valueTwo = (U) v.valueTwo;
				} else
				{
					throw new RuntimeException("Can not set to DoubleValue, class mismatch other (second value): %s != this: %s".formatted(v.valueTwo.getClass(), valueTwo.getClass()));
				}
			} else
			{
				throw new RuntimeException("Can not set to DoubleValue, class mismatch other (first value): %s != this: %s".formatted(v.valueOne.getClass(), valueOne.getClass()));
			}
		} else
		{
			throw new RuntimeException("Can not set to DoubleValue from %s".formatted(other.getClass().getSimpleName()));
		}
	}

	@Override
	//TODO: implement
	public UniversalValue asUniversal()
	{
		throw new RuntimeException("Unsopported Operation (DoubleValue)");
	}

	@Override
	public void addValuesToMemory(Memory memory)
	{
		throw new RuntimeException("Unsopported Operation (DoubleValue)");
	}

	@Override
	public Value getValueByName(String name)
	{
		throw new RuntimeException("Unsopported Operation (DoubleValue)");
	}

	@Override
	public void setValueByName(String name, Value value)
	{
		throw new RuntimeException("Unsopported Operation (DoubleValue)");
	}

	@Override
	public String toString()
	{
		return valueOne + ", " + valueTwo;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(type, valueOne, valueTwo);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		DoubleValue<?, ?> value = (DoubleValue<?, ?>) o;
		return Objects.equals(this.type, value.type) && Objects.equals(this.valueOne, value.valueOne) && Objects.equals(this.valueTwo, value.valueTwo);
	}
}
