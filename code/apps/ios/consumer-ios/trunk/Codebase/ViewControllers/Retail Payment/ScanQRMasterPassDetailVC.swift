//
//  ScanQRMasterPassDetailVC.swift
//  JSBL-MB
//
//  Created by Maria Alvi on 11/16/17.
//  Copyright © 2017 inov8. All rights reserved.
//

import Foundation
import UIKit
import ActionSheetPicker_3_0

class ScanQRMasterPassDetailVC : BaseViewController , UITextFieldDelegate{
    
    var arrCategory = [Category]()
    var subCategory =  [Category]()
    var QRResponseObj = QRResponse()
    var arrTitles = [String]()
    var currentAccIndex : Int = 0
    
    @IBOutlet weak var fromAccountBtn: UIButton!
    @IBOutlet weak var merchantNameLbl: UILabel!
    @IBOutlet weak var cityNameLbl: UILabel!
    @IBOutlet weak var paymentAmountValueLbl: UITextField!
    @IBOutlet weak var nextBtn: UIButton!
    @IBOutlet weak var bottomImageView: UIImageView!
    @IBOutlet weak var fetchAccountBtn: UIButton!
    @IBOutlet weak var amountTxt: FPMBottomBorderTextField!
    @IBOutlet weak var pkrLbl: UILabel!
    @IBOutlet weak var paymentAmountLbl: UILabel!
    @IBOutlet weak var lineView: UIView!
    @IBOutlet weak var merchantDetailViewheightConstraint: NSLayoutConstraint!
    
    var isEnterAmount : Bool = false
    var merchantID : String = ""
    
    // MARK: UIViewController Methods
    override func viewDidLoad() {
        //super.viewDidLoad()
        initialization()
    }
    
    func initialization(){
        self.setupHeaderBarView("QR Payment", isBackButtonHidden: false, isHomeButtonHidden: false, isSignoutButtonHidden: false)
        if (QRResponseObj.merchantID != ""){
            if(QRResponseObj.amount != ""){
                paymentAmountLbl.text = "Payment Amount"
                amountTxt.isHidden = true
                pkrLbl.isHidden = false
                paymentAmountValueLbl.text = QRResponseObj.amount
                lineView.layer.backgroundColor = UIColor.lightGray.cgColor
                merchantNameLbl.isHidden = false
                cityNameLbl.isHidden = false
                merchantDetailViewheightConstraint.constant = 60
            } else {
                paymentAmountLbl.text = "Enter Payment Amount"
                paymentAmountValueLbl.isHidden = true
                amountTxt.isHidden = false
                amountTxt.keyboardType = .numberPad
                pkrLbl.isHidden = true
                lineView.layer.backgroundColor = UIColor.lightGray.cgColor
                merchantNameLbl.isHidden = false
                cityNameLbl.isHidden = false
                isEnterAmount = true
                merchantDetailViewheightConstraint.constant = 60
            }
        }else {
            paymentAmountLbl.text = "Enter Payment Amount"
            paymentAmountValueLbl.isHidden = true
            amountTxt.isHidden = false
            pkrLbl.isHidden = true
            amountTxt.keyboardType = .numberPad
            lineView.layer.backgroundColor = UIColor.white.cgColor
            merchantNameLbl.isHidden = true
            cityNameLbl.isHidden = true
            isEnterAmount = true
            merchantDetailViewheightConstraint.constant = 0
        }
    }
    
