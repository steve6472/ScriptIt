package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.expressions.*;
import steve6472.scriptit.expressions.Assignment;
import steve6472.scriptit.tokenizer.*;
import steve6472.scriptit.type.ArrayType;

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
		/*
		 * a += 2;
		 */
		if (token.type() != Operator.ASSIGN)
		{
			if (leftExpression instanceof ChainedVariable)
			{
				throw new RuntimeException("Non-Simple assignment can not be a declaration");
			} else
			{

				Expression right = parser.parse(getPrecedence());

				return Assignment.newFancyStyle(leftExpression, new BinaryOperator(token.type(), leftExpression, right));
			}
		}
		else
		{
			/*
			 * obj.var = xxx;
			 * int a = xxx;
             * int[] a = xxx;
			 */
			return Assignment.newFancyStyle(leftExpression, parser.parse(getPrecedence()));
		}
	}

	@Override
	public Precedence getPrecedence()
	{
		return Precedence.ASSIGN;
	}
}
