//
//  XMLError.swift
//  Timepey
//
//  Created by Adnan Ahmed on 12/07/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//
import AEXML

class XMLError: NSObject{

    var code:String?
    var level:String?
    var msg: String?
    
    convenience init(obj: XMLError)
    {
        self.init()
        self.code = ""
        self.level = ""
        self.msg = ""
    }
    
    func parseXML(_ AEXMLElementObj: AEXMLElement){
        
        self.code = AEXMLElementObj.attributes["code"]
        self.level = AEXMLElementObj.attributes["level"]
        self.msg = AEXMLElementObj.value
    }
    
    override var description: String{get{
        return "code: \(String(describing: code)) \nlevel: \(String(describing: level)) \nmsg: \(String(describing: msg))"
        }
    }
}

