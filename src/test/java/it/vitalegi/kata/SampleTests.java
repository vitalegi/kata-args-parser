package it.vitalegi.kata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class SampleTests {

	@Test
	void test_dummy1() {
		assertEquals(10, 10);
	}

	@Test
	void test_exceptionHandling() {
		RuntimeException e = assertThrows(RuntimeException.class, () -> {
			throw new RuntimeException("AAA");
		});
		assertEquals("AAA", e.getMessage());
	}

}
