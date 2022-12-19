//
//  ScanQRMasterPassConfrimVC.swift
//  JSBL-MB
//
//  Created by Maria Alvi on 11/16/17.
//  Copyright © 2017 inov8. All rights reserved.
//

import Foundation
import UIKit
class ScanQRMasterPassConfrimVC: BaseViewController,UITableViewDelegate,UITableViewDataSource,UITextFieldDelegate, FinancialPinPopupDelegate {
    
    

    @IBOutlet weak var tblConfrimView: UITableView!
    
    @IBOutlet weak var paymentAmountValueLbl: UITextField!
    @IBOutlet weak var pkrLbl: UILabel!
    @IBOutlet weak var paymentAmountLbl: UILabel!
    
    var arrCategory = [Category]()
    var subCategory =  [Category]()
    let arrHeaderTitles = ["Merchant Details", "Payment Details"]
    var arrTitles = [[""]]
    var arrValues = [[""]]
    var dict = [String:String]()
    var response = (XMLError(), XMLMessage(),[retailMerchant]())
    var QRResponseObj : retailMerchant = retailMerchant()
    
    var isEnterAmount : Bool = false
    
    var mechantID = String()
    var mechantName = String()
    var transactionType = String()
    var amount = String()
    var fpin = String()
    var transactionFee = String()
    
    var QRString: String = String()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.setupHeaderBarView("QR Payment", isBackButtonHidden: false, isHomeButtonHidden: false, isSignoutButtonHidden: false)
        
        paymentAmountValueLbl.text = "\(QRResponseObj.totalAmountFormatted)"
        arrTitles = [["Merchant ID","Merchant Name"]]
       // arrValues = [[arrAccounts[0].accountTitle!,arrAccounts[0].accountNo!]]
         arrValues = [[QRResponseObj.merchantID,QRResponseObj.merchantName]]
        
