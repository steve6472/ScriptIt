package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.*;
import steve6472.scriptit.tokenizer.PrefixParselet;
import steve6472.scriptit.types.PrimitiveTypes;

/**********************
 * Created by steve6472
 * On date: 12/26/2021
 * Project: ScriptIt
 *
 ***********************/
public class CharParslet implements PrefixParselet
{
	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token)
	{
		return new ValueConstant(Value.newValue(PrimitiveTypes.CHAR, token.sval().charAt(0)));
	}
}
