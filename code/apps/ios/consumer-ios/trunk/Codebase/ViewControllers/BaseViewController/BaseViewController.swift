//
//  BaseViewController.swift
//  Timepey
//
//  Created by Adnan Ahmed on 02/06/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import UIKit
import IQKeyboardManagerSwift

class BaseViewController: UIViewController, BottomBarDelegate, HeaderBarDelegate {
    
    let bottomBarView = BottomBarView()
    let headerBarView = HeaderBarView()
    let alertView = AlertView()
    var isTermsViewController = false
    var isDebitCardTermsVC = false
    var isChangePIN = false
    var isForgetLoginPIN = false
    var isMore: Bool = false
    var isLocator: Bool?
    var isMyAccount: Bool?
    var customeCatelog = [Category]()
    var moreCatelog = [Category]()
    var isMpinSetLater = false
    
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        //Setup Bottom Bar
        if !(isTermsViewController || isDebitCardTermsVC || isChangePIN || isForgetLoginPIN) {
            setupBottomBarView()
        }
        isMpinSetLater = UserDefaults.standard.bool(forKey: "isMpinSetLater")
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.dismissKeyboard()
    }
    
    func setupHeaderBarView(_ headerTitle:String, isBackButtonHidden: Bool, isHomeButtonHidden: Bool, isSignoutButtonHidden: Bool) {
        
        let screenSize = UIScreen.main.bounds.size
        
        var headerFrame = headerBarView.frame
        headerFrame.size.width = screenSize.width
        
        switch UIDevice.current.userInterfaceIdiom {
        case .phone:
            headerFrame.size.height = 64
        case .pad:
            headerFrame.size.height = 100
        default:
            break
        }
        if Constants.DeviceType.IS_IPHONE_X {
            headerFrame.origin.y = 45
        }
        else {
            headerFrame.origin.y = 0
        }
        headerFrame.origin.x = 0
        headerBarView.xibSetup(self, viewFrame: headerFrame, isBackButtonHidden, isHomeButtonHidden: isHomeButtonHidden, isSignoutButtonHidden: isSignoutButtonHidden, title: headerTitle)
        headerBarView.frame = headerFrame
        self.view.addSubview(headerBarView)
    
    }
    
    func setupBottomBarView() {
        
        let screenSize = UIScreen.main.bounds.size
        var frame = self.bottomBarView.frame
        frame.size.width = screenSize.width
        let bottomBarheight: CGFloat = screenSize.width * 0.165625
        frame.size.height = bottomBarheight
        if Constants.DeviceType.IS_IPHONE_X {
            frame.origin.y = screenSize.height - frame.size.height - 30
        }
        else
        {
            frame.origin.y = screenSize.height - frame.size.height
        }
        frame.origin.x = 0
        DispatchQueue.main.async {
            self.bottomBarView.xibSetup(self, viewFrame: frame)
            self.bottomBarView.frame = frame
            self.view.addSubview(self.bottomBarView)
        }
        
        
    }

