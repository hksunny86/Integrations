//
//  ChangePin.swift
//  Timepey
//
//  Created by Adnan Ahmed on 11/07/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation
import UIKit

class ChangePin: BaseViewController, UITextFieldDelegate {

    
    @IBOutlet weak var screenTitleLabel: UILabel!
    
    @IBOutlet weak var oldPinLabel: UILabel!
    @IBOutlet weak var oldPinTextField: UITextField!
    
    @IBOutlet weak var newPinLabel: UILabel!
    @IBOutlet weak var newPinTextField: UITextField!
    
    @IBOutlet weak var confirmPinLabel: UILabel!
    @IBOutlet weak var confirmnewPinTextField: UITextField!
    
    @IBOutlet weak var changePinButton: UIButton!
    
    var productId: String?
    var screenTitle: String?
    var isNewUser: Bool?
    var pgrFlowActive: Bool?
    
    var isUserLogin: Bool?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if(isUserLogin == false || isNewUser == true) {
            self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: true)
        }else{
            self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: false, isSignoutButtonHidden: false)
        }
        
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard))
        view.addGestureRecognizer(tap)

        screenTitleLabel.text = screenTitle!
        
        if(productId == Constants.ProductID.CHANGE_LOGINPIN){
            oldPinLabel.text = "Old Login PIN"
            newPinLabel.text = "New Login PIN"
            confirmPinLabel.text = "Confirm New Login PIN"
            
            oldPinTextField.keyboardType = UIKeyboardType.numberPad
            newPinTextField.keyboardType = UIKeyboardType.numberPad
            confirmnewPinTextField.keyboardType = UIKeyboardType.numberPad
            
            changePinButton.setTitle("CHANGE LOGIN PIN", for: .normal)
            
        }else{
            oldPinLabel.text = "Old MPIN"
            newPinLabel.text = "New MPIN"
            confirmPinLabel.text = "Confirm New MPIN"
            
            oldPinTextField.keyboardType = UIKeyboardType.numberPad
            newPinTextField.keyboardType = UIKeyboardType.numberPad
            confirmnewPinTextField.keyboardType = UIKeyboardType.numberPad
            
            changePinButton.setTitle("CHANGE MPIN", for: .normal)

        }
        
        setupView()
        
    }
    
    func emptyTextFields(){
        oldPinTextField.text?.removeAll()
        newPinTextField.text?.removeAll()
        confirmnewPinTextField.text?.removeAll()
    }
    
    
    
    override func actHeaderBtn(_ buttonPressedType: HEADER_BAR_BUTTON_PRESSED){
        
        if(buttonPressedType == HEADER_BAR_BUTTON_PRESSED.HEADER_BACK_BUTTON){
            
            if(isNewUser == true){
                self.goToCustomerLogin()
            }else{
                self.popViewController()
            }
            
        }
        else if(buttonPressedType == HEADER_BAR_BUTTON_PRESSED.HEADER_HOME_BUTTON){
            if(Customer.sharedInstance.appV == nil){
                self.goToCustomerLogin()
            }else{
                self.popViewControllerAndGotoStart()
            }
        }
        else if(buttonPressedType == HEADER_BAR_BUTTON_PRESSED.HEADER_SIGNOUT_BUTTON){
            //print(Customer.sharedInstance.appV)
            if(Customer.sharedInstance.appV != nil){
                self.signoutCustomer()
            }
            //self.signoutCustomer()
        }
    }
    
    func setupView(){
        
        
        
        oldPinTextField.delegate = self
        newPinTextField.delegate = self
        confirmnewPinTextField.delegate = self
        
        changePinButton.layer.cornerRadius = 2
        
        oldPinTextField.layer.cornerRadius = 2
        oldPinTextField.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        oldPinTextField.layer.borderWidth = 0.7
        
        newPinTextField.layer.cornerRadius = 2
        newPinTextField.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        newPinTextField.layer.borderWidth = 0.7
        
        confirmnewPinTextField.layer.cornerRadius = 2
        confirmnewPinTextField.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        confirmnewPinTextField.layer.borderWidth = 0.7
        
        let paddingForFirst = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.oldPinTextField.frame.size.height))
        //Adding the padding to the second textField
        oldPinTextField.leftView = paddingForFirst
        oldPinTextField.leftViewMode = UITextField.ViewMode .always
        
        let paddingForSec = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.newPinTextField.frame.size.height))
        //Adding the padding to the second textField
        newPinTextField.leftView = paddingForSec
        newPinTextField.leftViewMode = UITextField.ViewMode .always
        
        let paddingForThird = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.confirmnewPinTextField.frame.size.height))
        //Adding the padding to the second textField
        confirmnewPinTextField.leftView = paddingForThird
        confirmnewPinTextField.leftViewMode = UITextField.ViewMode .always
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(true)
        
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if(productId == Constants.ProductID.CHANGE_LOGINPIN){
            let cs = CharacterSet(charactersIn: Constants.Validation.ACCEPTABLE_CHARACTERS).inverted
            let filtered: String = (string.components(separatedBy: cs) as NSArray).componentsJoined(by: "")
            if(string == filtered){
                guard let text = textField.text else { return true }
                let newLength = text.count + string.count - range.length
                return newLength <= Constants.Validation.Login.PASSWORD_MAX
            }else{
                return false
            }
            
        }else{
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 4)
        }
    }

    

    func isAlphaNumaric(_ string: String)-> Bool{
        let letters = CharacterSet(charactersIn: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz")
        let numbers = CharacterSet(charactersIn: "0123456789")
        
        var lettersResult: Bool?
        
        var numbersResult: Bool?
        
        for i in  String(string).utf16  {
            if !letters.contains(UnicodeScalar(i)!) {
                lettersResult = false
            }else{
                lettersResult = true
                break
            }
        }
        for i in  String(string).utf16  {
            if !numbers.contains(UnicodeScalar(i)!) {
                numbersResult = false
            }else{
                numbersResult = true
                break
            }
        }
        
        if(lettersResult == true && numbersResult == true){
            return true
        }else{
            return false
        }
    }
    
    
    @IBAction func changePinPressed(_ sender: UIButton) {
        
        self.dismissKeyboard()
        
        var errorMessage: String?
        
        let oldPin = oldPinTextField.text
        let newPin = newPinTextField.text
        let confirmPin = confirmnewPinTextField.text
        
        if(productId == Constants.ProductID.CHANGE_LOGINPIN){
            
            if(oldPin == nil || oldPin == ""){
                errorMessage = "Old Login PIN field must not be empty."
            }
            else if((oldPin?.count)! < Constants.Validation.Login.PASSWORD_MIN || (oldPin?.count)! > Constants.Validation.Login.PASSWORD_MAX){
                if(Constants.Validation.Login.PASSWORD_MIN == Constants.Validation.Login.PASSWORD_MAX){
                    errorMessage = "Old Login PIN length should be of \(Constants.Validation.Login.PASSWORD_MIN) digits."
                }else{
                    errorMessage = " Old Login PIN length should be between \(Constants.Validation.Login.PASSWORD_MIN) to \(Constants.Validation.Login.PASSWORD_MAX) digits."
                }
            }
//            else if(isAlphaNumaric(oldPin!) == false){
//                errorMessage = "Current Login PIN should be a combination of alphanumeric characters."
//            }
            
            else if(newPin == nil || newPin == ""){
                errorMessage = "New Login PIN field must not be empty."
            }
            else if((newPin?.count)! < Constants.Validation.Login.PASSWORD_MIN || (newPin?.count)! > Constants.Validation.Login.PASSWORD_MAX){
                if(Constants.Validation.Login.PASSWORD_MIN == Constants.Validation.Login.PASSWORD_MAX){
                    errorMessage = "New Login PIN length should be of \(Constants.Validation.Login.PASSWORD_MIN) digits."
                }else{
                    errorMessage = "New Login PIN length should be between \(Constants.Validation.Login.PASSWORD_MIN) to \(Constants.Validation.Login.PASSWORD_MAX) digits."
                }
            }
//            else if(isAlphaNumaric(newPin!) == false){
//                errorMessage = "New Login PIN should be a combination of alphanumeric characters."
//            }
            
            else if(confirmPin == nil || confirmPin == ""){
                errorMessage = "Confirm Login PIN field must not be empty."
            }
            else if((confirmPin?.count)! < Constants.Validation.Login.PASSWORD_MIN || (confirmPin?.count)! > Constants.Validation.Login.PASSWORD_MAX){
                if(Constants.Validation.Login.PASSWORD_MIN == Constants.Validation.Login.PASSWORD_MAX){
                    errorMessage = "Confirm Login PIN length should be of \(Constants.Validation.Login.PASSWORD_MIN) digits."
                }else{
                    errorMessage = "Confirm Login PIN length should be between \(Constants.Validation.Login.PASSWORD_MIN) to \(Constants.Validation.Login.PASSWORD_MAX) digits."
                }
            }
//            else if(isAlphaNumaric(confirmPin!) == false){
//                errorMessage = "Confirm Login PIN should be a combination of alphanumeric characters."
//            }
            
            else if(newPin != confirmPin){
                errorMessage = "New and Confirm Login PIN Mismatch."
            }
            
            else if(newPin == oldPin){
                errorMessage = "New and Old Login PIN cannot be same."
            }
            
        }else{
            
            if(oldPin == nil || oldPin == ""){
                errorMessage = "Old MPIN field must not be empty."
            }else if(oldPin?.count != 4){
                errorMessage = "Old MPIN length should be of 4 digits."
            }
                
            else if(newPin == nil || newPin == ""){
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
            
            else if(newPin == oldPin){
                errorMessage = "New and Old MPIN cannot be same."
            }
            
        }
        
        if(errorMessage != nil){
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: errorMessage!, actionType: "serverFailure", isCancelBtnHidden: true)
        }else{
            changeMPinRequest()
        }
    }
    
    func changeMPinRequest() {
        
        var response = (XMLError(), XMLMessage())
        
        let oldPinText = oldPinTextField.text
        let newPinText = newPinTextField.text
        let confirmNewPinText = confirmnewPinTextField.text
        
        self.showLoadingView()
        
        var fileName: String?
        if(productId == Constants.ProductID.CHANGE_LOGINPIN){
            fileName = "Command-1-Change Login PIN"
        }else if(productId == Constants.ProductID.CHANGE_MPIN){
            fileName = "Command-5-Change MPIN"
        }
        
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                xmlPath = Bundle.main.path(forResource: fileName!, ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                
                else { return }
            
            response = XMLParser.changePINXMLParsing(data)
            if(response.0.msg != nil){
                if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                }else{
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "", isCancelBtnHidden: true)
                }
            }else if(response.1.msg != nil){
                
                if(self.productId == Constants.ProductID.CHANGE_LOGINPIN) {
                    if(self.pgrFlowActive == true){
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE_IPCR_SUCCESS, msgLabelText: response.1.msg!, actionType: "setMPIN", isCancelBtnHidden: true)
                    }
                    if(self.isNewUser == true){
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE_IPCR_SUCCESS, msgLabelText: response.1.msg!, actionType: "changeMPIN", isCancelBtnHidden: true)
                    }else{
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE_IPCR_SUCCESS, msgLabelText: response.1.msg!, actionType: "SignOut", isCancelBtnHidden: true)
                    }
                }else if(self.productId == Constants.ProductID.CHANGE_MPIN){
                    
                    if(self.isNewUser == true){
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE_IMPCR_SUCCESS, msgLabelText: response.1.msg!, actionType: "SignOut", isCancelBtnHidden: true)
                    }else{
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE_IMPCR_SUCCESS, msgLabelText: response.1.msg!, actionType: "goToMainMenu", isCancelBtnHidden: true)
                    }
                    
                }
                
            }
            self.hideLoadingView()
            
        }else{
            
            
            let encryptedOldPin = try! oldPinText!.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
            let encryptedNewPin = try! newPinText!.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
            let encryptedConfirmNewPin = try! confirmNewPinText!.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
            
            self.emptyTextFields()
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            var commandId: String?
            if(productId == Constants.ProductID.CHANGE_LOGINPIN){
                commandId = Constants.CommandId.CHANGE_LOGINPIN
            }else if(productId == Constants.ProductID.CHANGE_MPIN){
                commandId = Constants.CommandId.CHANGE_MPIN
            }
            
            let myAccApi : MyAccountWebAPI = MyAccountWebAPI()
            
            myAccApi.changeMPINPostRequest(
                commandId!,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                pin: encryptedOldPin ,
                nPin: encryptedNewPin,
                cPin: encryptedConfirmNewPin,
                ENCT: Constants.AppConfig.ENCT_KEY,
                onSuccess:{(data) -> () in
                    
                    response = XMLParser.changePINXMLParsing(data)
                    if(response.0.msg != nil){
                        if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                        }else{
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "requestDenied", isCancelBtnHidden: true)
                            self.oldPinTextField.text = ""
                            self.newPinTextField.text = ""
                            self.confirmnewPinTextField.text = ""
                        }
                    }else if(response.1.msg != nil){
                        if(self.productId == Constants.ProductID.CHANGE_LOGINPIN) {
                            if(self.pgrFlowActive == true) {
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE_IPCR_SUCCESS, msgLabelText: response.1.msg!, actionType: "setMPIN", isCancelBtnHidden: true)
                            }
                            if(self.isNewUser == true) {
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE_IPCR_SUCCESS, msgLabelText: response.1.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                            }else{
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE_IPCR_SUCCESS, msgLabelText: response.1.msg!, actionType: "SignOut", isCancelBtnHidden: true)
                            }
                        }else if(self.productId == Constants.ProductID.CHANGE_MPIN) {
                            
                            if(self.isNewUser == true){
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE_IMPCR_SUCCESS, msgLabelText: response.1.msg!, actionType: "SignOut", isCancelBtnHidden: true)
                            }else{
                                if(self.isUserLogin == false){
                                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE_IMPCR_SUCCESS, msgLabelText: response.1.msg!, actionType: "goToMainMenu", isCancelBtnHidden: true)
                                }else{
                                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE_IMPCR_SUCCESS, msgLabelText: response.1.msg!, actionType: "", isCancelBtnHidden: true)
                                }
                            }
                            
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
