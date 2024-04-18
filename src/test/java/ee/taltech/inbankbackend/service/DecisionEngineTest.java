package ee.taltech.inbankbackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.exceptions.InvalidCountryException;
import ee.taltech.inbankbackend.exceptions.InvalidCustomerAgeException;
import ee.taltech.inbankbackend.exceptions.InvalidLoanAmountException;
import ee.taltech.inbankbackend.exceptions.InvalidLoanPeriodException;
import ee.taltech.inbankbackend.exceptions.InvalidPersonalCodeException;
import ee.taltech.inbankbackend.exceptions.NoValidLoanException;

class DecisionEngineTest {

	private DecisionEngine decisionEngine;

	private String debtorPersonalCode;
	private String segment1PersonalCode;
	private String segment2PersonalCode;
	private String segment3PersonalCode;
	private String ageUnderLifetime;
	private String ageOverLifetime;

	@BeforeEach
	void setUp() {
		debtorPersonalCode = "37605030299";
		segment1PersonalCode = "50307172740";
		segment2PersonalCode = "38411266610";
		segment3PersonalCode = "35006069515";
		ageUnderLifetime = "60605206610";
		ageOverLifetime = "44110230825";

		// Didn't figure out how to inject components with Mockito,
		// didn't work with it yet.
		decisionEngine = new DecisionEngine(new CreditDataService(), new CreditScoreCalculator());
	}

	@Test
	void testDebtorPersonalCode() {
		assertThrows(NoValidLoanException.class,
				() -> decisionEngine.calculateApprovedLoan(debtorPersonalCode, 4000L, 12, "Estonia"));
	}

	@Test
	void testSegment1PersonalCode()
			throws InvalidLoanPeriodException, NoValidLoanException, InvalidPersonalCodeException,
			InvalidLoanAmountException, InvalidCountryException, InvalidCustomerAgeException {
		Decision decision = decisionEngine.calculateApprovedLoan(segment1PersonalCode, 4000L, 12, "Estonia");
		assertEquals(2000, decision.getLoanAmount());
		assertEquals(20, decision.getLoanPeriod());
	}

	@Test
	void testSegment2PersonalCode()
			throws InvalidLoanPeriodException, NoValidLoanException, InvalidPersonalCodeException,
			InvalidLoanAmountException, InvalidCountryException, InvalidCustomerAgeException {
		Decision decision = decisionEngine.calculateApprovedLoan(segment2PersonalCode, 4000L, 12, "Estonia");
		assertEquals(3600, decision.getLoanAmount());
		assertEquals(12, decision.getLoanPeriod());
	}

	@Test
	void testSegment3PersonalCode()
			throws InvalidLoanPeriodException, NoValidLoanException, InvalidPersonalCodeException,
			InvalidLoanAmountException, InvalidCountryException, InvalidCustomerAgeException {
		Decision decision = decisionEngine.calculateApprovedLoan(segment3PersonalCode, 4000L, 12, "Estonia");
		assertEquals(10000, decision.getLoanAmount());
		assertEquals(12, decision.getLoanPeriod());
	}

	@Test
	void testInvalidPersonalCode() {
		String invalidPersonalCode = "12345678901";
		assertThrows(InvalidPersonalCodeException.class,
				() -> decisionEngine.calculateApprovedLoan(invalidPersonalCode, 4000L, 12, "Estonia"));
	}

	@Test
	void testInvalidLoanAmount() {
		Long tooLowLoanAmount = DecisionEngineConstants.MINIMUM_LOAN_AMOUNT - 1L;
		Long tooHighLoanAmount = DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT + 1L;

		assertThrows(InvalidLoanAmountException.class,
				() -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, tooLowLoanAmount, 12, "Estonia"));

		assertThrows(InvalidLoanAmountException.class,
				() -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, tooHighLoanAmount, 12, "Estonia"));
	}

	@Test
	void testInvalidLoanPeriod() {
		int tooShortLoanPeriod = DecisionEngineConstants.MINIMUM_LOAN_PERIOD - 1;
		int tooLongLoanPeriod = DecisionEngineConstants.MAXIMUM_LOAN_PERIOD + 1;

		assertThrows(InvalidLoanPeriodException.class,
				() -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, 4000L, tooShortLoanPeriod, "Estonia"));

		assertThrows(InvalidLoanPeriodException.class,
				() -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, 4000L, tooLongLoanPeriod, "Estonia"));
	}

	@Test
	void testFindSuitableLoanPeriod()
			throws InvalidLoanPeriodException, NoValidLoanException, InvalidPersonalCodeException,
			InvalidLoanAmountException, InvalidCountryException, InvalidCustomerAgeException {
		Decision decision = decisionEngine.calculateApprovedLoan(segment2PersonalCode, 2000L, 12, "Estonia");
		assertEquals(3600, decision.getLoanAmount());
		assertEquals(12, decision.getLoanPeriod());
	}

	@Test
	void testNoValidLoanFound() {
		assertThrows(NoValidLoanException.class,
				() -> decisionEngine.calculateApprovedLoan(debtorPersonalCode, 10000L, 60, "Estonia"));
	}

	@Test
	void testInvalidCountryException() {
		assertThrows(InvalidCountryException.class,
				() -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, 5000L, 60, "Filnald"));
	}

	@Test
	void testInvalidAgeException() throws InvalidCustomerAgeException {
		assertThrows(InvalidCustomerAgeException.class,
				() -> decisionEngine.calculateApprovedLoan(ageUnderLifetime, 5000L, 60, "Estonia"));
		assertThrows(InvalidCustomerAgeException.class,
				() -> decisionEngine.calculateApprovedLoan(ageOverLifetime, 5000L, 60, "Estonia"));
	}

}
