package ee.taltech.inbankbackend.service;

import org.springframework.stereotype.Component;

@Component
public class CreditScoreCalculator {

	/**
	 * Calculates the largest valid loan for the current credit modifier and loan
	 * period.
	 *
	 * @return Largest valid loan amount
	 */

	public int calculateCreditScore(int creditModifier, int loanPeriod) {
		// In a real implementation, this would use the retrieved credit data
		// and potentially a more complex scoring algorithm
		return creditModifier * loanPeriod;
	}
}
