package steve6472.scriptit.expression;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/1/2021
 * Project: ScriptIt
 *
 ***********************/
@FunctionalInterface
public interface Constructor
{
	Value apply(Value... args);
}
