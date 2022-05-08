package steve6472.scriptit.expressions;

import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.Type;
import steve6472.scriptit.Value;
import steve6472.scriptit.tokenizer.ChainedVariable;

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

	public ClassDeclaration(String name, Type type)
	{
		this.name = name;
		this.type = type;
		variables = new ArrayList<>();
		functions = new ArrayList<>();
		constructors = new ArrayList<>();
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
				System.out.println(ass);
				if (ass.expression == null)
				{
					return new VarDec(script.getMemory().getType(ass.typeName), ass.varName, Value.NULL);
				} else
				{
					Result apply = ass.expression.apply(script);
					//System.out.println("ClassDeclaration.java:39 " + apply);
					return new VarDec(script.getMemory().getType(ass.typeName), ass.varName, apply.getValue());
				}
			});
		}
		else if (ex instanceof ChainedVariable var)
		{
			variables.add((script) ->
			{
				if (var.exp1 instanceof Variable var1 && var.exp2 instanceof Variable var2)
				{
					Type type = script.memory.getType(var1.source.variableName);
					String variableName = var2.source.variableName;

					if (type != null)
					{
						return new VarDec(type, variableName, Value.newValue(type));
					}
				}

				throw new RuntimeException("Chained Variable is not suitable to create un-initialized declaration of variable.");
			});
		}
	}

	@Override
	public Result apply(Script script)
	{
		Type type = script.getWorkspace().getType(name);
		if (type == null)
		{
			throw new RuntimeException("Type '" + name + "' not found!");
		}
		script.getMemory().addType(type);

		functions.forEach(f -> script.getMemory().addFunction(f.params, f.function));

		if (constructors.isEmpty())
		{
			script.getMemory().addFunction(FunctionParameters.create(type), new Function()
			{
				@Override
				public Result apply(Script script)
				{
					Value value = Value.newValue(type);
					for (java.util.function.Function<Script, VarDec> variable : variables)
					{
						VarDec apply = variable.apply(script);
						value.setValue(apply.name, apply.val);
					}
					return Result.value(value);
				}
			});
		} else
		{
			constructors.forEach(f -> script.getMemory().addFunction(f.params, f.function));
		}

		return Result.pass();
	}

	@Override
	public String showCode(int a)
	{
		return "class " + name;
	}

	private record VarDec(Type type, String name, Value val) {}
}
