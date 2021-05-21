package steve6472.scriptit.exp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/20/2021
 * Project: ScriptIt
 *
 ***********************/
@FunctionalInterface
interface IFunction
{
	double apply(ExpressionExecutor executor, double... args);
}