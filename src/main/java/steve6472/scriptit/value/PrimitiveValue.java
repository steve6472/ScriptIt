package steve6472.scriptit.value;

import steve6472.scriptit.Log;
import steve6472.scriptit.Memory;
import steve6472.scriptit.ScriptItSettings;
import steve6472.scriptit.type.Type;
import steve6472.scriptit.exceptions.TypeMismatchException;
import steve6472.scriptit.type.PrimitiveTypes;

import java.util.Objects;

/**
 * Created by steve6472
 * Date: 7/30/2022
 * Project: ScriptIt
 */
public class PrimitiveValue<T> extends Value
{
	private T value;

	protected PrimitiveValue(Type type, T value)
	{
		super(type);
		this.value = value;
	}

	public static <T> PrimitiveValue<T> newValue(Type type, T value)
	{
		Type guessedType = decidePrimitiveType(value);
		if (value != null && guessedType != PrimitiveTypes.NULL && type != guessedType)
		{
			Log.scriptWarning(ScriptItSettings.PRIMITIVE_TYPES_MISMATCH_WARNING, "Primitive type mismatch, guessed %s != %s".formatted(guessedType, type));
		}
		return new PrimitiveValue<>(type, value);
	}

	public static <T> PrimitiveValue<T> newValue(T value)
	{
		Type primitiveType = decidePrimitiveType(value);
		if (primitiveType != PrimitiveTypes.NULL)
		{
			throw new RuntimeException("Primitive type not found for %s".formatted(value));
		}
		return new PrimitiveValue<>(primitiveType, value);
	}

	public static PrimitiveValue<?> newEmptyValue(Type type)
	{
		return new PrimitiveValue<>(type, null);
	}

	public PrimitiveValue<T> set(T value)
	{
		this.value = value;
		return this;
	}

	public T get()
	{
		return value;
	}

	public boolean getBoolean()
	{
		if (type != PrimitiveTypes.BOOL)
			throw new TypeMismatchException("Tried to get %s, but value is %s", PrimitiveTypes.BOOL, type);
		return (boolean) value;
	}

	public int getInt()
	{
		if (type != PrimitiveTypes.INT)
			throw new TypeMismatchException("Tried to get %s, but value is %s", PrimitiveTypes.INT, type);
		return (int) value;
	}

	public double getDouble()
	{
		if (type != PrimitiveTypes.DOUBLE)
			throw new TypeMismatchException("Tried to get %s, but value is %s", PrimitiveTypes.DOUBLE, type);
		return (double) value;
	}

	public float getFloat()
	{
		if (type != PrimitiveTypes.FLOAT)
			throw new TypeMismatchException("Tried to get %s, but value is %s", PrimitiveTypes.FLOAT, type);
		return (float) value;
	}

	public char getChar()
	{
		if (type != PrimitiveTypes.CHAR)
			throw new TypeMismatchException("Tried to get %s, but value is %s", PrimitiveTypes.CHAR, type);
		return (char) value;
	}

	public String getString()
	{
		if (type != PrimitiveTypes.STRING)
			throw new TypeMismatchException("Tried to get %s, but value is %s", PrimitiveTypes.STRING, type);
		return (String) value;
	}

	public static Type decidePrimitiveType(Object javaPrimitive)
	{
		if (javaPrimitive instanceof Double) return PrimitiveTypes.DOUBLE;
		else if (javaPrimitive instanceof Integer) return PrimitiveTypes.INT;
		else if (javaPrimitive instanceof Boolean) return PrimitiveTypes.BOOL;
		else if (javaPrimitive instanceof String) return PrimitiveTypes.STRING;
		else if (javaPrimitive instanceof Character) return PrimitiveTypes.CHAR;
		return PrimitiveTypes.NULL;
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
		if (other instanceof PrimitiveValue<?> v)
		{
			if (value == null || v.value.getClass().equals(value.getClass()))
			{
				value = (T) v.value;
			} else
			{
				throw new RuntimeException("Can not set to PrimitiveValue, class mismatch other: %s != this: %s".formatted(v.value.getClass(), value.getClass()));
			}
		}
		else
		{
			throw new RuntimeException("Can not set to PrimitiveValue from %s".formatted(other.getClass().getSimpleName()));
		}
	}

	@Override
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
		PrimitiveValue<?> value = (PrimitiveValue<?>) o;
		return Objects.equals(type, value.type) && Objects.equals(this.value, value.value);
	}
}
