//
//  MiniStatementVC.swift
//  Timepey
//
//  Created by Adnan Ahmed on 12/07/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation
import UIKit

class MiniStatementVC: BaseViewController, UITableViewDelegate, UITableViewDataSource {

    @IBOutlet weak var screenTitleLabel: UILabel!
    @IBOutlet weak var myTableView: UITableView!
    @IBOutlet weak var okButton: UIButton!
    @IBOutlet weak var tableHightConstraint: NSLayoutConstraint!
    
    @IBOutlet weak var btnTopConstraint: NSLayoutConstraint!

    var screenTitle: String?
    var pinText: String?
    var response = (XMLError(), XMLMessage(), [MiniStatement]())
    let userDefault = UserDefaults.standard
    var isHRA = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: false, isSignoutButtonHidden: false)
        
        myTableView.tableFooterView = UIView()
        
        okButton.layer.cornerRadius = 2
        myTableView.layer.cornerRadius = 4
        if isHRA == "1" {
            miniStatmentHRA()
        }
        else {
            miniStatment()
        }
    }
    
    
    func setupView(){
        var numOfRows = 0
        if(response.2.count > 0){
            numOfRows = response.2.count
        }
        
        var tableHight: Int?
        switch UIDevice.current.userInterfaceIdiom {
        case .pad:
            myTableView.rowHeight = 90
            tableHight = 95 * numOfRows
        case .phone:
            myTableView.rowHeight = 60
            tableHight = 65 * numOfRows
        default:
            break
        }
        
        
        var frameSize = myTableView.frame.size
        frameSize.height = CGFloat(tableHight!)
        myTableView.frame.size = frameSize
        tableHightConstraint.constant = CGFloat(tableHight!)
        
        var butnMargin: Int = 10
        
        if(numOfRows == 1){
            butnMargin = 60
        }else if(numOfRows == 2){
            butnMargin = 50
        }else if(numOfRows == 3){
            butnMargin = 40
        }else if(numOfRows == 4){
            butnMargin = 25
        }else if(numOfRows == 5){
            butnMargin = 10
        }
        
        btnTopConstraint.constant = CGFloat(butnMargin)
        
    }
    
    func handleResponse(response:(XMLError, XMLMessage, [MiniStatement])){

        if(response.0.msg != nil){
            if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
            }else{
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "", isCancelBtnHidden: true)
            }
        }else if(response.1.msg != nil){
            
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.1.msg!, actionType: "", isCancelBtnHidden: true)
            
        }else{
            DispatchQueue.main.async(){
                self.setupView()
                self.myTableView.reloadData()
                self.hideLoadingView()
            }
        }
    }
    
    
    func miniStatment() {
        
    
        self.showLoadingView()
        
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                xmlPath = Bundle.main.path(forResource: "Command-66-MiniStatment", ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                
                else { return }
            
            //let newStr = String(data: data, encoding: NSUTF8StringEncoding)
            //print(newStr)
            self.response = XMLParser.miniStatementXMLParsing(data)
            handleResponse(response: self.response)
            
        }else{
            
            
            let encryptedPin = try! pinText!.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            
            let myAccApi : MyAccountWebAPI = MyAccountWebAPI()
            
            //print(Customer.sharedInstance)
            
            myAccApi.miniStatementPostRequest(
                Constants.CommandId.MINI_STATEMENT,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                pin:encryptedPin ,
                ENCT: Constants.AppConfig.ENCT_KEY,
                ACCTYPE: "1",
                STNO: "1",
                ETNO:  "5",
                APID: "\(userDefault.object(forKey: "useracc")!)",
                BBACID: (Customer.sharedInstance.bank?.accounts[0].id)!,
                
                onSuccess:{(data) -> () in
                    
                    self.response = XMLParser.miniStatementXMLParsing(data)
                    self.handleResponse(response: self.response)
                },
                onFailure: {(reason) ->() in
                    //print("Failure")
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                    self.hideLoadingView()
            })
        }
    }
    
    func miniStatmentHRA() {
        
    
        self.showLoadingView()
        
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                xmlPath = Bundle.main.path(forResource: "Command-66-MiniStatment", ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                
                else { return }
            
            //let newStr = String(data: data, encoding: NSUTF8StringEncoding)
            //print(newStr)
            self.response = XMLParser.miniStatementXMLParsing(data)
            handleResponse(response: self.response)
            
        }else{
            
            
            let encryptedPin = try! pinText!.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            
            let myAccApi : MyAccountWebAPI = MyAccountWebAPI()
            
            //print(Customer.sharedInstance)
            
            myAccApi.miniStatementHRAPostRequest(
                Constants.CommandId.MINI_STATEMENT,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                pin:encryptedPin ,
                ENCT: Constants.AppConfig.ENCT_KEY,
                ACCTYPE: "1",
                STNO: "1",
                ETNO:  "5",
                APID: "\(userDefault.object(forKey: "useracc")!)",
                BBACID: (Customer.sharedInstance.bank?.accounts[0].id)!,
                ISHRA: "1",
                
                onSuccess:{(data) -> () in
                    
                    self.response = XMLParser.miniStatementXMLParsing(data)
                    self.handleResponse(response: self.response)
                },
                onFailure: {(reason) ->() in
                    //print("Failure")
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                    self.hideLoadingView()
            })
        }
    }
    
    
//Mark: UITableViewDelegate
    
    func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        return 5.0
    }
    
    func tableView(_ tableView: UITableView, viewForFooterInSection section: Int) -> UIView? {
        
        let headerView = UIView()
        headerView.backgroundColor = UIColor.clear
        
        return headerView
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return response.2.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }

    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cellIdentifier = "Cell"
        let cell: UITableViewCell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier)!
        
        
        let view = cell.viewWithTag(22)
        
        view?.layer.cornerRadius = 2
        view?.layer.shadowColor = UIColor.lightGray.cgColor
        view?.layer.shadowOffset = CGSize(width: 0, height: 0)
        view?.layer.borderWidth = 0
        view?.layer.shadowRadius = 2
        view?.layer.shadowOpacity = 0.4
        view?.layer.cornerRadius = 2
        
        
        if(response.2[(indexPath as NSIndexPath).section].date != nil){
            if let dateLabel = cell.viewWithTag(1) as? UILabel {
                dateLabel.text = response.2[(indexPath as NSIndexPath).section].datef!
            }
            if let descLabel = cell.viewWithTag(2) as? UILabel {
                descLabel.text = response.2[(indexPath as NSIndexPath).section].desc!
            }
            if let amountLabel = cell.viewWithTag(3) as? UILabel {
                amountLabel.text = "PKR \(response.2[(indexPath as NSIndexPath).section].tamtf!)"
            }
        }
        
        
        return cell
    }
    
    @IBAction func okButtonPressed(_ sender: UIButton) {
        self.popViewControllerAndGotoStart()
    }
}
