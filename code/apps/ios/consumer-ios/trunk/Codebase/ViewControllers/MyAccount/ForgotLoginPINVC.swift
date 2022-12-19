//
//  ForgotLoginPINVC.swift
//  JSBL-BB
//
//  Created by Hassan Masood on 1/14/21.
//  Copyright © 2021 Inov8. All rights reserved.
//

import UIKit

class ForgotLoginPINVC: BaseViewController, UITextFieldDelegate {
    
    @IBOutlet weak var mobileCodeTextField: FPMBottomBorderTextField!
    @IBOutlet weak var mobileNumberTextField: FPMBottomBorderTextField!
    @IBOutlet weak var cnicTextFieldFirst: FPMBottomBorderTextField!
    @IBOutlet weak var cnicTextFieldSecond: FPMBottomBorderTextField!
    @IBOutlet weak var cnicTextFieldThird: FPMBottomBorderTextField!
    @IBOutlet weak var btnNext: UIButton!
    @IBOutlet weak var screenTitle: UILabel!
    @IBOutlet weak var mobileNumberView: UIView!
    @IBOutlet weak var cnicNumberView: UIView!
    @IBOutlet weak var headerView: HeaderTitleView!
    @IBOutlet weak var cnicViewTopConst: NSLayoutConstraint!
    
    
    var isComingFromForgotMPIN = false
    var mCode = ""
    var mobileNumber = ""
    
    
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        if isComingFromForgotMPIN {
            mobileNumberView.isHidden = true
            cnicViewTopConst.constant = 15
            screenTitle.text = "Forgot MPIN"
        }
        
    }
    override func viewDidLoad() {
        isForgetLoginPIN = true
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: true)
        setView()
        mobileCodeTextField.text = mCode
        mobileNumberTextField.text = mobileNumber
        
        mobileCodeTextField.delegate = self
        mobileNumberTextField.delegate = self
        cnicTextFieldFirst.delegate = self
        cnicTextFieldSecond.delegate = self
        cnicTextFieldThird.delegate = self
        
        mobileCodeTextField.isEnabled = false
        mobileNumberTextField.isEnabled = false
        cnicTextFieldFirst.isEnabled = true
        cnicTextFieldSecond.isEnabled = false
        cnicTextFieldThird.isEnabled = false
        
        mobileCodeTextField.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        mobileNumberTextField.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        cnicTextFieldFirst.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        cnicTextFieldSecond.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        cnicTextFieldThird.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        
    }
    
    @objc func editingChanged(_ textField: UITextField) {
        
        
        let text = textField.text
        
        if textField.tag == 1 {
            if text?.utf16.count == 4 {
                mobileNumberTextField.isEnabled = true
                mobileNumberTextField.becomeFirstResponder()
            }
        }
        if textField.tag == 2 {
            if text?.utf16.count == 7 {
                cnicTextFieldFirst.isEnabled = true
                cnicTextFieldFirst.becomeFirstResponder()
            }
        }
        
        if textField.tag == 3 {
            if text?.utf16.count == 5 {
                cnicTextFieldSecond.isEnabled = true
                cnicTextFieldSecond.becomeFirstResponder()
            }
        }
        if textField.tag == 4 {
            if text?.utf16.count == 7 {
                cnicTextFieldThird.isEnabled = true
                cnicTextFieldThird.becomeFirstResponder()
            }
        }
        
    }
    
    func setView(){
        
        
        btnNext.layer.cornerRadius = Constants.UI.Button.CORNER_RADIUS
        
        let paddingForFirst = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.mobileNumberTextField.frame.size.height))
        //Adding the padding to the second textField
        mobileNumberTextField.leftView = paddingForFirst
        mobileNumberTextField.leftViewMode = UITextField.ViewMode .always
        
        let paddingForSec = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.cnicTextFieldFirst.frame.size.height))
        //Adding the padding to the second textField
        cnicTextFieldFirst.leftView = paddingForSec
        cnicTextFieldFirst.leftViewMode = UITextField.ViewMode .always
        
        let paddingForThird = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.cnicTextFieldSecond.frame.size.height))
        //Adding the padding to the second textField
        cnicTextFieldSecond.leftView = paddingForThird
        cnicTextFieldSecond.leftViewMode = UITextField.ViewMode .always
        
        let paddingForFourth = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.cnicTextFieldThird.frame.size.height))
        //Adding the padding to the second textField
        cnicTextFieldThird.leftView = paddingForFourth
        cnicTextFieldThird.leftViewMode = UITextField.ViewMode .always
        
        let paddingForFifth = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.mobileCodeTextField.frame.size.height))
        //Adding the padding to the second textField
        mobileCodeTextField.leftView = paddingForFifth
        mobileCodeTextField.leftViewMode = UITextField.ViewMode .always
        
        
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        mobileCodeTextField.focusedHeight()
        mobileNumberTextField.focusedHeight()
        cnicTextFieldFirst.focusedHeight()
        cnicTextFieldSecond.focusedHeight()
        cnicTextFieldThird.focusedHeight()
        
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        mobileCodeTextField.normalHeight()
        mobileNumberTextField.normalHeight()
        cnicTextFieldFirst.normalHeight()
        cnicTextFieldSecond.normalHeight()
        cnicTextFieldThird.normalHeight()
    }
    
    
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if (textField.tag == 1) {
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 4)
        } else if(textField.tag == 2) {
            guard let text = mobileNumberTextField.text else { return true }
            let newLength  = text.count + string.count - range.length
            
            if newLength == 0 {
                
                mobileCodeTextField.becomeFirstResponder()
                mobileNumberTextField.text = ""
                return false
            }
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 7)
            
        } else if(textField.tag == 3) {
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 5)
            
        } else if(textField.tag == 4) {
            guard let text = cnicTextFieldSecond.text else { return true }
            let newLength  = text.count + string.count - range.length
            
            if newLength == 0 {
                
                cnicTextFieldFirst.becomeFirstResponder()
                cnicTextFieldSecond.text = ""
                return false
            }
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 7)
            
        } else if(textField.tag == 5) {
            guard let text = cnicTextFieldThird.text else { return true }
            let newLength  = text.count + string.count - range.length
            
            if newLength == 0 {
                
                cnicTextFieldSecond.becomeFirstResponder()
                cnicTextFieldThird.text = ""
                return false
            }
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 1)
        } else if(textField.tag == 7) {
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_ALL, length: 30)
        } else {
            return false
        }
    }
    
    
    @IBAction func actNext(_ sender: Any) {
        self.dismissKeyboard()
        var errorMessage: String?
        
        
        let mobileNumText = mobileCodeTextField.text!+mobileNumberTextField.text!
        
        let cnicFirstText = cnicTextFieldFirst.text
        let cnicSecText = cnicTextFieldSecond.text
        let cnicThirdText = cnicTextFieldThird.text
        
        
        if isComingFromForgotMPIN {
            
            if((cnicFirstText == nil && cnicSecText == nil && cnicThirdText == nil) || (cnicFirstText! == "" && cnicSecText! == "" && cnicThirdText! == "")){
                errorMessage = "CNIC field must not be empty."
            }else if((cnicFirstText?.count)! != 5 || (cnicSecText?.count)! != 7 || (cnicThirdText?.count)! != 1){
                errorMessage = "CNIC length should be of 13 digits."
            }
            if(errorMessage != nil){
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: errorMessage!, actionType: "serverFailure", isCancelBtnHidden: true)
            } else {
                forgotMPINRequest()
            }
            
        } else {
            
            if(mobileNumText == ""){
                errorMessage = "Mobile number field must not be empty."
            }else if(mobileNumText.count != 11){
                errorMessage = "Mobile number length should be of 11 digits."
            }else if(mobileNumText[mobileNumText.index(mobileNumText.startIndex, offsetBy: 0)] != "0" || mobileNumText[mobileNumText.index(mobileNumText.startIndex, offsetBy: 1)] != "3"){
                errorMessage = "Mobile number must start with 03."
            }
            else if((cnicFirstText == nil && cnicSecText == nil && cnicThirdText == nil) || (cnicFirstText! == "" && cnicSecText! == "" && cnicThirdText! == "")){
                errorMessage = "CNIC field must not be empty."
            }else if((cnicFirstText?.count)! != 5 || (cnicSecText?.count)! != 7 || (cnicThirdText?.count)! != 1){
                errorMessage = "CNIC length should be of 13 digits."
            }
            if(errorMessage != nil){
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: errorMessage!, actionType: "serverFailure", isCancelBtnHidden: true)
            } else {
                
                forgotLoginPINRequest()
            }
        }
        
        
    }
    
    func navigateToSuccessVC(msg:String?) {
        DispatchQueue.main.async {
            let viewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "SuccessVC") as! SuccessVC
            viewController.msg = msg!
            self.pushViewController(viewController)
        }
        
    }
    
    func navigateToOTPScreen() {
        DispatchQueue.main.async {
            let viewController = UIStoryboard(name: "SelfRegistration", bundle: nil).instantiateViewController(withIdentifier: "OTPVerificationVC") as! OTPVerificationVC
            viewController.isComingFromForgotMPIN = self.isComingFromForgotMPIN
            viewController.MOBN = Customer.sharedInstance.cMob!
            self.pushViewController(viewController)
        }
    }
    
    
    func handleResponse(response:(XMLError, XMLMessage, [String : String])){
        if(response.0.msg != nil) {
            if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED) {
                alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: response.0.msg!, okButtonPressed: {
                    self.alertView.hide()
                    Utility.popVCToCustomerLogin()
                })
                DispatchQueue.main.async {
                    self.alertView.show(parentView: self.view)
                }
                
                
            }
            else {
                alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: response.0.msg!, okButtonPressed: {
                    self.alertView.hide()
                })
                DispatchQueue.main.async {
                    self.alertView.show(parentView: self.view)
                }
            }
            
        }
        else if response.2["DTID"] != nil {
            navigateToOTPScreen()
        }
        else if(response.1.msg != nil) {
            if(response.1.code == Constants.ErrorCode.SESSION_EXPIRED) {
                alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: response.1.msg!, okButtonPressed: {
                    self.alertView.hide()
                    Utility.popVCToCustomerLogin()
                })
                DispatchQueue.main.async {
                    self.alertView.show(parentView: self.view)
                }
            }
            else {
                
                navigateToSuccessVC(msg: response.1.msg!)
            }
        }
        
    }
    
    
    func forgotLoginPINRequest() {
        
        self.showLoadingView()
        
        var response = (XMLError(), XMLMessage(), [String:String]())
        
        let mobileNumber = mobileCodeTextField.text!+mobileNumberTextField.text!
        let cnicNumber = cnicTextFieldFirst.text!+cnicTextFieldSecond.text!+cnicTextFieldThird.text!
        let fileName: String = "Command-185-Customer Self-Registration"
        
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                    xmlPath = Bundle.main.path(forResource: fileName, ofType: "xml"),
                  let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
            
            else { return }
            
            response = XMLParser.paramTypeXMLParsing(data)
            handleResponse(response: response)
        }else{
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let myAccApi = MyAccountWebAPI()
            myAccApi.forgotLoginPINRequest(
                Constants.CommandId.FORGOT_LOGIN_PIN,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                MOBN: mobileNumber,
                CNIC: cnicNumber,
                onSuccess:{(data) -> () in
                    self.hideLoadingView()
                    response = XMLParser.paramTypeXMLParsing(data)
                    self.handleResponse(response: response)
                },
                onFailure: {(reason) ->() in
                    self.alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: reason, okButtonPressed: {
                        self.alertView.hide()
                    })
                    self.alertView.show(parentView: self.view)
                    Utility.hideLoadingView(view: self.view)
                })
        }
    }
    
    func forgotMPINRequest() {
        
        self.showLoadingView()
        
        var response = (XMLError(), XMLMessage(), [String:String]())
        
        let mobileNumber = Customer.sharedInstance.cMob!
        let cnicNumber = cnicTextFieldFirst.text!+cnicTextFieldSecond.text!+cnicTextFieldThird.text!
        let fileName: String = "Command-178 OTP Verification"
        
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                    xmlPath = Bundle.main.path(forResource: fileName, ofType: "xml"),
                  let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
            
            else { return }
            self.hideLoadingView()
            response = XMLParser.paramTypeXMLParsing(data)
            handleResponse(response: response)
        }else{
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let myAccApi = MyAccountWebAPI()
            myAccApi.forgotMPINRequest(
                Constants.CommandId.FORGOT_MPIN,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                MOBN: mobileNumber,
                CNIC: cnicNumber,
                onSuccess:{(data) -> () in
                    self.hideLoadingView()
                    response = XMLParser.paramTypeXMLParsing(data)
                    self.handleResponse(response: response)
                },
                onFailure: {(reason) ->() in
                    self.alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: reason, okButtonPressed: {
                        self.alertView.hide()
                    })
                    self.alertView.show(parentView: self.view)
                    Utility.hideLoadingView(view: self.view)
                })
        }
    }
    
    
}
