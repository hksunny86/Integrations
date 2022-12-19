//
//  Account.swift
//  Timepey
//
//  Created by Adnan Ahmed on 14/07/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import AEXML

class Account: NSObject {
    
    var id:String?
    var nick:String?
    var isBPinReq:String?
    var isDef:String?
    var pinChReq:String?
    var cvv:String?
    var tpin:String?
    var mpin:String?
    
    convenience init(obj: Account)
    {
        self.init()
        self.id = ""
        self.nick = ""
        self.isBPinReq = ""
        self.isDef = ""
        self.pinChReq = ""
        self.cvv = ""
        self.tpin = ""
        self.mpin = ""
    }
    
    func parseAccountXML(_ AEXMLElementObj: AEXMLElement){
        self.id = AEXMLElementObj.attributes["id"]
        self.nick = AEXMLElementObj.attributes["nick"]
        self.isBPinReq = AEXMLElementObj.attributes["isBPinReq"]
        self.cvv = AEXMLElementObj.attributes["cvv"]
        self.tpin = AEXMLElementObj.attributes["tpin"]
        self.mpin = AEXMLElementObj.attributes["mpin"]
    }
    
    override var description: String{get{
        return "id: \(String(describing: id)) \nnick: \(String(describing: nick)) \nisBPinReq: \(String(describing: isBPinReq)) \ncvv: \(String(describing: cvv)) \ntpin: \(String(describing: tpin)) \nmpin: \(String(describing: mpin))"
        }
    }
}
