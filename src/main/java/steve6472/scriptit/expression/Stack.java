package steve6472.scriptit.expression;

import java.util.ArrayList;
import java.util.List;

/**
 * https://stackoverflow.com/questions/16240014/stack-array-using-pop-and-push/17535157
 */
public class Stack<T>
{
	private final int size;
	private  List<T> stack;

	public Stack(int size)
	{
		this.size = size;
		this.stack = new ArrayList<>(size);
	}

	public void push(T exp)
	{
		stack.add(0, exp);
	}

	public T pop()
	{
		if (!stack.isEmpty())
		{
			T exp = stack.get(0);
			stack.remove(0);
			return exp;
		} else
		{
			return null;// Or any invalid value
		}
	}

	public T peek()
	{
		return stack.get(0);
	}

	public boolean isEmpty()
	{
		return stack.isEmpty();
	}

	public void clear()
	{
		stack = new ArrayList<>(size);
	}

	public void printStack()
	{
		System.out.println(stack);
	}
}