package steve6472.scriptit;

import steve6472.scriptit.expression.Operator;
import steve6472.scriptit.expression.Stack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

		public Function(Expression[] arguments, IFunction function)
		{
			this.arguments = arguments;
			this.function = function;
		}

		@Override
		double apply(ExpressionExecutor executor)
		{
			executor.last = this;

			double[] args = new double[arguments.length];
			for (int i = 0; i < arguments.length; i++)
			{
				args[i] = arguments[i].apply(executor);
			}
			return function.apply(executor, args);
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
			executor.last = this;

			return constant;
		}

		@Override
		void print(int i)
		{
			System.out.println(i + " Constant " + constant);
		}
	}

	static class BinaryOperator extends Expression
	{
		Operator operator;
		Expression left, right;

		public BinaryOperator(Operator operator, Expression left, Expression right)
		{
			this.operator = operator;
			this.left = left;
			this.right = right;
		}

		double apply(ExpressionExecutor ex)
		{
			ex.last = this;

			return switch (operator)
				{
					case MUL -> left.apply(ex) * right.apply(ex);
					case DIV -> left.apply(ex) / right.apply(ex);
					case MOD -> left.apply(ex) % right.apply(ex);
					case ADD -> left.apply(ex) + right.apply(ex);
					case SUB -> left.apply(ex) - right.apply(ex);
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
			executor.last = this;

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
		if (i == 0)
		{
			if (eat(','))
			{
				if (functionParameters.isEmpty())
					throw new RuntimeException("Parameter separator found outside of function");

				Expression a = next(memory, 0);
				functionParameters.peek().add(a);
				return next(memory, i + 1);
			}
		}

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
					Expression ex = next(memory, 0);
					eat(')');
					return ex;
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

//					System.out.println("Found function " + name);

					Expression firstParameter = next(memory, i);

//					functionParameters.printStack();
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

		Expression ex = next(memory, i + 1);

		for (; ;)
		{
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
		Expression last;
		Memory memory;
		public int skipCount = 0;

		ExpressionExecutor(Memory memory, Expression ex)
		{
			this.memory = memory;
			this.fullExpression = ex;
			this.last = fullExpression;
		}

		public void interrupt()
		{
			skipCount++;
		}

		public void next()
		{

		}

		public double execute()
		{
			last.print(0);
			if (skipCount > 0)
				skipCount--;
			return last.apply(this);
		}

		public boolean canContinue()
		{
			if (skipCount > 0)
			{
				System.out.println("Skipping");
				skipCount--;
				return false;
			}
			return true;
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

	public static void main(String[] mainArgs)
	{
		Memory memory = new Memory();
		memory.addFunction("pi", 0, (exe, args) -> Math.PI);
		memory.addFunction("interrupt", 1, (exe, args) ->
		{
			System.out.println("Interrupted");
			exe.interrupt();
			return args[0];
		});
		memory.addFunction("toDeg", 1, (exe, args) -> Math.toDegrees(args[0]));

		MyParser myParser = new MyParser();
//		myParser.line = "((4 - 2 % 3 + 1) * -(3*3+4*4)) / 2";
//		myParser.line = "pi() + interrupt() + toDeg(pi())";
		myParser.line = "1 - 2 * (3 * -4) + 2";
		ExpressionExecutor parse = new ExpressionExecutor(memory, myParser.parse(memory));
		System.out.println(parse.execute());
//		System.out.println(parse.execute());
//		System.out.println(parse.execute());
//		System.out.println(parse.execute());
//		System.out.println(parse.execute());
	}
}
