package steve6472.scriptit.exceptions;

import steve6472.scriptit.Script;

import java.util.Collections;

/**********************
 * Created by steve6472
 * On date: 7/23/2022
 * Project: ScriptIt
 *
 ***********************/
public class ScriptItExceptionHelper
{
	public static String getHelpfully(Script script, Exception ex)
	{
		StringBuilder builder = new StringBuilder();

		if (ex != null)
		{
			builder.append(ex.getClass().getSimpleName()).append("\n");
			builder.append(ex.getMessage()).append("\n").append("\n");
		}


		Collections.reverse(Script.STACK_TRACE);

		for (String s : Script.STACK_TRACE)
		{
			builder.append(s).append("\n");
		}

		return builder.toString();
	}

	public static String getStackTrace(Script script, boolean reverse)
	{
		StringBuilder builder = new StringBuilder();

		if (reverse)
		{
			Collections.reverse(Script.STACK_TRACE);
		}

		for (String s : Script.STACK_TRACE)
		{
			builder.append(s).append("\n");
		}

		return builder.toString();
	}
}
