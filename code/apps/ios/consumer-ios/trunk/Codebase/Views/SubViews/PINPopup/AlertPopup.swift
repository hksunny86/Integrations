//
//  AlertPopup.swift
//  Timepey
//
//  Created by Adnan Ahmed on 12/07/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import UIKit
import Foundation


class AlertPopup: BaseViewController {

    @IBOutlet weak var parentView: UIView!
    @IBOutlet weak var transparentView: UIView!
    @IBOutlet weak var headerImage: UIImageView!
    @IBOutlet weak var headerLabel: UILabel!
    @IBOutlet weak var msgLabel: UILabel!
    
    @IBOutlet weak var okButtonLeadingConstraint: NSLayoutConstraint!
    @IBOutlet weak var widthOkButton: NSLayoutConstraint!
    
    @IBOutlet weak var okButtonTrailingConstraint: NSLayoutConstraint!
    @IBOutlet weak var okButton: UIButton!
    @IBOutlet weak var cancelButton: UIButton!
    
    var cancelButtonHidden = true
    var headerLabelText: String?
    var msgLabelText: String?
    var actionType: String?
    var VC: UIViewController?
    var isYesNoButtons: Bool?
    var appURLString: String?
    var setMpinBool:Bool?
    
    weak var delegate: AlertPopupDelegate?
    
    override func viewDidLoad() {
        
        self.dismissKeyboard()
        parentView.layer.cornerRadius = 5
        okButton.layer.cornerRadius = 2
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(true)

        
        if(actionType == "locationService" || actionType == "locationServiceMain"){
            okButton.setTitle("SETTINGS", for: .normal)
        }
        
        
        if(actionType == "9009-2" || actionType == "9009-3"){
            okButton.setTitle("DOWNLOAD", for: .normal)
        }
        
        if((headerLabelText) != nil){
            headerLabel.text = headerLabelText
        }
        
        if((msgLabelText) != nil){
            
//            if(actionType == "9009-2"){
//                let input = "\((msgLabelText)!)"
//                let detector = try! NSDataDetector(types: NSTextCheckingResult.CheckingType.link.rawValue)
//                let matches = detector.matches(in: input, options: [], range: NSRange(location: 0, length: input.utf16.count))
//                for match in matches {
//                    let url = (input as NSString).substring(with: match.range)
//                    appURLString = url
//                    let gesture = UITapGestureRecognizer(target: self, action: #selector(AlertPopup.userTappedOnLink))
//                    // if labelView is not set userInteractionEnabled, you must do so
//                    msgLabel.isUserInteractionEnabled = true
//                    msgLabel.addGestureRecognizer(gesture)
//                }
//            }
            
            msgLabel.text = msgLabelText
            
        }
        
        if(isYesNoButtons == true){
            okButton.setTitle("YES", for: .normal)
            cancelButton.setTitle("NO", for: .normal)
        }
        
        if(headerLabelText == Constants.Message.ALERT_NOTIFICATION_TITLE_PGR_SUCCESS || headerLabelText == Constants.Message.ALERT_NOTIFICATION_TITLE_IPCR_SUCCESS || headerLabelText == Constants.Message.ALERT_NOTIFICATION_TITLE_IMPCR_SUCCESS || headerLabelText == Constants.Message.ALERT_NOTIFICATION_TITLE_HRA_SUCCESS) {
            headerImage.image = UIImage(named: "popup_notification_success_message_icon.png")
        }
    }
    
    override func viewDidLayoutSubviews() {
        if(cancelButtonHidden == true){
            cancelButton.isHidden = true
            switch UIDevice.current.userInterfaceIdiom {
            case .phone:
                
                okButtonTrailingConstraint.constant = -260
            case .pad:
                okButtonTrailingConstraint.constant = 10
            default:
                break
            }
        }
        else {
            okButtonTrailingConstraint.constant = 7
        }
    }
    
