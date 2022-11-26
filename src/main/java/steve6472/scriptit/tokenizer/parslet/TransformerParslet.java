package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.ScriptItSettings;
import steve6472.scriptit.expressions.Expression;
import steve6472.scriptit.expressions.TransformerUse;
import steve6472.scriptit.tokenizer.*;

/**
 * Created by steve6472
 * Date: 11/25/2022
 * Project: ScriptIt <br>
 */
public class TransformerParslet implements PrefixParselet
{
	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token)
	{
		Tokenizer.Token nextToken = parser.tokenizer.nextToken();
		if (nextToken.type() == Operator.STRING)
		{
			if (!ScriptItSettings.ALLOW_UNSAFE_CUSTOM_TRANSFORMER)
			{
				throw new RuntimeException("Unsafe operation detected! Allow unsafe or remove operation. (transformer)");
			} else
			{
				String name = nextToken.sval();
				return new TransformerUse(name, true);
			}
		} else if (nextToken.type() == Operator.NAME)
		{
			String name = nextToken.sval();
			return new TransformerUse(name, false);
		} else
		{
			throw new RuntimeException("Invalid type for transformer");
		}
	}
}
