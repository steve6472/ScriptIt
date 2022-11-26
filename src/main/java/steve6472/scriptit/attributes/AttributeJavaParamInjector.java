package steve6472.scriptit.attributes;

import steve6472.scriptit.transformer.JavaTransformer;
import steve6472.scriptit.Script;
import steve6472.scriptit.ScriptItSettings;
import steve6472.scriptit.expressions.*;
import steve6472.scriptit.type.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve6472
 * Date: 8/25/2022
 * Project: ScriptIt
 */
public class AttributeJavaParamInjector extends Attribute
{
	@Override
	public void apply(Script script, Expression nextExpr, ValueConstant... constants)
	{
		if (!ScriptItSettings.ALLOW_UNSAFE)
		{
			throw new RuntimeException("Unsafe operation detected! Allow unsafe or remove operation. (attribute java param injector)");
		}

		if (!(nextExpr instanceof DeclareFunction func))
		{
			return;
		}

		FunctionParameters parameters = func.params;
		Function function = script.getMemory().functions.remove(parameters);

		String path = constants[0].constantSuplier.get().asPrimitive().getString();
		String name = constants[1].constantSuplier.get().asPrimitive().getString();
		Type type;

		try
		{
			Class<?> aClass = Class.forName(path);
			type = JavaTransformer.generateType(aClass, script);
		} catch (ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}

		List<String> args = new ArrayList<>(List.of(function.argumentNames));
		args.add(name);
		function.argumentNames = args.toArray(new String[0]);

		FunctionParameters.FunctionParametersBuilder builder = FunctionParameters.function(parameters.getName());

		for (Type parametersType : parameters.getTypes())
		{
			builder.addType(parametersType);
		}

		builder.addType(type);

		FunctionParameters newParameters = builder.build();

		script.getMemory().addFunction(newParameters, function);
	}

	@Override
	public String getName()
	{
		return "injectJavaParam";
	}

	@Override
	public Class<?>[] allowedExpressions()
	{
		return new Class[] {DeclareFunction.class};
	}
}