//MARK: Bottom Bar Delegate
    func actBottomBtn(_ buttonPressedType: BOTTOM_BAR_BUTTON_PRESSED){
        if(buttonPressedType == BOTTOM_BAR_BUTTON_PRESSED.bottom_BAR_LOCATOR){
            if(Customer.sharedInstance.appV == nil){
                self.goToCustomerLogin()
            }else{
                self.popViewControllerAndGotoStart()
            }
        }
        else if(buttonPressedType == BOTTOM_BAR_BUTTON_PRESSED.bottom_BAR_MYACCOUNT){
            if (isMyAccount != false){
                if(customeCatelog.count == 0){
                    populateMyAccountMenu()
                }
                let viewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "ProductListingVC") as! ProductListingVC
                viewController.categoryArray = customeCatelog[0].categories
                viewController.mainCatID = customeCatelog[0].cID
                viewController.catLable = customeCatelog[0].name
                viewController.isMpinSetLater = self.isMpinSetLater
                self.pushViewController(viewController)
            }
        }
        else if(buttonPressedType == BOTTOM_BAR_BUTTON_PRESSED.bottom_BAR_QRPAY) {
            if !(self is RetailPaymentMasterPassVC || self is MasterPassInputVC || self is ScanQRMasterPassVC || self is MasterPassSuccessVC || self is ScanQRCodeVC ||  self is ScanQRMasterPassConfrimVC  ) {
                if isMpinSetLater == false {
                    let viewController = UIStoryboard(name: "RetailPayment", bundle: nil).instantiateViewController(withIdentifier: "RetailPaymentMasterPassVC") as! RetailPaymentMasterPassVC
                    self.pushViewController(viewController)
                    
                }
                  else {
                    alertView.initWithSetMPINNowWithSetMPINLater(title: "SET MPIN", message: "Dear Customer, you have not setup your MPIN yet, please set up the MPIN", SetMPINNowPressed: {
                        self.alertView.hide()
                        let viewController = UIStoryboard(name: "MyAccount", bundle: nil).instantiateViewController(withIdentifier: "SetMPINVC") as! SetMPINVC
                        self.pushViewController(viewController)
                    }, SetMPINLaterPressed: {
                        self.alertView.hide()
                    })
                    alertView.show(parentView: view)
                }
            }
            
        }
        else if(buttonPressedType == BOTTOM_BAR_BUTTON_PRESSED.bottom_BAR_CONTACT_US){
            
            if(isLocator != false){
                if(Reachability.isConnectedToNetwork() == true){
                    let viewController = UIStoryboard(name: "Locator", bundle: nil).instantiateViewController(withIdentifier: "LocatorVC") as! LocatorVC
                    self.pushViewController(viewController)
                    
                }else{
                    self.showNoInternetPopup()
                }
            }
        }
        else if(buttonPressedType == BOTTOM_BAR_BUTTON_PRESSED.bottom_BAR_FAQ) {
                createMoreMenu()
                let viewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "ProductListingVC") as! ProductListingVC
                viewController.mainCatID = "420"
                viewController.catLable = "Help"
                viewController.categoryArray = moreCatelog[0].categories
            viewController.isMpinSetLater = self.isMpinSetLater
                self.pushViewController(viewController)
            
        }
    }
    
