//
//  OTPVerificationVC.swift
//  JSBL-BB
//
//  Created by Adnan Ahmed on 21/12/2017.
//  Copyright © 2017 Inov8. All rights reserved.
//
import UIKit
import Foundation
import Firebase

class OTPVerificationVC: BaseViewController, UITextFieldDelegate {
    
    @IBOutlet weak var lableText: UILabel!
    @IBOutlet weak var hintLableText: UILabel!
    @IBOutlet weak var mainViewTopConstraint: NSLayoutConstraint!
    @IBOutlet weak var btnRegenerateOTP: UIButton!
    @IBOutlet weak var firstTextField: TextField!
    @IBOutlet weak var secondTextField: TextField!
    @IBOutlet weak var thirdTextField: TextField!
    @IBOutlet weak var fourthTextField: TextField!
    @IBOutlet weak var fifthTextField: TextField!
    @IBOutlet weak var btnNext: UIButton!
    @IBOutlet weak var stepperHeader: HeaderTitleView!
    
    
    var flow: String = ""
    var encryptedPin: String?
    var response = (XMLError(), XMLMessage(), [String:String]())
    var pinRetryCount = 0
    var MOBN = ""
    var CNIC = ""
    var isComingFromForgotMPIN = false
    var customerAccount = JSAccount()
    
    
    override func viewDidLayoutSubviews() {
        if(flow == "FP"){
            mainViewTopConstraint.constant = -25
        } else if isComingFromForgotMPIN {
            mainViewTopConstraint.constant = -25
            btnRegenerateOTP.isHidden = true
            stepperHeader.isHidden = true
            btnNext.frame.origin.y = btnRegenerateOTP.frame.origin.y + 50
        }
        
        
    }
    
    
    override func viewDidLoad() {
        if isComingFromForgotMPIN {
            self.setupHeaderBarView("Forgot MPIN", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: false)
            btnNext.setTitle("NEXT",for: .normal)
            
        } else {
            if(flow == "FP"){
                self.setupHeaderBarView("Forgot Password", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: true)
                stepperHeader.isHidden = true
                lableText.text = "OTP Verification"
                hintLableText.text = "Enter your 05 digits One Time password(OTP)"
                btnNext.setTitle("OK",for: .normal)
                
            }else{
                self.setupHeaderBarView("Open Account", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: true)
                btnNext.setTitle("NEXT",for: .normal)
            }
        }
        
        
        firstTextField.delegate = self
        secondTextField.delegate = self
        thirdTextField.delegate = self
        fourthTextField.delegate = self
        fifthTextField.delegate = self
        
        btnNext.layer.cornerRadius = Constants.UI.Button.CORNER_RADIUS
        
        firstTextField.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        secondTextField.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        thirdTextField.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        fourthTextField.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        fifthTextField.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        
        firstTextField.isEnabled = true
        secondTextField.isEnabled = false
        thirdTextField.isEnabled = false
        fourthTextField.isEnabled = false
        fifthTextField.isEnabled = false
        
    }
    
    
    @objc func editingChanged(_ textField: UITextField) {
        switch textField {
        case firstTextField:
            if (firstTextField.text?.utf16.count)! > 0 {
                secondTextField.isEnabled = true
                secondTextField.becomeFirstResponder()
            }
        case secondTextField:
            thirdTextField.isEnabled = true
            thirdTextField.becomeFirstResponder()
        case thirdTextField:
            fourthTextField.isEnabled = true
            fourthTextField.becomeFirstResponder()
        case fourthTextField:
            fifthTextField.isEnabled = true
            fifthTextField.becomeFirstResponder()
        case fifthTextField:
            fifthTextField.resignFirstResponder()
        default:
            break
        }
        
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if textField == secondTextField {
            guard let text = secondTextField.text else { return true }
            let newLength  = text.count + string.count - range.length
            if newLength == 0 {
            
                firstTextField.becomeFirstResponder()
                secondTextField.text = ""
                return false
            }
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 1)
        }else if textField == thirdTextField {
            guard let text = thirdTextField.text else { return true }
            let newLength  = text.count + string.count - range.length
        
            if newLength == 0 {
            
                secondTextField.becomeFirstResponder()
                thirdTextField.text = ""
                return false
            }
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 1)
        } else if textField == fourthTextField {
            guard let text = fourthTextField.text else { return true }
            let newLength  = text.count + string.count - range.length
        
            if newLength == 0 {
            
                thirdTextField.becomeFirstResponder()
                fourthTextField.text = ""
                return false
            }
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 1)
        }else if textField == fifthTextField {
            guard let text = fifthTextField.text else { return true }
            let newLength  = text.count + string.count - range.length
        
            if newLength == 0 {
            
                fourthTextField.becomeFirstResponder()
                fifthTextField.text = ""
                return false
            }
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 1)
        }
        return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 1)
    }
    
    func resetTextFields(){
        DispatchQueue.main.async {
            self.firstTextField.text?.removeAll()
            self.secondTextField.text?.removeAll()
            self.thirdTextField.text?.removeAll()
            self.fourthTextField.text?.removeAll()
            self.fifthTextField.text?.removeAll()
        }
    }
    
    
    @IBAction func actRegenerate(_ sender: Any) {
        
        RegenerateOTP()
        
    }
    
    
    
    @IBAction func actNext(_ sender: UIButton) {
        Utility.dismissKeyboard(view: self.view)
        var errorMessage: String?
        
        let mPin = firstTextField.text!+secondTextField.text!+thirdTextField.text!+fourthTextField.text!+fifthTextField.text!
        
        if(mPin == ""){
            errorMessage = "PIN must not be empty."
        }else if(mPin.count != Constants.Validation.REG_OTP_LENGTH){
            errorMessage = "PIN length should be of \(Constants.Validation.REG_OTP_LENGTH) digits."
        }
        
        if(errorMessage != nil){
            alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: errorMessage!, okButtonPressed: {
                self.alertView.hide()
            })
            alertView.show(parentView: self.view)
        }else{
            encryptedPin = try! mPin.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
            verifyPin(pinText: mPin)
            
        }
        resetTextFields()
        
    }
    
    func RegenerateOTP(){
        
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
            
            myAccApi.regenerateOTPPostRequest(
                Constants.CommandId.REGENERATE_OTP,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                MOBN: MOBN,
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
            })
        }
    }
    
    func verifyPin(pinText: String) {
        
        Utility.showLoadingView(view: self.view)
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                xmlPath = Bundle.main.path(forResource: "Command-74-PinVerification", ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                
                else { return }
            Utility.hideLoadingView(view: self.view)
            response = XMLParser.pinVerificationXMLParsing(data)
            self.handleResponse(response: response)
        }else{
            
            //let pinText = inputField.text!
            let encryptedPin = try! pinText.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let myAccApi : MyAccountWebAPI = MyAccountWebAPI()
            
            var cnic = ""
            if(flow != "FP") {
                cnic = CNIC
            }
            
            myAccApi.verifyPinPostRequest(
                Constants.CommandId.VERIFY_PIN_FONEPAY,
                CMDID: Constants.CommandId.VERIFY_PIN_FONEPAY,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                pin:encryptedPin ,
                ENCT: Constants.AppConfig.ENCT_KEY,
                PIN_RETRY_COUNT: "\(pinRetryCount)",
                MOBN: MOBN,
                CNIC: cnic,
                ACTION: "0",
                onSuccess:{(data) -> () in
                    DispatchQueue.main.async {
                        Utility.hideLoadingView(view: self.view)
                    }

                    self.response = XMLParser.pinVerificationXMLParsing(data)
                    self.handleResponse(response: self.response)
                    
            },
                onFailure: {(reason) ->() in
                    //print("Failure")
                    self.alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: reason, okButtonPressed: {
                        self.alertView.hide()
                    })
                    DispatchQueue.main.async {
                        Utility.hideLoadingView(view: self.view)
                        self.alertView.show(parentView: self.view)
                    }
            })
        }
    }
    
    func handleResponse(response: (XMLError, XMLMessage, [String:String])){
        if(self.response.0.msg != nil) {
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
            DispatchQueue.main.async {
                self.alertView.show(parentView: self.view)
            }
        }else if(self.response.1.msg != nil){
            self.pinRetryCount += 1
            alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: self.response.1.msg!, okButtonPressed: {
                self.alertView.hide()
            })
            DispatchQueue.main.async {
                self.alertView.show(parentView: self.view)
            }
        }else{
            
            if isComingFromForgotMPIN {
                DispatchQueue.main.async {
                    let viewController = UIStoryboard(name: "MyAccount", bundle: nil).instantiateViewController(withIdentifier: "SetMPINVC") as! SetMPINVC
                    self.pushViewController(viewController)
                }
            } else {
                if(self.response.2["DTID"] != nil){
                    
                    if (flow == "FP") {
                        DispatchQueue.main.async {
                            let viewController = UIStoryboard(name: "ForgotPassword", bundle: nil).instantiateViewController(withIdentifier: "SetPasswordVC") as! SetPasswordVC
                            viewController.MOBN = self.MOBN
                            self.pushViewController(viewController)
                        }

                    } else {
                        if((response.2["CNAME"] == nil)) {
                            self.selfRegVerificationPostRequest()
                        } else {
                            DispatchQueue.main.async {
                                let viewController = UIStoryboard(name: "SelfRegistration", bundle: nil).instantiateViewController(withIdentifier: "OpenDiscrepantAccountVC") as! OpenDiscrepantAccountVC
                                let acc = JSAccount()
                                acc.parsing(Dict: response.2)
                                acc.accountNo = self.MOBN
                                acc.cnic = self.CNIC
                                viewController.account = acc
                                self.pushViewController(viewController)
                            }
                        }
                    }
                }
            }
        }
        resetTextFields()
    }
    
    func handleRegistrationResponse(response:(XMLError, XMLMessage, [String : String])){
        
        //print(response)
        if(response.0.msg != nil){
            if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: response.0.msg!, okButtonPressed: {
                    self.alertView.hide()
                    Utility.popVCToCustomerLogin()
                })
                DispatchQueue.main.async {
                    self.alertView.show(parentView: self.view)
                }
                
                
            }else{
                alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: response.0.msg!, okButtonPressed: {
                    self.alertView.hide()
                })
                DispatchQueue.main.async {
                    self.alertView.show(parentView: self.view)
                }
                
                
            }
            
        }
        else if(response.1.msg != nil){
            Analytics.logEvent(AnalyticsEventSignUp, parameters: [
                "Registration":"Success"
            ])
            let viewController = UIStoryboard(name: "SelfRegistration", bundle: nil).instantiateViewController(withIdentifier: "RegistrationSuccessVC") as! RegistrationSuccessVC
            viewController.messageText = response.1.msg
            self.pushViewController(viewController)
            
        }
        else {
            
        }
    }
    
    func selfRegVerificationPostRequest() {
        
        self.showLoadingView()
        
        var response = (XMLError(), XMLMessage(), [String:String]())
        let fileName: String = "Command-186-Customer Self-Registration"
        
        if(Constants.AppConfig.IS_MOCK == 1){
            
            guard let
                xmlPath = Bundle.main.path(forResource: fileName, ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                
                else { return }
            Utility.hideLoadingView(view: self.view)
            response = XMLParser.paramTypeXMLParsing(data)
            handleRegistrationResponse(response: response)
        }else{
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let myAccApi = MyAccountWebAPI()
            

            myAccApi.selfRegFinalPostRequest(
                Constants.CommandId.SELF_REGISTRATION_FINAL,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                CNIC: customerAccount.cnic,
                CMOB: customerAccount.accountNo,
                CNIC_ISSUE_DATE: customerAccount.cnicIssueDate,
                CUST_ACC_TYPE: "1",
                IS_NEW_ACCOUNT: "1",
                EMAIL: customerAccount.customerEmail, CUST_MOB_NETWORK: customerAccount.customerSIMCarrier,
                onSuccess:{(data) -> () in
                    DispatchQueue.main.async {
                        Utility.hideLoadingView(view: self.view)
                    }
                    response = XMLParser.paramTypeXMLParsing(data)
                    self.handleRegistrationResponse(response: response)
            },
                onFailure: {(reason) ->() in
                    self.alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: reason, okButtonPressed: {
                        self.alertView.hide()
                    })
                    DispatchQueue.main.async {
                        Utility.hideLoadingView(view: self.view)
                        self.alertView.show(parentView: self.view)
                    }
            })
            
        }
    }
}
