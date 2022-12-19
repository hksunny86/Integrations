//
//  MobileNumVC.swift
//  JSBL-BB
//
//  Created by Adnan Ahmed on 26/03/2018.
//  Copyright © 2018 Inov8. All rights reserved.
//

import Foundation
import UIKit

class MobileNumVC: BaseViewController,UITextFieldDelegate {
    
    @IBOutlet weak var mobileCode: FPMBottomBorderTextField!
    @IBOutlet weak var mobileNumber: FPMBottomBorderTextField!
    var response = (XMLError(), XMLMessage(), [String:String]())
    var loginIDText:String = ""
    override func viewDidLoad() {
        
        self.setupHeaderBarView("Forgot Password", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: false)
        mobileCode.delegate = self
        mobileNumber.delegate = self
        mobileNumber.isEnabled = false
        mobileCode.keyboardType = UIKeyboardType.numberPad
        mobileNumber.keyboardType = UIKeyboardType.numberPad
        
        mobileCode.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        mobileNumber.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
    }
    
    @objc func editingChanged(_ textField: UITextField) {
        let text = textField.text
        if textField.tag == 1 {
            if text?.utf16.count == 4 {
                mobileNumber.isEnabled = true
                mobileNumber.becomeFirstResponder()
            }
        }
        else if textField.tag == 2 {
            if text?.utf16.count == 7 {
                mobileNumber.resignFirstResponder()
            }
        }
        
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        mobileCode.focusedHeight()
        mobileNumber.focusedHeight()
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        mobileCode.normalHeight()
        mobileNumber.normalHeight()
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if textField.tag == 1 {
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 4)
        } else if textField.tag == 2 {
            guard let text = mobileNumber.text else { return true }
            let newLength  = text.count + string.count - range.length
        
            if newLength == 0 {
            
                mobileCode.becomeFirstResponder()
                mobileNumber.text = ""
                return false
            }
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 7)
        } else { return false }
        
    }
    
    func getMobileNumber() -> String{
        return mobileCode.text!+mobileNumber.text!
    }
    
    
    @IBAction func nextBtnPressed(_ sender: UIButton) {
        
        var errorMessage: String?
        loginIDText = getMobileNumber()
        
        
        if(loginIDText == ""){
            errorMessage = "Mobile number field must not be empty."
        }else if(loginIDText.count != 11){
            errorMessage = "Mobile number length should be of 11 digits."
        }else if(loginIDText[loginIDText.index(loginIDText.startIndex, offsetBy: 0)] != "0" || loginIDText[loginIDText.index(loginIDText.startIndex, offsetBy: 1)] != "3"){
            errorMessage = "Mobile number must start with 03."
        }
        
        if(errorMessage != nil){
            
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: errorMessage!, actionType: "serverFailure", isCancelBtnHidden: true)
        }else{
            fpInfoRequest()
           
        }
        
    }
    
    func resetTextFields(){
        mobileCode.text?.removeAll()
        mobileNumber.text?.removeAll()
    }
    
    func fpInfoRequest(){
        
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                xmlPath = Bundle.main.path(forResource: "Command-74-PinVerification", ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                
                else { return }
            
            //let newStr = String(data: data, encoding: NSUTF8StringEncoding)
            //print(newStr)
            response = XMLParser.pinVerificationXMLParsing(data)
            //print(response)
            self.handleResponse(response: response)
        }else{
            
            //let pinText = inputField.text!
            self.showLoadingView()
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let myAccApi : MyAccountWebAPI = MyAccountWebAPI()
            
            
            myAccApi.fPInfoPostRequest(
                Constants.CommandId.FORGOT_PASSWORD_INFO,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                MOBN:loginIDText,
                onSuccess:{(data) -> () in
                    //print(data)
                    self.response = XMLParser.pinVerificationXMLParsing(data)
                    //print(response)
                    DispatchQueue.main.async(execute: {
                        self.hideLoadingView()
                        self.handleResponse(response: self.response)
                    })
            },
                onFailure: {(reason) -> () in
                    self.alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: reason, okButtonPressed: {
                        self.alertView.hide()
                    })
                    self.alertView.show(parentView: self.view)
            })
        }
    }
    
    func handleResponse(response: (XMLError, XMLMessage, [String:String])){
        if(self.response.0.msg != nil){
            if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: response.0.msg!, okButtonPressed: {
                    self.alertView.hide()
                    Utility.popVCToCustomerLogin()
                })
            }else if(self.response.0.code == Constants.ErrorCode.INVALID_OTP){
                alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: response.0.msg!, okButtonPressed: {
                    self.alertView.hide()
                })
            }
            else{
                alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: response.0.msg!, okButtonPressed: {
                    self.alertView.hide()
                })
            }
            self.alertView.show(parentView: self.view)
        }else if(self.response.1.msg != nil){
            alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: self.response.1.msg!, okButtonPressed: {
                self.alertView.hide()
            })
            self.alertView.show(parentView: self.view)
        }else{
            if(self.response.2["DTID"] != nil){
                let viewController = UIStoryboard(name: "SelfRegistration", bundle: nil).instantiateViewController(withIdentifier: "OTPVerificationVC") as! OTPVerificationVC
                viewController.MOBN = loginIDText
                viewController.flow = "FP"
                self.pushViewController(viewController)
            }
        }
        Utility.hideLoadingView(view: self.view)
        resetTextFields()
    }

}
