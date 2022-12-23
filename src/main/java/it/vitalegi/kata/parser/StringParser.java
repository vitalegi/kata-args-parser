package it.vitalegi.kata.parser;

public class StringParser implements Parser<String> {

	@Override
	public String getValue(String value) {
		return value;
	}
}
