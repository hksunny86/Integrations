//
//  DeviceDemographics.swift
//  Timepey
//
//  Created by Adnan Ahmed on 26/09/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation
import CoreTelephony
import UIKit

class DeviceDemographics{
    
    class func getCarrierName() -> String {
        
        let networkInfo = CTTelephonyNetworkInfo()
        let carrier = networkInfo.subscriberCellularProvider
        var carrierName: String?
        if(carrier != nil){
            if(carrier!.carrierName != nil){
                carrierName = carrier!.carrierName
            }else{
                carrierName = ""
            }
        }else{
            carrierName = ""
        }
        return carrierName!
        
    }
    
    class func getUDID() -> String{
        
        return (UIDevice.current.identifierForVendor?.uuidString)!
        
    }
    
    class func getModel() -> String{
        return UIDevice.current.model
    }
    
    class func getOSVersion() -> String{
        return UIDevice.current.systemVersion
    }
    
}
