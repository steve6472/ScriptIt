package steve6472.scriptit;

import steve6472.scriptit.attributes.AttributeJavaParamInjector;
import steve6472.scriptit.expressions.Function;
import steve6472.scriptit.expressions.FunctionParameters;
import steve6472.scriptit.libraries.LogLibrary;
import steve6472.scriptit.libraries.TestLibrary;
import steve6472.scriptit.type.PrimitiveTypes;
import steve6472.scriptit.type.Type;
import steve6472.scriptit.value.PrimitiveValue;
import steve6472.scriptit.value.UniversalValue;
import steve6472.scriptit.value.Value;

import java.io.File;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by steve6472
 * Date: 8/25/2022
 * Project: ScriptIt
 */
public class ClassTransformer
{
	private static final Map<Class<?>, Type> CACHE = new HashMap<>();

	static
	{
		CACHE.put(int.class, PrimitiveTypes.INT);
		CACHE.put(double.class, PrimitiveTypes.DOUBLE);
		CACHE.put(String.class, PrimitiveTypes.STRING);
		CACHE.put(boolean.class, PrimitiveTypes.BOOL);
		CACHE.put(char.class, PrimitiveTypes.CHAR);

		CACHE.put(Integer.class, PrimitiveTypes.INT);
		CACHE.put(Double.class, PrimitiveTypes.DOUBLE);
		CACHE.put(Boolean.class, PrimitiveTypes.BOOL);
		CACHE.put(Character.class, PrimitiveTypes.CHAR);

		CACHE.put(int[].class, PrimitiveTypes.INT.getArraySubtype());
		CACHE.put(double[].class, PrimitiveTypes.DOUBLE.getArraySubtype());
		CACHE.put(String[].class, PrimitiveTypes.STRING.getArraySubtype());
		CACHE.put(boolean[].class, PrimitiveTypes.BOOL.getArraySubtype());
		CACHE.put(char[].class, PrimitiveTypes.CHAR.getArraySubtype());

		CACHE.put(Integer[].class, PrimitiveTypes.INT.getArraySubtype());
		CACHE.put(Double[].class, PrimitiveTypes.DOUBLE.getArraySubtype());
		CACHE.put(Boolean[].class, PrimitiveTypes.BOOL.getArraySubtype());
		CACHE.put(Character[].class, PrimitiveTypes.CHAR.getArraySubtype());
	}

	public static Type generateType(Class<?> clazz)
	{
		Type cachedType = CACHE.get(clazz);

		if (cachedType != null)
		{
			return cachedType;
		}

		Type generatedType = new Type(generateClassName(clazz.getName()));
		generatedType.createArraySubtype();

		CACHE.put(clazz, generatedType);

		return generatedType;
	}

	public static String generateClassName(String classPath)
	{
		return "generated_" + classPath.replaceAll("\\.", "_").replaceAll("\\$", "___");
	}

	private static void generateFunctions(Class<?> clazz, Type type, UniversalValue value)
	{
		for (Method declaredMethod : clazz.getDeclaredMethods())
		{
			Parameter[] parameters = declaredMethod.getParameters();
			String[] argumentNames = new String[parameters.length];

			FunctionParameters.FunctionParametersBuilder builder = FunctionParameters.function(declaredMethod.getName());

			for (int i = 0; i < parameters.length; i++)
			{
				argumentNames[i] = parameters[i].getName();
				builder.addType(generateType(parameters[i].getType()));
			}

			Function function = new Function(argumentNames)
			{
				@Override
				public Result apply(Script script)
				{
					Object invoke;
					try
					{
						Object[] params = new Object[argumentNames.length];

						for (int i = 0; i < arguments.length; i++)
						{
							Value argument = arguments[i];
							if (argument instanceof PrimitiveValue<?> pv)
								params[i] = pv.get();
							if (argument instanceof UniversalValue pv)
								params[i] = pv.get("java object");
						}

						invoke = declaredMethod.invoke(returnThisHelper.asUniversal().get("java object"), params);
						if (invoke != null)
						{
							return Result.value(transformObject(invoke));
						} else {
							return Result.pass();
						}
					} catch (IllegalAccessException | InvocationTargetException e)
					{
						throw new RuntimeException(e);
					}
				}
			};
			function.setReturnThisHelper(value);

			type.addFunction(builder.build(), function);
		}
	}

