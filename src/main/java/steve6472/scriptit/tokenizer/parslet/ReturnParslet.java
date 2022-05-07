package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.expressions.Expression;
import steve6472.scriptit.expressions.Variable;
import steve6472.scriptit.simple.Return;
import steve6472.scriptit.simple.ReturnThis;
import steve6472.scriptit.tokenizer.Precedence;
import steve6472.scriptit.tokenizer.PrefixParselet;
import steve6472.scriptit.tokenizer.TokenParser;
import steve6472.scriptit.tokenizer.Tokenizer;

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
