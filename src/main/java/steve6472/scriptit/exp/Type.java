package steve6472.scriptit.exp;

import java.util.HashMap;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/21/2021
 * Project: ScriptIt
 *
 ***********************/
public class Type
{
	private final String keyword;

	public HashMap<Type, HashMap<Operator, Function>> binary;
	public HashMap<Operator, Function> unary;

	public Type(String keyword)
	{
		this.keyword = keyword;
		this.binary = new HashMap<>();
		this.unary = new HashMap<>();
	}

	public void addBinaryOperator(Type rightOperandType, Operator operator, Function function)
	{
		HashMap<Operator, Function> map = binary.get(rightOperandType);
		if (map == null)
		{
			map = new HashMap<>();
		}
		map.put(operator, function);
		binary.put(rightOperandType, map);
	}

	public void addUnaryOperator(Operator operator, Function function)
	{
		unary.put(operator, function);
	}

	public String getKeyword()
	{
		return keyword;
	}
}
