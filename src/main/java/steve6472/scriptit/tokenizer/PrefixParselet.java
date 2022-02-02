package steve6472.scriptit.tokenizer;

import steve6472.scriptit.Expression;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 12/18/2021
 * Project: ScriptIt
 *
 ***********************/
public interface PrefixParselet
{
	Expression parse(TokenParser parser, Tokenizer.Token token);
}