//MARK: Header Bar Delegate
    func actHeaderBtn(_ buttonPressedType: HEADER_BAR_BUTTON_PRESSED){
        //print(Customer.sharedInstance.appV)
        if(buttonPressedType == HEADER_BAR_BUTTON_PRESSED.HEADER_BACK_BUTTON){
            if  self is CustomWebView || self is LocatorVC {
                if(Customer.sharedInstance.appV == nil){
                    self.goToCustomerLoginNew()
                }else{
                    self.popViewControllerAndGotoStart()
                }
            }
            else if self is ScanQRMasterPassConfrimVC {
                self.popViewControllerAndGoToScan()
            }
            else if (isMyAccount == false){
                self.popViewControllerAndGotoStart()
            }
            else if self is MasterPassInputVC {
                self.goToPayNow()
            }
            else if self is ConfirmationVC {
                if isMore {
                    self.popViewControllerAndGotoStart()
                }
                else {
                    self.popViewController()
                }
            }
            else{
                self.popViewController()
            }
        }
        else if(buttonPressedType == HEADER_BAR_BUTTON_PRESSED.HEADER_SIGNOUT_BUTTON){
            if(Customer.sharedInstance.appV != nil){
                self.signoutCustomer()
            }
        }
    }
    
    func createMoreMenu(){
        let more: Category = Category()
        more.cID = "17"
        more.name = "More"
        more.icon = "main_icon_my_account.png"
        more.isProduct = "0"
        
        let contactUs = Category()
        contactUs.cID = "541"
        contactUs.name = "Contact Us"
        contactUs.isProduct = "1"
        let contactUsP = Product()
        contactUsP.id = "10029"
        contactUsP.fID = "540"
        contactUsP.name = "Contact Us"
        contactUs.products.append(contactUsP)
        
        more.categories.append(contactUs)
        
        
        let faqs = Category()
        faqs.cID = "541"
        faqs.name = "FAQs"
        faqs.isProduct = "1"
        
        let faqsP = Product()
        faqsP.id = "10027"
        faqsP.fID = "541"
        faqsP.name = "FAQs"
        faqs.products.append(faqsP)
        
        more.categories.append(faqs)
        
        let termsAndConditions = Category()
        termsAndConditions.cID = "542"
        termsAndConditions.name = "Terms And Conditions"
        termsAndConditions.isProduct = "1"
        
        let termsAndConditionsP = Product()
        termsAndConditionsP.id = "10093"
        termsAndConditionsP.fID = "542"
        termsAndConditionsP.name = "Terms And Conditions"
        termsAndConditions.products.append(termsAndConditionsP)
        
        more.categories.append(termsAndConditions)
        
        moreCatelog.append(more)
    }
    
    func populateMyAccountMenu(){
        
        let myAccount: Category = Category()
        myAccount.cID = "7"
        myAccount.name = "My Account"
        myAccount.icon = "main_icon_my_account.png"
        myAccount.isProduct = "0"
        
        let miniStatement = Category()
        miniStatement.cID = "15"
        miniStatement.name = "Mini Statement"
        miniStatement.isProduct = "1"
        
        let blbACStatment = Product()
        blbACStatment.id = "10029"
        blbACStatment.fID = "502"
        blbACStatment.name = "BB Account"
        miniStatement.products.append(blbACStatment)
        
        let hraACStatement = Product()
        hraACStatement.id = "10036"
        hraACStatement.fID = "103"
        hraACStatement.name = "HRA Account"
        miniStatement.products.append(hraACStatement)
        
    
        
        myAccount.categories.append(miniStatement)
        
        let managePin = Category()
        managePin.cID = "500"
        managePin.name = "PIN Management"
        managePin.isProduct = "0"

        let ChangeLoginPIN = Product()
        ChangeLoginPIN.id = "10027"
        ChangeLoginPIN.fID = "500"
        ChangeLoginPIN.name = "Change Login PIN"


        managePin.products.append(ChangeLoginPIN)

        let changeMPIN = Product()
        changeMPIN.id = "10028"
        changeMPIN.fID = "510"
        changeMPIN.name = "Change MPIN"

        managePin.products.append(changeMPIN)

        if UserDefaults.standard.bool(forKey: "isMpinSetLater") == true {
            let regenMPIN = Product()
            regenMPIN.id = "10035"
            regenMPIN.fID = "515"
            regenMPIN.name = "SET MPIN"
            managePin.products.append(regenMPIN)
        }

        let forgotMPIN = Product()
        forgotMPIN.id = "10099"
        forgotMPIN.fID = "516"
        forgotMPIN.name = "Forgot MPIN"
        managePin.products.append(forgotMPIN)

        myAccount.categories.append(managePin)
        
        
        let balanceInquery = Category()
        balanceInquery.cID = "13"
        balanceInquery.name = "Check Balance"
        balanceInquery.isProduct = "1"
        
        let BLBAC = Product()
        BLBAC.id = "10025"
        BLBAC.fID = "501"
        BLBAC.name = "BB Account"
        balanceInquery.products.append(BLBAC)
        
        let HRAAC = Product()
        HRAAC.id = "10026"
        HRAAC.fID = "101"
        HRAAC.name = "HRA Account"
        balanceInquery.products.append(HRAAC)
        
        myAccount.categories.append(balanceInquery)
        
        let ibanInquery = Category()
        ibanInquery.cID = "14"
        ibanInquery.name = "View My IBAN"
        ibanInquery.isProduct = "1"
        
        let checkIBAN = Product()
        checkIBAN.id = "10033"
        checkIBAN.fID = "1001"
        checkIBAN.name = "View My IBAN"
        ibanInquery.products.append(checkIBAN)
        
        myAccount.categories.append(ibanInquery)
        
        
        let myLimits = Category()
        myLimits.cID = "18"
        myLimits.name = "My Limits"
        myLimits.isProduct = "1"
        
        let myLimitsPrd = Product()
        myLimitsPrd.id = "10032"
        myLimitsPrd.fID = "1000"
        myLimitsPrd.name = "My Limits"
        myLimits.products.append(myLimitsPrd)
        myAccount.categories.append(myLimits)
        
        customeCatelog.append(myAccount)
        

        
        //Card Service
        let cardServices = Category()
        cardServices.cID = "19"
        cardServices.name = "Card Services"
        cardServices.icon = "main_icon_card_services.png"
        cardServices.isProduct = "0"
        
        let carsAct = Product()
        carsAct.id = "10043"
        carsAct.fID = "509"
        carsAct.name = "Card Activation"
        cardServices.products.append(carsAct)
        
        let atmPinChange = Product()
        atmPinChange.id = "10041"
        atmPinChange.fID = "507"
        atmPinChange.name = "ATM PIN Generation"
        cardServices.products.append(atmPinChange)
        
        let pinChange = Product()
        pinChange.id = "10040"
        pinChange.fID = "506"
        pinChange.name = "ATM PIN Change"
        cardServices.products.append(pinChange)
        
        let atmBlock = Product()
        atmBlock.id = "10042"
        atmBlock.fID = "508"
        atmBlock.name = "Temporary Block"
        cardServices.products.append(atmBlock)
        
        //myAccount.categories.append(cardServices)
        
    }
