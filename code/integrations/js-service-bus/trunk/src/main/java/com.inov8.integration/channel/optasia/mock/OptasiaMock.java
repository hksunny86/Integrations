package com.inov8.integration.channel.optasia.mock;

import java.awt.*;

public class OptasiaMock {

    public String offerListForCommodity() {
        String offerListForCommodity = "{\n" +
                "    \"identityValue\": \"270123456789\",\n" +
                "    \"identityType\": \"msisdn\",\n" +
                "    \"origSource\": \"mobileApp\",\n" +
                "    \"receivedTimestamp\": \"2021-07-20T13:10:38.738+03:00\",\n" +
                "    \"eligibilityStatus\": [\n" +
                "        {\n" +
                "            \"isEligible\": true,\n" +
                "            \"eligibilityStatus\": \"ELIGIBLE\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"loanOffersByLoanProductGroup\": [\n" +
                "        {\n" +
                "            \"loanProductGroup\": \"consumer_group\",\n" +
                "            \"loanOffers\": [\n" +
                "                {\n" +
                "                    \"offerClass\": \"RANGE\",\n" +
                "                    \"offerName\": \"CASHCONS1000\",\n" +
                "                    \"currencyCode\": \"USD\",\n" +
                "                    \"principalFrom\": 50,\n" +
                "                    \"principalTo\": 1000,\n" +
                "                    \"setupFees\": 0,\n" +
                "                    \"commodityType\": \"CASH\",\n" +
                "                    \"loanPlanId\": 1,\n" +
                "                    \"loanPlanName\": \"Weekly plan\",\n" +
                "                    \"loanProductGroup\": \"consumer_group\",\n" +
                "                    \"maturityDetails\": {\n" +
                "                        \"maturityDuration\": 7,\n" +
                "                        \"interest\": {\n" +
                "                            \"interestName\": \"interest\",\n" +
                "                            \"interestType\": \"PERCENTAGE_OF_PRINCIPAL\",\n" +
                "                            \"interestValue\": 0.15,\n" +
                "                            \"interestVAT\": 0,\n" +
                "                            \"daysOffset\": 0,\n" +
                "                            \"interval\": 1\n" +
                "                        },\n" +
                "                        \"oneOffCharges\": [\n" +
                "                            {\n" +
                "                                \"chargeName\": \"initiation fee\",\n" +
                "                                \"chargeType\": \"FIXED_AMOUNT\",\n" +
                "                                \"chargeValue\": 5,\n" +
                "                                \"chargeVAT\": 0.05,\n" +
                "                                \"daysOffset\": 0\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"recurringCharges\": [\n" +
                "                            {\n" +
                "                                \"chargeName\": \"service fee\",\n" +
                "                                \"chargeType\": \"FIXED_AMOUNT\",\n" +
                "                                \"chargeValue\": 1,\n" +
                "                                \"chargeVAT\": 0.05,\n" +
                "                                \"daysOffset\": 0,\n" +
                "                                \"interval\": 1\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    }\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ],\n" +
                "    \"outstandingStatus\": [\n" +
                "        {\n" +
                "            \"currencyCode\": \"USD\",\n" +
                "            \"numOutstandingLoans\": 0,\n" +
                "            \"totalGross\": 0,\n" +
                "            \"totalPrincipal\": 0,\n" +
                "            \"totalSetupFees\": 0,\n" +
                "            \"totalInterest\": 0,\n" +
                "            \"totalInterestVAT\": 0,\n" +
                "            \"totalCharges\": 0,\n" +
                "            \"totalChargesVAT\": 0,\n" +
                "            \"totalPendingLoans\": 0,\n" +
                "            \"totalPendingRecoveries\": 0\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        return offerListForCommodity;
    }

