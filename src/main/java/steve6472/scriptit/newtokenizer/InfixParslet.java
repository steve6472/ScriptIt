package steve6472.scriptit.newtokenizer;

/**********************
 * Created by steve6472
 * Date: 9/3/2022
 * Project: ScriptIt
 *
 ***********************/
public interface InfixParslet<R, D>
{
	R parse(Tokenizer tokenizer, R left, D data);

	IPrecedence getPrecedence();
}
