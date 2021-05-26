package steve6472.scriptit;

import steve6472.scriptit.types.PrimitiveTypes;

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
	public static boolean DEBUG = false;

	private static final Pattern RETURN_THIS = Pattern.compile("^return\s+this");

	/**
	 * https://en.cppreference.com/w/cpp/language/operator_precedence
	 */
	public static final Operator[][] BINARY_PRECENDENCE =
		{
			{
				Operator.DOT
			},
			{
				Operator.MUL, Operator.DIV, Operator.MOD
			},
			{
				Operator.ADD, Operator.SUB
			},
			{
				Operator.LSH, Operator.RSH
			},
			{
				Operator.LESS_THAN, Operator.GREATER_THAN, Operator.LESS_THAN_EQUAL, Operator.GREATER_THAN_EQUAL
			},
			{
				Operator.EQUAL, Operator.NOT_EQUAL
			},
			{
				Operator.BIT_AND
			},
			{
				Operator.BIT_XOR
			},
			{
				Operator.BIT_OR
			},
			{
				Operator.AND
			},
			{
				Operator.OR
			},
			{
				Operator.ASSIGN
			}
		};

	public static final Operator[] UNARY_OPERATORS =
		{
			Operator.ADD, Operator.SUB, Operator.NOT, Operator.NEG
		};

	private String line;
	private int pos = -1;
	private char ch;
	private final Stack<List<Expression>> functionParameters = new Stack<>(64);

	public MyParser setExpression(String expression)
	{
		pos = -1;
		functionParameters.clear();
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
		while (Character.isWhitespace(ch))
			nextChar();
		if (ch == charToEat)
		{
			if (pos < line.length() - 1)
			{
				char next = line.charAt(pos + 1);
				for (Character secondChar : Operator.secondChars)
				{
					if (secondChar.equals(next))
						return false;
				}
			}
			nextChar();
			return true;
		}
		return false;
	}

	boolean eat(char charToEat1, char charToEat2)
	{
		while (ch == ' ' || ch == '\n')
			nextChar();
		if (ch == charToEat1 && pos < line.length() && line.charAt(pos + 1) == charToEat2)
		{
			nextChar();
			nextChar();
			return true;
		}
		return false;
	}

	boolean eat(String s)
	{
		if (s.length() == 1)
		{
			return eat(s.charAt(0));
		} else
		{
			return eat(s.charAt(0), s.charAt(1));
		}
	}

	private Expression next(int i)
	{
		Expression ex = null;

		if (i < BINARY_PRECENDENCE.length)
			ex = next(i + 1);

		if (i == BINARY_PRECENDENCE.length)
		{
			if (line.substring(pos).startsWith("return"))
			{
				if (RETURN_THIS.matcher(line).matches())
				{
					pos = line.length();
					return new ReturnThis();
				} else
				{
					nextChar(6);
					if (line.substring(pos).isBlank())
					{
						return new Return(new Expression()
						{
							@Override
							public Result apply(Script script)
							{
								return Result.return_();
							}
						});
					} else
					{
						Expression next = next(0);
						return new Return(next);
					}
				}
			}

			if (line.substring(pos).startsWith("import"))
			{
				String[] split = line.split("\s");
				pos = line.length();
				if (split[1].equals("type"))
					return new Import(ImportType.TYPE, split[2]);
				else if (split[1].equals("library"))
					return new Import(ImportType.LIBRARY, split[2]);
				else
					throw new RuntimeException("Unknown import type '" + split[1] + "' with param '" + split[2] + "'");
			}

			if (line.substring(pos).startsWith("continue"))
			{
				nextChar(8);
				return new LoopControl(Result.continueLoop());
			}

			if (line.substring(pos).startsWith("break"))
			{
				nextChar(5);
				return new LoopControl(Result.breakLoop());
			}

			if (line.substring(pos).startsWith("true"))
			{
				nextChar(4);
				return new ValueConstant(PrimitiveTypes.TRUE);
			}

			if (line.substring(pos).startsWith("false"))
			{
				nextChar(5);
				return new ValueConstant(PrimitiveTypes.FALSE);
			}

			for (Operator op : UNARY_OPERATORS)
			{
				if (eat(op.getOperator()))
				{
					Expression right = next(i);

					return new UnaryOperator(op, right);
				}
			}

			int startPos = pos;
			if (eat("\""))
			{
				if (DEBUG)
					System.out.println("Inside String");
				if (ch == '"')
				{
					nextChar();
					if (DEBUG)
						System.out.println("Empty String");
					return new Constant(PrimitiveTypes.STRING, "");
				} else
				{
					boolean escape = false;
					StringBuilder bobTheBuilder = new StringBuilder();
					while (ch != '"' || escape)
					{
						if (ch == '\\')
						{
							escape = true;
							nextChar();
							continue;
						}
						bobTheBuilder.append(ch);
						nextChar();
						escape = false;
					}
					nextChar();
					String s = bobTheBuilder.toString();
					if (DEBUG)
						System.out.println("Finished String " + s);
					return new Constant(PrimitiveTypes.STRING, s);
				}
			}
			else if (eat("'"))
			{
				if (ch == '\'')
				{
					throw new IllegalArgumentException("Character has no char assigned!");
				}
				char c = ch;
				nextChar();
				nextChar();

				return new Constant(PrimitiveTypes.CHAR, c);
			}
			else if (eat("("))
			{
				if (ch == ')')
				{
					nextChar();
					ex = null;
				} else
				{
					Expression e = next(0);
					eat(")");
					ex = e;
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
					ex = new Constant(PrimitiveTypes.DOUBLE, Double.parseDouble(line.substring(startPos, this.pos)));
				} else
				{
					ex = new Constant(PrimitiveTypes.INT, Integer.parseInt(line.substring(startPos, this.pos)));
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

					Expression firstParameter = next(i);

					List<Expression> parameterList = functionParameters.pop();
					if (parameterList == null)
						parameterList = new ArrayList<>();
					if (firstParameter != null)
						parameterList.add(firstParameter);

					Collections.reverse(parameterList);

					ex = new FunctionCall(FunctionSource.function(name), parameterList.toArray(new Expression[0]));
				} else
				{
					ex = new Variable(VariableSource.memory(name));
				}
			} else
			{
				System.err.println(ch);
				throw new IllegalStateException("Dafuq");
			}

			return ex;
		}

		for (; ; )
		{
			if (i == 0)
			{
				if (eat(","))
				{
					Expression x = next(0);
					functionParameters.peek().add(x);
					return ex;
				}
			}

			boolean found = false;

			for (Operator op : BINARY_PRECENDENCE[BINARY_PRECENDENCE.length - 1 - i])
			{
				if (DEBUG)
					System.out.println("Testing " + op);
				if (eat(op.getOperator()))
				{
					if (DEBUG)
						System.out.println("Found: " + op);
					found = true;
					Expression left = ex;

					if (op == Operator.ASSIGN)
					{
						if (!(left instanceof Variable va))
						{
							throw new RuntimeException("Assignment requires variable at left");
						} else
						{
							ex = new Assignment(va.source.variableName, next(0));
						}
					}
					else if (op == Operator.DOT)
					{
						ex = new DotOperator(left, next(i + 1));
					} else
					{
						ex = new BinaryOperator(op, left, next(i + 1));
					}
					break;
				}
			}

			if (!found)
				return ex;
		}
	}

	public Expression parse()
	{
		nextChar();
		Expression ex = next(0);
		if (pos < line.length()) throw new RuntimeException("Unexpected: " + ch);
		return ex;
	}
}
