package steve6472.scriptit.newtokenizer;

/**********************
 * Created by steve6472
 * Date: 9/3/2022
 * Project: ScriptIt
 *
 ***********************/
public interface PrefixParselet<R, D>
{
	R parse(Tokenizer tokenizer, TokenParser<R, D> parser, D data);
}
