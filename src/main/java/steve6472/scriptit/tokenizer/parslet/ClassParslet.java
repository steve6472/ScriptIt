package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.Log;
import steve6472.scriptit.ScriptItSettings;
import steve6472.scriptit.Type;
import steve6472.scriptit.expressions.ClassDeclaration;
import steve6472.scriptit.expressions.Expression;
import steve6472.scriptit.expressions.FunctionCall;
import steve6472.scriptit.expressions.FunctionSource;
import steve6472.scriptit.tokenizer.*;

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
		if (ScriptItSettings.PARSER_DEBUG)
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

		if (parser.tokenizer.peekToken(1).type() == Operator.NAME)
		{
			String sval = parser.tokenizer.peekToken(1).sval();

			Expression parse = parser.parse(Precedence.ANYTHING);
			if (!(parse instanceof FunctionCall fc))
			{
				throw new RuntimeException("Function call expected (constructor of '" + name + "' class)");
			}

			fc.source = FunctionSource.function(name);

			declaration.make = parse;
			declaration.makeName = sval;
			TokenParser.print(Log.WHITE + "[" + Log.BRIGHT_BLUE + "MAKE" + Log.WHITE + "]" + Log.RESET);
		}

		if (ScriptItSettings.PARSER_DEBUG)
		{
			TokenParser.print(Log.BRIGHT_BLUE + "----------------------");
			TokenParser.print("---  END OF CLASS  ---");
			TokenParser.print("----------------------" + Log.RESET);
		}

		return declaration;
	}
}
