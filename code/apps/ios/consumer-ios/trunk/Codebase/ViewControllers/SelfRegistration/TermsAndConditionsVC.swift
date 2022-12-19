//
//  SelfRegistrationFinalVC.swift
//  Timepey
//
//  Created by Adnan Ahmed on 24/09/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation
import UIKit

class TermsAndConditionsVC: FPAJSWalletAccountOpenBaseVC, UIWebViewDelegate {
    
    @IBOutlet weak var myWebView: UIWebView!
    @IBOutlet weak var acceptButton: UIButton!
    //var account: JSAccount!
    
    var response: [String:String]?
    
    override func viewDidLoad() {
        
        myWebView.isOpaque = false
        myWebView.backgroundColor = UIColor.clear
        
        var cornerRadious: CGFloat?
        
        switch UIDevice.current.userInterfaceIdiom {
        case .phone:
            cornerRadious = 2
        case .pad:
            cornerRadious = 4
        default:
            break
        }
        
        acceptButton.layer.cornerRadius = cornerRadious!
        
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: true)
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        let urlString = Constants.ServerConfig.TERMS_AND_CONDITION_URL
        if(Reachability.isConnectedToNetwork() == true){
            let url = URL (string: urlString)
            let requestObj = URLRequest(url: url!)
            myWebView.loadRequest(requestObj)
            
        }else{
//            let popupView = AlertPopup(nibName: "AlertPopup", bundle: nil)
//            popupView.headerLabelText = Constants.Message.ALERT_NOTIFICATION_TITLE
//            popupView.msgLabelText = Constants.Message.CONNECTIVITY_ISSUE
//            popupView.actionType = "serverFailure"
//            popupView.cancelButtonHidden = true
//            popupView.modalPresentationStyle = .overCurrentContext
//            self.present(popupView, animated: false, completion: nil)
            
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: Constants.Message.CONNECTIVITY_ISSUE, actionType: "serverFailure", isCancelBtnHidden: true)
            
        }
    }
    
    func webView(_ webView: UIWebView, shouldStartLoadWith request: URLRequest, navigationType: UIWebViewNavigationType) -> Bool {
        self.showLoadingView()
        return true
    }
    
    func webViewDidFinishLoad(_ webview: UIWebView) {
        self.hideLoadingView()
        
    }
    
    @IBAction func acceptButtonPressed(_ sender: UIButton) {
        //selfRegFinalPostRequest()
        let viewController = UIStoryboard(name: "SelfRegistration", bundle: nil).instantiateViewController(withIdentifier: "ConfirmRegistrationVC") as! ConfirmRegistrationVC
        viewController.account = account
        self.pushViewController(viewController)
    }
    
}
