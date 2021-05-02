package steve6472.scriptit;

import steve6472.scriptit.expression.FunctionParameters;
import steve6472.scriptit.expression.Value;

import static steve6472.scriptit.TypeDeclarations.DOUBLE;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/2/2021
 * Project: ScriptIt
 *
 ***********************/
public class MathFunctions
{
	public static void importMathFunctionsRad(Script script)
	{
		Namespace namespace = script.namespace;
		namespace.addConstructor(FunctionParameters.function("sqrt").addType(DOUBLE).build(), a -> new Value(DOUBLE, Math.sqrt(a[0].getDouble("v"))));
		namespace.addConstructor(FunctionParameters.function("sin").addType(DOUBLE).build(), a -> new Value(DOUBLE, Math.sin(a[0].getDouble("v"))));
		namespace.addConstructor(FunctionParameters.function("cos").addType(DOUBLE).build(), a -> new Value(DOUBLE, Math.cos(a[0].getDouble("v"))));
		namespace.addConstructor(FunctionParameters.function("tan").addType(DOUBLE).build(), a -> new Value(DOUBLE, Math.tan(a[0].getDouble("v"))));
	}

	public static void definePi(Script script)
	{
		Namespace namespace = script.namespace;
		namespace.addValue("pi", new Value(DOUBLE, Math.PI));
	}
}