    public String loanOffer() {
        String loanOffer = "{\n" +
                "  \"code\": 0,\n" +
                "  \"message\": \"ACK\",\n" +
                "  \"identityValue\": \"270123456789\",\n" +
                "  \"identityType\": \"customerIdentity\",\n" +
                "  \"origSource\": \"mobileApp\",\n" +
                "  \"receivedTimestamp\": \"2021-07-20T13:10:38.738+03:00\",\n" +
                "  \"sourceRequestId\": \"EXT123\",\n" +
                "  \"externalLoanId\": \"REF123939\",\n" +
                "  \"internalLoanId\": \"ACS2021042634324123\",\n" +
                "  \"advanceOfferId\": \"REF123\",\n" +
                "  \"offerName\": \"CASH15\"\n" +
                "}\n";

        return loanOffer;
    }

    public String callBack() {
        String callBack = "{\n" +
                "    \"responseCode\":\"00\",\n" +
                "    \"responseDescription\":\"Success\"\n" +
                "}";

        return callBack;
    }

    public String loans() {

        String loans = "{\n" +
                "  \"identityValue\": \"16505130514\",\n" +
                "  \"identityType\": \"customeridentity\",\n" +
                "  \"origSource\": \"mobileApp\",\n" +
                "  \"receivedTimestamp\": \"2021-07-20T13:10:38.738+03:00\",\n" +
                "  \"loansPerState\": [\n" +
                "    {\n" +
                "      \"loanState\": \"OPEN\",\n" +
                "      \"loans\": [\n" +
                "        {\n" +
                "          \"loan\": {\n" +
                "            \"internalLoanId\": \"123444\",\n" +
                "            \"externalLoanId\": \"REF23232323\",\n" +
                "            \"loanState\": \"OPEN\",\n" +
                "            \"loanTimestamp\": \"2021-07-20T10:10:10.348+03:00\",\n" +
                "            \"loanReason\": \"USSD_INBOUND\",\n" +
                "            \"loanOffer\": {\n" +
                "              \"advanceOfferId\": \"101\",\n" +
                "              \"offerName\": \"CASH15\",\n" +
                "              \"commodityType\": \"CASH\",\n" +
                "              \"currencyCode\": \"USD\",\n" +
                "              \"principalAmount\": 15,\n" +
                "              \"setupFees\": 5,\n" +
                "              \"loanPlanId\": 1,\n" +
                "              \"loanPlanName\": \"Standard plan\",\n" +
                "              \"loanProductGroup\": \"Standard group\"\n" +
                "            }\n" +
                "          },\n" +
                "          \"report\": {\n" +
                "            \"repayment\": {\n" +
                "              \"repaymentsCount\": 0,\n" +
                "              \"gross\": 0,\n" +
                "              \"principal\": 0,\n" +
                "              \"setupFees\": 0,\n" +
                "              \"interest\": 0,\n" +
                "              \"interestVAT\": 0,\n" +
                "              \"charges\": 0,\n" +
                "              \"chargesVAT\": 0\n" +
                "            },\n" +
                "            \"outstanding\": {\n" +
                "              \"currencyCode\": \"USD\",\n" +
                "              \"totalGross\": 16,\n" +
                "              \"totalPrincipal\": 15,\n" +
                "              \"totalSetupFees\": 1,\n" +
                "              \"totalInterest\": 0,\n" +
                "              \"totalInterestVAT\": 0,\n" +
                "              \"totalCharges\": 0,\n" +
                "              \"totalChargesVAT\": 0,\n" +
                "              \"totalPendingRecoveries\": 0\n" +
                "            },\n" +
                "            \"plan\": {\n" +
                "              \"currentPeriod\": \"MATURITY\",\n" +
                "              \"daysLeftInPeriod\": 360,\n" +
                "              \"nextPeriod\": \"DEFAULT\"\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";

        return loans;
    }