    func userTappedOnLink() {
        UIApplication.shared.openURL(URL(string: appURLString!)!)
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        transparentView.backgroundColor = UIColor.clear
    }
    @IBAction func okPressed(_ sender: UIButton) {
        
        self.dismissKeyboard()
        
        if(actionType == "locationService") {
            UIApplication.shared.openURL(URL(string:UIApplication.openSettingsURLString)!)
            //UIApplication.shared.openURL(URL(string:"prefs:root=Privacy&path=LOCATION")!)
            DispatchQueue.main.async {
                self.dismiss(animated: false, completion: nil)
            }
            
        } else if(actionType == "locationServiceMain") {
            UIApplication.shared.openURL(URL(string:UIApplication.openSettingsURLString)!)
            //UIApplication.shared.openURL(URL(string:"prefs:root=LOCATION_SERVICES")!)
            DispatchQueue.main.async {
                self.dismiss(animated: false, completion: nil)
            }
            //self.goToCustomerLogin()
            
        }
        else if(actionType == "9009-2" || actionType == "9009-3") {
            UIApplication.shared.openURL(URL(string: Constants.Message.APP_DOWNLOAD_URL)!)
        }
        else if(actionType == "locationService") {
            let presentingViewController = self.presentingViewController
            DispatchQueue.main.async {
                self.dismiss(animated: false, completion: {
                    presentingViewController!.dismiss(animated: false, completion: {})
                })
            }
            self.popViewController()
        }
        else if(actionType == "OPTPinVerificationSuccess") {
            let presentingViewController = self.presentingViewController
            DispatchQueue.main.async {
                self.dismiss(animated: false, completion: {
                    presentingViewController!.dismiss(animated: false, completion: {})
                })
            }
            delegate?.okPressedAP()
        }
        else if(actionType == "SignOut") {
            self.signoutPostRequest()
            if let navController = UIApplication.shared.keyWindow?.rootViewController {
                DispatchQueue.main.async {
                    if let firstVC = navController.presentedViewController{
                        if let secVC = firstVC.presentedViewController{
                            secVC.dismiss(animated: false, completion: {
                                firstVC.dismiss(animated: false, completion: {
                                })
                            })
                        }else{
                            firstVC.dismiss(animated: false, completion: {})
                        }
                    }
                }
            }
        }else if(actionType == "signoutSuccess") {
            super.goToCustomerLogin()
            let presentingViewController = self.presentingViewController
            DispatchQueue.main.async {
                self.dismiss(animated: false, completion: {
                    presentingViewController!.dismiss(animated: false, completion: {})
                })
            }
        }else if(actionType == "serverFailure") {
            DispatchQueue.main.async {
                self.dismiss(animated: false, completion: nil)
            }
        }else if(actionType == "pinVerificationFailed" || actionType == "signInFailure" || actionType == "requestDenied"){
            DispatchQueue.main.async {
                self.dismiss(animated: false, completion: nil)
            }
        } else if(actionType == "noInternetAvailable") {
            DispatchQueue.main.async {
                self.dismiss(animated: false, completion: nil)
            }
        } else if(actionType == "changeMPIN") {
            DispatchQueue.main.async {
                self.dismiss(animated: false, completion: nil)
            }
            let viewController = UIStoryboard(name: "MyAccount", bundle: nil).instantiateViewController(withIdentifier: "ChangePinVC") as! ChangePin
            viewController.isNewUser = true
            viewController.screenTitle = "Change MPIN"
            viewController.productId = Constants.ProductID.CHANGE_MPIN
            self.pushViewController(viewController)
        } else if(actionType == "setMPIN") {
            DispatchQueue.main.async {
                self.dismiss(animated: false, completion: nil)
            }
            let viewController = UIStoryboard(name: "MyAccount", bundle: nil).instantiateViewController(withIdentifier: "SetMPINVC") as! SetMPINVC
            viewController.isMoveToLogin = true
            self.pushViewController(viewController)
        }
        else if(actionType == "delegate") {
            
            let presentingViewController = self.presentingViewController
            DispatchQueue.main.async {
                self.dismiss(animated: false, completion: {
                    presentingViewController!.dismiss(animated:false, completion: {})
                })
            }
            delegate?.okPressedAP()
        }else if(actionType == "goToMainMenu"){
            DispatchQueue.main.async {
                self.dismiss(animated: false, completion: nil)
            }
            self.PushVCToMainMenu()
        }
        else if actionType == "popVC" {
            DispatchQueue.main.async {
                self.dismiss(animated: false, completion: nil)
            }
            self.popViewController()
        }
        else {
            self.popViewControllerAndGotoStart()
            DispatchQueue.main.async {
                self.dismiss(animated: false, completion: nil)
            }
        }

    }
    func signoutPostRequest(){
        let reachabilityManager = Reachability.isConnectedToNetwork()
        
        if(reachabilityManager != true){
            Customer.destroy()
            self.goToCustomerLogin()
        }else{
            var response = (XMLError(), XMLMessage(), String())
            
            self.showLoadingView()
            if(Constants.AppConfig.IS_MOCK == 1){
                guard let
                    xmlPath = Bundle.main.path(forResource: "Command-128-Signout", ofType: "xml"),
                    let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                    else { return }
                
                response = XMLParser.customerSignoutXMLParsing(data)
                
                if(response.0.msg != nil){
                    if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                        Customer.destroy()
                        self.goToCustomerLogin()
                        
                    }else{
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "", isCancelBtnHidden: true)
                    }
                }else if(response.1.msg != nil){
                    
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.1.msg!, actionType: "", isCancelBtnHidden: true)
                    
                }else{
                    Customer.destroy()
                    self.goToCustomerLogin()
                }
                self.hideLoadingView()
                
            }else{
                
                let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
                
                let LogInApi : UserAccountAPI = UserAccountAPI()
                LogInApi.customerSignoutPostRequest(
                    Constants.CommandId.SIGNOUT,
                    reqTime: currentTime,
                    DTID:Constants.AppConfig.DTID_KEY,
                    onSuccess: {(data) -> () in
                        
                        response = XMLParser.customerSignoutXMLParsing(data)
                        
                        if(response.0.msg != nil){
                            if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                                Customer.destroy()
                                self.goToCustomerLogin()
                            }else{
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "", isCancelBtnHidden: true)
                            }
                        }else if(response.1.msg != nil){
                            
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.1.msg!, actionType: "", isCancelBtnHidden: true)
                            
                        }else{
                            
                            Customer.destroy()
                            self.goToCustomerLogin()
                        }
                        self.hideLoadingView()
                }, onFailure: {(reason) ->() in
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "noInternetAvailable", isCancelBtnHidden: true)
                    self.hideLoadingView()
                })
            }
        }
        
    }
    @IBAction func CancelPressed(_ sender: UIButton) {
        DispatchQueue.main.async {
            self.dismiss(animated: false, completion: nil)
        }
        if(actionType == "locationService"){
            self.popViewController()
        }else if(actionType == "locationServiceMain"){
            self.popViewController()
        }
    }
}
