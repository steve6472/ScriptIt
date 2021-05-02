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
public class AssignValue extends Command
{
	String name;
	Expression expression;

	public AssignValue(String line)
	{
		super(line);

		String[] split = line.split("\s*=\s*", 2);
		name = split[0];
		ExpressionParser parser = new ExpressionParser();
		expression = parser.parse(split[1]);
	}

	@Override
	public Value execute(Script script)
	{
		Value eval = expression.eval(script);
		Value value = script.getValue(name);
		if (value.type != eval.type)
			throw new RuntimeException("Type mismatch");
		value.values = eval.values;
		return null;
	}

	@Override
	public String toString()
	{
		return "AssignValue{" + "name='" + name + '\'' + ", expression=" + expression + '}';
	}
}
