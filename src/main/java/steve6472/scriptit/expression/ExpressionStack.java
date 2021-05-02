package steve6472.scriptit.expression;

import java.util.ArrayList;
import java.util.List;

/**
 * https://stackoverflow.com/questions/16240014/stack-array-using-pop-and-push/17535157
 */
public class ExpressionStack
{
	private final List<Expression> stack;

	public ExpressionStack(int size)
	{
		stack = new ArrayList<>(size);
	}

	public void push(Expression exp)
	{
		stack.add(0, exp);
	}

	public Expression pop()
	{
		if (!stack.isEmpty())
		{
			Expression exp = stack.get(0);
			stack.remove(0);
			return exp;
		} else
		{
			return null;// Or any invalid value
		}
	}

	public Expression peek()
	{
		if (!stack.isEmpty())
		{
			return stack.get(0);
		} else
		{
			return null;// Or any invalid value
		}
	}

	public boolean isEmpty()
	{
		return stack.isEmpty();
	}

}