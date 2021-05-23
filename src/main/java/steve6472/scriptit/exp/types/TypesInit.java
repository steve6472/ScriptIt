package steve6472.scriptit.exp.types;

import steve6472.scriptit.exp.Function;
import steve6472.scriptit.exp.Result;
import steve6472.scriptit.exp.Script;
import steve6472.scriptit.exp.Value;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/23/2021
 * Project: ScriptIt
 *
 ***********************/
public class TypesInit
{
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

		protected BinaryOperatorOverload(BinaryOperator func)
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

		protected UnaryOperatorOverload(UnaryOperator func)
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

		protected Constructor(IConstructor constructor)
		{
			this.constructor = constructor;
		}

		@Override
		public Result apply(Script script)
		{
			return Result.value(constructor.construct(arguments));
		}
	}
}
