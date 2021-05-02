package steve6472.scriptit.expression;

@FunctionalInterface
public interface UnaryOperatorOverloadFunction
{
	Value apply(Value itself);
}