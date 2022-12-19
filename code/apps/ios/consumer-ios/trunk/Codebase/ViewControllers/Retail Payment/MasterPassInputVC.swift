//
//  MasterPassInputVC.swift
//  JSBL-BB
//
//  Created by Uzair on 1/11/18.
//  Copyright © 2018 Inov8. All rights reserved.
//

import UIKit

class MasterPassInputVC: BaseViewController, UITextFieldDelegate {
    
    var response = (XMLError(), XMLMessage(),[String:String]())
    var merchantID = String()
    var QRResponseObj = QRResponse()
    
    @IBOutlet weak var amountField: FPMBottomBorderTextField!
    @IBOutlet weak var amountView: UIView!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

    //self.bottomBarView.isHidden = true
    self.setupHeaderBarView("QR Payment", isBackButtonHidden: false, isHomeButtonHidden: false, isSignoutButtonHidden: false)
        
    amountView.layer.cornerRadius = 5
        
        // Do any additional setup after loading the view.
    }
    
    func verifyMerchant(merchantID:String){
        //print("verify Merchant")
        
        
        if(Constants.AppConfig.IS_MOCK == 1)  {
            guard let
                xmlPath = Bundle.main.path(forResource: "Command-208-RetailPayment", ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                else { return }
            
            response = XMLParser.paramTypeXMLParsing(data)
            //print(response)
            
            if(response.0.msg != nil){
                
                self.alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: response.0.msg!, okButtonPressed: {
                    self.alertView.hide()
                })
                self.alertView.show(parentView: self.view)
            }
                
            else if(response.1.msg != nil){
                
                self.alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: response.1.msg!, okButtonPressed: {
                    self.alertView.hide()
                    self.popViewControllerAndGotoStart()
                })
                self.alertView.show(parentView: self.view)
            } else {
                let merchantResponse :  retailMerchant = retailMerchant()
                merchantResponse.merchantID = response.2["MRID"]!
                merchantResponse.totalAmount = response.2["TAMT"]!
                merchantResponse.totalAmountFormatted = response.2["TAMTF"]!
                merchantResponse.merchantName = response.2["MNAME"]!
                
                let scanQRMasterPassConfrimVC = UIStoryboard(name: "RetailPayment", bundle: nil).instantiateViewController(withIdentifier: "ScanQRMasterPassConfrimVC") as! ScanQRMasterPassConfrimVC
//                scanQRMasterPassConfrimVC.arrCategory =  self.arrCategory
//                scanQRMasterPassConfrimVC.subCategory =  self.subCategory
                scanQRMasterPassConfrimVC.QRResponseObj = merchantResponse
                self.pushViewController(scanQRMasterPassConfrimVC)
            }
        } else {
            
            self.showLoadingView()
            
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let webApi : TransactionAPI = TransactionAPI()
            
            webApi.retailPaymentInfoPostRequest(
                Constants.CommandId.RETAILPAYMENT_INFO,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                MRID: merchantID,
                TAMT: amountField.text!,
                QRString: QRResponseObj.qrString,
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
                        let merchantResponse :  retailMerchant = retailMerchant()
                        merchantResponse.merchantID = self.response.2["MRID"]!
                        merchantResponse.totalAmount = self.response.2["TAMT"]!
                        merchantResponse.totalAmountFormatted = self.response.2["TAMTF"]!
                        merchantResponse.merchantName = self.response.2["MNAME"]!
                        
                        let scanQRMasterPassConfrimVC = UIStoryboard(name: "RetailPayment", bundle: nil).instantiateViewController(withIdentifier: "ScanQRMasterPassConfrimVC") as! ScanQRMasterPassConfrimVC
                        scanQRMasterPassConfrimVC.QRResponseObj = merchantResponse
                        scanQRMasterPassConfrimVC.QRString = self.QRResponseObj.qrString
                        self.pushViewController(scanQRMasterPassConfrimVC)
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
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if (textField.tag == 1){
            
            guard let text = amountField.text else { return true }
            let newLength = text.count + string.count - range.length
            return newLength <= 7
        }else{
            return true
        }
        
    }
    @IBAction func actNext(_ sender: UIButton) {
        
        var errorMessage : String!
        if ( amountField.text == nil || amountField.text == "") {
            errorMessage = "Amount fiels must not be empty."
        }
        
        if(errorMessage != nil){
            self.showMessage(errorMessage!)
        } else {
            
            self.verifyMerchant(merchantID: merchantID)
        }
    }
    @IBAction func actCancel(_ sender: UIButton) {
        
        let popupView = AlertPopup(nibName: "AlertPopup", bundle: nil)
        popupView.headerLabelText = Constants.Message.ALERT_NOTIFICATION_TITLE
        popupView.isYesNoButtons = true
        popupView.msgLabelText = "Are you sure to cancel?"
        popupView.cancelButtonHidden = false
        popupView.modalPresentationStyle = .overCurrentContext
        self.present(popupView, animated: false, completion: nil)
    }
    
}
