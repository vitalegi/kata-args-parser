package it.vitalegi.kata.parser;

import java.math.BigDecimal;

import it.vitalegi.kata.ArgsException;

public class BigDecimalParser extends Parser<BigDecimal> {

	@Override
	public boolean isMatch(String formatEntry) {
		return getType(formatEntry).equals("$");
	}

	@Override
	public BigDecimal getValue(String value) {
		try {
			return new BigDecimal(value);
		} catch (NumberFormatException e) {
			throw new ArgsException("Cannot parse value", e);
		}
	}
}
