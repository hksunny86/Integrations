//
//  SetPasswordVC.swift
//  JSBL-BB
//
//  Created by Adnan Ahmed on 26/03/2018.
//  Copyright © 2018 Inov8. All rights reserved.
//

import Foundation
import UIKit

class SetPasswordVC: BaseViewController, UITextFieldDelegate {
    
    //New Password
    @IBOutlet weak var nfirstTextField: FPMBottomBorderTextField!
    @IBOutlet weak var nsecondTextField: FPMBottomBorderTextField!
    @IBOutlet weak var nthirdTextField: FPMBottomBorderTextField!
    @IBOutlet weak var nfourthTextField: FPMBottomBorderTextField!
    //Confirm Password
    @IBOutlet weak var cfirstTextField: FPMBottomBorderTextField!
    @IBOutlet weak var csecondTextField: FPMBottomBorderTextField!
    @IBOutlet weak var cthirdTextField: FPMBottomBorderTextField!
    @IBOutlet weak var cfourthTextField: FPMBottomBorderTextField!
    
    @IBOutlet weak var btnProceed: UIButton!
    
    var encryptedPin: String?
    var encryptedCPin: String?
    var response = (XMLError(), XMLMessage(), [String:String]())
    var MOBN = ""
    
    override func viewDidLoad() {
        
        self.setupHeaderBarView("Forgot Password", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: true)
        
        nfirstTextField.delegate = self
        nsecondTextField.delegate = self
        nthirdTextField.delegate = self
        nfourthTextField.delegate = self
        
        cfirstTextField.delegate = self
        csecondTextField.delegate = self
        cthirdTextField.delegate = self
        cfourthTextField.delegate = self
        
        
        btnProceed.layer.cornerRadius = Constants.UI.Button.CORNER_RADIUS
        
        nfirstTextField.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        nsecondTextField.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        nthirdTextField.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        nfourthTextField.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        
        cfirstTextField.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        csecondTextField.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        cthirdTextField.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        cfourthTextField.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        
        nfirstTextField.isEnabled = true
        nsecondTextField.isEnabled = false
        nthirdTextField.isEnabled = false
        nfourthTextField.isEnabled = false
        
        cfirstTextField.isEnabled = false
        csecondTextField.isEnabled = false
        cthirdTextField.isEnabled = false
        cfourthTextField.isEnabled = false
        
    }
    
