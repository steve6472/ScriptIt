package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.*;
import steve6472.scriptit.tokenizer.Operator;
import steve6472.scriptit.tokenizer.Precedence;
import steve6472.scriptit.tokenizer.PrefixParselet;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472
 * On date: 12/28/2021
 * Project: ScriptIt
 *
 ***********************/
public class FunctionParslet implements PrefixParselet
{
	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token)
	{
		String name = parser.tokenizer.nextToken().sval();
		String[][] argTypeName = parseArguments(parser);
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

		return new DeclareFunction(name, b, argTypeName[1], types);
	}

	private String[][] parseArguments(TokenParser parser)
	{
		Tokenizer tokenizer = parser.tokenizer;
		List<Pair> args = new ArrayList<>();
		if (tokenizer.matchToken(Operator.BRACKET_LEFT, true))
		{
			if (!tokenizer.matchToken(Operator.BRACKET_RIGHT, true))
			{
				do
				{
					String type = tokenizer.nextToken().sval();
					String name = tokenizer.nextToken().sval();
					args.add(new Pair(type, name));
				} while (tokenizer.matchToken(Operator.COMMA, true));
				tokenizer.consumeToken(Operator.BRACKET_RIGHT);
			}
		}

		String[][] r = new String[2][];
		r[0] = new String[args.size()];
		r[1] = new String[args.size()];
		for (int i = 0; i < args.size(); i++)
		{
			r[0][i] = args.get(i).a;
			r[1][i] = args.get(i).b;
		}

		return r;
	}

	private record Pair(String a, String b) {}
}