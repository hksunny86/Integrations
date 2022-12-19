//
//  TransDataEntryVC.swift
//  Timepey
//
//  Created by Adnan Ahmed on 28/07/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import UIKit
import Foundation
import ContactsUI

class TransDataEntryVC: BaseViewController, UITextFieldDelegate, UIPickerViewDelegate, UIPickerViewDataSource, CNContactPickerDelegate {
    
    @IBOutlet weak var screenTitleLabel: UILabel!
    @IBOutlet weak var screenSubTitleLabel: UILabel!
    
    @IBOutlet weak var firstTitleLabel: UILabel!
    @IBOutlet weak var firstTextField: UITextField!
    @IBOutlet weak var firstHintLabel: UILabel!
    @IBOutlet weak var firstHintHeight: NSLayoutConstraint!
    
    @IBOutlet weak var secondTitleLabel: UILabel!
    @IBOutlet weak var secondTextField: UITextField!
    @IBOutlet weak var secondHintLabel: UILabel!
    
    @IBOutlet weak var phoneBookButton: UIButton!
    @IBOutlet weak var imgDropDown: UIImageView!
    @IBOutlet weak var nextButton: UIButton!
    
    
    var denomPicker: UIPickerView = UIPickerView()
    var denomDataSource = [String]()
    var prodDenom = [String]()
    var selectedCard = ""
    var selectedCardIndex = 0
    let userDefault = UserDefaults.standard
    //var PMTTYPE: String?
    var product = Product()
    var screenTitleText: String?
    var screenSubTitleText: String?
    var response = (XMLError(), XMLMessage(), [String:String]())
    var arrTelcosProductIDs = ["60027", "60028", "60032", "60033", "60034", "60035", "60036", "60037", "60038", "60039", "2511348", "50050", "50052", "10245160", "50053"]
    
    
    override func viewDidLoad() {
        
        super.viewDidLoad()
        
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: false, isSignoutButtonHidden: false)
        
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard))
        view.addGestureRecognizer(tap)
        
        if(screenTitleText == nil) {
            screenTitleLabel.isHidden = true
        }else{
            screenTitleLabel.text = screenTitleText!
        }
        
        if(screenSubTitleText == nil) {
            screenSubTitleLabel.isHidden = true
        }else{
            screenSubTitleLabel.text = screenSubTitleText!
        }
        
        secondTitleLabel.isHidden = true
        secondTextField.isHidden = true
        secondHintLabel.isHidden = true
        firstHintLabel.isHidden = true
        imgDropDown.isHidden = true
        firstHintHeight.constant = 8
        
        
        if(product.fID == Constants.FID.BILL_PAYMENT || product.fID == Constants.FID.ZONG_MINILOAD_FID) {
            
            if(product.consumerMinLength != nil && product.consumerMinLength != "" && product.consumerMaxLength != nil && product.consumerMaxLength != "") {
                if(product.label != nil){
                    firstTitleLabel.text = product.label!
                }
            }
            else{
                if(product.label != nil){
                    firstTitleLabel.text = product.label!
                }
            }
            
            if(product.inrequired == "0") {
                secondTitleLabel.isHidden = false
                secondTextField.isHidden = false
                secondHintLabel.isHidden = false
                secondTitleLabel.text = "Amount"
                if(product.minamtf != nil && product.maxamtf != nil){
                    secondHintLabel.text = "Enter an amount of PKR \((product.minamtf)!) to PKR \((product.maxamtf)!)"
                }
            }
            if (product.denomFlag == "1") {
                firstTitleLabel.text = product.label!
                secondTitleLabel.text = "Amount"
                secondHintLabel.isHidden = true
                firstHintLabel.isHidden = true
                secondTextField.isHidden = false
                secondTitleLabel.isHidden = false
                imgDropDown.isHidden = false
                denomDataSource =  product.denomString!.components(separatedBy: ",")
                prodDenom = product.prodDenom!.components(separatedBy: ",")
                secondTextField.text = denomDataSource[0]
                pickerViewIntegration()
            }
        }
        else if(product.fID == Constants.FID.COLLECTION_PAYMENT) {
            if(product.inrequired == "0") {
                firstTitleLabel.text = "Amount"
                if(product.minamtf != nil && product.maxamtf != nil){
                    firstHintLabel.isHidden = false
                    firstHintHeight.constant = 32
                    firstHintLabel.text = "Enter an amount of PKR \((product.minamtf)!) to PKR \((product.maxamtf)!)"
                }
            }else if(product.inrequired == "1"){
                firstTitleLabel.text = product.label
            }
        }
        else if (product.fID == Constants.FID.LOAN_PAYMENTS_FID) {
            if(product.inrequired == "0") {
                firstTitleLabel.text = "Amount"
                if(product.minamtf != nil && product.maxamtf != nil){
                    firstHintLabel.isHidden = true
                    firstHintHeight.constant = 32
                    firstHintLabel.text = "Enter an amount of PKR \((product.minamtf)!) to PKR \((product.maxamtf)!)"
                }
            }else if(product.inrequired == "1"){
                firstTitleLabel.text = product.label
            }
            
        }
        else if (product.fID == Constants.FID.CHALLAN_NUMBER) {
            
            if(product.consumerMinLength != nil && product.consumerMinLength != "" && product.consumerMaxLength != nil && product.consumerMaxLength != ""){
                if(product.label != nil){
                    firstTitleLabel.text = product.label!
                }
            }
            else{
                if(product.label != nil){
                    firstTitleLabel.text = product.label!
                }
            }
            secondTitleLabel.isHidden = true
            secondTextField.isHidden = true
            secondHintLabel.isHidden = true
            firstHintLabel.isHidden = true
            firstHintHeight.constant = 8
            
        }
        
        
        setupView()
        
    }
    
    func setupView(){
        
        firstTextField.delegate = self
        secondTextField.delegate = self
        
        firstTextField.keyboardType = UIKeyboardType.numberPad
        secondTextField.keyboardType = UIKeyboardType.numberPad
        
        if(product.type == "ALPHANUMERIC") {
            firstTextField.keyboardType = .asciiCapable
        }
        
        
        nextButton.layer.cornerRadius = 2
        
        firstTextField.layer.cornerRadius = 2
        firstTextField.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        firstTextField.layer.borderWidth = 0.7
        
        secondTextField.layer.cornerRadius = 2
        secondTextField.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        secondTextField.layer.borderWidth = 0.7
        
        let paddingForFirst = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.firstTextField.frame.size.height))
        //Adding the padding to the second textField
        firstTextField.leftView = paddingForFirst
        firstTextField.leftViewMode = UITextField.ViewMode .always
        
        let paddingForSec = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.secondTextField.frame.size.height))
        //Adding the padding to the second textField
        secondTextField.leftView = paddingForSec
        secondTextField.leftViewMode = UITextField.ViewMode .always
        
        if arrTelcosProductIDs.contains((product.id)!) {
            phoneBookButton.isHidden = false
        }
        else {
            phoneBookButton.isHidden = true
        }
        
        
    }
    
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        if(secondTextField.isHidden == true){
            var frame = nextButton.frame
            
            switch UIDevice.current.userInterfaceIdiom {
            case .pad:
                
                if(firstHintLabel.isHidden == true){
                    frame.origin.y = CGFloat(firstTextField.frame.origin.y + firstTextField.frame.size.height + 50)
                }else{
                    frame.origin.y = CGFloat(firstTextField.frame.origin.y + firstTextField.frame.size.height + 82)
                }
                
            case .phone:
                if(firstHintLabel.isHidden == true){
                    frame.origin.y = CGFloat(firstTextField.frame.origin.y + firstTextField.frame.size.height + 30)
                }else{
                    frame.origin.y = CGFloat(firstTextField.frame.origin.y + firstTextField.frame.size.height + 62)
                }
                
            default:
                break
            }
            
            
            nextButton.frame = frame
        }
    }
    
    func validateAmount(_ textField: UITextField) -> Bool{
        let minAmount = Double((product.minamt)!)
        let maxAmount = Double((product.maxamt)!)
        let amount = Double(textField.text!)
        
        if(amount! < minAmount! || amount! > maxAmount!){
            return false
        }else{
            return true
        }
    }
    
    //MASK: TesxtField Delegate
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if (textField.tag == 1){
            if(product.consumerMaxLength != nil && product.consumerMaxLength != "") {
                
                if let myNumber = NumberFormatter().number(from: (product.consumerMaxLength)!) {
                    let maxInt = myNumber.intValue
                    if(product.type == "ALPHANUMERIC") {
                        return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_ALPHANUMERIC, length: maxInt)
                    } else {
                        return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: maxInt)
                    }
                } else {
                    if product.type == "ALPHANUMERIC" {
                        return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_ALPHANUMERIC, length: 30)
                    } else {
                        return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 30)
                    }
                }
                
            } else {
                if product.type == "ALPHANUMERIC" {
                    return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_ALPHANUMERIC, length: 30)
                } else {
                    return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 30)
                }
            }
        } else {
            if product.type == "ALPHANUMERIC" {
                return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_ALPHANUMERIC, length: 7)
            } else {
                return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 7)
            }

        }
    }
    
    func pickerViewIntegration() {
        
        denomPicker = UIPickerView(frame: CGRect(x: 0, y: 200, width: view.frame.width, height: 300))
        denomPicker.backgroundColor = UIColor.white
        
        denomPicker.showsSelectionIndicator = true
        denomPicker.delegate = self
        denomPicker.dataSource = self
        
        let toolBar = UIToolbar()
        toolBar.barStyle = UIBarStyle.default
        toolBar.isTranslucent = true
        toolBar.tintColor = UIColor(red: 76/255, green: 217/255, blue: 100/255, alpha: 1)
        toolBar.sizeToFit()
        
        let doneButton = UIBarButtonItem(title: "Done", style: UIBarButtonItem.Style.plain, target: self, action: #selector(TransDataEntryVC.donePicker))
        let spaceButton = UIBarButtonItem(barButtonSystemItem: UIBarButtonItem.SystemItem.flexibleSpace, target: nil, action: nil)
        let cancelButton = UIBarButtonItem(title: "Cancel", style: UIBarButtonItem.Style.plain, target: self, action: #selector(TransDataEntryVC.donePicker))
        
        toolBar.setItems([cancelButton, spaceButton, doneButton], animated: false)
        toolBar.isUserInteractionEnabled = true
        
        secondTextField.inputView = denomPicker
        secondTextField.inputAccessoryView = toolBar
        
    }
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        denomDataSource.count
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return denomDataSource[row]
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        secondTextField.text = denomDataSource[row]
        selectedCard = denomDataSource[row]
        selectedCardIndex = row
    }
    
    @objc func donePicker() {
        secondTextField.resignFirstResponder()
    }
    
    
    @IBAction func actPhonebookTap(_ sender: Any) {
        let contacVC = CNContactPickerViewController()
          contacVC.delegate = self
          self.present(contacVC, animated: true, completion: nil)
    }

    
    
    // MARK: Delegate method CNContectPickerDelegate
    func contactPicker(_ picker: CNContactPickerViewController, didSelect contact: CNContact) {
        
        let numbers = contact.phoneNumbers.first
        let number = String(numbers?.value.stringValue ?? "")
        let numberArray = number.map { String ($0) }
        let numbersOnly = numberArray.filter { Int($0) != nil || $0.contains("+")}
        let phoneNumber = numbersOnly.joined(separator: "")
        if phoneNumber.starts(with: "03") || phoneNumber.starts(with: "+92") || phoneNumber.starts(with: "0092"){
            let newPhoneNumber = Utility.addPhoneNoCountryCode(phoneNo: phoneNumber)
            if newPhoneNumber!.count == 11 {
                self.firstTextField.text = newPhoneNumber
            }
            else {
                self.showMessage("Invalid Mobile Number.")
            }
        }
        else {
            self.showMessage("Invalid Mobile Number.")
        }

          
    }

    func contactPickerDidCancel(_ picker: CNContactPickerViewController) {
        self.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func nextButtonPressed(_ sender: UIButton) {
        
        self.dismissKeyboard()
        
        var errorMessage: String?
        
        var minConsumerLength = 0
        var maxConsumerLength = 0
        
        var minAmount = 0
        var maxAmount = 0
        
        if(product.consumerMinLength != nil && product.consumerMinLength != "" && product.consumerMaxLength != nil && product.consumerMaxLength != ""){
            minConsumerLength = ((NumberFormatter().number(from: (product.consumerMinLength)!))?.intValue)!
            maxConsumerLength = ((NumberFormatter().number(from: (product.consumerMaxLength)!))?.intValue)!
        }else{
            minConsumerLength = Constants.Validation.TextField.MIN_CONSUMER_LEN
            maxConsumerLength = Constants.Validation.TextField.MAX_CONSUMER_LEN
        }
        
        if(product.minamt != nil && product.minamt != "" && product.maxamt != nil && product.maxamt != ""){
            
            minAmount = ((NumberFormatter().number(from: (product.minamt)!))?.intValue)!
            maxAmount = ((NumberFormatter().number(from: (product.maxamt)!))?.intValue)!
        }else{
            minAmount = Constants.Validation.TextField.AMOUNT_MIN
            maxAmount = Constants.Validation.TextField.AMOUNT_MAX
        }
        
        if(product.fID == Constants.FID.COLLECTION_PAYMENT){
            
            if(product.inrequired == "0"){
                if(firstTextField.text == nil || firstTextField.text == ""){
                    errorMessage = "Amount field must not be empty."
                }else if(Int((firstTextField?.text)!)! < minAmount || Int((firstTextField?.text)!)! > maxAmount){
                    errorMessage = "Invalid amount entered."
                }
            }else{
                if(firstTextField.text == nil || firstTextField.text == ""){
                    errorMessage = "\(firstTitleLabel.text!) field must not be empty."
                }else if((firstTextField.text?.count)! < minConsumerLength || (firstTextField.text?.count)! > maxConsumerLength){
                    errorMessage = "Please enter valid \(firstTitleLabel.text!)."
                }
            }
            
        }else{
            
            if(firstTextField.text == nil || firstTextField.text == ""){
                errorMessage = "\(firstTitleLabel.text!) field must not be empty."
            }else if((firstTextField.text?.count)! < minConsumerLength || (firstTextField.text?.count)! > maxConsumerLength){
                
                if(minConsumerLength == maxConsumerLength){
                    errorMessage = "\(firstTitleLabel.text!) length should be \(minConsumerLength) digits."
                }else{
                    errorMessage = "\(firstTitleLabel.text!) should be between \(minConsumerLength) to \(maxConsumerLength) digits."
                }
                
            }else if(product.fID == Constants.FID.LOAN_PAYMENTS_FID) {
                
                if(product.inrequired == "0") {
                    if(firstTextField.text == nil || firstTextField.text == "") {
                        errorMessage = "Amount field must not be empty."
                    }else if(Int((firstTextField?.text)!)! < minAmount || Int((firstTextField?.text)!)! > maxAmount){
                        errorMessage = "Invalid amount entered."
                    }
                }
                else {
                    if(firstTextField.text == nil || firstTextField.text == ""){
                        errorMessage = "\(firstTitleLabel.text!) field must not be empty."
                    }else if((firstTextField.text?.count)! < minConsumerLength || (firstTextField.text?.count)! > maxConsumerLength){
                        errorMessage = "Please enter valid \(firstTitleLabel.text!)."
                    }
                }
                
                
            } else if product.fID == Constants.FID.CHALLAN_NUMBER {
                
                if(product.inrequired == "1") {
                    if(firstTextField.text == nil || firstTextField.text == "") {
                        errorMessage = "Amount field must not be empty."
                    }else if(Int((firstTextField?.text)!)! < minAmount || Int((firstTextField?.text)!)! > maxAmount){
                        errorMessage = "Invalid amount entered."
                    }
                }
                else {
                    if(firstTextField.text == nil || firstTextField.text == ""){
                        errorMessage = "\(firstTitleLabel.text!) field must not be empty."
                    }else if((firstTextField.text?.count)! < minConsumerLength || (firstTextField.text?.count)! > maxConsumerLength){
                        errorMessage = "Please enter valid \(firstTitleLabel.text!)."
                    }
                }
                
            }
            else if( product.inrequired == "0") {
                if(secondTextField.text == nil || secondTextField.text == ""){
                    errorMessage = "Amount field must not be empty."
                }else if(Int((secondTextField?.text)!)! < minAmount || Int((secondTextField?.text)!)! > maxAmount){
                    
                    errorMessage = "Invalid amount entered."
                }
                
            }
        }
        
        if(errorMessage != nil){
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: errorMessage!, actionType: "serverFailure", isCancelBtnHidden: true)
        }else{
            
            transactionInfoPostRequest()
        }
    }
    
    func transactionInfoPostRequest() {
        
        self.showLoadingView()
        
        //print(product.fID)
        
        if(Constants.AppConfig.IS_MOCK == 1){
            
            var xmlFileName: String?
            if(product.fID == Constants.FID.ZONG_MINILOAD_FID){
                xmlFileName = "Command-144-Mini Load Info"
            }
            else if(product.fID == Constants.FID.COLLECTION_PAYMENT){
                xmlFileName = "Command-209 Collection Payment Info"
            } else if(product.fID == Constants.FID.CHALLAN_NUMBER ) {
                
                xmlFileName = "Command 209 Collection Payment Info"
            }
            else{
                xmlFileName = "Command-183-Bill Payment Info"
            }
            
            guard let
                xmlPath = Bundle.main.path(forResource: xmlFileName, ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                
                else { return }
            
            //let newStr = String(data: data, encoding: NSUTF8StringEncoding)
            //print(newStr)
            self.response = XMLParser.moneyTransferInfoXMLParsing(data)
            
            if(self.response.0.msg != nil){
                
                if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                    self.hideLoadingView()
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                }else{
                    self.hideLoadingView()
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                }
            }else if(self.response.1.msg != nil){
                self.hideLoadingView()
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                
            }else{
                DispatchQueue.main.async {
                    self.hideLoadingView()
                    let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "ConfirmationVC") as! ConfirmationVC
                    viewController.responseDict = self.response.2
                    viewController.product = self.product
                    viewController.isMpinSetLater = self.isMpinSetLater
                    self.pushViewController(viewController)
                }
                
            }
            
            
        }else {
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let webApi : TransactionAPI = TransactionAPI()
            
            //print(product.fID)
            
            if(product.fID == Constants.FID.ZONG_MINILOAD_FID) {
                
                webApi.BalanceTopupInfoPostRequest(
                    Constants.CommandId.BALANCE_TOPUP_INFO,
                    reqTime: currentTime,
                    DTID: Constants.AppConfig.DTID_KEY,
                    PID: product.id!,
                    TMOB: firstTextField.text!,
                    TXAM: secondTextField.text!,
                    onSuccess:{(data) -> () in
                        self.response = XMLParser.moneyTransferInfoXMLParsing(data)
                        //print(self.response)
                        
                        if(self.response.0.msg != nil){
                            self.hideLoadingView()
                            if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                            }else{
                                self.hideLoadingView()
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            }
                        }else if(self.response.1.msg != nil){
                            self.hideLoadingView()
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            
                        }else{
                            DispatchQueue.main.async {
                                self.hideLoadingView()
                                let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "ConfirmationVC") as! ConfirmationVC
                                viewController.responseDict = self.response.2
                                viewController.product = self.product
                                viewController.isMpinSetLater = self.isMpinSetLater
                                self.pushViewController(viewController)
                                
                            }
                            
                        }
                        
                },
                    onFailure: {(reason) ->() in
                        //print("Failure")
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                        self.hideLoadingView()
                })
                
            }
            else if(product.fID == Constants.FID.COLLECTION_PAYMENT) {
                
                var CNSMRNO: String = ""
                var TXAM: String = ""
                
                if(product.inrequired == "0"){
                    TXAM = firstTextField.text!
                }else if(product.inrequired == "1"){
                    CNSMRNO = firstTextField.text!
                }
                
                webApi.CollectionPaymentInfoPostRequest(
                    Constants.CommandId.COLLECTION_PAYMENT_INFO,
                    reqTime: currentTime,
                    DTID: Constants.AppConfig.DTID_KEY,
                    PID: product.id!,
                    CNSMRNO: CNSMRNO,
                    TXAM: TXAM,
                    onSuccess:{(data) -> () in
                        self.response = XMLParser.moneyTransferInfoXMLParsing(data)
                        //print(self.response)
                        
                        if(self.response.0.msg != nil){
                            self.hideLoadingView()
                            if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                            }else{
                                self.hideLoadingView()
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            }
                        }else if(self.response.1.msg != nil){
                            self.hideLoadingView()
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            
                        }else{
                            DispatchQueue.main.async {
                                self.hideLoadingView()
                                let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "ConfirmationVC") as! ConfirmationVC
                                viewController.responseDict = self.response.2
                                viewController.product = self.product
                                viewController.CNSMRNO = CNSMRNO
                                viewController.isMpinSetLater = self.isMpinSetLater
                                self.pushViewController(viewController)
                                
                            }
                        }
                },
                    onFailure: {(reason) ->() in
                        //print("Failure")
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                        self.hideLoadingView()
                })
            }
            else if(product.fID == Constants.FID.LOAN_PAYMENTS_FID) {
                
                guard let TXAM = firstTextField.text else {
                    return
                }
                webApi.LoanPaymentsInfoRequest(
                    Constants.CommandId.LOAN_PAYMENTS_INFO,
                    reqTime: currentTime,
                    DTID: Constants.AppConfig.DTID_KEY,
                    appId:"2", PID: product.id!,
                    CMOB:Customer.sharedInstance.cMob!, TXAM: TXAM,
                    CNIC: Customer.sharedInstance.cnic!,
                    onSuccess:{(data) -> () in
                        self.response = XMLParser.moneyTransferInfoXMLParsing(data)
                        //print(self.response)
                        
                        if(self.response.0.msg != nil){
                            self.hideLoadingView()
                            if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                            } else {
                                self.hideLoadingView()
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            }
                        }else if(self.response.1.msg != nil){
                            self.hideLoadingView()
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            
                        }else{
                            DispatchQueue.main.async {
                                self.hideLoadingView()
                                let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "ConfirmationVC") as! ConfirmationVC
                                viewController.responseDict = self.response.2
                                viewController.product = self.product
                                viewController.isMpinSetLater = self.isMpinSetLater
                                self.pushViewController(viewController)
                                
                            }
                        }
                },
                    onFailure: {(reason) ->() in
                        //print("Failure")
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                        self.hideLoadingView()
                })
            }
            else if(product.fID == Constants.FID.CHALLAN_NUMBER) {
                
                var CNSMRNO: String = ""
                CNSMRNO = firstTextField.text!
                
                
                webApi.ChallanPaymentInfoPostRequest(
                    Constants.CommandId.COLLECTION_PAYMENT_INFO,
                    reqTime: currentTime,
                    DTID: Constants.AppConfig.DTID_KEY,
                    PID: product.id!,
                    CSCD: CNSMRNO,
                    CMOB: Customer.sharedInstance.cMob!,
                    onSuccess:{(data) -> () in
                        self.response = XMLParser.moneyTransferInfoXMLParsing(data)
                        //print(self.response)
                        
                        if(self.response.0.msg != nil){
                            self.hideLoadingView()
                            if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                            }else{
                                self.hideLoadingView()
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            }
                        }else if(self.response.1.msg != nil){
                            self.hideLoadingView()
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            
                        }else{
                            
                            if self.response.2["BPAID"] == "1" {
                                
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText:"Challan already paid", actionType: "serverFailure", isCancelBtnHidden: true)
                                
                            } else {
                                DispatchQueue.main.async {
                                    self.hideLoadingView()
                                    let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "ConfirmationVC") as! ConfirmationVC
                                    viewController.responseDict = self.response.2
                                    viewController.product = self.product
                                    viewController.CNSMRNO = CNSMRNO
                                    viewController.isMpinSetLater = self.isMpinSetLater
                                    self.pushViewController(viewController)
                                }
                                
                            }
                            
                        }
                },
                    onFailure: {(reason) ->() in
                        //print("Failure")
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                        self.hideLoadingView()
                })
            }
            else{
                var amount = ""
                if product.denomFlag == "1" {
                    amount = prodDenom[selectedCardIndex]
                }else if product.inrequired == "0" {
                    amount = secondTextField.text!
                }
                else {
                    amount = ""
                }
                
                webApi.BillPaymentInfoPostRequest(
                    Constants.CommandId.BILLPAYMENT_INFO,
                    reqTime: currentTime,
                    DTID: Constants.AppConfig.DTID_KEY,
                    PID: product.id!,
                    AMOB: firstTextField.text!,
                    CMOB: Customer.sharedInstance.cMob!,
                    BAMT: amount,
                    CSCD:firstTextField.text!,
                    PMTTYPE: "0",//PMTTYPE!,
                    BAID: Customer.sharedInstance.bank!.id!,
                    onSuccess:{(data) -> () in
                        self.response = XMLParser.moneyTransferInfoXMLParsing(data)
                        //print(self.response)
                        
                        if(self.response.0.msg != nil){
                            if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                                self.hideLoadingView()
                            }else{
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                                self.hideLoadingView()
                            }
                        }else if(self.response.1.msg != nil){
                            
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            self.hideLoadingView()
                            
                        }else{
                            DispatchQueue.main.async {
                                self.hideLoadingView()
                                let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "ConfirmationVC") as! ConfirmationVC
                                viewController.responseDict = self.response.2
                                viewController.product = self.product
                                viewController.CSCD = self.firstTextField.text!
                                viewController.isMpinSetLater = self.isMpinSetLater
                                self.pushViewController(viewController)
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
        
    }
    
    
}

