//
//  CashWithdrawalLeg1VC.swift
//  Timepey
//
//  Created by Adnan Ahmed on 07/09/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import UIKit
import Foundation

class CashWithdrawalLeg1VC: BaseViewController, UITextFieldDelegate, FinancialPinPopupDelegate {
   
    
    
    
    @IBOutlet weak var tranxCodeTitleLabel: UILabel!
    @IBOutlet weak var tranxCodeTextField: UITextField!
    
    @IBOutlet weak var nextButton: UIButton!
    @IBOutlet weak var skipButton: UIButton!
    
    
    var screenTitleText: String?
    var product: Product?
    var response = (XMLError(), XMLMessage(), [String:String]())
    
    override func viewDidLoad() {
        
        //super.viewDidLoad()
        
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: false, isSignoutButtonHidden: false)
        
        nextButton.layer.cornerRadius = 2
        skipButton.layer.cornerRadius = 2
        
        tranxCodeTextField.layer.cornerRadius = 2
        tranxCodeTextField.layer.borderWidth = 0.7
        tranxCodeTextField.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        
        let paddingForSec = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.tranxCodeTextField.frame.size.height))
        //Adding the padding to the second textField
        tranxCodeTextField.leftView = paddingForSec
        tranxCodeTextField.leftViewMode = UITextField.ViewMode .always
        
        tranxCodeTextField.delegate = self
        tranxCodeTextField.keyboardType = UIKeyboardType.numberPad
    }
    
    @IBAction func nextButtonPressed(_ sender: UIButton) {
    
        self.dismissKeyboard()
        
        var errorMessage: String?
        
        let transCode = tranxCodeTextField.text
        
        if(transCode == nil || transCode == ""){
            errorMessage = "Transaction Code field must not be empty."
        }else if(transCode?.count != 4){
            errorMessage = "Transaction Code length should be of 4 digits."
        }
        
        if(errorMessage != nil){
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: errorMessage!, actionType: "serverFailure", isCancelBtnHidden: true)
        }else{
            
            let popupView = FinancialPinPopup(nibName: "FinancialPinPopup", bundle: nil)
            popupView.delegate = self
            popupView.product = product!
            popupView.isMpinSetLater = self.isMpinSetLater
            popupView.modalPresentationStyle = .overCurrentContext
            self.present(popupView, animated: false, completion: nil)
        }
    }
    
    @IBAction func skipButtonPressed(_ sender: UIButton) {
        
        tranxCodeTextField.text = ""
        
        self.dismissKeyboard()
        
        let popupView = FinancialPinPopup(nibName: "FinancialPinPopup", bundle: nil)
        popupView.delegate = self
        popupView.product = product!
        popupView.isMpinSetLater = self.isMpinSetLater
        popupView.modalPresentationStyle = .overCurrentContext
        self.present(popupView, animated: false, completion: nil)
    }
    
    //MASK: Financial PIN Delegate
    func okPressedFP() {
        //print("pin verified")
        if(tranxCodeTextField.text != nil){
            transactionInfoPostRequest(tranxCodeTextField.text!)
        }else{
            transactionInfoPostRequest("")
        }
        
    }
    func canclePressedFP() {
        
    }
    
    //MASK: TextField Delegate
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        guard let text = tranxCodeTextField.text else { return true }
        let newLength = text.count + string.count - range.length
        return newLength <= 4
    }
    
    
    func transactionInfoPostRequest(_ tranxCode: String) {
        
        //var responseArray = [[String:String]]()
        
        self.showLoadingView()
        
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                xmlPath = Bundle.main.path(forResource: "Command-195-CustomerCashWithdrawal", ofType: "xml"),
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
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                }
            }else if(self.response.1.msg != nil){
                
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                
            }else{
                //print(tranxCode)
                //print(self.response.2)
                let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "CashWithdrawalLeg1InfoVC") as! CashWithdrawalLeg1InfoVC
                viewController.responseDict = self.response.2
                self.pushViewController(viewController)

                
            }
            self.hideLoadingView()
            
        }else{
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let webApi : CashWithdrawalWebAPI = CashWithdrawalWebAPI()
            
            var encryptedPin: String = ""
            
            if(tranxCode != ""){
                encryptedPin = try! tranxCode.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
            }
            
            
            webApi.cashWithdrawalLeg1InfoPostRequest(
                Constants.CommandId.CASH_WITHDRAWAL_LEG1_INFO,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                PID: "", //(product?.id!)!,
                PIN: "",
                ENCT: Constants.AppConfig.ENCT_KEY,
                MANUAL_OTPIN:encryptedPin,
                
                onSuccess:{(data) -> () in
                    //print(data)
                    self.response = XMLParser.cashWithdrawalInfoXMLParsing(data)
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
                        let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "CashWithdrawalLeg1InfoVC") as! CashWithdrawalLeg1InfoVC
                        viewController.responseDict = self.response.2
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
    func okPressedChallanNo(EncMpin: String) {
        // no code
    }
}