	public static Value transformObject(Object object) throws IllegalAccessException
	{
		Class<?> clazz = object.getClass();

		if (clazz == Integer.class) return PrimitiveTypes.INT.newValue(object);
		if (clazz == Double.class) return PrimitiveTypes.DOUBLE.newValue(object);
		if (clazz == String.class) return PrimitiveTypes.STRING.newValue(object);
		if (clazz == Boolean.class) return PrimitiveTypes.BOOL.newValue(object);
		if (clazz == Character.class) return PrimitiveTypes.CHAR.newValue(object);
		if (clazz == Float.class) return PrimitiveTypes.DOUBLE.newValue(((Float) object).doubleValue());

		if (clazz.isArray())
		{
			int length = Array.getLength(object);
			List<Value> arr = new ArrayList<>();

			for (int i = 0; i < length; i++)
			{
				Object o = Array.get(object, i);
				if (o == null)
				{
					arr.add(Value.NULL);
				} else
				{
					Value value = transformObject(o);
					arr.add(value);
				}
			}

			Value array = null;
			if (clazz.getComponentType() == Integer.class || clazz.getComponentType() == int.class) array = PrimitiveTypes.INT.createArray(arr);
			if (clazz.getComponentType() == Double.class || clazz.getComponentType() == double.class) array = PrimitiveTypes.DOUBLE.createArray(arr);
			if (clazz.getComponentType() == String.class) array = PrimitiveTypes.STRING.createArray(arr);
			if (clazz.getComponentType() == Boolean.class || clazz.getComponentType() == boolean.class) array = PrimitiveTypes.BOOL.createArray(arr);
			if (clazz.getComponentType() == Character.class || clazz.getComponentType() == char.class) array = PrimitiveTypes.CHAR.createArray(arr);
			if (clazz.getComponentType() == Float.class || clazz.getComponentType() == float.class) array = PrimitiveTypes.DOUBLE.createArray(arr);

			return array;
		}


		Type type = generateType(clazz);
		UniversalValue uv = UniversalValue.newValue(type);
		uv.setValue("java object", object);
		generateFunctions(clazz, type, uv);

		for (Field declaredField : clazz.getDeclaredFields())
		{
			declaredField.setAccessible(true);
			Object o = declaredField.get(object);
			if (o == null)
			{
				uv.setValue(declaredField.getName(), Value.NULL);
			} else
			{
				uv.setValue(declaredField.getName(), transformObject(o));
			}
		}

		return uv;
	}

	public static void main(String[] args) throws IllegalAccessException
	{
		Foo foo = new Foo();
		foo.setCount(5);
		foo.fancy = false;
		foo.bar = new Bar();
		foo.bar.setName("James");

		PrimitiveTypes.TRUE();

		Value value = transformObject(foo);

		if (value == null)
		{
			System.out.println("NULL");
			return;
		}

		System.out.println("");

		System.out.println(value.type);
		if (value instanceof UniversalValue universal)
			universal.values.forEach((k, v) -> System.out.println("\t" + k + " -> " + v));
		else
			System.out.println(value);
		System.out.println("Binary: ");
		value.type.binary.forEach((a, b) -> System.out.println("\t" + a + " -> " + b));
		System.out.println("Unary: ");
		value.type.unary.forEach((a, b) -> System.out.println("\t" + a + " -> " + b));
		System.out.println("Constructors: ");
		value.type.constructors.forEach((a, b) -> System.out.println("\t" + a + " -> " + b));
		System.out.println("Functions: ");
		value.type.functions.forEach((a, b) -> System.out.println("\t" + a + " -> " + b));

		Script script = testScript("!object_transformer");
		script.getMemory().addVariable("foo", value);
		script.runWithDelay();
	}

	private static Script testScript(String name)
	{
		return testScript(name, new Workspace());
	}

	private static Script testScript(String name, Workspace workspace)
	{
		ScriptItSettings.ALLOW_UNSAFE = true;
		workspace.addLibrary(new TestLibrary());
		workspace.addLibrary(new LogLibrary());
		workspace.addAttribute(new AttributeJavaParamInjector());
		Script script = Script.create(workspace, new File("scripts/" + name + ".scriptit"));
		Highlighter.basicHighlight();
		System.out.println(script.showCode() + "\n");
		return script;
	}

	static class Foo
	{
		int count;
		boolean fancy;
		Bar bar;

		public int getCount()
		{
			return count;
		}

		public void setCount(int count)
		{
			this.count = count;
		}
	}

	static class Bar
	{
		String name;

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}
	}
}
