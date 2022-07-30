package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.type.Type;
import steve6472.scriptit.expressions.Expression;
import steve6472.scriptit.expressions.Function;
import steve6472.scriptit.expressions.OverloadFunction;
import steve6472.scriptit.tokenizer.*;

/**********************
 * Created by steve6472
 * On date: 5/10/2022
 * Project: ScriptIt
 *
 ***********************/
public class OverloadParslet implements PrefixParselet
{
	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token)
	{
		String typeName = parser.tokenizer.nextToken().sval();
		OverloadFunction.OverloadType type;

		if ("unary".equals(typeName))
		{
			type = OverloadFunction.OverloadType.UNARY;
		} else if ("binary".equals(typeName))
		{
			type = OverloadFunction.OverloadType.BINARY;
		} else
		{
			throw new RuntimeException("Invalid overload type '" + typeName + "'");
		}

		IOperator operator = parser.tokenizer.nextToken().type();
		String[][] argTypeName = FunctionParslet.parseArguments(parser);
		Expression body = parser.parse(Precedence.ANYTHING);

		if (!(body instanceof Function b))
			throw new RuntimeException("Function has no body!");

		Type[] types = new Type[argTypeName[1].length];
		String[] strings = argTypeName[0];
		for (int i = 0; i < strings.length; i++)
		{
			String s = strings[i];
			types[i] = parser.script.getWorkspace().getType(s);
			if (types[i] == null)
			{
				throw new RuntimeException("Type '" + s + "' not found");
			}
		}

		if (types.length > 1 || argTypeName[1].length > 1)
		{
			throw new RuntimeException("Overloading does not support multiple arguments yet!");
		}

		return new OverloadFunction(operator, types, argTypeName[1], b, type);
	}
}
