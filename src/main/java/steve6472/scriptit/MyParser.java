package steve6472.scriptit;

import steve6472.scriptit.expression.Operator;
import steve6472.scriptit.expression.Stack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/13/2021
 * Project: ScriptIt
 *
 ***********************/
public class MyParser
{
	public static final Operator[][] BINARY_PRECENDENCE =
		{
			{
				Operator.MUL, Operator.DIV, Operator.MOD
			},
			{
				Operator.ADD, Operator.SUB
			}
		};

	public static final Operator[] UNARY_OPERATORS =
		{
			Operator.ADD, Operator.SUB
		};

	abstract static class Expression
	{
		abstract double apply(ExpressionExecutor executor);

		abstract void print(int i);
	}

	static class Function extends Expression
	{
		Expression[] arguments;
		IFunction function;
		double delayValue = Double.NaN;

		public Function(Expression[] arguments, IFunction function)
		{
			this.arguments = arguments;
			this.function = function;
		}

		@Override
		double apply(ExpressionExecutor executor)
		{
			double[] args = new double[arguments.length];
			for (int i = 0; i < arguments.length; i++)
			{
				args[i] = arguments[i].apply(executor);
			}

			// If no delay -> get value from function
			if (Double.isNaN(delayValue))
			{
				delayValue = function.apply(executor, args);
				return Double.NaN;
			}
			// If delay -> assing the already calculated value
			else
			{
				double temp = delayValue;
				delayValue = Double.NaN;
				return temp;
			}
		}

		@Override
		void print(int i)
		{
			System.out.println(i + " Function with " + arguments.length + " parameters:");
			for (Expression ex : arguments)
			{
				ex.print(i + 1);
			}
			System.out.println();
		}
	}

	static class Constant extends Expression
	{
		double constant;

		public Constant(double constant)
		{
			this.constant = constant;
		}

		@Override
		double apply(ExpressionExecutor executor)
		{
			return constant;
		}

		@Override
		void print(int i)
		{
			System.out.println(i + " Constant " + constant);
		}

		@Override
		public String toString()
		{
			return "Constant{" + "constant=" + constant + '}';
		}
	}

	static class BinaryOperator extends Expression
	{
		Operator operator;
		Expression left, right;
		double leftVal = Double.NaN;
		double rightVal = Double.NaN;

		public BinaryOperator(Operator operator, Expression left, Expression right)
		{
			this.operator = operator;
			this.left = left;
			this.right = right;
		}

		double apply(ExpressionExecutor executor)
		{
			double lv;
			if (Double.isNaN(leftVal))
			{
				lv = left.apply(executor);
			} else
				lv = leftVal;
			if (Double.isNaN(lv))
			{
				return Double.NaN;
			} else
			{
				leftVal = lv;
			}

			double rv;
			if (Double.isNaN(rightVal))
			{
				rv = right.apply(executor);
			} else
				rv = rightVal;
			if (Double.isNaN(rv))
			{
				return Double.NaN;
			} else
			{
				rightVal = rv;
			}

			return switch (operator)
				{
					case MUL -> lv * rv;
					case DIV -> lv / rv;
					case MOD -> lv % rv;
					case ADD -> lv + rv;
					case SUB -> lv - rv;
					default -> throw new IllegalStateException("Unexpected value: " + operator);
				};
		}

		@Override
		void print(int i)
		{
			left.print(i + 1);
			System.out.println(i + " Operator " + operator);
			right.print(i + 1);
		}

		@Override
		public String toString()
		{
			return "BinaryOperator{" + "operator=" + operator + '}';
		}
	}

	static class UnaryOperator extends Expression
	{
		Operator operator;
		Expression left;

		public UnaryOperator(Operator operator, Expression left)
		{
			this.operator = operator;
			this.left = left;
		}

		double apply(ExpressionExecutor executor)
		{
			return switch (operator)
				{
					case ADD -> +left.apply(executor);
					case SUB -> -left.apply(executor);
					default -> throw new IllegalStateException("Unexpected value: " + operator);
				};
		}

