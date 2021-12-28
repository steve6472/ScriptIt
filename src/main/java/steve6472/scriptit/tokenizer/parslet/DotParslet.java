package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.*;
import steve6472.scriptit.tokenizer.InfixParslet;
import steve6472.scriptit.tokenizer.Precedence;

/**********************
 * Created by steve6472
 * On date: 12/26/2021
 * Project: ScriptIt
 *
 ***********************/
public class DotParslet implements InfixParslet
{
	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token, Expression leftExpression)
	{
		//TODO: automatically recognize imported library from value dot
		if (leftExpression instanceof ValueConstant vc)
		{
			Expression parse = parser.parse(Precedence.ANYTHING);
			if (!(parse instanceof FunctionCall fc))
				throw new RuntimeException("Not a function call ???? (" + parse + ")");

			Expression[] exps = new Expression[fc.getArguments().length];
			DelayValue[] arguments = fc.getArguments();
			for (int i = 0; i < arguments.length; i++)
			{
				DelayValue argument = arguments[i];
				exps[i] = argument.expression;
			}

			return new FunctionCall(FunctionSource.dot(fc.getSource().functionName, vc.constant), exps);
		}

		if (!(leftExpression instanceof Variable))
			throw new RuntimeException("Not a name ????");
		Expression parse = parser.parse(Precedence.ANYTHING);
		if (!(parse instanceof FunctionCall fc))
			throw new RuntimeException("Not a function call ???? (" + parse + ")");
		Expression[] exps = new Expression[fc.getArguments().length];
		DelayValue[] arguments = fc.getArguments();
		for (int i = 0; i < arguments.length; i++)
		{
			DelayValue argument = arguments[i];
			exps[i] = argument.expression;
		}
		return new FunctionCall(FunctionSource.staticFunction(fc.getSource().functionName, parser.workspace.getLibrary(((Variable) leftExpression).source.variableName)), exps);
	}

	@Override
	public Precedence getPrecedence()
	{
		return Precedence.NAME_CHAIN;
	}
}
