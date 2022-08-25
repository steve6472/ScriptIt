package steve6472.scriptit.attributes;

import steve6472.scriptit.*;
import steve6472.scriptit.expressions.*;
import steve6472.scriptit.expressions.Assignment;
import steve6472.scriptit.type.Type;

/**
 * Created by steve6472
 * Date: 7/24/2022
 * Project: ScriptIt
 */
public class AttributeSet extends Attribute
{
	@Override
	public void apply(Script script, Expression nextExpr, ValueConstant... constants)
	{/*
		if (!(nextExpr instanceof Assignment ass))
		{
			return;
		}

		if (ass.varName == null)
		{
			throw new RuntimeException("Can not generate set function for expression %s because varName is null".formatted(nextExpr));
		}

		if (ass.typeName == null)
		{
			throw new RuntimeException("Can not generate set function for expression %s because typeName is null".formatted(nextExpr));
		}

		Type type = script.getWorkspace().getType(ass.typeName);

		String functionName = "set" + ass.varName.substring(0, 1).toUpperCase() + ass.varName.substring(1);

		Assignment assignment = Assignment.assignment(ass.varName, new Variable("_value"));

		MemoryStack memory = script.getMemory();

		//TODO: return helper when in class
		Function func = new Function("_value");
		func.setExpressions(assignment);

		memory.addFunction(FunctionParameters.function(functionName).addType(type).build(), func);*/
	}

	@Override
	public String getName()
	{
		return "set";
	}

	@Override
	public Class<?>[] allowedExpressions()
	{
		return new Class[] {Assignment.class};
	}
}
