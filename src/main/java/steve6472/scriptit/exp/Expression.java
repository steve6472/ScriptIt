package steve6472.scriptit.exp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
public abstract class Expression
{
	public abstract Result apply(ExpressionExecutor executor);

	public abstract void print(int i);
}
