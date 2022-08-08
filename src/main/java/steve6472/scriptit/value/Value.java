package steve6472.scriptit.value;

import steve6472.scriptit.Memory;
import steve6472.scriptit.type.PrimitiveType;
import steve6472.scriptit.type.PrimitiveTypes;
import steve6472.scriptit.type.Type;

import java.util.Objects;

public abstract class Value
{
	/**
	 * null value of type null
	 */
	public static final Value NULL = new NullValue();

	public Type type;

	protected Value(Type type)
	{
		this.type = type;
	}

	/*
	 * Universal for all Value types
	 */

	public final boolean isNull()
	{
		return type == PrimitiveTypes.NULL;
	}

	public final boolean isPrimitive()
	{
		return this instanceof PrimitiveValue<?> || this == NULL;
	}

	/*
	 * QOL functions
	 */

	@SuppressWarnings("unchecked")
	public <T> PrimitiveValue<T> asPrimitive()
	{
		return (PrimitiveValue<T>) this;
	}

	public UniversalValue asUniversal()
	{
		return (UniversalValue) this;
	}

	@SuppressWarnings("unchecked")
	public <T extends Value> T as(PrimitiveType<T> t)
	{
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public <T extends Value> T as(Class<T> t)
	{
		return (T) this;
	}

	/*
	 * Memory stuff
	 */

	public abstract void clear();
	public abstract void setFrom(Value other);
	public abstract void addValuesToMemory(Memory memory);

	/*
	 * Because values can hold other values.
	 * Example: color.r; or color.b = 6;
	 */

	public abstract Value getValueByName(String name);
	public abstract void setValueByName(String name, Value value);

	/*
	 * Fancy functions
	 */

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "{" + "type=" + type.getKeyword() + '}';
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Value value = (Value) o;
		return Objects.equals(type, value.type);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(type);
	}

	/*
	 * Null
	 */

	private static class NullValue extends Value
	{
		protected NullValue()
		{
			super(PrimitiveTypes.NULL);
		}

		@Override
		public <T> PrimitiveValue<T> asPrimitive()
		{
			throw new RuntimeException("Unsopported Operation (null)");
		}

		@Override
		public UniversalValue asUniversal()
		{
			throw new RuntimeException("Unsopported Operation (null)");
		}

		@Override
		public <T extends Value> T as(Class<T> t)
		{
			throw new RuntimeException("Unsopported Operation (null)");
		}

		@Override
		public <T extends Value> T as(PrimitiveType<T> t)
		{
			throw new RuntimeException("Unsopported Operation (null)");
		}

		@Override
		public void clear()
		{
			throw new RuntimeException("Unsopported Operation (null)");
		}

		@Override
		public void setFrom(Value other)
		{
			throw new RuntimeException("Unsopported Operation (null)");
		}

		@Override
		public void addValuesToMemory(Memory memory)
		{
			throw new RuntimeException("Unsopported Operation (null)");
		}

		@Override
		public Value getValueByName(String name)
		{
			throw new RuntimeException("Unsopported Operation (null)");
		}

		@Override
		public void setValueByName(String name, Value value)
		{
			throw new RuntimeException("Unsopported Operation (null)");
		}
	}
}