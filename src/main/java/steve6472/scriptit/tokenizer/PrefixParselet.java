package steve6472.scriptit.tokenizer;

import steve6472.scriptit.expressions.Expression;

/**********************
 * Created by steve6472
 * On date: 12/18/2021
 * Project: ScriptIt
 *
 ***********************/
public interface PrefixParselet
{
	Expression parse(TokenParser parser, Tokenizer.Token token);
}
