package steve6472.scriptit.expression;

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

	public Map<String, Value> variables;
	public Map<FunctionParameters, Constructor> constructors;

	public ExpressionParser()
	{
		this.variables = new HashMap<>();
		this.constructors = new HashMap<>();

		// Technically a double constructors
		addConstructor(FunctionParameters.function("sqrt").addType(DOUBLE).build(), a -> new Value(DOUBLE, Math.sqrt(a[0].getDouble("v"))));
		addConstructor(FunctionParameters.function("sin").addType(DOUBLE).build(), a -> new Value(DOUBLE, Math.sin(a[0].getDouble("v"))));
		addConstructor(FunctionParameters.function("cos").addType(DOUBLE).build(), a -> new Value(DOUBLE, Math.cos(a[0].getDouble("v"))));
		addConstructor(FunctionParameters.function("tan").addType(DOUBLE).build(), a -> new Value(DOUBLE, Math.tan(a[0].getDouble("v"))));
	}

	public void addVariable(String name, Value value)
	{
		variables.put(name, value);
	}

	public void addConstructor(FunctionParameters parameters, Constructor function)
	{
		constructors.put(parameters, function);
	}

//	public void addFunction(FunctionParameters parameters, Function function)
//	{
//		constructors.put(parameters, function);
//	}

	public Constructor getFunction(String name, Type[] types)
	{
		if (DEBUG)
			System.out.println("Looking for function with name '" + name + "' and types " + Arrays.toString(types));

		main: for (Map.Entry<FunctionParameters, Constructor> entry : constructors.entrySet())
		{
			FunctionParameters k = entry.getKey();
			Constructor v = entry.getValue();
			if (!k.getName().equals(name))
				continue;
			if (k.getTypes().length != types.length)
				continue;
			for (int i = 0; i < types.length; i++)
			{
				if (k.getTypes()[i] != types[i])
				{
					continue main;
				}
			}
			return v;
		}
		return null;
	}

	public Expression parse(String str)
	{
		return parse(str, null);
	}

	public Expression parse(final String str, List<String> unknownNames)
	{
		return new Object()
		{
			int pos = -1, ch;

			void print(String s)
			{
				if (DEBUG)
					System.out.println(s);
			}

			final List<Expression> functionParameters = new ArrayList<>();
			final ExpressionStack previousTypes = new ExpressionStack(32);

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
				Expression x = parseTerm();
				for (; ; )
				{
					if (eat('+'))
					{
						Expression a = x;
						Expression b = parseTerm();
						Value aEval = a.eval();
						Value bEval = b.eval();
						x = () -> aEval.type.addFunctions.get(bEval.type).apply(aEval, bEval);
					} else if (eat('-'))
					{
						Expression a = x;
						Expression b = parseTerm();
						Value aEval = a.eval();
						Value bEval = b.eval();
						x = () -> aEval.type.subFunctions.get(bEval.type).apply(aEval, bEval);
					} else if (eat(','))
					{
						Expression a = parseExpression();
						functionParameters.add(a);
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
				Expression x = parseFactor();
				for (; ; )
				{
					if (eat('*'))
					{
						Expression a = x;
						Expression b = parseFactor();
						Value aEval = a.eval();
						Value bEval = b.eval();
						x = () -> aEval.type.mulFunctions.get(bEval.type).apply(aEval, bEval);
					} else if (eat('/'))
					{
						Expression a = x;
						Expression b = parseFactor();
						Value aEval = a.eval();
						Value bEval = b.eval();
						x = () -> aEval.type.divFunctions.get(bEval.type).apply(aEval, bEval);
					} else
					{
						return x;
					}
				}
			}

			Expression parseFactor()
			{
				if (eat('+'))
				{
					Value value = parseFactor().eval();
					Value v = value.type.unaryAddFunctions.get(value.type).apply(value);
					return () -> v; // unary plus
				}
				if (eat('-'))
				{
					Value value = parseFactor().eval();
					Value v = value.type.unarySubFunctions.get(value.type).apply(value);
					return () -> v; // unary minus
				}
				if (eat('*'))
				{
					Value value = parseFactor().eval();
					Value v = value.type.unaryMulFunctions.get(value.type).apply(value);
					return () -> v; // unary mul
				}
				if (eat('/'))
				{
					Value value = parseFactor().eval();
					Value v = value.type.unaryDivFunctions.get(value.type).apply(value);
					return () -> v; // unary div
				}
				if (eat('^'))
				{
					Value value = parseFactor().eval();
					Value v = value.type.unaryPowFunctions.get(value.type).apply(value);
					return () -> v; // unary div
				}
				if (eat('~'))
				{
					Value value = parseFactor().eval();
					Value v = value.type.unaryNegFunctions.get(value.type).apply(value);
					return () -> v; // unary neg
				}
				if (eat('!'))
				{
					Value value = parseFactor().eval();
					Value v = value.type.unaryNotFunctions.get(value.type).apply(value);
					return () -> v; // unary not
				}

				Expression x;
				int startPos = this.pos;
				if (eat('"'))
				{
					print("String!");
					if (ch == '"')
					{
						print("Empty string");
						x = () -> new Value(STRING, "");
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
						x = () -> new Value(STRING, s);
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

					x = () -> new Value(CHAR, c);
				} else if (eat('('))
				{ // parentheses
					if (ch == ')')
					{
						print("Empty function");
						eat(')');
						x = () -> null;
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
						x = () -> val;
					} else
					{
						int xx = Integer.parseInt(str.substring(startPos, this.pos));
						print("parsed to int " + xx);
						Value val = new Value(INT, xx);
						x = () -> val;
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
						Value ev;
						ev = exp.eval();
						if (!functionParameters.isEmpty() || ev != null)
						{
							Type[] typeArr = new Type[functionParameters.size() + 1];
							Value[] arr = new Value[functionParameters.size() + 1];
							arr[0] = ev;
							typeArr[0] = arr[0].type;
							print("Function param: " + ev);
							for (int i = 0; i < functionParameters.size(); i++)
							{
								Expression functionParameter = functionParameters.get(i);
								Value eval = functionParameter.eval();
								arr[functionParameters.size() - i] = eval;
								typeArr[functionParameters.size() - i] = eval.type;
								print("Function param: " + eval);
							}
							functionParameters.clear();
							if (!previousTypes.isEmpty())
							{
								x = () ->
								{
									Value eval = previousTypes.pop().eval();
									Function function = eval.type.getFunction(name, typeArr);
									if (function == null)
										throw new RuntimeException("Unknown function '" + name + "' with types " + Arrays.toString(typeArr));
									return function.apply(eval, arr);
								};
							} else
							{
								x = () ->
								{
									Constructor function = getFunction(name, typeArr);
									if (function == null)
										throw new RuntimeException("Unknown function '" + name + "' with types " + Arrays.toString(typeArr));
									return function.apply(arr);
								};
							}
						} else
						{
							if (!previousTypes.isEmpty())
							{
								x = () ->
								{
									Value eval = previousTypes.pop().eval();
									return eval.type.getFunction(name, NO_PARAMETERS).apply(eval);
								};
							} else
							{
								x = () -> getFunction(name, NO_PARAMETERS).apply();
							}
						}
					} else
					{
						if (variables.containsKey(name))
						{
							x = () -> variables.get(name);
						} else
						{
							x = () -> variables.get(name);
							if (unknownNames == null)
								throw new RuntimeException("Unknown variable/function name: " + name);
							else
								unknownNames.add(name);
						}
					}
				} else
				{
					throw new RuntimeException("Unexpected: " + (char) ch);
				}

				if (eat('^'))
				{
					Value v = x.eval();
					Value eval = parseFactor().eval();
					x = () -> v.type.powFunctions.get(eval.type).apply(v, eval); // exponentiation
				}

				return x;
			}
		}.parse();
	}

	public static void main(String[] args)
	{
		ExpressionParser parser = new ExpressionParser();
		parser.addVariable("pi", new Value(DOUBLE, Math.PI));
		parser.addVariable("true", new Value(BOOL, true));
		parser.addVariable("false", new Value(BOOL, false));

		VEC2.importIntoParser(parser);
		STRING.importIntoParser(parser);
		CHAR.importIntoParser(parser);
		BOOL.importIntoParser(parser);
		DOUBLE.importIntoParser(parser);
		INT.importIntoParser(parser);

		Expression exp = parser.parse("int(6)*7");
//		Expression exp = parser.parse("(\"Hello\" + \" world!\").print()");
//		Expression exp = parser.parse("(\"Hello\" + ' ' + \"world\" + '!').len() * 2");
//		Expression exp = parser.parse("string(\"_1\\\"3_\").print()");'
//		Expression exp = parser.parse("vec2(0, 9).-toString().print()");
//		Expression exp = parser.parse("string(\"Hello World\").-len()");
//		Expression exp = parser.parse("\"Hello World\"-3");
//		System.out.println(unknownNames)
		Value eval = exp.eval();
		System.out.println(eval);
	}
}
