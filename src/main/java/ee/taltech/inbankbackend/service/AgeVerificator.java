package ee.taltech.inbankbackend.service;

import java.time.LocalDate;

import com.github.vladislavgoltjajev.personalcode.exception.PersonalCodeException;
import com.github.vladislavgoltjajev.personalcode.locale.estonia.EstonianPersonalCodeParser;

import ee.taltech.inbankbackend.config.DecisionEngineConstants;

public class AgeVerificator {

	public static LocalDate extractDateOfBirth(String personalCode) {
		// Assuming that personal code is in the same format in all countries

		EstonianPersonalCodeParser parser = new EstonianPersonalCodeParser();
		LocalDate dateOfBirth = null;
		try {
			dateOfBirth = parser.getDateOfBirth(personalCode);
		} catch (PersonalCodeException e) {
			e.printStackTrace();
		}
		return dateOfBirth;
	}

	public static boolean isValidAge(String personalCode, int loanPeriod) {
		LocalDate dateOfBirth = extractDateOfBirth(personalCode);
		LocalDate maxAgeMinusLoanPeriod = DecisionEngineConstants.MAXIMUM_AGE.minusMonths(loanPeriod);

		return dateOfBirth.getYear() < DecisionEngineConstants.MINIMUM_AGE.getYear()
				&& dateOfBirth.getYear() > maxAgeMinusLoanPeriod.getYear();
	}

}
