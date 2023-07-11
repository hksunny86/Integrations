package com.inov8.integration.channel.raast.mock;

public class RaastMock {

    public String customerAliasAccountResponse() {

        return "{\n" +
                "    \"ResponseCode\": \"00\",\n" +
                "    \"ResponseDescription\": \"Success\",\n" +
                "    \"IDs\": {\n" +
                "        \"CustomerID\": \"429189\",\n" +
                "        \"AliasID\": \"399854\",\n" +
                "        \"AccountID\": \"317276\"\n" +
                "    }\n" +
                "}";
    }

    public String getDefaultAccountByAlias() {

        return "{\n" +
                "    \"id\": {\n" +
                "        \"iban\": \"PK91JSBL9138000001339534\"\n" +
                "    },\n" +
                "    \"type\": \"DFLT\",\n" +
                "    \"currency\": \"PKR\",\n" +
                "    \"servicer\": {\n" +
                "        \"memberId\": \"JSBLPKKA\"\n" +
                "    },\n" +
                "    \"name\": \"Aashir\",\n" +
                "    \"surname\": \"Aashir\",\n" +
                "    \"nickName\": \"pola\",\n" +
                "    \"isDefault\": true,\n" +
                "    \"Response\": {\n" +
                "        \"responseCode\": \"00\",\n" +
                "        \"responseDescription\": [\n" +
                "            \"Success\"\n" +
                "        ]\n" +
                "    }\n" +
                "}";
    }

    public String getCustomerInformation(){

        return "{\n" +
                "    \"uid\": {\n" +
                "        \"type\": \"NTN\",\n" +
                "        \"value\": \"4220188556725\"\n" +
                "    },\n" +
                "    \"documentType\": \"NTN\",\n" +
                "    \"documentNumber\": \"4220188556725\",\n" +
                "    \"documentValidityDate\": \"2025-10-22\",\n" +
                "    \"name\": \"Zindigi RAAST P2M - Merchant 01\",\n" +
                "    \"address\": {\n" +
                "        \"country\": \"PK\",\n" +
                "        \"city\": \"KARACHI\",\n" +
                "        \"stateProvinceRegion\": \"SINDH\",\n" +
                "        \"address\": \"7th Floor Forum Karachi\"\n" +
                "    },\n" +
                "    \"additionalDetails\": {\n" +
                "        \"dba\": \"Zindigi Pilot Merchant 01\",\n" +
                "        \"mcc\": \"00112233\"\n" +
                "    },\n" +
                "    \"recordId\": \"385667\",\n" +
                "    \"status\": \"active\",\n" +
                "    \"_links\": {\n" +
                "        \"self\": {\n" +
                "            \"href\": \"/customers/385667\"\n" +
                "        },\n" +
                "        \"suspend\": {\n" +
                "            \"href\": \"/customers/385667/suspend\"\n" +
                "        },\n" +
                "        \"update\": {\n" +
                "            \"href\": \"/customers/385667\"\n" +
                "        },\n" +
                "        \"delete\": {\n" +
                "            \"href\": \"/customers/385667\"\n" +
                "        },\n" +
                "        \"accounts\": {\n" +
                "            \"href\": \"/customers/385667/accounts\"\n" +
                "        },\n" +
                "        \"aliases\": {\n" +
                "            \"href\": \"/customers/385667/aliases\"\n" +
                "        },\n" +
                "        \"addAccount\": {\n" +
                "            \"href\": \"/customers/385667/accounts\"\n" +
                "        },\n" +
                "        \"addAlias\": {\n" +
                "            \"href\": \"/customers/385667/aliases\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"Response\": {\n" +
                "        \"responseCode\": \"00\",\n" +
                "        \"responseDescription\": [\n" +
                "            \"Success\"\n" +
                "        ]\n" +
                "    }\n" +
                "}";
    }

