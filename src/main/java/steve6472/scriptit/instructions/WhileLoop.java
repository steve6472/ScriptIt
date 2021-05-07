package steve6472.scriptit.instructions;

import steve6472.scriptit.Instruction;
import steve6472.scriptit.Script;
import steve6472.scriptit.ScriptIt;
import steve6472.scriptit.Workspace;
import steve6472.scriptit.expression.Expression;
import steve6472.scriptit.expression.ExpressionParser;
import steve6472.scriptit.expression.Value;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/7/2021
 * Project: ScriptIt
 *
 ***********************/
public class WhileLoop extends Instruction
{
	private final Expression condition;
	private final Script script;

	public WhileLoop(Workspace workspace, Script parentScript, String line)
	{
		super(line);

		String[] mainSplit = line.split("\\)", 2);
		String body = mainSplit[1].trim();
		body = body.substring(1, body.length() - 1); // remove first '{' and last '}'

		String condition = mainSplit[0].split("\\(", 2)[1];

		ExpressionParser parser = new ExpressionParser();
		this.condition = parser.parse(condition);
		this.script = ScriptIt.createScript(workspace, parentScript, body, true, ScriptIt.mainCommandMap);
	}

	@Override
	public Value execute(Script script)
	{
		m: while (condition.eval(script).getBoolean())
		{
			for (Instruction c : this.script.instructions)
			{
				if (c instanceof Break)
					break m;

				if (c instanceof Continue)
					continue m;

				Value execute = c.execute(this.script);
				if (execute != null)
					return execute;
			}
		}
		return null;
	}
}
