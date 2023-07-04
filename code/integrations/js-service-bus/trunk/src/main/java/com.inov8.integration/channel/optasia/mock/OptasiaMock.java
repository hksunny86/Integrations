package com.inov8.integration.channel.optasia.mock;

import java.awt.*;

public class OptasiaMock {

    public LoanOfferResponse sendLoanOfferMock(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        LoanOfferResponse loanOfferResponse = new LoanOfferResponse();

        if (i8SBSwitchControllerRequestVO.getIdentityValue().equalsIgnoreCase("053a6b94e026b3c220776afeffa7e25a20288d977393814826947fb37e76e24b")) {
            loanOfferResponse.setCode("1002");
            loanOfferResponse.setMessage("Missing parameter");
        } else if (i8SBSwitchControllerRequestVO.getIdentityValue().equalsIgnoreCase("b3c767c2af61884290d2dce2c369454a88ef3a2bf1b42b9cd42f6479e471c906")) {
            loanOfferResponse.setCode("1003");
            loanOfferResponse.setMessage("Invalid parameter name");
        } else if (i8SBSwitchControllerRequestVO.getIdentityValue().equalsIgnoreCase("3cf37fd1bf92b11e5a53a31eb6067df7c12e3afbf263c917a380949358c3ffc1")) {
            loanOfferResponse.setCode("1200");
            loanOfferResponse.setMessage("Backend Fatal Error");
        } else if (i8SBSwitchControllerRequestVO.getIdentityValue().equalsIgnoreCase("2b41c9108784303e0998b6ed19721d49a29082859842966ec93235c25657317c")) {
            loanOfferResponse.setCode("1301");
            loanOfferResponse.setMessage("The content of the request and its syntax is correct but the server was not\n" +
                    "able to process the contained instructions. Reason: No offer available");
        } else if (i8SBSwitchControllerRequestVO.getIdentityValue().equalsIgnoreCase("48a45cf7fb8834a187a51f6a0dbc288f395b40322bdc9e84cc74ad75936f885c")) {
            loanOfferResponse.setCode("1302");
            loanOfferResponse.setMessage("The content of the request and its syntax is correct but the server was not\n" +
                    "able to process the contained instructions. Reason: Customer not eligible");
        } else if (i8SBSwitchControllerRequestVO.getIdentityValue().equalsIgnoreCase("a164af70e3a5edb485de0577bd1ac082797d58f48bb37e078a3b9aa392d3511c")) {
            loanOfferResponse.setCode("1303");
            loanOfferResponse.setMessage("The content of the request and its syntax is correct but the server was not\n" +
                    "able to process the contained instructions. Reason: Customer not found");
        } else if (i8SBSwitchControllerRequestVO.getIdentityValue().equalsIgnoreCase("0ebb5ea13c7dfc187defe2fc69e7e37bca66ba58cc8652b93969caecb03b9917")) {
            loanOfferResponse.setCode("1304");
            loanOfferResponse.setMessage("The content of the request and its syntax is correct but the server was not\n" +
                    "able to process the contained instructions. Reason: Backend operation\n" +
                    "failure");
        } else if (i8SBSwitchControllerRequestVO.getIdentityValue().equalsIgnoreCase("be2e898b056bd8e3547ec60b34105d112f87e7606632f999f569492f511d7488")) {
            loanOfferResponse.setCode("1305");
            loanOfferResponse.setMessage("The content of the request and its syntax is correct but the server was not\n" +
                    "able to process the contained instructions. Reason: Loan not found");
        } else if (i8SBSwitchControllerRequestVO.getIdentityValue().equalsIgnoreCase("feeb8bf187a939f288fb78a65e399b9451e6bd57de8db10eb16bcaf91ec6e8e5")) {
            loanOfferResponse.setCode("2000");
            loanOfferResponse.setMessage("Generic error");
        } else if (i8SBSwitchControllerRequestVO.getIdentityValue().equalsIgnoreCase("3504a46ac18b5df02f67aabbf89f4f66914c1d0a818adccb4493a324039daa6d")) {
            loanOfferResponse.setCode("2001");
            loanOfferResponse.setMessage("Backend failure");
        } else if (i8SBSwitchControllerRequestVO.getIdentityValue().equalsIgnoreCase("b9c894f7152b2d93e403a289bf3c20f7a875257773fb800f151c836e7e6b73c5")) {
            loanOfferResponse.setCode("2002");
            loanOfferResponse.setMessage("Operation timeout");
        } else if (i8SBSwitchControllerRequestVO.getIdentityValue().equalsIgnoreCase("94b3a44d36dcd7b8af291e1af799ca70c5d1b5b5fb6460842bba8421817c9f53")) {
            loanOfferResponse.setCode("2003");
            loanOfferResponse.setMessage("Insufficient balance");
        } else {
            loanOfferResponse.setCode("500");
            loanOfferResponse.setMessage("Customer Id doesn't matched");
        }
        return loanOfferResponse;
    }

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

