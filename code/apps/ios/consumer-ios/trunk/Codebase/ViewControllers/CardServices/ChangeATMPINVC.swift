//
//  ChangeATMPINVC.swift
//  Timepey
//
//  Created by Adnan Ahmed on 15/11/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation
import UIKit

class ChangeATMPINVC: BaseViewController, UITextFieldDelegate {
    
    
    @IBOutlet weak var screenTitleLabel: UILabel!
    @IBOutlet weak var newPinLabel: UILabel!
    @IBOutlet weak var newPinTextField: UITextField!
    @IBOutlet weak var confirmPinLabel: UILabel!
    @IBOutlet weak var confirmnewPinTextField: UITextField!
    
    @IBOutlet weak var changePinButton: UIButton!
    
    var product: Product?
    
    override func viewDidLoad() {
        //super.viewDidLoad()
        
        self.setupHeaderBarView("", isBackButtonHidden: true, isHomeButtonHidden: false, isSignoutButtonHidden: false)
        
        //viewController.screenTitle = "Change ATM PIN"
        
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard))
        view.addGestureRecognizer(tap)
        
        setupView()
        
    }
    
    func setupView(){
        
        if(product?.fID == Constants.FID.CHANGE_ATM_PIN){
            screenTitleLabel.text = "Change ATM PIN"
            newPinLabel.text = "New ATM PIN"
            confirmPinLabel.text = "Confirm New ATM PIN"
            
        }else if(product?.fID == Constants.FID.GENRATE_ATM_PIN){
            screenTitleLabel.text = "Generate ATM PIN"
            newPinLabel.text = "New ATM PIN"
            confirmPinLabel.text = "Confirm New ATM PIN"
            changePinButton.setTitle("Generate ATM PIN", for: .normal)
        }
        
        newPinTextField.delegate = self
        confirmnewPinTextField.delegate = self
        
        changePinButton.layer.cornerRadius = Constants.UI.Button.CORNER_RADIUS

        
        newPinTextField.layer.cornerRadius = Constants.UI.TextField.CORNER_RADIUS
        newPinTextField.layer.borderColor = Constants.UI.TextField.BORDER_COLOR
        newPinTextField.layer.borderWidth = Constants.UI.TextField.BORDER_WIDTH
        
        confirmnewPinTextField.layer.cornerRadius = Constants.UI.TextField.CORNER_RADIUS
        confirmnewPinTextField.layer.borderColor = Constants.UI.TextField.BORDER_COLOR
        confirmnewPinTextField.layer.borderWidth = Constants.UI.TextField.BORDER_WIDTH
        
        let paddingForSec = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.newPinTextField.frame.size.height))
        //Adding the padding to the second textField
        newPinTextField.leftView = paddingForSec
        newPinTextField.leftViewMode = UITextField.ViewMode .always
        
        let paddingForThird = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.confirmnewPinTextField.frame.size.height))
        //Adding the padding to the second textField
        confirmnewPinTextField.leftView = paddingForThird
        confirmnewPinTextField.leftViewMode = UITextField.ViewMode .always
        
    }
    
    
    override func actHeaderBtn(_ buttonPressedType: HEADER_BAR_BUTTON_PRESSED){
        
        if(buttonPressedType == HEADER_BAR_BUTTON_PRESSED.HEADER_BACK_BUTTON){
            self.popViewController()
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
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(true)
        
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        guard let text = textField.text else { return true }
        let newLength = text.count + string.count - range.length
        return newLength <= 4
    }
    
    @IBAction func changePinPressed(_ sender: UIButton) {
        
        self.dismissKeyboard()
        
        var errorMessage: String?
        
        let newPin = newPinTextField.text
        let confirmPin = confirmnewPinTextField.text
        
        if(newPin == nil || newPin == ""){
            errorMessage = "New ATM PIN field must not be empty."
        }else if(newPin?.count != 4){
            errorMessage = "New ATM PIN length should be of 4 digits."
        }
            
        else if(confirmPin == nil || confirmPin == ""){
            errorMessage = "Confirm ATM PIN field must not be empty."
        }else if(confirmPin?.count != 4){
            errorMessage = "Confirm ATM PIN length should be of 4 digits."
        }
            
        else if(newPin != confirmPin){
            errorMessage = "New and Confirm ATM PIN Mismatch."
        }
        
        if(errorMessage != nil){
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: errorMessage!, actionType: "serverFailure", isCancelBtnHidden: true)
        }else{
            changeATMPinRequest()
        }
    }

    func changeATMPinRequest() {
        
        var response = (XMLError(), XMLMessage())
        
        let newPinText = newPinTextField.text
        let confirmNewPinText = confirmnewPinTextField.text
        
        newPinTextField.text = ""
        confirmnewPinTextField.text = ""
        
        self.showLoadingView()
        
        var fileName: String?
        
        fileName = "Command-158-ATM PIN Change & Generation"
        
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
                
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE_IMPCR_SUCCESS, msgLabelText: response.1.msg!, actionType: "", isCancelBtnHidden: true)
                
            }
            self.hideLoadingView()
            
        }else{
            let encryptedNewPin = try! newPinText!.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
            let encryptedConfirmNewPin = try! confirmNewPinText!.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let commandId = Constants.CommandId.CARD_ATM_PIN_GEN_CHANAGE_CHECKOUT
            
            let webApi : CardServicesAPI = CardServicesAPI()
            
            var APING: String?
            if(product?.fID == Constants.FID.GENRATE_ATM_PIN){
                APING = "true"
            }else{
                APING = "false"
            }
            
            
            webApi.genrateATMPINFinalPostRequest(
                commandId,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                nPin: encryptedNewPin,
                cPin: encryptedConfirmNewPin,
                aPING: APING!,
                ENCT: Constants.AppConfig.ENCT_KEY,
                onSuccess:{(data) -> () in
                    
                    response = XMLParser.changePINXMLParsing(data)
                    if(response.0.msg != nil){
                        if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                        }else{
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "requestDenied", isCancelBtnHidden: true)
                            self.newPinTextField.text = ""
                            self.confirmnewPinTextField.text = ""
                        }
                    }else if(response.1.msg != nil){
                        
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE_IMPCR_SUCCESS, msgLabelText: response.1.msg!, actionType: "", isCancelBtnHidden: true)
                        
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
