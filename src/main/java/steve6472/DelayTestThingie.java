package steve6472;

import steve6472.scriptit.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**********************
 * Created by steve6472
 * On date: 1/21/2022
 * Project: ScriptIt
 *
 ***********************/
public class DelayTestThingie
{
	private static String RESET = Log.RESET;
	private static String BINARY = Log.GREEN;
	private static String CONSTANT = Log.BLUE;
	private static String DELAY = Log.MAGENTA;
	private static String DELAY_CONSTANT = Log.YELLOW;
	private static String DELAY_EXECUTOR = Log.CYAN;

	private static int depth;

	private static void print(String s)
	{
		System.out.println("\t".repeat(depth) + s);
	}

	private static void depthInc()
	{
		depth++;
	}

	private static void depthDec()
	{
		depth--;
	}

	private static void safeSleep(long ms)
	{
		try
		{
			Thread.sleep(ms);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		MainExecutor executor = new MainExecutor(
			new BinaryOp(new ConstantNumber(2), new ConstantNumber(3), BinaryOp.TYPE_ADD),
			new Delay(500),
			new BinaryOp(new ConstantNumber(6), new ConstantNumber(3), BinaryOp.TYPE_MUL)
		);
//		MainExecutor executor = new MainExecutor(
//			new BinaryOp(new DelayConstant(2000, 3), new DelayConstant(500, 3), BinaryOp.TYPE_MUL)
//		);
//		MainExecutor executor = new MainExecutor(
//			new BinaryOp(new ConstantNumber(6), new ConstantNumber(6), BinaryOp.TYPE_MUL)
//		);

		Result result;

		do
		{
			result = executor.executeSingle();
//			System.out.println(result);

			safeSleep(50);
		} while (executor.canExecuteMore());

		print("RESULT: " + result);
	}

	interface Result {}

	static record ResultDelay(long ms, boolean skip) implements Result {}
	static record ResultDelayWait() implements Result {}
	static record ResultValue(double value) implements Result {}

	static class MainExecutor
	{
		private final List<Expression> expressions;
		private int lastExecuted = -1;
		private boolean wasLastDelay = false;
		public long nextAllowedExecute = -1;

		public MainExecutor(Expression... expressions)
		{
			this.expressions = Arrays.asList(expressions);
		}

		private long getCurrentTime()
		{
			return System.currentTimeMillis();
		}

		public boolean canExecuteMore()
		{
			return lastExecuted <= expressions.size() - 2 || wasLastDelay;
		}

		public Result executeSingle()
		{
			if (nextAllowedExecute != -1 && getCurrentTime() < nextAllowedExecute)
				return new ResultDelayWait();

			if (!wasLastDelay)
				lastExecuted++;

			wasLastDelay = false;

			Expression expression = expressions.get(lastExecuted);
			Result result = expression.execute(this);

			if (result instanceof ResultDelay del)
			{
				wasLastDelay = true;
				if (del.skip())
					lastExecuted++;
			}

			System.out.println("\n" + "-".repeat(16) + "\n------MAIN------\n" + "-".repeat(16));

			return result;
		}
	}

	interface Expression
	{
		Result execute(MainExecutor executor);
	}

	static class ConstantNumber implements Expression
	{
		private final double val;

		public ConstantNumber(double value)
		{
			this.val = value;
		}

		@Override
		public Result execute(MainExecutor executor)
		{
			print(CONSTANT + "Constant " + RESET + val);
			return new ResultValue(val);
		}
	}

	static class Delay implements Expression
	{
		private final long delay;

		public Delay(long delayMs)
		{
			this.delay = delayMs;
		}

		@Override
		public Result execute(MainExecutor executor)
		{
			print(DELAY + "Delay " + RESET + delay + "ms");
			executor.nextAllowedExecute = executor.getCurrentTime() + delay;
			return new ResultDelay(delay, true);
		}
	}

	static class DelayConstant implements Expression
	{
		private final Executor executor;

		public DelayConstant(long delay, double value)
		{
			this.executor = new Executor(new Delay(delay), new ConstantNumber(value));
		}

		@Override
		public Result execute(MainExecutor executor)
		{
			print(DELAY_CONSTANT + "Delay Constant" + RESET);
			depthInc();
			if (this.executor.executeWhatYouCan(executor))
			{
				depthDec();
				ResultDelay resultDelay = new ResultDelay(0, false);
				print(DELAY_CONSTANT + "Result from DelayConstant: " + RESET + resultDelay);
				return resultDelay;
			}
			depthDec();
			Result result = this.executor.getResult(1);
			print(DELAY_CONSTANT + "Result from DelayConstant: " + RESET + result);
			return result;
		}
	}

	static class BinaryOp implements Expression
	{
		public static final int TYPE_ADD = 1;
		public static final int TYPE_SUB = 2;
		public static final int TYPE_MUL = 4;
		public static final int TYPE_DIV = 8;

		private final Executor valExec;
		private final int type;

		public BinaryOp(Expression left, Expression right, int type)
		{
			valExec = new Executor(left, right);
			this.type = type;
		}

		@Override
		public Result execute(MainExecutor executor)
		{
			print(BINARY + "Binary Op" + RESET);
			depthInc();
			boolean bool = valExec.executeWhatYouCan(executor);
			if (bool)
			{
				depthDec();
				ResultDelay resultDelay = new ResultDelay(0, false);
				print(BINARY + "Binary result: " + RESET + resultDelay);
				return resultDelay;
			}

			double v0 = ((ResultValue) valExec.getResult(0)).value();
			double v1 = ((ResultValue) valExec.getResult(1)).value();

			Result r = switch (type)
				{
					case TYPE_ADD -> new ResultValue(v0 + v1);
					case TYPE_SUB -> new ResultValue(v0 - v1);
					case TYPE_MUL -> new ResultValue(v0 * v1);
					case TYPE_DIV -> new ResultValue(v0 / v1);
					default -> throw new IllegalStateException("Unexpected value: " + type);
				};

			valExec.reset();
			depthDec();
			print(BINARY + "Binary result: " + RESET + r);

			return r;
		}
	}

	static class Executor
	{
		private final List<Result> passedResults;
		private final List<Expression> expressions;
		private int lastExecuted = 0;
		private final int totalExpresisons;

		public Executor(Expression... expressions)
		{
			this.expressions = Arrays.asList(expressions);
			this.passedResults = new ArrayList<>(expressions.length);
			totalExpresisons = expressions.length;
		}

		public Result executeSingle(MainExecutor executor)
		{
			Expression expression = expressions.get(lastExecuted);
			Result result = expression.execute(executor);
			passedResults.add(lastExecuted, result);

			return result;
		}

		/**
		 * @return true if should return delay, false otherwise
		 */
		public boolean executeWhatYouCan(MainExecutor executor)
		{
			print(DELAY_EXECUTOR + "Delay Executor " + RESET + Integer.toHexString(hashCode()));
			print(DELAY_EXECUTOR + "Start at " + RESET + lastExecuted + "/" + (totalExpresisons - 1));
			depthInc();
			Result r = null;

			while (!(r instanceof ResultDelay) && lastExecuted < totalExpresisons)
			{
				print("Delay Executor " + lastExecuted + "/" + (totalExpresisons - 1));
				depthInc();
				r = executeSingle(executor);
				depthDec();

				if (r instanceof ResultDelay d)
				{
					if (d.skip())
						lastExecuted++;
				} else
				{
					lastExecuted++;
				}
			}

			depthDec();
			print(DELAY_EXECUTOR + "Delay Exec Result: " + RESET + r + " " + Integer.toHexString(hashCode()) + " (" + lastExecuted + "/" + (totalExpresisons - 1) + ")");

			return r instanceof ResultDelay;
		}

		public Result getResult(int index)
		{
			return passedResults.get(index);
		}

		public void reset()
		{
			passedResults.clear();
			lastExecuted = -1;
		}
	}
}
