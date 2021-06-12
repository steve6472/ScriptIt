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

	private static final String COLOR_NAME = "\u001b[38;5;61m";
	private static final String COLOR_VARIABLE = Log.WHITE;

	private static int depth = 0;

	private static String tree()
	{
		return "\t".repeat(ScriptReader.depth) + " ".repeat(depth - 1);
	}

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

	boolean eat(char charToEat, boolean insideString)
	{
		while (Character.isWhitespace(ch))
			nextChar();
		if (ch == charToEat)
		{
			if (!insideString && pos < line.length() - 1)
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

	boolean eat(String s, boolean insideString)
	{
		if (s.length() == 1)
		{
			return eat(s.charAt(0), insideString);
		} else
		{
			return eat(s.charAt(0), s.charAt(1));
		}
	}

	boolean eat(String s)
	{
		return eat(s, false);
	}

	private Expression next(int i)
	{
		Expression ex = null;

//		if (DEBUG)
//			System.out.println(tree() + Log.WHITE + "Current: " + i + Log.RESET);

		if (i == 0)
		{
			if (line.substring(pos).startsWith("return"))
			{
				depth++;
				if (DEBUG)
					System.out.println(tree() + COLOR_NAME + "return" + Log.RESET);
				if (RETURN_THIS.matcher(line).matches())
				{
					pos = line.length();
					depth--;
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
						depth--;
						return new Return(next);
					}
				}
			}

			if (line.substring(pos).startsWith("import"))
			{
				depth++;
				if (DEBUG)
					System.out.println(tree() + COLOR_NAME + "import" + Log.RESET);
				String[] split = line.split("\s");
				pos = line.length();
				if (split[1].equals("type"))
				{
					if (DEBUG)
						System.out.println(tree() + COLOR_NAME + "type " + COLOR_VARIABLE + split[2] + Log.RESET);
					depth--;
					return new Import(ImportType.TYPE, split[2]);
				} else if (split[1].equals("library"))
				{
					if (DEBUG)
						System.out.println(tree() + COLOR_NAME + "library " + COLOR_VARIABLE + split[2] + Log.RESET);
					depth--;
					return new Import(ImportType.LIBRARY, split[2]);
				} else
					throw new RuntimeException("Unknown import type '" + split[1] + "' with param '" + split[2] + "'");
			}

			if (line.substring(pos).startsWith("continue"))
			{
				depth++;
				if (DEBUG)
					System.out.println(tree() + COLOR_NAME + "continue" + Log.RESET);
				nextChar(8);
				depth--;
				return new LoopControl(Result.continueLoop());
			}

			if (line.substring(pos).startsWith("break"))
			{
				depth++;
				if (DEBUG)
					System.out.println(tree() + COLOR_NAME + "break" + Log.RESET);
				nextChar(5);
				depth--;
				return new LoopControl(Result.breakLoop());
			}

			if (line.substring(pos).trim().startsWith("true"))
			{
				depth++;
				if (DEBUG)
					System.out.println(tree() + COLOR_VARIABLE + "true" + Log.RESET);
				nextChar('e');
				nextChar();
				depth--;
				return new ValueConstant(PrimitiveTypes.TRUE());
			}

			if (line.substring(pos).trim().startsWith("false"))
			{
				depth++;
				if (DEBUG)
					System.out.println(tree() + COLOR_VARIABLE + "false" + Log.RESET);
				nextChar('e');
				nextChar();
				depth--;
				return new ValueConstant(PrimitiveTypes.FALSE());
			}
		}

		if (i < BINARY_PRECENDENCE.length)
			ex = next(i + 1);

		if (i == BINARY_PRECENDENCE.length)
		{
//			if (DEBUG)
//				System.out.println("Trying unary");

			for (Operator op : UNARY_OPERATORS)
			{
				if (eat(op.getOperator()))
				{
					depth++;
					if (DEBUG)
						System.out.println(tree() + COLOR_NAME + "Unary: " + COLOR_VARIABLE + op);

					Expression right = next(i);

					depth--;
					return new UnaryOperator(op, right);
				}
			}

//			if (DEBUG)
//				System.out.println("Trying other");

			int startPos = pos;
			depth++;
			if (eat("\"", true))
			{
				if (DEBUG)
					System.out.println(tree() + COLOR_NAME + "Inside String, next: '" + COLOR_VARIABLE + (ch == '\n' ? "\\n" : ch) + COLOR_NAME + "'" + Log.RESET);
				if (ch == '"')
				{
					nextChar();
					if (DEBUG)
						System.out.println(tree() + COLOR_NAME + "Empty String" + Log.RESET);
					depth--;
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
						System.out.println(tree() + COLOR_NAME + "Finished String \"" + COLOR_VARIABLE + s.replaceAll("\n", "\\\\n") + COLOR_NAME + "\"" + Log.RESET);
					depth--;
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

				if (DEBUG)
					System.out.println(tree() + COLOR_NAME + "Char: '" + COLOR_VARIABLE + c + COLOR_NAME + "'" + Log.RESET);

				depth--;
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

				if (DEBUG)
					System.out.println(tree() + COLOR_NAME + "Number: " + COLOR_VARIABLE + line.substring(startPos, this.pos) + Log.RESET);

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
					if (DEBUG)
						System.out.println(tree() + COLOR_NAME + "Function: " + COLOR_VARIABLE + name + Log.RESET);

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
					if (DEBUG)
						System.out.println(tree() + COLOR_NAME + "Variable: " + COLOR_VARIABLE + name + Log.RESET);

					ex = new Variable(VariableSource.memory(name));
				}
			} else
			{
				System.err.println(ch);
				throw new IllegalStateException("Dafuq " + i);
			}

			depth--;
			return ex;
		}

		for (; ; )
		{
			if (i == 0)
			{
				if (eat(","))
				{
					depth++;
					if (DEBUG)
						System.out.println(tree() + COLOR_NAME + "comma" + Log.RESET);
					Expression x = next(0);
					functionParameters.peek().add(x);
					depth--;
					return ex;
				}
			}

			boolean found = false;

			for (Operator op : BINARY_PRECENDENCE[BINARY_PRECENDENCE.length - 1 - i])
			{
				if (eat(op.getOperator()))
				{
					depth++;
					if (DEBUG)
						System.out.println(tree() + COLOR_NAME + "Found: " + COLOR_VARIABLE + op + Log.RESET);
					found = true;
					Expression left = ex;

					if (op == Operator.ASSIGN)
					{
						if (!(left instanceof Variable va))
						{
							throw new RuntimeException("Assignment requires variable at left");
						} else
						{

							depth--;
							ex = new Assignment(va.source.variableName, next(0));
						}
					}
					else if (op == Operator.DOT)
					{
						depth--;
						ex = new DotOperator(left, next(i + 1));
					} else
					{
						depth--;
						ex = new BinaryOperator(op, left, next(i + 1));
					}
					break;
				}
			}

			if (!found)
			{
				return ex;
			}
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
