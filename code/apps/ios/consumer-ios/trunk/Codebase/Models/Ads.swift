//
//  Ads.swift
//  Timepey
//
//  Created by Adnan Ahmed on 14/07/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import AEXML
import Foundation
import UIKit

class AdImage : NSObject {
    
    var arrAdImages = [String]()
    var adImage: UIImage?
    
    convenience init(obj: AdImage)
    {
        self.init()
        
        self.adImage = nil
        self.arrAdImages = [""]
    }
    
    class func parse(_ dict: [String:String]) -> AdImage {
        let addImage = AdImage()
        let imageName = "\(Constants.ServerConfig.BASE_URL)/images/ads/\(dict["name"]!)"
        addImage.arrAdImages.append(imageName)
        //print(defaults.object(forKey: "ads") as! [String])
        return addImage
    }
    
    
}
