package it.vitalegi.kata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import it.vitalegi.kata.parser.BigDecimalParser;
import it.vitalegi.kata.parser.BooleanParser;
import it.vitalegi.kata.parser.IntegerParser;
import it.vitalegi.kata.parser.LocalDateParser;
import it.vitalegi.kata.parser.Parser;
import it.vitalegi.kata.parser.StringParser;

class ArgsTests {

	@Test
	void test_args() {
		Args args = new Args("a#,b!,c,d&,e%,f$",
				"-a 10 -f 10000000.002251 -e 2022-12-25  -b true   -c  hello   -d aaaa");
		assertEquals(10, args.getInteger("a"));
		assertEquals(true, args.getBoolean("b"));
		assertEquals("hello", args.getString("c"));
		assertEquals("aaaa", args.getString("d"));
		assertEquals(LocalDate.of(2022, 12, 25), args.getLocalDate("e"));
		assertEquals(new BigDecimal("10000000.002251"), args.getBigDecimal("f"));
	}

	@Test
	void test_extractFields() {
		Args args = new Args();
		Map<String, Parser<?>> fields = args.extractFields("a#,b!,c,d&,e%,f$,a2#,b2!,c2,d2&, e2%,f2$ ,abcd");
		assertEquals(IntegerParser.class, fields.get("a").getClass());
		assertEquals(IntegerParser.class, fields.get("a2").getClass());
		assertEquals(BooleanParser.class, fields.get("b").getClass());
		assertEquals(BooleanParser.class, fields.get("b2").getClass());
		assertEquals(StringParser.class, fields.get("c").getClass());
		assertEquals(StringParser.class, fields.get("c2").getClass());
		assertEquals(StringParser.class, fields.get("d").getClass());
		assertEquals(StringParser.class, fields.get("d2").getClass());
		assertEquals(StringParser.class, fields.get("abcd").getClass());
		assertEquals(LocalDateParser.class, fields.get("e").getClass());
		assertEquals(LocalDateParser.class, fields.get("e2").getClass());
		assertEquals(BigDecimalParser.class, fields.get("f").getClass());
		assertEquals(BigDecimalParser.class, fields.get("f2").getClass());
	}

	@Test
	void test_extractFields_missingName_shouldFail() {
		Args args = new Args();
		ArgsException e = assertThrows(ArgsException.class, () -> args.extractFields("#"));
		assertEquals("Entry must have a name", e.getMessage());

		e = assertThrows(ArgsException.class, () -> args.extractFields(""));
		assertEquals("Entry is empty and cannot be parsed", e.getMessage());
	}

	@Test
	void test_getInteger_validValue_shouldReturnValue() {
		Args args = new Args("a#", "-a 10");
		assertEquals(10, args.getInteger("a"));
	}

	@Test
	void test_getIntegers_validValues_shouldReturnValues() {
		Args args = new Args("a#", "-a 10 -a 15");
		assertEquals(Arrays.asList(10, 15), args.getIntegers("a"));
	}

	@Test
	void test_getBoolean_validTrueValue_shouldReturnValue() {
		Args args = new Args("a!", "-a true");
		assertEquals(true, args.getBoolean("a"));
	}

	@Test
	void test_getBoolean_validFalseValue_shouldReturnValue() {
		Args args = new Args("a!", "-a false");
		assertEquals(false, args.getBoolean("a"));
	}

	@Test
	void test_getBoolean_invalidValue_shouldFail() {
		Args args = new Args("a!", "-a fail");
		ArgsException e = assertThrows(ArgsException.class, () -> args.getBoolean("a"));
		assertEquals("Cannot parse fail", e.getMessage());
	}

	@Test
	void test_getBooleans_validValues_shouldReturnValues() {
		Args args = new Args("a!", "-a true -a false -b true");
		assertEquals(Arrays.asList(true, false), args.getBooleans("a"));
	}

	@Test
	void test_getString_validValue_shouldReturnValue() {
		Args args = new Args("a&", "-a hello");
		assertEquals("hello", args.getString("a"));
	}

	@Test
	void test_getString_validValueWithImplicitDefinition_shouldReturnValue() {
		Args args = new Args("a", "-a hello");
		assertEquals("hello", args.getString("a"));
	}

	@Test
	void test_getString_stringHasWhitespaces_shouldReturnValue() {
		Args args = new Args("a", "-a \"hello world\"");
		assertEquals("hello world", args.getString("a"));
	}

	@Test
	void test_getString_stringHasWhitespacesAndDoublequotes_shouldReturnValue() {
		Args args = new Args("a", "-a \"hello \\\"world\\\"\"");
		assertEquals("hello \\\"world\\\"", args.getString("a"));
	}

	@Test
	void test_getStrings_multipleValues_shouldReturnValues() {
		Args args = new Args("a", "-a \"hello world\" -a foo -b bar");
		assertEquals(Arrays.asList("hello world", "foo"), args.getStrings("a"));
	}

	@Test
	void test_getLocalDate_validValue_shouldReturnValue() {
		Args args = new Args("a%", "-a 2022-12-22");
		assertEquals(LocalDate.of(2022, 12, 22), args.getLocalDate("a"));
	}

	@Test
	void test_getBigDecimal_validValue_shouldReturnValue() {
		Args args = new Args("a$", "-a 10000000.002251");
		assertEquals(new BigDecimal("10000000.002251"), args.getBigDecimal("a"));
	}

	@Test
	void test_getBigDecimal_invalidValue_shouldFail() {
		Args args = new Args("a", "-a hello");
		ArgsException e = assertThrows(ArgsException.class, () -> args.getBigDecimal("a"));
	}

	@Test
	void test_getBigDecimals_invalidValue_shouldFail() {
		Args args = new Args("a", "-a hello");
		ArgsException e = assertThrows(ArgsException.class, () -> args.getBigDecimals("a"));
	}

	@Test
	void test_extractArgs_singleValue_shouldExtractValue() {
		Args args = new Args();
		Map<String, List<String>> values = args.extractArgs("-a 10");
		assertEquals(Arrays.asList("10"), values.get("a"));
		assertEquals(1, values.size());
	}

	@Test
	void test_extractArgs_multipleValues_shouldExtractValues() {
		Args args = new Args();
		Map<String, List<String>> values = args.extractArgs("  -a 10 -b 500000  ");
		assertEquals(2, values.size());
		assertEquals(Arrays.asList("10"), values.get("a"));
		assertEquals(Arrays.asList("500000"), values.get("b"));
	}

	@Test
	void test_extractArgs_keyWithoutValue_shouldFail() {
		Args args = new Args();

		ArgsException e = assertThrows(ArgsException.class, () -> args.extractArgs("-a "));
		assertEquals("Missing value for token a", e.getMessage());
	}

	@Test
	void test_extractArgs_emptyArgs_shouldReturnEmptyMap() {
		Args args = new Args();
		Map<String, List<String>> values = args.extractArgs("  ");
		assertEquals(0, values.size());
	}
}
