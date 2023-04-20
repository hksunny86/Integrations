package com.inov8.integration.channel.optasia.mock;

import java.awt.*;

public class OptasiaMock {

    public String ecibData() {
        String ecibdata = "{\n" +
                "    \"responseCode\": \"00\",\n" +
                "    \"responseDescription\": \"Success\"\n" +
                "}";

        return ecibdata;
    }

    public String offerListForCommodity() {
        String offerListForCommodity = "{\n" +
                "    \"identityValue\": \"test\",\n" +
                "    \"identityType\": \"customerIdentity\",\n" +
                "    \"origSource\": \"mobileApp\",\n" +
                "    \"receivedTimestamp\": \"2023-03-08T19:50:53.051+05:00\",\n" +
                "    \"eligibilityStatus\": {\n" +
                "        \"eligibilityStatus\": \"ELIGIBLE\",\n" +
                "        \"isEligible\": true\n" +
                "    },\n" +
                "    \"loanOffersByProductGroup\": [\n" +
                "        {\n" +
                "            \"loanProductGroup\": \"XTRACASH\",\n" +
                "            \"loanOffers\": [\n" +
                "                {\n" +
                "                    \"offerClass\": \"RANGE\",\n" +
                "                    \"offerName\": \"XTRACASHFED1\",\n" +
                "                    \"commodityType\": \"CASH\",\n" +
                "                    \"currencyCode\": \"PKR\",\n" +
                "                    \"setupFees\": 0.0,\n" +
                "                    \"loanProductGroup\": \"XTRACASH\",\n" +
                "                    \"loanPlanId\": 1,\n" +
                "                    \"loanPlanName\": \"XtraCash plan FED1\",\n" +
                "                    \"maturityDetails\": {\n" +
                "                        \"maturityDuration\": 28,\n" +
                "                        \"oneOffCharges\": [\n" +
                "                            {\n" +
                "                                \"chargeName\": \"FED1\",\n" +
                "                                \"chargeType\": \"CUSTOM_CALCULATION\",\n" +
                "                                \"chargeValue\": null,\n" +
                "                                \"chargeVAT\": 0.0,\n" +
                "                                \"daysOffset\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"chargeName\": \"Set-up fee\",\n" +
                "                                \"chargeType\": \"CUSTOM_CALCULATION\",\n" +
                "                                \"chargeValue\": null,\n" +
                "                                \"chargeVAT\": 0.0,\n" +
                "                                \"daysOffset\": 0\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"recurringCharges\": [\n" +
                "                            {\n" +
                "                                \"chargeName\": \"Weekly Fee\",\n" +
                "                                \"chargeType\": \"PERCENTAGE_OF_TOTAL_OUTSTANDING\",\n" +
                "                                \"chargeValue\": 0.045,\n" +
                "                                \"chargeVAT\": 0.0,\n" +
                "                                \"daysOffset\": 0,\n" +
                "                                \"interval\": 7\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    \"principalFrom\": 98.0,\n" +
                "                    \"principalTo\": 98000.0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"offerClass\": \"RANGE\",\n" +
                "                    \"offerName\": \"XTRACASHFED2\",\n" +
                "                    \"commodityType\": \"CASH\",\n" +
                "                    \"currencyCode\": \"PKR\",\n" +
                "                    \"setupFees\": 0.0,\n" +
                "                    \"loanProductGroup\": \"XTRACASH\",\n" +
                "                    \"loanPlanId\": 2,\n" +
                "                    \"loanPlanName\": \"XtraCash plan FED2\",\n" +
                "                    \"maturityDetails\": {\n" +
                "                        \"maturityDuration\": 28,\n" +
                "                        \"oneOffCharges\": [\n" +
                "                            {\n" +
                "                                \"chargeName\": \"Set-up fee\",\n" +
                "                                \"chargeType\": \"CUSTOM_CALCULATION\",\n" +
                "                                \"chargeValue\": null,\n" +
                "                                \"chargeVAT\": 0.0,\n" +
                "                                \"daysOffset\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"chargeName\": \"FED2\",\n" +
                "                                \"chargeType\": \"CUSTOM_CALCULATION\",\n" +
                "                                \"chargeValue\": null,\n" +
                "                                \"chargeVAT\": 0.0,\n" +
                "                                \"daysOffset\": 0\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"recurringCharges\": [\n" +
                "                            {\n" +
                "                                \"chargeName\": \"Weekly Fee\",\n" +
                "                                \"chargeType\": \"PERCENTAGE_OF_TOTAL_OUTSTANDING\",\n" +
                "                                \"chargeValue\": 0.045,\n" +
                "                                \"chargeVAT\": 0.0,\n" +
                "                                \"daysOffset\": 0,\n" +
                "                                \"interval\": 7\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    \"principalFrom\": 98.0,\n" +
                "                    \"principalTo\": 98000.0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"offerClass\": \"RANGE\",\n" +
                "                    \"offerName\": \"XTRACASHFED3\",\n" +
                "                    \"commodityType\": \"CASH\",\n" +
                "                    \"currencyCode\": \"PKR\",\n" +
                "                    \"setupFees\": 0.0,\n" +
                "                    \"loanProductGroup\": \"XTRACASH\",\n" +
                "                    \"loanPlanId\": 3,\n" +
                "                    \"loanPlanName\": \"XtraCash plan FED3\",\n" +
                "                    \"maturityDetails\": {\n" +
                "                        \"maturityDuration\": 28,\n" +
                "                        \"oneOffCharges\": [\n" +
                "                            {\n" +
                "                                \"chargeName\": \"Set-up fee\",\n" +
                "                                \"chargeType\": \"CUSTOM_CALCULATION\",\n" +
                "                                \"chargeValue\": null,\n" +
                "                                \"chargeVAT\": 0.0,\n" +
                "                                \"daysOffset\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"chargeName\": \"FED3\",\n" +
                "                                \"chargeType\": \"CUSTOM_CALCULATION\",\n" +
                "                                \"chargeValue\": null,\n" +
                "                                \"chargeVAT\": 0.0,\n" +
                "                                \"daysOffset\": 0\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"recurringCharges\": [\n" +
                "                            {\n" +
                "                                \"chargeName\": \"Weekly Fee\",\n" +
                "                                \"chargeType\": \"PERCENTAGE_OF_TOTAL_OUTSTANDING\",\n" +
                "                                \"chargeValue\": 0.045,\n" +
                "                                \"chargeVAT\": 0.0,\n" +
                "                                \"daysOffset\": 0,\n" +
                "                                \"interval\": 7\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    \"principalFrom\": 98.0,\n" +
                "                    \"principalTo\": 98000.0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"offerClass\": \"RANGE\",\n" +
                "                    \"offerName\": \"XTRACASHFED4\",\n" +
                "                    \"commodityType\": \"CASH\",\n" +
                "                    \"currencyCode\": \"PKR\",\n" +
                "                    \"setupFees\": 0.0,\n" +
                "                    \"loanProductGroup\": \"XTRACASH\",\n" +
                "                    \"loanPlanId\": 4,\n" +
                "                    \"loanPlanName\": \"XtraCash plan FED4\",\n" +
                "                    \"maturityDetails\": {\n" +
                "                        \"maturityDuration\": 28,\n" +
                "                        \"oneOffCharges\": [\n" +
                "                            {\n" +
                "                                \"chargeName\": \"FED4\",\n" +
                "                                \"chargeType\": \"CUSTOM_CALCULATION\",\n" +
                "                                \"chargeValue\": null,\n" +
                "                                \"chargeVAT\": 0.0,\n" +
                "                                \"daysOffset\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"chargeName\": \"Set-up fee\",\n" +
                "                                \"chargeType\": \"CUSTOM_CALCULATION\",\n" +
                "                                \"chargeValue\": null,\n" +
                "                                \"chargeVAT\": 0.0,\n" +
                "                                \"daysOffset\": 0\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"recurringCharges\": [\n" +
                "                            {\n" +
                "                                \"chargeName\": \"Weekly Fee\",\n" +
                "                                \"chargeType\": \"PERCENTAGE_OF_TOTAL_OUTSTANDING\",\n" +
                "                                \"chargeValue\": 0.045,\n" +
                "                                \"chargeVAT\": 0.0,\n" +
                "                                \"daysOffset\": 0,\n" +
                "                                \"interval\": 7\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    \"principalFrom\": 98.0,\n" +
                "                    \"principalTo\": 98000.0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"offerClass\": \"RANGE\",\n" +
                "                    \"offerName\": \"XTRACASHFED5\",\n" +
                "                    \"commodityType\": \"CASH\",\n" +
                "                    \"currencyCode\": \"PKR\",\n" +
                "                    \"setupFees\": 0.0,\n" +
                "                    \"loanProductGroup\": \"XTRACASH\",\n" +
                "                    \"loanPlanId\": 5,\n" +
                "                    \"loanPlanName\": \"XtraCash plan FED5\",\n" +
                "                    \"maturityDetails\": {\n" +
                "                        \"maturityDuration\": 28,\n" +
                "                        \"oneOffCharges\": [\n" +
                "                            {\n" +
                "                                \"chargeName\": \"Set-up fee\",\n" +
                "                                \"chargeType\": \"CUSTOM_CALCULATION\",\n" +
                "                                \"chargeValue\": null,\n" +
                "                                \"chargeVAT\": 0.0,\n" +
                "                                \"daysOffset\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"chargeName\": \"FED5\",\n" +
                "                                \"chargeType\": \"CUSTOM_CALCULATION\",\n" +
                "                                \"chargeValue\": null,\n" +
                "                                \"chargeVAT\": 0.0,\n" +
                "                                \"daysOffset\": 0\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"recurringCharges\": [\n" +
                "                            {\n" +
                "                                \"chargeName\": \"Weekly Fee\",\n" +
                "                                \"chargeType\": \"PERCENTAGE_OF_TOTAL_OUTSTANDING\",\n" +
                "                                \"chargeValue\": 0.045,\n" +
                "                                \"chargeVAT\": 0.0,\n" +
                "                                \"daysOffset\": 0,\n" +
                "                                \"interval\": 7\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    \"principalFrom\": 98.0,\n" +
                "                    \"principalTo\": 98000.0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"offerClass\": \"RANGE\",\n" +
                "                    \"offerName\": \"XTRACASHFED6\",\n" +
                "                    \"commodityType\": \"CASH\",\n" +
                "                    \"currencyCode\": \"PKR\",\n" +
                "                    \"setupFees\": 0.0,\n" +
                "                    \"loanProductGroup\": \"XTRACASH\",\n" +
                "                    \"loanPlanId\": 6,\n" +
                "                    \"loanPlanName\": \"XtraCash plan FED6\",\n" +
                "                    \"maturityDetails\": {\n" +
                "                        \"maturityDuration\": 28,\n" +
                "                        \"oneOffCharges\": [\n" +
                "                            {\n" +
                "                                \"chargeName\": \"Set-up fee\",\n" +
                "                                \"chargeType\": \"CUSTOM_CALCULATION\",\n" +
                "                                \"chargeValue\": null,\n" +
                "                                \"chargeVAT\": 0.0,\n" +
                "                                \"daysOffset\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"chargeName\": \"FED6\",\n" +
                "                                \"chargeType\": \"CUSTOM_CALCULATION\",\n" +
                "                                \"chargeValue\": null,\n" +
                "                                \"chargeVAT\": 0.0,\n" +
                "                                \"daysOffset\": 0\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"recurringCharges\": [\n" +
                "                            {\n" +
                "                                \"chargeName\": \"Weekly Fee\",\n" +
                "                                \"chargeType\": \"PERCENTAGE_OF_TOTAL_OUTSTANDING\",\n" +
                "                                \"chargeValue\": 0.045,\n" +
                "                                \"chargeVAT\": 0.0,\n" +
                "                                \"daysOffset\": 0,\n" +
                "                                \"interval\": 7\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    \"principalFrom\": 98.0,\n" +
                "                    \"principalTo\": 98000.0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"offerClass\": \"RANGE\",\n" +
                "                    \"offerName\": \"XTRACASHFED7\",\n" +
                "                    \"commodityType\": \"CASH\",\n" +
                "                    \"currencyCode\": \"PKR\",\n" +
                "                    \"setupFees\": 0.0,\n" +
                "                    \"loanProductGroup\": \"XTRACASH\",\n" +
                "                    \"loanPlanId\": 7,\n" +
                "                    \"loanPlanName\": \"XtraCash plan FED7\",\n" +
                "                    \"maturityDetails\": {\n" +
                "                        \"maturityDuration\": 28,\n" +
                "                        \"oneOffCharges\": [\n" +
                "                            {\n" +
                "                                \"chargeName\": \"Set-up fee\",\n" +
                "                                \"chargeType\": \"CUSTOM_CALCULATION\",\n" +
                "                                \"chargeValue\": null,\n" +
                "                                \"chargeVAT\": 0.0,\n" +
                "                                \"daysOffset\": 0\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"chargeName\": \"FED7\",\n" +
                "                                \"chargeType\": \"CUSTOM_CALCULATION\",\n" +
                "                                \"chargeValue\": null,\n" +
                "                                \"chargeVAT\": 0.0,\n" +
                "                                \"daysOffset\": 0\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"recurringCharges\": [\n" +
                "                            {\n" +
                "                                \"chargeName\": \"Weekly Fee\",\n" +
                "                                \"chargeType\": \"PERCENTAGE_OF_TOTAL_OUTSTANDING\",\n" +
                "                                \"chargeValue\": 0.045,\n" +
                "                                \"chargeVAT\": 0.0,\n" +
                "                                \"daysOffset\": 0,\n" +
                "                                \"interval\": 7\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    \"principalFrom\": 98.0,\n" +
                "                    \"principalTo\": 98000.0\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ],\n" +
                "    \"outstandingStatus\": [\n" +
                "        {\n" +
                "            \"currencyCode\": \"PKR\",\n" +
                "            \"availableCreditLimit\": 98000.0,\n" +
                "            \"dynamicCreditLimit\": 98000.0,\n" +
                "            \"numOutstandingLoans\": 0,\n" +
                "            \"totalGross\": 0.0,\n" +
                "            \"totalPrincipal\": 0.0,\n" +
                "            \"totalSetupFees\": 0.0,\n" +
                "            \"totalInterest\": 0.0,\n" +
                "            \"totalInterestVAT\": 0.0,\n" +
                "            \"totalCharges\": 0.0,\n" +
                "            \"totalChargesVAT\": 0.0,\n" +
                "            \"totalPendingLoans\": 0.0,\n" +
                "            \"totalPendingRecoveries\": 0.0\n" +
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
                "    \"identityValue\": \"test\",\n" +
                "    \"identityType\": \"customerIdentity\",\n" +
                "    \"origSource\": \"mobileApp\",\n" +
                "    \"receivedTimestamp\": \"2023-03-08T19:51:15.907+05:00\",\n" +
                "    \"loanOffer\": {\n" +
                "        \"offerClass\": \"FIXED\",\n" +
                "        \"offerName\": \"XTRACASHFED1\",\n" +
                "        \"advanceOfferId\": \"11eBhf79Dk\",\n" +
                "        \"commodityType\": \"CASH\",\n" +
                "        \"currencyCode\": \"PKR\",\n" +
                "        \"principalAmount\": 588.0,\n" +
                "        \"setupFees\": 0.0,\n" +
                "        \"loanProductGroup\": \"XTRACASH\",\n" +
                "        \"loanPlanId\": 1,\n" +
                "        \"loanPlanName\": \"XtraCash plan FED1\",\n" +
                "        \"maturityDetails\": {\n" +
                "            \"maturityDuration\": 28,\n" +
                "            \"oneOffCharges\": [\n" +
                "                {\n" +
                "                    \"chargeName\": \"FED1\",\n" +
                "                    \"chargeType\": \"CUSTOM_CALCULATION\",\n" +
                "                    \"chargeValue\": null,\n" +
                "                    \"chargeVAT\": 0.0,\n" +
                "                    \"daysOffset\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"chargeName\": \"Set-up fee\",\n" +
                "                    \"chargeType\": \"CUSTOM_CALCULATION\",\n" +
                "                    \"chargeValue\": null,\n" +
                "                    \"chargeVAT\": 0.0,\n" +
                "                    \"daysOffset\": 0\n" +
                "                }\n" +
                "            ],\n" +
                "            \"recurringCharges\": [\n" +
                "                {\n" +
                "                    \"chargeName\": \"Weekly Fee\",\n" +
                "                    \"chargeType\": \"PERCENTAGE_OF_TOTAL_OUTSTANDING\",\n" +
                "                    \"chargeValue\": 0.045,\n" +
                "                    \"chargeVAT\": 0.0,\n" +
                "                    \"daysOffset\": 0,\n" +
                "                    \"interval\": 7\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    },\n" +
                "    \"periodsProjections\": [\n" +
                "        {\n" +
                "            \"periodIndex\": 1,\n" +
                "            \"periodType\": \"MATURITY\",\n" +
                "            \"periodStartTimestamp\": \"2023-03-08T00:00:00.000+05:00\",\n" +
                "            \"periodExpirationTimestamp\": \"2023-04-04T23:59:59.999+05:00\",\n" +
                "            \"periodStartDayOfLoanIndex\": 1,\n" +
                "            \"periodEndDayOfLoanIndex\": 28,\n" +
                "            \"currencyCode\": \"PKR\",\n" +
                "            \"totalGross\": 127.51,\n" +
                "            \"principal\": 588.0,\n" +
                "            \"totalExpenses\": 127.51,\n" +
                "            \"totalSetupFees\": 0.0,\n" +
                "            \"totalInterest\": 0.0,\n" +
                "            \"totalInterestVAT\": 0.0,\n" +
                "            \"totalCharges\": 127.51116,\n" +
                "            \"totalChargesVAT\": 0.0,\n" +
                "            \"totalOneOffCharges\": [\n" +
                "                {\n" +
                "                    \"chargeName\": \"FED1\",\n" +
                "                    \"charge\": 1.92,\n" +
                "                    \"chargeVAT\": 0.0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"chargeName\": \"Set-up fee\",\n" +
                "                    \"charge\": 10.08,\n" +
                "                    \"chargeVAT\": 0.0\n" +
                "                }\n" +
                "            ],\n" +
                "            \"totalRecurringCharges\": [\n" +
                "                {\n" +
                "                    \"chargeName\": \"Weekly Fee\",\n" +
                "                    \"charge\": 115.51116,\n" +
                "                    \"chargeVAT\": 0.0\n" +
                "                }\n" +
                "            ],\n" +
                "            \"milestones\": [\n" +
                "                {\n" +
                "                    \"date\": \"2023-03-15T00:01:00.000+05:00\",\n" +
                "                    \"dayOfLoanIndex\": 8,\n" +
                "                    \"chargeAdjustments\": [\n" +
                "                        {\n" +
                "                            \"gross\": 0.0,\n" +
                "                            \"net\": 0.0,\n" +
                "                            \"vat\": 0.0,\n" +
                "                            \"name\": \"FED1\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"gross\": 0.0,\n" +
                "                            \"net\": 0.0,\n" +
                "                            \"vat\": 0.0,\n" +
                "                            \"name\": \"Set-up fee\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"gross\": 28.215,\n" +
                "                            \"net\": 28.215,\n" +
                "                            \"vat\": 0.0,\n" +
                "                            \"name\": \"Weekly Fee\"\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"interestAdjustment\": {\n" +
                "                        \"gross\": 0.0,\n" +
                "                        \"net\": 0.0,\n" +
                "                        \"vat\": 0.0\n" +
                "                    },\n" +
                "                    \"principal\": 588.0,\n" +
                "                    \"totalGross\": 28.22,\n" +
                "                    \"totalExpenses\": 67.22,\n" +
                "                    \"totalSetupFees\": 0.0,\n" +
                "                    \"totalInterest\": 0.0,\n" +
                "                    \"totalInterestVAT\": 0.0,\n" +
                "                    \"totalCharges\": 67.215,\n" +
                "                    \"totalChargesVAT\": 0.0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"date\": \"2023-03-22T00:01:00.000+05:00\",\n" +
                "                    \"dayOfLoanIndex\": 15,\n" +
                "                    \"chargeAdjustments\": [\n" +
                "                        {\n" +
                "                            \"gross\": 0.0,\n" +
                "                            \"net\": 0.0,\n" +
                "                            \"vat\": 0.0,\n" +
                "                            \"name\": \"FED1\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"gross\": 0.0,\n" +
                "                            \"net\": 0.0,\n" +
                "                            \"vat\": 0.0,\n" +
                "                            \"name\": \"Set-up fee\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"gross\": 29.484675,\n" +
                "                            \"net\": 29.484675,\n" +
                "                            \"vat\": 0.0,\n" +
                "                            \"name\": \"Weekly Fee\"\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"interestAdjustment\": {\n" +
                "                        \"gross\": 0.0,\n" +
                "                        \"net\": 0.0,\n" +
                "                        \"vat\": 0.0\n" +
                "                    },\n" +
                "                    \"principal\": 588.0,\n" +
                "                    \"totalGross\": 29.48,\n" +
                "                    \"totalExpenses\": 96.7,\n" +
                "                    \"totalSetupFees\": 0.0,\n" +
                "                    \"totalInterest\": 0.0,\n" +
                "                    \"totalInterestVAT\": 0.0,\n" +
                "                    \"totalCharges\": 96.699675,\n" +
                "                    \"totalChargesVAT\": 0.0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"date\": \"2023-03-29T00:01:00.000+05:00\",\n" +
                "                    \"dayOfLoanIndex\": 22,\n" +
                "                    \"chargeAdjustments\": [\n" +
                "                        {\n" +
                "                            \"gross\": 0.0,\n" +
                "                            \"net\": 0.0,\n" +
                "                            \"vat\": 0.0,\n" +
                "                            \"name\": \"FED1\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"gross\": 0.0,\n" +
                "                            \"net\": 0.0,\n" +
                "                            \"vat\": 0.0,\n" +
                "                            \"name\": \"Set-up fee\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"gross\": 30.811485,\n" +
                "                            \"net\": 30.811485,\n" +
                "                            \"vat\": 0.0,\n" +
                "                            \"name\": \"Weekly Fee\"\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"interestAdjustment\": {\n" +
                "                        \"gross\": 0.0,\n" +
                "                        \"net\": 0.0,\n" +
                "                        \"vat\": 0.0\n" +
                "                    },\n" +
                "                    \"principal\": 588.0,\n" +
                "                    \"totalGross\": 30.81,\n" +
                "                    \"totalExpenses\": 127.51,\n" +
                "                    \"totalSetupFees\": 0.0,\n" +
                "                    \"totalInterest\": 0.0,\n" +
                "                    \"totalInterestVAT\": 0.0,\n" +
                "                    \"totalCharges\": 127.51116,\n" +
                "                    \"totalChargesVAT\": 0.0\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"periodIndex\": 2,\n" +
                "            \"periodType\": \"ROLLOVER\",\n" +
                "            \"periodStartTimestamp\": \"2023-03-08T00:00:00.000+05:00\",\n" +
                "            \"periodExpirationTimestamp\": \"2023-05-04T23:59:59.999+05:00\",\n" +
                "            \"periodStartDayOfLoanIndex\": 29,\n" +
                "            \"periodEndDayOfLoanIndex\": 86,\n" +
                "            \"currencyCode\": \"PKR\",\n" +
                "            \"totalGross\": 249.05,\n" +
                "            \"principal\": 588.0,\n" +
                "            \"totalExpenses\": 249.05,\n" +
                "            \"totalSetupFees\": 0.0,\n" +
                "            \"totalInterest\": 0.0,\n" +
                "            \"totalInterestVAT\": 0.0,\n" +
                "            \"totalCharges\": 249.046853,\n" +
                "            \"totalChargesVAT\": 0.0,\n" +
                "            \"totalOneOffCharges\": [\n" +
                "                {\n" +
                "                    \"chargeName\": \"FED1\",\n" +
                "                    \"charge\": 1.92,\n" +
                "                    \"chargeVAT\": 0.0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"chargeName\": \"Set-up fee\",\n" +
                "                    \"charge\": 10.08,\n" +
                "                    \"chargeVAT\": 0.0\n" +
                "                }\n" +
                "            ],\n" +
                "            \"totalRecurringCharges\": [\n" +
                "                {\n" +
                "                    \"chargeName\": \"Weekly Fee\",\n" +
                "                    \"charge\": 237.046853,\n" +
                "                    \"chargeVAT\": 0.0\n" +
                "                }\n" +
                "            ],\n" +
                "            \"milestones\": [\n" +
                "                {\n" +
                "                    \"date\": \"2023-04-07T00:01:00.000+05:00\",\n" +
                "                    \"dayOfLoanIndex\": 31,\n" +
                "                    \"chargeAdjustments\": [\n" +
                "                        {\n" +
                "                            \"gross\": 0.0,\n" +
                "                            \"net\": 0.0,\n" +
                "                            \"vat\": 0.0,\n" +
                "                            \"name\": \"FED1\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"gross\": 0.0,\n" +
                "                            \"net\": 0.0,\n" +
                "                            \"vat\": 0.0,\n" +
                "                            \"name\": \"Set-up fee\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"gross\": 28.620446,\n" +
                "                            \"net\": 28.620446,\n" +
                "                            \"vat\": 0.0,\n" +
                "                            \"name\": \"Weekly Fee\"\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"interestAdjustment\": {\n" +
                "                        \"gross\": 0.0,\n" +
                "                        \"net\": 0.0,\n" +
                "                        \"vat\": 0.0\n" +
                "                    },\n" +
                "                    \"principal\": 588.0,\n" +
                "                    \"totalGross\": 28.62,\n" +
                "                    \"totalExpenses\": 156.13,\n" +
                "                    \"totalSetupFees\": 0.0,\n" +
                "                    \"totalInterest\": 0.0,\n" +
                "                    \"totalInterestVAT\": 0.0,\n" +
                "                    \"totalCharges\": 156.131606,\n" +
                "                    \"totalChargesVAT\": 0.0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"date\": \"2023-04-14T00:01:00.000+05:00\",\n" +
                "                    \"dayOfLoanIndex\": 38,\n" +
                "                    \"chargeAdjustments\": [\n" +
                "                        {\n" +
                "                            \"gross\": 0.0,\n" +
                "                            \"net\": 0.0,\n" +
                "                            \"vat\": 0.0,\n" +
                "                            \"name\": \"FED1\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"gross\": 0.0,\n" +
                "                            \"net\": 0.0,\n" +
                "                            \"vat\": 0.0,\n" +
                "                            \"name\": \"Set-up fee\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"gross\": 29.765264,\n" +
                "                            \"net\": 29.765264,\n" +
                "                            \"vat\": 0.0,\n" +
                "                            \"name\": \"Weekly Fee\"\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"interestAdjustment\": {\n" +
                "                        \"gross\": 0.0,\n" +
                "                        \"net\": 0.0,\n" +
                "                        \"vat\": 0.0\n" +
                "                    },\n" +
                "                    \"principal\": 588.0,\n" +
                "                    \"totalGross\": 29.77,\n" +
                "                    \"totalExpenses\": 185.9,\n" +
                "                    \"totalSetupFees\": 0.0,\n" +
                "                    \"totalInterest\": 0.0,\n" +
                "                    \"totalInterestVAT\": 0.0,\n" +
                "                    \"totalCharges\": 185.89687,\n" +
                "                    \"totalChargesVAT\": 0.0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"date\": \"2023-04-21T00:01:00.000+05:00\",\n" +
                "                    \"dayOfLoanIndex\": 45,\n" +
                "                    \"chargeAdjustments\": [\n" +
                "                        {\n" +
                "                            \"gross\": 0.0,\n" +
                "                            \"net\": 0.0,\n" +
                "                            \"vat\": 0.0,\n" +
                "                            \"name\": \"FED1\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"gross\": 0.0,\n" +
                "                            \"net\": 0.0,\n" +
                "                            \"vat\": 0.0,\n" +
                "                            \"name\": \"Set-up fee\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"gross\": 30.955874,\n" +
                "                            \"net\": 30.955874,\n" +
                "                            \"vat\": 0.0,\n" +
                "                            \"name\": \"Weekly Fee\"\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"interestAdjustment\": {\n" +
                "                        \"gross\": 0.0,\n" +
                "                        \"net\": 0.0,\n" +
                "                        \"vat\": 0.0\n" +
                "                    },\n" +
                "                    \"principal\": 588.0,\n" +
                "                    \"totalGross\": 30.96,\n" +
                "                    \"totalExpenses\": 216.85,\n" +
                "                    \"totalSetupFees\": 0.0,\n" +
                "                    \"totalInterest\": 0.0,\n" +
                "                    \"totalInterestVAT\": 0.0,\n" +
                "                    \"totalCharges\": 216.852744,\n" +
                "                    \"totalChargesVAT\": 0.0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"date\": \"2023-04-28T00:01:00.000+05:00\",\n" +
                "                    \"dayOfLoanIndex\": 52,\n" +
                "                    \"chargeAdjustments\": [\n" +
                "                        {\n" +
                "                            \"gross\": 0.0,\n" +
                "                            \"net\": 0.0,\n" +
                "                            \"vat\": 0.0,\n" +
                "                            \"name\": \"FED1\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"gross\": 0.0,\n" +
                "                            \"net\": 0.0,\n" +
                "                            \"vat\": 0.0,\n" +
                "                            \"name\": \"Set-up fee\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"gross\": 32.194109,\n" +
                "                            \"net\": 32.194109,\n" +
                "                            \"vat\": 0.0,\n" +
                "                            \"name\": \"Weekly Fee\"\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"interestAdjustment\": {\n" +
                "                        \"gross\": 0.0,\n" +
                "                        \"net\": 0.0,\n" +
                "                        \"vat\": 0.0\n" +
                "                    },\n" +
                "                    \"principal\": 588.0,\n" +
                "                    \"totalGross\": 32.19,\n" +
                "                    \"totalExpenses\": 249.05,\n" +
                "                    \"totalSetupFees\": 0.0,\n" +
                "                    \"totalInterest\": 0.0,\n" +
                "                    \"totalInterestVAT\": 0.0,\n" +
                "                    \"totalCharges\": 249.046853,\n" +
                "                    \"totalChargesVAT\": 0.0\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"periodIndex\": 3,\n" +
                "            \"periodType\": \"COLLECTION\",\n" +
                "            \"periodStartTimestamp\": \"2023-03-08T00:00:00.000+05:00\",\n" +
                "            \"periodExpirationTimestamp\": \"2023-05-04T23:59:59.999+05:00\",\n" +
                "            \"periodStartDayOfLoanIndex\": 59,\n" +
                "            \"periodEndDayOfLoanIndex\": 116,\n" +
                "            \"currencyCode\": \"PKR\",\n" +
                "            \"totalGross\": 249.05,\n" +
                "            \"principal\": 588.0,\n" +
                "            \"totalExpenses\": 249.05,\n" +
                "            \"totalSetupFees\": 0.0,\n" +
                "            \"totalInterest\": 0.0,\n" +
                "            \"totalInterestVAT\": 0.0,\n" +
                "            \"totalCharges\": 249.046853,\n" +
                "            \"totalChargesVAT\": 0.0,\n" +
                "            \"totalOneOffCharges\": [\n" +
                "                {\n" +
                "                    \"chargeName\": \"FED1\",\n" +
                "                    \"charge\": 1.92,\n" +
                "                    \"chargeVAT\": 0.0\n" +
                "                },\n" +
                "                {\n" +
                "                    \"chargeName\": \"Set-up fee\",\n" +
                "                    \"charge\": 10.08,\n" +
                "                    \"chargeVAT\": 0.0\n" +
                "                }\n" +
                "            ],\n" +
                "            \"totalRecurringCharges\": [\n" +
                "                {\n" +
                "                    \"chargeName\": \"Weekly Fee\",\n" +
                "                    \"charge\": 237.046853,\n" +
                "                    \"chargeVAT\": 0.0\n" +
                "                }\n" +
                "            ],\n" +
                "            \"milestones\": []\n" +
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
                "            \"totalGross\":0,\n" +
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

