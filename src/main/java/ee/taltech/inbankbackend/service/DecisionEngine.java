package ee.taltech.inbankbackend.service;

import org.springframework.stereotype.Service;

import com.github.vladislavgoltjajev.personalcode.locale.estonia.EstonianPersonalCodeValidator;

import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.exceptions.InvalidLoanAmountException;
import ee.taltech.inbankbackend.exceptions.InvalidLoanPeriodException;
import ee.taltech.inbankbackend.exceptions.InvalidPersonalCodeException;
import ee.taltech.inbankbackend.exceptions.NoValidLoanException;

/**
 * A service class that provides a method for calculating an approved loan
 * amount and period for a customer. The loan amount is calculated based on the
 * customer's credit modifier, which is determined by the last four digits of
 * their ID code.
 */
@Service
public class DecisionEngine {

	private final CreditDataService creditDataService;
	private final CreditScoreCalculator creditScoreCalculator;
	// Used to check for the validity of the presented ID code.
	private final EstonianPersonalCodeValidator validator = new EstonianPersonalCodeValidator();

	public DecisionEngine(CreditDataService creditDataService, CreditScoreCalculator creditScoreCalculator) {
		this.creditDataService = creditDataService;
		this.creditScoreCalculator = creditScoreCalculator;
	}

	/**
	 * Calculates the maximum loan amount and period for the customer based on their
	 * ID code, the requested loan amount and the loan period. The loan period must
	 * be between 12 and 60 months (inclusive). The loan amount must be between 2000
	 * and 10000€ months (inclusive).
	 *
	 * @param personalCode ID code of the customer that made the request.
	 * @param loanAmount   Requested loan amount
	 * @param loanPeriod   Requested loan period
	 * @return A Decision object containing the approved loan amount and period, and
	 *         an error message (if any)
	 * @throws InvalidPersonalCodeException If the provided personal ID code is
	 *                                      invalid
	 * @throws InvalidLoanAmountException   If the requested loan amount is invalid
	 * @throws InvalidLoanPeriodException   If the requested loan period is invalid
	 * @throws NoValidLoanException         If there is no valid loan found for the
	 *                                      given ID code, loan amount and loan
	 *                                      period
	 */
	public Decision calculateApprovedLoan(String personalCode, Long loanAmount, int loanPeriod)
			throws InvalidPersonalCodeException, InvalidLoanAmountException, InvalidLoanPeriodException,
			NoValidLoanException {
		try {
			verifyInputs(personalCode, loanAmount, loanPeriod);
		} catch (Exception e) {
			return new Decision(null, null, e.getMessage());
		}
		int creditModifier = 0;
		int outputLoanAmount;

		creditModifier = creditDataService.getCreditModifier(personalCode);

		if (creditModifier == 0) {
			throw new NoValidLoanException("No valid loan found!");
		}

		while (creditScoreCalculator.calculateCreditScore(creditModifier,
				loanPeriod) < DecisionEngineConstants.MINIMUM_LOAN_AMOUNT) {
			loanPeriod++;
		}

		if (loanPeriod <= DecisionEngineConstants.MAXIMUM_LOAN_PERIOD) {
			outputLoanAmount = Math.min(DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT,
					creditScoreCalculator.calculateCreditScore(creditModifier, loanPeriod));
		} else {
			throw new NoValidLoanException("No valid loan found!");
		}

		return new Decision(outputLoanAmount, loanPeriod, null);
	}

	/**
	 * Verify that all inputs are valid according to business rules. If inputs are
	 * invalid, then throws corresponding exceptions.
	 *
	 * @param personalCode Provided personal ID code
	 * @param loanAmount   Requested loan amount
	 * @param loanPeriod   Requested loan period
	 * @throws InvalidPersonalCodeException If the provided personal ID code is
	 *                                      invalid
	 * @throws InvalidLoanAmountException   If the requested loan amount is invalid
	 * @throws InvalidLoanPeriodException   If the requested loan period is invalid
	 */
	private void verifyInputs(String personalCode, Long loanAmount, int loanPeriod)
			throws InvalidPersonalCodeException, InvalidLoanAmountException, InvalidLoanPeriodException {

		if (!validator.isValid(personalCode)) {
			throw new InvalidPersonalCodeException("Invalid personal ID code!");
		}
		if (!(DecisionEngineConstants.MINIMUM_LOAN_AMOUNT <= loanAmount)
				|| !(loanAmount <= DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT)) {
			throw new InvalidLoanAmountException("Invalid loan amount!");
		}
		if (!(DecisionEngineConstants.MINIMUM_LOAN_PERIOD <= loanPeriod)
				|| !(loanPeriod <= DecisionEngineConstants.MAXIMUM_LOAN_PERIOD)) {
			throw new InvalidLoanPeriodException("Invalid loan period!");
		}

	}
}
