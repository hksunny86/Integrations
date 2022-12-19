//
//  CustomWebView.swift
//  Timepey
//
//  Created by Adnan Ahmed on 20/09/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation
import UIKit
import WebKit
import SwiftyJSON
import Security

class CustomWebView: BaseViewController, AlertPopupDelegate, UIWebViewDelegate, WKUIDelegate, WKNavigationDelegate, WKScriptMessageHandler {
    
    
    
    @IBOutlet weak var headerView: HeaderTitleView!
    @IBOutlet weak var screenTitleLabel: UILabel!
    @IBOutlet weak var screenTitleImage: UIImageView!
    @IBOutlet weak var webViewContainer: UIView!
    @IBOutlet weak var webViewContainerTopConstraint: NSLayoutConstraint!
    
    
    var webView:WKWebView!
    var screenTitleText: String?
    var imgString: String?
    var urlString: String?
    var isBottomBarHidden = false
    var product = Product()
    lazy var backButton = UIButton()
    var jsonString = String()
    var responseDict = [String:String]()
    var arrivalCity = ""
    var departureCity = ""
    var bName = ""
    var bCnic = ""
    var bNumber = ""
    var bEmail = ""
    var response = (XMLError(), XMLMessage(), [String:String]())
    
    
    
    
    override func viewWillAppear(_ animated: Bool) {
        
        if(Reachability.isConnectedToNetwork() == true) {
            if imgString == "heading_icon_terms" {
                if(urlString != nil) {
                    
                    let url = URL(string: urlString!)
                    let requestObj = URLRequest(url: url!)
                    webView.load(requestObj)
                    
                }
            }
            else {
                setupBackButton()
                let customername = (Customer.sharedInstance.fName ?? "") + (Customer.sharedInstance.lName ?? "")
                let phoneNumber = Customer.sharedInstance.cMob ?? ""
                let cnic = Customer.sharedInstance.cnic ?? ""
                let data = "name=\(customername)&cnic=\(cnic)&email=\(Customer.sharedInstance.email!)&mobile_no=\(phoneNumber)&api_key=\(Constants.BOOKME.APIKEY)"
                let encryptedData = RSA.encrypt(string: data, publicKey: Constants.BOOKME.RSA_PUBLIC_KEY)
                let encodedData = encryptedData?.escape(string: encryptedData!)
                if(urlString != nil) {
                    
                    let url = URL(string: urlString! + encodedData!)
                    let requestObj = URLRequest(url: url!)
                    webView.load(requestObj)
                }
            }
        }
        else {
            let popupView = AlertPopup(nibName: "AlertPopup", bundle: nil)
            popupView.headerLabelText = Constants.Message.ALERT_NOTIFICATION_TITLE
            popupView.msgLabelText = Constants.Message.CONNECTIVITY_ISSUE
            popupView.actionType = "delegate"
            popupView.cancelButtonHidden = true
            popupView.delegate = self
            popupView.isMpinSetLater = self.isMpinSetLater
            popupView.modalPresentationStyle = .overCurrentContext
            self.present(popupView, animated: false, completion: nil)
            
        }
        
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        if imgString == "heading_icon_terms" {
            webViewContainerTopConstraint.constant = 15
        } else {
            headerView.isHidden = true
            webViewContainer.topAnchor.constraint(equalTo: view.layoutMarginsGuide.topAnchor, constant: 0).isActive = true
        }
        webViewContainer.layoutIfNeeded()
    }
    
    override func viewDidLoad() {
        
        super.viewDidLoad()
        
        if imgString == "heading_icon_terms" {
            
            self.isTermsViewController = true
            if self.isBottomBarHidden {
                self.bottomBarView.isHidden = true
            }
            self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: false)
            
        } else {
            self.isTermsViewController = false
        }
        
        if(screenTitleText != nil) {
            screenTitleLabel.text = screenTitleText!
        }
        
        if(imgString != nil) {
            screenTitleImage.image = UIImage(named: imgString!)
        }
        
