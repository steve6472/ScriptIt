package steve6472.scriptit;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 17.04.2020
 * Project: NoiseGenerator
 * https://en.wikipedia.org/wiki/ANSI_escape_code#Escape_sequences
 *
 ***********************/
public class Log
{
	public static boolean PRINT_INFO = false;
	public static boolean PRINT_DEBUG = false;
	public static boolean PRINT_ERROR = true;

	public static final String BLACK = "\u001b[30m";
	public static final String RED = "\u001b[31m";
	public static final String GREEN = "\u001b[32m";
	public static final String YELLOW = "\u001b[33m";
	public static final String BLUE = "\u001b[34m";
	public static final String MAGENTA = "\u001b[35m";
	public static final String CYAN = "\u001b[36m";
	public static final String WHITE = "\u001b[37m";
	public static final String RESET = "\u001b[0m";

	public static final String BRIGHT_BLACK = "\u001b[30;1m";
	public static final String BRIGHT_RED = "\u001b[31;1m";
	public static final String BRIGHT_GREEN = "\u001b[32;1m";
	public static final String BRIGHT_YELLOW = "\u001b[33;1m";
	public static final String BRIGHT_BLUE = "\u001b[34;1m";
	public static final String BRIGHT_MAGENTA = "\u001b[35;1m";
	public static final String BRIGHT_CYAN = "\u001b[36;1m";
	public static final String BRIGHT_WHITE = "\u001b[37;1m";

	public static void black() { System.out.print(BLACK); }
	public static void red() { System.out.print(RED); }
	public static void green() { System.out.print(GREEN); }
	public static void yellow() { System.out.print(YELLOW); }
	public static void blue() { System.out.print(BLUE); }
	public static void magenta() { System.out.print(MAGENTA); }
	public static void cyan() { System.out.print(CYAN); }
	public static void white() { System.out.print(WHITE); }
	public static void reset() { System.out.print(RESET); }

	public static void brightBlack() { System.out.print(BRIGHT_BLACK); }
	public static void brightRed() { System.out.print(BRIGHT_RED); }
	public static void brightGreen() { System.out.print(BRIGHT_GREEN); }
	public static void brightYellow() { System.out.print(BRIGHT_YELLOW); }
	public static void brightBlue() { System.out.print(BRIGHT_BLUE); }
	public static void brightMagenta() { System.out.print(BRIGHT_MAGENTA); }
	public static void brightCyan() { System.out.print(BRIGHT_CYAN); }
	public static void brightWhite() { System.out.print(BRIGHT_WHITE); }

	public static void info(String text)
	{
		if (PRINT_INFO)
			System.out.println(WHITE + text);
	}

	public static void debug(String text)
	{
		if (PRINT_DEBUG)
			System.out.println(BRIGHT_WHITE + text + WHITE);
	}

	public static void err(String text)
	{
		if (PRINT_ERROR)
			System.out.println(RED + text + WHITE);
	}

}