        arrTitles.append(["Transaction Type","Transaction Fee"])
      //  arrValues.append(z)
        arrValues.append(["QR Payment","PKR 0.0"])
     
    }
    
    //  MARK: UITableView
    func tableView(_ tableView: UITableView, viewForFooterInSection section: Int) -> UIView? {
        
        let view:UIView = UIView.init(frame: CGRect.init(x: 0, y: 0, width: self.tblConfrimView.bounds.size.width, height: 10))
        view.backgroundColor = .clear
        return view
    }
    func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        return 10.0
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 30
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        
        let view = UIView(frame: CGRect(x: 0, y: 0, width: tblConfrimView.frame.size.width, height:30))
        let label = UILabel(frame: view.bounds)
        label.text = arrHeaderTitles[section]
        label.textColor = UIColor.lightGray
        label.backgroundColor = self.view.backgroundColor
        if #available(iOS 8.2, *) {
            label.font = UIFont.systemFont(ofSize: 13, weight: .light)
        } else {
            label.font = UIFont.systemFont(ofSize: 13)
        }
        view.addSubview(label)
        view.backgroundColor = UIColor.clear
        return view
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 50
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return arrHeaderTitles.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        let arrTitle = arrTitles[section]
        return arrTitle.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let identifier = "Cell"
        let cell = UITableViewCell(style: UITableViewCell.CellStyle.value1, reuseIdentifier: identifier)
        
        cell.textLabel?.text = arrTitles[indexPath.section][indexPath.row]
        cell.detailTextLabel?.text = arrValues[indexPath.section][indexPath.row]
        
        if #available(iOS 8.2, *) {
            cell.textLabel?.font = UIFont.systemFont(ofSize: 15, weight: .light)
            cell.detailTextLabel?.font = UIFont.systemFont(ofSize: 15, weight: .light)
        } else {
            cell.textLabel?.font = UIFont.systemFont(ofSize: 15)
            cell.detailTextLabel?.font = UIFont.systemFont(ofSize: 15)
        }
        cell.textLabel?.textColor = UIColor.darkGray
        cell.detailTextLabel?.textColor = UIColor.lightGray
        cell.layer.borderColor = UIColor.init(red: 159.0/255.0, green: 159.0/255.0, blue: 159.0/255.0, alpha: 1.0).cgColor
        cell.layer.borderWidth = 0.2
        cell.preservesSuperviewLayoutMargins = false
        cell.separatorInset = UIEdgeInsets.zero
        cell.layoutMargins = UIEdgeInsets.zero
        cell.layer.cornerRadius = 2
        return cell
    }
    // MARK: WebAPIs
    func transferMerchant(){
        //print("transfer Merchant")
        
        
        if(Constants.AppConfig.IS_MOCK == 1)  {
            guard let
                xmlPath = Bundle.main.path(forResource: "Command-209-RetailPayment-Checkout", ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                else { return }
            
            response = XMLParser.merchantTranferQRXMLParsing(data)
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
                
                response.2[0].merchantID =     self.QRResponseObj.merchantID
                let masterPassSuccessVC = UIStoryboard(name: "RetailPayment", bundle: nil).instantiateViewController(withIdentifier: "MasterPassSuccessVC") as! MasterPassSuccessVC
                masterPassSuccessVC.arrCategory =  self.arrCategory
                masterPassSuccessVC.subCategory =  self.subCategory
                masterPassSuccessVC.QRResponseObj = response.2[0]
                self.pushViewController(masterPassSuccessVC)
            }
        } else {
            
            self.showLoadingView()
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            let webApi : TransactionAPI = TransactionAPI()
            
            webApi.retailPaymentCheckoutPostRequest(
                Constants.CommandId.RETAILPAYMENT_CHECKOUT,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                MRID: QRResponseObj.merchantID,
                TAMT: QRResponseObj.totalAmount,
                MNAME: QRResponseObj.merchantName,
                QRString: QRString,
                onSuccess:{(data) -> () in
                    //print(data)
                    self.response = XMLParser.merchantTranferQRXMLParsing(data)
                    //print(self.response)
                    //print(self.response.2["BALF"])
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
                            self.dismiss(animated: false, completion: nil)
                        }
                        
                        self.response.2[0].merchantID =     self.QRResponseObj.merchantID
                        let masterPassSuccessVC = UIStoryboard(name: "RetailPayment", bundle: nil).instantiateViewController(withIdentifier: "MasterPassSuccessVC") as! MasterPassSuccessVC
                        masterPassSuccessVC.arrCategory =  self.arrCategory
                        masterPassSuccessVC.subCategory =  self.subCategory
                        masterPassSuccessVC.QRResponseObj = self.response.2[0]
                        self.pushViewController(masterPassSuccessVC)
                        
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
    
    // MARK: IBActions
    
    
    @IBAction func actConfirm(_ sender: UIButton) {
        DispatchQueue.main.async {
            self.dismissKeyboard()
            let popupView = FinancialPinPopup(nibName: "FinancialPinPopup", bundle: nil)
            popupView.delegate = self
            popupView.requiredAction = "delegate"
            popupView.isMpinSetLater = self.isMpinSetLater
            popupView.modalPresentationStyle = .overCurrentContext
            self.present(popupView, animated: false, completion: nil)
        }
        
    }
    @IBAction func actCancel(_ sender: UIButton) {
        
        let popupView = AlertPopup(nibName: "AlertPopup", bundle: nil)
        popupView.headerLabelText = Constants.Message.ALERT_NOTIFICATION_TITLE
        popupView.isYesNoButtons = true
        popupView.msgLabelText = "Are you sure to cancel?"
        popupView.cancelButtonHidden = false
        popupView.isMpinSetLater = self.isMpinSetLater
        popupView.modalPresentationStyle = .overCurrentContext
        self.present(popupView, animated: false, completion: nil)
        
    }
    
    func okPressedFP() {
        
            self.transferMerchant()
        //self.transferMerchant()
    }
    
    func canclePressedFP() {
        
        self.popViewController()
    }
    func okPressedChallanNo(EncMpin: String) {
        //
    }
   
}
