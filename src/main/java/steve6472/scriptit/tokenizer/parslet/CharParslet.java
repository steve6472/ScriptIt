package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.expressions.Expression;
import steve6472.scriptit.expressions.ValueConstant;
import steve6472.scriptit.tokenizer.PrefixParselet;
import steve6472.scriptit.tokenizer.TokenParser;
import steve6472.scriptit.tokenizer.Tokenizer;
import steve6472.scriptit.type.PrimitiveTypes;
import steve6472.scriptit.value.PrimitiveValue;

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
		return new ValueConstant(() -> PrimitiveValue.newValue(PrimitiveTypes.CHAR, token.sval().charAt(0)));
	}
}
