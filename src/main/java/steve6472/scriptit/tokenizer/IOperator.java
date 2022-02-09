package steve6472.scriptit.tokenizer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**********************
 * Created by steve6472
 * On date: 2/9/2022
 * Project: ScriptIt
 *
 ***********************/
public interface IOperator
{
	Set<IOperator> OPERATOR_LIST = new HashSet<>();

	String getSymbol();

	boolean isMerge();

	static Set<IOperator> getAllOperators()
	{
		return OPERATOR_LIST;
	}

	static void addOperators(IOperator[] operators)
	{
		Collections.addAll(OPERATOR_LIST, operators);
		Tokenizer.updateMergableOperators();
	}

	static IOperator fromSymbol(String symbol)
	{
		for (IOperator tokenType : OPERATOR_LIST)
		{
			if (tokenType.getSymbol().equals(symbol))
			{
				return tokenType;
			}
		}

		return null;
	}

	int ordinal();
}
