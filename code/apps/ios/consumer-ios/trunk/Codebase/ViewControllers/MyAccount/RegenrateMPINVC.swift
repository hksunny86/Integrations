//
//  RegenrateMPINVC.swift
//  JSBL-BB
//
//  Created by Adnan Ahmed on 26/03/2018.
//  Copyright © 2018 Inov8. All rights reserved.
//

import Foundation
import UIKit

class RegenrateMPINVC: BaseViewController {

    var response = (XMLError(), XMLMessage(), [String:String]())
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: false)
        regenrateMPINPostRequest()
    }
    
    
    
    func regenrateMPINPostRequest() {
        self.showLoadingView()
        
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                xmlPath = Bundle.main.path(forResource: "Command-6-Balance Inquiry", ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                
                else { return }
            
            //let newStr = String(data: data, encoding: NSUTF8StringEncoding)
            //print(newStr)
            response = XMLParser.balanceEnquiryXMLParsing(data)
            
            if(self.response.0.msg != nil){
                if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                }else{
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                }
            }else if(response.1.msg != nil){
                
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE_IMPCR_SUCCESS, msgLabelText: response.1.msg!, actionType: "", isCancelBtnHidden: true)
                
            }
            
            self.hideLoadingView()
        }else{
            
            
            //let encryptedPin = try! inputText.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let myAccApi : MyAccountWebAPI = MyAccountWebAPI()
            
            myAccApi.regenrateMPINPostRequest(
                Constants.CommandId.REGENRATE_MPIN,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                MOBN:(Customer.sharedInstance.cMob)!,
                
                onSuccess:{(data) -> () in
                    //print(data)
                    self.response = XMLParser.balanceEnquiryXMLParsing(data)
                    
                    if(self.response.0.msg != nil){
                        if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                        }else{
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                        }
                    }else if(self.response.1.msg != nil){
                        
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "", isCancelBtnHidden: true)
                        
                    }
                    
                    self.hideLoadingView()
            },
                onFailure: {(reason) ->() in
                    //print("Failure")
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                    self.hideLoadingView()
                    
            })
            
        }
        
    }
    
}


