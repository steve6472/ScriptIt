package steve6472;

import steve6472.scriptit.Workspace;

import java.io.IOException;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 12/18/2021
 * Project: ScriptIt
 *
 ***********************/
public class TokenizerTest
{

	public static void main(String[] args) throws IOException
	{
		String code = """
			import library System;
			import type int;

			int a = 6;
			int b = 9;

			int c = 6 * (a - b) + -a;

			System.println("C: " + c + '!');

			if (c == -24)
			{
				System.println("True");
			};

			§ FIXME: remove the need for ; after body
			§ Probably with hard-coded exception in the do-while loop

			int c = 5;
			
			function add(int A, int B)
			{
				return A + B;
			};
			
			System.println("Expected: -1");
			return add(c, -6);
			""";
//		String code = """
//			import type int;
//			int a = 1;
//			int b = 2;
//			int c = a + b;
//			return c;
//			""";

//		String code = """
//			import library System;
//			int a = 6;
//			double c = 7;
//			int a = 213 + 2 / 0.5 + 5 + 2 * 3;
//			""";

//		StreamTokenizer stok = new StreamTokenizer(new StringReader(code));
//		stok.ordinaryChar('.');
//
//		int token = 0;
//		while ((token = stok.nextToken()) != -1)
//		{
//			System.out.println(token + " " + stok);
//		}

		Workspace workspace = new Workspace();
//
//		TokenParser.DEBUG = true;
//		TokenParser tokenParser = new TokenParser();
//		Script script = new Script(workspace);
//		tokenParser.setExpression(script, workspace, code);
//		List<Expression> parse = tokenParser.parseAll();
//		Highlighter.basicHighlight();
//		parse.forEach(System.out::println);
//		parse.forEach(c -> System.out.println(c.showCode(0)));
//		script.setExpressions(parse.toArray(Expression[]::new));
//		System.out.println("\n\n" + Log.BRIGHT_CYAN + "Running:" + Log.RESET);
//		Value value = script.runWithDelay();
//		System.out.println("\n" + Log.BRIGHT_GREEN + "Result: " + Log.RESET + value);
	}
}
