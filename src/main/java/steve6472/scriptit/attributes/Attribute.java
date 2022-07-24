package steve6472.scriptit.attributes;

import steve6472.scriptit.Script;
import steve6472.scriptit.expressions.Expression;
import steve6472.scriptit.expressions.ValueConstant;

/**
 * Created by steve6472
 * Date: 7/24/2022
 * Project: ScriptIt
 */
public abstract class Attribute
{
	public abstract void apply(Script script, Expression nextExpr, ValueConstant... constants);

	public abstract String getName();

	/**
	 *
	 * @return null for all, otherwise array of allowed expressions to follow the attribute
	 */
	public abstract Class<?>[] allowedExpressions();
}