        setupWebView()
        
    }

    //MARK: WebView Delegate
    func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
        guard let urlAsString = navigationAction.request.url?.absoluteString.lowercased() else {
            return
            
        }
        if urlAsString.starts(with: Constants.BOOKME.BUSES_URL) || urlAsString.starts(with: Constants.BOOKME.AIRLINES_URL) || urlAsString.starts(with: Constants.BOOKME.HOTELS_URL) || urlAsString.starts(with: Constants.BOOKME.CINEMAS_URL) || urlAsString.starts(with: Constants.BOOKME.EVENTS_URL) {
            backButton.isUserInteractionEnabled = true
        }
        else {
            backButton.isUserInteractionEnabled = false
        }
        decisionHandler(.allow)
    }
    
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        
        let currentUrlString = webView.url?.absoluteString
        if currentUrlString?.starts(with: "https://bookme.pk/widgets/jsmb/shiftControl?order_ref_id=") == true {
            self.webView.isHidden = true
            navigateToConfirmation()
        }
        
    }
    
    func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        if message.name == "JSBookME", let messageBody = message.body as? String {
            print(messageBody)
        }
    }
    
    //MARK: Functions
    func setupBackButton() {
        
        let margins = view.layoutMarginsGuide

        backButton = UIButton()
        self.view.addSubview(backButton)
        backButton.translatesAutoresizingMaskIntoConstraints = false
        backButton.widthAnchor.constraint(equalToConstant: 80).isActive = true
        backButton.heightAnchor.constraint(equalToConstant: 50).isActive = true
        backButton.topAnchor.constraint(equalTo: margins.topAnchor, constant: 5).isActive = true
        backButton.leadingAnchor.constraint(equalTo: margins.leadingAnchor,constant: -5).isActive = true
        backButton.addTarget(self, action: #selector(goToMainMenu), for: .touchUpInside)
    }
    
    @objc func goToMainMenu() {
        self.popViewController()
    }
    
    func setupWebView() {
        
        let preferences = WKPreferences()
        preferences.javaScriptEnabled = true
        
        let config = WKWebViewConfiguration()
        config.preferences = preferences
        config.userContentController = WKUserContentController()
        config.userContentController.add(self, name: "JSBookME")
        
        
        let customFrame = CGRect.init(origin: CGPoint.zero, size: CGSize.init(width: 0.0, height: self.webViewContainer.frame.size.height))
        self.webView = WKWebView (frame: customFrame , configuration: config)
        webView.translatesAutoresizingMaskIntoConstraints = false
        self.webViewContainer.addSubview(webView)
        webView.topAnchor.constraint(equalTo: webViewContainer.topAnchor).isActive = true
        webView.rightAnchor.constraint(equalTo: webViewContainer.rightAnchor).isActive = true
        webView.leftAnchor.constraint(equalTo: webViewContainer.leftAnchor).isActive = true
        webView.bottomAnchor.constraint(equalTo: webViewContainer.bottomAnchor).isActive = true
        webView.heightAnchor.constraint(equalTo: webViewContainer.heightAnchor).isActive = true
        webView.uiDelegate = self
        webView.navigationDelegate = self
        webView.isOpaque = false
        webView.backgroundColor = UIColor.clear
    }
    
    func okPressedAP() {
        if(Customer.sharedInstance.appV == nil){
            self.goToCustomerLogin()
        } else {
            self.popViewControllerAndGotoStart()
        }
    }
    
    func canclePressedAP() {
    }
    
    func navigateToConfirmation() {
        
        webView.evaluateJavaScript("document.getElementsByTagName('html')[0].innerHTML") { innerHTML, error in
            if let htmlString = innerHTML as? String {
                let str = htmlString.stripOutHtml()
                let data: Data = (str?.data(using: String.Encoding(rawValue: String.Encoding.utf8.rawValue)))!
                do {
                    let swiftyObj = try JSON(data: data)
                    self.showLoadingView()
                    for (key, object) in swiftyObj {
                        self.responseDict[key] = object.stringValue
                    }
                    let orderDetails = swiftyObj["ORDER_DETAILS"].dictionaryValue
                    for (key, object) in orderDetails {
                        self.responseDict[key] = object.stringValue
                    }
                    if self.responseDict["TYPE"] == "transport" {
                        self.responseDict["TYPE"] = "bus"
                    }
                    
                    if self.product.fID == Constants.FID.BOOKME_AIR {
                        self.departureCity = (orderDetails["flight_from"]?.string)!
                        self.arrivalCity = (orderDetails["flight_to"]?.string)!
                        
                    } else if self.product.fID == Constants.FID.BOOKME_BUSES {
                        let deptCity = orderDetails["departure_city"]?.dictionary
                        let arrivalCity = orderDetails["arrival_city"]?.dictionary
                        if let deptCity = deptCity?["city_name"]?.string {
                            self.departureCity = deptCity
                        }
                        if let arrivalCity = arrivalCity?["city_name"]?.string {
                            self.arrivalCity = arrivalCity
                        }
                    } else if self.product.fID == Constants.FID.BOOKME_CINEMA {
                        let movieDetails = orderDetails["departure_city"]?.dictionary
                        let cinemaDetails = orderDetails["cinema_name"]?.dictionary
                        if let movieTitle = movieDetails?["title"]?.string {
                            self.departureCity = movieTitle
                        }
                        if let cinemaName = cinemaDetails?["cinema_name"]?.string {
                            self.arrivalCity = cinemaName
                        }
                        
                    } else if self.product.fID == Constants.FID.BOOKME_EVENTS {
                        let eventDetails = orderDetails["event_name"]?.dictionary
                        self.responseDict["name"] = eventDetails?["name"]?.string
                        if let duration = eventDetails?["duration"]?.string {
                            self.departureCity = duration
                        }
                        if let venue = eventDetails?["venue"]?.string {
                            self.arrivalCity = venue
                        }
                    }
                    if let bNumber = orderDetails["contact_no"]?.string {
                        self.bNumber = bNumber
                    }
                    if let bCnic = orderDetails["cnic"]?.string {
                        self.bCnic = bCnic
                    }
                    if let bEmail = orderDetails["email"]?.string {
                        self.bEmail = bEmail
                    }
                    if let bName = orderDetails["name"]?.string {
                        self.bName = bName
                    }
                    
                    self.bookMeInfoRequest()
                } catch _ {
                    self.print(error?.localizedDescription ?? "No JSON Object")
                    
                }
            }
        }
    }
    
    
    //MARK: Web Requests
    func bookMeInfoRequest() {
        
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                    xmlPath = Bundle.main.path(forResource: "Command-206-My Limits", ofType: "xml"),
                  let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
            else { return }
            self.response = XMLParser.pinVerificationXMLParsing(data)
            
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
                    self.webView.isHidden = true
                }
                
            }
            self.hideLoadingView()
            
        }else{
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let webApi : TransactionAPI = TransactionAPI()
            
            webApi.BookMeInfoRequest(
                Constants.CommandId.BOOKME_INFO,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                PID: self.product.id!,
                CMOB: Customer.sharedInstance.cMob!,
                BAMT: self.responseDict["AMOUNT"]!,
                PMTTYPE: "0",
                ORDER_ID:self.responseDict["ORDER_REF_ID"]!,
                STYPE: self.responseDict["TYPE"]!,
                SPNAME: self.responseDict["SERVICE_PROVIDER"]!,
                BNAME:self.bName,
                BCNIC:self.bCnic,
                BMOB:self.bNumber,
                BEMAIL:self.bEmail,
                onSuccess:{(data) -> () in
                    self.response = XMLParser.paramTypeXMLParsing(data)
                    self.hideLoadingView()
                    if(self.response.0.msg != nil) {
                        if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                        }else{
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "goToMainMenu", isCancelBtnHidden: true)
                        }
                    }else if(self.response.1.msg != nil){
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "goToMainMenu", isCancelBtnHidden: true)
                        
                    }else{
                        DispatchQueue.main.async {
                            let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "ConfirmationVC") as! ConfirmationVC
                            viewController.product = self.product
                            viewController.responseDict = self.response.2
                            viewController.orderDetails = self.responseDict
                            viewController.departureCity = self.departureCity
                            viewController.arrivalCity = self.arrivalCity
                            viewController.isMpinSetLater = self.isMpinSetLater
                            self.pushViewController(viewController)
                        }
                    }
                },
                onFailure: {(reason) ->() in
                    //print("Failure")
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                    self.popViewControllerAndGotoStart()
                    self.hideLoadingView()
                })
            
        }
        
    }
}
