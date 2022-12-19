//
//  SetMPIN.swift
//  Timepey
//
//  Created by Adnan Ahmed on 24/09/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation
import UIKit

class SetMPINVC: BaseViewController, UITextFieldDelegate {
    
    
    @IBOutlet weak var headerTitleLabel: UILabel!
    @IBOutlet weak var newPinLabel: UILabel!
    @IBOutlet weak var confirmNewPinLabel: UILabel!
    
    @IBOutlet weak var setMPINButton: UIButton!
    @IBOutlet weak var setMPINLaterButton: UIButton!
    @IBOutlet weak var newPINTextField: UITextField!
    @IBOutlet weak var confirmNewPinTextField: UITextField!
    
    
    var selectedProduct: Product?
    var isMoveToLogin: Bool?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: true)
        
        setupView()
    }
    
    func setupView(){
        
        newPINTextField.delegate = self
        confirmNewPinTextField.delegate = self
        newPINTextField.keyboardType = UIKeyboardType.numberPad
        confirmNewPinTextField.keyboardType = UIKeyboardType.numberPad
        
        setMPINButton.layer.cornerRadius = 2
        if isChangePIN {
            setMPINLaterButton.isHidden = false
        } else {
            setMPINLaterButton.isHidden = true
        }
        newPINTextField.layer.cornerRadius = 2
        newPINTextField.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        newPINTextField.layer.borderWidth = 0.7
        
        confirmNewPinTextField.layer.cornerRadius = 2
        confirmNewPinTextField.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        confirmNewPinTextField.layer.borderWidth = 0.7
        
        let paddingForFirst = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.newPINTextField.frame.size.height))
        //Adding the padding to the second textField
        newPINTextField.leftView = paddingForFirst
        newPINTextField.leftViewMode = UITextField.ViewMode .always
        
        let paddingForSec = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.confirmNewPinTextField.frame.size.height))
        //Adding the padding to the second textField
        confirmNewPinTextField.leftView = paddingForSec
        confirmNewPinTextField.leftViewMode = UITextField.ViewMode .always
        
    }
    
    override func actHeaderBtn(_ buttonPressedType: HEADER_BAR_BUTTON_PRESSED){
        if(buttonPressedType == HEADER_BAR_BUTTON_PRESSED.HEADER_BACK_BUTTON){
            self.popViewController()
        }
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 4)
    }
    
    @IBAction func setMPINButtonPressed(_ sender: UIButton) {
        
        self.dismissKeyboard()
        
        var errorMessage: String?
        
        let newPin = newPINTextField.text
        let confirmPin = confirmNewPinTextField.text
        
        if(newPin == nil || newPin == ""){
            errorMessage = "New MPIN field must not be empty."
        }else if(newPin?.count != 4){
            errorMessage = "New MPIN length should be of 4 digits."
        }
            
        else if(confirmPin == nil || confirmPin == ""){
            errorMessage = "Confirm MPIN field must not be empty."
        }else if(confirmPin?.count != 4){
            errorMessage = "Confirm MPIN length should be of 4 digits."
        }
            
        else if(newPin != confirmPin){
            errorMessage = "New and Confirm MPIN Mismatch."
        }
        
        if(errorMessage != nil){
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: errorMessage!, actionType: "serverFailure", isCancelBtnHidden: true)
        }else{
            changeMPinRequest()
        }
    }
    
    @IBAction func setMPINLaterPressed(_ sender: Any) {
        setMPINLaterRequest()
    }
    
    
    
    func changeMPinRequest() {
        
        self.showLoadingView()
        
        var response = (XMLError(), XMLMessage())
        
        let newPinText = newPINTextField.text
        
        var fileName: String?
        fileName = "Command-200-SetMPIN"

        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                xmlPath = Bundle.main.path(forResource: fileName!, ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                
                else { return }
            
            //let newStr = String(data: data, encoding: NSUTF8StringEncoding)
            //print(newStr)
            
            response = XMLParser.changePINXMLParsing(data)
            if(response.0.msg != nil){
                if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                }else{
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "", isCancelBtnHidden: true)
                }
            }else if(response.1.msg != nil){
                if(isMoveToLogin == true){
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE_PGR_SUCCESS, msgLabelText: response.1.msg!, actionType: "goToMainMenu", isCancelBtnHidden: true)
                } else {
                    UserDefaults.standard.set(false, forKey: "isMpinSetLater")
                    UserDefaults.standard.synchronize()
                    self.isMpinSetLater = false
                    self.clearMainVC()
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE_PGR_SUCCESS, msgLabelText: response.1.msg!, actionType: "goToMainMenu", isCancelBtnHidden: true)
                }
            }
            self.hideLoadingView()
            
        }else{
            
            let encryptedNewPin = try! newPinText!.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            var commandId: String?
            commandId = Constants.CommandId.SET_MPIN
            let myAccApi : MyAccountWebAPI = MyAccountWebAPI()
            
            myAccApi.setMPINPostRequest(
                commandId!,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                pin: encryptedNewPin,
                ENCT: Constants.AppConfig.ENCT_KEY,
                
                onSuccess:{ (data) -> () in
                    
                    response = XMLParser.changePINXMLParsing(data)
                    if(response.0.msg != nil){
                        if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                        }else{
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "requestDenied", isCancelBtnHidden: true)
                            DispatchQueue.main.async {
                                self.newPINTextField.text = ""
                                self.confirmNewPinTextField.text = ""
                            }
                        }
                    }else if(response.1.msg != nil) {
                        if(self.isMoveToLogin == true) {
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE_PGR_SUCCESS, msgLabelText: response.1.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                        } else {
                            self.isChangePIN = false
                            UserDefaults.standard.set(false, forKey: "isMpinSetLater")
                            UserDefaults.standard.synchronize()
                            self.clearMainVC()
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE_PGR_SUCCESS, msgLabelText: response.1.msg!, actionType: "goToMainMenu", isCancelBtnHidden: true)
                        }
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
    
    func setMPINLaterRequest() {
        
        var response = (XMLError(), XMLMessage(), [String:String]())
        
        self.showLoadingView()
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                xmlPath = Bundle.main.path(forResource: "Command-206-My Limits", ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                
                else { return }
            response = XMLParser.paramTypeXMLParsing(data)
            
            if(response.0.msg != nil){
                if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED) {
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                }else{
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                }
            } else if(response.1.msg != nil) {
                
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                
            } else {
                UserDefaults.standard.set(true, forKey: "isMpinSetLater")
                self.isMpinSetLater = true
                UserDefaults.standard.synchronize()
                self.PushVCToMainMenu()
            }
            self.hideLoadingView()
            
        }else{
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let webApi : MyAccountWebAPI = MyAccountWebAPI()
            
            webApi.setMPINLaterPostRequest(
                Constants.CommandId.SETMPINLATER,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                MOBN: Customer.sharedInstance.cMob ?? "",
                CNIC: Customer.sharedInstance.cnic ?? "",
                isMpinSetLater: "1",
                onSuccess:{(data) -> () in
                    //print(data)
                    response = XMLParser.paramTypeXMLParsing(data)
                    //print(self.response)
                    if(response.0.msg != nil) {
                        if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                        } else {
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                        }
                    } else if(response.1.msg != nil) {
                        
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                        
                    } else {
                        if(response.2["DTID"] != nil) {
                            UserDefaults.standard.set(true, forKey: "isMpinSetLater")
                            self.isMpinSetLater = true
                            UserDefaults.standard.synchronize()
                            self.PushVCToMainMenu()
                        }
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
