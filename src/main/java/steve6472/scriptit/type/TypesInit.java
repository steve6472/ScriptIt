package steve6472.scriptit.type;

import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.expressions.Function;
import steve6472.scriptit.expressions.FunctionParameters;
import steve6472.scriptit.value.PrimitiveValue;
import steve6472.scriptit.value.UniversalValue;
import steve6472.scriptit.value.Value;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/23/2021
 * Project: ScriptIt
 *
 ***********************/
public class TypesInit
{
	public static final PrimitiveType<PrimitiveValue<Double>> DOUBLE = new PrimitiveType<>("double");
	public static final PrimitiveType<PrimitiveValue<Integer>> INT = new PrimitiveType<>("int");
	public static final PrimitiveType<PrimitiveValue<Boolean>> BOOL = new PrimitiveType<>("bool");
	public static final PrimitiveType<PrimitiveValue<String>> STRING = new PrimitiveType<>("string");
	public static final PrimitiveType<PrimitiveValue<Character>> CHAR = new PrimitiveType<>("char");
	public static final PrimitiveType<PrimitiveValue<Void>> NULL = new PrimitiveType<>("null");

	// Create alias for "any type" parameter... type. . . thingie
	public static final PrimitiveType<PrimitiveValue<Void>> ANY_TYPE = NULL;

	public static <T> PrimitiveValue<T> newPrimitive(PrimitiveType<?> type, T value)
	{
		return PrimitiveValue.newValue(type, value);
	}

	public static <T> PrimitiveValue<T> newPrimitive(T value)
	{
		return PrimitiveValue.newValue(value);
	}

	public static UniversalValue newUniversal(Type type)
	{
		return UniversalValue.newValue(type);
	}

	public static UniversalValue newUniversal(Type type, Object value)
	{
		return UniversalValue.newValue(type, value);
	}



	@FunctionalInterface
	protected interface BinaryOperator
	{
		Value apply(Value left, Value right);
	}

	@FunctionalInterface
	protected interface UnaryOperator
	{
		Value apply(Value left);
	}

	@FunctionalInterface
	protected interface IConstructor
	{
		Value construct(Value[] args);
	}

	protected static class BinaryOperatorOverload extends Function
	{
		private final BinaryOperator func;

		public BinaryOperatorOverload(BinaryOperator func)
		{
			super("left", "right");
			this.func = func;
		}

		@Override
		public Result apply(Script script)
		{
			return Result.value(func.apply(arguments[0], arguments[1]));
		}
	}

	protected static class UnaryOperatorOverload extends Function
	{
		private final UnaryOperator func;

		public UnaryOperatorOverload(UnaryOperator func)
		{
			super("right");
			this.func = func;
		}

		@Override
		public Result apply(Script script)
		{
			return Result.value(func.apply(arguments[0]));
		}
	}

	protected static class Constructor extends Function
	{
		private final IConstructor constructor;

		public Constructor(IConstructor constructor)
		{
			this.constructor = constructor;
		}

		@Override
		public Result apply(Script script)
		{
			return Result.value(constructor.construct(arguments));
		}
	}



	private static final class Func extends Function
	{
		MultiParamFunction func;

		public Func(MultiParamFunction func)
		{
			super((String[]) null);
			this.func = func;
		}

		@Override
		public Result apply(Script script)
		{
			return Result.value(func.apply(returnThisHelper, arguments));
		}
	}

	@FunctionalInterface
	protected interface NoParamFunction
	{
		Value apply(Value itself);
	}

	@FunctionalInterface
	protected interface OneParamFunction
	{
		Value apply(Value itself, Value param1);
	}

	@FunctionalInterface
	protected interface TwoParamFunction
	{
		Value apply(Value itself, Value param1, Value param2);
	}

	@FunctionalInterface
	protected interface ThreeParamFunction
	{
		Value apply(Value itself, Value param1, Value param2, Value param3);
	}

	@FunctionalInterface
	protected interface FourParamFunction
	{
		Value apply(Value itself, Value param1, Value param2, Value param3, Value param4);
	}

	@FunctionalInterface
	protected interface FiveParamFunction
	{
		Value apply(Value itself, Value param1, Value param2, Value param3, Value param4, Value param5);
	}

	@FunctionalInterface
	protected interface SixParamFunction
	{
		Value apply(Value itself, Value param1, Value param2, Value param3, Value param4, Value param5, Value param6);
	}