    public String projection() {
        String projection = "{\n" +
                "    \"identityValue\": \"16505130514\",\n" +
                "    \"identityType\": \" customerIdentity\",\n" +
                "    \"origSource\": \"mobileApp\",\n" +
                "    \"receivedTimestamp\": \"2021-07-01T13:10:38.738+03:00\",\n" +
                "    \"loanOffer\": {\n" +
                "        \"offerClass\": \"RANGE\",\n" +
                "        \"offerName\": \"CASH7\",\n" +
                "        \"currencyCode\": \"USD\",\n" +
                "        \"principalFrom\": 50,\n" +
                "        \"principalTo\": 500,\n" +
                "        \"setupFees\": 0,\n" +
                "        \"commodityType\": \"CASH\",\n" +
                "        \"offerName\": \"CASH10000\",\n" +
                "        \"loanPlanId\": 1,\n" +
                "        \"loanPlanName\": \"28DaysPlan\",\n" +
                "        \"loanProductGroup\": \"Standard group\",\n" +
                "        \"maturityDetails\": {\n" +
                "            \"maturityDuration\": 28,\n" +
                "            \"interest\": {\n" +
                "                \"interestName\": \"interest\",\n" +
                "                \"interestType\": \"PERCENTAGE_OF_OUTSTANDING_PRINCIPAL\",\n" +
                "                \"interestValue\": 0.035,\n" +
                "                \"interestVAT\": 0.13,\n" +
                "                \"daysOffset\": 0,\n" +
                "                \"interval\": 7\n" +
                "            },\n" +
                "            \"oneOffCharges\": {\n" +
                "                \"chargeName\": \"initiation_fee\",\n" +
                "                \"chargeType\": \"PERCENTAGE_OF_OUTSTANDING_PRINCIPAL\",\n" +
                "                \"chargeValue\": 5,\n" +
                "                \"chargeVAT\": 0.1025,\n" +
                "                \"daysOffset\": 0\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    \"periodsProjections\": [\n" +
                "        {\n" +
                "            \"periodIndex\": 1,\n" +
                "            \"periodType\": \"MATURITY\",\n" +
                "            \"periodStartTimemp\": \"2021-07-01T13:10:38.738+03:00\",\n" +
                "            \"periodEndTimestamp\": \"2021-07-28T23:59:59.999+03:00\",\n" +
                "            \"periodStartDayOfLoanIndex\": 1,\n" +
                "            \"periodEndDayOfLoanIndex\": 28,\n" +
                "            \"principal\": 10000,\n" +
                "            \"totalExpenses\": 2740.25,\n" +
                "            \"totalGross\": 12740.25,\n" +
                "            \"totalInterest\": 1400,\n" +
                "            \"totalInterestVAT\": 182,\n" +
                "            \"totalCharges\": 1025,\n" +
                "            \"totalChargesVAT\": 133.25,\n" +
                "            \"totalOneOffCharges\": [\n" +
                "                {\n" +
                "                    \"chargeName\": \"initiation_fee\",\n" +
                "                    \"chargeAmount\": 1025,\n" +
                "                    \"chargeVAT\": 133.25\n" +
                "                }\n" +
                "            ],\n" +
                "            \"milestones\": [\n" +
                "                {\n" +
                "                    \"dayOfLoan\": 1,\n" +
                "                    \"date\": \"2021-07-01T13:10:38.738+03:00\",\n" +
                "                    \"interestAdjustment\": {\n" +
                "                        \"gross\": 395.5,\n" +
                "                        \"net\": 350,\n" +
                "                        \"vat\": 45.5\n" +
                "                    },\n" +
                "                    \"chargeAdjustments\": [\n" +
                "                        {\n" +
                "                            \"name\": \"initiation_fee\",\n" +
                "                            \"gross\": 1158.25,\n" +
                "                            \"net\": 1025,\n" +
                "                            \"vat\": 133.25\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"principal\": 10000,\n" +
                "                    \"totalExpenses\": 1553.5,\n" +
                "                    \"totalGross\": 11553.75,\n" +
                "                    \"totalInterest\": 350,\n" +
                "                    \"totalInterestVAT\": 45.5,\n" +
                "                    \"totalCharges\": 1025,\n" +
                "                    \"totalChargesVAT\": 133.25\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dayOfLoan\": 8,\n" +
                "                    \"date\": \"2021-07-08T00:01:00.000+03:00\",\n" +
                "                    \"interestAdjustment\": {\n" +
                "                        \"gross\": 395.5,\n" +
                "                        \"net\": 350,\n" +
                "                        \"vat\": 45.5\n" +
                "                    },\n" +
                "                    \"principal\": 10000,\n" +
                "                    \"totalExpenses\": 11949.25,\n" +
                "                    \"totalGross\": 11949.25,\n" +
                "                    \"totalInterest\": 700,\n" +
                "                    \"totalInterestVAT\": 91,\n" +
                "                    \"totalCharges\": 1025,\n" +
                "                    \"totalChargesVAT\": 133.25\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dayOfLoan\": 15,\n" +
                "                    \"date\": \"2021-07-15T00:01:00.000+03:00\",\n" +
                "                    \"interestAdjustment\": {\n" +
                "                        \"gross\": 395.5,\n" +
                "                        \"net\": 350,\n" +
                "                        \"vat\": 45.5\n" +
                "                    },\n" +
                "                    \"principal\": 10000,\n" +
                "                    \"totalExpenses\": 2344.75,\n" +
                "                    \"totalGross\": 12344.75,\n" +
                "                    \"totalInterest\": 1050,\n" +
                "                    \"totalInterestVAT\": 136.5,\n" +
                "                    \"totalCharges\": 1025,\n" +
                "                    \"totalChargesVAT\": 133.25\n" +
                "                },\n" +
                "                {\n" +
                "                    \"dayOfLoan\": 22,\n" +
                "                    \"date\": \"2021-07-22T00:01:00.000+03:00\",\n" +
                "                    \"interestAdjustment\": {\n" +
                "                        \"gross\": 395.5,\n" +
                "                        \"net\": 350,\n" +
                "                        \"vat\": 45.5\n" +
                "                    },\n" +
                "                    \"principal\": 10000,\n" +
                "                    \"totalExpenses\": 2740.25,\n" +
                "                    \"totalGross\": 12740.25,\n" +
                "                    \"totalInterest\": 1400,\n" +
                "                    \"totalInterestVAT\": 182,\n" +
                "                    \"totalCharges\": 1025,\n" +
                "                    \"totalChargesVAT\": 133.25\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        return projection;
    }

