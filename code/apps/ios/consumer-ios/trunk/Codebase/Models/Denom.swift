//
//  Denoms.swift
//  Timepey
//
//  Created by Adnan Ahmed on 14/07/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import AEXML

class Denom: NSObject {
    
    var amt:String?
    var amtf:String?
    var code:String?
    var isActive:String?
    var name:String?
    
    convenience init(obj: Denom)
    {
        self.init()
        self.amt = ""
        self.amtf = ""
        self.code = ""
        self.isActive = ""
        self.name = ""
    }
    
    func parseAdsXML(_ AEXMLElementObj: AEXMLElement){
        
        self.amt = AEXMLElementObj.attributes["amt"]
        self.amtf = AEXMLElementObj.attributes["amtf"]
        self.code = AEXMLElementObj.attributes["code"]
        self.isActive = AEXMLElementObj.attributes["isActive"]
        self.name = AEXMLElementObj.attributes["name"]
    }
    
    override var description: String{get{
        return "amt: \(String(describing: amt)) \namtf: \(String(describing: amtf)) \ncode: \(String(describing: code)) \nisActive: \(String(describing: isActive)) \nname: \(String(describing: name))"
        }
    }
}
