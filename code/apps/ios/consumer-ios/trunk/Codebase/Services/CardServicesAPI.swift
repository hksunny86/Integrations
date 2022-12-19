//
//  CardServicesAPI.swift
//  Timepey
//
//  Created by Adnan Ahmed on 05/01/2017.
//  Copyright © 2017 Inov8. All rights reserved.
//

import UIKit
import Foundation


class CardServicesAPI {
    
    func cardActivationInfoPostRequest(_ commandId: String, reqTime: String, DTID: String, CMOB: String, OCA: String,
                                       onSuccess: @escaping (_ data: Data) ->(),
                                       onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"CMOB\">\(CMOB)</param><param name=\"OCA\">\(OCA)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
        },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    func cardActivationFinalPostRequest(_ commandId: String, reqTime: String, DTID: String, ENCT: String, CMOB: String,TRXID: String, OCA: String,
                                       onSuccess: @escaping (_ data: Data) ->(),
                                       onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"ENCT\">\(ENCT)</param><param name=\"CMOB\">\(CMOB)</param><param name=\"TRXID\">\(TRXID)</param><param name=\"OCA\">\(OCA)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
        },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }

    func cardBlockInfoPostRequest(_ commandId: String, reqTime: String, DTID: String,
                                       onSuccess: @escaping (_ data: Data) ->(),
                                       onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
        },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    func cardBlockFinalPostRequest(_ commandId: String, reqTime: String, DTID: String,
                                  onSuccess: @escaping (_ data: Data) ->(),
                                  onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
        },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    func genrateATMPINInfoPostRequest(_ commandId: String, reqTime: String, aPING: String, DTID: String,
                                  onSuccess: @escaping (_ data: Data) ->(),
                                  onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"APING\">\(aPING)</param><param name=\"DTID\">\(DTID)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
        },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    //    <msg id="158" reqTime="1349262768465">
    //    <params>
    //    <param name="DTID">5</param>
    //    <param name="NPIN">bDWFak4S85Ogrx2ml2ChCg==</param>
    //    <param name="CPIN">bDWFak4S85Ogrx2ml2ChCg==</param>
    //    <param name="ENCT">1</param>
    //    </params>
    //    </msg>

    
    func genrateATMPINFinalPostRequest(_ commandId: String, reqTime: String, DTID: String, nPin: String, cPin: String, aPING: String, ENCT: String,
                               onSuccess: @escaping (_ data: Data) ->(),
                               onFailure: @escaping (_ reason: String) ->())
    {
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"NPIN\">\(nPin)</param><param name=\"CPIN\">\(cPin)</param><param name=\"APING\">\(aPING)</param><param name=\"ENCT\">\(ENCT)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
        },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
}