//MARK: Keyboard integration
    
    @objc func dismissKeyboard() {
        IQKeyboardManager.shared.shouldResignOnTouchOutside = true
        self.view.endEditing(true)
    }

    
//MARK: No InternetPopUp
    func showNoInternetPopup() {
        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: Constants.Message.CONNECTIVITY_ISSUE, actionType: "serverFailure", isCancelBtnHidden: true)
    }
    
//MARK: Custom OTP Popups
    
    func showOTPPinPopup(_ messageText: String, otpFlow: String, delegate: OTPDelegate, isResendButtonHidden: Bool){
        
        let popupView = OTPPinPopup(nibName: "OTPPinPopup", bundle: nil)
        popupView.messageText = messageText
        popupView.otpFlow = otpFlow
        popupView.delegate = delegate
        popupView.isResendButtonHidden = isResendButtonHidden
        popupView.modalPresentationStyle = .overCurrentContext
        self.present(popupView, animated: false, completion: nil)
    }
    
//MARK: Financial Pin Popup

    func showFinancialPinPopup(_ accountType: String, requiredAction: String, delegate: FinancialPinPopupDelegate, productFlowID: String, productId: String) {
        DispatchQueue.main.async(execute: {
            let popupView = FinancialPinPopup(nibName: "FinancialPinPopup", bundle: nil)
            popupView.accountType = accountType
            popupView.requiredAction = requiredAction
            popupView.delegate = delegate
            popupView.product.fID = productFlowID
            popupView.product.id = productId
            popupView.isMpinSetLater = self.isMpinSetLater
            popupView.modalPresentationStyle = .overCurrentContext
            self.present(popupView, animated: false, completion: nil)
        })
    }
    
//MARK: Alert Popup
    
    func showNotificationPopup(_ headerLabelText: String, msgLabelText: String, actionType: String, isCancelBtnHidden: Bool){
        DispatchQueue.main.async(execute: {
            let popupView = AlertPopup(nibName: "AlertPopup", bundle: nil)
            popupView.headerLabelText = headerLabelText //Constants.Message.ALERT_NOTIFICATION_TITLE
            popupView.msgLabelText = msgLabelText
            popupView.actionType = actionType
            popupView.cancelButtonHidden = isCancelBtnHidden
            popupView.isMpinSetLater = self.isMpinSetLater
            popupView.modalPresentationStyle = .overCurrentContext
            self.present(popupView, animated: false, completion: nil)
        })
    }
    
//MARK: Show AlertView
    
    func showMessage(_ message : String)
    {
        DispatchQueue.main.async(execute: {
            self.alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: message, okButtonPressed: {
                self.alertView.hide()
            })
            self.alertView.show(parentView: self.view)
        })
    }
    
//MARK: PushViewController Configuration
    
    func pushViewController(_ viewController: UIViewController )
    {
        DispatchQueue.main.async(execute: {
            let applicationDelegate : AppDelegate = UIApplication.shared.delegate as! AppDelegate
            let mainViewController :UINavigationController = applicationDelegate.mainNavigationController!
            mainViewController.pushViewController(viewController, animated: true)

        })
    }
    
    func pushViewControllerAndClearQueue(_ viewController: UIViewController )
    {
        DispatchQueue.main.async(execute: {
            let applicationDelegate : AppDelegate = UIApplication.shared.delegate as! AppDelegate
            applicationDelegate.mainNavigationController!.viewControllers.removeAll()
            applicationDelegate.mainNavigationController!.pushViewController(viewController, animated: true)
        })
    }
    
    func clearMainVC() {
        DispatchQueue.main.async(execute: {
            let applicationDelegate : AppDelegate = UIApplication.shared.delegate as! AppDelegate
            for vc:UIViewController in applicationDelegate.mainNavigationController!.viewControllers{
                //print(vc.description)
                if(vc is MainMenuVC) {
                    applicationDelegate.mainNavigationController?.viewControllers.removeAll(where: {$0 == vc})
                }
            }
        })
    }
    
