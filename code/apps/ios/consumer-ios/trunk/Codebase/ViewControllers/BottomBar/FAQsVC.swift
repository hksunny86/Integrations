//
//  FAQsVC.swift
//  Timepey
//
//  Created by Adnan Ahmed on 29/09/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation
import UIKit


class FAQsVC: BaseViewController, UITableViewDelegate, UITableViewDataSource{
    
    var response = (XMLError(), XMLMessage(), [[String:String]]())
    var selectedRowIndex = IndexPath()
    var prevRowIndex: IndexPath?
    
    
    @IBOutlet weak var myTableView: UITableView!
    @IBOutlet weak var okButton: UIButton!
    
    var textViewSize: CGRect?
    var labelSize: CGRect?
    
    override func viewDidLoad() {
        //super.viewDidLoad()
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: false)
        faqsPostRequest()
        myTableView.tableFooterView = UIView()
        
        okButton.layer.cornerRadius = Constants.UI.Button.CORNER_RADIUS
        
        switch UIDevice.current.userInterfaceIdiom {
        case .phone:
            myTableView.estimatedRowHeight = 50
        case .pad:
            myTableView.estimatedRowHeight = 80
        default:
            break
        }
        myTableView.rowHeight = UITableView.automaticDimension
        
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return response.2.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        if(selectedRowIndex == indexPath){
            let cellIdentifier = "DetailCell"
            let cell: UITableViewCell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier)!
            
            let questionTextView = cell.viewWithTag(1) as? UITextView
            // top, left, bottom, right
            let answerTextView = cell.viewWithTag(2) as? UITextView
            
            answerTextView?.textAlignment = .justified
            
            switch UIDevice.current.userInterfaceIdiom {
            case .phone:
                questionTextView?.textContainerInset = UIEdgeInsets.init(top: 10,left: 10,bottom: 10,right: 10)
                answerTextView?.textContainerInset = UIEdgeInsets.init(top: 10,left: 10,bottom: 10,right: 10)
            case .pad:
                questionTextView?.textContainerInset = UIEdgeInsets.init(top: 15,left: 15,bottom: 15,right: 15)
                answerTextView?.textContainerInset = UIEdgeInsets.init(top: 15,left: 15,bottom: 15,right: 15)
            default:
                break
            }
            
            
            for (key, value) in response.2[indexPath.row]{
                questionTextView?.text = String(describing: key)
                answerTextView?.text = String(describing: value)
            }
            
            return cell
        }else{
            let cellIdentifier = "InfoCell"
            let cell: UITableViewCell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier)!
            
            let questionTextView = cell.viewWithTag(1) as? UITextView
            switch UIDevice.current.userInterfaceIdiom {
            case .phone:
                questionTextView?.textContainerInset = UIEdgeInsets.init(top: 10,left: 10,bottom: 10,right: 10)
            case .pad:
                questionTextView?.textContainerInset = UIEdgeInsets.init(top: 15,left: 15,bottom: 15,right: 15)
            default:
                break
            }
            
            
            for (key, _) in response.2[indexPath.row]{
                questionTextView?.text = String(describing: key)
            }
        
            return cell
        }
    }

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        
    
        if(selectedRowIndex == indexPath){
            selectedRowIndex = IndexPath()
        }else{
            selectedRowIndex = indexPath
        }
        
            
        tableView.deselectRow(at: indexPath, animated: true)
        
        myTableView.beginUpdates()
        let selectedIndexPath = IndexPath(row: indexPath.row, section: 0)
        myTableView.reloadRows(at: [selectedIndexPath], with: UITableView.RowAnimation.none)
        myTableView.endUpdates()
        //myTableView.reloadData()

    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableView.automaticDimension
    }
    
    
    @IBAction func okButtonPressed(_ sender: UIButton) {
        if(Customer.sharedInstance.appV == nil){
            self.goToCustomerLogin()
        }else{
            self.popViewControllerAndGotoStart()
        }
    }
    
    func faqsPostRequest() {
        
        self.showLoadingView()
        
        
        
        let fileName: String = "Command-180-FAQsCommand"
        
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                xmlPath = Bundle.main.path(forResource: fileName, ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                
                else { return }
            
            //let newStr = String(data: data, encoding: String.Encoding.utf8)
            //print(newStr)
            
            response = XMLParser.faqsXMLParsing(data)
            //print(response)
            if(response.0.msg != nil){
                if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                }else{
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                }
            }else if(response.1.msg != nil){
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.1.msg!, actionType: "", isCancelBtnHidden: true)
                
            }else{
                myTableView.reloadData()
            }
            self.hideLoadingView()
            
        }else{
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            var commandId: String?
            commandId = Constants.CommandId.FAQ
            let myAccApi : MyAccountWebAPI = MyAccountWebAPI()
            
            myAccApi.faqPostRequest(
                commandId!,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                FVNO: "5",
                onSuccess:{(data) -> () in
                    //let newStr = String(data: data, encoding: String.Encoding.utf8)
                    //print(newStr)
                    self.response = XMLParser.faqsXMLParsing(data)
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
                            self.myTableView.reloadData()
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
    
}




