//
//  Product.swift
//  Timepey
//
//  Created by Adnan Ahmed on 14/07/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import AEXML

class Product: NSObject, NSCoding {
    
    var amtRequired:String?
    var dovalidate:String?
    var fID:String?
    var id:String?
    var label:String?
    var minamt:String?
    var minamtf:String?
    var maxamt:String?
    var maxamtf:String?
    var multiple:String?
    var name:String?
    var consumerMinLength: String?
    var consumerMaxLength: String?
    var inrequired: String?
    var pprequired: String?
    var type: String?
    var denomFlag: String?
    var prodDenom: String?
    var denomString: String?
    var url: String?
    
    // MARK: NSCoding
    func encode(with encoder:NSCoder) {
        encoder.encode(self.amtRequired, forKey: "amtRequired")
        encoder.encode(self.dovalidate, forKey: "dovalidate")
        encoder.encode(self.fID, forKey: "fID")
        encoder.encode(self.id, forKey: "id")
        encoder.encode(self.label, forKey: "label")
        encoder.encode(self.minamt, forKey: "minamt")
        encoder.encode(self.minamtf, forKey: "minamtf")
        encoder.encode(self.maxamt, forKey: "maxamt")
        encoder.encode(self.maxamtf, forKey: "maxamtf")
        encoder.encode(self.multiple, forKey: "multiple")
        encoder.encode(self.name, forKey: "name")
        encoder.encode(self.consumerMinLength, forKey: "consumerMinLength")
        encoder.encode(self.consumerMaxLength, forKey: "consumerMaxLength")
        encoder.encode(self.inrequired, forKey: "inrequired")
        encoder.encode(self.pprequired, forKey: "ppallowed")
        encoder.encode(self.prodDenom, forKey: "prodDenom")
        encoder.encode(self.denomFlag, forKey: "denomFlag")
        encoder.encode(self.denomString, forKey: "denomString")
        encoder.encode(self.url, forKey: "url")
        encoder.encode(self.type, forKey: "type")
    }
    required convenience init(coder decoder:NSCoder){
        self.init()
        amtRequired = decoder.decodeObject(forKey: "amtRequired") as? String
        dovalidate = decoder.decodeObject(forKey: "dovalidate") as? String
        fID = decoder.decodeObject(forKey: "fID") as? String
        id = decoder.decodeObject(forKey: "id") as? String
        label = decoder.decodeObject(forKey: "label") as? String
        minamt = decoder.decodeObject(forKey: "minamt") as? String
        minamtf = decoder.decodeObject(forKey: "minamtf") as? String
        maxamt = decoder.decodeObject(forKey: "maxamt") as? String
        maxamtf = decoder.decodeObject(forKey: "maxamtf") as? String
        multiple = decoder.decodeObject(forKey: "multiple") as? String
        name = decoder.decodeObject(forKey: "name") as? String
        consumerMinLength = decoder.decodeObject(forKey: "consumerMinLength") as? String
        consumerMaxLength = decoder.decodeObject(forKey: "consumerMaxLength") as? String
        inrequired = decoder.decodeObject(forKey: "inrequired") as? String
        pprequired = decoder.decodeObject(forKey: "ppallowed") as? String
        type = decoder.decodeObject(forKey: "type") as? String
        prodDenom = decoder.decodeObject(forKey: "prodDenom") as? String
        denomFlag = decoder.decodeObject(forKey: "denomFlag") as? String
        denomString = decoder.decodeObject(forKey: "denomString") as? String
        url = decoder.decodeObject(forKey: "url") as? String
    }
    
    
    func parseProductXML(_ AEXMLElementObj: AEXMLElement){
        
        self.amtRequired = AEXMLElementObj.attributes[Constants.ProductsXML.AMT_REQUIRED]
        self.dovalidate = AEXMLElementObj.attributes[Constants.ProductsXML.DOVALIDATE]
        self.fID = AEXMLElementObj.attributes[Constants.ProductsXML.FID]
        self.id = AEXMLElementObj.attributes[Constants.ProductsXML.ID]
        self.label = AEXMLElementObj.attributes[Constants.ProductsXML.LABEL]
        self.minamt = AEXMLElementObj.attributes[Constants.ProductsXML.MINAMT]
        self.minamtf = AEXMLElementObj.attributes[Constants.ProductsXML.MINAMTF]
        self.maxamt = AEXMLElementObj.attributes[Constants.ProductsXML.MAXAMT]
        self.maxamtf = AEXMLElementObj.attributes[Constants.ProductsXML.MAXAMTF]
        self.multiple = AEXMLElementObj.attributes[Constants.ProductsXML.MULTIPLE]
        self.name = AEXMLElementObj.attributes["name"]
        self.consumerMinLength = AEXMLElementObj.attributes["consumerMinLength"]
        self.consumerMaxLength = AEXMLElementObj.attributes["consumerMaxLength"]
        self.inrequired = AEXMLElementObj.attributes["inrequired"]
        self.pprequired = AEXMLElementObj.attributes["ppallowed"]
        self.type = AEXMLElementObj.attributes["type"]
        self.prodDenom = AEXMLElementObj.attributes["prodDenom"]
        self.denomString = AEXMLElementObj.attributes["denomString"]
        self.denomFlag = AEXMLElementObj.attributes["denomFlag"]
        self.url = AEXMLElementObj.attributes["url"]
        
    }
    
    override var description: String { get {
        return "amtRequired: \(String(describing: amtRequired))  \ndovalidate: \(String(describing: dovalidate)) \nfID: \(String(describing: fID)) \nid: \(String(describing: id)) \nlabel: \(String(describing: label)) \nminamt: \(String(describing: minamt)) \nminamtf: \(String(describing: minamtf)) \nmaxamt: \(String(describing: maxamt)) \nmaxamtf: \(String(describing: maxamtf)) \nmultiple: \(String(describing: multiple)) \nname: \(String(describing: name)) \nconsumerMinLength: \(String(describing: consumerMinLength)) \nconsumerMaxLength: \(String(describing: consumerMaxLength)) \ninrequired: \(String(describing: inrequired)) \npprequired: \(String(describing: pprequired)) \ntype: \(String(describing: type)) \nprodDenom: \(String(describing: prodDenom)) \ndenomString: \(String(describing: denomString)) \ndenomFlag: \(String(describing: denomFlag)) \nURL: \(String(describing: url))"
        }
    }
    
}
