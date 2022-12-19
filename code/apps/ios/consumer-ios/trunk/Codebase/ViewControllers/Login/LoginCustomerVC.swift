//
//  LoginCustomerVC.swift
//  Timepey
//
//  Created by Adnan Ahmed on 07/06/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation
import UIKit
import AEXML
import CoreTelephony
import Firebase

class LoginCustomerVC: BaseViewController, UITextFieldDelegate, OTPDelegate, FinancialPinPopupDelegate, AlertPopupDelegate {
    
    //Mobile number text fields
    @IBOutlet weak var mCode: FPMBottomBorderTextField!
    @IBOutlet weak var mNumber: FPMBottomBorderTextField!
    //PIN text fields
    @IBOutlet weak var firstPIN: FPMBottomBorderTextField!
    @IBOutlet weak var secondPIN: FPMBottomBorderTextField!
    @IBOutlet weak var thirdPIN: FPMBottomBorderTextField!
    @IBOutlet weak var fourthPIN: FPMBottomBorderTextField!
    @IBOutlet weak var ivrMsg: UILabel!
    
    @IBOutlet weak var termsAndConditions: UILabel!
    @IBOutlet weak var agreeButton: UIButton!
    @IBOutlet weak var showPINButton: UIButton!
    @IBOutlet weak var loginButton: UIButton!
    
    var response = (XMLError(), XMLMessage(), LoginStatus: Bool())
    let userDefault = UserDefaults.standard
    var errorCode: String?
    var loginIDText = ""
    var passwordText = ""
    var isAgreeToTerms: Bool = Bool()
    
