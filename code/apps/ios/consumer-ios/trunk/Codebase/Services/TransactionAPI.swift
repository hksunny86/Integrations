//
//  MoneyTransferWebAPI.swift
//  Timepey
//
//  Created by Adnan Ahmed on 15/07/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation


class TransactionAPI {
    
    
    
    
    func FetchBanksList(_ commandId: String, reqTime: String, DTID: String, onSuccess: @escaping (_ data: Data) ->(),
                        onFailure: @escaping (_ reason: String) ->()) {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"IS_IBFT\">1</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                onSuccess(data)
                              },
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
                              })
    }
    
    func FetchPaymentPurposeList(_ commandId: String, reqTime: String, DTID: String, onSuccess: @escaping (_ data: Data) ->(),
                                 onFailure: @escaping (_ reason: String) ->()) {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                onSuccess(data)
                              },
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
                              })
    }
    
    func AccToCashInfoPostRequest(_ commandId: String, reqTime: String, DTID: String, PID: String, RWMOB: String, CMOB: String, RWCNIC: String, TXAM: String, TRX_PUR: String,
                                  onSuccess: @escaping (_ data: Data) ->(),
                                  onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PID\">\(PID)</param><param name=\"RCMOB\">\(RWMOB)</param><param name=\"CMOB\">\(CMOB)</param><param name=\"RWCNIC\">\(RWCNIC)</param><param name=\"TXAM\">\(TXAM)</param><param name=\"TRX_PUR\">\(TRX_PUR)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
                              },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
                              })
    }
    
    
    
    func AccToCashCheckoutPostRequest(_ commandId: String, reqTime: String, DTID: String, PID: String, PIN: String, ENCT: String,RWMOB: String, CMOB: String, RWCNIC: String, TXAM: String, CAMT: String, TPAM: String,  TAMT: String,
                                      onSuccess: @escaping (_ data: Data) ->(),
                                      onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PID\">\(PID)</param><param name=\"PIN\">\(PIN)</param><param name=\"ENCT\">\(ENCT)</param><param name=\"RCMOB\">\(RWMOB)</param><param name=\"CMOB\">\(CMOB)</param><param name=\"RWCNIC\">\(RWCNIC)</param><param name=\"TXAM\">\(TXAM)</param><param name=\"CAMT\">\(CAMT)</param><param name=\"TPAM\">\(TPAM)</param><param name=\"TAMT\">\(TAMT)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
                              },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
                              })
    }
    
    func AccToAccInfoPostRequest(_ commandId: String, reqTime: String, DTID: String, PID: String, RCMOB: String, CMOB: String, TXAM: String,
                                 onSuccess: @escaping (_ data: Data) ->(),
                                 onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PID\">\(PID)</param><param name=\"RCMOB\">\(RCMOB)</param><param name=\"CMOB\">\(CMOB)</param><param name=\"TXAM\">\(TXAM)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
                              },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
                              })
    }
    
    func AccToAccCheckoutPostRequest(_ commandId: String, reqTime: String, DTID: String, PID: String, PIN: String, ENCT: String,RCMOB: String, CMOB: String, TXAM: String, CAMT: String, TPAM: String,  TAMT: String,
                                     onSuccess: @escaping (_ data: Data) ->(),
                                     onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PID\">\(PID)</param><param name=\"PIN\">\(PIN)</param><param name=\"ENCT\">\(ENCT)</param><param name=\"RCMOB\">\(RCMOB)</param><param name=\"CMOB\">\(CMOB)</param><param name=\"TXAM\">\(TXAM)</param><param name=\"CAMT\">\(CAMT)</param><param name=\"TPAM\">\(TPAM)</param><param name=\"TAMT\">\(TAMT)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
                              },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
                              })
    }
    
    func retailPaymentInfoPostRequest(_ commandId: String, reqTime: String, DTID: String, MRID: String, TAMT: String, QRString: String,
                                      onSuccess: @escaping (_ data: Data) ->(),
                                      onFailure: @escaping (_ reason: String) ->())
    {
        var mrID = String()
        if(MRID == ""){
            mrID = "NULL"
        }else{
            mrID = MRID
        }
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"MRID\">\(mrID)</param><param name=\"TAMT\">\(TAMT)</param><param name=\"QR_STRING\">\(QRString)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
                              },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
                              })
    }
    
    
    func retailPaymentCheckoutPostRequest(_ commandId: String, reqTime: String, DTID: String, MRID: String, TAMT: String, MNAME: String, QRString: String,
                                          onSuccess: @escaping (_ data: Data) ->(),
                                          onFailure: @escaping (_ reason: String) ->())
    {
        var mrID = String()
        if(MRID == ""){
            mrID = "NULL"
        }else{
            mrID = MRID
        }
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"MRID\">\(mrID)</param><param name=\"TAMT\">\(TAMT)</param><param name=\"MNAME\">\(MNAME)</param><param name=\"QR_STRING\">\(QRString)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
                              },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
                              })
    }
    
    func hraToWalletInfoPostRequest(_ commandId: String, reqTime: String, DTID: String, PID: String, TXAM: String,
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
    
    func hraToWalletCheckOutPostRequest(_ commandId: String, reqTime: String, DTID: String, PID: String, TXAM: String, TAMT:String, CAMT:String, TPAM:String,
                                        onSuccess: @escaping (_ data: Data) ->(),
                                        onFailure: @escaping (_ reason: String) ->())
    {
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PID\">\(PID)</param><param name=\"TXAM\">\(TXAM)</param> <param name=\"TAMT\">\(TAMT)</param><param name=\"CAMT\">\(CAMT)</param> <param name=\"TPAM\">\(TPAM)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
                              },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
                              })
    }
    
    
    
    
    func BBToCoreInfoPostRequest(_ commandId: String, reqTime: String, DTID: String, PID: String, RCMOB: String, CMOB: String, COREACID: String, TXAM: String,
                                 onSuccess: @escaping (_ data: Data) ->(),
                                 onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PID\">\(PID)</param><param name=\"RCMOB\">\(RCMOB)</param><param name=\"CMOB\">\(CMOB)</param><param name=\"COREACID\">\(COREACID)</param><param name=\"TXAM\">\(TXAM)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
                              },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
                              })
    }
    
    func BBToCoreCheckoutPostRequest(_ commandId: String, reqTime: String, DTID: String, PID: String, PIN: String, ENCT: String, RCMOB: String, CMOB: String, COREACID: String, COREACTL: String, TXAM: String, CAMT: String, TPAM: String,  TAMT: String,
                                     onSuccess: @escaping (_ data: Data) ->(),
                                     onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PID\">\(PID)</param><param name=\"PIN\">\(PIN)</param><param name=\"ENCT\">\(ENCT)</param><param name=\"RCMOB\">\(RCMOB)</param><param name=\"CMOB\">\(CMOB)</param><param name=\"COREACID\">\(COREACID)</param><param name=\"COREACTL\">\(COREACTL)</param><param name=\"TXAM\">\(TXAM)</param><param name=\"CAMT\">\(CAMT)</param><param name=\"TPAM\">\(TPAM)</param><param name=\"TAMT\">\(TAMT)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
                              },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
                              })
    }
    
    func BBToIBFTInfoPostRequest(_ commandId: String, reqTime: String, DTID: String, PID: String, RCMOB: String,  COREACID: String, TXAM: String, CMOB: String, BAIMD: String, PMTTYPE: String, BENE_BANK_NAME: String, TRANS_PURPOSE_CODE: String,
                                 onSuccess: @escaping (_ data: Data) ->(),
                                 onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PID\">\(PID)</param><param name=\"RCMOB\">\(RCMOB)</param><param name=\"COREACID\">\(COREACID)</param> <param name=\"BAIMD\">\(BAIMD)</param> <param name=\"PMTTYPE\">\(PMTTYPE)</param><param name=\"BENE_BANK_NAME\">\(BENE_BANK_NAME)</param> <param name=\"TRANS_PURPOSE_CODE\">\(TRANS_PURPOSE_CODE)</param> <param name=\"TXAM\">\(TXAM)</param><param name=\"CMOB\">\(CMOB)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
                              },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
                              })
    }
    
    func BBToIBFTCheckoutPostRequest(_ commandId: String, reqTime: String, DTID: String, PID: String, PIN: String, ENCT: String, CMOB: String, RCMOB: String, COREACID: String, COREACTL: String, BAIMD: String, PMTTYPE: String, TXAM: String, TXAMF: String, CAMT: String, CAMTF: String, TPAMF: String, TPAM: String, TAMT: String, BENEFICIARYBANK: String, BENEFICIARYBRANCH: String, BENEFICIARYIBAN: String, CRDR: String, COREACTITLE: String, TRANSACTION_PURPOSE_CODE: String,
                                     onSuccess: @escaping (_ data: Data) ->(),
                                     onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PID\">\(PID)</param><param name=\"PIN\">\(PIN)</param><param name=\"ENCT\">\(ENCT)</param><param name=\"CMOB\">\(CMOB)</param> <param name=\"RCMOB\">\(RCMOB)</param><param name=\"COREACID\">\(COREACID)</param><param name=\"COREACTL\">\(COREACTL)</param><param name=\"BAIMD\">\(BAIMD)</param><param name=\"PMTTYPE\">\(PMTTYPE)</param><param name=\"TXAM\">\(TXAM)</param> <param name=\"TXAMF\">\(TXAMF)</param> <param name=\"CAMT\">\(CAMT)</param> <param name=\"CAMTF\">\(CAMTF)</param> <param name=\"TPAM\">\(TPAM)</param> <param name=\"TPAMF\">\(TPAMF)</param> <param name=\"TAMT\">\(TAMT)</param> <param name=\"TAMTF\">\(TAMT)</param> <param name=\"BENE_BANK_NAME\"/> \(BENEFICIARYBANK)<param name=\"BENE_BRANCH_NAME\"/>\(BENEFICIARYBRANCH)<param name=\"BENE_IBAN\">\(BENEFICIARYIBAN)</param><param name=\"CR_DR\"/>\(CRDR)<param name=\"COREACTITLE\">\(COREACTITLE)</param><param name=\"TRANS_PURPOSE_CODE\">\(TRANSACTION_PURPOSE_CODE)</param> </params> </msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
                              },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
                              })
    }
    
    
    func BillPaymentInfoPostRequest(_ commandId: String, reqTime: String, DTID: String, PID: String, AMOB: String, CMOB: String, BAMT: String, CSCD: String, PMTTYPE: String, BAID: String,
                                    onSuccess: @escaping (_ data: Data) ->(),
                                    onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PID\">\(PID)</param><param name=\"AMOB\">\(AMOB)</param><param name=\"CMOB\">\(CMOB)</param><param name=\"BAMT\">\(BAMT)</param><param name=\"CSCD\">\(CSCD)</param><param name=\"PMTTYPE\">\(PMTTYPE)</param><param name=\"BAID\">\(BAID)</param></params></msg>"
        //print(requestXML)
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
                              },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
                              })
    }
    
    func BillPaymentCheckoutPostRequest(_ commandId: String, reqTime: String, DTID: String, PID: String, PIN: String, ENCT: String,CMOB: String, AMOB: String, CSCD: String, PMTTYPE: String, BAID: String, BAMT: String,
                                        onSuccess: @escaping (_ data: Data) ->(),
                                        onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PID\">\(PID)</param><param name=\"PIN\">\(PIN)</param><param name=\"ENCT\">\(ENCT)</param><param name=\"CMOB\">\(CMOB)</param><param name=\"AMOB\">\(AMOB)</param><param name=\"CSCD\">\(CSCD)</param><param name=\"PMTTYPE\">\(PMTTYPE)</param><param name=\"BAID\">\(BAID)</param><param name=\"BAMT\">\(BAMT)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
                              },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
                              })
    }
    
    func BalanceTopupInfoPostRequest(_ commandId: String, reqTime: String, DTID: String, PID: String, TMOB: String, TXAM: String,
                                     onSuccess: @escaping (_ data: Data) ->(),
                                     onFailure: @escaping (_ reason: String) ->())
    {
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PID\">\(PID)</param><param name=\"TMOB\">\(TMOB)</param><param name=\"TXAM\">\(TXAM)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
                              },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
                              })
    }
    
    func BalanceTopupCheckoutPostRequest(_ commandId: String, reqTime: String, DTID: String, PID: String, PIN: String, ENCT: String, TXAM: String, TMOB: String, CAMT: String, TPAM: String, TAMT: String,
                                         onSuccess: @escaping (_ data: Data) ->(),
                                         onFailure: @escaping (_ reason: String) ->())
    {
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PID\">\(PID)</param><param name=\"PIN\">\(PIN)</param><param name=\"ENCT\">\(ENCT)</param><param name=\"TXAM\">\(TXAM)</param><param name=\"TMOB\">\(TMOB)</param><param name=\"CAMT\">\(CAMT)</param><param name=\"TPAM\">\(TPAM)</param><param name=\"TAMT\">\(TAMT)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
                              },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
                              })
    }
    
    func CollectionPaymentInfoPostRequest(_ commandId: String, reqTime: String, DTID: String, PID: String, CNSMRNO: String, TXAM: String,
                                          onSuccess: @escaping (_ data: Data) ->(),
                                          onFailure: @escaping (_ reason: String) ->())
    {
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PID\">\(PID)</param><param name=\"CNSMRNO\">\(CNSMRNO)</param><param name=\"TXAM\">\(TXAM)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
                              },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
                              })
    }
    func  ChallanPaymentInfoPostRequest(_ commandId: String, reqTime: String, DTID: String, PID: String, CSCD: String, CMOB: String,
                                        onSuccess: @escaping (_ data: Data) ->(),
                                        onFailure: @escaping (_ reason: String) ->())
    {
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PID\">\(PID)</param><param name=\"CSCD\">\(CSCD)</param><param name=\"CMOB\">\(CMOB)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
                              },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
                              })
    }
    
    func CollectionPaymentCheckoutPostRequest(_ commandId: String, reqTime: String, DTID: String, PID: String, CNSMRNO: String, TXAM: String, TPAM: String, TAMT: String,
                                              onSuccess: @escaping (_ data: Data) ->(),
                                              onFailure: @escaping (_ reason: String) ->())
    {
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PID\">\(PID)</param><param name=\"CNSMRNO\">\(CNSMRNO)</param><param name=\"TXAM\">\(TXAM)</param><param name=\"TPAM\">\(TPAM)</param><param name=\"TAMT\">\(TAMT)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                onSuccess(data)
                              },
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
                              })
    }
    func ChallanPaymentCheckoutPostRequest(_ commandId: String, reqTime: String, DTID: String, PIN: String,PID: String, ENCT: String, CMOB: String, CSCD: String, BAMT: String, TPAM: String, TAMT: String,
                                           onSuccess: @escaping (_ data: Data) ->(),
                                           onFailure: @escaping (_ reason: String) ->())
    {
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"PIN\">\(PIN)</param><param name=\"PID\">\(PID)</param><param name=\"ENCT\">\(ENCT)</param><param name=\"CMOB\">\(CMOB)</param><param name=\"CSCD\">\(CSCD)</param><param name=\"BAMT\">\(BAMT)</param><param name=\"TPAM\">\(TPAM)</param><param name=\"TAMT\">\(TAMT)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
                              },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
                              })
    }
    
    func LoanPaymentsInfoRequest (_ commandId: String, reqTime: String, DTID: String, appId: String,PID: String, CMOB: String, TXAM:String, CNIC: String, onSuccess: @escaping (_ data: Data) ->(),
                                  onFailure: @escaping (_ reason: String) ->()) {
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"APPID\">\(appId)</param><param name=\"PID\">\(PID)</param><param name=\"CNIC\">\(CNIC)</param><param name=\"CMOB\">\(CMOB)</param><param name=\"TXAM\">\(TXAM)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
                              },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
                              })
        
    }
    
    
    func BookMeInfoRequest (_ commandId: String, reqTime: String, DTID: String, PID: String, CMOB: String, BAMT:String, PMTTYPE: String, ORDER_ID: String, STYPE: String, SPNAME: String, BNAME: String, BCNIC: String, BMOB: String, BEMAIL:String, onSuccess: @escaping (_ data: Data) ->(),
                            onFailure: @escaping (_ reason: String) ->()) {
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"BNAME\">\(BNAME)</param><param name=\"BCNIC\">\(BCNIC)</param><param name=\"BEMAIL\">\(BEMAIL)</param><param name=\"BMOB\">\(BMOB)</param><param name=\"BAMT\">\(BAMT)</param><param name=\"PID\">\(PID)</param><param name=\"PMTTYPE\">\(PMTTYPE)</param><param name=\"CMOB\">\(CMOB)</param><param name=\"ORDERID\">\(ORDER_ID)</param><param name=\"STYPE\">\(STYPE)</param><param name=\"SPNAME\">\(SPNAME)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
                              },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
                              })
        
    }
    
    func BookMeCheckOutRequest (_ commandId: String, reqTime: String, DTID: String, BNAME: String, BCNIC: String, BMOB: String, BEMAIL:String, PID: String, CMOB: String, BAMT:String, PMTTYPE: String, ORDER_ID: String, STYPE: String, SPNAME: String, TPAM: String , TAMT: String, CAMT: String, BFARE: String, TAPAMT: String, DAMT:String, TAX: String, FEE:String, onSuccess: @escaping (_ data: Data) ->(),
                                onFailure: @escaping (_ reason: String) ->()) {
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param><param name=\"BNAME\">\(BNAME)</param><param name=\"BCNIC\">\(BCNIC)</param><param name=\"BEMAIL\">\(BEMAIL)</param><param name=\"BMOB\">\(BMOB)</param><param name=\"BAMT\">\(BAMT)</param><param name=\"PID\">\(PID)</param><param name=\"PMTTYPE\">\(PMTTYPE)</param><param name=\"CMOB\">\(CMOB)</param><param name=\"ORDERID\">\(ORDER_ID)</param><param name=\"STYPE\">\(STYPE)</param><param name=\"SPNAME\">\(SPNAME)</param><param name=\"TPAM\">\(TPAM)</param><param name=\"TAMT\">\(TAMT)</param><param name=\"CAMT\">\(CAMT)</param><param name=\"BFARE\">\(BFARE)</param><param name=\"TAPAMT\">\(TAPAMT)</param><param name=\"DAMT\">\(DAMT)</param><param name=\"TAX\">\(TAX)</param><param name=\"FEE\">\(FEE)</param></params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
                              },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
                              })
        
    }
    
    
    func LoanPaymentsCheckOutRequest (_ commandId: String, reqTime: String, DTID: String, PID: String, PIN:String, ENCT: String, CMOB: String, CNIC: String, TXAM:String, CAMT: String, TPAM: String, TAMT: String, RRN: String, onSuccess: @escaping (_ data: Data) ->(),
                                      onFailure: @escaping (_ reason: String) ->()) {
        
        let requestXML = "<msg id=\"\(commandId)\" reqTime=\"\(reqTime)\"><params><param name=\"DTID\">\(DTID)</param> <param name=\"PID\">\(PID)</param> <param name=\"PIN\">\(PIN)</param> <param name=\"ENCT\">\(ENCT)</param><param name=\"CMOB\">\(CMOB)</param><param name=\"CNIC\">\(CNIC)</param><param name=\"TXAM\">\(TXAM)</param> <param name=\"CAMT\">\(CAMT)</param> <param name=\"TPAM\">\(TPAM)</param> <param name=\"TAMT\">\(TAMT)</param><param name=\"THIRD_PARTY_RRN\">\(RRN)</param> </params></msg>"
        
        APIManager.urlRequest(requestXML: requestXML,
                              onSuccess:{(data) -> () in
                                
                                onSuccess(data)
                              },
                              
                              
                              onFailure: {(reason) ->() in
                                onFailure(reason)
                              })
        
    }
    
}
