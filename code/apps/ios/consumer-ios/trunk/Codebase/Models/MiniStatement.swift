//
//  MiniStatement.swift
//  Timepey
//
//  Created by Adnan Ahmed on 12/07/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import AEXML

class MiniStatement: NSObject {
    
    var date:String?
    var datef:String?
    var desc: String?
    var tamt:String?
    var tamtf:String?
    
    convenience init(obj: MiniStatement)
    {
        self.init()
        self.datef = ""
        self.date = ""
        self.datef = ""
        self.desc = ""
        self.tamt = ""
        self.tamtf = ""
        
    }
    
    func parseXML(_ AEXMLElementObj: AEXMLElement){
        
        self.date = AEXMLElementObj.attributes["DATE"]
        self.datef = AEXMLElementObj.attributes["DATEF"]
        self.desc = AEXMLElementObj.attributes["DESCRIPTION"]
        self.tamt = AEXMLElementObj.attributes["TAMT"]
        self.tamtf = AEXMLElementObj.attributes["TAMTF"]
    }
    
    override var description: String{get{
        return "date: \(String(describing: date)) \ndatef: \(String(describing: datef)) \ndesc: \(String(describing: desc)) \ntamt: \(String(describing: tamt)) \ntamtf: \(String(describing: tamtf))"
        }
    }
}
