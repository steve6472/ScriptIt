package steve6472.scriptit.expression;

@FunctionalInterface
public interface OperatorOverloadFunction
{
	Value apply(Value left, Value right);
}