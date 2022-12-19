//
//  Category.swift
//  Timepey
//
//  Created by Adnan Ahmed on 03/06/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation
import AEXML

class Category: NSObject, NSCoding {
    
    var cID:String?
    var fID:String?
    var icon:String?
    var isActive:String?
    var isProduct:String?
    var msg:String?
    var name:String?
    var seq:String?
    var categories = [Category]()
    var products = [Product]()
    var url:String?
    
// MARK: NSCoding
    func encode(with encoder:NSCoder) {
        encoder.encode(self.cID, forKey: "cID")
        encoder.encode(self.fID, forKey: "fID")
        encoder.encode(self.icon, forKey: "icon")
        encoder.encode(self.isActive, forKey: "isActive")
        encoder.encode(self.isProduct, forKey: "isProduct")
        encoder.encode(self.msg, forKey: "msg")
        encoder.encode(self.name, forKey: "name")
        encoder.encode(self.seq, forKey: "seq")
        encoder.encode(self.categories, forKey: "categories")
        encoder.encode(self.products, forKey: "products")
        encoder.encode(self.url, forKey: "url")
    }
    required convenience init(coder decoder:NSCoder){
        self.init()
        cID = decoder.decodeObject(forKey: "cID") as? String
        fID = decoder.decodeObject(forKey: "fID") as? String
        icon = decoder.decodeObject(forKey: "icon") as? String
        isActive = decoder.decodeObject(forKey: "isActive") as? String
        isProduct = decoder.decodeObject(forKey: "isProduct") as? String
        msg = decoder.decodeObject(forKey: "msg") as? String
        name = decoder.decodeObject(forKey: "name") as? String
        seq = decoder.decodeObject(forKey: "seq") as? String
        categories = (decoder.decodeObject(forKey: "categories") as? [Category])!
        products = (decoder.decodeObject(forKey: "products") as? [Product])!
        url = (decoder.decodeObject(forKey: "url")) as? String
    }
    
    func parseCategoryXML(_ AEXMLElementObj: AEXMLElement){
        
        self.cID = AEXMLElementObj.attributes[Constants.CatelogXML.CAT_ID]
        self.name = AEXMLElementObj.attributes[Constants.CatelogXML.CAT_NAME]
        self.msg = AEXMLElementObj.attributes[Constants.CatelogXML.CAT_MSG]
        self.icon = AEXMLElementObj.attributes[Constants.CatelogXML.CAT_ICON]
        self.seq = AEXMLElementObj.attributes[Constants.CatelogXML.CAT_SEQ_KEY]
        self.isActive = AEXMLElementObj.attributes[Constants.CatelogXML.CAT_ISACTIVE_KEY]
        self.isProduct = AEXMLElementObj.attributes[Constants.CatelogXML.CAT_ISPRODUCT_KEY]
        self.fID = AEXMLElementObj.attributes[Constants.CatelogXML.CAT_FLOW_ID]
        self.url = AEXMLElementObj.attributes[Constants.CatelogXML.CAR_IMAGE_URL]
        
        if(AEXMLElementObj.children.count >  0){
            
            for childCat in AEXMLElementObj.children{
                if(childCat.name == "category"){
                    let catObj = Category()
                    catObj.parseCategoryXML(childCat)
                    self.categories.append(catObj)
                }
                if(childCat.name == "prds"){
                    for prd in childCat.children{
                        let prdObj = Product()
                        prdObj.parseProductXML(prd)
                        self.products.append(prdObj)
                    }
                }
            }
        }
    }
    
    override var description: String { get {
        return "cID: \(String(describing: cID))  \nfID: \(String(describing: fID)) \nicon: \(String(describing: icon)) \nisActive: \(String(describing: isActive)) \nisProduct: \(String(describing: isProduct)) \nmsg: \(String(describing: msg))  \nname: \(String(describing: name)) \nseq: \(String(describing: seq)) \ncategories: \(categories) \nURL: \(String(describing: url))\n \nproducts: \(products)" }
    }
    
}
