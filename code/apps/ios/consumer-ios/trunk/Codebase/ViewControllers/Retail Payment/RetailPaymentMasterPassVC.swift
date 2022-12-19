//
//  RetailPaymentMasterPassVC.swift
//  JSBL-MB
//
//  Created by Maria Alvi on 11/16/17.
//  Copyright © 2017 inov8. All rights reserved.
//

import Foundation
import UIKit

class RetailPaymentMasterPassVC : BaseViewController , UITextFieldDelegate {
    
    var arrCategory = [Category]()
    var subCategory =  [Category]()
    var response = (XMLError(), XMLMessage(),[String:String]())
    
    @IBOutlet weak var merchantIDText: FPMBottomBorderTextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        initialize()
        //self.bottomBarView.isHidden = true
    }
    
    func initialize(){
        
        self.setupHeaderBarView("QR Payment", isBackButtonHidden: false, isHomeButtonHidden: false, isSignoutButtonHidden: false)
        merchantIDText.delegate = self
    }
    
    
    // MARK: IBActions
    
    @IBAction func nextBtn_Pressed(_ sender: UIButton) {
        
        
        var errorMessage : String!
        if (merchantIDText.text == nil || merchantIDText.text == "") {
            errorMessage = "Merchant ID must not be empty."
        }
        
        if(errorMessage != nil){
            self.showMessage(errorMessage!)
        } else {
//        let scanQRMasterPassDetailVC = UIStoryboard(name: "RetailPayment", bundle: nil).instantiateViewController(withIdentifier: "ScanQRMasterPassDetailVC") as! ScanQRMasterPassDetailVC
//        scanQRMasterPassDetailVC.arrCategory =  self.arrCategory
//        scanQRMasterPassDetailVC.subCategory =  self.subCategory
//        scanQRMasterPassDetailVC.merchantID = merchantIDText.text!
//        self.pushViewController(scanQRMasterPassDetailVC)
            
            let scanQRMasterPassInput = UIStoryboard(name: "RetailPayment", bundle: nil).instantiateViewController(withIdentifier: "MasterPassInputVC") as! MasterPassInputVC
            scanQRMasterPassInput.merchantID = merchantIDText.text!
            //                    scanQRMasterPassConfrimVC.arrCategory =  self.arrCategory
            //                    scanQRMasterPassConfrimVC.subCategory =  self.subCategory
            //                    scanQRMasterPassConfrimVC.QRResponseObj = merchantResponse
            self.pushViewController(scanQRMasterPassInput)
            
           // self.verifyMerchant(merchantID: merchantIDText.text!)
        }
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
                scanQRMasterPassConfrimVC.arrCategory =  self.arrCategory
                scanQRMasterPassConfrimVC.subCategory =  self.subCategory
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
                TAMT: "",
                QRString: "",
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
                        scanQRMasterPassConfrimVC.arrCategory =  self.arrCategory
                        scanQRMasterPassConfrimVC.subCategory =  self.subCategory
                        scanQRMasterPassConfrimVC.QRResponseObj = merchantResponse
                        scanQRMasterPassConfrimVC.QRString = ""
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
    
    @IBAction func scanFroQR_Pressed(_ sender: UIButton) {
        
        let scanQRMasterPassVC = UIStoryboard(name: "RetailPayment", bundle: nil).instantiateViewController(withIdentifier: "ScanQRMasterPassVC") as! ScanQRMasterPassVC
        scanQRMasterPassVC.arrCategory =  self.arrCategory
        scanQRMasterPassVC.subCategory =  self.subCategory
        self.pushViewController(scanQRMasterPassVC)
    }
    // MARK: TextField Delegate Methods
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if (textField.tag == 1){
            
            guard let text = merchantIDText.text else { return true }
            let newLength = text.count + string.count - range.length
            return newLength <= 7
        }else{
            return true
        }
        
    }
    
}
