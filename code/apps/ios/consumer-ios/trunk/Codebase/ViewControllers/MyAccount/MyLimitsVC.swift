//
//  MyLimitsVC.swift
//  Timepey
//
//  Created by Adnan Ahmed on 05/10/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation
import UIKit

class MyLimitsVC: BaseViewController {
    
    
    @IBOutlet weak var parentView: UIView!
    @IBOutlet weak var limitLabel: UILabel!
    @IBOutlet weak var debitLabel: UILabel!
    @IBOutlet weak var creditLabel: UILabel!
    @IBOutlet weak var dailyLabel: UILabel!
    @IBOutlet weak var monthlyLabel: UILabel!
    @IBOutlet weak var yearlyLabel: UILabel!
    @IBOutlet weak var dailyDebitLabel: UILabel!
    @IBOutlet weak var dailyCeditLabel: UILabel!
    @IBOutlet weak var monthlyDebitLabel: UILabel!
    @IBOutlet weak var monthlyCreditLabel: UILabel!
    @IBOutlet weak var yearlyDebit: UILabel!
    @IBOutlet weak var yearlyCredit: UILabel!
    
    @IBOutlet weak var okButton: UIButton!
    
    var response = (XMLError(), XMLMessage(), [String:String]())
    
    override func viewDidLoad() {
        
        super.viewDidLoad()
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: false, isSignoutButtonHidden: false)
    
        myLimitsPostRequest()
        parentView.isHidden = true
        okButton.isHidden = true
        setupView()
    }
    
    func setupView(){
        
        parentView.layer.cornerRadius = 4
        
        okButton.layer.cornerRadius = Constants.UI.Button.CORNER_RADIUS
        
        parentView.layer.borderWidth = 0.7
        limitLabel.layer.borderWidth = 0.7
        debitLabel.layer.borderWidth = 0.7
        creditLabel.layer.borderWidth = 0.7
        dailyLabel.layer.borderWidth = 0.7
        monthlyLabel.layer.borderWidth = 0.7
        yearlyLabel.layer.borderWidth = 0.7
        dailyDebitLabel.layer.borderWidth = 0.7
        dailyCeditLabel.layer.borderWidth = 0.7
        monthlyDebitLabel.layer.borderWidth = 0.7
        monthlyCreditLabel.layer.borderWidth = 0.7
        yearlyDebit.layer.borderWidth = 0.7
        yearlyCredit.layer.borderWidth = 0.7
        
        parentView.layer.borderColor = UIColor.black.cgColor
        limitLabel.layer.borderColor = UIColor.black.cgColor
        debitLabel.layer.borderColor = UIColor.black.cgColor
        creditLabel.layer.borderColor = UIColor.black.cgColor
        dailyLabel.layer.borderColor = UIColor.black.cgColor
        monthlyLabel.layer.borderColor = UIColor.black.cgColor
        yearlyLabel.layer.borderColor = UIColor.black.cgColor
        
        dailyDebitLabel.layer.borderColor = UIColor.black.cgColor
        dailyCeditLabel.layer.borderColor = UIColor.black.cgColor
        monthlyDebitLabel.layer.borderColor = UIColor.black.cgColor
        monthlyCreditLabel.layer.borderColor = UIColor.black.cgColor
        yearlyDebit.layer.borderColor = UIColor.black.cgColor
        yearlyCredit.layer.borderColor = UIColor.black.cgColor
        
    }
    
    func setupLimitValues(){
        
        dailyDebitLabel.text = response.2["DDEBIT"]
        dailyCeditLabel.text = response.2["DCREDIT"]
        monthlyDebitLabel.text = response.2["MDEBIT"]
        monthlyCreditLabel.text = response.2["MCREDIT"]
        yearlyDebit.text = response.2["YDEBIT"]
        yearlyCredit.text = response.2["YCREDIT"]
        
    }
    
    func myLimitsPostRequest() {
        
        self.showLoadingView()
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                xmlPath = Bundle.main.path(forResource: "Command-206-My Limits", ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                
                else { return }
            
            //let newStr = String(data: data, encoding: NSUTF8StringEncoding)
            //print(newStr)
            self.response = XMLParser.pinVerificationXMLParsing(data)
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
                DispatchQueue.main.async {
                    self.parentView.isHidden = false
                    self.okButton.isHidden = false
                    self.setupLimitValues()
                }
                
            }
            self.hideLoadingView()
            
        }else{
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let webApi : MyAccountWebAPI = MyAccountWebAPI()
            
            webApi.myLimitsRequest(
                Constants.CommandId.MY_LIMITS,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                ENCT: Constants.AppConfig.ENCT_KEY,
                onSuccess:{(data) -> () in
                    //print(data)
                    self.response = XMLParser.pinVerificationXMLParsing(data)
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
                        
                        DispatchQueue.main.async {
                            self.parentView.isHidden = false
                            self.okButton.isHidden = false
                            self.setupLimitValues()
                        }
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
    
    @IBAction func OKButtonPressed(_ sender: UIButton) {
        self.popViewControllerAndGotoStart()
    }
    
}
