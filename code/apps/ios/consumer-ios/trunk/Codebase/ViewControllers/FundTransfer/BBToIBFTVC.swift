//
//  BBToIBFT_InitialVC.swift
//  Timepey
//
//  Created by Adnan Ahmed on 15/08/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import UIKit
import Foundation
import ContactsUI

class BBToIBFTVC: BaseViewController, UITextFieldDelegate, UIPickerViewDelegate, UIPickerViewDataSource, CNContactPickerDelegate {
    
    
    @IBOutlet weak var headerLabelView: HeaderTitleView!
    @IBOutlet weak var screenTitleLabel: UILabel!
    @IBOutlet weak var bankTextField: UITextField!
    @IBOutlet weak var accHelperMsgLabel: UILabel!
    @IBOutlet weak var accountNumberTextField: UITextField!
    @IBOutlet weak var rcMobNumLabel: UILabel!
    @IBOutlet weak var rcMobNumTextField: UITextField!
    @IBOutlet weak var amountTitleLabel: UILabel!
    @IBOutlet weak var amountTextField: UITextField!
    @IBOutlet weak var nextButton: UIButton!
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var paymentPurposeTextField: UITextField!
    @IBOutlet weak var headerLabelHC: NSLayoutConstraint!
    
    @IBOutlet weak var phoneBookButton: UIButton!
    var selectedBank: Bank?
    var product: Product?
    var screenTitleText: String?
    var response = (XMLError(), XMLMessage(), [String:String]())
    var bankPicker: UIPickerView = UIPickerView()
    var pickerDataSource = Customer.sharedInstance.mbanks
    var purposeDataSource = Customer.sharedInstance.tpurps
    var tranxPurposeCode = ""
    
    override func viewDidLoad() {
        
        super.viewDidLoad()
        
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: false, isSignoutButtonHidden: false)
        
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard))
        view.addGestureRecognizer(tap)
        bankTextField.isUserInteractionEnabled = false
        paymentPurposeTextField.isUserInteractionEnabled = false
        pickerViewIntegration()
        setView()
        
        
        if((pickerDataSource?.count)! > 0) {
            bankTextField.text = pickerDataSource![0].name!
            if(pickerDataSource![0].minLength! == pickerDataSource![0].maxLength!){
                accHelperMsgLabel.text = "Please enter \(pickerDataSource![0].minLength!) digit \(pickerDataSource![0].name!) Account Number."
            }else{
                accHelperMsgLabel.text = "Please enter \(pickerDataSource![0].minLength!) to \(pickerDataSource![0].maxLength!) digit \(pickerDataSource![0].name!) Account Number."
            }
            selectedBank = pickerDataSource![0]
        }
        
        if (purposeDataSource?.count)! > 0 {
            paymentPurposeTextField.text = purposeDataSource![0].name!
        }
        
        amountTitleLabel.text = "Amount"
        tranxPurposeCode = purposeDataSource![0].id!
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        if(screenTitleText != nil){
            screenTitleLabel.text = screenTitleText!
        }
    }
    
    override func viewDidLayoutSubviews() {
        self.scrollView.contentSize = CGSize(width: self.view.frame.size.width, height: self.view.frame.size.height + headerLabelHC.constant + nextButton.frame.size.height)
    }
    
    func setView(){
        
        
        accountNumberTextField.keyboardType = UIKeyboardType.numberPad
        rcMobNumTextField.keyboardType = UIKeyboardType.numberPad
        amountTextField.keyboardType = UIKeyboardType.numberPad
        
        
        bankTextField.delegate = self
        accountNumberTextField.delegate = self
        rcMobNumTextField.delegate = self
        amountTextField.delegate = self
        paymentPurposeTextField.delegate = self
        
        bankTextField.layer.cornerRadius = 2
        accountNumberTextField.layer.cornerRadius = 2
        rcMobNumTextField.layer.cornerRadius = 2
        amountTextField.layer.cornerRadius = 2
        nextButton.layer.cornerRadius = 2
        paymentPurposeTextField.layer.cornerRadius = 2
        
        bankTextField.layer.borderWidth = 0.7
        accountNumberTextField.layer.borderWidth = 0.7
        rcMobNumTextField.layer.borderWidth = 0.7
        amountTextField.layer.borderWidth = 0.7
        paymentPurposeTextField.layer.borderWidth = 0.7
        
        bankTextField.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        accountNumberTextField.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        rcMobNumTextField.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        amountTextField.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        paymentPurposeTextField.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        
        let paddingForFirst = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.bankTextField.frame.size.height))
        //Adding the padding to the second textField
        bankTextField.leftView = paddingForFirst
        bankTextField.leftViewMode = UITextField.ViewMode .always
        
        let paddingForSec = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.accountNumberTextField.frame.size.height))
        //Adding the padding to the second textField
        accountNumberTextField.leftView = paddingForSec
        accountNumberTextField.leftViewMode = UITextField.ViewMode .always
        
        let paddingForThird = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.rcMobNumTextField.frame.size.height))
        //Adding the padding to the second textField
        rcMobNumTextField.leftView = paddingForThird
        rcMobNumTextField.leftViewMode = UITextField.ViewMode .always
        
        let paddingForFourth = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.amountTextField.frame.size.height))
        //Adding the padding to the second textField
        amountTextField.leftView = paddingForFourth
        amountTextField.leftViewMode = UITextField.ViewMode .always
        
        let paddingForFifth = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.amountTextField.frame.size.height))
        //Adding the padding to the second textField
        paymentPurposeTextField.leftView = paddingForFifth
        paymentPurposeTextField.leftViewMode = UITextField.ViewMode .always

    }
    
    func validateAmount(_ textField: UITextField) -> Bool{
        let minAmount = Double((product?.minamt)!)
        let maxAmount = Double((product?.maxamt)!)
        let amount = Double(textField.text!)
        if(amount! < minAmount! || amount! > maxAmount!){
            return false
        }else{
            return true
        }
    }
    
