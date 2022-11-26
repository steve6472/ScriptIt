package steve6472.scriptit;

import steve6472.scriptit.executor.ExpressionExecutor;
import steve6472.scriptit.executor.MainExecutor;
import steve6472.scriptit.expressions.*;
import steve6472.scriptit.libraries.Library;
import steve6472.scriptit.simple.Comment;
import steve6472.scriptit.tokenizer.Operator;
import steve6472.scriptit.tokenizer.Precedence;
import steve6472.scriptit.tokenizer.TokenParser;
import steve6472.scriptit.transformer.SchemeParser;
import steve6472.scriptit.transformer.parser.config.Config;
import steve6472.scriptit.type.PrimitiveTypes;
import steve6472.scriptit.type.Type;
import steve6472.scriptit.value.Value;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5/23/2021
 * Project: ScriptIt
 *
 ***********************/
public class Script
{
	static
	{
		Operator.init();
	}

	private record QueuedFunctionCall<A, B>(A executor, B mustExist)
	{
	}

	private final Workspace workspace;

	public SchemeParser schemeParser;
	public List<Config> transformerConfigs = new ArrayList<>();

	public TokenParser parser;
	public MemoryStack memory;
	MainExecutor mainExecutor;
	boolean exitOnError = false;

	public static final LinkedList<String> STACK_TRACE = new LinkedList<>();

	QueuedFunctionCall<ExpressionExecutor, Boolean> currentFunction = null;

	public Supplier<Long> delayStartSupplier = System::currentTimeMillis;
	public BiFunction<Long, Long, Boolean> shouldAdvance = (start, delay) -> System.currentTimeMillis() - start >= delay;

	public void setGetDelayStart(Supplier<Long> delayStartSupplier)
	{
		this.delayStartSupplier = delayStartSupplier;
	}

	public void setShouldAdvance(BiFunction<Long, Long, Boolean> shouldAdvance)
	{
		this.shouldAdvance = shouldAdvance;
	}

	public static Script create(Workspace workspace, File source)
	{
		String code = readFromFile(source);

		Script script = new Script(workspace);
		script.parser = new TokenParser();
		script.schemeParser = new SchemeParser();

		script.parser.setExpression(script, code);
		List<Expression> parse = script.parser.parseAll();
		script.setExpressions(parse.toArray(Expression[]::new));
		return script;
	}

	public static Script create(Workspace workspace, String code)
	{
		Script script = new Script(workspace);
		script.parser = new TokenParser();
		script.schemeParser = new SchemeParser();

		script.parser.setExpression(script, code);
		List<Expression> parse = script.parser.parseAll();
		script.setExpressions(parse.toArray(Expression[]::new));
		return script;
	}



	public static Script create(Workspace workspace, File source, TokenParser parser)
	{
		String code = readFromFile(source);

		Script script = new Script(workspace);
		script.parser = parser;
		script.schemeParser = new SchemeParser();

		script.parser.setExpression(script, code);
		List<Expression> parse = script.parser.parseAll();
		script.setExpressions(parse.toArray(Expression[]::new));
		return script;
	}

	public static Script create(Workspace workspace, String code, TokenParser parser)
	{
		Script script = new Script(workspace);
		script.parser = parser;
		script.schemeParser = new SchemeParser();

		script.parser.setExpression(script, code);
		List<Expression> parse = script.parser.parseAll();
		script.setExpressions(parse.toArray(Expression[]::new));
		return script;
	}

	public static Script createEmpty(Workspace workspace)
	{
		return new Script(workspace);
	}

	private Script(Workspace workspace)
	{
		this.workspace = workspace;
		this.memory = new MemoryStack(64);

		if (ScriptItSettings.IMPORT_PRIMITIVES)
		{
			addTypeIfFound(PrimitiveTypes.INT);
			addTypeIfFound(PrimitiveTypes.DOUBLE);
			addTypeIfFound(PrimitiveTypes.CHAR);
			addTypeIfFound(PrimitiveTypes.STRING);
			addTypeIfFound(PrimitiveTypes.BOOL);
		}
	}

	private void addTypeIfFound(Type type)
	{
		if (workspace.getType(type.getKeyword()) != null)
		{
			memory.addType(type);
		}
	}

	public void addVariable(String name, Value value)
	{
		memory.addVariable(name, value);
	}

	public void addLibrary(Library library)
	{
		memory.addLibrary(library);
	}

	public void setExpressions(String... expressions)
	{
		Expression[] lines = new Expression[expressions.length];
		for (int i = 0; i < expressions.length; i++)
		{
			lines[i] = parser.parse(Precedence.ANYTHING);
		}
		mainExecutor = new MainExecutor(lines);
	}

	public void setExpressions(Expression... expressions)
	{
		mainExecutor = new MainExecutor(expressions);
	}

	public Value runWithDelay()
	{
		if (ScriptItSettings.STACK_TRACE)
		{
			STACK_TRACE.clear();
			Expression.stackTraceDepth = 0;
		}

		Result ret;

		do
		{
			ret = mainExecutor.executeSingle(this);
		} while (mainExecutor.canExecuteMore() && !ret.isReturnValue() && !ret.isReturn());

		if (!mainExecutor.isWasLastDelay() && !mainExecutor.canExecuteMore())
		{
			mainExecutor.reset();
		}

		if (ret.isReturnValue())
			return ret.getValue();
		else
			return Value.NULL;
	}

	public MainExecutor getMainExecutor()
	{
		return mainExecutor;
	}

	public MemoryStack getMemory()
	{
		return memory;
	}

	public Workspace getWorkspace()
	{
		return workspace;
	}

	public TokenParser getParser()
	{
		return parser;
	}

	public String showCode()
	{
		StringBuilder s = new StringBuilder();
		int len = mainExecutor.getExpressions().size();
		for (int i = 0; i < len; i++)
		{
			Expression expression = mainExecutor.getExpressions().get(i);
			s.append(expression.showCode(0));
			if (!(expression instanceof Comment))
				s.append(Highlighter.END);
			if (i < len - 1)
				s.append("\n");
		}
		return s.toString();
	}

	private static String readFromFile(File file)
	{
		if (!file.exists())
			throw new IllegalArgumentException("File not found");
		if (file.isDirectory())
			throw new IllegalArgumentException("File is a directory");

		StringBuilder bobTheBuilder = new StringBuilder();

		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String s;
			while ((s = reader.readLine()) != null)
			{
				bobTheBuilder.append(s).append("\n");
			}
			reader.close();

		} catch (IOException e)
		{
			e.printStackTrace();
		}

		return bobTheBuilder.toString();
	}
}