	@FunctionalInterface
	protected interface SevenParamFunction
	{
		Value apply(Value itself, Value param1, Value param2, Value param3, Value param4, Value param5, Value param6, Value param7);
	}

	@FunctionalInterface
	protected interface EightParamFunction
	{
		Value apply(Value itself, Value param1, Value param2, Value param3, Value param4, Value param5, Value param6, Value param7, Value param8);
	}

	@FunctionalInterface
	protected interface NineParamFunction
	{
		Value apply(Value itself, Value param1, Value param2, Value param3, Value param4, Value param5, Value param6, Value param7, Value param8, Value param9);
	}

	@FunctionalInterface
	protected interface MultiParamFunction
	{
		Value apply(Value itself, Value... params);
	}

	public static void addFunction(Type type, String name, NoParamFunction function)
	{
		type.addFunction(FunctionParameters.create(name), new Func((itself, args) -> function.apply(itself)));
	}

	public static void addFunction(Type type, String name, OneParamFunction function, Type... types)
	{
		type.addFunction(FunctionParameters.create(name, types), new Func((itself, args) -> function.apply(itself, args[0])));
	}

	public static void addFunction(Type type, String name, TwoParamFunction function, Type... types)
	{
		type.addFunction(FunctionParameters.create(name, types), new Func((itself, args) -> function.apply(itself, args[0], args[1])));
	}

	public static void addFunction(Type type, String name, ThreeParamFunction function, Type... types)
	{
		type.addFunction(FunctionParameters.create(name, types), new Func((itself, args) -> function.apply(itself, args[0], args[1], args[2])));
	}

	public static void addFunction(Type type, String name, FourParamFunction function, Type... types)
	{
		type.addFunction(FunctionParameters.create(name, types), new Func((itself, args) -> function.apply(itself, args[0], args[1], args[2], args[3])));
	}

	public static void addFunction(Type type, String name, FiveParamFunction function, Type... types)
	{
		type.addFunction(FunctionParameters.create(name, types), new Func((itself, args) -> function.apply(itself, args[0], args[1], args[2], args[3], args[4])));
	}

	public static void addFunction(Type type, String name, SixParamFunction function, Type... types)
	{
		type.addFunction(FunctionParameters.create(name, types), new Func((itself, args) -> function.apply(itself, args[0], args[1], args[2], args[3], args[4], args[5])));
	}

	public static void addFunction(Type type, String name, SevenParamFunction function, Type... types)
	{
		type.addFunction(FunctionParameters.create(name, types), new Func((itself, args) -> function.apply(itself, args[0], args[1], args[2], args[3], args[4], args[5], args[6])));
	}

