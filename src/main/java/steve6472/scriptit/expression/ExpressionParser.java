package steve6472.scriptit.expression;

import steve6472.scriptit.MathFunctions;
import steve6472.scriptit.Script;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static steve6472.scriptit.TypeDeclarations.*;

/**********************
 * <br>Created by Boann<br>
 * https://stackoverflow.com/a/26227947/13201452<br>
 * With help from https://stackoverflow.com/questions/40975678/evaluating-a-math-expression-with-variables-java-8<br>
 * On date: 5/1/2021
 * Project: ScriptIt
 *
 ***********************/
public class ExpressionParser
{
	public static boolean EVAL_DEBUG = false;
	public static boolean PARSE_DEBUG = false;

	private static final Type[] NO_PARAMETERS = new Type[0];

	public Expression parse(final String str)
	{
		return new Object()
		{
			int pos = -1, ch;

			void printParse(String s)
			{
				if (PARSE_DEBUG)
					System.out.println(s);
			}

			void printEval(String s)
			{
				if (EVAL_DEBUG)
					System.out.println(s);
			}

			final Stack<List<Expression>> functionParameters = new Stack<>(64);
			final Stack<Expression> previousTypes = new Stack<>(32);

			void nextChar()
			{
				printParse("" + (char) ch);
				ch = (++pos < str.length()) ? str.charAt(pos) : -1;
			}

			boolean eat(int charToEat)
			{
				printParse("eat till " + (char) charToEat);
				while (ch == ' ' || ch == '\n')
					nextChar();
				if (ch == charToEat)
				{
					nextChar();
					printParse("found");
					return true;
				}
				printParse("not found");
				return false;
			}

			boolean eat(char charToEat1, char charToEat2)
			{
				printParse("eat till " + charToEat1 + " " + charToEat2);
				while (ch == ' ' || ch == '\n')
					nextChar();
				if (ch == charToEat1 && pos < str.length() && str.charAt(pos + 1) == charToEat2)
				{
					nextChar();
					nextChar();
					printParse("found");
					return true;
				}
				printParse("not found");
				return false;
			}

			Expression parse()
			{
				nextChar();
				Expression x = parseExpression();
				if (pos < str.length())
					throw new RuntimeException("Unexpected: '" + (char) ch + "' at position " + pos);
				return x;
			}

			// Grammar:
			// expression = term | expression `+` term | expression `-` term
			// term = factor | term `*` factor | term `/` factor
			// factor = `+` factor | `-` factor | `(` expression `)`
			//        | number | functionName factor | factor `^` factor

			Expression parseExpression()
			{
				printParse("Parsing expression");
				Expression x = parseTerm();
				for (; ; )
				{
					if (eat('+'))
					{
						Expression a = x;
						Expression b = parseTerm();
						x = (script) ->
						{
							Value aEval = a.eval(script);
							Value bEval = b.eval(script);
							return aEval.type.binary.get(bEval.type).get(Operator.ADD).apply(aEval, bEval);
						};
					} else if (eat('-'))
					{
						Expression a = x;
						Expression b = parseTerm();
						x = (script) ->
						{
							Value aEval = a.eval(script);
							Value bEval = b.eval(script);
							return aEval.type.binary.get(bEval.type).get(Operator.SUB).apply(aEval, bEval);
						};
					} else if (eat(','))
					{
						Expression a = parseExpression();
						printParse("Adding parameter " + a);
						functionParameters.peek().add(a);
						return x;
					} else if (eat('.'))
					{
						previousTypes.push(x);
						return parseExpression();
					} else
					{
						return x;
					}
				}
			}

			Expression parseTerm()
			{
				printParse("Parsing term");
				Expression x = parseFactor();
				for (; ; )
				{
					printParse("term");
					if (eat('*'))
					{
						Expression a = x;
						Expression b = parseFactor();
						x = (script) ->
						{
							Value aEval = a.eval(script);
							Value bEval = b.eval(script);
							return aEval.type.binary.get(bEval.type).get(Operator.MUL).apply(aEval, bEval);
						};
					} else if (eat('/'))
					{
						Expression a = x;
						Expression b = parseFactor();
						x = (script) ->
						{
							Value aEval = a.eval(script);
							Value bEval = b.eval(script);
							return aEval.type.binary.get(bEval.type).get(Operator.DIV).apply(aEval, bEval);
						};
					} else
					{
						return x;
					}
				}
			}

			Expression parseFactor()
			{
				printParse("Parsing factor");
				for (Operator op : Operator.getUnaryOps())
				{
					if (op.getOperator().length() == 1)
					{
						if (eat(op.getOperator().charAt(0)))
						{
							Expression expression = parseFactor();
							return (script) ->
							{
								Value value = expression.eval(script);
								return value.type.unary.get(value.type).get(op).apply(value);
							};
						}
					} else
					{
						if (eat(op.getOperator().charAt(0), op.getOperator().charAt(1)))
						{
							Expression expression = parseFactor();
							return (script) ->
							{
								Value value = expression.eval(script);
								return value.type.unary.get(value.type).get(op).apply(value);
							};
						}
					}
				}

				Expression x;
				int startPos = this.pos;
				if (eat('"'))
				{
					printParse("String!");
					if (ch == '"')
					{
						printParse("Empty string");
						x = (script) -> new Value(STRING, "");
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
							bobTheBuilder.append((char) ch);
							nextChar();
							escape = false;
						}
						nextChar();
						printParse(bobTheBuilder.toString());
						String s = bobTheBuilder.toString();
						x = (script) -> new Value(STRING, s);
					}
				} else if (eat('\''))
				{
					printParse("Char!");
					if (ch == '\'')
					{
						throw new IllegalArgumentException("Character has no char assigned!");
					}
					char c = (char) ch;
					nextChar();
					nextChar();

					printParse("Char: '" + c + "'");

					x = (script) -> new Value(CHAR, c);
				} else if (eat('('))
				{ // parentheses
					functionParameters.push(new ArrayList<>());
					if (ch == ')')
					{
						printParse("Empty function");
						eat(')');
						functionParameters.pop();
						x = null;
					} else
					{
						x = parseExpression();
						eat(')');
					}
				} else if ((ch >= '0' && ch <= '9') || ch == '.')
				{ // numbers
					printParse("number?");
					boolean foundDot = false;
					while ((ch >= '0' && ch <= '9') || ch == '.')
					{
						if (ch == '.')
							foundDot = true;
						nextChar();
					}
					if (foundDot)
					{
						double xx = Double.parseDouble(str.substring(startPos, this.pos));
						printParse("parsed to double " + xx);
						Value val = new Value(DOUBLE, xx);
						x = (script) -> val;
					} else
					{
						int xx = Integer.parseInt(str.substring(startPos, this.pos));
						printParse("parsed to int " + xx);
						Value val = new Value(INT, xx);
						x = (script) -> val;
					}
				} else if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || ch == '_') // can only start with [a-zA-Z_]
				{ // functions
					printParse("function?");
					while ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9') || ch == '_')
						nextChar();
					String name = str.substring(startPos, this.pos);
					printParse("function name: " + name);

					if (ch == '(')
					{
						Expression exp = parseFactor();
						if (exp != null && functionParameters.peek().isEmpty())
						{
							printParse("One param function");
							x = (script) ->
							{
								Type[] typeArr = new Type[1];
								Value[] arr = new Value[1];
								arr[0] = exp.eval(script);
								typeArr[0] = arr[0].type;
								printParse("Function param: " + arr[0]);
								if (!previousTypes.isEmpty())
								{
									Value eval = previousTypes.pop().eval(script);
									Function function = eval.type.getFunction(name, typeArr);
									if (function == null)
										throw new RuntimeException("Unknown function '" + name + "' with types " + Arrays.toString(typeArr));
									return function.apply(eval, arr);
								} else
								{
									Constructor function = script.getFunction(name, typeArr);
									if (function == null)
										throw new RuntimeException("Unknown function '" + name + "' with types " + Arrays.toString(typeArr));
									return function.apply(arr);
								}
							};
						}
						else if (!functionParameters.isEmpty() && !functionParameters.peek().isEmpty() && exp != null)
						{
							printParse("Function");
							List<Expression> peek = functionParameters.peek();
							x = (script) ->
							{
								Type[] typeArr = new Type[peek.size() + 1];
								Value[] arr = new Value[peek.size() + 1];
								arr[0] = exp.eval(script);
								typeArr[0] = arr[0].type;
								printEval("Function param: " + arr[0]);
								for (int i = 0; i < peek.size(); i++)
								{
									Expression functionParameter = peek.get(i);
									Value eval = functionParameter.eval(script);
									arr[peek.size() - i] = eval;
									typeArr[peek.size() - i] = eval.type;
									printEval("Function param: " + eval);
								}
								if (!previousTypes.isEmpty())
								{
									Value eval = previousTypes.pop().eval(script);
									Function function = eval.type.getFunction(name, typeArr);
									if (function == null)
										throw new RuntimeException("Unknown function '" + name + "' with types " + Arrays.toString(typeArr));
									return function.apply(eval, arr);
								} else
								{
									Constructor function = script.getFunction(name, typeArr);
									if (function == null)
										throw new RuntimeException("Unknown function '" + name + "' with types " + Arrays.toString(typeArr));
									return function.apply(arr);
								}
							};
						} else
						{
							printParse("Procedure");
							// Procedure (function with no parameters)
							if (!previousTypes.isEmpty())
							{
								Expression peek = previousTypes.pop();
								x = (script) ->
								{
									Value eval = peek.eval(script);
									return eval.type.getFunction(name, NO_PARAMETERS).apply(eval);
								};
							} else
							{
								x = (script) -> script.getFunction(name, NO_PARAMETERS).apply();
							}
						}
						functionParameters.pop();
					} else
					{
						x = (scipt) ->
						{
							if (scipt.hasValue(name))
							{
								return scipt.getValue(name);
							} else
							{
								throw new RuntimeException("Unknown variable/function name: " + name);
							}
						};
					}
				} else
				{
					throw new RuntimeException("Unexpected: " + (char) ch);
				}

//				if (eat('^'))
//				{
//					Expression a = x;
//					x = (script) ->
//					{
//						Value v = a.eval(script);
//						Value eval = parseFactor().eval(script);
//						return v.type.powFunctions.get(eval.type).apply(v, eval);
//					}; // exponentiation
//				}

				return x;
			}
		}.parse();
	}

	public static void main(String[] args)
	{
		ExpressionParser parser = new ExpressionParser();

		Script script = new Script();
		script.importType(INT);
		script.importType(STRING);
		script.importType(VEC2);

		MathFunctions.definePi(script);
		MathFunctions.importMathFunctionsRad(script);

		script.addConstructor(FunctionParameters.function("stuff").addType(INT).addType(INT).build(), (arags) -> {
			int temp = arags[0].getInt() * arags[1].getInt();
			return new Value(INT, temp + arags[0].getInt());
		});

		Expression exp = parser.parse("int(7)");
//		Expression exp = parser.parse("-(6*7)");
//		Expression exp = parser.parse("int(6)*7");
//		Expression exp = parser.parse("(\"Hello\" + \" world!\").print()");
//		Expression exp = parser.parse("(\"Hello\" + ' ' + \"world\" + '!').len() * 2");
//		Expression exp = parser.parse("string(\"_1\\\"3_\").print()");
//		Expression exp = parser.parse("vec2(0, 9).-toString()");
//		Expression exp = parser.parse("string(\"Hello World\").-len()");
//		Expression exp = parser.parse("\"Hello World\"-3");
//		Expression exp = parser.parse("(\"#-\"*10 + '#').print()");
//		Expression exp = parser.parse("vec2(8 + stuff(3, int(6)) * 4, 2).toString()"); // [92.0, 2.0]
//		System.out.println(unknownNames)
		for (int i = 0; i < 1; i++)
		{
			Value eval = exp.eval(script);
			System.out.println(eval);
		}
	}
}
