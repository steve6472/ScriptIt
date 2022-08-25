package steve6472.scriptit.expressions;

import steve6472.scriptit.*;
import steve6472.scriptit.type.Type;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/28/2021
 * Project: ScriptIt
 *
 ***********************/
public class DeclareFunction extends Expression
{
	public Function function;
	public FunctionParameters params;

	public DeclareFunction(String name, Function function, String[] argumentNames, Type[] argumentTypes)
	{
		this.function = function;
		this.function.argumentNames = argumentNames;
		this.function.name = name;
		this.params = FunctionParameters.create(name, argumentTypes);
	}

	@Override
	public Result apply(Script script)
	{
		stackTrace(0, "Declaring function '" + function.name + "'");
		script.getMemory().addFunction(params, function);

		return Result.pass();
	}

	@Override
	public String showCode(int a)
	{
		StringBuilder s = new StringBuilder(Highlighter.IF + "function " + Highlighter.RESET + params.getName() + "(");
		String[] argumentNames = function.argumentNames;
		for (int i = 0; i < argumentNames.length; i++)
		{
			String argumentName = argumentNames[i];
			s.append(params.getTypes()[i].getKeyword()).append(" ").append(argumentName);
			if (i < argumentNames.length - 1)
				s.append(", ");
		}
		s.append(")\n{");
		s.append(function.showCode(a));
		s.append("}");
		return s.toString();
	}

	@Override
	public String toString()
	{
		return "DeclareFunction{" + "function=" + function + ", params=" + params + '}';
	}
}
