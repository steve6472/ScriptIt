package steve6472.scriptit;

import steve6472.scriptit.tokenizer.IOperator;
import steve6472.scriptit.tokenizer.Operator;

import java.util.HashSet;
import java.util.Set;

/**********************
 * Created by steve6472
 * On date: 7/23/2022
 * Project: ScriptIt
 *
 ***********************/
public class ScriptItSettings
{
	public static boolean PARSER_DEBUG = false;
	public static boolean DELAY_DEBUG = false;
	public static boolean TOKENIZER_DEBUG = false;
	public static boolean STACK_TRACE = false;
	public static int MAX_STACK_TRACE_SIZE = 1024;
	public static boolean PARSER_WARNINGS = false;
	public static boolean PRIMITIVE_TYPES_MISMATCH_WARNING = true;

	public static boolean IMPORT_PRIMITIVES = true;
	public static boolean ALLOW_AUTOMATIC_CONVERSION = true;
	public static boolean ALLOW_UNSAFE = false;


	/**
	 * Used for pretty print in debug
	 */
	public static final Set<IOperator> COMPOUND_ASSINGMENT = new HashSet<>();

	static
	{
		COMPOUND_ASSINGMENT.add(Operator.ASSIGN_ADD);
		COMPOUND_ASSINGMENT.add(Operator.ASSIGN_SUB);
		COMPOUND_ASSINGMENT.add(Operator.ASSIGN_MUL);
		COMPOUND_ASSINGMENT.add(Operator.ASSIGN_DIV);
		COMPOUND_ASSINGMENT.add(Operator.ASSIGN_MOD);
		COMPOUND_ASSINGMENT.add(Operator.ASSIGN_BIT_AND);
		COMPOUND_ASSINGMENT.add(Operator.ASSIGN_BIT_OR);
		COMPOUND_ASSINGMENT.add(Operator.ASSIGN_BIT_XOR);
	}
}
