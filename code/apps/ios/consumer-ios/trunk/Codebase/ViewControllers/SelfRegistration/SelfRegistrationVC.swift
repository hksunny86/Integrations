//
//  SelfRegistrationVC.swift
//  Timepey
//
//  Created by Adnan Ahmed on 24/09/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation
import IQKeyboardManagerSwift
import ActionSheetPicker_3_0

class SelfRegistrationVC: BaseViewController, UITextFieldDelegate{
    
    @IBOutlet weak var mobileCodeTextField: FPMBottomBorderTextField!
    @IBOutlet weak var mobileNumberTextField: FPMBottomBorderTextField!
    @IBOutlet weak var cnicTextFieldFirst: FPMBottomBorderTextField!
    @IBOutlet weak var cnicTextFieldSecond: FPMBottomBorderTextField!
    @IBOutlet weak var cnicTextFieldThird: FPMBottomBorderTextField!
    @IBOutlet weak var nextButton: UIButton!
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var cnicIssueDateTextField: FPMBottomBorderTextField!
    @IBOutlet weak var btnCnicIssueDate: UIButton!
    @IBOutlet weak var btnSIMCarrierSelector: FPADropDownButton!
    @IBOutlet weak var emailTextField: FPMBottomBorderTextField!
    @IBOutlet weak var termsAndConditions: UILabel!
    @IBOutlet weak var agreeButton: UIButton!
    
    let fPINPopupView = FPINPopupView()
    var cnicIssueDate: String = ""
    var selectedSIMCarrier: String = ""
    var allSIMCarriers = ["Jazz", "Ufone", "Telenor", "Zong", "SCOM"]
    var currentSelectedIndex = 0
    var currentSelectedDate: Date = Date()
    let calendar = Calendar.current
    var customerAccountObj = JSAccount()
    var isAgreeToTerms: Bool = Bool()
    
