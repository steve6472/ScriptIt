package steve6472.scriptit.expressions;

import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.ScriptItSettings;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
public abstract class Expression
{
	public static String depthOperator = "\t";
	public static int stackTraceDepth = 0;

	public abstract Result apply(Script script);

	public abstract String showCode(int a);

	protected void stackTrace(int depthChange)
	{
		if (!ScriptItSettings.STACK_TRACE) return;

		stackTraceDepth += depthChange;
	}

	protected void stackTrace(int depthChange, String text)
	{
		if (!ScriptItSettings.STACK_TRACE) return;

		stackTraceDepth += depthChange;

		if (text == null || text.isEmpty())
		{
			return;
		}

		Script.STACK_TRACE.add(depthOperator.repeat(Math.max(0, stackTraceDepth)) + text);
	}

	protected void stackTrace(String text)
	{
		if (!ScriptItSettings.STACK_TRACE) return;

		if (text == null || text.isEmpty())
		{
			return;
		}

		Script.STACK_TRACE.add(depthOperator.repeat(Math.max(0, stackTraceDepth)) + text);
	}

	protected String depth(int a)
	{
		return depthOperator.repeat(Math.max(0, a));
	}

	public static void setDepthOperator(String depthOperator)
	{
		Expression.depthOperator = depthOperator;
	}

	//TODO: add this from Function: protected Value typeFunction = null;
}
