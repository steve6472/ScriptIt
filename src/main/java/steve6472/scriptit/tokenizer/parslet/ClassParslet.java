package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.Log;
import steve6472.scriptit.Type;
import steve6472.scriptit.expressions.ClassDeclaration;
import steve6472.scriptit.expressions.Expression;
import steve6472.scriptit.tokenizer.Operator;
import steve6472.scriptit.tokenizer.PrefixParselet;
import steve6472.scriptit.tokenizer.TokenParser;
import steve6472.scriptit.tokenizer.Tokenizer;

import java.util.List;

/**********************
 * Created by steve6472
 * On date: 5/7/2022
 * Project: ScriptIt
 *
 ***********************/
public class ClassParslet implements PrefixParselet
{
	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token)
	{
		if (TokenParser.DEBUG)
		{
			TokenParser.print(Log.BRIGHT_BLUE + "---------------");
			TokenParser.print("---  CLASS  ---");
			TokenParser.print("---------------" + Log.RESET);
		}

		String name = parser.tokenizer.nextToken().sval();
		if (parser.tokenizer.peekToken().type() == Operator.EXTENDS)
		{
			throw new RuntimeException("Inheritance not yet implemented");
		}

		Type type = new Type(name);

		ClassDeclaration declaration = new ClassDeclaration(name, type);
		parser.script.getWorkspace().addType(type);

		parser.tokenizer.consumeToken(Operator.BRACKET_CURLY_LEFT);
		List<Expression> expressions = parser.parseClass();
		declaration.add(expressions);

		if (TokenParser.DEBUG)
		{
			TokenParser.print(Log.BRIGHT_BLUE + "----------------------");
			TokenParser.print("---  END OF CLASS  ---");
			TokenParser.print("----------------------" + Log.RESET);
		}

		return declaration;
	}
}
