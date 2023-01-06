package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.expressions.*;
import steve6472.scriptit.expressions.Assignment;
import steve6472.scriptit.type.Type;
import steve6472.scriptit.tokenizer.*;

/**********************
 * Created by steve6472
 * On date: 12/18/2021
 * Project: ScriptIt
 *
 ***********************/
public class ChainedNameParslet implements InfixParslet
{
	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token, Expression leftExpression)
	{
		if (leftExpression instanceof AutoExpression)
		{
			return new ChainedVariable(leftExpression, new Variable(token.sval()));
		}

		if (!(leftExpression instanceof Variable var1))
		{
			throw new RuntimeException("Left expression is not NameExpression (" + leftExpression + ")");
		}

		/*
		 * Try to create un-initialized assignment if possible
		 * int a;
		 */
		Type type = parser.script.getWorkspace().getType(var1.variableName);
		if (type != null && parser.tokenizer.peekToken().type() == Operator.SEMICOLON)
		{
			String variableName = token.sval();
			return Assignment.newFancyStyle(new ChainedVariable(new Variable(type.getKeyword()), new Variable(variableName)), null);
		}

		return new ChainedVariable(leftExpression, new Variable(token.sval()));
	}

	@Override
	public Precedence getPrecedence()
	{
		return Precedence.NAME_CHAIN;
	}
}
