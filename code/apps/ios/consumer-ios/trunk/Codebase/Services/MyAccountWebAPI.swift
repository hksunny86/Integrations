//
//  MyAccount.swift
//  Timepey
//
//  Created by Adnan Ahmed on 29/06/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation


class MyAccountWebAPI
{

    func fPInfoPostRequest(_ commandId: String, reqTime: String, DTID: String, MOBN: String,
                                  onSuccess: @escaping (_ data: Data) ->(),
                                  onFailure: @escaping (_ reason: String) ->())
    {
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"MOBN\">\(MOBN)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
        },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    func fPFinalPostRequest(_ commandId: String, reqTime: String, DTID: String, MOBN: String, NMPIN:String, CMPIN:String,
                           onSuccess: @escaping (_ data: Data) ->(),
                           onFailure: @escaping (_ reason: String) ->())
    {
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"MOBN\">\(MOBN)</param><param name=\"NMPIN\">\(NMPIN)</param><param name=\"CMPIN\">\(CMPIN)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
        },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    func forgotLoginPINRequest(_ commandId: String, reqTime: String, DTID: String, MOBN: String, CNIC:String,
                           onSuccess: @escaping (_ data: Data) ->(),
                           onFailure: @escaping (_ reason: String) ->())
    {
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"CMOB\">\(MOBN)</param><param name=\"CNIC\">\(CNIC)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
        },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    func forgotMPINRequest(_ commandId: String, reqTime: String, DTID: String, MOBN: String, CNIC:String,
                           onSuccess: @escaping (_ data: Data) ->(),
                           onFailure: @escaping (_ reason: String) ->())
    {
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"CMOB\">\(MOBN)</param><param name=\"CNIC\">\(CNIC)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
        },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    
    func regenrateMPINPostRequest(_ commandId: String, reqTime: String, DTID: String, MOBN: String,
                               onSuccess: @escaping (_ data: Data) ->(),
                               onFailure: @escaping (_ reason: String) ->())
    {
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"MOBN\">\(MOBN)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
        },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    
    
