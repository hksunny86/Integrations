//
//  FinancialPinPopup.swift
//  Timepey
//
//  Created by Adnan Ahmed on 29/06/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import UIKit
import Foundation


class FinancialPinPopup: BaseViewController, UITextFieldDelegate {
    
    @IBOutlet weak var transparentView: UIView!
    @IBOutlet weak var parentView: UIView!
    @IBOutlet weak var inputParentView: UIView!
    
    @IBOutlet weak var okButton: UIButton!
    @IBOutlet weak var cancelButton: UIButton!
    
    @IBOutlet weak var firstTextField: UITextField!
    @IBOutlet weak var secondTextField: UITextField!
    @IBOutlet weak var thirdTextField: UITextField!
    @IBOutlet weak var fourthTextField: UITextField!
    
    
    
    weak var delegate: FinancialPinPopupDelegate?
    let userDefault = UserDefaults.standard
    var encryptedPin: String?
    var response = (XMLError(), XMLMessage(), [String:String]())
    var accountType: String?
    var requiredAction: String?
    var partialBillAmount: String?
    var PMTTYPE: String?
    var tranxCode: String?
    var BAMID: String?
    var product = Product()
    var bankName: String?
    var responseDict = [String:String]()
    var pinRetryCount = 0
    var mPin = ""
    var receiverName = ""
    var bCnic = ""
    var bNumber = ""
    var bEmail = ""
    var bName = ""
    var bFare = "0.0"
    var discountAmt = "0"
    var approxAmt = "0"
    var tax = "0"
    var fee = "0"
    
    override func viewDidDisappear(_ animated: Bool) {
        transparentView.backgroundColor = UIColor.clear
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(true)
    }
    
