package ee.taltech.inbankbackend.endpoint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Holds the request data of the REST endpoint
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor // Without default constructor application doesen't work on Eclipse
public class DecisionRequest {
	private String personalCode;
	private Long loanAmount;
	private int loanPeriod;
}