    //03427111333
    override func viewDidLoad() {
        
        ivrMsg.isHidden = true
        isAgreeToTerms = false
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: true)
        
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(UIApplicationDidEnterBackground),
            name: UIApplication.didEnterBackgroundNotification,
            object: nil)
        
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard))
        view.addGestureRecognizer(tap)
        let labelTap = UITapGestureRecognizer(target: self, action: #selector(LoginCustomerVC.tapFunction))
        termsAndConditions.isUserInteractionEnabled = true
        termsAndConditions.addGestureRecognizer(labelTap)
        
        showPINButton.addTarget(self, action: #selector(self.holdDown), for: .touchDown)
        showPINButton.addTarget(self, action: #selector(self.holdRelease), for: [.touchUpInside, .touchUpOutside])
        
        mCode.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        mNumber.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        firstPIN.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        secondPIN.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        thirdPIN.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        fourthPIN.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        
        mNumber.isEnabled = true
        firstPIN.isEnabled = true
        secondPIN.isEnabled = false
        thirdPIN.isEnabled = false
        fourthPIN.isEnabled = false
        self.setupView()
        
    }
    
    @objc func editingChanged(_ textField: UITextField) {
        let text = textField.text
        
        if textField.tag == 1 {
            if text?.utf16.count == 4 {
                mNumber.isEnabled = true
                mNumber.becomeFirstResponder()
            }
        }
        else {
            if textField.tag == 2 {
                if text?.utf16.count == 7 {
                    firstPIN.isEnabled = true
                    firstPIN.becomeFirstResponder() 
                }
            }
            else {
                switch textField {
                case firstPIN:
                    if firstPIN.text?.utf16.count == 1 {
                        secondPIN.isEnabled = true
                        secondPIN.becomeFirstResponder()
                    }
                case secondPIN:
                    thirdPIN.isEnabled = true
                    thirdPIN.becomeFirstResponder()
                case thirdPIN:
                    fourthPIN.isEnabled = true
                    fourthPIN.becomeFirstResponder()
                case fourthPIN:
                    fourthPIN.resignFirstResponder()
                default:
                    break
                }
            }
        }
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        mCode.focusedHeight()
        mNumber.focusedHeight()
        firstPIN.focusedHeight()
        secondPIN.focusedHeight()
        thirdPIN.focusedHeight()
        fourthPIN.focusedHeight()
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        mCode.normalHeight()
        mNumber.normalHeight()
        firstPIN.normalHeight()
        secondPIN.normalHeight()
        thirdPIN.normalHeight()
        fourthPIN.normalHeight()
    }
    
    @objc func holdDown(){
        firstPIN.isSecureTextEntry = false
        secondPIN.isSecureTextEntry = false
        thirdPIN.isSecureTextEntry = false
        fourthPIN.isSecureTextEntry = false
    }
    
    @objc func holdRelease(){
        firstPIN.isSecureTextEntry = true
        secondPIN.isSecureTextEntry = true
        thirdPIN.isSecureTextEntry = true
        fourthPIN.isSecureTextEntry = true
    }
    
    func setupView(){
        //Mobile Number
        mCode.delegate = self
        mNumber.delegate = self
        //PIN
        firstPIN.delegate = self
        secondPIN.delegate = self
        thirdPIN.delegate = self
        fourthPIN.delegate = self
        
        mCode.keyboardType = UIKeyboardType.numberPad
        mNumber.keyboardType = UIKeyboardType.numberPad
        firstPIN.keyboardType = UIKeyboardType.numberPad
        secondPIN.keyboardType = UIKeyboardType.numberPad
        thirdPIN.keyboardType = UIKeyboardType.numberPad
        fourthPIN.keyboardType = UIKeyboardType.numberPad
        
        loginButton.layer.cornerRadius = Constants.UI.Button.CORNER_RADIUS
    }
    
    override func viewDidAppear(_ animated: Bool) {
        //print("yes")
    }
    
    func getMobileNumber() -> String{
        return mCode.text!+mNumber.text!
    }
    
    func getPIN() -> String{
        return firstPIN.text!+secondPIN.text!+thirdPIN.text!+fourthPIN.text!
    }
    
    func resetTextFields(){
        //Mobile Number
        mCode.text?.removeAll()
        mNumber.text?.removeAll()
        //PIN
        firstPIN.text?.removeAll()
        secondPIN.text?.removeAll()
        thirdPIN.text?.removeAll()
        fourthPIN.text?.removeAll()
    }
    
    func resetPINTextFields(){
        //PIN
        firstPIN.text?.removeAll()
        secondPIN.text?.removeAll()
        thirdPIN.text?.removeAll()
        fourthPIN.text?.removeAll()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(true)
        resetTextFields()
        if let mobileCode = userDefault.string(forKey: "MCODE") {
            let mobileNumber = userDefault.string(forKey: "MNUMBER")
            if (mobileCode != "") {
                mCode.text = mobileCode
                mNumber.text = mobileNumber
            }
        }
        self.dismissKeyboard()
    }
    
    @objc func UIApplicationDidEnterBackground(notification: NSNotification) {
        resetTextFields()
        if let mobileCode = userDefault.string(forKey: "MCODE") {
            let mobileNumber = userDefault.string(forKey: "MNUMBER")
            if (mobileCode != "") {
                mCode.text = mobileCode
                mNumber.text = mobileNumber
            }
        }
    }
    
    
    //Delegate methods of OTP popup
    func okPressedOTP(_ pinText: String) {
        
        //Device Update
        if(errorCode == "9096"){
            //OK of OTLP
            loginRequest(commandId: Constants.CommandId.OTLIP, pinText: pinText, actionType: "")
        }else if(errorCode == "9025"){
            //OK Pressed of Device Update
            loginRequest(commandId: Constants.CommandId.VERIFY_OTP, pinText: pinText, actionType: "0")
        }
        else{
            loginRequest(commandId: Constants.CommandId.VERIFY_OTP, pinText: pinText, actionType: "2")
        }
        //OTP
        
    }
    func canclePressedOTP(){
        //print("cancel pressed by OTP popup")
    }
    func resendOTP(_ pinText: String) {
        loginRequest(commandId: Constants.CommandId.VERIFY_OTP, pinText: pinText, actionType: "1")
    }
    
    //Delegate of Alert popup
    func okPressedAP(){
        //print("ok pressed by Alert popup")
        loginRequest(commandId: Constants.CommandId.LOGIN, pinText: "", actionType: "")
    }
    func canclePressedAP(){
        //print("cancel pressed by FP popup")
    }
    
    //Delegate of Financial pin popup
    func okPressedFP(){
        //print("ok pressed by FP popup")
    }
    func okPressedChallanNo(EncMpin : String){
        
    }
    func canclePressedFP(){
        //print("cancel pressed by FP popup")
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        
        if (textField.tag == 1) {
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 4)
        } else if(textField.tag == 2) {
            
            guard let text = mNumber.text else { return true }
            let newLength  = text.count + string.count - range.length
            
            if newLength == 0 {
                
                mCode.becomeFirstResponder()
                mNumber.text = ""
                return false
            }
            
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 7)
            
        } else if(textField.tag == 3) {
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 1)
            
        } else if(textField.tag == 4) {
            guard let text = secondPIN.text else { return true }
            let newLength  = text.count + string.count - range.length
            
            if newLength == 0 {
                
                firstPIN.becomeFirstResponder()
                secondPIN.text = ""
                return false
            }
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 1)
            
        } else if(textField.tag == 5) {
            guard let text = thirdPIN.text else { return true }
            let newLength  = text.count + string.count - range.length
            
            if newLength == 0 {
                
                secondPIN.becomeFirstResponder()
                thirdPIN.text = ""
                return false
            }
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 1)
        } else if(textField.tag == 6) {
            guard let text = fourthPIN.text else { return true }
            let newLength  = text.count + string.count - range.length
            
            if newLength == 0 {
                
                thirdPIN.becomeFirstResponder()
                fourthPIN.text = ""
                return false
            }
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 1)
        } else{
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 1)
        }
    }
    
    
    
    @IBAction func forgotPasswordPressed(_ sender: UIButton) {
        let viewController:UIViewController = UIStoryboard(name: "ForgotPassword", bundle: nil).instantiateViewController(withIdentifier: "MobileNumVC") as! MobileNumVC
        self.pushViewController(viewController)
    }
    
    @IBAction func sendLoginRequest(_ sender: UIButton) {
        
        self.dismissKeyboard()
        self.proceedWithLogin()
    }
    
    @IBAction func contactusPressed(_ sender: UIButton) {
        let viewController:UIViewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "ContactUSVC") as UIViewController
        self.pushViewController(viewController)
    }
    
    @IBAction func actAgreeTap(_ sender: UIButton) {
        isAgreeToTerms = !isAgreeToTerms
        let imageName = isAgreeToTerms ? "btn_check_box_pressed" : ""
        sender.setImage(UIImage(named:imageName), for: UIControl.State())
    }
    
    
    func proceedWithLogin(){
        var errorMessage: String?
        
        
        guard let mobileCode = mCode?.text else {return}
        guard let mobileNumber = mNumber?.text else {return}
        loginIDText = mobileCode + mobileNumber 
        
        passwordText = getPIN()
        
        if(loginIDText == ""){
            errorMessage = "Mobile number field must not be empty."
        }else if(loginIDText.count != 11){
            errorMessage = "Mobile number length should be of 11 digits."
        }else if(loginIDText[loginIDText.index(loginIDText.startIndex, offsetBy: 0)] != "0" || loginIDText[loginIDText.index(loginIDText.startIndex, offsetBy: 1)] != "3"){
            errorMessage = "Mobile number must start with 03."
        }
        else if(passwordText == ""){
            errorMessage = "Login PIN field must not be empty."
        }else if((passwordText.count) < Constants.Validation.Login.PASSWORD_MIN || (passwordText.count) > Constants.Validation.Login.PASSWORD_MAX){
            
            if(Constants.Validation.Login.PASSWORD_MIN == Constants.Validation.Login.PASSWORD_MAX){
                errorMessage = "Login PIN length should be \(Constants.Validation.Login.PASSWORD_MIN) characters."
            }else{
                errorMessage = "Login PIN length should be between \(Constants.Validation.Login.PASSWORD_MIN) to \(Constants.Validation.Login.PASSWORD_MAX) characters."
            }
            
        }
        else if(isAgreeToTerms == false){
            errorMessage = "Please agree to the Terms and Conditions."
        }
        if(errorMessage != nil){
            
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: errorMessage!, actionType: "serverFailure", isCancelBtnHidden: true)
        }else{
            loginRequest(commandId: Constants.CommandId.LOGIN,pinText: passwordText, actionType: "")
            resetPINTextFields()
        }
    }
    
    @objc func tapFunction(tapRecognizer: UITapGestureRecognizer) {
        
        if(Reachability.isConnectedToNetwork() == true){
            let touchPoint = tapRecognizer.location(in: termsAndConditions)
            
            var validFrame = CGRect()
            switch UIDevice.current.userInterfaceIdiom {
            case .pad:
                validFrame = CGRect(x: 140, y: 0, width: 170, height: 20)
            case .phone:
                validFrame = CGRect(x: 65, y: 0, width: 200, height: 18)
            default:
                break
            }
            if(true == validFrame.contains(touchPoint))
            {   termsAndConditions.textColor = UIColor.black
                let viewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "CustomWebView") as! CustomWebView
                viewController.screenTitleText = "Terms And Conditions"
                viewController.urlString = "\(Constants.ServerConfig.TERMS_AND_CONDITION_URL)"
                viewController.imgString = "heading_icon_terms"
                viewController.isBottomBarHidden = true
                self.pushViewController(viewController)
            }
        }else{
            self.showNoInternetPopup()
        }
        
        
    }
    
    func loginRequest(commandId: String, pinText: String, actionType: String) {
        
        self.showLoadingView()
        
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                    xmlPath = Bundle.main.path(forResource: "Command-33-Login", ofType: "xml"),
                  let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
            else { return }
            
            self.response = XMLParser.loginXMLParsing(data)
            self.hideLoadingView()
            
            if(self.response.0.msg != nil){
                //Handle Server Error
                handleServerErrors(Error: self.response.0)
            }
            else if(self.response.1.msg != nil){
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "signInFailure", isCancelBtnHidden: true)
                
            }
            else if(self.response.LoginStatus == true) {
                handleSuccessfulLogin()
            }
        }
        else{
            
            var CVNO : String = ""
            if(self.userDefault.object(forKey: "catVersion") == nil){
                CVNO = "-1"
            }else{
                CVNO = String(describing: self.userDefault.object(forKey: "catVersion")!)
            }
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let encryptedPin = try! pinText.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
            
            let LogInApi : UserAccountAPI = UserAccountAPI()
            LogInApi.postLoginRequest(
                commandId,
                reqTime: currentTime,
                userId: loginIDText,
                pin: encryptedPin,
                ENCT: Constants.AppConfig.ENCT_KEY,
                DTID: Constants.AppConfig.DTID_KEY,
                udid: DeviceDemographics.getUDID(),
                appVersion: Constants.AppConfig.APP_VERSION,
                APPID: Constants.AppConfig.APP_ID,
                USTY: Constants.AppConfig.USER_TYPE,
                Operating_System: Constants.AppConfig.OPERATOR_SYSTEM,
                OSVERSION: DeviceDemographics.getOSVersion(),
                iPHONE_MODEL: DeviceDemographics.getModel(),
                VENDOR: Constants.AppConfig.VENDOR,
                ispNetwork: DeviceDemographics.getCarrierName(),
                CVNO: CVNO,
                actionType: actionType,
                onSuccess: {(data) -> () in
                    self.response = XMLParser.loginXMLParsing(data)
                    self.hideLoadingView()
                    
                    if(self.response.0.msg != nil){
                        //Handle Server Error
                        self.handleServerErrors(Error: self.response.0)
                    }
                    else if(self.response.1.msg != nil){
                        ////Handle Server Messages
                        DispatchQueue.main.async(execute: {
                            self.alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: self.response.1.msg!, actionType:"Success", okButtonPressed: {
                                self.alertView.hide()
                                self.loginRequest(commandId: Constants.CommandId.LOGIN,pinText: self.passwordText, actionType: "")
                            })
                            self.alertView.show(parentView: self.view)
                        })
                    }
                    else if(self.response.LoginStatus == true){
                        self.handleSuccessfulLogin()
                    }
                }, onFailure: {(reason) ->() in
                    self.hideLoadingView()
                    DispatchQueue.main.async {
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                    }
                })
        }
        
    }
    
    func handleServerErrors(Error: XMLError) {
        
        if(Error.code == Constants.ErrorCode.APP_VERSION_COMPATIBILITY){
            checkAppVersionCompatibility(APUL: Error.level!)
        }
        else if(Error.code == "9098"){
            self.errorCode = "9096"
            DispatchQueue.main.async {
                self.showOTPPinPopup(Error.msg!, otpFlow: "OK", delegate: self, isResendButtonHidden: true)
            }
            
        }
        else if(Error.code == "9096" || Error.code == "9097"){
            
            self.errorCode = "9096"
            DispatchQueue.main.async {
                self.showOTPPinPopup(Error.msg!, otpFlow: "OC", delegate: self, isResendButtonHidden: true)
            }
            
        }
        else if(Error.code == "9029"){
            self.errorCode = "9029"
            DispatchQueue.main.async {
                self.showOTPPinPopup(Error.msg!, otpFlow: "RC", delegate: self, isResendButtonHidden: false)
            }
        }
        else if(Error.code == "9025"){
            
            self.errorCode = "9025"
            DispatchQueue.main.async {
                self.showOTPPinPopup(Error.msg!, otpFlow: "OCR", delegate: self, isResendButtonHidden: false)
            }
        }
        
        else if(Error.code == "9026" || Error.code == "9027" || Error.code == "9028") {
            self.errorCode = "9025"
            
            DispatchQueue.main.async {
                self.showOTPPinPopup(Error.msg!, otpFlow: "OCR", delegate: self, isResendButtonHidden: false)
            }
        }
        else if (Error.code == "9004") {
            self.errorCode = "9004"
            self.alertView.initWithResetLoginPINButton(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: Error.msg!, okButtonPressed:{
                self.navigateToForgotLoginPIN()
                self.alertView.hide()
            } , cancelButtonPressed: {
            })
            DispatchQueue.main.async {
                self.alertView.show(parentView: self.view)
            }

        }
        else {
            
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: Error.msg!, actionType: "signInFailure", isCancelBtnHidden: true)
            
        }
        
    }
    
    func handleSuccessfulLogin() {
        
        if(Customer.sharedInstance.appV == nil) {
            
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: Constants.Message.UNKNOWN_SERVER_ERROR, actionType: "signInFailure", isCancelBtnHidden: true)
        } else {
            
            DispatchQueue.main.async{
                self.userDefault.set(self.loginIDText, forKey: "useracc")
                self.userDefault.synchronize()
            }
            
            //IPCR + PGR
            if((Customer.sharedInstance.ipcr)! == "1") {
                DispatchQueue.main.async {
                    let viewController = UIStoryboard(name: "MyAccount", bundle: nil).instantiateViewController(withIdentifier: "ChangePinVC") as! ChangePin
                    viewController.screenTitle = "Change Login PIN"
                    viewController.isNewUser = true
                    viewController.isUserLogin = false
                    viewController.isChangePIN = true
                    viewController.productId = Constants.ProductID.CHANGE_LOGINPIN
                    self.pushViewController(viewController)
                }
            }
            else if((Customer.sharedInstance.pgr)! == "1" && (Customer.sharedInstance.ipcr)! == "1"){
                
                DispatchQueue.main.async {
                    let viewController = UIStoryboard(name: "MyAccount", bundle: nil).instantiateViewController(withIdentifier: "ChangePinVC") as! ChangePin
                    viewController.screenTitle = "Change Login PIN"
                    viewController.pgrFlowActive = true
                    viewController.isUserLogin = false
                    viewController.productId = Constants.ProductID.CHANGE_LOGINPIN
                    self.pushViewController(viewController)
                }
                
            }
            else if((Customer.sharedInstance.isMigrated)! == "1" && Customer.sharedInstance.isMpinSetLater == "0") {
                DispatchQueue.main.async {
                    let viewController = UIStoryboard(name: "MyAccount", bundle: nil).instantiateViewController(withIdentifier: "SetMPINVC") as! SetMPINVC
                    viewController.isChangePIN = true
                    self.pushViewController(viewController)
                }
            }
            else if((Customer.sharedInstance.ipcr)! == "1" && (Customer.sharedInstance.impcr)! == "1") {
                DispatchQueue.main.async {
                    let viewController = UIStoryboard(name: "MyAccount", bundle: nil).instantiateViewController(withIdentifier: "ChangePinVC") as! ChangePin
                    viewController.isNewUser = true
                    viewController.isUserLogin = false
                    viewController.screenTitle = "Change Login PIN"
                    viewController.productId = Constants.ProductID.CHANGE_LOGINPIN
                    self.pushViewController(viewController)
                }
            }
            else{
                checkAppVersionCompatibility(APUL: Customer.sharedInstance.apul!)
            }
            
        }
    }
    
    func navigateToForgotLoginPIN() {
        DispatchQueue.main.async {
            let forgotPINVC:ForgotLoginPINVC = UIStoryboard(name: "MyAccount", bundle: nil).instantiateViewController(withIdentifier: "ForgotLoginPINVC") as! ForgotLoginPINVC
            forgotPINVC.mCode = self.mCode.text!
            forgotPINVC.mobileNumber = self.mNumber.text!
            self.pushViewController(forgotPINVC)
        }
    }
    
    func navigateToMainMenu() {
        Analytics.logEvent(AnalyticsEventSignUp, parameters: [
            "Login":"Success"
        ])
        let viewController:MainMenuVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "MainMenuVC") as! MainMenuVC
        if Customer.sharedInstance.isMpinSetLater == "1" {
            if Customer.sharedInstance.isMigrated == "0" {
                viewController.isMpinSetLater = false
                UserDefaults.standard.set(false, forKey: "isMpinSetLater")
            }
            else {
                viewController.isMpinSetLater = true
                UserDefaults.standard.set(true, forKey: "isMpinSetLater")
            }
        }
        self.pushViewController(viewController)
    }
    
    func checkAppVersionCompatibility(APUL: String) {
        if(APUL == Constants.APP_VERSION_COMPATIBILITY.NORMAL){
            self.userDefault.set(self.loginIDText, forKey: "useracc")
            self.userDefault.setValue("\(self.loginIDText.prefix(4))", forKey: "MCODE")
            self.userDefault.setValue("\(self.loginIDText.suffix(7))", forKey: "MNUMBER")
            self.userDefault.synchronize()
            DispatchQueue.main.async {
                self.navigateToMainMenu()
            }
        }
        else if(APUL == Constants.APP_VERSION_COMPATIBILITY.CRITICAL) {
            //Store APUL_STATUS into userDefault
            self.userDefault.set(Constants.AppUsageLevel.CRITICAL, forKey: "AppUsageLevel")
            //Should show OK and Download button
            self.alertView.initWithOkAndDownload(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: Constants.Message.APPLICATION_VERSION_LEVEL_OBSOLETE, okButtonPressed: {
                self.userDefault.set(self.loginIDText, forKey: "useracc")
                self.userDefault.setValue("\(self.loginIDText.prefix(4))", forKey: "MCODE")
                self.userDefault.setValue("\(self.loginIDText.suffix(7))", forKey: "MNUMBER")
                self.userDefault.synchronize()
                DispatchQueue.main.async {
                    self.alertView.hide()
                    self.navigateToMainMenu()
                }
            }, downloadButtonPressed: {
                DispatchQueue.main.async {
                    UIApplication.shared.openURL(URL(string: Constants.Message.APP_DOWNLOAD_URL)!)
                }
                
            })
            DispatchQueue.main.async {
                self.alertView.show(parentView: self.view)
            }
            
            
        }
        else if(APUL == Constants.APP_VERSION_COMPATIBILITY.OBSELETE || APUL == Constants.APP_VERSION_COMPATIBILITY.BLOCK) {
            //Store APUL_STATUS into userDefault
            self.userDefault.set(Constants.AppUsageLevel.BLOCK, forKey: "AppUsageLevel")
            //Should show Download Only button
            
            self.alertView.initWithTitleAndDownloadOnlyButton(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: Constants.Message.APPLICATION_VERSION_LEVEL_OBSOLETE, okButtonPressed: {
                DispatchQueue.main.async {
                    UIApplication.shared.openURL(URL(string: Constants.Message.APP_DOWNLOAD_URL)!)
                }
            }, cancelButtonPressed: {
                DispatchQueue.main.async {
                    self.alertView.hide()
                }
            })
        }
    }
}
