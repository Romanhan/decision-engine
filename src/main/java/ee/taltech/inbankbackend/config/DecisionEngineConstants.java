package ee.taltech.inbankbackend.config;

import java.time.LocalDate;

/**
 * Holds all necessary constants for the decision engine.
 */
public class DecisionEngineConstants {
	public static final Integer MINIMUM_LOAN_AMOUNT = 2000;
	public static final Integer MAXIMUM_LOAN_AMOUNT = 10000;
	public static final Integer MAXIMUM_LOAN_PERIOD = 60;
	public static final Integer MINIMUM_LOAN_PERIOD = 12;
	public static final Integer SEGMENT_1_CREDIT_MODIFIER = 100;
	public static final Integer SEGMENT_2_CREDIT_MODIFIER = 300;
	public static final Integer SEGMENT_3_CREDIT_MODIFIER = 1000;
	public static final LocalDate MINIMUM_AGE = LocalDate.now().minusYears(18);
	public static final LocalDate MAXIMUM_AGE = LocalDate.now().minusYears(75);
	public static final String[] BALTIC_COUNTRIES = { "Estonia", "Latvia", "Lithuania" };
}
