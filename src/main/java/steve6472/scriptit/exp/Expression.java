package steve6472.scriptit.exp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
abstract class Expression
{
	abstract double apply(ExpressionExecutor executor);

	abstract void print(int i);
}
