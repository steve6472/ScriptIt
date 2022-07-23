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

		builder.append(ex.getClass().getSimpleName()).append("\n");
		builder.append(ex.getMessage()).append("\n").append("\n");

		Collections.reverse(Script.STACK_TRACE);

		for (String s : Script.STACK_TRACE)
		{
			builder.append(s).append("\n");
		}

		return builder.toString();
	}
}
