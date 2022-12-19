//
//  BalanceInquiry.swift
//  Timepey
//
//  Created by Adnan Ahmed on 29/06/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import UIKit
import Foundation

class BalanceInquiry: BaseViewController {
    
    @IBOutlet weak var balanceInquiry: UILabel!
    @IBOutlet weak var accountType: UILabel!
    @IBOutlet weak var dateTime: UILabel!
    @IBOutlet weak var okButton: UIButton!
    @IBOutlet weak var currentAccBalLabel: UILabel!
    
    let userDefault = UserDefaults.standard
    var response = (XMLError(), XMLMessage(), [String:String]())
    var accType: String?
    var pinText: String?
    var productID: String?
    var isHRA = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: false, isSignoutButtonHidden: false)
        
        hideSubViews()
        
        okButton.layer.cornerRadius = 2

        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        if(productID == "10025"){
            balanceInquiryRequest(accType: "1")
            accountType.text = "BB Account"
        }
        else if productID == "10026" {
            balanceInquiryHRA(accType: "1")
            accountType.text = "HRA Account"
            isHRA = "1"
        }
        else{
            balanceInquiryRequest(accType: "0")
            accountType.text = "Core Account"
        }
        
    }
    
    
    func hideSubViews(){
        balanceInquiry.isHidden = true
        accountType.isHidden = true
        dateTime.isHidden = true
        okButton.isHidden = true
        currentAccBalLabel.isHidden = true
    }
    
    func showSubView(){
        balanceInquiry.isHidden = false
        accountType.isHidden = false
        dateTime.isHidden = false
        okButton.isHidden = false
        currentAccBalLabel.isHidden = false
    }
    
    func handleResponse(response: (XMLError, XMLMessage, [String:String])){
        if(self.response.0.msg != nil){
            if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "delegate", isCancelBtnHidden: true)
            }else{
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "popVC", isCancelBtnHidden: true)
            }
            self.hideLoadingView()
        }else if(self.response.1.msg != nil){
            
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "delegate", isCancelBtnHidden: true)
            self.hideLoadingView()
            
        }else{
            DispatchQueue.main.async(){
                if(self.response.2["BALF"] != nil){
                    self.balanceInquiry.text = "PKR \(self.response.2["BALF"]!)"
                    if(self.response.2["DATEF"] != nil){
                        self.dateTime.text = "\(self.response.2["DATEF"]!)"
                    }
                }else{
                    self.balanceInquiry.text = "Not Available"
                }
                
                if(self.accType != nil){
                    self.accountType.text = "(\(self.accType!))"
                }
                self.showSubView()
                self.hideLoadingView()
            }
        }
    }
    
    
    func balanceInquiryRequest(accType: String) {
        self.showLoadingView()
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                xmlPath = Bundle.main.path(forResource: "Command-6-Balance Inquiry", ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                
                else { return }
            response = XMLParser.balanceEnquiryXMLParsing(data)
            handleResponse(response: response)
            
        }else{
            
            
            //let encryptedPin = try! pinText!.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let myAccApi : MyAccountWebAPI = MyAccountWebAPI()
            
            //print(Customer.sharedInstance.bank)
            //print(Customer.sharedInstance.bank?.accounts[0].id)
            
            myAccApi.balanceInqueryPostRequest(
                Constants.CommandId.BAL_INQUIRY,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                pin:"" ,
                ENCT: Constants.AppConfig.ENCT_KEY,
                ACCTYPE: accType,
                APID: "\(userDefault.object(forKey: "useracc")!)",
                BBACID: Customer.sharedInstance.bank?.accounts[0].id != nil ? (Customer.sharedInstance.bank?.accounts[0].id)! : "",
                onSuccess:{(data) -> () in
                    self.response = XMLParser.balanceEnquiryXMLParsing(data)
                    self.handleResponse(response: self.response)
                },
                onFailure: {(reason) ->() in
                    //print("Failure")
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                    self.hideLoadingView()
            })
            
        }
    
        
        
        
    }
    func balanceInquiryHRA(accType: String) {
        self.showLoadingView()
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                xmlPath = Bundle.main.path(forResource: "Command-6-Balance Inquiry", ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                
                else { return }
            response = XMLParser.balanceEnquiryXMLParsing(data)
            handleResponse(response: response)
            
        }else{
            
            
            //let encryptedPin = try! pinText!.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let myAccApi : MyAccountWebAPI = MyAccountWebAPI()
            
            //print(Customer.sharedInstance.bank)
            //print(Customer.sharedInstance.bank?.accounts[0].id)
            
            myAccApi.balanceInqueryHRAPostRequest(
                Constants.CommandId.BAL_INQUIRY,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                pin:"" ,
                ENCT: Constants.AppConfig.ENCT_KEY,
                ACCTYPE: accType,
                APID: "\(userDefault.object(forKey: "useracc")!)",
                BBACID: Customer.sharedInstance.bank?.accounts[0].id != nil ? (Customer.sharedInstance.bank?.accounts[0].id)! : "",
                ISHRA: "1",
                onSuccess:{(data) -> () in
                    self.response = XMLParser.balanceEnquiryXMLParsing(data)
                    self.handleResponse(response: self.response)
                },
                onFailure: {(reason) ->() in
                    //print("Failure")
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                    self.hideLoadingView()
            })
            
        }
    
        
        
        
    }
    
    @IBAction func okPressed(_ sender: UIButton) {
        self.popViewControllerAndGotoStart()
    }

    @IBAction func signOutPressed(_ sender: UIButton) {
        super.signoutCustomer()
    }
    
}
