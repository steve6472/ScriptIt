package steve6472.scriptit.executor;

import steve6472.scriptit.Expression;
import steve6472.scriptit.Result;
import steve6472.scriptit.Script;

import java.util.ArrayList;
import java.util.List;

public class Executor
{
	private final List<Result> passedResults;
	private final List<Expression> expressions;
	private int lastExecuted = 0;
	private final int totalExpresisons;

	public Executor(Expression... expressions)
	{
		this.expressions = List.of(expressions);
		this.passedResults = new ArrayList<>(expressions.length);
		totalExpresisons = expressions.length;
	}

	public Result executeSingle(Script script)
	{
		Expression expression = expressions.get(lastExecuted);
		Result result = expression.apply(script);
		passedResults.add(lastExecuted, result);

		return result;
	}

	/**
	 * @return true if should return delay, false otherwise
	 */
	public ExecutorResult executeWhatYouCan(Script script)
	{
		Result res = null;

		while ((res == null || !res.isDelay()) && lastExecuted < totalExpresisons)
		{
			res = executeSingle(script);

			if (res.isDelay())
			{
				if (Result.isDelaySkip(res))
					lastExecuted++;
			} else
			{
				lastExecuted++;
			}
		}

		if (res == null)
			return ExecutorResult.SKIP;

		return res.isDelay() ? ExecutorResult.DELAY : ExecutorResult.PASS;
	}

	public Result getResult(int index)
	{
		return passedResults.get(index);
	}

	public Result getLastResult()
	{
		return passedResults.get(passedResults.size() - 1);
	}

	public List<Expression> getExpressions()
	{
		return expressions;
	}

	public void reset()
	{
		passedResults.clear();
		lastExecuted = 0;
	}

	public int getIndex()
	{
		return lastExecuted;
	}

	public enum ExecutorResult
	{
		DELAY, SKIP, PASS;

		public boolean isDelay()
		{
			return this == DELAY;
		}

		public boolean isPassed()
		{
			return this == PASS;
		}

		public boolean isSkipped()
		{
			return this == SKIP;
		}
	}

	@Override
	public String toString()
	{
		return "Executor{" + "passedResults=" + passedResults + ", expressions=" + expressions + ", lastExecuted=" + lastExecuted + ", totalExpresisons=" + totalExpresisons + '}';
	}
}