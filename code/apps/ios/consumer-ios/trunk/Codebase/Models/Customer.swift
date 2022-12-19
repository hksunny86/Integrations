//
//  Customer.swift
//  Timepey
//
//  Created by Adnan Ahmed on 06/06/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation
import AEXML

class Customer: NSObject {
    
    struct Static
    {
        static var instance: Customer?
    }

    class var sharedInstance: Customer {
        if Static.instance == nil
        {
            Static.instance = Customer()
        }
        return Static.instance!
    }
    
    var usty: String?
    var aType: String?
    var fName: String?
    var lName: String?
    var ipcr: String?
    var tstr: String?
    var appV: String?
    var apul: String?
    var balance: String?
    var balf: String?
    var tbam: String?
    var impcr: String?
    var pgr: String?
    var cMob: String?
    var ipcca: String?
    var mbanks: [Bank]?
    var tpurps: [TPURPS]?
    var tpurp: TPURPS?
    var bank: Bank?
    var adType: String?
    var cnic: String?
    var email: String?
    var isMigrated: String?
    var isMpinSetLater: String?
    var iBAN:String?
    
    
    class func destroy() {
        Customer.Static.instance = nil
    }
    
    func addParseResults(_ dict: [String: String]){
        self.usty = dict["USTY"] != nil ? dict["USTY"] : ""
        self.aType = dict["ATYPE"] != nil ? dict["ATYPE"] : ""
        self.fName = dict["FNAME"] != nil ? dict["FNAME"] : ""
        self.lName = dict["LNAME"] != nil ? dict["LNAME"] : ""
        self.ipcr = dict["IPCR"] != nil ? dict["IPCR"] : ""
        self.tstr = dict["TSTR"] != nil ? dict["TSTR"] : ""
        self.appV = dict["APPV"] != nil ? dict["APPV"] : ""
        self.apul = dict["APUL"] != nil ? dict["APUL"] : ""
        self.balance = dict["BAL"] != nil ? dict["BAL"] : ""
        self.balf = dict["BALF"] != nil ? dict["BALF"] : ""
        self.tbam = dict["TBAM"] != nil ? dict["TBAM"] : ""
        self.impcr = dict["IMPCR"] != nil ? dict["IMPCR"] : ""
        self.pgr = dict["PGR"] != nil ? dict["PGR"] : ""
        self.cMob = dict["MOBN"] != nil ? dict["MOBN"] : ""
        self.ipcca = dict["IPCCA"] != nil ? dict["IPCCA"] : ""
        self.adType = dict["ADTYPE"] != nil ? dict["ADTYPE"] : ""
        self.cnic = dict["CNIC"] != nil ? dict["CNIC"] : ""
        self.email = dict["EMAIL"] != nil ? dict["EMAIL"] : ""
        self.isMigrated = dict["IS_MIGRATED"] != nil ? dict["IS_MIGRATED"] : ""
        self.isMpinSetLater = dict["IS_SET_MPIN_LATER"] != nil ? dict["IS_SET_MPIN_LATER"] : ""
        self.iBAN = dict["SENDER_IBAN"] != nil ? dict["SENDER_IBAN"] : ""
    }
    
    override var description: String { get {
        
        return "usty: \(String(describing: usty)) \naType: \(String(describing: aType)) \nfName: \(String(describing: fName)) \nlName: \(String(describing: lName)) \nipcr: \(String(describing: ipcr)) \ntstr: \(String(describing: tstr)) \nappV: \(String(describing: appV)) \napul: \(String(describing: apul)) \nBalance: \(String(describing: balance)) \nbalf: \(String(describing: balf)) \ntbam: \(String(describing: tbam)) \nimpcr: \(String(describing: impcr)) \npgr: \(String(describing: pgr)) \ncMob: \(String(describing: cMob)) \nipcca: \(String(describing: ipcca)) \nmbanks: \(String(describing: mbanks)) \ntpurps: \(String(describing: tpurps)) \nbanks: \(String(describing: bank)) \ntpurp: \(String(describing: tpurp)) \nCNIC: \(String(describing: cnic)) \nadtype: \(String(describing: adType))"}
        
    }
}