		@Override
		void print(int i)
		{
			System.out.println(i + " Unary " + operator);
			left.print(i + 1);
		}

		@Override
		public String toString()
		{
			return "UnaryOperator{" + "operator=" + operator + '}';
		}
	}

	String line;
	int pos = -1;
	char ch;
	Stack<List<Expression>> functionParameters = new Stack<>(64);

	void nextChar()
	{
		ch = (char) ((++pos < line.length()) ? line.charAt(pos) : -1);
	}

	boolean eat(char charToEat)
	{
		while (ch == ' ' || ch == '\n')
			nextChar();
		if (ch == charToEat)
		{
			nextChar();
			return true;
		}
		return false;
	}

	private Expression next(Memory memory, int i)
	{
		Expression ex = null;
		if (i < BINARY_PRECENDENCE.length)
			ex = next(memory, i + 1);

		if (i == BINARY_PRECENDENCE.length)
		{
			for (Operator op : UNARY_OPERATORS)
			{
				if (eat(op.getOperator().charAt(0)))
				{
					System.out.println("found unary " + op);
					Expression left = next(memory, i);
					return new UnaryOperator(op, left);
				}
			}

			int startPos = pos;
			if (eat('('))
			{
				if (ch == ')')
				{
					nextChar();
					return null;
				} else
				{
					Expression e = next(memory, 0);
					eat(')');
					return e;
				}
			} else if ((ch >= '0' && ch <= '9') || ch == '.')
			{
				while ((ch >= '0' && ch <= '9') || ch == '.')
					nextChar();
				return new Constant(Double.parseDouble(line.substring(startPos, this.pos)));
			} else if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || ch == '_')
			{
				while ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9') || ch == '_')
					nextChar();

				// Next is function
				if (ch == '(')
				{
					String name = line.substring(startPos, this.pos);
					functionParameters.push(new ArrayList<>());

					Expression firstParameter = next(memory, i);

					List<Expression> parameterList = functionParameters.pop();
					if (parameterList == null)
						parameterList = new ArrayList<>();
					if (firstParameter != null)
						parameterList.add(0, firstParameter);
					int argumentCount = parameterList.size();

					IFunction function = memory.getFunction(name, argumentCount);
					return new Function(parameterList.toArray(new Expression[0]), function);
				} else
				{
					throw new IllegalStateException("Soon to be variable");
				}
			}

			// TODO: variables
			else
			{
				System.err.println(ch);
				throw new IllegalStateException("Dafuq");
			}
		}

