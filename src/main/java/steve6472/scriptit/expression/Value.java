package steve6472.scriptit.expression;

import steve6472.scriptit.TypeDeclarations;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Value
{
	/**
	 * The value is "PRIMITIVE VALUE" with space, which is (hopefully) impossible to get from the Script
	 */
	public static final String SINGLE_VALUE = "PRIMITIVE VALUE";

	public final boolean isPrimitive;
	public Type type;
	public Map<String, Object> values;

	/**
	 * DOUBLE, INT, STRING, CHAR, BOOL are primitive
	 * @param type type
	 * @return new value with automatically assigned isPrimitive based on type
	 */
	public static Value newValue(Type type)
	{
		return new Value(TypeDeclarations.isPrimitive(type), type);
	}

	public static Value newValue(Type type, Object value)
	{
		return new Value(TypeDeclarations.isPrimitive(type), type, value);
	}

	private Value(boolean isPrimitive, Type type)
	{
		this.isPrimitive = isPrimitive;
		this.type = type;
		this.values = new HashMap<>();
	}

	private Value(boolean isPrimitive, Type type, Object value)
	{
		this(isPrimitive, type, SINGLE_VALUE, value);
	}

	private Value(boolean isPrimitive, Type type, String valueName, Object value)
	{
		this.isPrimitive = isPrimitive;
		this.type = type;
		this.values = new HashMap<>();
		setValue(valueName, value);
	}

	public boolean isPrimitive()
	{
		return isPrimitive;
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

	public Value getValue(String name)
	{
		return (Value) values.get(name);
	}

	public Object get(String name)
	{
		return values.get(name);
	}

	public boolean getBooleanValue(String name)
	{
		return ((Value) values.get(name)).getBoolean();
	}

	public int getIntValue(String name)
	{
		return ((Value) values.get(name)).getInt();
	}

	public double getDoubleValue(String name)
	{
		return ((Value) values.get(name)).getDouble();
	}

	public char getCharValue(String name)
	{
		return ((Value) values.get(name)).getChar();
	}

	public String getStringValue(String name)
	{
		return ((Value) values.get(name)).getString();
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
		if (isPrimitive)
			return "{" + type.getKeyword() + "=" + values.get(SINGLE_VALUE) + "}";
		return "Value{" + "type=" + type.getKeyword() + ", values=" + values + '}';
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