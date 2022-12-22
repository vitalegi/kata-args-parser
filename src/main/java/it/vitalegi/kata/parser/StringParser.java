package it.vitalegi.kata.parser;

public class StringParser extends Parser<String> {

	@Override
	public boolean isMatch(String formatEntry) {
		String type = getType(formatEntry);
		return type.equals("") || type.equals("&");
	}

	@Override
	public String getValue(String value) {
		return value;
	}
}
