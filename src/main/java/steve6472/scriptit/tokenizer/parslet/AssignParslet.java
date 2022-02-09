package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.*;
import steve6472.scriptit.tokenizer.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 12/18/2021
 * Project: ScriptIt
 *
 ***********************/
public class AssignParslet implements InfixParslet
{

	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token, Expression leftExpression)
	{
		if (token.type() != Operator.ASSIGN)
		{
			if (leftExpression instanceof ChainedVariable)
			{
				throw new RuntimeException("Non-Simple assignment can not be a declaration");
			} else
			{
				String name = null;

				if (leftExpression instanceof Variable ne)
					name = ne.source.variableName;

				Expression right = parser.parse(getPrecedence());

				IOperator op = IOperator.fromSymbol(token.type().getSymbol().substring(0, token.type().getSymbol().length() - 1));

				return new Assignment(name, new BinaryOperator(op, leftExpression, right));
			}
		}
		else
		{
			if (leftExpression instanceof ChainedVariable cne)
			{
				Expression typeExp = cne.exp1;
				Expression nameExp = cne.exp2;

				String type = null;
				String name = null;

				if (typeExp instanceof Variable ne)
					type = ne.source.variableName;

				if (nameExp instanceof Variable ne)
					name = ne.source.variableName;

				if (type == null || name == null)
					throw new RuntimeException();

				Expression right = parser.parse(getPrecedence());

				return new Assignment(type, name, right);
			} else
			{
				String name = null;

				if (leftExpression instanceof Variable ne)
					name = ne.source.variableName;

				Expression parse = parser.parse(getPrecedence());
				return new Assignment(name, parse);
			}
		}
	}

	@Override
	public Precedence getPrecedence()
	{
		return Precedence.ASSIGN;
	}
}
