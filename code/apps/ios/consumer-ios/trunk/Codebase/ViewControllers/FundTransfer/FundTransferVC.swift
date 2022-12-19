//
//  MTReceiverDataVC.swift
//  Timepey
//
//  Created by Adnan Ahmed on 18/07/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import UIKit
import Foundation
import ContactsUI

class FundTransferVC: BaseViewController, UITextFieldDelegate, UIPickerViewDelegate, UIPickerViewDataSource, CNContactPickerDelegate {
    
    
    @IBOutlet weak var screenTitleLabel: UILabel!
    @IBOutlet weak var receiverTitleLabel: UILabel!
    @IBOutlet weak var receiverCNICTextField: UITextField!
    
    @IBOutlet weak var receiverMobileLabel: UILabel!
    @IBOutlet weak var receiverMobileTextField: UITextField!
    @IBOutlet weak var amountTitleLabel: UILabel!
    @IBOutlet weak var amountTextField: UITextField!
    
    @IBOutlet weak var purposeOfTranxLabel: UILabel!
    @IBOutlet weak var purposeOfTranxTextView: UITextField!
    
    
    @IBOutlet weak var nextButton: UIButton!
    @IBOutlet weak var nextButtonTopConstraint: NSLayoutConstraint!
    
    @IBOutlet weak var secondPhoneNumber: UIButton!
    @IBOutlet weak var phoneBookButton: UIButton!
    //        @IBOutlet weak var myPickerView: UIPickerView!
    var pickerDataSource = Customer.sharedInstance.tpurps
    var selectedPrpTranx: TPURPS?
    var arrTelcosProductIDs = ["60027", "60028", "60032", "60033", "60034", "60035", "60036", "60037", "60038", "60039", "2511348", "50050", "50052", "10245160", "50053"]
    
    @IBOutlet weak var receiverMobileTopConstraint: NSLayoutConstraint!
    