    func balanceInqueryPostRequest(_ commandId: String, reqTime: String, DTID: String, pin: String, ENCT: String, ACCTYPE: String, APID: String, BBACID: String, onSuccess: @escaping (_ data: Data) ->(), onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PIN\">\(pin)</param><param name=\"ENCT\">\(ENCT)</param><param name=\"ACCTYPE\">\(ACCTYPE)</param><param name=\"APID\">\(APID)</param><param name=\"BBACID\">\(BBACID)</param> <param name=\"APPID\">\(2)</param></params></msg>"
        
        
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
            },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    func balanceInqueryHRAPostRequest(_ commandId: String, reqTime: String, DTID: String, pin: String, ENCT: String, ACCTYPE: String, APID: String, BBACID: String, ISHRA:String,
                          onSuccess: @escaping (_ data: Data) ->(),
                          onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PIN\">\(pin)</param><param name=\"ENCT\">\(ENCT)</param><param name=\"ACCTYPE\"></param><param name=\"APPID\">\(2)</param> <param name=\"APID\">\(APID)</param><param name=\"BBACID\">\(BBACID)</param><param name=\"PAYMENT_MODE\">\(ISHRA)</param></params></msg>"
        
        
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
            },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    func changeMPINPostRequest(_ commandId: String, reqTime: String, DTID: String, pin: String, nPin: String, cPin: String, ENCT: String,
                                   onSuccess: @escaping (_ data: Data) ->(),
                                   onFailure: @escaping (_ reason: String) ->())
    {
       
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PIN\">\(pin)</param><param name=\"NPIN\">\(nPin)</param><param name=\"CPIN\">\(cPin)</param><param name=\"ENCT\">\(ENCT)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
            },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    func miniStatementPostRequest(_ commandId: String, reqTime: String, DTID: String, pin: String, ENCT: String, ACCTYPE: String, STNO: String, ETNO: String, APID: String, BBACID: String,
                                   onSuccess: @escaping (_ data: Data) ->(),
                                   onFailure: @escaping (_ reason: String) ->())
    {
       
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PIN\">\(pin)</param><param name=\"ENCT\">\(ENCT)</param><param name=\"ACCTYPE\">\(ACCTYPE)</param><param name=\"STNO\">\(STNO)</param><param name=\"ETNO\">\(ETNO)</param><param name=\"APID\">\(APID)</param><param name=\"BBACID\">\(BBACID)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
            },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    func miniStatementHRAPostRequest(_ commandId: String, reqTime: String, DTID: String, pin: String, ENCT: String, ACCTYPE: String, STNO: String, ETNO: String, APID: String, BBACID: String, ISHRA:String,
                                   onSuccess: @escaping (_ data: Data) ->(),
                                   onFailure: @escaping (_ reason: String) ->())
    {
       
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PIN\">\(pin)</param><param name=\"ENCT\">\(ENCT)</param><param name=\"ACCTYPE\">\(ACCTYPE)</param><param name=\"STNO\">\(STNO)</param><param name=\"ETNO\">\(ETNO)</param><param name=\"APID\">\(APID)</param><param name=\"BBACID\">\(BBACID)</param> <param name=\"PAYMENT_MODE\">\(1)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
            },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    func verifyPinPostRequest(_ commandId: String, CMDID: String, reqTime: String, DTID: String, pin: String, ENCT: String, PIN_RETRY_COUNT: String, MOBN:String, CNIC: String, ACTION: String,
                                   onSuccess: @escaping (_ data: Data) ->(),
                                   onFailure: @escaping (_ reason: String) ->())
    {
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"CMDID\">\(CMDID)</param><param name=\"DTID\">\(DTID)</param><param name=\"MOBN\">\(MOBN)</param><param name=\"CNIC\">\(CNIC)</param><param name=\"ACTION\">\(ACTION)</param><param name=\"PIN\">\(pin)</param><param name=\"ENCT\">\(ENCT)</param><param name=\"PIN_RETRY_COUNT\">\(PIN_RETRY_COUNT)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
            },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    func verifyOTPPinPostRequest(_ commandId: String,  reqTime: String, DTID: String, pin: String, UDID: String, ENCT: String, UID: String, USTY: String, actionType: String, CVNO: String, APPV: String, OS: String,APPID: String,
                              onSuccess: @escaping (_ data: Data) ->(),
                              onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PIN\">\(pin)</param><param name=\"UDID\">\(UDID)</param><param name=\"ENCT\">\(ENCT)</param><param name=\"UID\">\(UID)</param><param name=\"USTY\">\(USTY)</param><param name=\"ACTION\">\(actionType)</param><param name=\"CVNO\">\(CVNO)</param><param name=\"APPV\">\(APPV)</param><param name=\"APPID\">\(APPID)</param><param name=\"OS\">\(OS)</param></params></msg>"
        //print(requestXML)
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
            },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    func regenerateOTPPostRequest(_ commandId: String, reqTime: String, DTID: String, MOBN: String,
                               onSuccess: @escaping (_ data: Data) ->(),
                               onFailure: @escaping (_ reason: String) ->())
    {
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"MOBN\">\(MOBN)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
        },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    
    func setMPINPostRequest(_ commandId: String, reqTime: String, DTID: String, pin: String, ENCT: String,
                               onSuccess: @escaping (_ data: Data) ->(),
                               onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param> <param name=\"NPIN\">\(pin)</param> <param name=\"CPIN\">\(pin)</param> <param name=\"ENCT\">\(ENCT)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
            },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    
    func selfRegVerficationPostRequest(_ commandId: String, reqTime: String, DTID: String, MOBN: String, CNIC: String, IS_UPGRADE: String, onSuccess: @escaping (_ data: Data) ->(), onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"MOBN\">\(MOBN)</param><param name=\"CNIC\">\(CNIC)</param>  <param name=\"IS_UPGRADE\">\(IS_UPGRADE)</param></params></msg>"
        print(requestXML)
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
            },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
        
    }
    
    
    func selfRegFinalPostRequest(_ commandId: String, reqTime: String, DTID: String, CNIC: String, CMOB: String, CNIC_ISSUE_DATE: String, CUST_ACC_TYPE: String, IS_NEW_ACCOUNT: String, EMAIL: String, CUST_MOB_NETWORK:String,
                                       onSuccess: @escaping (_ data: Data) ->(),
                                       onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"DATETIME\">\(reqTime)</param><param name=\"CNIC\">\(CNIC)</param><param name=\"CMOB\">\(CMOB)</param><param name=\"CNIC_ISSUE_DATE\">\(CNIC_ISSUE_DATE)</param><param name=\"CUST_ACC_TYPE\">1</param><param name=\"IS_NEW_ACCOUNT\">\(IS_NEW_ACCOUNT)</param><param name=\"EMAIL_ADDRESS\">\(EMAIL)</param><param name=\"CUST_MOB_NETWORK\">\(CUST_MOB_NETWORK)</param></params></msg>"

        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
            },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
       
    }
    
    func faqPostRequest(_ commandId: String, reqTime: String, DTID: String, FVNO: String,
                                 onSuccess: @escaping (_ data: Data) ->(),
                                 onFailure: @escaping (_ reason: String) ->())
    {
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"FVNO\">\(FVNO)</param><param name=\"DTID\">\(DTID)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
            },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    func myLimitsRequest(_ commandId: String, reqTime: String, DTID: String, ENCT: String,
                        onSuccess: @escaping (_ data: Data) ->(),
                        onFailure: @escaping (_ reason: String) ->())
    {
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"ENCT\">\(ENCT)</param><param name=\"DTID\">\(DTID)</param></params></msg>"
        

        
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
            },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    func transferInfoRequest(_ commandId: String, reqTime: String, DTID: String, PID: String, TXAM: String,
                         onSuccess: @escaping (_ data: Data) ->(),
                         onFailure: @escaping (_ reason: String) ->())
    {
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"PID\">\(PID)</param><param name=\"DTID\">\(DTID)</param><param name=\"TXAM\">\(TXAM)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
            },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    
    
    func transferCheckoutRequest(_ commandId: String, reqTime: String, DTID: String, PID: String, BBACID: String, COREACID: String, COREACTL: String, BAID: String, TXAM: String, CAMT: String, TPAM: String, TAMT: String,
                                   onSuccess: @escaping (_ data: Data) ->(),
                                   onFailure: @escaping (_ reason: String) ->())
    {
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"PID\">\(PID)</param><param name=\"DTID\">\(DTID)</param><param name=\"BBACID\">\(BBACID)</param><param name=\"COREACID\">\(COREACID)</param><param name=\"COREACTL\">\(COREACTL)</param><param name=\"BAID\">\(BAID)</param><param name=\"TXAM\">\(TXAM)</param><param name=\"CAMT\">\(CAMT)</param><param name=\"TPAM\">\(TPAM)</param><param name=\"TAMT\">\(TAMT)</param></params></msg>"
        
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
            },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    func debitCardIssuanceInfo(_ commandId: String, reqTime: String, DTID: String, APPID: String, onSuccess: @escaping (_ data: Data) -> (), onFailure: @escaping (_ reason: String) ->()) {
    let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param> <param name=\"APPID\">\(APPID)</param> </params></msg>"

        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
            },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    func debitCardIssuanceRequest(_ commandId: String, reqTime: String, DTID: String, APPID: String, cardDescription: String, mailingAddress: String, onSuccess: @escaping (_ data: Data) -> (), onFailure: @escaping (_ reason: String) ->()) {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param> <param name=\"APPID\">\(APPID)</param> <param name=\"CARD_DESCRIPTION\">\(cardDescription)</param> <param name=\"MAILING_ADDRESS\">\(mailingAddress)</param> <param name=\"CNIC\">\(Customer.sharedInstance.cnic!)</param> <param name=\"CMOB\">\(Customer.sharedInstance.cMob!)</param> </params></msg>"
        
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
            },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    
    func HRAAccountOpeningInfo(_ commandId: String, reqTime: String, DTID: String, APPID: String, CNIC: String, onSuccess: @escaping (_ data: Data) -> (), onFailure: @escaping (_ reason: String) ->()) {
        
    let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"CNIC\">\(CNIC)</param> <param name=\"APPID\">\(APPID)</param> </params></msg>"

        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
            },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    func HRAAccountOpeningRequest(_ commandId: String, reqTime: String, DTID: String, ENCT: String, CMOB: String, PIN:String, CNIC: String, CNAME: String, CDOB:String, FATHER_HUSBND_NAME:String, AMOB:String, OCCUPATION:String, ORG_LOC1:String,  ORG_LOC2:String, ORG_LOC3:String, ORG_LOC4:String, ORG_LOC5:String, ORG_REL1:String, ORG_REL2:String, ORG_REL3:String, ORG_REL4:String, ORG_REL5:String, TRX_PUR:String, SOI:String, KIN_NAME:String, KIN_MOB_NO:String, KIN_CNIC:String, KIN_RELATIONSHIP:String, onSuccess: @escaping (_ data: Data) -> (), onFailure: @escaping (_ reason: String) ->()) {
        
    let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param> <param name=\"ENCT\">\(ENCT)</param> <param name=\"PIN\">\(PIN)</param> <param name=\"CNIC\">\(CNIC)</param> <param name=\"CMOB\">\(CMOB)</param> <param name=\"CNAME\">\(CNAME)</param> <param name=\"CDOB\">\(CDOB)</param> <param name=\"FATHER_HUSBND_NAME\">\(FATHER_HUSBND_NAME)</param> <param name=\"AMOB\">\(AMOB)</param> <param name=\"OCCUPATION\">\(OCCUPATION)</param> <param name=\"ORG_LOC1\">\(ORG_LOC1)</param> <param name=\"ORG_LOC2\">\(ORG_LOC2)</param> <param name=\"ORG_LOC3\">\(ORG_LOC3)</param><param name=\"ORG_LOC4\">\(ORG_LOC4)</param> <param name=\"ORG_LOC5\">\(ORG_LOC5)</param> <param name=\"ORG_REL1\">\(ORG_REL1)</param> <param name=\"ORG_REL2\">\(ORG_REL2)</param> <param name=\"ORG_REL3\">\(ORG_REL3)</param> <param name=\"ORG_REL4\">\(ORG_REL4)</param> <param name=\"ORG_REL5\">\(ORG_REL5)</param> <param name=\"TRX_PUR\">\(TRX_PUR)</param> <param name=\"SOI\">\(SOI)</param> <param name=\"KIN_NAME\">\(KIN_NAME)</param> <param name=\"KIN_MOB_NO\">\(KIN_MOB_NO)</param> <param name=\"KIN_CNIC\">\(KIN_CNIC)</param> <param name=\"KIN_RELATIONSHIP\">\(KIN_RELATIONSHIP)</param> </params></msg>"

        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
            },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    
    func locatorPostRequest(_ commandId: String, reqTime: String, DTID: String, CITY: String, RADIUS: String, LATITUDE: String, LONGITUDE: String, PAGENO: String, PAGESIZE: String, TYPE: String,
                        onSuccess: @escaping (_ data: Data) ->(),
                        onFailure: @escaping (_ reason: String) ->())
    {
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"CITY\">\(CITY)</param><param name=\"RADIUS\">\(RADIUS)</param><param name=\"LATITUDE\">\(LATITUDE)</param><param name=\"LONGITUDE\">\(LONGITUDE)</param><param name=\"PAGE_NO\">\(PAGENO)</param><param name=\"PAGE_SIZE\">\(PAGESIZE)</param><param name=\"TYPE\">\(TYPE)</param></params></msg>"
        
        //print(requestXML)
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
        },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }
    
    func setMPINLaterPostRequest(_ commandId: String, reqTime: String, DTID: String, MOBN: String, CNIC: String, isMpinSetLater: String,
                                  onSuccess: @escaping (_ data: Data) ->(),
                                  onFailure: @escaping (_ reason: String) ->())
    {
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"CMOB\">\(MOBN)</param><param name=\"CNIC\">\(CNIC)</param><param name=\"IS_SET_MPIN_LATER\">\(isMpinSetLater)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
        },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
        })
    }

}