    @objc func editingChanged(_ textField: UITextField) {
        let text = textField.text
        
        if textField.tag == 1 {
            if text?.utf16.count == 1 {
                self.nsecondTextField.isEnabled = true
                self.nsecondTextField.becomeFirstResponder()
            }
        }
        else if textField.tag == 2 {
            if text?.utf16.count == 1 {
                nthirdTextField.isEnabled = true
                self.nthirdTextField.becomeFirstResponder()
            }
            
        }
        else if textField.tag == 3 {
            if text?.utf16.count == 1 {
                nfourthTextField.isEnabled = true
                nfourthTextField.becomeFirstResponder()
            }
            
        }
        else if textField.tag == 4 {
            if text?.utf16.count == 1 {
                nfourthTextField.resignFirstResponder()
                cfirstTextField.isEnabled = true
            }
        }
        else if(textField.tag == 5) {
            if text?.utf16.count == 1 {
                csecondTextField.isEnabled = true
                csecondTextField.becomeFirstResponder()
            }
        }else if(textField.tag == 6) {
            
            cthirdTextField.isEnabled = true
            cthirdTextField.becomeFirstResponder()
            
        }else if(textField.tag == 7) {
            cfourthTextField.isEnabled = true
            cfourthTextField.becomeFirstResponder()
        }
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if textField.tag == 1 {
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 1)
        } else if textField.tag == 2 {
            guard let text = nsecondTextField.text else { return true }
            let newLength  = text.count + string.count - range.length
            
            if newLength == 0 {
                
                nfirstTextField.becomeFirstResponder()
                nsecondTextField.text = ""
                return false
            }
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 1)
        }  else if textField.tag == 3 {
            guard let text = nthirdTextField.text else { return true }
            let newLength  = text.count + string.count - range.length
            
            if newLength == 0 {
                
                nsecondTextField.becomeFirstResponder()
                nthirdTextField.text = ""
                return false
            }
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 1)
        }  else if textField.tag == 4 {
            guard let text = nfourthTextField.text else { return true }
            let newLength  = text.count + string.count - range.length
            
            if newLength == 0 {
                
                nthirdTextField.becomeFirstResponder()
                nfourthTextField.text = ""
                return false
            }
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 1)
        } else if textField.tag == 5 {
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 1)
        } else if textField.tag == 6 {
            guard let text = csecondTextField.text else { return true }
            let newLength  = text.count + string.count - range.length
            
            if newLength == 0 {
                
                cfirstTextField.becomeFirstResponder()
                csecondTextField.text = ""
                return false
            }
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 1)
        } else if textField.tag == 7 {
            guard let text = cthirdTextField.text else { return true }
            let newLength  = text.count + string.count - range.length
            
            if newLength == 0 {
                
                csecondTextField.becomeFirstResponder()
                cthirdTextField.text = ""
                return false
            }
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 1)
        } else if textField.tag == 8 {
            guard let text = cfourthTextField.text else { return true }
            let newLength  = text.count + string.count - range.length
            
            if newLength == 0 {
                
                cthirdTextField.becomeFirstResponder()
                cfourthTextField.text = ""
                return false
            }
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 1)
        }
        else { return false }
    }
    
    func resetTextFields(){
        nfirstTextField.text?.removeAll()
        nsecondTextField.text?.removeAll()
        nthirdTextField.text?.removeAll()
        nfourthTextField.text?.removeAll()
        
        cfirstTextField.text?.removeAll()
        csecondTextField.text?.removeAll()
        cthirdTextField.text?.removeAll()
        cfourthTextField.text?.removeAll()
    }
    
    @IBAction func actProceed(_ sender: UIButton) {
        Utility.dismissKeyboard(view: self.view)
        var errorMessage: String?
        
        let mPin = nfirstTextField.text!+nsecondTextField.text!+nthirdTextField.text!+nfourthTextField.text!
        let cmPin = cfirstTextField.text!+csecondTextField.text!+cthirdTextField.text!+cfourthTextField.text!
        
        if(mPin == ""){
            errorMessage = "New PIN must not be empty."
        }else if(mPin.count != Constants.Validation.PASSWORD){
            errorMessage = "New PIN length should be of \(Constants.Validation.PASSWORD) digits."
        }else if(cmPin == ""){
            errorMessage = "Confirm New PIN must not be empty."
        }else if(cmPin.count != Constants.Validation.PASSWORD){
            errorMessage = "Confirm New PIN length should be of \(Constants.Validation.PASSWORD) digits."
        }
        else if(mPin != cmPin){
            errorMessage = "New and Confirm PIN Mismatch."
        }
        
        if(errorMessage != nil){
            alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: errorMessage!, okButtonPressed: {
                self.alertView.hide()
            })
            alertView.show(parentView: self.view)
        }else{
            encryptedPin = try! mPin.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
            encryptedCPin = try! cmPin.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
            Utility.showLoadingView(view: self.view)
            fpFinalRequest(pinText: encryptedPin!, cPinText: encryptedCPin!)
            
        }
        resetTextFields()
        
    }
    
    func fpFinalRequest(pinText: String, cPinText:String){
        
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
            
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let myAccApi : MyAccountWebAPI = MyAccountWebAPI()
            
            
            myAccApi.fPFinalPostRequest(
                Constants.CommandId.FORGOT_PASSWORD_FINAL,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                MOBN: MOBN,
                NMPIN: pinText,
                CMPIN: cPinText,
                onSuccess:{(data) -> () in
                    //print(data)
                    self.response = XMLParser.pinVerificationXMLParsing(data)
                    //print(response)
                    DispatchQueue.main.async(execute: {
                        self.handleResponse(response: self.response)
                    })
                },
                onFailure: {(reason) ->() in
                    //print("Failure")
                    self.alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: reason, okButtonPressed: {
                        self.alertView.hide()
                    })
                    self.alertView.show(parentView: self.view)
                    Utility.hideLoadingView(view: self.view)
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
            let viewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "SuccessVC") as! SuccessVC
            viewController.msg = self.response.1.msg!
            self.pushViewController(viewController)
            
            
        }
        Utility.hideLoadingView(view: self.view)
        resetTextFields()
    }
}
