package steve6472.scriptit.attributes;

import steve6472.scriptit.MemoryStack;
import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.expressions.*;
import steve6472.scriptit.expressions.Assignment;

/**
 * Created by steve6472
 * Date: 7/24/2022
 * Project: ScriptIt
 */
public class AttributeGet extends Attribute
{
	@Override
	public void apply(Script script, Expression nextExpr, ValueConstant... constants)
	{
		/*
		if (!(nextExpr instanceof Assignment ass))
		{
			return;
		}

		if (ass.varName == null)
		{
			throw new RuntimeException("Can not generate get function for expression %s because varName is null".formatted(nextExpr));
		}

		String functionName = "get" + ass.varName.substring(0, 1).toUpperCase() + ass.varName.substring(1);

		MemoryStack memory = script.getMemory();
		memory.addFunction(FunctionParameters.function(functionName).build(), new Function()
		{
			@Override
			public Result apply(Script script)
			{
				return Result.value(script.getMemory().getVariable(ass.varName));
			}
		});*/
	}

	@Override
	public String getName()
	{
		return "get";
	}

	@Override
	public Class<?>[] allowedExpressions()
	{
		return new Class[] {Assignment.class};
	}
}
