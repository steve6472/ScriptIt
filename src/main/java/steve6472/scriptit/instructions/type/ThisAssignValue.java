package steve6472.scriptit.instructions.type;

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
public class ThisAssignValue extends Instruction
{
	String name;
	Expression expression;
	public Value thisValue;

	public ThisAssignValue(Script script, String line)
	{
		super(line);

		// Remove "this." and split it to value name and explression
		String[] split = line.replaceAll("^this\\.", "").split("\s*=\s*", 2);
		name = split[0];
		ExpressionParser parser = new ExpressionParser();
		expression = parser.parse(split[1]);
	}

	@Override
	public Value execute(Script script)
	{
		Value eval = expression.eval(script);
		thisValue.setValue(name, eval);
		return null;
	}

	@Override
	public String toString()
	{
		return "DeclareAssignValue{" + "name='" + name + '\'' + ", expression=" + expression + '}';
	}
}
