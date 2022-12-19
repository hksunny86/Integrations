//
//  CashWithdrawalWebAPI.swift
//  Timepey
//
//  Created by Adnan Ahmed on 15/07/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation


class CashWithdrawalWebAPI {
    
    
    func cashWithdrawalInfoPostRequest(_ commandId: String, reqTime: String, DTID: String, PID: String, TXAM: String,
                                       onSuccess: @escaping (_ data: Data) ->(),
                                       onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PID\">\(PID)</param><param name=\"TXAM\">\(TXAM)</param></params></msg>"
     
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
            },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    
    func cashWithdrawalCheckoutPostRequest(_ commandId: String, reqTime: String, DTID: String, PID: String, PIN: String, ENCT: String, TXAM: String, TPAM: String, CAMT: String, TAMT: String,
                                           onSuccess: @escaping (_ data: Data) ->(),
                                           onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PID\">\(PID)</param><param name=\"PIN\">\(PIN)</param><param name=\"ENCT\">\(ENCT)</param><param name=\"TXAM\">\(TXAM)</param><param name=\"TPAM\">\(TPAM)</param><param name=\"CAMT\">\(CAMT)</param><param name=\"TAMT\">\(TAMT)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
            },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    func cashWithdrawalLeg1InfoPostRequest(_ commandId: String, reqTime: String, DTID: String, PID: String, PIN: String, ENCT: String, MANUAL_OTPIN: String,
                                       onSuccess: @escaping (_ data: Data) ->(),
                                       onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PID\">\(PID)</param><param name=\"PIN\">\(PIN)</param><param name=\"ENCT\">\(ENCT)</param><param name=\"MANUAL_OTPIN\">\(MANUAL_OTPIN)</param></params></msg>"
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
            },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
}
