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
 * On date: 12/19/2021
 * Project: ScriptIt
 *
 ***********************/
public class NumberParslet implements PrefixParselet
{
	private final boolean isInteger;

	public NumberParslet(boolean isInteger)
	{
		this.isInteger = isInteger;
	}

	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token)
	{
		if (!isInteger)
			return new ValueConstant(() -> PrimitiveValue.newValue(PrimitiveTypes.DOUBLE, Double.parseDouble(token.sval())));
		else
			return new ValueConstant(() -> PrimitiveValue.newValue(PrimitiveTypes.INT, Integer.parseInt(token.sval())));
	}
}