    public String getCustomerAccounts(){
        return "{\n" +
                "    \"Data\": [\n" +
                "        {\n" +
                "            \"type\": \"DFLT\",\n" +
                "            \"currency\": \"PKR\",\n" +
                "            \"servicer\": {\n" +
                "                \"memberId\": \"JSBLPKKA\"\n" +
                "            },\n" +
                "            \"openingDate\": \"2023-07-10\",\n" +
                "            \"closingDate\": \"2028-07-10\",\n" +
                "            \"id\": {\n" +
                "                \"iban\": \"PK28JSBL9999903459866522\"\n" +
                "            },\n" +
                "            \"recordId\": \"317328\",\n" +
                "            \"_links\": {\n" +
                "                \"self\": {\n" +
                "                    \"href\": \"/accounts/317328\"\n" +
                "                },\n" +
                "                \"update\": {\n" +
                "                    \"href\": \"/accounts/317328\"\n" +
                "                },\n" +
                "                \"delete\": {\n" +
                "                    \"href\": \"/accounts/317328\"\n" +
                "                },\n" +
                "                \"aliases\": {\n" +
                "                    \"href\": \"/accounts/317328/aliases\"\n" +
                "                },\n" +
                "                \"updateDetails\": null,\n" +
                "                \"linkAccountToAlias\": {\n" +
                "                    \"href\": \"/accounts/317328/aliases\"\n" +
                "                }\n" +
                "            },\n" +
                "            \"responseCode\": null,\n" +
                "            \"responseDescription\": null\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": null,\n" +
                "            \"currency\": null,\n" +
                "            \"servicer\": null,\n" +
                "            \"openingDate\": null,\n" +
                "            \"closingDate\": null,\n" +
                "            \"id\": null,\n" +
                "            \"recordId\": null,\n" +
                "            \"_links\": null,\n" +
                "            \"responseCode\": \"00\",\n" +
                "            \"responseDescription\": [\n" +
                "                \"Success\"\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

    public String getCustomerAliases(){
        return "{\n" +
                "    \"Data\": [\n" +
                "        {\n" +
                "            \"type\": \"TILL_CODE\",\n" +
                "            \"currency\": null,\n" +
                "            \"servicer\": null,\n" +
                "            \"openingDate\": null,\n" +
                "            \"closingDate\": null,\n" +
                "            \"id\": null,\n" +
                "            \"recordId\": \"400020\",\n" +
                "            \"_links\": {\n" +
                "                \"self\": {\n" +
                "                    \"href\": \"/aliases/400020\"\n" +
                "                },\n" +
                "                \"update\": {\n" +
                "                    \"href\": \"/aliases/400020\"\n" +
                "                },\n" +
                "                \"delete\": {\n" +
                "                    \"href\": \"/aliases/400020\"\n" +
                "                },\n" +
                "                \"aliases\": null,\n" +
                "                \"updateDetails\": null,\n" +
                "                \"linkAccountToAlias\": null\n" +
                "            },\n" +
                "            \"responseCode\": null,\n" +
                "            \"responseDescription\": null\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": null,\n" +
                "            \"currency\": null,\n" +
                "            \"servicer\": null,\n" +
                "            \"openingDate\": null,\n" +
                "            \"closingDate\": null,\n" +
                "            \"id\": null,\n" +
                "            \"recordId\": null,\n" +
                "            \"_links\": null,\n" +
                "            \"responseCode\": \"00\",\n" +
                "            \"responseDescription\": [\n" +
                "                \"Success\"\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

    public String deleteAccount(){
        return "{\n" +
                "    \"Response\": {\n" +
                "        \"responseCode\": \"00\",\n" +
                "        \"responseDescription\": [\n" +
                "            \"Success\"\n" +
                "        ]\n" +
                "    }\n" +
                "}";
    }
    public String deleteAlias(){
        return "{\n" +
                "    \"Response\": {\n" +
                "        \"responseCode\": \"00\",\n" +
                "        \"responseDescription\": [\n" +
                "            \"Success\"\n" +
                "        ]\n" +
                "    }\n" +
                "}";
    }
    public String deleteCustomer(){
        return "{\n" +
                "    \"Response\": {\n" +
                "        \"responseCode\": \"00\",\n" +
                "        \"responseDescription\": [\n" +
                "            \"Success\"\n" +
                "        ]\n" +
                "    }\n" +
                "}";
    }
}
