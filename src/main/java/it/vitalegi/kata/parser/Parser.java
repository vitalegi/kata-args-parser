package it.vitalegi.kata.parser;

import it.vitalegi.kata.ArgsException;

public abstract class Parser<E> {

	public abstract boolean isMatch(String formatEntry);

	public String getName(String formatEntry) {
		if (formatEntry == null) {
			throw new ArgsException("Entry is null");
		}
		if (formatEntry.length() == 0) {
			throw new ArgsException("Entry is empty");
		}
		return formatEntry.substring(0, 1);
	}

	protected String getType(String formatEntry) {
		String name = getName(formatEntry);
		return formatEntry.substring(name.length());
	}

	public abstract E getValue(String arg);
}
