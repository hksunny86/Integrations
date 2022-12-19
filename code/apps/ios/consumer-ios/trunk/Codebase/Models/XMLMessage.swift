//
//  XMLMessage.swift
//  Timepey
//
//  Created by Adnan Ahmed on 14/07/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import AEXML

class XMLMessage: NSObject{
    
    var level:String?
    var msg:String?
    var code:String?
    
    convenience init(obj: XMLError)
    {
        self.init()
        self.level = ""
        self.msg = ""
        self.code = ""
    }
    
    func parseXML(_ AEXMLElementObj: AEXMLElement){
        
        self.level = AEXMLElementObj.attributes["level"]
        self.code = AEXMLElementObj.attributes["code"]
        self.msg = AEXMLElementObj.value
        
    }
    
    override var description: String{get{
        return "level: \(String(describing: level)) code: \(String(describing: code)) \nmsg: \(String(describing: msg))"
        }
    }
}
