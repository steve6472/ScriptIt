package steve6472.scriptit.expressions;

import steve6472.scriptit.Highlighter;
import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.executor.Executor;
import steve6472.scriptit.types.PrimitiveTypes;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/22/2021
 * Project: ScriptIt
 *
 ***********************/
public class If extends Expression
{
	Expression condition, body;

	Executor conditionExecutor, bodyExecutor;

	public If(Expression condition, Expression body)
	{
		this.condition = condition;
		this.body = body;
		if (body instanceof Function f)
			f.setBody(true);

		conditionExecutor = new Executor(condition);
		bodyExecutor = new Executor(body);
	}

	@Override
	public Result apply(Script script)
	{
		if (conditionExecutor.executeWhatYouCan(script).isDelay())
			return Result.delay();

		Result conditionResult = conditionExecutor.getLastResult();

		if (conditionResult.getValue().type != PrimitiveTypes.BOOL)
			throw new RuntimeException("Incorrect type returned for condition (" + conditionResult.getValue().type + ")");

		if (!conditionResult.getValue().getBoolean())
		{
			conditionExecutor.reset();
			return Result.pass();
		}

		if (bodyExecutor.executeWhatYouCan(script).isDelay())
			return Result.delay();

//		System.out.println("BODY----------------");
//
//		for (int i = 0; i < bodyExecutor.getExpressions().size(); i++)
//		{
//			System.out.println(bodyExecutor.getExpressions().get(i) + " -> " + bodyExecutor.getResult(i));
//		}
//
//		System.out.println("----------------\n\n");

		Result result = bodyExecutor.getLastResult();

		conditionExecutor.reset();
		bodyExecutor.reset();

		return result;
	}

	@Override
	public String toString()
	{
		return "If{" + "condition=" + condition + ", body=" + body + '}';
	}

	@Override
	public String showCode(int a)
	{
		if (body instanceof Function)
			return Highlighter.IF + "if " + Highlighter.BRACET + "(" + condition.showCode(0) + Highlighter.BRACET + ")\n" + Highlighter.BRACET + depth(a - 1) + "{" + Highlighter.RESET + "\n" + body.showCode(a) + Highlighter.BRACET + depth(a - 1) + "}" + Highlighter.RESET;
		else
			return Highlighter.IF + "if " + Highlighter.BRACET + "(" + condition.showCode(0) + ")\n" + body.showCode(a) + Highlighter.RESET;
	}
}
