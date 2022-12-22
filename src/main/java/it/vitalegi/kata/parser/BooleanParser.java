package it.vitalegi.kata.parser;

import it.vitalegi.kata.ArgsException;

public class BooleanParser extends Parser<Boolean> {

	@Override
	public boolean isMatch(String formatEntry) {
		return getType(formatEntry).equals(  "!");
	}

	@Override
	public Boolean getValue(String value) {
		if ("true".equals(value)) {
			return true;
		}
		if ("false".equals(value)) {
			return false;
		}
		throw new ArgsException("Cannot parse " + value);
	}
}