	public static void addFunction(Type type, String name, EightParamFunction function, Type... types)
	{
		type.addFunction(FunctionParameters.create(name, types), new Func((itself, args) -> function.apply(itself, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7])));
	}

	public static void addFunction(Type type, String name, NineParamFunction function, Type... types)
	{
		type.addFunction(FunctionParameters.create(name, types), new Func((itself, args) -> function.apply(itself, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8])));
	}

	public static void addFunctionM(Type type, String name, MultiParamFunction function, Type... types)
	{
		type.addFunction(FunctionParameters.create(name, types), new Func(function));
	}


	private static final class Proc extends Function
	{
		MultiParamProcedure proc;

		public Proc(MultiParamProcedure proc)
		{
			super((String[]) null);
			this.proc = proc;
		}

		@Override
		public Result apply(Script script)
		{
			proc.apply(returnThisHelper, arguments);
			return Result.pass();
		}
	}

	@FunctionalInterface
	protected interface NoParamProcedure
	{
		void apply(Value itself);
	}

	@FunctionalInterface
	protected interface OneParamProcedure
	{
		void apply(Value itself, Value param1);
	}

	@FunctionalInterface
	protected interface TwoParamProcedure
	{
		void apply(Value itself, Value param1, Value param2);
	}

	@FunctionalInterface
	protected interface ThreeParamProcedure
	{
		void apply(Value itself, Value param1, Value param2, Value param3);
	}

	@FunctionalInterface
	protected interface FourParamProcedure
	{
		void apply(Value itself, Value param1, Value param2, Value param3, Value param4);
	}

	@FunctionalInterface
	protected interface FiveParamProcedure
	{
		void apply(Value itself, Value param1, Value param2, Value param3, Value param4, Value param5);
	}

	@FunctionalInterface
	protected interface SixParamProcedure
	{
		void apply(Value itself, Value param1, Value param2, Value param3, Value param4, Value param5, Value param6);
	}

	@FunctionalInterface
	protected interface SevenParamProcedure
	{
		void apply(Value itself, Value param1, Value param2, Value param3, Value param4, Value param5, Value param6, Value param7);
	}

	@FunctionalInterface
	protected interface EightParamProcedure
	{
		void apply(Value itself, Value param1, Value param2, Value param3, Value param4, Value param5, Value param6, Value param7, Value param8);
	}

	@FunctionalInterface
	protected interface NineParamProcedure
	{
		void apply(Value itself, Value param1, Value param2, Value param3, Value param4, Value param5, Value param6, Value param7, Value param8, Value param9);
	}

	@FunctionalInterface
	protected interface MultiParamProcedure
	{
		void apply(Value itself, Value... params);
	}

	public static void addProcedure(Type type, String name, NoParamProcedure procedure)
	{
		type.addFunction(FunctionParameters.create(name), new Proc((itself, args) -> procedure.apply(itself)));
	}

	public static void addProcedure(Type type, String name, OneParamProcedure procedure, Type... types)
	{
		type.addFunction(FunctionParameters.create(name, types), new Proc((itself, args) -> procedure.apply(itself, args[0])));
	}

	public static void addProcedure(Type type, String name, TwoParamProcedure procedure, Type... types)
	{
		type.addFunction(FunctionParameters.create(name, types), new Proc((itself, args) -> procedure.apply(itself, args[0], args[1])));
	}

	public static void addProcedure(Type type, String name, ThreeParamProcedure procedure, Type... types)
	{
		type.addFunction(FunctionParameters.create(name, types), new Proc((itself, args) -> procedure.apply(itself, args[0], args[1], args[2])));
	}

	public static void addProcedure(Type type, String name, FourParamProcedure procedure, Type... types)
	{
		type.addFunction(FunctionParameters.create(name, types), new Proc((itself, args) -> procedure.apply(itself, args[0], args[1], args[2], args[3])));
	}

	public static void addProcedure(Type type, String name, FiveParamProcedure procedure, Type... types)
	{
		type.addFunction(FunctionParameters.create(name, types), new Proc((itself, args) -> procedure.apply(itself, args[0], args[1], args[2], args[3], args[4])));
	}

	public static void addProcedure(Type type, String name, SixParamProcedure procedure, Type... types)
	{
		type.addFunction(FunctionParameters.create(name, types), new Proc((itself, args) -> procedure.apply(itself, args[0], args[1], args[2], args[3], args[4], args[5])));
	}

	public static void addProcedure(Type type, String name, SevenParamProcedure procedure, Type... types)
	{
		type.addFunction(FunctionParameters.create(name, types), new Proc((itself, args) -> procedure.apply(itself, args[0], args[1], args[2], args[3], args[4], args[5], args[6])));
	}

	public static void addProcedure(Type type, String name, EightParamProcedure procedure, Type... types)
	{
		type.addFunction(FunctionParameters.create(name, types), new Proc((itself, args) -> procedure.apply(itself, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7])));
	}

	public static void addProcedure(Type type, String name, NineParamProcedure procedure, Type... types)
	{
		type.addFunction(FunctionParameters.create(name, types), new Proc((itself, args) -> procedure.apply(itself, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8])));
	}

	public static void addFProcedure(Type type, String name, MultiParamProcedure procedure, Type... types)
	{
		type.addFunction(FunctionParameters.create(name, types), new Proc(procedure));
	}

	/*
	 * Primitive specific
	 */

	public static class PBinaryOperatorOverload<Left extends Value, Right extends Value> extends Function
	{
		private final PrimitiveType.BinaryOperator<Left, Right> func;

		public PBinaryOperatorOverload(PrimitiveType.BinaryOperator<Left, Right> func)
		{
			super("left", "right");
			this.func = func;
		}

		@Override
		@SuppressWarnings(value = "unchecked")
		public Result apply(Script script)
		{
			return Result.value(func.apply((Left) arguments[0], (Right) arguments[1]));
		}
	}

	public static class PUnaryOperatorOverload<Right extends Value> extends Function
	{
		private final PrimitiveType.UnaryOperator<Right> func;

		public PUnaryOperatorOverload(PrimitiveType.UnaryOperator<Right> func)
		{
			super("right");
			this.func = func;
		}

		@Override
		@SuppressWarnings(value = "unchecked")
		public Result apply(Script script)
		{
			return Result.value(func.apply((Right) arguments[0]));
		}
	}
}
