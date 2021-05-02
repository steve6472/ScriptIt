package steve6472.scriptit.commands;

import steve6472.scriptit.Command;
import steve6472.scriptit.Script;
import steve6472.scriptit.expression.Expression;
import steve6472.scriptit.expression.ExpressionParser;
import steve6472.scriptit.expression.Value;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/2/2021
 * Project: ScriptIt
 *
 ***********************/
public class EvalExpression extends Command
{
	Expression expression;
	String code;

	public EvalExpression(String line)
	{
		super(line);

		ExpressionParser parser = new ExpressionParser();
		expression = parser.parse(line);
		code = line;
	}

	@Override
	public Value execute(Script script)
	{
		expression.eval(script);
		return null;
	}

	@Override
	public String toString()
	{
		if (code != null)
			System.out.println(code);
		return "EvalExpression{" + "expression=" + expression + '}';
	}
}
