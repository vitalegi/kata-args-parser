package it.vitalegi.kata.parser;

import it.vitalegi.kata.ArgsException;

public class IntegerParser extends Parser<Integer> {

	@Override
	public boolean isMatch(String formatEntry) {
		return getType(formatEntry).equals( "#");
	}

	@Override
	public Integer getValue(String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new ArgsException("Cannot parse value", e);
		}
	}
}