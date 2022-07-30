package steve6472.scriptit.value;

import steve6472.scriptit.Memory;
import steve6472.scriptit.type.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UniversalValue extends Value
{
	/**
	 * The value is "PRIMITIVE VALUE" with space, which is (hopefully) impossible to get from the Script
	 */
	public static final String SINGLE_VALUE = "PRIMITIVE VALUE";

	public Map<String, Object> values;

	/**
	 * DOUBLE, INT, STRING, CHAR, BOOL are primitive
	 * @param type type
	 * @return new value with automatically assigned isPrimitive based on type
	 */
	public static UniversalValue newValue(Type type)
	{
		return new UniversalValue(type);
	}

	public static UniversalValue newValue(Type type, Object value)
	{
		return new UniversalValue(type, value);
	}

	protected UniversalValue(Type type)
	{
		super(type);
		this.values = new HashMap<>();
	}

	@Override
	public void clear()
	{
		values.clear();
	}

	@Override
	public void setFrom(Value other)
	{
		if (!(other instanceof UniversalValue uv))
			throw new RuntimeException("Can not set to UniversalValue from %s".formatted(other.getClass().getSimpleName()));
		uv.values.forEach(this::setValue);
	}

	@Override
	public void addValuesToMemory(Memory memory)
	{
		values.forEach((k, v) -> memory.addVariable(k, (Value) v));
	}

	protected UniversalValue(Type type, Object value)
	{
		this(type, SINGLE_VALUE, value);
	}

	protected UniversalValue(Type type, String valueName, Object value)
	{
		super(type);
		this.values = new HashMap<>();
		this.values.put(valueName, value);
	}

	public UniversalValue setValue(String name, Object value)
	{
		values.put(name, value);
		return this;
	}

	public UniversalValue setSingle(Object value)
	{
		return setValue(SINGLE_VALUE, value);
	}

	public Object getSingle()
	{
		return values.get(SINGLE_VALUE);
	}

	@Override
	public Value getValueByName(String name)
	{
		return getValue(name);
	}

	@Override
	public void setValueByName(String name, Value value)
	{
		setValue(name, value);
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
		return (boolean) values.get(name);
	}

	public int getIntValue(String name)
	{
		return (int) values.get(name);
	}

	public double getDoubleValue(String name)
	{
		return (double) values.get(name);
	}

	public char getCharValue(String name)
	{
		return (char) values.get(name);
	}

	public String getStringValue(String name)
	{
		return (String) values.get(name);
	}

	@Override
	public <T> PrimitiveValue<T> asPrimitive()
	{
		throw new RuntimeException("Unsopported Operation (Universal Value)");
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "{" + "type=" + type.getKeyword() + ", values=" + values + '}';
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UniversalValue value = (UniversalValue) o;
		return Objects.equals(type, value.type) && Objects.equals(values, value.values);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(type, values);
	}
}