    var product: Product?
    var screenTitleText: String?
    var response = (XMLError(), XMLMessage(), [String:String]())
    
    
    override func viewDidLoad() {
        
        super.viewDidLoad()
        
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: false, isSignoutButtonHidden: false)
        
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard))
        view.addGestureRecognizer(tap)
        
        purposeOfTranxLabel.isHidden = true
        purposeOfTranxTextView.isHidden = true
        
        if(screenTitleText != nil){
            screenTitleLabel.text = screenTitleText!
        }
        
        setView()
        
        if(product?.fID == Constants.FID.ACCTOCASH_FID) {
            purposeOfTranxLabel.isHidden = false
            purposeOfTranxTextView.isHidden = false
            pickerViewIntegration()
            if((pickerDataSource?.count)! > 0) {
                purposeOfTranxTextView.text = pickerDataSource![0].name!
                selectedPrpTranx = pickerDataSource![0]
            }
            
        }else if(product?.fID == Constants.FID.BBTOCORE_FID){
            receiverTitleLabel.text = "Receiver Mobile Number"
            receiverMobileLabel.text = "Account Number"
            secondPhoneNumber.isHidden = false
            phoneBookButton.isHidden = true
        }
        
        
        
        amountTitleLabel.text = "Amount"
        
    }
    
    override func viewWillLayoutSubviews() {
        if(purposeOfTranxLabel.isHidden == true){
            nextButtonTopConstraint.constant = -50
        }
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        if(product?.fID == Constants.FID.ACCTOACC_FID || product?.fID == Constants.FID.RETAILPAYMENT_FID) {
            receiverTitleLabel.isHidden = true
            receiverCNICTextField.isHidden = true
            
            switch UIDevice.current.userInterfaceIdiom {
            case .pad:
                receiverMobileTopConstraint.constant = -105
            case .phone:
                receiverMobileTopConstraint.constant = -68
            default:
                break
            }
            
        }
        else if (product?.fID == Constants.FID.TRANSFER_HRA_TO_WALLET) {
            receiverTitleLabel.isHidden = true
            receiverCNICTextField.isHidden = true
            receiverMobileLabel.isHidden = true
            receiverMobileTextField.isHidden = true
            
            switch UIDevice.current.userInterfaceIdiom {
            case .pad:
                receiverMobileTopConstraint.constant = -155
            case .phone:
                receiverMobileTopConstraint.constant = -128
            default:
                break
            }
        }
        if(product?.fID == Constants.FID.BBTOCORE_FID || product?.fID == Constants.FID.ACCTOACC_FID || product?.fID == Constants.FID.TRANSFER_HRA_TO_WALLET){
            var frame = nextButton.frame
            switch UIDevice.current.userInterfaceIdiom {
            case .pad:
                frame.origin.y = CGFloat(amountTextField.frame.origin.y + amountTextField.frame.size.height + 50)
            case .phone:
                frame.origin.y = CGFloat(amountTextField.frame.origin.y + amountTextField.frame.size.height + 30)
            default:
                break
            }
            nextButton.frame = frame
        }
    }
    
    func setView(){
        
        receiverCNICTextField.keyboardType = UIKeyboardType.numberPad
        receiverMobileTextField.keyboardType = UIKeyboardType.numberPad
        amountTextField.keyboardType = UIKeyboardType.numberPad
        
        
        receiverCNICTextField.delegate = self
        receiverMobileTextField.delegate = self
        amountTextField.delegate = self
        purposeOfTranxTextView.delegate = self
        
        
        receiverCNICTextField.layer.cornerRadius = 2
        receiverMobileTextField.layer.cornerRadius = 2
        amountTextField.layer.cornerRadius = 2
        purposeOfTranxTextView.layer.cornerRadius = 2
        nextButton.layer.cornerRadius = 2
        
        receiverCNICTextField.layer.borderWidth = 0.7
        receiverMobileTextField.layer.borderWidth = 0.7
        amountTextField.layer.borderWidth = 0.7
        purposeOfTranxTextView.layer.borderWidth = 0.7
        
        receiverCNICTextField.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        receiverMobileTextField.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        amountTextField.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        purposeOfTranxTextView.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        
        
        let paddingForFirst = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.receiverCNICTextField.frame.size.height))
        //Adding the padding to the second textField
        receiverCNICTextField.leftView = paddingForFirst
        receiverCNICTextField.leftViewMode = UITextField.ViewMode .always
        
        let paddingForSec = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.receiverMobileTextField.frame.size.height))
        //Adding the padding to the second textField
        receiverMobileTextField.leftView = paddingForSec
        receiverMobileTextField.leftViewMode = UITextField.ViewMode .always
        
        let paddingForFourth = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.amountTextField.frame.size.height))
        //Adding the padding to the second textField
        amountTextField.leftView = paddingForFourth
        amountTextField.leftViewMode = UITextField.ViewMode .always
        
        let paddingForFifth = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.purposeOfTranxTextView.frame.size.height))
        //Adding the padding to the second textField
        purposeOfTranxTextView.leftView = paddingForFifth
        purposeOfTranxTextView.leftViewMode = UITextField.ViewMode .always
        
        if arrTelcosProductIDs.contains((product!.id)!) {
            phoneBookButton.isHidden = false
        }
        else {
            phoneBookButton.isHidden = true
        }
        
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
    
    func pickerViewIntegration(){
        let picker: UIPickerView
        picker = UIPickerView(frame: CGRect(x: 0, y: 200, width: view.frame.width, height: 300))
        picker.backgroundColor = UIColor.white
        
        picker.showsSelectionIndicator = true
        picker.delegate = self
        picker.dataSource = self
        
        let toolBar = UIToolbar()
        toolBar.barStyle = UIBarStyle.default
        toolBar.isTranslucent = true
        toolBar.tintColor = UIColor(red: 76/255, green: 217/255, blue: 100/255, alpha: 1)
        toolBar.sizeToFit()
        
        let doneButton = UIBarButtonItem(title: "Done", style: UIBarButtonItem.Style.plain, target: self, action: #selector(FundTransferVC.donePicker))
        let spaceButton = UIBarButtonItem(barButtonSystemItem: UIBarButtonItem.SystemItem.flexibleSpace, target: nil, action: nil)
        let cancelButton = UIBarButtonItem(title: "Cancel", style: UIBarButtonItem.Style.plain, target: self, action: #selector(FundTransferVC.donePicker))
        
        toolBar.setItems([cancelButton, spaceButton, doneButton], animated: false)
        toolBar.isUserInteractionEnabled = true
        
        purposeOfTranxTextView.inputView = picker
        purposeOfTranxTextView.inputAccessoryView = toolBar
    }
    
    @objc func donePicker(){
        purposeOfTranxTextView.resignFirstResponder()
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return pickerDataSource!.count;
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return pickerDataSource![row].name
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        purposeOfTranxTextView.text = pickerDataSource![row].name
        selectedPrpTranx = pickerDataSource![row]
    }
    
    //MASK: TextField Delegate
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if (textField.tag == 1){
            if(product?.fID == Constants.FID.ACCTOCASH_FID){
                return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 13)
            }else if(product?.fID == Constants.FID.BBTOCORE_FID) {
                return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 11)
            }
            else{
                return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 15)
            }
        }else if(textField.tag == 2){
            if(product?.fID == Constants.FID.BBTOCORE_FID) {
                return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 20)
            }else{
                return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 11)
            }
        }else{
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 7)
        }
    }
    
    func validateAccountNo(_ textField: UITextField) -> Bool{
        let minAccNo = Double(1)
        let maxAccNo = Double(20)
        let length = Double(textField.text!.count)
        if(length < minAccNo || length > maxAccNo){
            return false
        }else{
            return true
        }
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
                if product?.fID == Constants.FID.BBTOCORE_FID {
                    self.receiverCNICTextField.text = newPhoneNumber
                } else {
                    self.receiverMobileTextField.text = newPhoneNumber
                }
                
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
    
    
    @IBAction func phoneBookPressed(_ sender: Any) {
        let contacVC = CNContactPickerViewController()
        contacVC.delegate = self
        self.present(contacVC, animated: true, completion: nil)
    }
    
    
    @IBAction func nextButtonPressed(_ sender: UIButton) {
        
        self.dismissKeyboard()
        
        var errorMessage: String?
        
        if(product?.fID == Constants.FID.ACCTOACC_FID || product?.fID == Constants.FID.RETAILPAYMENT_FID){
            
            
            let mobileNumText = receiverMobileTextField.text
            let amountText = amountTextField.text
            
            if(mobileNumText == nil || mobileNumText == ""){
                errorMessage = "Mobile number field must not be empty."
            }else if(mobileNumText![mobileNumText!.index(mobileNumText!.startIndex, offsetBy: 0)] != "0" || mobileNumText![mobileNumText!.index(mobileNumText!.startIndex, offsetBy: 1)] != "3"){
                errorMessage = "Mobile number must start with 03."
            }else if(mobileNumText?.count != 11){
                errorMessage = "Mobile number length should be of 11 digits."
            }
            else if(amountText == nil || amountText == ""){
                errorMessage = "Amount field must not be empty."
            }
            else if(Int(amountText!)! < Constants.Validation.TextField.AMOUNT_MIN || Int(amountText!)! > Constants.Validation.TextField.AMOUNT_MAX){
                errorMessage = "Invalid amount entered."
            }
            
        }else if(product?.fID == Constants.FID.ACCTOCASH_FID){
            let cnicText = receiverCNICTextField.text
            let mobileNumText = receiverMobileTextField.text
            let amountText = amountTextField.text
            
            if(cnicText == nil || cnicText == ""){
                errorMessage = "CNIC number field must not be empty."
            }else if(cnicText?.count != 13){
                errorMessage = "CNIC number length should be of 13 digits."
            }
                
            else if(mobileNumText == nil || mobileNumText == ""){
                errorMessage = "Mobile number field must not be empty."
            }else if(mobileNumText![mobileNumText!.index(mobileNumText!.startIndex, offsetBy: 0)] != "0" || mobileNumText![mobileNumText!.index(mobileNumText!.startIndex, offsetBy: 1)] != "3"){
                errorMessage = "Mobile number must start with 03."
            }else if(mobileNumText?.count != 11){
                errorMessage = "Mobile number length should be of 11 digits."
            }
                
                
            else if(amountText == nil || amountText == ""){
                errorMessage = "Amount field must not be empty."
            }
            else if(Int(amountText!)! < Constants.Validation.TextField.AMOUNT_MIN || Int(amountText!)! > Constants.Validation.TextField.AMOUNT_MAX){
                errorMessage = "Invalid amount entered."
            }
        }
        else if(product?.fID == Constants.FID.BBTOCORE_FID){
            let cnicText = receiverMobileTextField.text
            let mobileNumText = receiverCNICTextField.text
            let amountText = amountTextField.text
            
            
            if(mobileNumText == nil || mobileNumText == ""){
                errorMessage = "Mobile number field must not be empty."
            }else if(mobileNumText![mobileNumText!.index(mobileNumText!.startIndex, offsetBy: 0)] != "0" || mobileNumText![mobileNumText!.index(mobileNumText!.startIndex, offsetBy: 1)] != "3"){
                errorMessage = "Mobile number must start with 03."
            }else if(mobileNumText?.count != 11){
                errorMessage = "Mobile number length should be of 11 digits."
            }
                
            else if(cnicText == nil || cnicText == ""){
                errorMessage = "Account number field must not be empty."
            }else if(validateAccountNo(receiverMobileTextField) == false){
                errorMessage = "Invalid account number entered."
            }
                
            else if(amountText == nil || amountText == ""){
                errorMessage = "Amount field must not be empty."
            }
            else if(Int(amountText!)! < Constants.Validation.TextField.AMOUNT_MIN || Int(amountText!)! > Constants.Validation.TextField.AMOUNT_MAX){
                errorMessage = "Invalid amount entered."
            }
        }
        else if(product?.fID == Constants.FID.TRANSFER_HRA_TO_WALLET) {
            let amountText = amountTextField.text
            
            if(amountText == nil || amountText == ""){
                errorMessage = "Amount field must not be empty."
            }
            else if(Int(amountText!)! < Constants.Validation.TextField.AMOUNT_MIN || Int(amountText!)! > Constants.Validation.TextField.AMOUNT_MAX){
                
                errorMessage = "Invalid amount entered."
            }
        }
        else{
            let cnicText = receiverCNICTextField.text
            let mobileNumText = receiverMobileTextField.text
            let amountText = amountTextField.text
            
            if(cnicText == nil || cnicText == ""){
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
        }
        
        
        if(errorMessage != nil){
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: errorMessage!, actionType: "serverFailure", isCancelBtnHidden: true)
        }else{
            if(product?.fID == Constants.FID.ACCTOACC_FID){
                MTInfoAccToAccPostRequest()
            }else if(product?.fID == Constants.FID.ACCTOCASH_FID){
                MTInfoAccToCashPostRequest()
            }else if(product?.fID == Constants.FID.RETAILPAYMENT_FID){
                retailPaymentPostRequest()
            }else if(product?.fID == Constants.FID.BBTOCORE_FID){
                MTInfoBBToCorePostRequest()
            }
            else if (product?.fID == Constants.FID.TRANSFER_HRA_TO_WALLET) {
                MTInfoHRATOWalletPostRequest()
            }
        }
        
    }
    
    @IBAction func signOutPressed(_ sender: UIButton) {
        super.signoutCustomer()
    }
    
    @IBAction func backButtonPressed(_ sender: UIButton) {
        self.popViewController()
    }
    
    
    func MTInfoAccToAccPostRequest() {
        
        //var responseArray = [[String:String]]()
        
        self.showLoadingView()
        
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                xmlPath = Bundle.main.path(forResource: "Command-182-MT AccToAcc Info", ofType: "xml"),
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
                viewController.isMpinSetLater = self.isMpinSetLater
                self.pushViewController(viewController)
                
            }
            self.hideLoadingView()
            
        }else{
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let webApi : TransactionAPI = TransactionAPI()
            
            
            
            webApi.AccToAccInfoPostRequest(
                Constants.CommandId.MT_ACCTOACC_INFO,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                PID: (product?.id!)!,
                RCMOB: receiverMobileTextField.text!,
                CMOB: Customer.sharedInstance.cMob!,
                TXAM: amountTextField.text!,
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
    
    func MTInfoAccToCashPostRequest() {
        self.showLoadingView()
        
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                xmlPath = Bundle.main.path(forResource: "Command-69-MT AccToCash Info", ofType: "xml"),
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
                viewController.selectedPrpTranx = self.selectedPrpTranx?.name
                viewController.isMpinSetLater = self.isMpinSetLater
                self.pushViewController(viewController)
            }
            self.hideLoadingView()
            
        }else{
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let webApi : TransactionAPI = TransactionAPI()
            
            
            
            webApi.AccToCashInfoPostRequest(
                Constants.CommandId.MT_ACCTOCASH_INFO,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                PID: (product?.id!)!,
                RWMOB: receiverMobileTextField.text!,
                CMOB: Customer.sharedInstance.cMob!,
                RWCNIC: receiverCNICTextField.text!,
                TXAM: amountTextField.text!,
                TRX_PUR: "",//(selectedPrpTranx?.name)!,
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
                        let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "ConfirmationVC") as! ConfirmationVC
                        viewController.responseDict = self.response.2
                        viewController.product = self.product
                        viewController.selectedPrpTranx = self.selectedPrpTranx?.name
                        viewController.isMpinSetLater = self.isMpinSetLater
                        self.pushViewController(viewController)
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
    
    func retailPaymentPostRequest() {
        
        self.showLoadingView()
        
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                xmlPath = Bundle.main.path(forResource: "Command-81-RetailPayment", ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                
                else { return }
            
            //let newStr = String(data: data, encoding: NSUTF8StringEncoding)
            //print(newStr)
            self.response = XMLParser.retailPaymentInfoXMLParsing(data)
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
                viewController.isMpinSetLater = self.isMpinSetLater
                self.pushViewController(viewController)
            }
            self.hideLoadingView()
            
        }else{
            
            //            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            //
            //            let webApi : TransactionAPI = TransactionAPI()
            
            //print(product?.id)
            
            //            webApi.retailPaymentInfoPostRequest(
            //                Constants.CommandId.RETAILPAYMENT_INFO,
            //                reqTime: currentTime,
            //                DTID: Constants.AppConfig.DTID_KEY,
            //                PID: (product?.id!)!,
            //                AMOB: receiverMobileTextField.text!,
            //                CMOB: Customer.sharedInstance.cMob!,
            //                TXAM: amountTextField.text!,
            //                onSuccess:{(data) -> () in
            //                    //print(data)
            //                    self.response = XMLParser.moneyTransferInfoXMLParsing(data)
            //                    //print(self.response)
            //                    if(self.response.0.msg != nil){
            //                        if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
            //                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
            //                        }else{
            //                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
            //                        }
            //                    }else if(self.response.1.msg != nil){
            //
            //                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
            //
            //                    }else{
            //                        let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "ConfirmationVC") as! ConfirmationVC
            //                        viewController.responseDict = self.response.2
            //                        viewController.product = self.product
            //                        self.pushViewController(viewController)
            //                    }
            //                    self.hideLoadingView()
            //            },
            //                onFailure: {(reason) ->() in
            //                    print("Failure")
            //                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
            //                    self.hideLoadingView()
            //            })
        }
    }
    
    func MTInfoBBToCorePostRequest() {
        
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
                viewController.isMpinSetLater = self.isMpinSetLater
                self.pushViewController(viewController)
            }
            self.hideLoadingView()
            
        }else{
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let webApi : TransactionAPI = TransactionAPI()
            
            webApi.BBToCoreInfoPostRequest(
                Constants.CommandId.MT_BBTOCORE_INFO,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                PID: (product?.id!)!,
                RCMOB: receiverCNICTextField.text!,
                CMOB: Customer.sharedInstance.cMob!,
                COREACID: receiverMobileTextField.text!,
                TXAM: amountTextField.text!,
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
                        let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "ConfirmationVC") as! ConfirmationVC
                        viewController.responseDict = self.response.2
                        viewController.product = self.product
                        viewController.isMpinSetLater = self.isMpinSetLater
                        self.pushViewController(viewController)
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
    
    func MTInfoHRATOWalletPostRequest() {
        
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
                viewController.isMpinSetLater = self.isMpinSetLater
                self.pushViewController(viewController)
            }
            self.hideLoadingView()
            
        }else{
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let webApi : TransactionAPI = TransactionAPI()
            
            webApi.hraToWalletInfoPostRequest(
                Constants.CommandId.MT_HRATOWALLET_INFO,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                PID: (product?.id!)!,
                TXAM: amountTextField.text!,
                onSuccess:{(data) -> () in
                    
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
                        viewController.isMpinSetLater = self.isMpinSetLater
                        self.pushViewController(viewController)
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
