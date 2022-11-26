package steve6472.scriptit.transformer;

import steve6472.scriptit.Log;
import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.ScriptItSettings;
import steve6472.scriptit.expressions.Function;
import steve6472.scriptit.expressions.FunctionParameters;
import steve6472.scriptit.transformer.parser.config.*;
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

		CACHE.put(Object.class, PrimitiveTypes.ANY_TYPE);
	}

	public static Type generateType(Class<?> clazz, Script script)
	{
		if (!canBeTransformed(script, clazz))
		{
			throw new RuntimeException("Tried to transform denied class '" + clazz.getName() + "'");
		}

		Type cachedType = CACHE.get(clazz);

		if (cachedType != null)
		{
			return cachedType;
		}

		Log.scriptDebug(ScriptItSettings.CLASS_TRANSFORMER_DEBUG, Log.RED + "-".repeat(32));

		Type generatedType = new ClassType(getAliasFromClass(script, clazz), clazz);
		generatedType.createArraySubtype();
		generateConstructors(script, clazz, generatedType);

		Log.scriptDebug(ScriptItSettings.CLASS_TRANSFORMER_DEBUG, "Generated type -> " + generatedType.getKeyword());

		CACHE.put(clazz, generatedType);

		return generatedType;
	}

	public static String generateClassName(String classPath)
	{
		return "generated_" + classPath.replaceAll("\\.", "_").replaceAll("\\$", "___");
	}

	private static void generateConstructors(Script script, Class<?> clazz, Type type)
	{
		for (Constructor<?> constructor : clazz.getConstructors())
		{
			// Do not generate any constructors for abstract classes
			if ((clazz.getModifiers() & Modifier.ABSTRACT) != 0)
			{
				continue;
			}

			if (!canTransformConstructor(script, clazz, constructor))
			{
				Log.scriptDebug(ScriptItSettings.CLASS_TRANSFORMER_IGNORED_DEBUG, "Ignoring disabled constructor " + constructor);
				continue;
			}

			Parameter[] parameters = constructor.getParameters();
			String[] argumentNames = new String[parameters.length];

			FunctionParameters.FunctionParametersBuilder builder = FunctionParameters.function(type.getKeyword());

			for (int i = 0; i < parameters.length; i++)
			{
				argumentNames[i] = parameters[i].getName();
				Log.scriptDebug(ScriptItSettings.CLASS_TRANSFORMER_DEBUG, Log.YELLOW + "\tGenerating argument " + argumentNames[i] + ", " + parameters[i].getType());
				try
				{
					Type type1 = generateType(parameters[i].getType(), script);
					//					generateFunctions(parameters[i].getType(), type1);
					builder.addType(type1);
				} catch (Exception exception)
				{
					throw new RuntimeException(
						"Error thrown when generating types for constructor parameters (" + constructor.getName() + Arrays.toString(constructor.getParameters()) + " of class " + clazz.getName() + ")"
						, exception);
				}
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

						invoke = constructor.newInstance(params);
						return Result.value(transformObject(invoke, script));
					} catch (IllegalAccessException | InvocationTargetException | InstantiationException e)
					{
						throw new RuntimeException(e);
					}
				}
			};

			FunctionParameters build = builder.build();
			type.constructors.put(build, function);
			Log.scriptDebug(ScriptItSettings.CLASS_TRANSFORMER_DEBUG, Log.YELLOW + "\tCreated Constructor for " + build);

		}
	}

	private static void generateFunctions(Class<?> clazz, Type type, Script script)
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

			if (!canTransformMethod(script, clazz, declaredMethod))
			{
				Log.scriptDebug(ScriptItSettings.CLASS_TRANSFORMER_IGNORED_DEBUG, "Ignoring disabled method " + declaredMethod);
				continue;
			}

			Parameter[] parameters = declaredMethod.getParameters();
			String[] argumentNames = new String[parameters.length];

			Log.scriptDebug(ScriptItSettings.CLASS_TRANSFORMER_DEBUG, "Adding function -> " + declaredMethod.getName() + " (" + parameters.length + ")" + declaredMethod.getDeclaringClass());

			FunctionParameters.FunctionParametersBuilder builder = FunctionParameters.function(declaredMethod.getName());

			for (int i = 0; i < parameters.length; i++)
			{
				argumentNames[i] = parameters[i].getName();
				Log.scriptDebug(ScriptItSettings.CLASS_TRANSFORMER_DEBUG, Log.YELLOW + "\tGenerating argument " + argumentNames[i] + ", " + parameters[i].getType());
				try
				{
					Type type1 = generateType(parameters[i].getType(), script);
//					generateFunctions(parameters[i].getType(), type1);
					builder.addType(type1);
				} catch (Exception exception)
				{
					throw new RuntimeException(
						"Error thrown when generating types for method parameters (" + declaredMethod.getName() + Arrays.toString(declaredMethod.getParameters()) + " of class " + clazz.getName() + ")"
						, exception);
				}
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
							return Result.value(transformObject(invoke, script));
						} else {
							return Result.pass();
						}
					} catch (IllegalAccessException | InvocationTargetException e)
					{
						throw new RuntimeException(e);
					}
				}
			};

			FunctionParameters build = builder.build();
			type.addFunction(build, function);
			Log.scriptDebug(ScriptItSettings.CLASS_TRANSFORMER_DEBUG, Log.BLUE + "Created function -> " + build);
		}
	}

	public static Value transformObject(Object object, Script script) throws IllegalAccessException
	{
		Class<?> clazz = object.getClass();

		if (!canBeTransformed(script, clazz))
		{
			throw new RuntimeException("Tried to transform denied class " + clazz.getName());
		}

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
					Value value = transformObject(o, script);
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


		Type type = generateType(clazz, script);

		// custom Value, objects are held, accessed when needed and transformed to value
		JavaValue uv = new JavaValue(script, type);
		uv.setValue("java object", object);
		generateFunctions(clazz, type, script);

		for (Field declaredField : clazz.getDeclaredFields())
		{
			// ignore static
			if ((declaredField.getModifiers() & Modifier.STATIC) != 0)
				continue;

			if ((declaredField.getModifiers() & Modifier.PRIVATE) != 0)
				continue;

			if ((declaredField.getModifiers() & Modifier.TRANSIENT) != 0)
				continue;

			if (!canTransformField(script, clazz, declaredField))
			{
				Log.scriptDebug(ScriptItSettings.CLASS_TRANSFORMER_IGNORED_DEBUG, "Ignoring disabled field " + declaredField);
				continue;
			}

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

	public static void addClasses(Script script)
	{
		for (Config transformerConfig : script.transformerConfigs)
		{
			if (transformerConfig instanceof ClassConfig cc)
			{
				if (cc.isFullyDisabled())
					continue;

				try
				{
					Type generatedType = generateType(Class.forName(cc.path), script);
					script.getMemory().addType(generatedType);
				} catch (ClassNotFoundException e)
				{
					throw new RuntimeException(e);
				}
			}
		}
	}

	public static boolean canBeTransformed(Script script, Class<?> clazz)
	{
		if (CACHE.containsKey(clazz))
			return true;

		for (Config transformerConfig : script.transformerConfigs)
		{
			if (transformerConfig instanceof ClassConfig cc)
			{
				if (cc.path.equals(clazz.getName()))
					return !cc.isFullyDisabled();
			}
		}
		return false;
	}

	public static boolean canTransformField(Script script, Class<?> clazz, Field field)
	{
		for (Config transformerConfig : script.transformerConfigs)
		{
			if (transformerConfig instanceof ClassConfig cc)
			{
				if (cc.path.equals(clazz.getName()))
				{
					if (cc.type == Setting.ALLOW || cc.type == Setting.ALLOW_FIELDS)
						return true;

					for (FieldConfig fieldConfig : cc.fields)
					{
						if (fieldConfig.type.equals(field.getType().getName()) && field.getName().equals(fieldConfig.name))
						{
							return fieldConfig.setting == Setting.ALLOW;
						}
					}

					return false;
				}
			}
		}
		return false;
	}

	public static boolean canTransformMethod(Script script, Class<?> clazz, Method method)
	{
		for (Config transformerConfig : script.transformerConfigs)
		{
			if (transformerConfig instanceof ClassConfig cc)
			{
				if (cc.path.equals(clazz.getName()))
				{
					if (method.getDeclaringClass().equals(Object.class) && cc.objectSetting == Setting.DENY)
						return false;

					if (cc.type == Setting.ALLOW || cc.type == Setting.ALLOW_METHODS)
						return true;

					methods: for (MethodConfig methodConfig : cc.methods)
					{
						if (methodConfig.returnType.equals(method.getReturnType().getName()) && method.getName().equals(methodConfig.name))
						{
							// not the same method
							if (method.getParameters().length != methodConfig.arguments.size())
								continue;

							for (int i = 0; i < method.getParameters().length; i++)
							{
								// Use JAVA_ALIAS to translate int -> java.lang.Integer
								if (!methodConfig.arguments
									.get(i)
									.equals(SchemeParser.JAVA_ALIAS.getOrDefault(method.getParameters()[i]
										.getType()
										.getName(), method.getParameters()[i].getType().getName())))
								{
									continue methods;
								}
							}

							return methodConfig.setting == Setting.ALLOW;
						}
					}

					return false;
				}
			}
		}
		return false;
	}

	public static boolean canTransformConstructor(Script script, Class<?> clazz, Constructor<?> constructor)
	{
		for (Config transformerConfig : script.transformerConfigs)
		{
			if (transformerConfig instanceof ClassConfig cc)
			{
				if (cc.path.equals(clazz.getName()))
				{
					if (cc.type == Setting.ALLOW || cc.type == Setting.ALLOW_METHODS)
						return true;

					methods: for (MethodConfig methodConfig : cc.methods)
					{
						if (clazz.getSimpleName().equals(methodConfig.name))
						{
							// not the same method
							if (constructor.getParameters().length !=  methodConfig.arguments.size())
								continue;

							for (int i = 0; i < constructor.getParameters().length; i++)
							{
								// Use JAVA_ALIAS to translate int -> java.lang.Integer
								if (!methodConfig.arguments
									.get(i)
									.equals(SchemeParser.JAVA_ALIAS.getOrDefault(constructor.getParameters()[i]
										.getType()
										.getName(), constructor.getParameters()[i].getType().getName())))
								{
									continue methods;
								}
							}

							return methodConfig.setting == Setting.ALLOW;
						}
					}

					return false;
				}
			}
		}
		return false;
	}

	private static String getAliasFromClass(Script script, Class<?> clazz)
	{
		for (String s : SchemeParser.JAVA_ALIAS.keySet())
		{
			String s1 = SchemeParser.JAVA_ALIAS.get(s);
			if (s1.equals(clazz.getName()))
				return s;
		}

		for (Config transformerConfig : script.transformerConfigs)
		{
			if (transformerConfig instanceof AliasConfig ac)
			{
				if (ac.getJavaPath().equals(clazz.getName()))
					return ac.getAlias();
			}
		}

		return clazz.getSimpleName();
	}
}
