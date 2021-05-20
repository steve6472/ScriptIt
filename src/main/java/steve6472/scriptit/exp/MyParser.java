package steve6472.scriptit.exp;

import steve6472.scriptit.expression.Stack;

import java.util.ArrayList;
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

	private String line;
	private int pos = -1;
	private char ch;
	private final Stack<List<Expression>> functionParameters = new Stack<>(64);

	public void setExpression(String expression)
	{
		this.line = expression;
	}

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

				String name = line.substring(startPos, this.pos);
				// Next is function
				if (ch == '(')
				{
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
					return new Variable(name);
				}
			}

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

	public static void main(String[] mainArgs)
	{
		Memory memory = new Memory();
		memory.addVariable("pi", Math.PI);
		memory.addFunction("delay", 2, (exe, args) ->
		{
			System.out.println("Delay " + args[0] + ", ret: " + args[1]);
			exe.delay((long) args[0]);
			return args[1];
		});
		memory.addFunction("toDeg", 1, (exe, args) -> Math.toDegrees(args[0]));

		MyParser myParser = new MyParser();
//		myParser.line = "((4 - 2 % 3 + 1) * -(3*3+4*4)) / 2";
//		myParser.line = "pi() + interrupt() + toDeg(pi())";
//		myParser.line = "1 - 2 * (delay(1000, 3) * -delay(1000, 4)) + 2";
//		myParser.line = "1 + delay(delay(1000, 1000 * 2), 3)";
//		myParser.line = "delay(1000, 1) + delay(1000, 2)";
//		myParser.line = "delay(1000, 3)";
//		myParser.line = "toDeg(3.14159265358979323846)";
		myParser.line = "toDeg(pi / 2)";
		ExpressionExecutor exe = new ExpressionExecutor(memory, myParser.parse(memory));
		double ret = Double.NaN;

		while (Double.isNaN(ret))
		{
			ret = exe.execute();
		}
		System.out.println("Final val: " + ret);
	}
}
