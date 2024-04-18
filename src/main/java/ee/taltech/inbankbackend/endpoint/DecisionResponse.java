package ee.taltech.inbankbackend.endpoint;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * Holds the response data of the REST endpoint.
 */
@Getter
@Setter
@Component
public class DecisionResponse {
	private Integer loanAmount;
	private Integer loanPeriod;
	private String errorMessage;
}