//MARK: PopViewController Configuration
    
    func popViewController()
    {
        DispatchQueue.main.async(execute: {
            let applicationDelegate : AppDelegate = UIApplication.shared.delegate as! AppDelegate
            applicationDelegate.mainNavigationController!.popViewController(animated: true)
        })
    }
    
    
    func popViewControllerAndGotoStart()
    {
        DispatchQueue.main.async(execute: {
            let applicationDelegate : AppDelegate = UIApplication.shared.delegate as! AppDelegate
            for vc:UIViewController in applicationDelegate.mainNavigationController!.viewControllers{
                //print(vc.description)
                if(vc is MainMenuVC) {
                  applicationDelegate.mainNavigationController?.popToViewController(vc, animated: true)
                }
            }
        })
    }
    
    func popViewControllerAndGoToScan() {
        DispatchQueue.main.async(execute: {
            let applicationDelegate : AppDelegate = UIApplication.shared.delegate as! AppDelegate
            for vc:UIViewController in applicationDelegate.mainNavigationController!.viewControllers{
                //print(vc.description)
                if(vc is RetailPaymentMasterPassVC) {
                  applicationDelegate.mainNavigationController?.popToViewController(vc, animated: true)
                }
            }
        })
    }
    
    func PushVCToMainMenu() {
        DispatchQueue.main.async {
            let viewController:MainMenuVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "MainMenuVC") as! MainMenuVC
            viewController.isMpinSetLater = self.isMpinSetLater
            self.pushViewController(viewController)
        }
    }
    
    func showLoadingView()
    {
        DispatchQueue.main.async(execute: {
            let applicationDelegate : AppDelegate = UIApplication.shared.delegate as! AppDelegate
            self.view.addSubview(applicationDelegate.loadingView!)
            self.view.isUserInteractionEnabled = false
            applicationDelegate.loadingView!.show()
                        
        })
    }
    
    func hideLoadingView()
    {
        DispatchQueue.main.async(execute: {
            let applicationDelegate : AppDelegate = UIApplication.shared.delegate as! AppDelegate
            self.view.addSubview(applicationDelegate.loadingView!)
            self.view.isUserInteractionEnabled = true
            applicationDelegate.loadingView!.hide()
        })
    }
    
    func signoutCustomer(){
        let popupView = AlertPopup(nibName: "AlertPopup", bundle: nil)
        popupView.headerLabelText = Constants.Message.ALERT_NOTIFICATION_TITLE
        popupView.msgLabelText = "Are you sure you want to logout?"
        popupView.actionType = "SignOut"
        popupView.isYesNoButtons = true
        popupView.cancelButtonHidden = false
        popupView.isMpinSetLater = self.isMpinSetLater
        popupView.modalPresentationStyle = .overFullScreen
        self.present(popupView, animated: false, completion: nil)
    }
    
    
    func appSessionExpired(_ VC: UIViewController){
        //print(self)
        let popupView = AlertPopup(nibName: "AlertPopup", bundle: nil)
        popupView.headerLabelText = Constants.Message.ALERT_NOTIFICATION_TITLE
        popupView.msgLabelText = Constants.Message.SESSION_TIMEOUT
        popupView.actionType = "SignOut"
        popupView.VC = VC
        popupView.isMpinSetLater = self.isMpinSetLater
        popupView.modalPresentationStyle = .overCurrentContext
        VC.present(popupView, animated: false, completion: nil)
    }

    func goToCustomerLogin(){
        DispatchQueue.main.async(execute: {
            let applicationDelegate : AppDelegate = UIApplication.shared.delegate as! AppDelegate
            for vc: UIViewController in applicationDelegate.mainNavigationController!.viewControllers{
                if(vc is MainViewController){
                    applicationDelegate.mainNavigationController?.popToViewController(vc, animated: false)
                    let loginVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "LoginCustomerVC") as! LoginCustomerVC
                    Utility.pushViewController(loginVC)
                }
            }
        })
    }
    
    func goToCustomerLoginNew(){
        DispatchQueue.main.async(execute: {
            let applicationDelegate : AppDelegate = UIApplication.shared.delegate as! AppDelegate
            for vc:UIViewController in applicationDelegate.mainNavigationController!.viewControllers{
                //print(vc.description)
                if(vc is LoginCustomerVC) {
                    applicationDelegate.mainNavigationController?.popToViewController(vc, animated: true)
                }
                if (vc is SelfRegistrationVC) {
                    applicationDelegate.mainNavigationController?.popToViewController(vc, animated: true)
                }
            }
        })
    }
    
    func goToPayNow(){
        DispatchQueue.main.async(execute: {
            let applicationDelegate : AppDelegate = UIApplication.shared.delegate as! AppDelegate
            for vc:UIViewController in applicationDelegate.mainNavigationController!.viewControllers{
                //print(vc.description)
                if(vc is RetailPaymentMasterPassVC){
                    applicationDelegate.mainNavigationController?.popToViewController(vc, animated: true)
                }
            }
        })
    }
    
    func print(_ items: Any...) {
        #if DEBUG
        Swift.print(items[0])
        #endif
    }
    
}
