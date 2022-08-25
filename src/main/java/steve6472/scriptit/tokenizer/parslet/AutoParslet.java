package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.expressions.AutoExpression;
import steve6472.scriptit.expressions.Expression;
import steve6472.scriptit.expressions.Variable;
import steve6472.scriptit.tokenizer.*;

/**
 * Created by steve6472
 * Date: 8/22/2022
 * Project: ScriptIt
 */
public class AutoParslet implements PrefixParselet
{
	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token)
	{
		return new AutoExpression();
	}
}
