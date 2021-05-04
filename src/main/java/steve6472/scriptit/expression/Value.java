package steve6472.scriptit.expression;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Value
{
	private static final String SINGLE_VALUE = " ";

	public Type type;
	public Map<String, Object> values;

	public Value(Type type)
	{
		this.type = type;
		this.values = new HashMap<>();
	}

	public Value(Type type, Object value)
	{
		this(type, SINGLE_VALUE, value);
	}

	public Value(Type type, String valueName, Object value)
	{
		this.type = type;
		this.values = new HashMap<>();
		setValue(valueName, value);
	}

	public Value setValue(String name, Object value)
	{
		values.put(name, value);
		return this;
	}

	public Value setValue(Object value)
	{
		values.put(SINGLE_VALUE, value);
		return this;
	}

	public boolean getBoolean(String name)
	{
		return (boolean) values.get(name);
	}

	public int getInt(String name)
	{
		return (int) values.get(name);
	}

	public double getDouble(String name)
	{
		return (double) values.get(name);
	}

	public char getChar(String name)
	{
		return (char) values.get(name);
	}

	public String getString(String name)
	{
		return (String) values.get(name);
	}




	public boolean getBoolean()
	{
		return (boolean) values.get(SINGLE_VALUE);
	}

	public int getInt()
	{
		return (int) values.get(SINGLE_VALUE);
	}

	public double getDouble()
	{
		return (double) values.get(SINGLE_VALUE);
	}

	public char getChar()
	{
		return (char) values.get(SINGLE_VALUE);
	}

	public String getString()
	{
		return (String) values.get(SINGLE_VALUE);
	}

	@Override
	public String toString()
	{
		return "Value{" + "type=" + type + ", values=" + values + '}';
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Value value = (Value) o;
		return Objects.equals(type, value.type) && Objects.equals(values, value.values);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(type, values);
	}
}