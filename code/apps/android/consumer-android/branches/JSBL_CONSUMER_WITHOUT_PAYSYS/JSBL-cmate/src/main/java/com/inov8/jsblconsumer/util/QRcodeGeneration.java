package com.inov8.jsblconsumer.util;

import java.util.HashMap;
import java.util.Map;

public class QRcodeGeneration {

    public static Map genericQRCodeBreak(String qr) {

        Map<String, String> fieldMap = new HashMap<>();
        Boolean isDynamic = false;

        int endTag = 0;
        String fieldIdentifier = qr.substring(endTag, endTag + 2);
        String fieldLength = qr.substring(endTag + 2, endTag + 4);
        if (fieldLength.equals("08") || fieldLength.equals("8")) {
            return breakOldMasterCardQRCode(qr);
        }
        endTag = 4 + (Integer.parseInt(fieldLength));
        String fieldValue = qr.substring(4, endTag);
        fieldMap.put(fieldIdentifier, fieldValue);
        AppLogger.i("Field Identifier: " + fieldIdentifier + ", Field Value: " + fieldValue);

        fieldIdentifier = qr.substring(endTag, endTag + 2);
        if (fieldIdentifier.equals(QRCodeEnum.INITIATION_METHOD.getValue())) {
            fieldLength = String.valueOf((Integer.parseInt(qr.substring(endTag + 2, endTag + 4))));
            endTag = endTag + 4;
            fieldValue = qr.substring(endTag, (endTag + Integer.parseInt(fieldLength)));
            endTag = (endTag + Integer.parseInt(fieldLength));
            fieldMap.put(fieldIdentifier, fieldValue);
            if (fieldValue.equals(QRCodeEnum.MASTERCARD_DYNAMIC.getValue()))
                isDynamic = true;
            AppLogger.i("Field Identifier: " + fieldIdentifier + ", Field Value: " + fieldValue);
        }

        fieldIdentifier = qr.substring(endTag, endTag + 2);
        if (fieldIdentifier.equals(QRCodeEnum.PAN.getValue())) {
            fieldLength = String.valueOf((Integer.parseInt(qr.substring(endTag + 2, endTag + 4))));
            endTag = endTag + 4;
            fieldValue = qr.substring(endTag, (endTag + Integer.parseInt(fieldLength)));
            endTag = (endTag + Integer.parseInt(fieldLength));
            fieldMap.put(fieldIdentifier, fieldValue);
            String checkDigit = PANCheckDigitCalculator.calculateCheckDigit(fieldValue);
            AppLogger.i("PAN value before check digit: " + fieldValue);
            AppLogger.i("PAN Calculated Check digit: " + checkDigit);
            fieldValue = fieldValue + checkDigit;
            AppLogger.i("Field Identifier: " + fieldIdentifier + ", Field Value: " + fieldValue);
        }


        fieldIdentifier = qr.substring(endTag, endTag + 2);
        if (fieldIdentifier.equals(QRCodeEnum.MCC.getValue())) {
            fieldLength = String.valueOf((Integer.parseInt(qr.substring(endTag + 2, endTag + 4))));
            endTag = endTag + 4;
            fieldValue = qr.substring(endTag, (endTag + Integer.parseInt(fieldLength)));
            endTag = (endTag + Integer.parseInt(fieldLength));
            fieldMap.put(fieldIdentifier, fieldValue);
            AppLogger.i("Field Identifier: " + fieldIdentifier + ", Field Value: " + fieldValue);
        }

        fieldIdentifier = qr.substring(endTag, endTag + 2);
        if (fieldIdentifier.equals(QRCodeEnum.CURRENCY_CODE.getValue())) {
            fieldLength = String.valueOf((Integer.parseInt(qr.substring(endTag + 2, endTag + 4))));
            endTag = endTag + 4;
            fieldValue = qr.substring(endTag, (endTag + Integer.parseInt(fieldLength)));
            endTag = (endTag + Integer.parseInt(fieldLength));
            fieldMap.put(fieldIdentifier, fieldValue);
            AppLogger.i("Field Identifier: " + fieldIdentifier + ", Field Value: " + fieldValue);
        }

        fieldIdentifier = qr.substring(endTag, endTag + 2);
        if (fieldIdentifier.equals(QRCodeEnum.TRANSACTION_AMOUNT.getValue())) {
            fieldLength = String.valueOf((Integer.parseInt(qr.substring(endTag + 2, endTag + 4))));
            endTag = endTag + 4;
            fieldValue = qr.substring(endTag, (endTag + Integer.parseInt(fieldLength)));
            endTag = (endTag + Integer.parseInt(fieldLength));
            fieldMap.put(fieldIdentifier, fieldValue);
            AppLogger.i("Field Identifier: " + fieldIdentifier + ", Field Value: " + fieldValue);
        }

        fieldIdentifier = qr.substring(endTag, endTag + 2);
        if (fieldIdentifier.equals(QRCodeEnum.COUNTRY_CODE.getValue())) {
            fieldLength = String.valueOf((Integer.parseInt(qr.substring(endTag + 2, endTag + 4))));
            endTag = endTag + 4;
            fieldValue = qr.substring(endTag, (endTag + Integer.parseInt(fieldLength)));
            endTag = (endTag + Integer.parseInt(fieldLength));
            fieldMap.put(fieldIdentifier, fieldValue);
            AppLogger.i("Field Identifier: " + fieldIdentifier + ", Field Value: " + fieldValue);
        }

        fieldIdentifier = qr.substring(endTag, endTag + 2);
        if (fieldIdentifier.equals(QRCodeEnum.MERCHANT_NAME.getValue())) {
            fieldLength = String.valueOf((Integer.parseInt(qr.substring(endTag + 2, endTag + 4))));
            endTag = endTag + 4;
            fieldValue = qr.substring(endTag, (endTag + Integer.parseInt(fieldLength)));
            endTag = (endTag + Integer.parseInt(fieldLength));
            fieldMap.put(fieldIdentifier, fieldValue);
            AppLogger.i("Field Identifier: " + fieldIdentifier + ", Field Value: " + fieldValue);
        }


        fieldIdentifier = qr.substring(endTag, endTag + 2);
        if (fieldIdentifier.equals(QRCodeEnum.MERCHANT_CITY.getValue())) {
            fieldLength = String.valueOf((Integer.parseInt(qr.substring(endTag + 2, endTag + 4))));
            endTag = endTag + 4;
            fieldValue = qr.substring(endTag, (endTag + Integer.parseInt(fieldLength)));
            endTag = (endTag + Integer.parseInt(fieldLength));
            fieldMap.put(fieldIdentifier, fieldValue);
            AppLogger.i("Field Identifier: " + fieldIdentifier + ", Field Value: " + fieldValue);
        }


        fieldIdentifier = qr.substring(endTag, endTag + 2);
        if (fieldIdentifier.equals(QRCodeEnum.ADDITIONAL_DATA.getValue())) {
            fieldLength = String.valueOf((Integer.parseInt(qr.substring(endTag + 2, endTag + 4))));
            AppLogger.i("Additional Data Field length: " + fieldLength);
            endTag = endTag + 4;
            String additionalDataString = qr.substring(endTag, (endTag + Integer.parseInt(fieldLength)));

            String addidtionalDataFieldLength;
            String addidtionalDataFieldValue;
            String addidtionalDataFieldIdentifier;

            int additionalDataEndTag = 0;
            addidtionalDataFieldIdentifier = additionalDataString.substring(additionalDataEndTag, additionalDataEndTag + 2);
            if (isDynamic) {
                if (addidtionalDataFieldIdentifier.equals(QRCodeEnum.BILL_NO.getValue())) {
                    addidtionalDataFieldLength = additionalDataString.substring(additionalDataEndTag + 2, additionalDataEndTag + 4);
                    additionalDataEndTag = 4 + (Integer.parseInt(addidtionalDataFieldLength));
                    addidtionalDataFieldValue = additionalDataString.substring(4, additionalDataEndTag);
                    fieldMap.put(addidtionalDataFieldIdentifier, addidtionalDataFieldValue);
                    AppLogger.i("Field Identifier: " + addidtionalDataFieldIdentifier + ", Field Value: " + addidtionalDataFieldValue);
                }

                addidtionalDataFieldIdentifier = additionalDataString.substring(additionalDataEndTag, additionalDataEndTag + 2);
                if (addidtionalDataFieldIdentifier.equals(QRCodeEnum.MERCHAN_ID_SUB_CATEGORY.getValue())) {
                    addidtionalDataFieldLength = String.valueOf((Integer.parseInt(additionalDataString.substring(additionalDataEndTag + 2, additionalDataEndTag + 4))));
                    additionalDataEndTag = additionalDataEndTag + 4;
                    addidtionalDataFieldValue = additionalDataString.substring(additionalDataEndTag, (additionalDataEndTag + Integer.parseInt(addidtionalDataFieldLength)));
                    additionalDataEndTag = (additionalDataEndTag + Integer.parseInt(addidtionalDataFieldLength));
                    fieldMap.put(addidtionalDataFieldIdentifier, addidtionalDataFieldValue);
                    AppLogger.i("Field Identifier: " + addidtionalDataFieldIdentifier + ", Field Value: " + addidtionalDataFieldValue);
                }


                if (additionalDataString.length() != additionalDataEndTag) {
                    addidtionalDataFieldIdentifier = additionalDataString.substring(additionalDataEndTag, additionalDataEndTag + 2);
                    if (addidtionalDataFieldIdentifier.equals(QRCodeEnum.REFERENCE_NO.getValue())) {
                        addidtionalDataFieldLength = String.valueOf((Integer.parseInt(additionalDataString.substring(additionalDataEndTag + 2, additionalDataEndTag + 4))));
                        additionalDataEndTag = additionalDataEndTag + 4;
                        addidtionalDataFieldValue = additionalDataString.substring(additionalDataEndTag, (additionalDataEndTag + Integer.parseInt(addidtionalDataFieldLength)));
                        additionalDataEndTag = (additionalDataEndTag + Integer.parseInt(addidtionalDataFieldLength));
                        fieldMap.put(addidtionalDataFieldIdentifier, addidtionalDataFieldValue);
                        AppLogger.i("Field Identifier: " + addidtionalDataFieldIdentifier + ", Field Value: " + addidtionalDataFieldValue);
                    }
                }

                if (additionalDataString.length() != additionalDataEndTag) {
                    addidtionalDataFieldIdentifier = additionalDataString.substring(additionalDataEndTag, additionalDataEndTag + 2);
                    if (addidtionalDataFieldIdentifier.equals(QRCodeEnum.PURPOSE.getValue())) {
                        addidtionalDataFieldLength = String.valueOf((Integer.parseInt(additionalDataString.substring(additionalDataEndTag + 2, additionalDataEndTag + 4))));
                        additionalDataEndTag = additionalDataEndTag + 4;
                        addidtionalDataFieldValue = additionalDataString.substring(additionalDataEndTag, (additionalDataEndTag + Integer.parseInt(addidtionalDataFieldLength)));
                        additionalDataEndTag = (additionalDataEndTag + Integer.parseInt(addidtionalDataFieldLength));
                        fieldMap.put(addidtionalDataFieldIdentifier, addidtionalDataFieldValue);
                        AppLogger.i("Field Identifier: " + addidtionalDataFieldIdentifier + ", Field Value: " + addidtionalDataFieldValue);
                    }
                }
            } else {
                if (addidtionalDataFieldIdentifier.equals(QRCodeEnum.MERCHAN_ID_SUB_CATEGORY.getValue())) {
                    addidtionalDataFieldLength = additionalDataString.substring(additionalDataEndTag + 2, additionalDataEndTag + 4);
                    additionalDataEndTag = 4 + (Integer.parseInt(addidtionalDataFieldLength));
                    addidtionalDataFieldValue = additionalDataString.substring(4, additionalDataEndTag);
                    fieldMap.put(addidtionalDataFieldIdentifier, addidtionalDataFieldValue);
                    AppLogger.i("Field Identifier: " + addidtionalDataFieldIdentifier + ", Field Value: " + addidtionalDataFieldValue);
                }
            }

//            // Purpose field is used to identify whether QR Code is generated for Online or Retail Payment. Device Flow ID is saved against Purpose field
//            if(!TextUtils.isEmpty(addidtionalDataFieldIdentifier) && addidtionalDataFieldIdentifier.equals(QRCodeEnum.PURPOSE.getValue())){
//                addidtionalDataFieldLength = additionalDataString.substring(additionalDataEndTag+2,additionalDataEndTag+4);
//                additionalDataEndTag = Integer.parseInt(addidtionalDataFieldLength);
//                addidtionalDataFieldValue = additionalDataString.substring(4,additionalDataEndTag);
//                fieldMap.put(addidtionalDataFieldIdentifier,addidtionalDataFieldValue);
//                AppLogger.i("Field Identifier: "+addidtionalDataFieldIdentifier+", Field Value: "+addidtionalDataFieldValue);
//            }
        }

        return fieldMap;
    }

