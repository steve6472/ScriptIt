package steve6472.scriptit.expressions;

import steve6472.scriptit.Result;
import steve6472.scriptit.Script;
import steve6472.scriptit.Workspace;
import steve6472.scriptit.attributes.Attribute;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve6472
 * Date: 7/24/2022
 * Project: ScriptIt
 */
public class Attributes extends Expression
{
	List<Atrbut> attributeList;
	private Expression nextExpr;

	public Attributes()
	{
		attributeList = new ArrayList<>(0);
	}

	public Attributes(List<Atrbut> attributes)
	{
		this.attributeList = attributes;
	}

	public void setNextExpr(Workspace workspace, Expression expression)
	{
		this.nextExpr = expression;

		for (Atrbut atrbut : attributeList)
		{
			Attribute attribute = workspace.getAttribute(atrbut.name());
			if (attribute == null)
			{
				throw new RuntimeException("Attribute with name '%s' not found in Workspace!".formatted(atrbut.name()));
			}

			for (Class<?> aClass : attribute.allowedExpressions())
			{
				if (!aClass.isAssignableFrom(expression.getClass()))
				{
					throw new RuntimeException("Expression '%s' not allowed for Attribute '%s'".formatted(expression
						.getClass()
						.getSimpleName(), atrbut.name()));
				}
			}
		}
	}

	@Override
	public Result apply(Script script)
	{
		stackTrace(0, "Processing attributes");
		stackTrace(1);

		for (Atrbut atrbut : attributeList)
		{
			stackTrace("Processing attribute '" + atrbut.name() + "'");

			Attribute attribute = script.getWorkspace().getAttribute(atrbut.name());

			// It should never get here, but I'll leave it here just in case
			if (attribute == null)
			{
				throw new RuntimeException("Attribute with name '%s' not found in Workspace!".formatted(atrbut.name()));
			}

			attribute.apply(script, nextExpr, atrbut.arguments());
		}

		stackTrace(-1);

		return Result.pass();
	}

	@Override
	public String showCode(int a)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("^--[");
		for (int j = 0; j < attributeList.size(); j++)
		{
			Atrbut attribute = attributeList.get(j);
			builder.append(attribute.name);
			builder.append("(");

			ValueConstant[] arguments = attribute.arguments;
			for (int i = 0; i < arguments.length; i++)
			{
				builder.append(arguments[i].showCode(0));

				if (i < arguments.length - 1)
					builder.append(", ");
			}
			builder.append(")");

			if (j < attributeList.size() - 1)
				builder.append(", ");
		}
		builder.append("]");
		return builder.toString();
	}

	public record Atrbut(String name, ValueConstant... arguments) {}
}
