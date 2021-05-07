package steve6472.scriptit;

import java.util.ArrayList;
import java.util.List;

/**
 * https://stackoverflow.com/questions/16240014/stack-array-using-pop-and-push/17535157
 */
public class Stack<T>
{
	private final List<T> stack;

	public Stack(int size)
	{
		stack = new ArrayList<>(size);
	}

	public Stack()
	{
		stack = new ArrayList<>();
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

	public T get(int index)
	{
		return stack.get(index);
	}

	public boolean isEmpty()
	{
		return stack.isEmpty();
	}

	@Override
	public String toString()
	{
		return "Stack{" + "stack=" + stack + '}';
	}
}