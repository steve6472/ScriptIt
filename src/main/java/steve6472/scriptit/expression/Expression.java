package steve6472.scriptit.expression;

import steve6472.scriptit.Script;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/1/2021
 * Project: ScriptIt
 *
 ***********************/
@FunctionalInterface
public interface Expression
{
	Value eval(Script scipt);
}