    override func viewDidLoad() {
        
        //print(product)
        //SettingUp Subviews
        firstTextField.delegate = self
        secondTextField.delegate = self
        thirdTextField.delegate = self
        fourthTextField.delegate = self
        
        firstTextField.keyboardType = UIKeyboardType.numberPad
        secondTextField.keyboardType = UIKeyboardType.numberPad
        thirdTextField.keyboardType = UIKeyboardType.numberPad
        fourthTextField.keyboardType = UIKeyboardType.numberPad
        
        firstTextField.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        secondTextField.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        thirdTextField.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        fourthTextField.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        
        firstTextField.isEnabled = true
        secondTextField.isEnabled = false
        thirdTextField.isEnabled = false
        fourthTextField.isEnabled = false
        
        parentView.layer.cornerRadius = 5
        okButton.layer.cornerRadius = 2
        cancelButton.layer.cornerRadius = 2
        
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard))
        view.addGestureRecognizer(tap)
        
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(UIApplicationDidEnterBackground),
            name: UIApplication.didEnterBackgroundNotification,
            object: nil)
        
        
        //Partial Bill Payment Check
        //        if(productFlowID == Constants.FID.MOBILE_PREPAID){
        //            PMTTYPE = "1"
        //        }else{
        //            PMTTYPE = "0"
        //        }
    }
    
    @objc func UIApplicationDidEnterBackground(notification: NSNotification) {
        firstTextField.text?.removeAll()
        secondTextField.text?.removeAll()
        thirdTextField.text?.removeAll()
        fourthTextField.text?.removeAll()
    }
    
    
    @objc func editingChanged(_ textField: UITextField) {
        let text = textField.text
        
        if textField.tag == 1 {
            if text?.utf16.count == 1 {
                self.secondTextField.isEnabled = true
                self.secondTextField.becomeFirstResponder()
            }
        }
        else if textField.tag == 2 {
            if text?.utf16.count == 1 {
                thirdTextField.isEnabled = true
                thirdTextField.becomeFirstResponder()
            }
            
        }
        else if textField.tag == 3 {
            if text?.utf16.count == 1 {
                fourthTextField.isEnabled = true
                fourthTextField.becomeFirstResponder()
            }
            
        }
        else if textField.tag == 4 {
            if text?.utf16.count == 1 {
                fourthTextField.resignFirstResponder()
            }
        }
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool
    {
        return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 1)
    }
    
    
    @IBAction func okPressed(_ sender: UIButton) {
        
        self.dismissKeyboard()
        
        var errorMessage: String?
        
        mPin = firstTextField.text!+secondTextField.text!+thirdTextField.text!+fourthTextField.text!
        
        if(mPin == ""){
            errorMessage = "MPIN field must not be empty."
        }else if(mPin.count != Constants.Validation.OTP_LENGTH){
            errorMessage = "MPIN length should be of \(Constants.Validation.OTP_LENGTH) digits."
        }
        
        if(errorMessage != nil){
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: errorMessage!, actionType: "serverFailure", isCancelBtnHidden: true)
        }else{
            encryptedPin = try! mPin.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
            self.showLoadingView()
            verifyPin(pinText: mPin)
        }
        resetTextFields()
    }
    
    @IBAction func cancelPressed(_ sender: UIButton) {
        DispatchQueue.main.async {
            self.dismiss(animated: false, completion: nil)
        }
        if(product.fID == Constants.FID.CASH_WITHDRAWAL_LEG1_FID || product.fID == Constants.FID.GENRATE_ATM_PIN){
            delegate?.canclePressedFP()
        }
    }
    
    func nextOperation() {
        
        if(product.fID == Constants.FID.CASH_WITHDRAWAL_LEG1_FID || product.fID == Constants.FID.GENRATE_ATM_PIN || product.fID == Constants.FID.ATM_CARD_BLOCK || product.fID == Constants.FID.ATM_CARD_ACTIVATION || product.fID == Constants.FID.CHANGE_ATM_PIN || product.fID == Constants.FID.COLLECTION_PAYMENT || product.fID == Constants.FID.DEBIT_CARD_ISSUANCE_INFO_UAT) {
            DispatchQueue.main.async {
                self.dismiss(animated: false, completion: nil)
            }
            delegate?.okPressedFP()
        } else if(product.fID == Constants.FID.CHALLAN_NUMBER) {
            DispatchQueue.main.async {
                self.dismiss(animated: false, completion: nil)
            }
            delegate?.okPressedChallanNo(EncMpin: encryptedPin!)
        }
        else if(product.fID == Constants.FID.BALANCE_INQUERY_FID) {
            DispatchQueue.main.async {
                self.dismiss(animated: false, completion: nil)
            }
            
            let viewController = UIStoryboard(name: "MyAccount", bundle: nil).instantiateViewController(withIdentifier: "BalanceInquiry") as! BalanceInquiry
            viewController.productID = self.product.id
            viewController.pinText = mPin
            self.pushViewController(viewController)
            
        }else if(product.fID == Constants.FID.MINISTATEMENT_FID) {
            DispatchQueue.main.async {
                self.dismiss(animated: false, completion: nil)
                let viewController = UIStoryboard(name: "MyAccount", bundle: nil).instantiateViewController(withIdentifier: "MiniStatementVC") as! MiniStatementVC
                viewController.screenTitle = self.requiredAction
                viewController.pinText = self.mPin
                self.pushViewController(viewController)
            }
            
        }
        else if (product.fID == Constants.FID.HRA_MINI_STATEMENT_FID) {
            DispatchQueue.main.async {
                self.dismiss(animated: false, completion: nil)
                let viewController = UIStoryboard(name: "MyAccount", bundle: nil).instantiateViewController(withIdentifier: "MiniStatementVC") as! MiniStatementVC
                viewController.screenTitle = self.requiredAction
                viewController.pinText = self.mPin
                viewController.isHRA = "1"
                self.pushViewController(viewController)
            }
        }
        else if(product.fID == Constants.FID.CASH_WITHDRAWAL){
            
            cashWithdrawalCheckOutPostRequest()
            
        }
        else if(product.fID == Constants.FID.ACCTOCASH_FID || product.fID == Constants.FID.BBTOCORE_FID || product.fID == Constants.FID.BBToIBFT_FID || product.fID == Constants.FID.ACCTOACC_FID || product.fID == Constants.FID.RETAILPAYMENT_FID || product.fID == Constants.FID.TRANSFER_IN_FID || product.fID == Constants.FID.TRANSFER_OUT_FID || product.fID == Constants.FID.TRANSFER_HRA_TO_WALLET){
            
            fundTransferCheckoutPostRequest()
            
        }
        else if(product.fID == Constants.FID.ZONG_MINILOAD_FID || product.fID == Constants.FID.BILL_PAYMENT){
            
            tranxCheckoutPostRequest()
            
        }
        else if (product.fID == Constants.FID.HRA_ACCCOUNT_OPENING) {
            if let encryptedMPIN = encryptedPin {
                delegate?.okPressedChallanNo(EncMpin: encryptedMPIN)
                DispatchQueue.main.async {
                    self.dismiss(animated: false, completion: nil)
                }
            }
        }
        else if (product.fID == Constants.FID.LOAN_PAYMENTS_FID) {
            advanceSalaryPaymentPostRequest()
        }
        else if (product.fID == Constants.FID.BOOKME_BUSES || product.fID == Constants.FID.BOOKME_AIR || product.fID == Constants.FID.BOOKME_HOTEL || product.fID == Constants.FID.BOOKME_EVENTS || product.fID == Constants.FID.BOOKME_CINEMA) {
            tranxCheckoutPostRequest()
            
        }
        else if(requiredAction == "balanceInquiry" || requiredAction == "delegate"){
            delegate?.okPressedFP()
            DispatchQueue.main.async {
                self.dismiss(animated: false, completion: nil)
            }
        }
        
        //self.hideLoadingView()
    }
    
    func hideKeyboard(){
        self.view.endEditing(true)
    }
    
    func resetTextFields(){
        firstTextField.text = ""
        secondTextField.text = ""
        thirdTextField.text = ""
        fourthTextField.text = ""
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
            if(self.response.0.msg != nil){
                if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                }else{
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "pinVerificationFailed", isCancelBtnHidden: true)
                }
            }else if(self.response.1.msg != nil){
                self.pinRetryCount += 1
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "pinVerificationFailed", isCancelBtnHidden: true)
            }else{
                if(self.response.2["DTID"] != nil){
                    nextOperation()
                }
            }
            self.hideLoadingView()
            resetTextFields()
        }else{
            
            //let pinText = inputField.text!
            let encryptedPin = try! pinText.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let myAccApi : MyAccountWebAPI = MyAccountWebAPI()
            
            myAccApi.verifyPinPostRequest(
                Constants.CommandId.VERIFY_PIN,
                CMDID: Constants.CommandId.VERIFY_PIN,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                pin:encryptedPin ,
                ENCT: Constants.AppConfig.ENCT_KEY,
                PIN_RETRY_COUNT: "\(pinRetryCount)",
                MOBN: "",
                CNIC: "",
                ACTION: "",
                onSuccess:{(data) -> () in
                    //print(data)
                    self.response = XMLParser.pinVerificationXMLParsing(data)
                    //print(self.response)
                    if(self.response.0.msg != nil){
                        if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                            self.hideLoadingView()
                        }else{
                            if(self.response.0.code == Constants.ErrorCode.PIN_RETRY_EXHAUSTED){
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "SignOut", isCancelBtnHidden: true)
                            }else{
                                self.pinRetryCount += 1
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "pinVerificationFailed", isCancelBtnHidden: true)
                            }
                            self.hideLoadingView()
                        }
                    }else if(self.response.1.msg != nil){
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "pinVerificationFailed", isCancelBtnHidden: true)
                        self.hideLoadingView()
                    }else{
                        if(self.response.2["DTID"] != nil){
                            self.nextOperation()
                        }
                    }
                },
                onFailure: {(reason) ->() in
                    //print("Failure")
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                    self.hideLoadingView()
                    
                })
        }
    }
    
    func advanceSalaryPaymentPostRequest() {
        let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
        
        let webApi : TransactionAPI = TransactionAPI()
        
        webApi.LoanPaymentsCheckOutRequest(Constants.CommandId.LOAN_PAYMENTS_FINAL, reqTime: currentTime, DTID: Constants.AppConfig.DTID_KEY, PID: product.id!, PIN: encryptedPin!, ENCT: "1", CMOB: responseDict["CMOB"]!, CNIC: responseDict["CUSTOMERCNIC"]!, TXAM: responseDict["TXAM"]!, CAMT: responseDict["CAMT"]!, TPAM: responseDict["TPAM"]!, TAMT: responseDict["TAMT"]!, RRN: responseDict["THIRD_PARTY_RRN"]!, onSuccess:{ data in
            
            self.response = XMLParser.moneyTransferCheckoutXMLParsing(data)
            
            if(self.response.0.msg != nil) {
                if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED) {
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                }else{
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                }
            }else if(self.response.1.msg != nil){
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType:"serverFailure", isCancelBtnHidden: true)
            }else{
                DispatchQueue.main.async {
                    self.dismiss(animated: false, completion: nil)
                }
                DispatchQueue.main.async {
                    let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "NotificationVC") as! NotificationVC
                    viewController.checkOutResponse = self.response
                    viewController.product = self.product
                    viewController.totalAmount = self.response.2["TAMTF"]
                    viewController.isMpinSetLater = self.isMpinSetLater
                    self.pushViewController(viewController)
                }
                
            }
            
            self.hideLoadingView()
        },
        onFailure: {(reason) ->() in
            //print("Failure")
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
            DispatchQueue.main.async {
                self.dismiss(animated: false, completion: nil)
            }
            self.hideLoadingView()
        })
        
        
        
    }
    
    //Fund Transfer + Retail Payment
    func fundTransferCheckoutPostRequest() {
        
        if(Constants.AppConfig.IS_MOCK == 1){
            var xmlFileName: String?
            if(product.fID == Constants.FID.ACCTOCASH_FID){
                xmlFileName = "Command-190-MT AccToCash Checkout"
            }
            else if(product.fID == Constants.FID.ACCTOACC_FID){
                xmlFileName = "Command-181-MT AccToAcc Checkout"
            }else if(product.fID == Constants.FID.RETAILPAYMENT_FID){
                xmlFileName = "Command-82-RetailPayment-Checkout"
            }else if(product.fID == Constants.FID.BBTOCORE_FID || product.fID == Constants.FID.BBToIBFT_FID){
                xmlFileName = "Command-192-MT BBToCore Checkout"
            }else if(product.fID == Constants.FID.TRANSFER_IN_FID){
                xmlFileName = "Command-104-TranferIn_Checkout"
            }
            else if(product.fID == Constants.FID.TRANSFER_OUT_FID){
                xmlFileName = "Command-106-TransferOut_Checkout"
            }
            
            guard let
                    xmlPath = Bundle.main.path(forResource: xmlFileName, ofType: "xml"),
                  let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
            
            else { return }
            
            //let newStr = String(data: data, encoding: NSUTF8StringEncoding)
            //print(newStr)
            self.response = XMLParser.retailPaymentCheckoutXMLParsing(data)
            //print(self.response)
            
            if(self.response.0.msg != nil){
                if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                }else{
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                }
            }else if(self.response.1.msg != nil){
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
            }else{
                DispatchQueue.main.async {
                    self.dismiss(animated: false, completion: nil)
                }
                let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "NotificationVC") as! NotificationVC
                viewController.checkOutResponse = self.response
                viewController.product = self.product
                viewController.totalAmount = self.response.2["TAMTF"]
                if(bankName != nil){
                    viewController.bankName = bankName!
                }
                viewController.isMpinSetLater = self.isMpinSetLater
                self.pushViewController(viewController)
                
                if(self.response.2["BALF"] != nil){
                    self.userDefault.set(self.response.2["BALF"], forKey: "BAL")
                }
            }
            self.hideLoadingView()
        }else{
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let webApi : TransactionAPI = TransactionAPI()
            
            if(product.fID == Constants.FID.ACCTOCASH_FID){
                webApi.AccToCashCheckoutPostRequest(
                    Constants.CommandId.MT_ACCTOCASH_CHECKOUT,
                    reqTime: currentTime,
                    DTID: Constants.AppConfig.DTID_KEY,
                    PID: (product.id!),
                    PIN: encryptedPin!,
                    ENCT: Constants.AppConfig.ENCT_KEY,
                    RWMOB: responseDict["RCMOB"]!,
                    CMOB: Customer.sharedInstance.cMob!,
                    RWCNIC: responseDict["RWCNIC"]!,
                    TXAM: responseDict["TXAM"]!,
                    CAMT: responseDict["CAMT"]!,
                    TPAM: responseDict["TPAM"]!,
                    TAMT: responseDict["TAMT"]!,
                    onSuccess:{(data) -> () in
                        //print(data)
                        self.response = XMLParser.cashWithdrawalCheckoutXMLParsing(data)
                        //print(self.response)
                        if(self.response.0.msg != nil){
                            if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                            }else{
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            }
                        }else if(self.response.1.msg != nil){
                            
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            
                        }else{
                            DispatchQueue.main.async {
                                self.dismiss(animated: false, completion: nil)
                            }
                            
                            let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "NotificationVC") as! NotificationVC
                            viewController.checkOutResponse = self.response
                            viewController.product = self.product
                            viewController.totalAmount = self.response.2["TAMTF"]
                            viewController.isMpinSetLater = self.isMpinSetLater
                            self.pushViewController(viewController)
                            
                            if(self.response.2["BALF"] != nil){
                                self.userDefault.set(self.response.2["BALF"], forKey: "BAL")
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
            else if(product.fID == Constants.FID.ACCTOACC_FID){
                webApi.AccToAccCheckoutPostRequest(
                    Constants.CommandId.MT_ACCTOACC_CHECKOUT,
                    reqTime: currentTime,
                    DTID: Constants.AppConfig.DTID_KEY,
                    PID: (product.id!),
                    PIN: encryptedPin!,
                    ENCT: Constants.AppConfig.ENCT_KEY,
                    RCMOB: responseDict["RCMOB"]!,
                    CMOB: Customer.sharedInstance.cMob!,
                    TXAM: responseDict["TXAM"]!,
                    CAMT: responseDict["CAMT"]!,
                    TPAM: responseDict["TPAM"]!,
                    TAMT: responseDict["TAMT"]!,
                    onSuccess:{(data) -> () in
                        //print(data)
                        self.response = XMLParser.cashWithdrawalCheckoutXMLParsing(data)
                        //print(self.response)
                        if(self.response.0.msg != nil){
                            if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                            }else{
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            }
                        }else if(self.response.1.msg != nil){
                            
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            
                        } else {
                            DispatchQueue.main.async {
                                self.dismiss(animated: false, completion: nil)
                                let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "NotificationVC") as! NotificationVC
                                viewController.checkOutResponse = self.response
                                viewController.product = self.product
                                viewController.totalAmount = self.response.2["TAMTF"]
                                viewController.receiverName = self.receiverName
                                viewController.isMpinSetLater = self.isMpinSetLater
                                self.pushViewController(viewController)
                                if(self.response.2["BALF"] != nil){
                                    self.userDefault.set(self.response.2["BALF"], forKey: "BAL")
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
            else if(product.fID == Constants.FID.RETAILPAYMENT_FID){
                //print(responseDict)
                //                webApi.retailPaymentCheckoutPostRequest(
                //                    Constants.CommandId.RETAILPAYMENT_CHECKOUT,
                //                    reqTime: currentTime,
                //                    DTID: Constants.AppConfig.DTID_KEY,
                //                    PID: (product.id!),
                //                    PIN: encryptedPin!,
                //                    ENCT: Constants.AppConfig.ENCT_KEY,
                //                    AMOB: responseDict["AMOB"]!,
                //                    CMOB: Customer.sharedInstance.cMob!,
                //                    TXAM: responseDict["TXAM"]!,
                //                    CAMT: responseDict["CAMT"]!,
                //                    TPAM: responseDict["TPAM"]!,
                //                    TAMT: responseDict["TAMT"]!,
                //                    onSuccess:{(data) -> () in
                //                        //print(data)
                //                        self.response = XMLParser.cashWithdrawalCheckoutXMLParsing(data)
                //                        //print(self.response)
                //                        //print(self.response.2["BALF"])
                //                        if(self.response.0.msg != nil){
                //                            if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                //                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                //                            }else{
                //                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                //                            }
                //                        }else if(self.response.1.msg != nil){
                //
                //                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                //
                //                        }else{
                //                            self.dismiss(animated: false, completion: nil)
                //
                //                            let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "NotificationVC") as! NotificationVC
                //                            viewController.checkOutResponse = self.response
                //                            viewController.product = self.product
                //                            viewController.totalAmount = self.response.2["TAMTF"]
                //                            self.pushViewController(viewController)
                //                            if(self.response.2["BALF"] != nil){
                //                                self.userDefault.set(self.response.2["BALF"], forKey: "BAL")
                //                            }
                //
                //                        }
                //                        self.hideLoadingView()
                //                    },
                //                    onFailure: {(reason) ->() in
                //                        print("Failure")
                //                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                //                        self.hideLoadingView()
                //                })
            }
            else if(product.fID == Constants.FID.BBTOCORE_FID) {
                
                let accTitle = responseDict["COREACTL"]!.replacingOccurrences(of: "&", with: " ")
                
                webApi.BBToCoreCheckoutPostRequest(
                    Constants.CommandId.MT_BBTOCORE_CHECKOUT,
                    reqTime: currentTime,
                    DTID: Constants.AppConfig.DTID_KEY,
                    PID: (product.id!),
                    PIN: encryptedPin!,
                    ENCT: Constants.AppConfig.ENCT_KEY,
                    RCMOB: responseDict["RCMOB"] != nil ? responseDict["RCMOB"]! : "",
                    CMOB: Customer.sharedInstance.cMob!,
                    COREACID: responseDict["COREACID"]!,
                    COREACTL: accTitle,
                    TXAM: responseDict["TXAM"]!,
                    CAMT: responseDict["CAMT"]!,
                    TPAM: responseDict["TPAM"]!,
                    TAMT: responseDict["TAMT"]!,
                    onSuccess:{(data) -> () in
                        //print(data)
                        self.response = XMLParser.cashWithdrawalCheckoutXMLParsing(data)
                        //print(self.response)
                        if(self.response.0.msg != nil){
                            if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                            }else{
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            }
                        }else if(self.response.1.msg != nil){
                            
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            
                        }else {
                            DispatchQueue.main.async {
                                self.dismiss(animated: false, completion: nil)
                                let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "NotificationVC") as! NotificationVC
                                viewController.checkOutResponse = self.response
                                viewController.product = self.product
                                viewController.totalAmount = self.response.2["TAMTF"]
                                viewController.isMpinSetLater = self.isMpinSetLater
                                self.pushViewController(viewController)
                                if(self.response.2["BALF"] != nil){
                                    self.userDefault.set(self.response.2["BALF"], forKey: "BAL")
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
            else if (product.fID == Constants.FID.BBToIBFT_FID) {
                
                let accTitle = responseDict["COREACTL"]!.replacingOccurrences(of: "&", with: " ")
                
                webApi.BBToIBFTCheckoutPostRequest(
                    Constants.CommandId.MT_BBTOCORE_CHECKOUT,
                    reqTime: currentTime,
                    DTID: Constants.AppConfig.DTID_KEY,
                    PID: (product.id!),
                    PIN: encryptedPin!,
                    ENCT: Constants.AppConfig.ENCT_KEY,
                    CMOB: Customer.sharedInstance.cMob!,
                    RCMOB: responseDict["RCMOB"] != nil ? responseDict["RCMOB"]! : "",
                    COREACID: responseDict["COREACID"]!,
                    COREACTL: accTitle, BAIMD: self.BAMID!, PMTTYPE: "0",
                    TXAM: responseDict["TXAM"]!,
                    TXAMF: responseDict["TXAMF"]!,
                    CAMT: responseDict["CAMT"]!,
                    CAMTF: responseDict["CAMTF"]!,
                    TPAMF:responseDict["TPAMF"]!,
                    TPAM: responseDict["TPAM"]!,
                    TAMT: responseDict["TAMT"]!,
                    BENEFICIARYBANK: responseDict["BENE_BANK_NAME"]!,
                    BENEFICIARYBRANCH: responseDict["BENE_BRANCH_NAME"]!,
                    BENEFICIARYIBAN: responseDict["BENE_IBAN"]!,
                    CRDR: responseDict["CR_DR"]!,
                    COREACTITLE: accTitle,
                    TRANSACTION_PURPOSE_CODE:tranxCode!,
                    
                    
                    onSuccess:{(data) -> () in
                        //print(data)
                        self.response = XMLParser.cashWithdrawalCheckoutXMLParsing(data)
                        //print(self.response)
                        if(self.response.0.msg != nil){
                            if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                            }else{
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            }
                        }else if(self.response.1.msg != nil){
                            
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            
                        }else {
                            DispatchQueue.main.async {
                                self.dismiss(animated: false, completion: nil)
                                let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "NotificationVC") as! NotificationVC
                                viewController.checkOutResponse = self.response
                                viewController.product = self.product
                                viewController.bankName = self.bankName
                                viewController.totalAmount = self.response.2["TAMTF"]
                                viewController.isMpinSetLater = self.isMpinSetLater
                                self.pushViewController(viewController)
                                if(self.response.2["BALF"] != nil){
                                    self.userDefault.set(self.response.2["BALF"], forKey: "BAL")
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
            else if (product.fID == Constants.FID.TRANSFER_HRA_TO_WALLET)
            {
                let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
                
                let webApi : TransactionAPI = TransactionAPI()
                
                webApi.hraToWalletCheckOutPostRequest(
                    Constants.CommandId.MT_HRATOWALLET_CHECKOUT,
                    reqTime: currentTime,
                    DTID: Constants.AppConfig.DTID_KEY,
                    PID: (product.id!),
                    TXAM: responseDict["TXAM"]!,
                    TAMT: responseDict["TAMT"]!,
                    CAMT: responseDict["CAMT"]!,
                    TPAM: responseDict["TPAM"]!,
                    onSuccess:{(data) -> () in
                        //print(data)
                        self.response = XMLParser.cashWithdrawalCheckoutXMLParsing(data)
                        //print(self.response)
                        if(self.response.0.msg != nil){
                            if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                            }else{
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            }
                        }else if(self.response.1.msg != nil){
                            
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            
                        }else {
                            DispatchQueue.main.async {
                                self.dismiss(animated: false, completion: nil)
                            }
                            
                            
                            let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "NotificationVC") as! NotificationVC
                            viewController.checkOutResponse = self.response
                            viewController.product = self.product
                            viewController.totalAmount = self.response.2["TAMTF"]
                            viewController.isMpinSetLater = self.isMpinSetLater
                            self.pushViewController(viewController)
                            
                            if(self.response.2["BALF"] != nil){
                                self.userDefault.set(self.response.2["BALF"], forKey: "BAL")
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
    //Cash Withdrawal
    func cashWithdrawalCheckOutPostRequest() {
        
        self.showLoadingView()
        
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                    xmlPath = Bundle.main.path(forResource: "Command-186-Cash Withdrawal Checkout", ofType: "xml"),
                  let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
            
            else { return }
            
            //let newStr = String(data: data, encoding: NSUTF8StringEncoding)
            //print(newStr)
            self.response = XMLParser.cashWithdrawalCheckoutXMLParsing(data)
            //print(response)
            if(self.response.0.msg != nil){
                if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                }else{
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                }
            }else if(self.response.1.msg != nil){
                
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                
            }else{
                DispatchQueue.main.async {
                    self.dismiss(animated: false, completion: nil)
                    let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "NotificationVC") as! NotificationVC
                    viewController.checkOutResponse = self.response
                    viewController.product = self.product
                    viewController.totalAmount = self.response.2["TAMTF"]
                    viewController.isMpinSetLater = self.isMpinSetLater
                    self.pushViewController(viewController)
                    
                    if(self.response.2["BALF"] != nil){
                        self.userDefault.set(self.response.2["BALF"], forKey: "BAL")
                    }
                }
            }
            self.hideLoadingView()
            
        }else{
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let webApi : CashWithdrawalWebAPI = CashWithdrawalWebAPI()
            
            
            //print(productId)
            //print(responseDict)
            webApi.cashWithdrawalCheckoutPostRequest(
                Constants.CommandId.CASH_WITHDRAWAL_CHECKOUT,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                PID: (product.id!),
                PIN: encryptedPin!,
                ENCT: Constants.AppConfig.ENCT_KEY,
                TXAM: responseDict["TXAM"]!,
                TPAM: responseDict["TPAM"]!,
                CAMT: responseDict["CAMT"]!,
                TAMT: responseDict["TAMT"]!,
                onSuccess:{(data) -> () in
                    //print(data)
                    self.response = XMLParser.cashWithdrawalCheckoutXMLParsing(data)
                    //print(self.response)
                    if(self.response.0.msg != nil){
                        if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                        }else{
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                        }
                    }else if(self.response.1.msg != nil){
                        
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                        
                    }else {
                        DispatchQueue.main.async {
                            self.dismiss(animated: false, completion: nil)
                            let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "NotificationVC") as! NotificationVC
                            viewController.checkOutResponse = self.response
                            viewController.product = self.product
                            viewController.totalAmount = self.response.2["TAMTF"]
                            viewController.isMpinSetLater = self.isMpinSetLater
                            self.pushViewController(viewController)
                        }
                        if(self.response.2["BALF"] != nil){
                            self.userDefault.set(self.response.2["BALF"], forKey: "BAL")
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
    //Bill payment + Balance Topup
    func tranxCheckoutPostRequest() {
        
        self.showLoadingView()
        
        if(Constants.AppConfig.IS_MOCK == 1){
            var xmlFileName: String?
            if(product.fID == Constants.FID.ZONG_MINILOAD_FID){
                xmlFileName = "Command-145-Mini Load Checkout"
            }else{
                xmlFileName = "Command-184-Bill Payment Checkout"
            }
            
            guard let
                    xmlPath = Bundle.main.path(forResource: xmlFileName, ofType: "xml"),
                  let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
            
            else { return }
            
            //let newStr = String(data: data, encoding: NSUTF8StringEncoding)
            //print(newStr)
            self.response = XMLParser.retailPaymentCheckoutXMLParsing(data)
            //print(self.response)
            if(self.response.0.msg != nil){
                if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                }else{
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                }
            }else if(self.response.1.msg != nil){
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType:"serverFailure", isCancelBtnHidden: true)
            }else{
                DispatchQueue.main.async {
                    self.dismiss(animated: false, completion: nil)
                    
                    let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "NotificationVC") as! NotificationVC
                    viewController.checkOutResponse = self.response
                    viewController.product = self.product
                    viewController.totalAmount = self.response.2["TAMTF"]
                    viewController.isMpinSetLater = self.isMpinSetLater
                    self.pushViewController(viewController)
                }
                if(self.response.2["BALF"] != nil){
                    self.userDefault.set(self.response.2["BALF"], forKey: "BAL")
                }
            }
            
            self.hideLoadingView()
        }else{
            
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let webApi : TransactionAPI = TransactionAPI()
            
            
            if(product.fID == Constants.FID.ZONG_MINILOAD_FID){
                //print(responseDict)
                webApi.BalanceTopupCheckoutPostRequest(
                    Constants.CommandId.BALANCE_TOPUP_CHECKOUT,
                    reqTime: currentTime,
                    DTID: Constants.AppConfig.DTID_KEY,
                    PID: (product.id!),
                    PIN: encryptedPin!,
                    ENCT: Constants.AppConfig.ENCT_KEY,
                    TXAM: responseDict["TXAM"]!,
                    TMOB: responseDict["TMOB"]!,
                    CAMT: responseDict["CAMT"]!,
                    TPAM: responseDict["TPAM"]!,
                    TAMT: responseDict["TAMT"]!,
                    onSuccess:{(data) -> () in
                        self.response = XMLParser.cashWithdrawalCheckoutXMLParsing(data)
                        
                        self.hideLoadingView()
                        if(self.response.0.msg != nil){
                            if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                            }else{
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            }
                        }else if(self.response.1.msg != nil){
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType:"serverFailure", isCancelBtnHidden: true)
                        }else{
                            DispatchQueue.main.async {
                                self.dismiss(animated: false, completion: nil)
                                let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "NotificationVC") as! NotificationVC
                                viewController.checkOutResponse = self.response
                                viewController.product = self.product
                                viewController.totalAmount = self.response.2["TAMTF"]
                                viewController.isMpinSetLater = self.isMpinSetLater
                                self.pushViewController(viewController)
                            }
                            
                            if(self.response.2["BALF"] != nil){
                                self.userDefault.set(self.response.2["BALF"], forKey: "BAL")
                            }
                        }
                    },
                    onFailure: {(reason) ->() in
                        //print("Failure")
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                        DispatchQueue.main.async {
                            self.dismiss(animated: false, completion: nil)
                        }
                        self.hideLoadingView()
                    })
            }
            
            
            else if(product.fID == Constants.FID.BOOKME_BUSES || product.fID == Constants.FID.BOOKME_AIR || product.fID == Constants.FID.BOOKME_HOTEL || product.fID == Constants.FID.BOOKME_EVENTS || product.fID == Constants.FID.BOOKME_CINEMA) {
                
                webApi.BookMeCheckOutRequest (
                    Constants.CommandId.BOOKME_FINAL,
                    reqTime: currentTime,
                    DTID: Constants.AppConfig.DTID_KEY,
                    BNAME:self.bName,
                    BCNIC:self.bCnic,
                    BMOB:self.bNumber,
                    BEMAIL:self.bEmail,
                    PID: (product.id!),
                    CMOB: responseDict["CMOB"]!,
                    BAMT: responseDict["BAMT"]!,
                    PMTTYPE:"0",
                    ORDER_ID:responseDict["ORDERID"]!,
                    STYPE: responseDict["STYPE"]!,
                    SPNAME: responseDict["SPNAME"]!,
                    TPAM: responseDict["TPAM"]!,
                    TAMT: responseDict["TAMT"]!,
                    CAMT:responseDict["CAMT"]!,
                    BFARE:bFare,
                    TAPAMT:approxAmt,
                    DAMT:discountAmt,
                    TAX: tax,
                    FEE: fee,
                    onSuccess: {(data) -> () in
                        //print(data)
                        self.response = XMLParser.transTypeXMLParsing(data)
                        self.hideLoadingView()
                        if(self.response.0.msg != nil){
                            if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                            }else{
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            }
                        }else if(self.response.1.msg != nil){
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType:"serverFailure", isCancelBtnHidden: true)
                        }else{
                            DispatchQueue.main.async {
                                self.dismiss(animated: false, completion: nil)
                                let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "NotificationVC") as! NotificationVC
                                viewController.checkOutResponse = self.response
                                viewController.product = self.product
                                viewController.totalAmount = self.response.2["TAMTF"]
                                viewController.bCnic = self.bCnic
                                viewController.bMobileNumber = self.bNumber
                                viewController.isMpinSetLater = self.isMpinSetLater
                                self.pushViewController(viewController)
                            }
                            
                            if(self.response.2["BALF"] != nil){
                                self.userDefault.set(self.response.2["BALF"], forKey: "BAL")
                            }
                        }
                    },
                    onFailure: {(reason) ->() in
                        //print("Failure")
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                        DispatchQueue.main.async {
                            self.dismiss(animated: false, completion: nil)
                        }
                        self.hideLoadingView()
                    })
            }
            else{
                
                var billAmount: String?
                if(partialBillAmount != nil && partialBillAmount != "") {
                    billAmount = partialBillAmount
                }else{
                    billAmount = responseDict["BAMT"]!
                }
                
                webApi.BillPaymentCheckoutPostRequest(
                    Constants.CommandId.BILLPAYMENT_CHECKOUT,
                    reqTime: currentTime,
                    DTID: Constants.AppConfig.DTID_KEY,
                    PID: (product.id!),
                    PIN: encryptedPin!,
                    ENCT: Constants.AppConfig.ENCT_KEY,
                    CMOB: Customer.sharedInstance.cMob!,
                    AMOB: Customer.sharedInstance.cMob!,
                    CSCD: responseDict["CONSUMER"]!,
                    PMTTYPE:"0", //PMTTYPE!,
                    BAID: Customer.sharedInstance.bank!.id!,
                    BAMT: billAmount!,
                    onSuccess:{(data) -> () in
                        //print(data)
                        self.response = XMLParser.cashWithdrawalCheckoutXMLParsing(data)
                        //print(self.response)
                        if(self.response.0.msg != nil){
                            if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                            }else{
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            }
                        }else if(self.response.1.msg != nil){
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType:"serverFailure", isCancelBtnHidden: true)
                        }else{
                            DispatchQueue.main.async {
                                self.dismiss(animated: false, completion: nil)
                                let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "NotificationVC") as! NotificationVC
                                viewController.checkOutResponse = self.response
                                viewController.product = self.product
                                viewController.totalAmount = self.response.2["TAMTF"]
                                viewController.isMpinSetLater = self.isMpinSetLater
                                self.pushViewController(viewController)
                            }
                            
                            if(self.response.2["BALF"] != nil){
                                self.userDefault.set(self.response.2["BALF"], forKey: "BAL")
                            }
                        }
                        
                        self.hideLoadingView()
                    },
                    onFailure: {(reason) ->() in
                        //print("Failure")
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                        DispatchQueue.main.async {
                            self.dismiss(animated: false, completion: nil)
                        }
                        self.hideLoadingView()
                    })
            }
            
        }
    }
    
}
