//
//  Bank.swift
//  Timepey
//
//  Created by Adnan Ahmed on 15/06/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//
import AEXML

class Bank: NSObject {
    
    var id:String?
    var isBank:String?
    var name:String?
    var imd: String?
    var minLength: String?
    var maxLength: String?
    var pinLevel:String?
    var key:String?
    var mod:String?
    var accounts = [Account]()
    
    convenience init(obj: Bank)
    {
        self.init()
        self.id = ""
        self.isBank = ""
        self.name = ""
        self.imd = ""
        self.minLength = ""
        self.maxLength = ""
        self.pinLevel = ""
        self.key = ""
        self.mod = ""
    }
    
    func parseBankXML(_ AEXMLElementObj: AEXMLElement){
        
        self.id = AEXMLElementObj.attributes["id"]
        self.isBank = AEXMLElementObj.attributes["isBank"]
        self.name = AEXMLElementObj.attributes["name"]
        self.imd = AEXMLElementObj.attributes["imd"]
        self.minLength = AEXMLElementObj.attributes["minlength"]
        self.maxLength = AEXMLElementObj.attributes["maxlength"]
        self.pinLevel = AEXMLElementObj.attributes["pinLevel"]
        self.key = AEXMLElementObj.attributes["key"]
        self.mod = AEXMLElementObj.attributes["mod"]
        
        if(AEXMLElementObj.children.count > 0){
            for childAcc in AEXMLElementObj.children{
                //print(childAcc)
                if(childAcc.name == "acc"){
                    let accObj = Account()
                    accObj.parseAccountXML(childAcc)
                    self.accounts.append(accObj)
                }
            }
        }
    }
    
    func parseMemberBankXML(_ AEXMLElementObj: AEXMLElement){
        self.name = AEXMLElementObj.attributes["name"]
        self.imd = AEXMLElementObj.attributes["IMD"]
        self.minLength = AEXMLElementObj.attributes["MIN_LENGTH"]
        self.maxLength = AEXMLElementObj.attributes["MAX_LENGTH"]
    }
    
    override var description: String{get{
        return "id: \(String(describing: id)) \nisBank: \(String(describing: isBank)) \nname: \(String(describing: name)) \nminLength: \(String(describing: minLength)) \nmaxLength: \(String(describing: maxLength)) \npinLevel: \(String(describing: pinLevel)) \nkey: \(String(describing: key)) \nmod: \(String(describing: mod)) \naccounts: \(accounts)"
        }
    }
}

class TPURPS: NSObject {
    
    var id:String?
    var name:String?
    
    convenience init(obj: Bank)
    {
        self.init()
        
    }
    
    func parseTpurpsXML(_ AEXMLElementObj: AEXMLElement){
        
        self.id = AEXMLElementObj.attributes["code"]
        self.name = AEXMLElementObj.attributes["name"]
    }
    
    override var description: String{get{
        return "code: \(String(describing: id)) \nname: \(String(describing: name))"
        }
    }
}

