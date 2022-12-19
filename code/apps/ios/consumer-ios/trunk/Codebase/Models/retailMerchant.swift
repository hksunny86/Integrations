//
//  MerchantVerification.swift
//  JSBL-MB
//
//  Created by Maria Alvi on 11/28/17.
//  Copyright © 2017 inov8. All rights reserved.
//

import Foundation
import Foundation
import AEXML

class retailMerchant : NSObject {
    
    var merchantID : String = ""
    var totalAmount : String = ""
    var totalAmountFormatted : String = ""
    var merchantName : String = ""
    var merchant : String = ""
    var transactionID : String = ""
    var transactionDate : String = ""
    var transactionDateFormatted : String = ""
    var transactionTimeFormatted : String = ""
    var PROD : String = ""

    func parseMerchantTransferResponse(_ AEXMLElementObj: AEXMLElement) {
        
        self.transactionID = AEXMLElementObj.attributes["TRXID"] != nil ? AEXMLElementObj.attributes["TRXID"]! : ""
        self.transactionDate = AEXMLElementObj.attributes["DATE"] != nil ? AEXMLElementObj.attributes["DATE"]! : ""
        self.transactionDateFormatted = AEXMLElementObj.attributes["DATEF"] != nil ? AEXMLElementObj.attributes["DATEF"]! : ""
        self.transactionTimeFormatted = AEXMLElementObj.attributes["TIMEF"] != nil ? AEXMLElementObj.attributes["TIMEF"]! : ""
        self.PROD = AEXMLElementObj.attributes["PROD"] != nil ? AEXMLElementObj.attributes["PROD"]! : ""
        
        self.merchantName = AEXMLElementObj.attributes["MNAME"] != nil ? AEXMLElementObj.attributes["MNAME"]! : ""
        self.totalAmount = AEXMLElementObj.attributes["TXAM"] != nil ? AEXMLElementObj.attributes["TXAM"]! : ""
        self.totalAmountFormatted = AEXMLElementObj.attributes["TXAMF"] != nil ? AEXMLElementObj.attributes["TXAMF"]! : ""
        
    }
    
    
    
}

