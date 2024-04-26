package ee.taltech.inbankbackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class AgeVerificationTest {

	@Test
	void testExtractDateOfBirth() {
		String idCode = "38411266610";

		LocalDate testAge = AgeVerificator.extractDateOfBirth(idCode);
		assertEquals(testAge, LocalDate.of(1984, 11, 26));
	}

	@Test
	void testIsValidAge() {
		String validAge = "38411266610";
		String ageUnderValidation = "60605206610";
		String ageOverLifetime = "44110230825";

		assertTrue(AgeVerificator.isValidAge(validAge, 12));
		assertFalse(AgeVerificator.isValidAge(ageUnderValidation, 12));
		assertFalse(AgeVerificator.isValidAge(ageOverLifetime, 12));
	}

}
