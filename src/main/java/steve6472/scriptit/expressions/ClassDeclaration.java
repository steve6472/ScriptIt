package steve6472.scriptit.expressions;

import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.type.Type;
import steve6472.scriptit.tokenizer.ChainedVariable;
import steve6472.scriptit.value.UniversalValue;
import steve6472.scriptit.value.Value;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472
 * On date: 5/7/2022
 * Project: ScriptIt
 *
 ***********************/
public class ClassDeclaration extends Expression
{
	public final String name;
	public final Type type;
	public final List<java.util.function.Function<Script, VarDec>> variables;
	public final List<DeclareFunction> functions, constructors;
	public final List<OverloadFunction> overloadFunctions;
	public Expression make = null;
	public String makeName = null;

	public ClassDeclaration(String name, Type type)
	{
		this.name = name;
		this.type = type;
		variables = new ArrayList<>();
		functions = new ArrayList<>();
		constructors = new ArrayList<>();
		overloadFunctions = new ArrayList<>();
	}

	public void add(List<Expression> expressions)
	{
		for (Expression expression : expressions)
		{
			add(expression);
		}
	}

	public void add(Expression ex)
	{
		if (ex instanceof DeclareFunction fun)
		{
			if (fun.params.getName().equals(type.getKeyword()))
			{
				constructors.add(fun);
			} else
			{
				functions.add(fun);
			}
		}
		else if (ex instanceof Assignment ass)
		{
			variables.add((script) ->
			{
				if (ass.expression == null)
				{
					Assignment.DeclarationData declarationData = ass.resolveDeclarationType(script, ass.valuePath);
					Value value = ass.declareUninitValue(declarationData);
					return new VarDec(declarationData.variableName(), value);
				} else
				{
					Assignment.DeclarationData declarationData = ass.resolveDeclarationType(script, ass.valuePath);
					Value value = ass.declareInitValue(script, declarationData);
					return new VarDec(declarationData.variableName(), value);
				}
			});
		}
		else if (ex instanceof ChainedVariable var)
		{
			variables.add((script) ->
			{
				if (var.exp1 instanceof Variable var1 && var.exp2 instanceof Variable var2)
				{
					Type type = script.memory.getType(var1.variableName);
					String variableName = var2.variableName;

					if (type != null)
					{
						return new VarDec(variableName, type.uninitValue());
					}
				}

				throw new RuntimeException("Chained Variable is not suitable to create un-initialized declaration of variable.");
			});
		} else if (ex instanceof OverloadFunction of)
		{
			overloadFunctions.add(of);
		}
	}

	@Override
	public Result apply(Script script)
	{
		stackTrace("ClassDeclaration");

		Type type = script.getWorkspace().getType(name);
		if (type == null)
		{
			throw new RuntimeException("Type '" + name + "' not found!");
		}
		script.getMemory().addType(type);

		functions.forEach(f ->
		{
			type.addFunction(f.params, f.function);
		});

		// Adds basic constructor without any parameters
		if (constructors.isEmpty())
		{
			Function function = new Function()
			{
				@Override
				public Result apply(Script script)
				{
					stackTrace(1, "Default Constructor for " + name);
					stackTrace(1);
					UniversalValue value = UniversalValue.newValue(type);
					script.getMemory().push();
					for (java.util.function.Function<Script, VarDec> variable : variables)
					{
						VarDec apply = variable.apply(script);
						stackTrace("Declaring variable " + apply.name);
						script.getMemory().variables.put(apply.name, apply.val);
						value.setValue(apply.name, apply.val);
					}
					script.getMemory().pop();
					stackTrace(-1);
					return Result.value(value);
				}
			};
			function.name = "Default Constructor for " + name;
			script.getMemory().addFunction(FunctionParameters.create(type), function);
		} else
		{
			//				System.out.println("Constructor " + f.params.getName() + " " + Arrays.toString(f.params.getTypes()));
			constructors.forEach(f -> script.getMemory().addFunction(f.params, createConstructor(f.function)));
		}

		//TODO: make this nice-er
		overloadFunctions.forEach(f ->
		{
			if (f.type == OverloadFunction.OverloadType.BINARY)
			{
				// overload binary + (vec2 other)
				if (f.types.length == 1)
				{
					type.addBinaryOperator(f.types[0], f.operator, new Function()
					{
						@Override
						public Result apply(Script script)
						{
							f.body.setReturnThisHelper(arguments[0]);
							f.body.setArguments(new Value[] {arguments[1]});
							return f.body.apply(script);
						}
					});
				}
			} else if (f.type == OverloadFunction.OverloadType.UNARY)
			{
				type.addUnaryOperator(f.operator, new Function()
				{
					@Override
					public Result apply(Script script)
					{
						f.body.setReturnThisHelper(arguments[0]);
						f.body.setArguments(new Value[] {arguments[0]});
						return f.body.apply(script);
					}
				});
			}
		});

		if (make != null)
		{
			Result apply = make.apply(script);
			script.memory.addVariable(makeName, apply.getValue());
		}

		return Result.pass();
	}

	private Function createConstructor(Function fun)
	{
		return new Function()
		{
			@Override
			public Result apply(Script script)
			{
				UniversalValue value = UniversalValue.newValue(type);
				for (java.util.function.Function<Script, VarDec> variable : variables)
				{
					VarDec apply = variable.apply(script);
					value.setValue(apply.name, apply.val);
				}

				fun.setReturnThisHelper(value);
				fun.setArguments(arguments);
				fun.apply(script);

				return Result.value(value);
			}
		};
	}

	@Override
	public String showCode(int a)
	{
		return "class " + name;
	}

	private record VarDec(String name, Value val) {}
}
