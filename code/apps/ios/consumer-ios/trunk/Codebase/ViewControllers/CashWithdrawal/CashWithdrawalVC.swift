//
//  EnterAmountVC.swift
//  Timepey
//
//  Created by Adnan Ahmed on 13/07/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import UIKit
import Foundation


class CashWithdrawalVC: BaseViewController, UITextFieldDelegate {

    @IBOutlet weak var screenTitleLabel: UILabel!
    @IBOutlet weak var amountTitleLabel: UILabel!
    @IBOutlet weak var amountTextField: UITextField!
    @IBOutlet weak var nextButton: UIButton!
    
    var product: Product?
    var screenTitleText: String?
    var response = (XMLError(), XMLMessage(), [String:String]())
    
    override func viewDidLoad() {
        //super.viewDidLoad()
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: false, isSignoutButtonHidden: false)
        
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard))
        view.addGestureRecognizer(tap)

        nextButton.layer.cornerRadius = 2
        amountTextField.layer.cornerRadius = 2
        amountTextField.layer.borderWidth = 0.7
        amountTextField.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        
        let paddingForFirst = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.amountTextField.frame.size.height))
        //Adding the padding to the second textField
        amountTextField.leftView = paddingForFirst
        amountTextField.leftViewMode = UITextField.ViewMode .always
        
        if(screenTitleText != nil){
            screenTitleLabel.text = screenTitleText!
        }
        
        amountTextField.delegate = self
        amountTextField.keyboardType = UIKeyboardType.numberPad
        amountTitleLabel.text = "Amount"
    }
    
    func validateAmount(_ textField: UITextField) -> Bool{
        let minAmount = Int((product?.minamt?.replacingOccurrences(of: ".0", with: ""))!)
        let maxAmount = Int((product?.maxamt?.replacingOccurrences(of: ".0", with: ""))!)
        
        let amount = Int(textField.text!)
        if(amount! < minAmount! || amount! > maxAmount!){
            return false
        }else{
            return true
        }
    }
    
//MASK: TextField Delegate
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_ALPHABETS, length: 7)
    }
    
    @IBAction func nextButtonPressed(_ sender: UIButton) {
        self.dismissKeyboard()
        
        var errorMessage: String?
        
        let textField = amountTextField.text
        
        if(textField == nil || textField == ""){
            errorMessage = "Amount field must not be empty."
        }else if(Int(textField!)! < Constants.Validation.TextField.AMOUNT_MIN || Int(textField!)! > Constants.Validation.TextField.AMOUNT_MAX){
            errorMessage = "Invalid amount entered."
        }
        
        if(errorMessage != nil){
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: errorMessage!, actionType: "serverFailure", isCancelBtnHidden: true)
        }else{
            if(product?.fID == Constants.FID.CASH_WITHDRAWAL){
                transactionInfoPostRequest()
            }else if(product?.fID == Constants.FID.TRANSFER_IN_FID || product?.fID == Constants.FID.TRANSFER_OUT_FID){
                fundTransferInfoPostRequest()
            }
        }
    }
    
    func fundTransferInfoPostRequest() {
        
        self.showLoadingView()
        
        if(Constants.AppConfig.IS_MOCK == 1){
            
            var fileName = String()
            if(product?.fID == Constants.FID.TRANSFER_IN_FID){
                fileName = "Command-103-TransferIn-Info"
            }
            else if(product?.fID == Constants.FID.TRANSFER_OUT_FID){
                fileName = "Command-105-TransferOut-Info"
            }
            
            guard let
                xmlPath = Bundle.main.path(forResource: fileName, ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                
                else { return }
            
            //let newStr = String(data: data, encoding: NSUTF8StringEncoding)
            //print(newStr)
            self.response = XMLParser.cashWithdrawalInfoXMLParsing(data)
            //print(self.response)
            if(self.response.0.msg != nil){
                if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                }else{
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "", isCancelBtnHidden: true)
                }
            }else if(self.response.1.msg != nil){
                
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "", isCancelBtnHidden: true)
                
            }else{
                let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "ConfirmationVC") as! ConfirmationVC
                viewController.responseDict = self.response.2
                viewController.product = product
                viewController.isMpinSetLater = self.isMpinSetLater
                self.pushViewController(viewController)
                
            }
            self.hideLoadingView()
            
        }else{
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let webApi : MyAccountWebAPI = MyAccountWebAPI()
            
            let commandId: String?
            
            if(product?.fID == Constants.FID.TRANSFER_IN_FID){
                commandId = Constants.CommandId.TRANSFER_IN_INFO
            }
            else{
                commandId = Constants.CommandId.TRANSFER_OUT_INFO
            }
            
            webApi.transferInfoRequest(
                commandId!,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                PID: (product?.id!)!,
                TXAM: amountTextField.text!,
                onSuccess:{(data) -> () in
                    //print(data)
                    self.response = XMLParser.cashWithdrawalInfoXMLParsing(data)
                    //print(self.response)
                    if(self.response.0.msg != nil){
                        if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                        }else{
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "", isCancelBtnHidden: true)
                        }
                    }else if(self.response.1.msg != nil){
                        
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "", isCancelBtnHidden: true)
                        
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
    
    func transactionInfoPostRequest() {
        
        self.showLoadingView()
        
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                xmlPath = Bundle.main.path(forResource: "Command-185-Cash Withdrawal Info", ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                
                else { return }
            
            //let newStr = String(data: data, encoding: NSUTF8StringEncoding)
            //print(newStr)
            self.response = XMLParser.cashWithdrawalInfoXMLParsing(data)
            //print(self.response)
            if(self.response.0.msg != nil){
                if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                }else{
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "", isCancelBtnHidden: true)
                }
            }else if(self.response.1.msg != nil){
                
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "", isCancelBtnHidden: true)
                
            }else{
                let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "ConfirmationVC") as! ConfirmationVC
                viewController.responseDict = self.response.2
                viewController.product = product
                viewController.isMpinSetLater = self.isMpinSetLater
                self.pushViewController(viewController)
                
            }
            self.hideLoadingView()
            
        }else{
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let webApi : CashWithdrawalWebAPI = CashWithdrawalWebAPI()

            
            
            webApi.cashWithdrawalInfoPostRequest(
                Constants.CommandId.CASH_WITHDRAWAL_INFO,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                PID: (product?.id!)!,
                TXAM: amountTextField.text!,
                onSuccess:{(data) -> () in
                    //print(data)
                    self.response = XMLParser.cashWithdrawalInfoXMLParsing(data)
                    //print(self.response)
                    if(self.response.0.msg != nil){
                        if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                        }else{
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "", isCancelBtnHidden: true)
                        }
                    }else if(self.response.1.msg != nil){
                        
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "", isCancelBtnHidden: true)
                        
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
