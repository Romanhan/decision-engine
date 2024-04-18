package ee.taltech.inbankbackend.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Holds the response data of the REST endpoint.
 */
@Getter
@AllArgsConstructor
public class Decision {
	private final Integer loanAmount;
	private final Integer loanPeriod;
	private final String errorMessage;
	private String county;

	public Decision(Integer loanAmount, Integer loanPeriod, String errorMessage) {
		this.loanAmount = loanAmount;
		this.loanPeriod = loanPeriod;
		this.errorMessage = errorMessage;
	}

}