    override func viewDidLayoutSubviews() {
        let scrollViewHeight = cnicIssueDateTextField.frame.size.height + nextButton.frame.size.height + emailTextField.frame.size.height + mobileCodeTextField.frame.size.height + cnicTextFieldFirst.frame.size.height + btnSIMCarrierSelector.frame.size.height
        self.scrollView.contentSize = CGSize(width: self.view.frame.size.width, height: scrollViewHeight + 332)
    }
    override func viewDidLoad() {
    
        self.dismissKeyboard()
        
        self.setupHeaderBarView("Open Account", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: true)
        
        
        mobileCodeTextField.delegate = self
        mobileNumberTextField.delegate = self
        cnicTextFieldFirst.delegate = self
        cnicTextFieldSecond.delegate = self
        cnicTextFieldThird.delegate = self
        isAgreeToTerms = false
        setView()
        
        mobileCodeTextField.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        mobileNumberTextField.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        cnicTextFieldFirst.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        cnicTextFieldSecond.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        cnicTextFieldThird.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        
        mobileCodeTextField.isEnabled = true
        mobileNumberTextField.isEnabled = false
        cnicTextFieldFirst.isEnabled = true
        cnicTextFieldSecond.isEnabled = false
        cnicTextFieldThird.isEnabled = false
        btnSIMCarrierSelector.setTitle(titleString: "Jazz")
        self.selectedSIMCarrier = "Jazz"
        
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
        
        
        nextButton.layer.cornerRadius = Constants.UI.Button.CORNER_RADIUS
        
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
        emailTextField.focusedHeight()
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        mobileCodeTextField.normalHeight()
        mobileNumberTextField.normalHeight()
        cnicTextFieldFirst.normalHeight()
        cnicTextFieldSecond.normalHeight()
        cnicTextFieldThird.normalHeight()
        emailTextField.normalHeight()
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
    
    @IBAction func actAgree(_ sender: UIButton) {
        isAgreeToTerms = !isAgreeToTerms
        let imageName = isAgreeToTerms ? "btn_check_box_pressed" : "btn_check_box_normal"
        sender.setImage(UIImage(named:imageName), for: UIControl.State())
        
    }
    
    @IBAction func actCnicIssueDateTap(_ sender: Any) {
        
        var minDateComponent = calendar.dateComponents([ .day, .month ,.year],from:Date())
        minDateComponent.day = 01
        minDateComponent.month = 01
        minDateComponent.year = 2000
        let minDate = calendar.date(from:minDateComponent)
        let maxDate: Date =  Calendar.current.date(byAdding: .year, value: 0, to: Date())!
        
        ActionSheetDatePicker.show(withTitle: "CNIC Issue Date", datePickerMode: UIDatePicker.Mode.date, selectedDate:currentSelectedDate, minimumDate: minDate, maximumDate: maxDate, doneBlock: { (picker, userSelectedDate, origin) in
            self.cnicIssueDate = Utility.getDate(date: userSelectedDate as! Date, withFormat: "d-M-yyyy")
            self.cnicIssueDateTextField.text = self.cnicIssueDate
            self.currentSelectedDate = userSelectedDate as! Date
        },cancel: { (success) in
            self.cnicIssueDateTextField.text = self.cnicIssueDate == "" ? "Select CNIC Issue Date" : self.cnicIssueDate
            
        }, origin: btnCnicIssueDate)
    }
    
    
    @IBAction func actSIMCarrierSelectorTap(_ sender: Any) {
        ActionSheetStringPicker.show(withTitle: "Select SIM Carrier", rows: allSIMCarriers, initialSelection: currentSelectedIndex, doneBlock: {(picker, index, value) in
            self.selectedSIMCarrier = self.allSIMCarriers[index]
            self.btnSIMCarrierSelector.setTitle(self.selectedSIMCarrier, for: .normal)
            self.currentSelectedIndex = index
        }, cancel: {(success) in
            self.selectedSIMCarrier = self.selectedSIMCarrier == "" ? "Select SIM Carrier" : self.selectedSIMCarrier
        }, origin: btnSIMCarrierSelector)
        
    }
    
    
    @IBAction func nextButtonPressed(_ sender: UIButton) {
        
        self.dismissKeyboard()
        
        var errorMessage: String?
        
        let mobileNumText = mobileCodeTextField.text!+mobileNumberTextField.text!
        
        let cnicFirstText = cnicTextFieldFirst.text
        let cnicSecText = cnicTextFieldSecond.text
        let cnicThirdText = cnicTextFieldThird.text
        let email = emailTextField.text
        
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
        else if cnicIssueDate == "" {
            errorMessage = "CNIC Issue Date field must not be empty."
        }
        else if email == ""  {
            errorMessage = "Email Address field must not be empty."
        }
        else if !isValidEmail(candidate: emailTextField.text!) {
            errorMessage = "Please enter valid Email Address."
        }        
        
        if(errorMessage != nil){
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: errorMessage!, actionType: "serverFailure", isCancelBtnHidden: true)
        }else{
            selfRegVerificationPostRequest()
        }
        
    }
    //For email validation
    func isValidEmail(candidate: String) -> Bool {
        
        let emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}"
        var valid = NSPredicate(format: "SELF MATCHES %@", emailRegex).evaluate(with: candidate)
        if valid {
            valid = !candidate.contains("..")
        }
        return valid
    }
    
    
    func handleResponse(response:(XMLError, XMLMessage, [String : String]), MOBN: String, CNIC: String){
        DispatchQueue.main.async {
         let status = Utility.serverErrorHandling(response: response, parentView: self.view)
            if(status == true) {
                
                let viewController = UIStoryboard(name: "SelfRegistration", bundle: nil).instantiateViewController(withIdentifier: "OTPVerificationVC") as! OTPVerificationVC
                viewController.MOBN = MOBN
                viewController.CNIC = CNIC
                self.customerAccountObj.accountNo = MOBN
                self.customerAccountObj.cnic = CNIC
                self.customerAccountObj.cnicIssueDate = self.cnicIssueDate
                self.customerAccountObj.customerSIMCarrier = self.selectedSIMCarrier
                self.customerAccountObj.customerEmail = self.emailTextField.text!
                viewController.customerAccount = self.customerAccountObj
                self.pushViewController(viewController)
                
            }
            self.hideLoadingView()
        }
    }
    
    
    func selfRegVerificationPostRequest() {
        
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
            handleResponse(response: response, MOBN:mobileNumber, CNIC:cnicNumber)
        }else{
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"

            let myAccApi = MyAccountWebAPI()
            myAccApi.selfRegVerficationPostRequest(
                Constants.CommandId.SELF_REGISTRATION_VERIFICATION,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                MOBN: mobileNumber,
                CNIC: cnicNumber,
                IS_UPGRADE: "0",
                onSuccess:{(data) -> () in
                    response = XMLParser.paramTypeXMLParsing(data)
                    self.handleResponse(response: response, MOBN:mobileNumber, CNIC:cnicNumber)
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

