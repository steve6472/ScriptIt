package steve6472.scriptit.instructions;

import steve6472.scriptit.Instruction;
import steve6472.scriptit.Script;
import steve6472.scriptit.ScriptIt;
import steve6472.scriptit.Workspace;
import steve6472.scriptit.expression.Expression;
import steve6472.scriptit.expression.ExpressionParser;
import steve6472.scriptit.expression.Value;

import java.util.Arrays;
import java.util.regex.Pattern;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/8/2021
 * Project: ScriptIt
 *
 ***********************/
public class If extends Instruction
{
	private static final Pattern SPLIT = Pattern.compile("\\)(?=(\\s*\\{))", Pattern.MULTILINE);

	private final Script parentScript;
	private final Script scriptBody;
	private final Expression condition;

	public If(Workspace workspace, Script parentScript, String line)
	{
		super(line);
		this.parentScript = parentScript;

		String[] mainSplit = SPLIT.split(line, 2);
		String body = mainSplit[1].trim();
		body = body.substring(1, body.length() - 1).trim(); // first '{' and last '}'

		String conditionText = mainSplit[0].split("\\(", 2)[1];

		if (ScriptIt.DEBUG)
		{
			System.out.println("mainSplit = " + Arrays.toString(mainSplit));
			System.out.println("body = " + body);
			System.out.println("conditionText = " + conditionText);
		}

		ExpressionParser parser = new ExpressionParser();
		condition = parser.parse(conditionText);

		scriptBody = ScriptIt.createScript(workspace, parentScript, body, true, ScriptIt.mainCommandMap);
	}

	@Override
	public Value execute(Script script)
	{
		if (condition.eval(this.parentScript).getBoolean())
		{
			scriptBody.run();
		}
		return null;
	}
}
