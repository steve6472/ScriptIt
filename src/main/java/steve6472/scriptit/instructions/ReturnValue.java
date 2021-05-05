package steve6472.scriptit.instructions;

import steve6472.scriptit.Instruction;
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
public class ReturnValue extends Instruction
{
	Expression expression;

	public ReturnValue(String line)
	{
		super(line);

		String[] split = line.split("return\s*", 2);
		ExpressionParser parser = new ExpressionParser();
		expression = parser.parse(split[1]);
	}

	@Override
	public Value execute(Script script)
	{
		return expression.eval(script);
//		if (value.type != eval.type)
//			throw new RuntimeException("Type mismatch");
//		value.values = eval.values;
	}

	@Override
	public String toString()
	{
		return "ReturnValue{" + "expression=" + expression + '}';
	}
}
