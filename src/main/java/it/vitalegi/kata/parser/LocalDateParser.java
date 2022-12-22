package it.vitalegi.kata.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import it.vitalegi.kata.ArgsException;

public class LocalDateParser extends Parser<LocalDate> {

	@Override
	public boolean isMatch(String formatEntry) {
		return getType(formatEntry).equals( "%");
	}

	@Override
	public LocalDate getValue(String value) {
		try {
			return LocalDate.parse(value);
		} catch (DateTimeParseException e) {
			throw new ArgsException("Cannot parse value", e);
		}
	}
}
