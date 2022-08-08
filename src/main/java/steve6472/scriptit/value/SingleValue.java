package steve6472.scriptit.value;

import steve6472.scriptit.Memory;
import steve6472.scriptit.type.Type;

import java.util.Objects;

/**
 * Created by steve6472
 * Date: 7/30/2022
 * Project: ScriptIt
 */
public class SingleValue<T> extends Value
{
	private T value;

	protected SingleValue(Type type, T value)
	{
		super(type);
		this.value = value;
	}

	public static <T> SingleValue<T> newValue(Type type, T value)
	{
		return new SingleValue<>(type, value);
	}

	public static SingleValue<?> newEmptyValue(Type type)
	{
		return new SingleValue<>(type, null);
	}

	public SingleValue<T> set(T value)
	{
		this.value = value;
		return this;
	}

	public T get()
	{
		return value;
	}

	@Override
	public void clear()
	{
		value = null;
	}

	@Override
	@SuppressWarnings(value = "unchecked") // Checked
	public void setFrom(Value other)
	{
		if (other instanceof SingleValue<?> v)
		{
			if (value == null || v.value.getClass().equals(value.getClass()))
			{
				value = (T) v.value;
			} else
			{
				throw new RuntimeException("Can not set to SingleValue, class mismatch other: %s != this: %s".formatted(v.value.getClass(), value.getClass()));
			}
		} else
		{
			throw new RuntimeException("Can not set to SingleValue from %s".formatted(other.getClass().getSimpleName()));
		}
	}

	@Override
	//TODO: implement
	public UniversalValue asUniversal()
	{
		throw new RuntimeException("Unsopported Operation (Primitive Value)");
	}

	@Override
	public void addValuesToMemory(Memory memory)
	{
		throw new RuntimeException("Unsopported Operation (Primitive Value)");
	}

	@Override
	public Value getValueByName(String name)
	{
		throw new RuntimeException("Unsopported Operation (Primitive Value)");
	}

	@Override
	public void setValueByName(String name, Value value)
	{
		throw new RuntimeException("Unsopported Operation (Primitive Value)");
	}

	@Override
	public String toString()
	{
		return "" + value;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(type, value);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		SingleValue<?> value = (SingleValue<?>) o;
		return Objects.equals(this.type, value.type) && Objects.equals(this.value, value.value);
	}
}