    public String outstanding() {

        String outstanding = "{\n" +
                "    \"identityValue\":\"16505130514\",\n" +
                "    \"identityType\":\"msisdn\",\n" +
                "    \"origSource\":\"mobileapp\",\n" +
                "    \"receivedTimestamp\":\"2021-07-20T13:10:38.738+03:00\",\n" +
                "    \"outstandingPerCurrency\":[\n" +
                "        {\n" +
                "            \"currencyCode\":\"USD\",\n" +
                "            \"numOutstandingLoans\":1,\n" +
                "            \"totalGross\":12,\n" +
                "            \"totalPrincipal\":10,\n" +
                "            \"totalSetupFees\":2,\n" +
                "            \"totalInterest\":0,\n" +
                "            \"totalInterestVAT\":0,\n" +
                "            \"totalCharges\":0,\n" +
                "            \"totalChargesVAT\":0,\n" +
                "            \"totalPendingLoans\":0,\n" +
                "            \"totalPendingRecoveries\":0\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        return outstanding;
    }

    public String transaction() {
        String transactions = "{\n" +
                "  \"identityValue\": \"16505130514\",\n" +
                "  \"identityType\": \"customerIdentity\",\n" +
                "  \"origSource\": \"mobileApp\",\n" +
                "  \"receivedTimestamp\": \"2021-07-20T13:10:38.738+03:00\",\n" +
                "  \"events\": [\n" +
                "    {\n" +
                "      \"eventType\": \"ADVANCE\",\n" +
                "      \"eventTypeStatus\": \"COMPLETED\",\n" +
                "      \"eventTransactionId\": \"ABC23434\",\n" +
                "      \"thirdPartyTransactionId\": \"REF123213\",\n" +
                "      \"eventReason\": \"USSD_INBOUND\",\n" +
                "      \"eventReasonDetails\": {\n" +
                "        \"shortCode\": \"123\"\n" +
                "      },\n" +
                "      \"period\": \"MATURITY\",\n" +
                "      \"periodIndex\": 1,\n" +
                "      \"periodExpirationTimestamp\": \"2021-07-28T00:01:00.000+03:00\",\n" +
                "      \"principalAdjustment\": 15,\n" +
                "      \"principalBefore\": 0,\n" +
                "      \"principalAfter\": 15,\n" +
                "      \"setupFeesAdjustment\": 5,\n" +
                "      \"setupFeesBefore\": 0,\n" +
                "      \"setupFeesAfter\": 5,\n" +
                "      \"interestAdjustment\": 0,\n" +
                "      \"interestAdjustmentVAT\": 0,\n" +
                "      \"interestBefore\": 0,\n" +
                "      \"interestAfter\": 0,\n" +
                "      \"totalChargesAfter\": 0,\n" +
                "      \"totalChargesAdjustment\": 0,\n" +
                "      \"totalChargesAdjustmentVAT\": 0,\n" +
                "      \"totalChargesBefore\": 0,\n" +
                "      \"eventTimestamp\": \"2021-07-20T10:10:10.348+03:00\",\n" +
                "      \"receptionTimestamp\": \"2021-07-20T10:10:10.348+03:00\",\n" +
                "      \"processingTimestamp\": \"2021-07-20T10:10:10.348+03:00\",\n" +
                "      \"sourceRequestId\": \"EXT123\",\n" +
                "      \"loanReason\": \"USSD_INBOUND\",\n" +
                "      \"loanReasonDetails\": {\n" +
                "        \"shortCode\": \"123\"\n" +
                "      },\n" +
                "      \"loanTimestamp\": \"2021-07-20T10:10:10.348+03:00\",\n" +
                "      \"internalLoanId\": \"123444\",\n" +
                "      \"loanState\": \"OPEN\",\n" +
                "      \"externalLoanId\": \"REF123\",\n" +
                "      \"offerName\": \"CASH15\",\n" +
                "      \"commodityType\": \"CASH\",\n" +
                "      \"currencyCode\": \"USD\",\n" +
                "      \"principalAmount\": 15,\n" +
                "      \"setupFees\": 5,\n" +
                "      \"loanPlanId\": 1,\n" +
                "      \"loanPlanName\": \"Standard plan\",\n" +
                "      \"loanProductGroup\": \"Standard group\",\n" +
                "      \"maturityDetails\": {\n" +
                "        \"maturityDuration\": 7\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"eventType\": \"RECOVERY\",\n" +
                "      \"eventTypeStatus\": \"COMPLETED\",\n" +
                "      \"eventTransactionId\": \"ABC23434\",\n" +
                "      \"thirdPartyTransactionId\": \"REF123213\",\n" +
                "      \"eventReason\": \"USER_FUNDS\",\n" +
                "      \"eventReasonDetails\": {\n" +
                "        \"type\": \"TOPUP\"\n" +
                "      },\n" +
                "      \"period\": \"MATURITY\",\n" +
                "      \"periodIndex\": 1,\n" +
                "      \"periodExpirationTimestamp\": \"2021-07-28T00:01:00.000+03:00\",\n" +
                "      \"principalAdjustment\": 15,\n" +
                "      \"principalBefore\": 15,\n" +
                "      \"principalAfter\": 0,\n" +
                "      \"setupFeesAdjustment\": 5,\n" +
                "      \"setupFeesBefore\": 5,\n" +
                "      \"setupFeesAfter\": 0,\n" +
                "      \"interestAdjustment\": 0,\n" +
                "      \"interestAdjustmentVAT\": 0,\n" +
                "      \"interestBefore\": 0,\n" +
                "      \"interestAfter\": 0,\n" +
                "      \"totalChargesAfter\": 0,\n" +
                "      \"totalChargesAdjustment\": 0,\n" +
                "      \"totalChargesAdjustmentVAT\": 0,\n" +
                "      \"totalChargesBefore\": 0,\n" +
                "      \"eventTimestamp\": \"2021-07-20T10:10:10.348+03:00\",\n" +
                "      \"receptionTimestamp\": \"2021-07-20T10:10:10.348+03:00\",\n" +
                "      \"processingTimestamp\": \"2021-07-20T10:10:10.348+03:00\",\n" +
                "      \"sourceRequestId\": \"EXT123\",\n" +
                "      \"loanReason\": \"USSD_INBOUND\",\n" +
                "      \"loanReasonDetails\": {\n" +
                "        \"shortCode\": \"123\"\n" +
                "      },\n" +
                "      \"loanTimestamp\": \"2021-07-20T10:10:10.348+03:00\",\n" +
                "      \"internalLoanId\": \"123456\",\n" +
                "      \"externalLoanId\": \"REF123\",\n" +
                "      \"loanState\": \"CLOSED\",\n" +
                "      \"offerName\": \"CASH15\",\n" +
                "      \"commodityType\": \"CASH\",\n" +
                "      \"currencyCode\": \"USD\",\n" +
                "      \"principalAmount\": 15,\n" +
                "      \"setupFees\": 5,\n" +
                "      \"loanPlanId\": 1,\n" +
                "      \"loanPlanName\": \"Standard plan\",\n" +
                "      \"loanProductGroup\": \"Standard group\",\n" +
                "      \"maturityDetails\": {\n" +
                "        \"maturityDuration\": 7\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";

        return transactions;
    }