    private static Map breakOldMasterCardQRCode(String qr) {
        AppLogger.i("--- Old MasterCard QR Code Breakup Field ---");
        int totalStrLenght = qr.length();

        Map<String, String> fieldMap = new HashMap<>();
        String fieldLength;
        String fieldValue;
        int endTag = 0;
        String fieldIdentifier = qr.substring(endTag, endTag + 2);
        if (fieldIdentifier.equals("00")) {

            fieldLength = qr.substring(endTag + 2, endTag + 4);
            endTag = 4 + (Integer.parseInt(fieldLength) * 2);
            fieldValue = qr.substring(4, endTag);

            AppLogger.i("PAN value before check digit: " + fieldValue);
            String merchantPan = fieldValue.substring(0, fieldValue.length() - 1);
            String checkDigit = PANCheckDigitCalculator.calculateCheckDigit(merchantPan);
            AppLogger.i("PAN Calculated Check digit: " + checkDigit);
            merchantPan = merchantPan + checkDigit;

            fieldMap.put(QRCodeEnum.PAN.getValue(), merchantPan);
            AppLogger.i("Field Identifier: " + fieldIdentifier + ", Field Value: " + merchantPan);
        }

        fieldIdentifier = qr.substring(endTag, endTag + 2);
        if (fieldIdentifier.equals("01")) {
            fieldLength = String.valueOf((Integer.parseInt(qr.substring(endTag + 2, endTag + 4)) * 2));
            endTag = endTag + 4;
            fieldValue = qr.substring(endTag, (endTag + Integer.parseInt(fieldLength)));
            endTag = (endTag + Integer.parseInt(fieldLength));
            fieldMap.put("merchantMobile", fieldValue);
            AppLogger.i("Field Identifier: " + fieldIdentifier + ", Field Value: " + fieldValue);
        }

        fieldIdentifier = qr.substring(endTag, endTag + 2);
        if (fieldIdentifier.equals("03")) {
            fieldLength = String.valueOf((Integer.parseInt(qr.substring(endTag + 2, endTag + 4)) * 2));
            endTag = endTag + 4;
            fieldValue = qr.substring(endTag, (endTag + Integer.parseInt(fieldLength)));
            endTag = (endTag + Integer.parseInt(fieldLength));
            if (fieldValue.startsWith("F")) {
                fieldValue = fieldValue.replace("F", "");
            }
            fieldMap.put(QRCodeEnum.MERCHAN_ID_SUB_CATEGORY.getValue(), fieldValue);
            AppLogger.i("Field Identifier: " + fieldIdentifier + ", Field Value: " + fieldValue);
        }


        fieldIdentifier = qr.substring(endTag, endTag + 2);
        fieldLength = qr.substring(endTag + 2, endTag + 4);
        endTag = endTag + 4;
        fieldValue = qr.substring(endTag, (endTag + Integer.parseInt(fieldLength)));
        endTag = (endTag + Integer.parseInt(fieldLength));
        fieldMap.put(QRCodeEnum.MERCHANT_NAME.getValue(), fieldValue);
        AppLogger.i("Field Identifier: " + fieldIdentifier + ", Field Value: " + fieldValue);

        fieldIdentifier = qr.substring(endTag, endTag + 2);
        fieldLength = String.valueOf((Integer.parseInt(qr.substring(endTag + 2, endTag + 4)) * 2));
        endTag = endTag + 4;
        fieldValue = qr.substring(endTag, (endTag + (Integer.parseInt(fieldLength))));
        endTag = (endTag + Integer.parseInt(fieldLength));
        fieldMap.put(QRCodeEnum.MCC.getValue(), fieldValue);
        AppLogger.i("Field Identifier: " + fieldIdentifier + ", Field Value: " + fieldValue);

        fieldIdentifier = qr.substring(endTag, endTag + 2);
        fieldLength = qr.substring(endTag + 2, endTag + 4);
        endTag = endTag + 4;
        fieldValue = qr.substring(endTag, (endTag + Integer.parseInt(fieldLength)));
        endTag = (endTag + Integer.parseInt(fieldLength));
        fieldMap.put(QRCodeEnum.MERCHANT_CITY.getValue(), fieldValue);
        AppLogger.i("Field Identifier: " + fieldIdentifier + ", Field Value: " + fieldValue);

//        fieldLength = qr.substring(endTag+2,endTag+4);
        fieldIdentifier = qr.substring(endTag, endTag + 2);
        if (fieldIdentifier.equals("0D")) {
            fieldLength = String.valueOf((Integer.parseInt(qr.substring(endTag + 2, endTag + 4))));
            if (fieldLength.equals("03") || fieldLength.equals("3"))
                fieldLength = String.valueOf((Integer.parseInt(qr.substring(endTag + 2, endTag + 4))));
            else
                fieldLength = String.valueOf((Integer.parseInt(qr.substring(endTag + 2, endTag + 4)) * 2));
            endTag = endTag + 4;
            fieldValue = qr.substring(endTag, (endTag + Integer.parseInt(fieldLength)));
            endTag = (endTag + Integer.parseInt(fieldLength));
            fieldMap.put(QRCodeEnum.COUNTRY_CODE.getValue(), fieldValue);
            AppLogger.i("Field Identifier: " + fieldIdentifier + ", Field Value: " + fieldValue);
        }


        fieldIdentifier = qr.substring(endTag, endTag + 2);
        if (fieldIdentifier.equals("0E")) {
            fieldLength = qr.substring(endTag + 2, endTag + 4);
            if (fieldLength.equals("03") || fieldLength.equals("3"))
                fieldLength = qr.substring(endTag + 2, endTag + 4);
            else
                fieldLength = String.valueOf((Integer.parseInt(qr.substring(endTag + 2, endTag + 4)) * 2));
            endTag = endTag + 4;
            fieldValue = qr.substring(endTag, (endTag + Integer.parseInt(fieldLength)));
            endTag = (endTag + Integer.parseInt(fieldLength));
            fieldMap.put(QRCodeEnum.CURRENCY_CODE.getValue(), fieldValue);
            AppLogger.i("Field Identifier: " + fieldIdentifier + ", Field Value: " + fieldValue);
        }

        fieldIdentifier = qr.substring(endTag, endTag + 2);
        if (fieldIdentifier.equals("A1")) {

            fieldLength = qr.substring(endTag + 2, endTag + 4);
            endTag = endTag + 4;
            fieldValue = qr.substring(endTag, (endTag + Integer.parseInt(fieldLength)));
            endTag = (endTag + Integer.parseInt(fieldLength));
            fieldMap.put(QRCodeEnum.TRANSACTION_AMOUNT.getValue(), fieldValue);
            AppLogger.i("Field Identifier: " + fieldIdentifier + ", Field Value: " + fieldValue);
        }


        fieldIdentifier = qr.substring(endTag, endTag + 2);
        if (fieldIdentifier.equals("A3")) {

            fieldLength = qr.substring(endTag + 2, endTag + 4);
            endTag = endTag + 4;
            fieldValue = qr.substring(endTag, (endTag + Integer.parseInt(fieldLength)));
            endTag = (endTag + Integer.parseInt(fieldLength));
            fieldMap.put(QRCodeEnum.BILL_NO.getValue(), fieldValue);
            AppLogger.i("Field Identifier: " + fieldIdentifier + ", Field Value: " + fieldValue);
        }


        fieldIdentifier = qr.substring(endTag, endTag + 2);
        if (fieldIdentifier.equals("A5")) {

            fieldLength = qr.substring(endTag + 2, endTag + 4);
            endTag = endTag + 4;
            fieldValue = qr.substring(endTag, (endTag + Integer.parseInt(fieldLength)));
            endTag = (endTag + Integer.parseInt(fieldLength));
            if (fieldValue.length() == 12) {
                fieldMap.put(QRCodeEnum.REFERENCE_NO.getValue(), fieldValue);
                AppLogger.i("Field Identifier: " + fieldIdentifier + ", Field Value: " + fieldValue);
            } else {
                fieldMap.put(QRCodeEnum.MERCHAN_ID_SUB_CATEGORY.getValue(), fieldValue);
                AppLogger.i("Field Identifier: " + fieldIdentifier + ", Field Value: " + fieldValue);
            }

        }

        if (endTag != totalStrLenght) {
            long remaining = Long.valueOf(String.valueOf(totalStrLenght)) - Long.valueOf(String.valueOf(endTag));
            if (remaining != 4) {
                fieldLength = qr.substring(endTag + 2, endTag + 4);
                endTag = endTag + 4;
                fieldValue = qr.substring(endTag, (endTag + Integer.parseInt(fieldLength)));
                fieldMap.put(QRCodeEnum.MERCHAN_ID_SUB_CATEGORY.getValue(), fieldValue);
                AppLogger.i("Field Identifier: " + fieldIdentifier + ", Field Value: " + fieldValue);
            }
        }

        return fieldMap;
    }
}
