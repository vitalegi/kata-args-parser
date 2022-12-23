package it.vitalegi.kata;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.vitalegi.kata.parser.BigDecimalParser;
import it.vitalegi.kata.parser.BooleanParser;
import it.vitalegi.kata.parser.IntegerParser;
import it.vitalegi.kata.parser.LocalDateParser;
import it.vitalegi.kata.parser.Parser;
import it.vitalegi.kata.parser.StringParser;

public class Args {

	protected static final Map<String, Parser<?>> PARSERS;
	protected static final Parser<?> DEFAULT_PARSER = new StringParser();

	static {
		PARSERS = new HashMap<String, Parser<?>>();
		PARSERS.put("#", new IntegerParser());
		PARSERS.put("!", new BooleanParser());
		PARSERS.put("&", new StringParser());
		PARSERS.put("%", new LocalDateParser());
		PARSERS.put("$", new BigDecimalParser());
	}

	protected Map<String, Parser<?>> parsers;
	protected Map<String, List<String>> args;

	protected Args() {
	}

	public Args(String format, String args) {
		this.parsers = extractFields(format);
		this.args = extractArgs(args);
	}

	public boolean getBoolean(String name) {
		return getValue(name, Boolean.class);
	}

	public List<Boolean> getBooleans(String name) {
		return getValues(name, Boolean.class);
	}

	public int getInteger(String name) {
		return getValue(name, Integer.class);
	}

	public List<Integer> getIntegers(String name) {
		return getValues(name, Integer.class);
	}

	public String getString(String name) {
		return getValue(name, String.class);
	}

	public List<String> getStrings(String name) {
		return getValues(name, String.class);
	}

	public LocalDate getLocalDate(String name) {
		return getValue(name, LocalDate.class);
	}

	public List<LocalDate> getLocalDates(String name) {
		return getValues(name, LocalDate.class);
	}

	public BigDecimal getBigDecimal(String name) {
		return getValue(name, BigDecimal.class);
	}

	public List<BigDecimal> getBigDecimals(String name) {
		return getValues(name, BigDecimal.class);
	}

	protected Map<String, Parser<?>> extractFields(String format) {
		String[] entries = format.split(",");
		Map<String, Parser<?>> parsers = new HashMap<>();
		for (int i = 0; i < entries.length; i++) {
			String entry = entries[i].trim();
			if (entry.length() == 0) {
				throw new ArgsException("Entry is empty and cannot be parsed");
			}
			String type = entry.substring(entry.length() - 1, entry.length());
			if (PARSERS.containsKey(type)) {
				if (entry.length() == 1) {
					throw new ArgsException("Entry must have a name");
				}
				parsers.put(entry.substring(0, entry.length() - 1), PARSERS.get(type));
			} else {
				parsers.put(entry, DEFAULT_PARSER);
			}
		}
		return parsers;
	}

	protected Map<String, List<String>> extractArgs(String args) {
		Map<String, List<String>> map = new HashMap<>();

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
			if (!map.containsKey(name)) {
				map.put(name, new ArrayList<>());
			}
			map.get(name).add(value);
		}
		return map;
	}

	protected <E> E getValue(String name, Class<E> clazz) {
		Object value = getArgParser(name).getValue(getArgValue(name));
		return cast(value, clazz);
	}

	protected <E> List<E> getValues(String name, Class<E> clazz) {
		Parser<?> parser = getArgParser(name);
		return getArgValues(name).stream()//
				.map(parser::getValue)//
				.map(v -> cast(v, clazz))//
				.collect(Collectors.toList());
	}

	protected <E> E cast(Object obj, Class<E> clazz) {
		try {
			return clazz.cast(obj);
		} catch (ClassCastException e) {
			throw new ArgsException("Cannot cast value", e);
		}
	}

	protected Parser<?> getArgParser(String name) {
		Parser<?> parser = parsers.get(name);
		if (parser == null) {
			throw new ArgsException("Unknown property " + name);
		}
		return parser;
	}

	protected String getArgValue(String name) {
		return getArgValues(name).get(0);
	}

	protected List<String> getArgValues(String name) {
		List<String> values = args.get(name);
		if (values == null) {
			throw new ArgsException("Missing value for " + name);
		}
		return values;
	}
}
