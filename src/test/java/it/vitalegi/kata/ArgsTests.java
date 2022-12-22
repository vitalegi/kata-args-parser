package it.vitalegi.kata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.Test;

class ArgsTests {

	@Test
	void test_args() {
		Args args = new Args("a#,b!,c,d&,e%,f$",
				"-a 10 -f 10000000.002251 -e 2022-12-25  -b true   -c  hello   -d aaaa");
		assertEquals(10, args.getInt("a"));
		assertEquals(true, args.getBoolean("b"));
		assertEquals("hello", args.getString("c"));
		assertEquals("aaaa", args.getString("d"));
		assertEquals(LocalDate.of(2022, 12, 25), args.getLocalDate("e"));
		assertEquals(new BigDecimal("10000000.002251"), args.getBigDecimal("f"));
	}

	@Test
	void test_getInteger_validValue_shouldReturnValue() {
		Args args = new Args("a#", "-a 10");
		assertEquals(10, args.getInt("a"));
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
	void test_extractArgs_singleValue_shouldExtractValue() {
		Args args = new Args();
		Map<String, String> values = args.extractArgs("-a 10");
		assertEquals("10", values.get("a"));
		assertEquals(1, values.size());
	}

	@Test
	void test_extractArgs_multipleValues_shouldExtractValues() {
		Args args = new Args();
		Map<String, String> values = args.extractArgs("  -a 10 -b 500000  ");
		assertEquals(2, values.size());
		assertEquals("10", values.get("a"));
		assertEquals("500000", values.get("b"));
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
		Map<String, String> values = args.extractArgs("  ");
		assertEquals(0, values.size());
	}
}
