package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.*;
import steve6472.scriptit.tokenizer.ChainedVariable;
import steve6472.scriptit.tokenizer.InfixParslet;
import steve6472.scriptit.tokenizer.Precedence;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 12/18/2021
 * Project: ScriptIt
 *
 ***********************/
public class ChainedNameParslet implements InfixParslet
{
	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token, Expression leftExpression)
	{
		if (!(leftExpression instanceof Variable))
			throw new RuntimeException("Left expression is not NameExpression (" + leftExpression + ")");
		return new ChainedVariable(leftExpression, new Variable(VariableSource.memory(token.sval())));
	}

	@Override
	public Precedence getPrecedence()
	{
		return Precedence.NAME_CHAIN;
	}
}
