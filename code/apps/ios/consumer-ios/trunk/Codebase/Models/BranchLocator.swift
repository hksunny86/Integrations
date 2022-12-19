//
//  BranchLocator.swift
//  Timepey
//
//  Created by Adnan Ahmed on 16/10/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//


import UIKit
import AEXML
import MapKit
import Foundation

class BranchLocator: NSObject {
    
    var name:String?
    var distance:String?
    var contact: String?
    var address:String?
    var lat:String?
    var long:String?
    var location:CLLocationCoordinate2D?
    
    convenience init(obj: BranchLocator)
    {
        self.init()
        self.name = ""
        self.distance = ""
        self.contact = ""
        self.address = ""
        self.lat = ""
        self.long = ""
    }
    
    func parseXML(_ AEXMLElementObj: AEXMLElement){
        
        self.name = AEXMLElementObj.attributes["NAME"]
        self.distance = AEXMLElementObj.attributes["DISTANCE"]
        self.contact = AEXMLElementObj.attributes["CONTACT"]
        self.address = AEXMLElementObj.attributes["ADD"]
        self.lat = AEXMLElementObj.attributes["LATITUDE"]
        self.long = AEXMLElementObj.attributes["LONGITUDE"]
        
        
        if (AEXMLElementObj.attributes["LATITUDE"] != nil && AEXMLElementObj.attributes["LONGITUDE"] != nil) {
            
            self.location = CLLocationCoordinate2DMake(Double(self.lat! as String)!,Double(self.long! as String)!)
        }
        
    }
    
    override var description: String{get{
        return "name: \(String(describing: name)) \ndistance: \(String(describing: distance)) \ncontact: \(String(describing: contact)) \nadd: \(String(describing: address)) \nlatitude: \(String(describing: lat)) \nlongitude: \(String(describing: long)) \nlocation: \(String(describing: location))"
        }
    }
    
}
