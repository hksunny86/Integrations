//
//  OTPPopup.swift
//  Timepey
//
//  Created by Adnan Ahmed on 05/09/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

//
//  FinancialPinPopup.swift
//  Timepey
//
//  Created by Adnan Ahmed on 29/06/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import UIKit
import Foundation

class OTPPinPopup: BaseViewController, UITextFieldDelegate {
    
    @IBOutlet weak var transparentView: UIView!
    @IBOutlet weak var parentView: UIView!
    @IBOutlet weak var messageTextField: UILabel!
    
    @IBOutlet weak var enterPinLabel: UILabel!
    @IBOutlet weak var inputParentView: UIView!
    @IBOutlet weak var firstTextField: UITextField!
    @IBOutlet weak var secondTextField: UITextField!
    @IBOutlet weak var thirdTextField: UITextField!
    @IBOutlet weak var fourthTextField: UITextField!
    
    
    @IBOutlet weak var okButton: UIButton!
    
    @IBOutlet weak var nextBtnTrailingConstraint: NSLayoutConstraint!
    
    @IBOutlet weak var cancelButton: UIButton!
    @IBOutlet weak var resendButton: UIButton!
    
    weak var delegate: OTPDelegate?
    
    let userDefault = UserDefaults.standard
    var encryptedPin: String?
    var response = (XMLError(), XMLMessage(), [String:String]())
    var product: Product = Product()
    var responseDict = [String:String]()
    var otpFlow: String?
    var messageText: String?
    var isResendButtonHidden:Bool?
    var pinText = ""
    
    override func viewDidLoad() {
        
        parentView.layer.cornerRadius = 5
        okButton.layer.cornerRadius = 2
        cancelButton.layer.cornerRadius = 2
        resendButton.layer.cornerRadius = 2
        
        
        
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
        
        
        if(messageText != nil){
            messageTextField.text = messageText!
        }
        if(otpFlow == "RC"){
            okButton.setTitle("REGENERATE", for: .normal)
            
            enterPinLabel.isHidden = true
            inputParentView.isHidden = true
            resendButton.isHidden = true
            //btnParentViewConstraint.constant = -46
            //parentViewHightConstraint.constant = 250
        }else if(otpFlow == "OC"){
            resendButton.isHidden = true
            //parentViewHightConstraint.constant = 290
        }
        else if(otpFlow == "OK"){
            //leadingOkButtonConstraint.constant = 30
            //widthOkButton.constant = 200
            enterPinLabel.isHidden = true
            inputParentView.isHidden = true
            resendButton.isHidden = true
            //btnParentViewConstraint.constant = -46
            //parentViewHightConstraint.constant = 250
        }
        
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(UIApplicationDidEnterBackground),
            name: UIApplication.didEnterBackgroundNotification,
            object: nil)
        
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard))
        view.addGestureRecognizer(tap)
        
    }
    
    override func viewDidLayoutSubviews() {
        
        if(otpFlow == "RC") {
            nextBtnTrailingConstraint.constant = -260
        }else if(otpFlow == "OC") {
            nextBtnTrailingConstraint.constant = 10
        }
        else if(otpFlow == "OK") {
            nextBtnTrailingConstraint.constant = 10
        }
    }
    
    @objc func editingChanged(_ textField: UITextField) {
        let text = textField.text
        
        if textField.text?.count == 1 {
        }
        
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
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 1)
    }
    
    
    @objc func UIApplicationDidEnterBackground(notification: NSNotification) {
        firstTextField.text?.removeAll()
        secondTextField.text?.removeAll()
        thirdTextField.text?.removeAll()
        fourthTextField.text?.removeAll()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(true)
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        transparentView.backgroundColor = UIColor.clear
    }
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func okPressed(_ sender: UIButton) {
        
        self.dismissKeyboard()
        
        if(otpFlow == "OK"){
            self.dismissKeyboard()
            DispatchQueue.main.async {
                self.dismiss(animated: false, completion: nil)
            }
        }else if(otpFlow == "RC"){
            DispatchQueue.main.async {
                self.dismiss(animated: false, completion: nil)
            }
            delegate?.okPressedOTP("")
        }else{
            
            var errorMessage: String?
            
            pinText = firstTextField.text!+secondTextField.text!+thirdTextField.text!+fourthTextField.text!
            
            if(pinText == ""){
                errorMessage = "PIN fields must not be empty."
            }else if(pinText.count != 4){
                errorMessage = "PIN length should be of 4 digits."
            }
            
            if(errorMessage != nil){
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: errorMessage!, actionType: "serverFailure", isCancelBtnHidden: true)
            }else{
                encryptedPin = try! pinText.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
                delegate?.okPressedOTP(pinText)
                DispatchQueue.main.async {
                    self.dismiss(animated: false, completion: nil)
                }
            }
        }
    }
    
    @IBAction func cancelPressed(_ sender: UIButton) {
        DispatchQueue.main.async {
            self.dismiss(animated: false, completion: nil)
        }
        delegate?.canclePressedOTP()
    }
    
    
    @IBAction func resendPressed(_ sender: UIButton) {
        DispatchQueue.main.async {
            self.dismiss(animated: false, completion: nil)
        }
        delegate?.resendOTP(pinText)
    }
    
    func nextOperation(){
        DispatchQueue.main.async {
            self.dismiss(animated: false, completion: nil)
        }
        let viewController:UIViewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "MainMenuVC") as UIViewController
        self.pushViewController(viewController)
        
    }
    
    func hideKeyboard(){
        self.view.endEditing(true)
    }
}