//Mask: PickerView Integration
    func pickerViewIntegration() {
        
        guard let banksCount = Customer.sharedInstance.mbanks?.count else {
            return
        }
        if banksCount > 0 {
            bankPicker = UIPickerView(frame: CGRect(x: 0, y: 200, width: view.frame.width, height: 300))
            bankPicker.backgroundColor = UIColor.white
            
            bankPicker.showsSelectionIndicator = true
            bankPicker.delegate = self
            bankPicker.dataSource = self
            
            let toolBar = UIToolbar()
            toolBar.barStyle = UIBarStyle.default
            toolBar.isTranslucent = true
            toolBar.tintColor = UIColor(red: 76/255, green: 217/255, blue: 100/255, alpha: 1)
            toolBar.sizeToFit()
            
            let doneButton = UIBarButtonItem(title: "Done", style: UIBarButtonItem.Style.plain, target: self, action: #selector(BBToIBFTVC.donePicker))
            let spaceButton = UIBarButtonItem(barButtonSystemItem: UIBarButtonItem.SystemItem.flexibleSpace, target: nil, action: nil)
            let cancelButton = UIBarButtonItem(title: "Cancel", style: UIBarButtonItem.Style.plain, target: self, action: #selector(BBToIBFTVC.donePicker))
            
            toolBar.setItems([cancelButton, spaceButton, doneButton], animated: false)
            toolBar.isUserInteractionEnabled = true
            
            bankTextField.inputView = bankPicker
            bankTextField.inputAccessoryView = toolBar
            bankTextField.isUserInteractionEnabled = true
        }
        
        guard let purposeCount = Customer.sharedInstance.tpurps?.count else {
            return
        }
        
        if purposeCount > 0 {
            let picker2: UIPickerView
            picker2 = UIPickerView(frame: CGRect(x: 0, y: 200, width: view.frame.width, height: 300))
            picker2.backgroundColor = UIColor.white
            
            picker2.showsSelectionIndicator = true
            picker2.delegate = self
            picker2.dataSource = self
            
            let toolBar2 = UIToolbar()
            toolBar2.barStyle = UIBarStyle.default
            toolBar2.isTranslucent = true
            toolBar2.tintColor = UIColor(red: 76/255, green: 217/255, blue: 100/255, alpha: 1)
            toolBar2.sizeToFit()
            
            let doneButton2 = UIBarButtonItem(title: "Done", style: UIBarButtonItem.Style.plain, target: self, action: #selector(BBToIBFTVC.donePickerPurposeOfPayment))
            let spaceButton2 = UIBarButtonItem(barButtonSystemItem: UIBarButtonItem.SystemItem.flexibleSpace, target: nil, action: nil)
            let cancelButton2 = UIBarButtonItem(title: "Cancel", style: UIBarButtonItem.Style.plain, target: self, action: #selector(BBToIBFTVC.donePickerPurposeOfPayment))
            
            toolBar2.setItems([cancelButton2, spaceButton2, doneButton2], animated: false)
            toolBar2.isUserInteractionEnabled = true
            
            paymentPurposeTextField.inputView = picker2
            paymentPurposeTextField.inputAccessoryView = toolBar2
            paymentPurposeTextField.isUserInteractionEnabled = true
        }
    
    }
    
    @objc func donePicker() {
        bankTextField.resignFirstResponder()
    }
    @objc func donePickerPurposeOfPayment() {
        paymentPurposeTextField.resignFirstResponder()
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        if pickerView == bankPicker {
            return pickerDataSource!.count
        }
        else {
            return purposeDataSource!.count
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if pickerView == bankPicker {
            return pickerDataSource![row].name
        }
        else {
            return purposeDataSource![row].name
            
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        if pickerView == bankPicker {
            bankTextField.text = pickerDataSource![row].name
            selectedBank = pickerDataSource![row]
            
            if(pickerDataSource![row].minLength! == pickerDataSource![row].maxLength!) {
                accHelperMsgLabel.text = "Please enter \(pickerDataSource![row].minLength!) digit \(pickerDataSource![row].name!) Account Number."
            }
            else {
                accHelperMsgLabel.text = "Please enter \(pickerDataSource![row].minLength!) to \(pickerDataSource![row].maxLength!) digit \(pickerDataSource![row].name!) Account Number."
            }
            accountNumberTextField.text = ""
        }
        else {
            paymentPurposeTextField.text = purposeDataSource![row].name
            tranxPurposeCode = purposeDataSource![row].id!
        }
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if (textField.tag == 1) {
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_ALPHABETS, length: 30)
            
        }
        else if(textField.tag == 2) {
            if(selectedBank?.maxLength != nil && selectedBank?.maxLength != "") {
                return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: Int((selectedBank?.maxLength)!)!)
            }else{
                return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: Constants.Validation.TextField.MAX_CONSUMER_LEN)
            }
        }
        else if(textField.tag == 3) {
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 11)
        }
        else {
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 7)
        }
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        
        if(textField == bankTextField){
            accountNumberTextField.becomeFirstResponder()
        }else if(textField == accountNumberTextField){
            bankTextField.becomeFirstResponder()
        }else if(textField == rcMobNumTextField){
            rcMobNumTextField.becomeFirstResponder()
        }else if(textField == amountTextField){
            rcMobNumTextField.becomeFirstResponder()
        }
        
        
        return true
    }
    
    // MARK: Delegate method CNContectPickerDelegate
    func contactPicker(_ picker: CNContactPickerViewController, didSelect contact: CNContact) {
        print(contact.phoneNumbers)
        let numbers = contact.phoneNumbers.first
        
        let number = String(numbers?.value.stringValue ?? "")
        let numberArray = number.map { String ($0) }
        let numbersOnly = numberArray.filter { Int($0) != nil || $0.contains("+")}
        let phoneNumber = numbersOnly.joined(separator: "")
        if phoneNumber.starts(with: "03") || phoneNumber.starts(with: "+92") || phoneNumber.starts(with: "0092"){
            let newPhoneNumber = Utility.addPhoneNoCountryCode(phoneNo: phoneNumber)
            if newPhoneNumber!.count == 11 {
                self.rcMobNumTextField.text = newPhoneNumber
            }
            else {
                self.showMessage("Please select a valid number.")
            }
        }
        else {
            self.showMessage("Please select a valid number.")
        }
          
    }

    func contactPickerDidCancel(_ picker: CNContactPickerViewController) {
        self.dismiss(animated: true, completion: nil)
    }
    
    
    @IBAction func actPhoneBook(_ sender: Any) {
        
        let contacVC = CNContactPickerViewController()
        contacVC.delegate = self
        self.present(contacVC, animated: true, completion: nil)
        
    }
    
    @IBAction func actPurposeOfPayment(_ sender: Any) {
        
        
    }
    
    
    
    @IBAction func nextButtonPressed(_ sender: UIButton) {
        
        self.dismissKeyboard()
        
        var errorMessage: String?
        
        let bankText = bankTextField.text
        let accNumText = accountNumberTextField.text
        let mobileNumText = rcMobNumTextField.text
        let amountText = amountTextField.text
        
        if(bankText == nil || bankText == ""){
            errorMessage = "Bank field must not be empty."
        }
        
            
        else if(accNumText == nil || accNumText == ""){
            errorMessage = "Account number field must not be empty."
        }
        
            
        else if(mobileNumText == nil || mobileNumText == ""){
            errorMessage = "Mobile number field must not be empty."
        }else if(mobileNumText?.count != 11){
            errorMessage = "Mobile number length should be of 11 digits."
        }else if(mobileNumText![mobileNumText!.index(mobileNumText!.startIndex, offsetBy: 0)] != "0" || mobileNumText![mobileNumText!.index(mobileNumText!.startIndex, offsetBy: 1)] != "3"){
            errorMessage = "Mobile number must start with 03."
        }
        
            
        else if(amountText == nil || amountText == ""){
            errorMessage = "Amount field must not be empty."
        }
        else if(Int(amountText!)! < Constants.Validation.TextField.AMOUNT_MIN || Int(amountText!)! > Constants.Validation.TextField.AMOUNT_MAX){
            errorMessage = "Invalid amount entered."
        }
        
        
        if(errorMessage != nil){
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: errorMessage!, actionType: "serverFailure", isCancelBtnHidden: true)
        }else{
            MTInfoBBToIBFTPostRequest()
        }
    }
    
    @IBAction func signOutPressed(_ sender: UIButton) {
        super.signoutCustomer()
    }
    
    @IBAction func backButtonPressed(_ sender: UIButton) {
        self.popViewController()
    }
    
    

    func MTInfoBBToIBFTPostRequest() {
        
        self.showLoadingView()
        
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                xmlPath = Bundle.main.path(forResource: "Command-191-MT BBToCore Info", ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                
                else { return }
            //let newStr = String(data: data, encoding: NSUTF8StringEncoding)
            //print(newStr)
            self.response = XMLParser.moneyTransferInfoXMLParsing(data)
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
                let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "ConfirmationVC") as! ConfirmationVC
                viewController.responseDict = self.response.2
                viewController.product = self.product
                viewController.bankID = (self.selectedBank?.imd)!
                viewController.bankName = (self.selectedBank?.name)!
                viewController.isMpinSetLater = self.isMpinSetLater
                self.pushViewController(viewController)
            }
            self.hideLoadingView()
            
        }else{
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let webApi : TransactionAPI = TransactionAPI()
            
            webApi.BBToIBFTInfoPostRequest(
                Constants.CommandId.MT_BBTOCORE_INFO,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                PID: (product?.id!)!,
                RCMOB: rcMobNumTextField.text!,
                COREACID: accountNumberTextField.text!,
                TXAM: amountTextField.text!,
                CMOB: Customer.sharedInstance.cMob!,
                BAIMD: (self.selectedBank?.imd)!,
                PMTTYPE: "0",
                BENE_BANK_NAME: (self.selectedBank?.name)!,
                TRANS_PURPOSE_CODE: self.tranxPurposeCode,
                onSuccess:{(data) -> () in 
                    //print(data)
                    self.response = XMLParser.moneyTransferInfoXMLParsing(data)
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
                            let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "ConfirmationVC") as! ConfirmationVC
                            viewController.responseDict = self.response.2
                            viewController.product = self.product
                            viewController.bankID = (self.selectedBank?.imd)!
                            viewController.bankName = (self.selectedBank?.name)!
                            viewController.selectedPrpTranxCode = self.tranxPurposeCode
                            viewController.isMpinSetLater = self.isMpinSetLater
                            self.pushViewController(viewController)
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

