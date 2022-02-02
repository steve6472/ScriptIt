package steve6472.scriptit.tokenizer.parslet;

import steve6472.scriptit.*;
import steve6472.scriptit.tokenizer.PrefixParselet;
import steve6472.scriptit.tokenizer.TokenParser;
import steve6472.scriptit.tokenizer.Tokenizer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 12/18/2021
 * Project: ScriptIt
 *
 ***********************/
public class ImportParslet implements PrefixParselet
{
	@Override
	public Expression parse(TokenParser parser, Tokenizer.Token token)
	{
		String type = parser.tokenizer.nextToken().sval();
		String name = parser.tokenizer.nextToken().sval();
		if (type.equals("library"))
			return new Import(ImportType.LIBRARY, name);
		else if (type.equals("type"))
		{
			parser.importedTypes.add(name);
			return new Import(ImportType.TYPE, name);
		} else
			throw new RuntimeException("Import Type '" + type + "' does not exist");
	}
}
