package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.*;
import steve6472.scriptit.tokenizer.Precedence;
import steve6472.scriptit.tokenizer.PrefixParselet;

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
		Expression ret = parser.parse(Precedence.ANYTHING);
		if (ret instanceof Variable && ((Variable) ret).source.variableName.equals("this"))
			return new ReturnThis();
		return new Return(ret);
	}
}
