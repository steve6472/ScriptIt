package steve6472.scriptit.expression;

import steve6472.scriptit.MathFunctions;
import steve6472.scriptit.Script;

import java.util.*;

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
	public static boolean DEBUG = false;

	private static final Type[] NO_PARAMETERS = new Type[0];

	public Expression parse(final String str)
	{
		return new Object()
		{
			int pos = -1, ch;

			void print(String s)
			{
				if (DEBUG)
					System.out.println(s);
			}

			final Stack<List<Expression>> functionParameters = new Stack<>(64);
			final Stack<Expression> previousTypes = new Stack<>(32);

			void nextChar()
			{
				print("" + (char) ch);
				ch = (++pos < str.length()) ? str.charAt(pos) : -1;
			}

			boolean eat(int charToEat)
			{
				print("eat till " + (char) charToEat);
				while (ch == ' ' || ch == '\n')
					nextChar();
				if (ch == charToEat)
				{
					nextChar();
					print("found");
					return true;
				}
				print("not found");
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
				print("Parsing expression");
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
							return aEval.type.addFunctions.get(bEval.type).apply(aEval, bEval);
						};
					} else if (eat('-'))
					{
						Expression a = x;
						Expression b = parseTerm();
						x = (script) ->
						{
							Value aEval = a.eval(script);
							Value bEval = b.eval(script);
							return aEval.type.subFunctions.get(bEval.type).apply(aEval, bEval);
						};
					} else if (eat(','))
					{
						Expression a = parseExpression();
						print("Adding parameter " + a);
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
				print("Parsing term");
				Expression x = parseFactor();
				for (; ; )
				{
					print("term");
					if (eat('*'))
					{
						Expression a = x;
						Expression b = parseFactor();
						x = (script) ->
						{
							Value aEval = a.eval(script);
							Value bEval = b.eval(script);
							return aEval.type.mulFunctions.get(bEval.type).apply(aEval, bEval);
						};
					} else if (eat('/'))
					{
						Expression a = x;
						Expression b = parseFactor();
						x = (script) ->
						{
							Value aEval = a.eval(script);
							Value bEval = b.eval(script);
							return aEval.type.divFunctions.get(bEval.type).apply(aEval, bEval);
						};
					} else
					{
						return x;
					}
				}
			}

			Expression parseFactor()
			{
				print("Parsing factor");
				if (eat('+'))
				{
					Expression expression = parseFactor();
					return (script) ->
					{
						Value value = expression.eval(script);
						return value.type.unaryAddFunctions.get(value.type).apply(value);
					}; // unary plus
				}
				if (eat('-'))
				{
					Expression expression = parseFactor();
					print("Returning unary minus");
					return (script) ->
					{
						Value value = expression.eval(script);
						return value.type.unarySubFunctions.get(value.type).apply(value);
					}; // unary minus
				}
				if (eat('*'))
				{
					Expression expression = parseFactor();
					return (script) ->
					{
						Value value = expression.eval(script);
						return value.type.unaryMulFunctions.get(value.type).apply(value);
					}; // unary mul
				}
				if (eat('/'))
				{
					Expression expression = parseFactor();
					return (script) ->
					{
						Value value = expression.eval(script);
						return value.type.unaryDivFunctions.get(value.type).apply(value);
					}; // unary div
				}
				if (eat('^'))
				{
					Expression expression = parseFactor();
					return (script) ->
					{
						Value value = expression.eval(script);
						return value.type.unaryPowFunctions.get(value.type).apply(value);
					}; // unary div
				}
				if (eat('~'))
				{
					Expression expression = parseFactor();
					return (scipt) ->
					{
						Value value = expression.eval(scipt);
						return value.type.unaryNegFunctions.get(value.type).apply(value);
					}; // unary neg
				}
				if (eat('!'))
				{
					Expression expression = parseFactor();
					return (script) ->
					{
						Value value = expression.eval(script);
						return value.type.unaryNotFunctions.get(value.type).apply(value);
					}; // unary not
				}

				Expression x;
				int startPos = this.pos;
				if (eat('"'))
				{
					print("String!");
					if (ch == '"')
					{
						print("Empty string");
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
						print(bobTheBuilder.toString());
						String s = bobTheBuilder.toString();
						x = (script) -> new Value(STRING, s);
					}
				} else if (eat('\''))
				{
					print("Char!");
					if (ch == '\'')
					{
						throw new IllegalArgumentException("Character has no char assigned!");
					}
					char c = (char) ch;
					nextChar();
					nextChar();

					print("Char: '" + c + "'");

					x = (script) -> new Value(CHAR, c);
				} else if (eat('('))
				{ // parentheses
					functionParameters.push(new ArrayList<>());
					if (ch == ')')
					{
						print("Empty function");
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
					print("number?");
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
						print("parsed to double " + xx);
						Value val = new Value(DOUBLE, xx);
						x = (script) -> val;
					} else
					{
						int xx = Integer.parseInt(str.substring(startPos, this.pos));
						print("parsed to int " + xx);
						Value val = new Value(INT, xx);
						x = (script) -> val;
					}
				} else if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || ch == '_') // can only start with [a-zA-Z_]
				{ // functions
					print("function?");
					while ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9') || ch == '_')
						nextChar();
					String name = str.substring(startPos, this.pos);
					print("function name: " + name);

					if (ch == '(')
					{
						Expression exp = parseFactor();
						if (exp != null && functionParameters.peek().isEmpty())
						{
							print("One param function");
							x = (script) ->
							{
								Type[] typeArr = new Type[1];
								Value[] arr = new Value[1];
								arr[0] = exp.eval(script);
								typeArr[0] = arr[0].type;
								print("Function param: " + arr[0]);
								if (!previousTypes.isEmpty())
								{
									Value eval = previousTypes.pop().eval(script);
									Function function = eval.type.getFunction(name, typeArr);
									if (function == null)
										throw new RuntimeException("Unknown function '" + name + "' with types " + Arrays.toString(typeArr));
									return function.apply(eval, arr);
								} else
								{
									Constructor function = script.namespace.getFunction(name, typeArr);
									if (function == null)
										throw new RuntimeException("Unknown function '" + name + "' with types " + Arrays.toString(typeArr));
									return function.apply(arr);
								}
							};
						}
						else if (!functionParameters.isEmpty() && !functionParameters.peek().isEmpty() && exp != null)
						{
							print("Function");
							List<Expression> peek = functionParameters.peek();
							x = (script) ->
							{
								Type[] typeArr = new Type[peek.size() + 1];
								Value[] arr = new Value[peek.size() + 1];
								arr[0] = exp.eval(script);
								typeArr[0] = arr[0].type;
								print("Function param: " + arr[0]);
								for (int i = 0; i < peek.size(); i++)
								{
									Expression functionParameter = peek.get(i);
									Value eval = functionParameter.eval(script);
									arr[peek.size() - i] = eval;
									typeArr[peek.size() - i] = eval.type;
									print("Function param: " + eval);
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
									Constructor function = script.namespace.getFunction(name, typeArr);
									if (function == null)
										throw new RuntimeException("Unknown function '" + name + "' with types " + Arrays.toString(typeArr));
									return function.apply(arr);
								}
							};
						} else
						{
							print("Procedure");
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
								x = (script) -> script.namespace.getFunction(name, NO_PARAMETERS).apply();
							}
						}
						functionParameters.pop();
					} else
					{
						x = (scipt) ->
						{
							if (scipt.namespace.valueMap.containsKey(name))
							{
								return scipt.namespace.valueMap.get(name);
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

				if (eat('^'))
				{
					Expression a = x;
					x = (script) ->
					{
						Value v = a.eval(script);
						Value eval = parseFactor().eval(script);
						return v.type.powFunctions.get(eval.type).apply(v, eval);
					}; // exponentiation
				}

				return x;
			}
		}.parse();
	}

	public static void main(String[] args)
	{
		ExpressionParser parser = new ExpressionParser();

		Script script = new Script();
		script.namespace.importType(INT);
		script.namespace.importType(STRING);
		script.namespace.importType(VEC2);

		MathFunctions.definePi(script);
		MathFunctions.importMathFunctionsRad(script);

		script.namespace.addConstructor(FunctionParameters.function("stuff").addType(INT).addType(INT).build(), (arags) -> {
			int temp = arags[0].getInt() * arags[1].getInt();
			return new Value(INT, temp + arags[0].getInt());
		});

//		Expression exp = parser.parse("int(7)");
//		Expression exp = parser.parse("-(6*7)");
//		Expression exp = parser.parse("int(6)*7");
		Expression exp = parser.parse("(\"Hello\" + \" world!\").print()");
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
