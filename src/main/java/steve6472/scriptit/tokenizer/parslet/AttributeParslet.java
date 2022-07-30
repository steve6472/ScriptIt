package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.Log;
import steve6472.scriptit.ScriptItSettings;
import steve6472.scriptit.expressions.Attributes;
import steve6472.scriptit.expressions.Attributes.Atrbut;
import steve6472.scriptit.expressions.Expression;
import steve6472.scriptit.expressions.ValueConstant;
import steve6472.scriptit.tokenizer.Operator;
import steve6472.scriptit.tokenizer.PrefixParselet;
import steve6472.scriptit.tokenizer.TokenParser;
import steve6472.scriptit.tokenizer.Tokenizer;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472
 * On date: 7/24/2022
 * Project: ScriptIt
 *
 ***********************/
public class AttributeParslet implements PrefixParselet
{
	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token)
	{
		Tokenizer tokenizer = parser.tokenizer;

		// No attributes
		if (tokenizer.peekToken().type() == Operator.BRACKET_SQUARE_RIGHT)
		{
			Log.scriptWarning(ScriptItSettings.PARSER_WARNINGS, "Attribute Field contains no Attributes");
			return new Attributes();
		}

		List<Atrbut> attributes = new ArrayList<>();

		Tokenizer.Token nextToken = tokenizer.nextToken();
		do
		{
			String attributeName = nextToken.sval();
			Expression[] attributeArguments = parser.parseArguments(true);

			ValueConstant[] attributeArgumentsChecked = new ValueConstant[attributeArguments.length];
			for (int i = 0; i < attributeArguments.length; i++)
			{
				Expression attributeArgument = attributeArguments[i];
				if (!(attributeArgument instanceof ValueConstant vc))
				{
					throw new RuntimeException("Only constants are allowed in attribute arguments!");
				}
				attributeArgumentsChecked[i] = vc;
			}

			attributes.add(new Atrbut(attributeName, attributeArgumentsChecked));

			tokenizer.matchToken(Operator.COMMA, true);
			nextToken = tokenizer.nextToken();
		}
		while (nextToken.type() == Operator.NAME);

		return new Attributes(attributes);
	}
}
