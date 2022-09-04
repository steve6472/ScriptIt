package steve6472.scriptit.transformer;

import steve6472.scriptit.Log;
import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.ScriptItSettings;
import steve6472.scriptit.expressions.Function;
import steve6472.scriptit.expressions.FunctionParameters;
import steve6472.scriptit.type.ClassType;
import steve6472.scriptit.type.PrimitiveTypes;
import steve6472.scriptit.type.Type;
import steve6472.scriptit.value.PrimitiveValue;
import steve6472.scriptit.value.UniversalValue;
import steve6472.scriptit.value.Value;

import java.lang.reflect.*;
import java.util.*;

/**
 * Created by steve6472
 * Date: 8/25/2022
 * Project: ScriptIt
 */
public class JavaTransformer
{
	public static final Map<Class<?>, Type> CACHE = new HashMap<>();
	public static final Set<Type> FUNCTION_GENERATED = new HashSet<>();
//	{
//		@Override
//		public Type put(Class<?> key, Type value)
//		{
//			System.out.println(Log.BLUE + "Putting " + key + " -> " + value + Log.RESET);
//			return super.put(key, value);
//		}
//	};

	static
	{
		initCache();
	}

	public static void initCache()
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

		Type generatedType = new ClassType(generateClassName(clazz.getName()), clazz);
		generatedType.createArraySubtype();

		Log.scriptDebug(ScriptItSettings.CLASS_TRANSFORMER_DEBUG, "");
		Log.scriptDebug(ScriptItSettings.CLASS_TRANSFORMER_DEBUG, "");
		Log.scriptDebug(ScriptItSettings.CLASS_TRANSFORMER_DEBUG, "Generated type -> " + generatedType.getKeyword());

		CACHE.put(clazz, generatedType);

		return generatedType;
	}

	public static String generateClassName(String classPath)
	{
		return "generated_" + classPath.replaceAll("\\.", "_").replaceAll("\\$", "___");
	}

	private static void generateFunctions(Class<?> clazz, Type type)
	{
		if (FUNCTION_GENERATED.contains(type))
			return;

		FUNCTION_GENERATED.add(type);

		Log.scriptDebug(ScriptItSettings.CLASS_TRANSFORMER_DEBUG, "Generating functions for -> " + type.getKeyword());
		main: for (Method declaredMethod : clazz.getMethods())
		{
//			for (Method method : Object.class.getDeclaredMethods())
//			{
//				if (declaredMethod.getName().equals(method.getName()) && Arrays.equals(declaredMethod.getParameters(), method.getParameters()))
//				{
//					continue main;
//				}
//			}

			Parameter[] parameters = declaredMethod.getParameters();
			String[] argumentNames = new String[parameters.length];

			Log.scriptDebug(ScriptItSettings.CLASS_TRANSFORMER_DEBUG, "Adding function -> " + declaredMethod.getName() + " (" + parameters.length + ")");

			FunctionParameters.FunctionParametersBuilder builder = FunctionParameters.function(declaredMethod.getName());

			for (int i = 0; i < parameters.length; i++)
			{
				argumentNames[i] = parameters[i].getName();
				Log.scriptDebug(ScriptItSettings.CLASS_TRANSFORMER_DEBUG, Log.YELLOW + "\tGenerating argument " + argumentNames[i] + ", " + parameters[i].getType());
				Type type1 = generateType(parameters[i].getType());
//				generateFunctions(parameters[i].getType(), type1);
				builder.addType(type1);
			}

			Log.scriptDebug(ScriptItSettings.CLASS_TRANSFORMER_DEBUG, "");

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

			type.addFunction(builder.build(), function);
		}
	}

	public static Value transformObject(Object object) throws IllegalAccessException
	{
		Class<?> clazz = object.getClass();
		Log.scriptDebug(ScriptItSettings.CLASS_TRANSFORMER_DEBUG, "");
		Log.scriptDebug(ScriptItSettings.CLASS_TRANSFORMER_DEBUG, "Transforming -> " + clazz.getName());

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

		// custom Value, objects are held, accessed when needed and transformed to value
		JavaValue uv = new JavaValue(type);
		uv.setValue("java object", object);
		generateFunctions(clazz, type);

		for (Field declaredField : clazz.getDeclaredFields())
		{
			// ignore static
			if ((declaredField.getModifiers() & Modifier.STATIC) != 0)
				continue;

			if ((declaredField.getModifiers() & Modifier.PRIVATE) != 0)
				continue;

			if ((declaredField.getModifiers() & Modifier.TRANSIENT) != 0)
				continue;

			try
			{
				Object o = declaredField.get(object);

				if (uv.getValue(declaredField.getName()) != null)
					continue;

				if (o == null)
				{
					uv.setValue(declaredField.getName(), null);
					Log.scriptDebug(ScriptItSettings.CLASS_TRANSFORMER_DEBUG, "Adding field -> " + declaredField.getName() + " -> null");
				} else
				{
					uv.setValue(declaredField.getName(), o);
					Log.scriptDebug(ScriptItSettings.CLASS_TRANSFORMER_DEBUG, "Adding field -> " + declaredField.getName() + " -> " + o);
				}
			} catch (IllegalAccessException ignored)
			{

			}
		}

		return uv;
	}
}