		for (; ; )
		{
			if (i == 0)
			{
				if (eat(','))
				{
					Expression x = next(memory, 0);
					functionParameters.peek().add(x);
					return ex;
				}
			}

			boolean found = false;

			for (Operator op : BINARY_PRECENDENCE[BINARY_PRECENDENCE.length - 1 - i])
			{
				if (eat(op.getOperator().charAt(0)))
				{
					found = true;
					System.out.println("found binary " + op);
					Expression left = ex;
					Expression right = next(memory, i + 1);
					ex = new BinaryOperator(op, left, right);
					break;
				}
			}

			if (!found)
				return ex;
		}
	}

	public Expression parse(Memory memory)
	{
		nextChar();
		Expression ex = next(memory, 0);
		if (pos < line.length()) throw new RuntimeException("Unexpected: " + ch);
		return ex;
	}

	static class ExpressionExecutor
	{
		Expression fullExpression;
		Memory memory;
		public long delay, delayStart;

		private Supplier<Long> delayStartSupplier = System::currentTimeMillis;
		private BiFunction<Long, Long, Boolean> shouldAdvance = (start, delay) -> System.currentTimeMillis() - start >= delay;

		public void setGetDelayStart(Supplier<Long> delayStartSupplier)
		{
			this.delayStartSupplier = delayStartSupplier;
		}

		public void setShouldAdvance(BiFunction<Long, Long, Boolean> shouldAdvance)
		{
			this.shouldAdvance = shouldAdvance;
		}

		ExpressionExecutor(Memory memory, Expression ex)
		{
			this.memory = memory;
			this.fullExpression = ex;
		}

		public void delay(long delay)
		{
			this.delay = delay;
			delayStart = delayStartSupplier.get();
		}

		public double execute()
		{
			if (delayStart != -1 && !shouldAdvance.apply(delayStart, delay))
				return Double.NaN;

			delayStart = -1;
			return fullExpression.apply(this);
		}
	}

	@FunctionalInterface
	interface IFunction
	{
		double apply(ExpressionExecutor executor, double... args);
	}

	static class Memory
	{
		HashMap<String, HashMap<Integer, IFunction>> functions;

		public Memory()
		{
			this.functions = new HashMap<>();
		}

		public void addFunction(String name, int argumentCount, IFunction function)
		{
			HashMap<Integer, IFunction> funcs = functions.computeIfAbsent(name, k -> new HashMap<>());

			funcs.put(argumentCount, function);
		}

		public IFunction getFunction(String name, int argumentCount)
		{
			HashMap<Integer, IFunction> funcs = functions.get(name);
			if (funcs == null)
				throw new IllegalArgumentException("Function with name '" + name + "' not found");

			IFunction iFunction = funcs.get(argumentCount);

			if (iFunction == null)
				throw new IllegalArgumentException("Function with name '" + name + "' and " + argumentCount + " arguments not found");

			return iFunction;
		}
	}

	public static void main(String[] mainArgs) throws InterruptedException
	{
		Memory memory = new Memory();
		memory.addFunction("pi", 0, (exe, args) -> Math.PI);
		memory.addFunction("delay", 2, (exe, args) ->
		{
			System.out.println("Delay " + args[1]);
			exe.delay((long) args[0]);
			return args[1];
		});
		memory.addFunction("toDeg", 1, (exe, args) -> Math.toDegrees(args[0]));

		MyParser myParser = new MyParser();
//		myParser.line = "((4 - 2 % 3 + 1) * -(3*3+4*4)) / 2";
//		myParser.line = "pi() + interrupt() + toDeg(pi())";
		myParser.line = "1 - 2 * (delay(1000, 3) * -delay(1000, 4)) + 2";
//		myParser.line = "delay(1000, 1) + delay(1000, 2)";
//		myParser.line = "delay(1000, 3)";
//		myParser.line = "toDeg(3.14159265358979323846)";
		ExpressionExecutor exe = new ExpressionExecutor(memory, myParser.parse(memory));
		double ret = Double.NaN;

		while (Double.isNaN(ret))
		{
			ret = exe.execute();
		}
		System.out.println("Final val: " + ret);

//		Constant ONE = new Constant(1);
//		Constant TWO = new Constant(2);
//		Constant THREE = new Constant(3);
//		Constant FOUR = new Constant(4);
//
//		UnaryOperator UNARY_MINUS_FOUR = new UnaryOperator(Operator.SUB, FOUR); // -4
//		BinaryOperator MUL_THREE_MIN_FOUR = new BinaryOperator(Operator.MUL, THREE, UNARY_MINUS_FOUR); // 3 * -4
//		BinaryOperator TWO_MUL__MUL_THREE_MIN_FOUR = new BinaryOperator(Operator.MUL, TWO, MUL_THREE_MIN_FOUR); // 2 * (3 * -4)
//		BinaryOperator ONE_SUB__TWO_MUL__MUL_THREE_MIN_FOUR = new BinaryOperator(Operator.SUB, ONE, TWO_MUL__MUL_THREE_MIN_FOUR); // 1 - 2 * (3 * -4)
//		BinaryOperator ALL_PLUS_TWO = new BinaryOperator(Operator.ADD, ONE_SUB__TWO_MUL__MUL_THREE_MIN_FOUR, TWO); // 1 - 2 * (3 * -4) + 2
//
//		ALL_PLUS_TWO.print(0);
//		System.out.println(ALL_PLUS_TWO.apply(exe));
	}
}
