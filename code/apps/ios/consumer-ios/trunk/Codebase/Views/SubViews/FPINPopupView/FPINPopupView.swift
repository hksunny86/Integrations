//
//  FPINPopupView.swift
//  JSBL-MB
//
//  Created by Uzair on 8/29/17.
//  Copyright © 2017 inov8. All rights reserved.
//

import UIKit

class FPINPopupView: UIView, UITextFieldDelegate {
    
   
    var okCompletionHandler: (() -> ())? = nil
    var cancelCompletionHandler: (() -> ())? = nil
    var pinCompletionHandler : (() -> ())? = nil
    var parentView = UIView()
    @IBOutlet var view: UIView!
    @IBOutlet weak var inputParentView: UIView!
    @IBOutlet weak var pinTextField: UITextField!
    @IBOutlet weak var btnOk: UIButton!
    @IBOutlet weak var btnCancel: UIButton!
    @IBOutlet weak var popUpView: UIView!
    
    var alertView = AlertView()
    var encryptedPin: String?
    var response = (XMLError(), XMLMessage(), [String:String]())
    var pinRetryCount = 0
    var MOBN = ""
    var CNIC = ""
    
    init() {
        super.init(frame: CGRect())
        loadFromXib()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        loadFromXib()
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        guard let text = pinTextField.text else { return true }
        let newLength = text.count + string.count - range.length
        return newLength <= Constants.Validation.REG_OTP_LENGTH
    }
    
    func loadFromXib(){
        DispatchQueue.main.async {
            Bundle.main.loadNibNamed("FPINPopupView", owner: self, options: nil)
            self.pinTextField.delegate = self
            self.popUpView.layer.cornerRadius = 5
            self.inputParentView.layer.cornerRadius = 2
            self.inputParentView.layer.borderWidth = 0.7
            self.inputParentView.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
            self.addSubview(self.view)
        }
    }
    
    func show(parentView:UIView){
        DispatchQueue.main.async(execute: {
            self.view.frame = parentView.frame
            self.parentView = parentView
            self.parentView.addSubview(self.view)
        });
    }
    
    func initWithPin(MOBN:String, CNIC:String, okButtonPressed:(() -> ())?, cancelButtonPressed:(() -> ())?){
        self.MOBN = MOBN
        self.CNIC = CNIC
        self.okCompletionHandler = okButtonPressed
        self.cancelCompletionHandler = cancelButtonPressed
    }
    
    
    func hide(){
        self.pinTextField.text = ""
        self.view.removeFromSuperview()
    }
    
    func resetTextFields(){
        self.pinTextField.text = ""
    }
    
    @IBAction func actOkay(_ sender: UIButton) {
        Utility.dismissKeyboard(view: self)
        var errorMessage: String?
        
        let mPin = pinTextField.text
        
        if(mPin == nil || mPin == ""){
            errorMessage = "MPIN field must not be empty."
        }else if(mPin?.count != Constants.Validation.REG_OTP_LENGTH){
            errorMessage = "MPIN length should be of \(Constants.Validation.REG_OTP_LENGTH) digits."
        }
        
        if(errorMessage != nil){
            self.hide()
            alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: errorMessage!, okButtonPressed: {
                self.alertView.hide()
                self.show(parentView: self.parentView)
            })
            alertView.show(parentView: parentView)
        }else{
            
            
            encryptedPin = try! mPin!.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
            Utility.showLoadingView(view: parentView)
            verifyPin(pinText: mPin!)
            
        }
        resetTextFields()
        
    }

    
    @IBAction func actCancel(_ sender: UIButton) {
        if(self.cancelCompletionHandler != nil){
            self.cancelCompletionHandler!()
        }
        
    }
    
    func handleResponse(response: (XMLError, XMLMessage, [String:String])){
        if(self.response.0.msg != nil){
            self.hide()
            if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: response.0.msg!, okButtonPressed: {
                    self.alertView.hide()
                    Utility.popVCToCustomerLogin()
                })
            }else if(self.response.0.code == Constants.ErrorCode.INVALID_OTP){
                alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: response.0.msg!, okButtonPressed: {
                    self.alertView.hide()
                    self.show(parentView: self.parentView)
                })
            }
            else{
                alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: response.0.msg!, okButtonPressed: {
                    self.alertView.hide()
                })
            }
            self.alertView.show(parentView: self.parentView)
        }else if(self.response.1.msg != nil){
            self.pinRetryCount += 1
            self.hide()
            alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: self.response.1.msg!, okButtonPressed: {
                self.alertView.hide()
                self.show(parentView: self.parentView)
            })
            self.alertView.show(parentView: self.parentView)
        }else{
            if(self.response.2["DTID"] != nil){
                if(self.okCompletionHandler != nil){
                    self.okCompletionHandler!()
                }
            }
        }
        Utility.hideLoadingView(view: parentView)
        pinTextField.text = ""
    }
    
    
    func verifyPin(pinText: String){
        
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
            let encryptedPin = try! pinText.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let myAccApi : MyAccountWebAPI = MyAccountWebAPI()
            
            myAccApi.verifyPinPostRequest(
                Constants.CommandId.VERIFY_PIN_FONEPAY,
                CMDID: Constants.CommandId.VERIFY_PIN_FONEPAY,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                pin:encryptedPin ,
                ENCT: Constants.AppConfig.ENCT_KEY,
                PIN_RETRY_COUNT: "\(pinRetryCount)",
                MOBN: MOBN,
                CNIC: CNIC,
                ACTION: "0",
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
                    self.hide()
                    self.alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: reason, okButtonPressed: {
                        self.alertView.hide()
                        self.show(parentView: self.parentView)
                    })
                    self.alertView.show(parentView: self.parentView)
                    Utility.hideLoadingView(view: self.parentView)
            })
        }
    }

}