    public String status() {

        String status = "{\n" +
                "  \"identityValue\": \"16505130514\",\n" +
                "  \"identityType\": \"customerIdentity\",\n" +
                "  \"origSource\": \"mobileApp\",\n" +
                "  \"receivedTimestamp\": \"2021-07-20T13:10:38.738+03:00\",\n" +
                "  \"loanInfo\": {\n" +
                "    \"loan\": {\n" +
                "      \"internalLoanId\": \"123444\",\n" +
                "      \"externalLoanId\": \"REF23232323\",\n" +
                "      \"loanState\": \"OPEN\",\n" +
                "      \"loanTimestamp\": \"2021-07-20T10:10:10.348+03:00\",\n" +
                "      \"loanReason\": \"USSD_INBOUND\",\n" +
                "      \"loanOffer\": {\n" +
                "        \"offerName\": \"CASH15\",\n" +
                "        \"commodityType\": \"CASH\",\n" +
                "        \"currencyCode\": \"USD\",\n" +
                "        \"principalAmount\": 15,\n" +
                "        \"setupFees\": 5,\n" +
                "        \"loanPlanId\": 1,\n" +
                "        \"loanPlanName\": \"Default\",\n" +
                "        \"loanProductGroup\": \"Standard group\",\n" +
                "        \"maturityDetails\": {\n" +
                "          \"maturityDuration\": 360\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"report\": {\n" +
                "      \"repayment\": {\n" +
                "        \"repaymentsCount\": 1,\n" +
                "        \"gross\": 10,\n" +
                "        \"principal\": 5,\n" +
                "        \"setupFees\": 0,\n" +
                "        \"interest\": 0,\n" +
                "        \"interestVAT\": 0,\n" +
                "        \"charges\": 0,\n" +
                "        \"chargesVAT\": 0\n" +
                "      },\n" +
                "      \"outstanding\": {\n" +
                "        \"currencyCode\": \"USD\",\n" +
                "        \"totalGross\": 15,\n" +
                "        \"totalPrincipal\": 10,\n" +
                "        \"totalSetupFees\": 5,\n" +
                "        \"totalInterest\": 0,\n" +
                "        \"totalInterestVAT\": 0,\n" +
                "        \"totalCharges\": 0,\n" +
                "        \"totalChargesVAT\": 0,\n" +
                "        \"totalPendingRecoveries\": 0\n" +
                "      },\n" +
                "      \"plan\": {\n" +
                "        \"currentPeriod\": \"MATURITY\",\n" +
                "        \"daysLeftInPeriod\": 360,\n" +
                "        \"nextPeriod\": \"DEFAULT\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}\n";

        return status;
    }

    public String payment() {

        String payment = "{\n" +
                "  \"code\": 0,\n" +
                "  \"message\": \"ACK\",\n" +
                "  \"identityValue\": \"16505130514\",\n" +
                "  \"identityType\": \"customerIdentity\",\n" +
                "  \"origSource\": \"mobileApp\",\n" +
                "  \"receivedTimestamp\": \"2021-07-20T13:10:38.738+03:00\",\n" +
                "  \"sourceRequestId\": \"EXT123\"\n" +
                "}\n";

        return payment;
    }
}

