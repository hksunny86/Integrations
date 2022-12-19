//
//  LoginWebAPI.swift
//  Timepey
//
//  Created by Adnan Ahmed on 17/06/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation


class UserAccountAPI
{
    
    func postLoginRequest(_ commandId:String, reqTime: String, userId:String, pin: String, ENCT:String, DTID:String, udid: String, appVersion: String, APPID: String, USTY: String, Operating_System: String, OSVERSION: String, iPHONE_MODEL: String, VENDOR: String, ispNetwork: String, CVNO: String, actionType: String,
                          onSuccess: @escaping (_ data: Data) ->(),
                          onFailure: @escaping (_ reason: String) ->())
    {
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"PIN\">\(pin)</param><param name=\"ENCT\">\(ENCT)</param><param name=\"UID\">\(userId)</param><param name=\"APPV\">\(appVersion)</param><param name=\"DTID\">\(DTID)</param><param name=\"UDID\">\(udid)</param><param name=\"ISROOTED\">0</param><param name=\"APPID\">\(APPID)</param><param name=\"USTY\">\(USTY)</param><param name=\"OS\">\(Operating_System)</param><param name=\"OSVERSION\">\(OSVERSION)</param><param name=\"MODEL\">\(iPHONE_MODEL)</param><param name=\"VENDOR\">\(VENDOR)</param><param name=\"NETWORK\">\(ispNetwork)</param><param name=\"CVNO\">\(CVNO)</param><param name=\"ACTION\">\(actionType)</param></params></msg>"
        
        //print(requestXML)
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
            },
                                onFailure: {(reason) ->() in
                                    onFailure(reason)
        })
    }
    
    func customerSignoutPostRequest(_ commandId:String, reqTime: String, DTID:String,
                          onSuccess: @escaping (_ data: Data) ->(),
                          onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param></params></msg>"
        //print(requestXML)
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
            },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
}
