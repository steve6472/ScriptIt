package steve6472.scriptit.instructions;

import steve6472.scriptit.Instruction;
import steve6472.scriptit.Script;
import steve6472.scriptit.ScriptIt;
import steve6472.scriptit.Workspace;
import steve6472.scriptit.expression.Expression;
import steve6472.scriptit.expression.ExpressionParser;
import steve6472.scriptit.expression.Value;

import java.util.Arrays;
import java.util.HashMap;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/7/2021
 * Project: ScriptIt
 *
 ***********************/
public class ForLoop extends Instruction
{
	private Script init, iteration;
	private final Expression condition;
	private final Script script;

	public ForLoop(Workspace workspace, Script parentScript, String line)
	{
		super(line);

		String[] mainSplit = line.split("\\)", 2);
		String body = mainSplit[1].trim();
		body = body.substring(1, body.length() - 1); // remove first '{' and last '}'

		String forThing = mainSplit[0].split("\\(", 2)[1];
		String[] forThingies = forThing.split(",");

		System.out.println(Arrays.toString(forThingies));

		if (!forThingies[0].isBlank())
			init = ScriptIt.createScript(workspace, parentScript, forThingies[0], true, ScriptIt.mainCommandMap);

		if (!forThingies[2].isBlank())
			iteration = ScriptIt.createScript(workspace, parentScript, forThingies[2], true, ScriptIt.mainCommandMap);

		ExpressionParser parser = new ExpressionParser();
		condition = parser.parse(forThingies[1]);

		this.script = ScriptIt.createScript(workspace, parentScript, body, true, ScriptIt.mainCommandMap);
	}

	@Override
	public Value execute(Script script)
	{
		if (init != null)
		{
			init.run();
			HashMap<String, Value> valueMap = init.getValueMap();
			this.script.getValueMap().forEach((k, v) -> valueMap.remove(k));

			String name = null;
			Value value = null;

			for (String s : valueMap.keySet())
			{
				name = s;
				value = valueMap.get(name);
				break;
			}

			System.out.println(name + " " + value);

			if (name != null && value != null)
			{
				this.script.addValue(name, value);
				if (this.iteration != null)
					this.iteration.addValue(name, value);
			}
		}

		m: while (condition.eval(this.script).getBoolean())
		{
			if (iteration != null)
				iteration.run();
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
