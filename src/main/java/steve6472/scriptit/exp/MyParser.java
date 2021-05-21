package steve6472.scriptit.exp;

import steve6472.scriptit.expression.Stack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/13/2021
 * Project: ScriptIt
 *
 ***********************/
public class MyParser
{
	private static final Pattern VARIABLE_NAME = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*");

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

	public MyParser setExpression(String expression)
	{
		pos = -1;
		this.line = expression;
		return this;
	}

	void nextChar()
	{
		ch = (char) ((++pos < line.length()) ? line.charAt(pos) : -1);
	}

	void nextChar(int l)
	{
		for (int i = 0; i < l; i++)
		{
			ch = (char) ((++pos < line.length()) ? line.charAt(pos) : -1);
		}
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
			if (line.substring(pos).startsWith("return"))
			{
				nextChar(6);
				Expression next = next(memory, 0);
				return new Return(next);
			}

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
				boolean foundDot = false;
				while ((ch >= '0' && ch <= '9') || ch == '.')
				{
					if (ch == '.')
						foundDot = true;
					nextChar();
				}
				if (foundDot)
				{
					return new Constant(PrimitiveTypes.DOUBLE, Double.parseDouble(line.substring(startPos, this.pos)));
				} else
				{
					return new Constant(PrimitiveTypes.INT, Integer.parseInt(line.substring(startPos, this.pos)));
				}
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
						parameterList.add(firstParameter);

					Collections.reverse(parameterList);

					return new FunctionCall(name, parameterList.toArray(new Expression[0]));

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
				int start = pos;

				if (eat('='))
				{
					String varName = line.substring(0, start).trim();
					if (!VARIABLE_NAME.matcher(varName).matches())
					{
						throw new IllegalArgumentException("Variable name '" + varName + "' is invalid");
					}

					return new Assignment(varName, next(memory, 0));
				}
				else if (eat(','))
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
}
