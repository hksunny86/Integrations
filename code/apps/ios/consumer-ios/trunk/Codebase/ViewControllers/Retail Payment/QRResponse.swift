//
//  QRResponse.swift
//  JSBL-MB
//
//  Created by Maria Alvi on 11/20/17.
//  Copyright © 2017 inov8. All rights reserved.
//

import Foundation

class QRResponse : NSObject {
    
    var PANNumber:String  = ""
    var merchantName : String  = ""
    var merchantCategoryCode:String  = ""
    var merchantCity : String  = ""
    var merchantCountryCode : String  = ""
    var merchantCurrencyCode : String  = ""
    var billNO : String  = ""
    var referenceID : String  = ""
    var merchantID : String  = ""
    var productID : String  = ""
    var amount : String  = ""
    var CRC_VALUE : String  = ""
    var payloadformat : String  = ""
    var merchantMOBILE_ : String  = ""
    var qrString: String = ""

    
    class   func parseQrCode(_ string: String) ->  QRResponse {
        
        let QRObj  = QRResponse()
        
        QRObj.qrString = string
        
        var startTag: Int = 0
        let fieldLengthGlobal: String = (string as NSString).substring(with: NSRange(location: startTag + 2, length: 2))
        if fieldLengthGlobal.isEqual("02") {
            while startTag < (string.count) - 4 {
                let qrTagValue: String = (string as NSString).substring(with: NSRange(location: startTag, length: 2))
                let fieldLength: String = (string as NSString).substring(with: NSRange(location: startTag + 2, length: 2))
                let fieldLengthInt = Int(fieldLength) ?? 0
                startTag = startTag + 4
                let fieldValue: String = (string as NSString).substring(with: NSRange(location: startTag, length: fieldLengthInt))
                if (qrTagValue == Constants.QRCode_ID.QR_PAN_TAG) {
                    
                   QRObj.PANNumber = fieldValue
                }
                else if (qrTagValue == Constants.QRCode_ID.QR_MERCHANT_NAME_TAG) {
                    QRObj.merchantName = fieldValue
                }
                else if (qrTagValue == Constants.QRCode_ID.QR_MERCHANT_CATEGORY_TAG) {
                    QRObj.merchantCategoryCode = fieldValue
                }
                else if (qrTagValue == Constants.QRCode_ID.QR_MERCHANT_CITY_TAG) {
                    QRObj.merchantCity = fieldValue
                }
                else if (qrTagValue == Constants.QRCode_ID.QR_MERCHANT_COUNTRY_CODE_TAG) {
                   QRObj.merchantCountryCode = fieldValue
                }
                else if (qrTagValue == Constants.QRCode_ID.QR_MERCHANT_CURRENCY_CODE_TAG) {
                   QRObj.merchantCurrencyCode = fieldValue
                }
                else if (qrTagValue == Constants.QRCode_ID.QR_ADDITIONAL_DATA_TAG) {
                    var additionalStartTag: Int = startTag
                    let additionalEndTag: Int = startTag + fieldLengthInt
                    while additionalStartTag < additionalEndTag {
                        let subTagValue: String = (string as NSString).substring(with: NSRange(location: additionalStartTag, length: 2))
                        let subFieldLength: String = (string as NSString).substring(with: NSRange(location: additionalStartTag + 2, length: 2))
                        additionalStartTag += 4
                        let subFieldLengthInt = Int(subFieldLength) ?? 0
                        let subFieldValue: String = (string as NSString).substring(with: NSRange(location: additionalStartTag, length: subFieldLengthInt))
                    
                        if (subTagValue == Constants.QRCode_ID.QR_BILL_NO_TAG) {
                           QRObj.billNO = subFieldValue
                        }
                        else if (subTagValue == Constants.QRCode_ID.QR_REFERENCE_ID_TAG) {
                            QRObj.referenceID = subFieldValue
                        }
                        else if (subTagValue == Constants.QRCode_ID.QR_MERCHANT_ID_TAG) {
                           QRObj.merchantID = subFieldValue
                        }
                        else if (subTagValue == Constants.QRCode_ID.QR_PRODUCT_ID_TAG) {
                           QRObj.productID = subFieldValue
                        }
                        
                        additionalStartTag += subFieldLengthInt
                    }
                }
                    
                else if (qrTagValue == Constants.QRCode_ID.QR_AMOUNT_TAG) {
                    QRObj.amount = fieldValue
                }
                else if (qrTagValue == Constants.QRCode_ID.QR_CRC_VALUE) {
                   QRObj.CRC_VALUE = fieldValue
                }
                else if (qrTagValue == Constants.QRCode_ID.QR_PAYLOAD_FORMAT_TAG) {
                    QRObj.payloadformat = fieldValue
                }
                else if (qrTagValue == Constants.QRCode_ID.QR_POI_TAG) {
                  QRObj.merchantMOBILE_ = fieldValue
                }
                
                startTag = startTag + fieldLengthInt
            }
        }
        else if fieldLengthGlobal.isEqual("08") {
            while startTag < (string.count) - 4 {
                let qrTagValue: String = (string as NSString).substring(with: NSRange(location: startTag, length: 2))
                let fieldLength: String = (string as NSString).substring(with: NSRange(location: startTag + 2, length: 2))
                var fieldLengthInt = Int(fieldLength) ?? 0
                if (qrTagValue == Constants.QRCode_ID.QR_PAN_TAG || qrTagValue == Constants.QRCode_ID.QR_MERCHANT_CATEGORY_TAG_OLD  || qrTagValue == Constants.QRCode_ID.QR_MERCHANT_CURRENCY_CODE_TAG_OLD  || qrTagValue == Constants.QRCode_ID.QR_MERCHANT_MOBILE_NO_OLD ) {
                    
                    fieldLengthInt = fieldLengthInt * 2
                    
                }
                startTag = startTag + 4
                let fieldValue: String = (string as NSString).substring(with: NSRange(location: startTag, length: fieldLengthInt))
        
                if (qrTagValue == Constants.QRCode_ID.QR_PAN_TAG) {
                    QRObj.PANNumber = fieldValue
                }
                else if (qrTagValue == Constants.QRCode_ID.QR_MERCHANT_NAME_TAG_OLD) {
                    QRObj.merchantName = fieldValue
                }
                else if (qrTagValue == Constants.QRCode_ID.QR_MERCHANT_CATEGORY_TAG_OLD) {
                    QRObj.merchantCategoryCode = fieldValue
                }
                else if (qrTagValue == Constants.QRCode_ID.QR_MERCHANT_CITY_TAG_OLD) {
                   QRObj.merchantCity = fieldValue
                }
                else if (qrTagValue == Constants.QRCode_ID.QR_MERCHANT_COUNTRY_CODE_TAG_OLD) {
                    QRObj.merchantCountryCode = fieldValue
                }
                else if (qrTagValue == Constants.QRCode_ID.QR_MERCHANT_CURRENCY_CODE_TAG_OLD) {
                   QRObj.merchantCurrencyCode = fieldValue
                }
                else if (qrTagValue == Constants.QRCode_ID.QR_MERCHANT_ID_TAG_OLD) {
                    QRObj.merchantID = fieldValue
                }
                else if (qrTagValue == Constants.QRCode_ID.QR_AMOUNT_TAG_OLD) {
                    QRObj.amount = fieldValue
                }
                
                startTag = startTag + fieldLengthInt
            }
            let crcCode: String? = (string as NSString).substring(from: string.count - 4)
            QRObj.CRC_VALUE = crcCode!
            
        }
        return QRObj
    }
}
