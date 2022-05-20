package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.expressions.Expression;
import steve6472.scriptit.simple.Return;
import steve6472.scriptit.simple.ReturnThis;
import steve6472.scriptit.tokenizer.*;

/**********************
 * Created by steve6472
 * On date: 12/28/2021
 * Project: ScriptIt
 *
 ***********************/
public class ReturnParslet implements PrefixParselet
{
	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token)
	{
		if (parser.tokenizer.matchToken(Operator.THIS, true))
			return new ReturnThis();
		Expression ret = parser.parse(Precedence.ANYTHING);
		return new Return(ret);
	}
}