    public String status(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        LoanStatusResponse loanStatusResponse = new LoanStatusResponse();
        String response = "";
        if (i8SBSwitchControllerRequestVO.getIdentityValue().equalsIgnoreCase("876f43461db21c3e1b2aaaf7dcbd66d8a9e60229e63262c652c1b0534cf54c9b")) {
            loanStatusResponse.setResponseCode("00");
            response = "{\n" +
                    "    \"loanInfo\": {\n" +
                    "        \"loan\": {\n" +
                    "            \"loanId\": \"243\",\n" +
                    "            \"internalLoanId\": \"168750801442313\",\n" +
                    "            \"loanTimestamp\": \"2023-06-23T13:13:34.421+05:00\",\n" +
                    "            \"loanState\": \"OPEN\",\n" +
                    "            \"loanReason\": \"API_MOBILEAPP\",\n" +
                    "            \"loanReasonDetails\": [\n" +
                    "                {\n" +
                    "                    \"userApp\": \"mobileApp\"\n" +
                    "                }\n" +
                    "            ],\n" +
                    "            \"loanOffer\": {\n" +
                    "                \"offerName\": \"XTRACASHFED7\",\n" +
                    "                \"advanceOfferId\": \"7\",\n" +
                    "                \"commodityType\": \"CASH\",\n" +
                    "                \"currencyCode\": \"PKR\",\n" +
                    "                \"principalAmount\": 450.0,\n" +
                    "                \"setupFees\": 0.0,\n" +
                    "                \"loanPlanId\": 7,\n" +
                    "                \"loanPlanName\": \"XtraCash plan FED7\",\n" +
                    "                \"loanProductGroup\": \"XTRACASH\",\n" +
                    "                \"maturityDetails\": {\n" +
                    "                    \"maturityDuration\": 28\n" +
                    "                }\n" +
                    "            }\n" +
                    "        },\n" +
                    "        \"report\": {\n" +
                    "            \"repayment\": {\n" +
                    "                \"lastRepaymentDate\": \"2023-06-23T13:13:41.098+05:00\",\n" +
                    "                \"repaymentsCount\": 1,\n" +
                    "                \"gross\": 9.0,\n" +
                    "                \"principal\": 0.0,\n" +
                    "                \"setupFees\": 0.0,\n" +
                    "                \"interest\": 0.0,\n" +
                    "                \"interestVAT\": 0.0,\n" +
                    "                \"charges\": 9.0,\n" +
                    "                \"chargesVAT\": 0.0\n" +
                    "            },\n" +
                    "            \"outstanding\": {\n" +
                    "                \"currencyCode\": \"PKR\",\n" +
                    "                \"totalGross\": 490.5,\n" +
                    "                \"totalPrincipal\": 450.0,\n" +
                    "                \"totalSetupFees\": 0.0,\n" +
                    "                \"totalInterest\": 0.0,\n" +
                    "                \"totalInterestVAT\": 0.0,\n" +
                    "                \"totalCharges\": 40.5,\n" +
                    "                \"totalChargesVAT\": 0.0,\n" +
                    "                \"totalPendingRecoveries\": 0.0\n" +
                    "            },\n" +
                    "            \"plan\": {\n" +
                    "                \"currentPeriod\": \"MATURITY\",\n" +
                    "                \"daysLeftInPeriod\": 17,\n" +
                    "                \"nextPeriod\": \"ROLLOVER\"\n" +
                    "            }\n" +
                    "        },\n" +
                    "        \"events\": [\n" +
                    "            {\n" +
                    "                \"eventType\": \"CHARGE_CALCULATION\",\n" +
                    "                \"eventTypeDetails\": {\n" +
                    "                    \"charge.calculation.name\": \"Weekly_Fee_of_Maturity\"\n" +
                    "                },\n" +
                    "                \"eventTypeStatus\": \"COMPLETED\",\n" +
                    "                \"eventReason\": \"SERVICE\",\n" +
                    "                \"eventReasonDetails\": [],\n" +
                    "                \"period\": \"MATURITY\",\n" +
                    "                \"periodIndex\": 2,\n" +
                    "                \"periodExpirationTimestamp\": \"2023-07-20T23:59:59.999+05:00\",\n" +
                    "                \"principalAdjustment\": 0.0,\n" +
                    "                \"principalBefore\": 450.0,\n" +
                    "                \"principalAfter\": 450.0,\n" +
                    "                \"setupFeesAdjustment\": 0.0,\n" +
                    "                \"setupFeesBefore\": 0.0,\n" +
                    "                \"setupFeesAfter\": 0.0,\n" +
                    "                \"interestAdjustment\": 0.0,\n" +
                    "                \"interestAdjustmentVAT\": 0.0,\n" +
                    "                \"interestBefore\": 0.0,\n" +
                    "                \"interestAfter\": 0.0,\n" +
                    "                \"totalChargesAdjustment\": 20.25,\n" +
                    "                \"totalChargesAdjustmentVAT\": 0.0,\n" +
                    "                \"totalChargesBefore\": 20.25,\n" +
                    "                \"totalChargesAfter\": 40.5,\n" +
                    "                \"eventTimestamp\": \"2023-06-30T00:01:01.727+05:00\",\n" +
                    "                \"receptionTimestamp\": \"2023-06-30T00:01:01.727+05:00\",\n" +
                    "                \"processingTimestamp\": \"2023-06-30T00:01:01.736+05:00\",\n" +
                    "                \"offerName\": \"XTRACASHFED7\",\n" +
                    "                \"commodityType\": \"CASH\",\n" +
                    "                \"currencyCode\": \"PKR\",\n" +
                    "                \"principalAmount\": 450.0,\n" +
                    "                \"setupFees\": 0.0,\n" +
                    "                \"loanProductGroup\": \"XTRACASH\",\n" +
                    "                \"loanPlanId\": 7,\n" +
                    "                \"loanPlanName\": \"XtraCash plan FED7\",\n" +
                    "                \"maturityDetails\": {\n" +
                    "                    \"maturityDuration\": 28\n" +
                    "                },\n" +
                    "                \"projectSpecific\": {},\n" +
                    "                \"loanId\": \"243\",\n" +
                    "                \"internalLoanId\": \"168750801442313\",\n" +
                    "                \"loanTimestamp\": \"2023-06-23T13:13:34.421+05:00\",\n" +
                    "                \"loanState\": \"OPEN\",\n" +
                    "                \"loanReason\": \"API_MOBILEAPP\",\n" +
                    "                \"loanReasonDetails\": [\n" +
                    "                    {\n" +
                    "                        \"userApp\": \"mobileApp\"\n" +
                    "                    }\n" +
                    "                ]\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"eventType\": \"RECOVERY\",\n" +
                    "                \"eventTypeDetails\": {\n" +
                    "                    \"is.passive.recovery\": true,\n" +
                    "                    \"recovery.passive.auto.recovered\": true\n" +
                    "                },\n" +
                    "                \"eventTypeStatus\": \"COMPLETED\",\n" +
                    "                \"thirdPartyTransactionId\": \"14632815633190\",\n" +
                    "                \"eventReason\": \"LOAN_PROVISION\",\n" +
                    "                \"eventReasonDetails\": [],\n" +
                    "                \"period\": \"MATURITY\",\n" +
                    "                \"periodIndex\": 2,\n" +
                    "                \"periodExpirationTimestamp\": \"2023-07-20T23:59:59.999+05:00\",\n" +
                    "                \"principalAdjustment\": 0.0,\n" +
                    "                \"principalBefore\": 450.0,\n" +
                    "                \"principalAfter\": 450.0,\n" +
                    "                \"setupFeesAdjustment\": 0.0,\n" +
                    "                \"setupFeesBefore\": 0.0,\n" +
                    "                \"setupFeesAfter\": 0.0,\n" +
                    "                \"interestAdjustment\": 0.0,\n" +
                    "                \"interestAdjustmentVAT\": 0.0,\n" +
                    "                \"interestBefore\": 0.0,\n" +
                    "                \"interestAfter\": 0.0,\n" +
                    "                \"totalChargesAdjustment\": -9.0,\n" +
                    "                \"totalChargesAdjustmentVAT\": 0.0,\n" +
                    "                \"totalChargesBefore\": 29.25,\n" +
                    "                \"totalChargesAfter\": 20.25,\n" +
                    "                \"eventTimestamp\": \"2023-06-23T13:13:41.098+05:00\",\n" +
                    "                \"receptionTimestamp\": \"2023-06-23T13:13:41.098+05:00\",\n" +
                    "                \"processingTimestamp\": \"2023-06-23T13:13:41.115+05:00\",\n" +
                    "                \"sourceRequestId\": \"14632815633190\",\n" +
                    "                \"offerName\": \"XTRACASHFED7\",\n" +
                    "                \"commodityType\": \"CASH\",\n" +
                    "                \"currencyCode\": \"PKR\",\n" +
                    "                \"principalAmount\": 450.0,\n" +
                    "                \"setupFees\": 0.0,\n" +
                    "                \"loanProductGroup\": \"XTRACASH\",\n" +
                    "                \"loanPlanId\": 7,\n" +
                    "                \"loanPlanName\": \"XtraCash plan FED7\",\n" +
                    "                \"maturityDetails\": {\n" +
                    "                    \"maturityDuration\": 28\n" +
                    "                },\n" +
                    "                \"projectSpecific\": {},\n" +
                    "                \"loanId\": \"243\",\n" +
                    "                \"internalLoanId\": \"168750801442313\",\n" +
                    "                \"loanTimestamp\": \"2023-06-23T13:13:34.421+05:00\",\n" +
                    "                \"loanState\": \"OPEN\",\n" +
                    "                \"loanReason\": \"API_MOBILEAPP\",\n" +
                    "                \"loanReasonDetails\": [\n" +
                    "                    {\n" +
                    "                        \"userApp\": \"mobileApp\"\n" +
                    "                    }\n" +
                    "                ]\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"eventType\": \"ADVANCE\",\n" +
                    "                \"eventTypeDetails\": {\n" +
                    "                    \"is.passive.advance\": false,\n" +
                    "                    \"pending.operation.id\": 261\n" +
                    "                },\n" +
                    "                \"eventTypeStatus\": \"COMPLETED\",\n" +
                    "                \"eventTransactionId\": \"168750801489\",\n" +
                    "                \"thirdPartyTransactionId\": \"14632815633190\",\n" +
                    "                \"eventReason\": \"API_MOBILEAPP\",\n" +
                    "                \"eventReasonDetails\": [\n" +
                    "                    {\n" +
                    "                        \"userApp\": \"mobileApp\"\n" +
                    "                    }\n" +
                    "                ],\n" +
                    "                \"period\": \"MATURITY\",\n" +
                    "                \"periodIndex\": 2,\n" +
                    "                \"periodExpirationTimestamp\": \"2023-07-20T23:59:59.999+05:00\",\n" +
                    "                \"principalAdjustment\": 450.0,\n" +
                    "                \"principalBefore\": 0.0,\n" +
                    "                \"principalAfter\": 450.0,\n" +
                    "                \"setupFeesAdjustment\": 0.0,\n" +
                    "                \"setupFeesBefore\": 0.0,\n" +
                    "                \"setupFeesAfter\": 0.0,\n" +
                    "                \"interestAdjustment\": 0.0,\n" +
                    "                \"interestAdjustmentVAT\": 0.0,\n" +
                    "                \"interestBefore\": 0.0,\n" +
                    "                \"interestAfter\": 0.0,\n" +
                    "                \"totalChargesAdjustment\": 29.25,\n" +
                    "                \"totalChargesAdjustmentVAT\": 0.0,\n" +
                    "                \"totalChargesBefore\": 0.0,\n" +
                    "                \"totalChargesAfter\": 29.25,\n" +
                    "                \"eventTimestamp\": \"2023-06-23T13:13:34.892+05:00\",\n" +
                    "                \"receptionTimestamp\": \"2023-06-23T13:13:34.790+05:00\",\n" +
                    "                \"processingTimestamp\": \"2023-06-23T13:13:41.010+05:00\",\n" +
                    "                \"remoteRequestId\": \"168750801489\",\n" +
                    "                \"sourceRequestId\": \"14632815633190\",\n" +
                    "                \"offerName\": \"XTRACASHFED7\",\n" +
                    "                \"commodityType\": \"CASH\",\n" +
                    "                \"currencyCode\": \"PKR\",\n" +
                    "                \"principalAmount\": 450.0,\n" +
                    "                \"setupFees\": 0.0,\n" +
                    "                \"loanProductGroup\": \"XTRACASH\",\n" +
                    "                \"loanPlanId\": 7,\n" +
                    "                \"loanPlanName\": \"XtraCash plan FED7\",\n" +
                    "                \"maturityDetails\": {\n" +
                    "                    \"maturityDuration\": 28\n" +
                    "                },\n" +
                    "                \"projectSpecific\": {},\n" +
                    "                \"loanId\": \"243\",\n" +
                    "                \"internalLoanId\": \"168750801442313\",\n" +
                    "                \"loanTimestamp\": \"2023-06-23T13:13:34.421+05:00\",\n" +
                    "                \"loanState\": \"OPEN\",\n" +
                    "                \"loanReason\": \"API_MOBILEAPP\",\n" +
                    "                \"loanReasonDetails\": [\n" +
                    "                    {\n" +
                    "                        \"userApp\": \"mobileApp\"\n" +
                    "                    }\n" +
                    "                ]\n" +
                    "            }\n" +
                    "        ]\n" +
                    "    }\n" +
                    "}";
        } else if (i8SBSwitchControllerRequestVO.getIdentityValue().equalsIgnoreCase("71c3296bddeea2c671c4319ee599b4c7f2eb4bbd43afc2555a9081057ed4f874")) {
            loanStatusResponse.setResponseCode("00");
            response = "{\n" +
                    "    \"identityValue\": \"16505130514\",\n" +
                    "    \"identityType\": \"customerIdentity\",\n" +
                    "    \"origSource\": \"mobileApp\",\n" +
                    "    \"receivedTimestamp\": \"2021-07-20T13:10:38.738+03:00\",\n" +
                    "    \"loanInfo\": {\n" +
                    "        \"loan\": {\n" +
                    "            \"internalLoanId\": \"123444\",\n" +
                    "            \"loanState\": \"PENDING_PROVISIONING\",\n" +
                    "            \"loanTimestamp\": \"2021-07-20T10:10:10.348+03:00\",\n" +
                    "            \"loanReason\": \"USSD_INBOUND\",\n" +
                    "            \"loanOffer\": {\n" +
                    "                \"offerName\": \"CASH15\",\n" +
                    "                \"commodityType\": \"CASH\",\n" +
                    "                \"currencyCode\": \"USD\",\n" +
                    "                \"principalAmount\": 15,\n" +
                    "                \"setupFees\": 5,\n" +
                    "                \"loanPlanId\": 1,\n" +
                    "                \"loanPlanName\": \"Default\",\n" +
                    "                \"loanProductGroup\": \"Standard group\",\n" +
                    "                \"maturityDetails\": {\n" +
                    "                    \"maturityDuration\": 360\n" +
                    "                }\n" +
                    "            }\n" +
                    "        },\n" +
                    "        \"report\": {\n" +
                    "            \"repayment\": {\n" +
                    "                \"repaymentsCount\": 1,\n" +
                    "                \"gross\": 10,\n" +
                    "                \"principal\": 5,\n" +
                    "                \"setupFees\": 0,\n" +
                    "                \"interest\": 0,\n" +
                    "                \"interestVAT\": 0,\n" +
                    "                \"charges\": 0,\n" +
                    "                \"chargesVAT\": 0\n" +
                    "            },\n" +
                    "            \"outstanding\": {\n" +
                    "                \"currencyCode\": \"USD\",\n" +
                    "                \"totalGross\": 15,\n" +
                    "                \"totalPrincipal\": 10,\n" +
                    "                \"totalSetupFees\": 5,\n" +
                    "                \"totalInterest\": 0,\n" +
                    "                \"totalInterestVAT\": 0,\n" +
                    "                \"totalCharges\": 0,\n" +
                    "                \"totalChargesVAT\": 0,\n" +
                    "                \"totalPendingRecoveries\": 0\n" +
                    "            },\n" +
                    "            \"plan\": {\n" +
                    "                \"currentPeriod\": \"MATURITY\",\n" +
                    "                \"daysLeftInPeriod\": 360,\n" +
                    "                \"nextPeriod\": \"DEFAULT\"\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";
        } else if (i8SBSwitchControllerRequestVO.getIdentityValue().equalsIgnoreCase("16a48a9f11f4995841c5f33fefec07798332883749a6cfba225393a28602bfd6")) {
            loanStatusResponse.setResponseCode("00");
            response = "{\n" +
                    "    \"identityValue\": \"16505130514\",\n" +
                    "    \"identityType\": \"customerIdentity\",\n" +
                    "    \"origSource\": \"mobileApp\",\n" +
                    "    \"receivedTimestamp\": \"2021-07-20T13:10:38.738+03:00\",\n" +
                    "    \"loanInfo\": {\n" +
                    "        \"loan\": {\n" +
                    "            \"internalLoanId\": \"123444\",\n" +
                    "            \"loanState\": \"OPEN\",\n" +
                    "            \"loanTimestamp\": \"2021-07-20T10:10:10.348+03:00\",\n" +
                    "            \"loanReason\": \"USSD_INBOUND\",\n" +
                    "            \"loanOffer\": {\n" +
                    "                \"offerName\": \"CASH15\",\n" +
                    "                \"commodityType\": \"CASH\",\n" +
                    "                \"currencyCode\": \"USD\",\n" +
                    "                \"principalAmount\": 15,\n" +
                    "                \"setupFees\": 5,\n" +
                    "                \"loanPlanId\": 1,\n" +
                    "                \"loanPlanName\": \"Default\",\n" +
                    "                \"loanProductGroup\": \"Standard group\",\n" +
                    "                \"maturityDetails\": {\n" +
                    "                    \"maturityDuration\": 360\n" +
                    "                }\n" +
                    "            }\n" +
                    "        },\n" +
                    "        \"report\": {\n" +
                    "            \"repayment\": {\n" +
                    "                \"repaymentsCount\": 1,\n" +
                    "                \"gross\": 10,\n" +
                    "                \"principal\": 5,\n" +
                    "                \"setupFees\": 0,\n" +
                    "                \"interest\": 0,\n" +
                    "                \"interestVAT\": 0,\n" +
                    "                \"charges\": 0,\n" +
                    "                \"chargesVAT\": 0\n" +
                    "            },\n" +
                    "            \"outstanding\": {\n" +
                    "                \"currencyCode\": \"USD\",\n" +
                    "                \"totalGross\": 15,\n" +
                    "                \"totalPrincipal\": 10,\n" +
                    "                \"totalSetupFees\": 5,\n" +
                    "                \"totalInterest\": 0,\n" +
                    "                \"totalInterestVAT\": 0,\n" +
                    "                \"totalCharges\": 0,\n" +
                    "                \"totalChargesVAT\": 0,\n" +
                    "                \"totalPendingRecoveries\": 0\n" +
                    "            },\n" +
                    "            \"plan\": {\n" +
                    "                \"currentPeriod\": \"MATURITY\",\n" +
                    "                \"daysLeftInPeriod\": 360,\n" +
                    "                \"nextPeriod\": \"DEFAULT\"\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";
        } else if (i8SBSwitchControllerRequestVO.getIdentityValue().equalsIgnoreCase("e9c48eff89bea0a7641fd82ad524596b3986a0567f56e8dde5a4ec0b96d9a3b1")) {
            loanStatusResponse.setResponseCode("00");
            response = "{\n" +
                    "    \"identityValue\": \"16505130514\",\n" +
                    "    \"identityType\": \"customerIdentity\",\n" +
                    "    \"origSource\": \"mobileApp\",\n" +
                    "    \"receivedTimestamp\": \"2021-07-20T13:10:38.738+03:00\",\n" +
                    "    \"loanInfo\": {\n" +
                    "        \"loan\": {\n" +
                    "            \"internalLoanId\": \"123444\",\n" +
                    "            \"loanState\": \"CLOSED\",\n" +
                    "            \"loanTimestamp\": \"2021-07-20T10:10:10.348+03:00\",\n" +
                    "            \"loanReason\": \"USSD_INBOUND\",\n" +
                    "            \"loanOffer\": {\n" +
                    "                \"offerName\": \"CASH15\",\n" +
                    "                \"commodityType\": \"CASH\",\n" +
                    "                \"currencyCode\": \"USD\",\n" +
                    "                \"principalAmount\": 15,\n" +
                    "                \"setupFees\": 5,\n" +
                    "                \"loanPlanId\": 1,\n" +
                    "                \"loanPlanName\": \"Default\",\n" +
                    "                \"loanProductGroup\": \"Standard group\",\n" +
                    "                \"maturityDetails\": {\n" +
                    "                    \"maturityDuration\": 360\n" +
                    "                }\n" +
                    "            }\n" +
                    "        },\n" +
                    "        \"report\": {\n" +
                    "            \"repayment\": {\n" +
                    "                \"repaymentsCount\": 1,\n" +
                    "                \"gross\": 10,\n" +
                    "                \"principal\": 5,\n" +
                    "                \"setupFees\": 0,\n" +
                    "                \"interest\": 0,\n" +
                    "                \"interestVAT\": 0,\n" +
                    "                \"charges\": 0,\n" +
                    "                \"chargesVAT\": 0\n" +
                    "            },\n" +
                    "            \"outstanding\": {\n" +
                    "                \"currencyCode\": \"USD\",\n" +
                    "                \"totalGross\": 15,\n" +
                    "                \"totalPrincipal\": 10,\n" +
                    "                \"totalSetupFees\": 5,\n" +
                    "                \"totalInterest\": 0,\n" +
                    "                \"totalInterestVAT\": 0,\n" +
                    "                \"totalCharges\": 0,\n" +
                    "                \"totalChargesVAT\": 0,\n" +
                    "                \"totalPendingRecoveries\": 0\n" +
                    "            },\n" +
                    "            \"plan\": {\n" +
                    "                \"currentPeriod\": \"MATURITY\",\n" +
                    "                \"daysLeftInPeriod\": 360,\n" +
                    "                \"nextPeriod\": \"DEFAULT\"\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";
        } else if (i8SBSwitchControllerRequestVO.getIdentityValue().equalsIgnoreCase("2674be6a234ae16630737116977931be2283ce7d8f07f7015eb756d278c23d2b")) {
            loanStatusResponse.setCode("00");
            response = "{\n" +
                    "    \"identityValue\": \"16505130514\",\n" +
                    "    \"identityType\": \"customerIdentity\",\n" +
                    "    \"origSource\": \"mobileApp\",\n" +
                    "    \"receivedTimestamp\": \"2021-07-20T13:10:38.738+03:00\",\n" +
                    "    \"loanInfo\": {\n" +
                    "        \"loan\": {\n" +
                    "            \"internalLoanId\": \"123444\",\n" +
                    "            \"loanState\": \"DEFAULTED\",\n" +
                    "            \"loanTimestamp\": \"2021-07-20T10:10:10.348+03:00\",\n" +
                    "            \"loanReason\": \"USSD_INBOUND\",\n" +
                    "            \"loanOffer\": {\n" +
                    "                \"offerName\": \"CASH15\",\n" +
                    "                \"commodityType\": \"CASH\",\n" +
                    "                \"currencyCode\": \"USD\",\n" +
                    "                \"principalAmount\": 15,\n" +
                    "                \"setupFees\": 5,\n" +
                    "                \"loanPlanId\": 1,\n" +
                    "                \"loanPlanName\": \"Default\",\n" +
                    "                \"loanProductGroup\": \"Standard group\",\n" +
                    "                \"maturityDetails\": {\n" +
                    "                    \"maturityDuration\": 360\n" +
                    "                }\n" +
                    "            }\n" +
                    "        },\n" +
                    "        \"report\": {\n" +
                    "            \"repayment\": {\n" +
                    "                \"repaymentsCount\": 1,\n" +
                    "                \"gross\": 10,\n" +
                    "                \"principal\": 5,\n" +
                    "                \"setupFees\": 0,\n" +
                    "                \"interest\": 0,\n" +
                    "                \"interestVAT\": 0,\n" +
                    "                \"charges\": 0,\n" +
                    "                \"chargesVAT\": 0\n" +
                    "            },\n" +
                    "            \"outstanding\": {\n" +
                    "                \"currencyCode\": \"USD\",\n" +
                    "                \"totalGross\": 15,\n" +
                    "                \"totalPrincipal\": 10,\n" +
                    "                \"totalSetupFees\": 5,\n" +
                    "                \"totalInterest\": 0,\n" +
                    "                \"totalInterestVAT\": 0,\n" +
                    "                \"totalCharges\": 0,\n" +
                    "                \"totalChargesVAT\": 0,\n" +
                    "                \"totalPendingRecoveries\": 0\n" +
                    "            },\n" +
                    "            \"plan\": {\n" +
                    "                \"currentPeriod\": \"MATURITY\",\n" +
                    "                \"daysLeftInPeriod\": 360,\n" +
                    "                \"nextPeriod\": \"DEFAULT\"\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";
        } else if (i8SBSwitchControllerRequestVO.getIdentityValue().equalsIgnoreCase("de248588815c241d66d1ca21641ae2900a1e84c3f0fc2cb5b412d32a388cd15c")) {
            loanStatusResponse.setCode("00");
            response = "{\n" +
                    "    \"identityValue\": \"16505130514\",\n" +
                    "    \"identityType\": \"customerIdentity\",\n" +
                    "    \"origSource\": \"mobileApp\",\n" +
                    "    \"receivedTimestamp\": \"2021-07-20T13:10:38.738+03:00\",\n" +
                    "    \"loanInfo\": {\n" +
                    "        \"loan\": {\n" +
                    "            \"internalLoanId\": \"123444\",\n" +
                    "            \"loanState\": \"FAILED\",\n" +
                    "            \"loanTimestamp\": \"2021-07-20T10:10:10.348+03:00\",\n" +
                    "            \"loanReason\": \"USSD_INBOUND\",\n" +
                    "            \"loanOffer\": {\n" +
                    "                \"offerName\": \"CASH15\",\n" +
                    "                \"commodityType\": \"CASH\",\n" +
                    "                \"currencyCode\": \"USD\",\n" +
                    "                \"principalAmount\": 15,\n" +
                    "                \"setupFees\": 5,\n" +
                    "                \"loanPlanId\": 1,\n" +
                    "                \"loanPlanName\": \"Default\",\n" +
                    "                \"loanProductGroup\": \"Standard group\",\n" +
                    "                \"maturityDetails\": {\n" +
                    "                    \"maturityDuration\": 360\n" +
                    "                }\n" +
                    "            }\n" +
                    "        },\n" +
                    "        \"report\": {\n" +
                    "            \"repayment\": {\n" +
                    "                \"repaymentsCount\": 1,\n" +
                    "                \"gross\": 10,\n" +
                    "                \"principal\": 5,\n" +
                    "                \"setupFees\": 0,\n" +
                    "                \"interest\": 0,\n" +
                    "                \"interestVAT\": 0,\n" +
                    "                \"charges\": 0,\n" +
                    "                \"chargesVAT\": 0\n" +
                    "            },\n" +
                    "            \"outstanding\": {\n" +
                    "                \"currencyCode\": \"USD\",\n" +
                    "                \"totalGross\": 15,\n" +
                    "                \"totalPrincipal\": 10,\n" +
                    "                \"totalSetupFees\": 5,\n" +
                    "                \"totalInterest\": 0,\n" +
                    "                \"totalInterestVAT\": 0,\n" +
                    "                \"totalCharges\": 0,\n" +
                    "                \"totalChargesVAT\": 0,\n" +
                    "                \"totalPendingRecoveries\": 0\n" +
                    "            },\n" +
                    "            \"plan\": {\n" +
                    "                \"currentPeriod\": \"MATURITY\",\n" +
                    "                \"daysLeftInPeriod\": 360,\n" +
                    "                \"nextPeriod\": \"DEFAULT\"\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";
        } else {
            loanStatusResponse.setResponseCode("500");
            loanStatusResponse.setResponseDescription("Customer Id doesn't matched");
        }

        return response;
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