    // MARK: Web APIs
    
    
    func verifyMerchant(){
        print("verify Merchant")
        
        var response = (XMLError(), XMLMessage(),[String:String]())
        if(Constants.AppConfig.IS_MOCK == 1)  {
            guard let
                xmlPath = Bundle.main.path(forResource: "Command-208-RetailPayment", ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                else { return }
            
            response = XMLParser.paramTypeXMLParsing(data)
            print(response)
            
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
            
//            self.showLoadingView()
//            let amount : String = QRResponseObj.amount != "" ? QRResponseObj.amount : amountTxt.text!
//            let merchantID : String = QRResponseObj.merchantID != "" ? QRResponseObj.merchantID : self.merchantID
//            
//            let dict : [String:String] =  ["DTID":"5","PID": "2510815","TAMT":amount,"SENACCOUNTNO":arrAccounts[currentAccIndex].accountNo!,"MRID":merchantID, "QRTYPE":"MPASS" ]
//            
//            
//            WebApiManager.sharedInstance.verifyMerchantForQR(dict, successBlock: { (data) in
//                
//                response = XMLParser.paramTypeXMLParsing(data)
//                print(response)
//                
//                if(response.0.msg != nil){
//                    
//                    DispatchQueue.main.async {
//                        self.alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: response.0.msg!,type: ALERT_TYPE._ERROR, okButtonPressed:{
//                            self.alertView.hide()
//                        })
//                        self.alertView.show(parentView: self.view)
//                    }
//                }
//                    
//                else if(response.1.msg != nil){
//                    
//                    DispatchQueue.main.async {
//                        self.alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: response.1.msg!,type: ALERT_TYPE._SUCCESS, okButtonPressed:{
//                            self.alertView.hide()
//                            self.goToMainMenu()
//                        })
//                        self.alertView.show(parentView: self.view)
//                    }
//                    
//                }else {
//                    
//                    let merchantResponse :  retailMerchant = retailMerchant()
//                    merchantResponse.merchantID = response.2["MRID"]!
//                    merchantResponse.totalAmount = response.2["TAMT"]!
//                    merchantResponse.totalAmountFormatted = response.2["TAMTF"]!
//                    merchantResponse.merchantName = response.2["MNAME"]!
//                    
//                    let scanQRMasterPassConfrimVC = UIStoryboard(name: "RetailPayment", bundle: nil).instantiateViewController(withIdentifier: "ScanQRMasterPassConfrimVC") as! ScanQRMasterPassConfrimVC
//    
//                    scanQRMasterPassConfrimVC.fromAccoutTitle = self.activeAccounts[self.currentAccIndex].accountTitle!
//                    scanQRMasterPassConfrimVC.fromAccoutNo = self.activeAccounts[self.currentAccIndex].accountNo!
//                    scanQRMasterPassConfrimVC.arrCategory =  self.arrCategory
//                    scanQRMasterPassConfrimVC.subCategory =  self.subCategory
//                    scanQRMasterPassConfrimVC.QRResponseObj = merchantResponse
//                    
//                    self.pushViewController(scanQRMasterPassConfrimVC)
//                }
//                self.hideLoadingView()
//                
//            }, failureBlock: { (reason) in
//                
//                DispatchQueue.main.async {
//                    
//                    self.alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: reason,type: ALERT_TYPE._ERROR, okButtonPressed:{
//                        self.alertView.hide()
//                    })
//                    self.alertView.show(parentView: self.view)
//                    self.hideLoadingView()
//                }
//            })
        }
    }
    // MARK: IBActions
    
    @IBAction func fetchAccont_Pressed(_ sender: UIButton) {
        
        self.view.endEditing(true)
        ActionSheetStringPicker.show(withTitle:"Select Account" , rows: arrTitles, initialSelection: currentAccIndex, doneBlock: { (picker, index, string) in
            
            self.currentAccIndex = index
            let titleStr = string as! String
            sender.setTitle(titleStr, for: .normal)
            
        }, cancel: { (picker) in
            
        }, origin: sender)
    }
    @IBAction func nextBtn_Pressed(_ sender: UIButton) {
        
        if (!isEnterAmount) {
             verifyMerchant()
        }else {
            if(amountTxt.text == ""){
                self.showMessage("Amount should not be empty.")
            }else {
                 verifyMerchant()
            }
        }
        
    }
    

    // MARK: TextViewDelegate Methods
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        return true
    }
}
