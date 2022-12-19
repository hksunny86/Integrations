//
//  RetailPaymentInputAmountVC.swift
//  Timepey
//
//  Created by Adnan Ahmed on 09/09/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import UIKit
import Foundation


class RetailPaymentInputAmountVC: BaseViewController, UITextFieldDelegate{
    
    var product: Product?
    var agentMobileNumber: String?
    
    @IBOutlet weak var amountTextField: UITextField!
    
    @IBOutlet weak var nextButton: UIButton!
    var response = (XMLError(), XMLMessage(), [String:String]())
    
    override func viewDidLoad() {
        //super.viewDidLoad()
        
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: false, isSignoutButtonHidden: false)
        
        
        amountTextField.delegate = self
        amountTextField.keyboardType = UIKeyboardType.numberPad
        
        amountTextField.layer.cornerRadius = 2
        nextButton.layer.cornerRadius = 2
        
        amountTextField.layer.borderWidth = 0.7
        amountTextField.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        
        let paddingForSec = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.amountTextField.frame.size.height))
        amountTextField.leftView = paddingForSec
        amountTextField.leftViewMode = UITextField.ViewMode .always
        
//        if(product?.minamtf != nil || product?.maxamtf != nil){
//            amountHelperLabel.text = "Enter an amount of PKR \((product?.minamtf!)!) to PKR \((product?.maxamtf!)!)"
//        }else{
//            amountHelperLabel.text = "Enter an amount of PKR 1.00 to 250,000.00"
//        }
        
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 7)
    }
    
    @IBAction func nextButtonPressed(_ sender: UIButton) {
        
        self.dismissKeyboard()
        
        var errorMessage: String?
        if(amountTextField.text == nil || amountTextField.text == ""){
            errorMessage = "Amount field must not be empty."
        }else if(Int((amountTextField?.text)!)! < Constants.Validation.TextField.AMOUNT_MIN || Int((amountTextField?.text)!)! > Constants.Validation.TextField.AMOUNT_MAX){
            
            errorMessage = "Invalid amount entered."
        }
        
        if(errorMessage != nil){
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: errorMessage!, actionType: "serverFailure", isCancelBtnHidden: true)
        }else{
            retailPaymentPostRequest()
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
                self.pushViewController(viewController)
            }
            self.hideLoadingView()
            
        }else{
            
            //let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            //let webApi : TransactionAPI = TransactionAPI()
            
//            webApi.retailPaymentInfoPostRequest(
//                Constants.CommandId.RETAILPAYMENT_INFO,
//                reqTime: currentTime,
//                DTID: Constants.AppConfig.DTID_KEY,
//                PID: (product?.id!)!,
//                AMOB: agentMobileNumber!,
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
//                },
//                onFailure: {(reason) ->() in
//                    print("Failure")
//                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
//                    self.hideLoadingView()
//            })
        }
    }
}
