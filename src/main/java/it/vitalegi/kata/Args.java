package it.vitalegi.kata;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import it.vitalegi.kata.parser.BigDecimalParser;
import it.vitalegi.kata.parser.BooleanParser;
import it.vitalegi.kata.parser.IntegerParser;
import it.vitalegi.kata.parser.LocalDateParser;
import it.vitalegi.kata.parser.Parser;
import it.vitalegi.kata.parser.StringParser;

public class Args {

	protected static final Parser<?>[] PARSERS = new Parser[] { new IntegerParser(), new BooleanParser(),
			new StringParser(), new LocalDateParser(), new BigDecimalParser() };

	protected Map<String, Parser<?>> parsers;
	protected Map<String, String> args;

	protected Args() {
	}

	public Args(String format, String args) {
		this.parsers = extractFields(format);
		this.args = extractArgs(args);
	}

	protected void setFormat(String format) {
		this.parsers = extractFields(format);
	}

	protected void setArgs(String args) {
		this.args = extractArgs(args);
	}

	public Boolean getBoolean(String name) {
		return (Boolean) getArgParser(name).getValue(getArgValue(name));
	}

	public Integer getInt(String name) {
		return (Integer) getArgParser(name).getValue(getArgValue(name));
	}

	public String getString(String name) {
		return (String) getArgParser(name).getValue(getArgValue(name));
	}

	public LocalDate getLocalDate(String name) {
		return (LocalDate) getArgParser(name).getValue(getArgValue(name));
	}

	public BigDecimal getBigDecimal(String name) {
		return (BigDecimal) getArgParser(name).getValue(getArgValue(name));
	}

	protected Map<String, Parser<?>> extractFields(String format) {
		String[] entries = format.split(",");
		Map<String, Parser<?>> parsers = new HashMap<>();
		for (int i = 0; i < entries.length; i++) {
			String entry = entries[i];
			Parser<?> parser = Arrays.stream(PARSERS)//
					.filter(p -> p.isMatch(entry)).findFirst()
					.orElseThrow(() -> new ArgsException("No parser found for [" + entry + "]"));
			parsers.put(parser.getName(entry), parser);
		}
		return parsers;
	}

	protected Map<String, String> extractArgs(String args) {
		Map<String, String> map = new HashMap<>();

		for (int i = 0; i < args.length(); i++) {
			int nextTokenStart = args.indexOf('-', i);
			if (nextTokenStart == -1) {
				break;
			}
			int nextTokenEnd = args.indexOf(' ', nextTokenStart + 1);
			if (nextTokenEnd == -1) {
				throw new ArgsException("Unparsable entry from character " + nextTokenStart);
			}
			String name = args.substring(nextTokenStart + 1, nextTokenEnd);
			int nextValueStart = nextTokenEnd + 1;
			while (nextValueStart < args.length() - 1 && args.charAt(nextValueStart) == ' ') {
				nextValueStart++;
			}
			char endChar = ' ';
			if (nextValueStart < args.length() && args.charAt(nextValueStart) == '"') {
				endChar = '"';
				nextValueStart++;
			}
			int nextValueEnd = nextValueStart;
			while (nextValueEnd < args.length()) {
				if (endChar == ' ' && args.charAt(nextValueEnd) == ' ') {
					break;
				}
				if (endChar == '"' && args.charAt(nextValueEnd - 1) != '\\' && args.charAt(nextValueEnd) == '"') {
					break;
				}
				nextValueEnd++;
			}
			if (nextValueStart == nextValueEnd) {
				throw new ArgsException("Missing value for token " + name);
			}
			String value = args.substring(nextValueStart, nextValueEnd);
			i = nextValueEnd;
			map.put(name, value);
		}
		return map;
	}

	protected Parser<?> getArgParser(String name) {
		Parser<?> parser = parsers.get(name);
		if (parser == null) {
			throw new ArgsException("Unknown property " + name);
		}
		return parser;
	}

	protected String getArgValue(String name) {
		String value = args.get(name);
		if (value == null) {
			throw new ArgsException("Missing value for " + name);
		}
		return value;
	}
}
