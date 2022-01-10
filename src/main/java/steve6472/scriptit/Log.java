package steve6472.scriptit;

/**********************
 * Created by steve6472
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

	public static String black() { System.out.print(BLACK); return BLACK; }
	public static String red() { System.out.print(RED); return RED; }
	public static String green() { System.out.print(GREEN); return GREEN; }
	public static String yellow() { System.out.print(YELLOW); return YELLOW; }
	public static String blue() { System.out.print(BLUE); return BLUE; }
	public static String magenta() { System.out.print(MAGENTA); return MAGENTA; }
	public static String cyan() { System.out.print(CYAN); return CYAN; }
	public static String white() { System.out.print(WHITE); return WHITE; }
	public static String reset() { System.out.print(RESET); return RESET; }

	public static String brightBlack() { System.out.print(BRIGHT_BLACK); return BRIGHT_BLACK; }
	public static String brightRed() { System.out.print(BRIGHT_RED); return BRIGHT_RED; }
	public static String brightGreen() { System.out.print(BRIGHT_GREEN); return BRIGHT_GREEN; }
	public static String brightYellow() { System.out.print(BRIGHT_YELLOW); return BRIGHT_YELLOW; }
	public static String brightBlue() { System.out.print(BRIGHT_BLUE); return BRIGHT_BLUE; }
	public static String brightMagenta() { System.out.print(BRIGHT_MAGENTA); return BRIGHT_MAGENTA; }
	public static String brightCyan() { System.out.print(BRIGHT_CYAN); return BRIGHT_CYAN; }
	public static String brightWhite() { System.out.print(BRIGHT_WHITE); return BRIGHT_WHITE; }

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
