package steve6472;

/**********************
 * Created by steve6472
 * On date: 12/29/2021
 * Project: ScriptIt
 *
 ***********************/
public class ToStringPrettyPrint
{
	public static String prettify(String s)
	{
		StringBuilder sb = new StringBuilder();
		int depth = 0;

		for (char c : s.toCharArray())
		{
			if (c == '{' || c == '[')
			{
				sb.append('\n').append("\t".repeat(depth)).append(c).append('\n').append("\t".repeat(depth + 1));
				depth++;
				continue;
			}

			if (c == '}' || c == ']')
			{
				depth--;
				sb.append('\n').append("\t".repeat(depth)).append(c);
				continue;
			}

			if (c == ',')
			{
				sb.append(c).append('\n').append("\t".repeat(depth));
				continue;
			}

			sb.append(c);
		}

		return sb.toString();
	}
}
