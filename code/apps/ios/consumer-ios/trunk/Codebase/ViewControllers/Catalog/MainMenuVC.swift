
//
//  MainMenuVC.swift
//  Timepey
//
//  Created by Adnan Ahmed on 02/06/2016.
//  Copyright © 2016 Inov8. All rights reserved.


import UIKit
import AEXML
import EYImageSlider
import Kingfisher


class MainMenuVC: BaseViewController, UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout, FinancialPinPopupDelegate,  EYImageSliderDelegate,EYImageSliderDataSource, EYImageSliderImageDataSource {
    
    var catelogArray = [Category]()
    var bankArray = [Bank]()
    var pinValue = String()
    
    @IBOutlet weak var sliderView: EYImageSlider!
    
    @IBOutlet weak var myCollectionView: UICollectionView!
    @IBOutlet weak var customerBalance: UILabel!
    @IBOutlet weak var balanceDecimal: UILabel!
    @IBOutlet weak var collectionViewBottom: NSLayoutConstraint!
    @IBOutlet weak var customerNamelabel: UILabel!
    @IBOutlet weak var IBANLabel: UILabel!
    
    
    let userDefault = UserDefaults.standard
    var response = (XMLError(), XMLMessage(), [String:String]())
    var banksList = (XMLError(), XMLMessage(), [Bank]())
    var paymentPurposeList = (XMLError(), XMLMessage(), [TPURPS]())
    var viewAppearedCount: Int?
    
    override func viewDidLoad() {
        
        super.viewDidLoad()
        
        //Slider View
        sliderView.delegate = self
        sliderView.dataSource = self
        sliderView.imageSource = self
        
        sliderView.imageCounterDisabled = true
        sliderView.pageControl.pageIndicatorTintColor = UIColor.white
        sliderView.pageControl.currentPageIndicatorTintColor = UIColor.systemOrange
        
        sliderView.slideShowTimeInterval = 7.0
        sliderView.bounces = false
        
        myCollectionView.register(UINib.init(nibName: "MenuCollectionViewCell", bundle: nil), forCellWithReuseIdentifier: "MenuCollectionViewCell")
        
        Utility.addShadowToView(view: sliderView)
        
        //Custom Catelog Setting
        self.isMpinSetLater = userDefault.bool(forKey: "isMpinSetLater")
         populateMyAccountMenu()
        //includeCustomCatelog()
        if let decodedNSData = userDefault.object(forKey: "catelog") as? Data { 
            catelogArray = (NSKeyedUnarchiver.unarchiveObject(with: decodedNSData) as? [Category])!
        }
        
        fetchBanks()
        fetchPaymentPurpose()
        
    }
    
    
    override func viewDidLayoutSubviews() {
        
        let viewHeight = self.view.frame.size.height
        let collectionViewHeight = self.myCollectionView.collectionViewLayout.collectionViewContentSize.height
        let collectionViewFrame = self.myCollectionView.frame
        
        let bottomBarheight: CGFloat = self.view.frame.size.width * 0.165625
        if(viewHeight >  ((collectionViewHeight + collectionViewFrame.origin.y) + bottomBarheight)){
            myCollectionView.frame.size.height = collectionViewHeight + 1
            collectionViewBottom.constant = ((viewHeight - 1) - (collectionViewHeight + collectionViewFrame.origin.y)) + 8
        } else {
            collectionViewBottom.constant = bottomBarheight + 8
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(true)
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        let bal: String = userDefault.object(forKey: "BAL") as! String
        customerNamelabel.text = (Customer.sharedInstance.fName ?? "") + " " + (Customer.sharedInstance.lName ?? "")
        IBANLabel.text = Customer.sharedInstance.iBAN ?? ""
        if(bal != ""){
            let balance = "\(userDefault.object(forKey: "BAL")!)"
            let balanceArr = balance.split{$0 == "."}.map(String.init)
            self.customerBalance.text = balanceArr[0]
            self.balanceDecimal.text = ".\(balanceArr[1])"
        }else{
            userDefault.set("0.00", forKey: "BAL")
            customerBalance.text = "0"
            balanceDecimal.text = ".00"
        }
        
    }
    
    @IBAction func refreshPressed(_ sender: UIButton) {
            balanceEnquiryPostRequest()
    }
    //Delegate methods of OTP popup
    func okPressedFP() {
        balanceEnquiryPostRequest()
    }
    func canclePressedFP() {
        //print("cancel")
    }
    
    // MARK: Image Slider Delegate
    
    func placeHoderImageForImageSlider(_ slider: EYImageSlider) -> UIImage {
        return UIImage.init(named: "ads_placeholder")!
    }
    
    func contentModeForImage(_ imageIndex: Int, inSlider: EYImageSlider) -> UIView.ContentMode {
        return  .scaleToFill
    }
    
    func arrayWithImages(_ slider: EYImageSlider) -> [URL] {
        
        var images = [URL]()
        if let data = userDefault.value(forKey: "ads") as? NSData {
            if let urlsArray = NSKeyedUnarchiver.unarchiveObject(with: data as Data) as? [NSURL] {
                images = urlsArray as [URL]
            }
        }
        if images.count == 0 {
            
            images = [NSURL (string: "")! as URL]
        }
        return images as [URL]
    }
    
    func sliderLoadImageView(_ imageView: UIImageView, withURL url: URL) {
        imageView.kf.indicatorType = .activity
        imageView.kf.setImage(with: url,
                              placeholder: UIImage.init(named: "ads_placeholder")!,
                              options: nil,
                              progressBlock: nil,
                              completionHandler: nil)
    }
    
    @IBAction func signoutPressed(_ sender: UIButton) {
        self.signoutCustomer()
    }
    
    //MARK: -  UICollectionViewDataSource
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }           
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        var rowCount:Int?
        if(catelogArray.count == 0){
            rowCount = 0
        }else{
            rowCount = catelogArray.count
            //            let myRowCount = rowCount! % 3
            //            if(myRowCount == 1){
            //                rowCount = rowCount! + 2
            //            }else if(myRowCount == 2){
            //                rowCount = rowCount! + 1
            //                rowCount = rowCount!
            //            }
        }
        return rowCount!
    }
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let identifier = "MenuCollectionViewCell"
        let cell: MenuCollectionViewCell = collectionView.dequeueReusableCell(withReuseIdentifier: identifier, for: indexPath) as! MenuCollectionViewCell as MenuCollectionViewCell
        
        if(indexPath.row < catelogArray.count) {
            let image: UIImage?
            if var catIcon = catelogArray[(indexPath as NSIndexPath).row].icon {
                if (catIcon as NSString).range(of: ".png").location != NSNotFound {
                    catIcon = catIcon.replacingOccurrences(of: ".png", with: "")
                }
                image = UIImage(named: catIcon)
            }
            else {
                image = UIImage(named: "")
            }
            cell.imageViewType.image = image
            cell.lblProductName.text = catelogArray[indexPath.row].name
            
        }
        return cell
    }
    //MARK: -  UICollectionViewDelegate
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let isMPIN = userDefault.bool(forKey: "isMpinSetLater") 
        
        if(indexPath.row < catelogArray.count) {
            if(catelogArray[(indexPath as NSIndexPath).row].categories.count > 0 || catelogArray[(indexPath as NSIndexPath).row].products.count > 0){
                
                if(catelogArray[(indexPath as NSIndexPath).row].isProduct == "1" && catelogArray[(indexPath as NSIndexPath).row].products.count > 0) {
                    if isMPIN == false {
                        let productFID = catelogArray[(indexPath as NSIndexPath).row].products[0].fID
                        if(productFID == Constants.FID.CHALLAN_NUMBER || productFID == Constants.FID.COLLECTION_PAYMENT || productFID == Constants.FID.LOAN_PAYMENTS_FID) {
                            let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "TransDataEntryVC") as! TransDataEntryVC
                            viewController.screenTitleText = "\(catelogArray[(indexPath as NSIndexPath).row].name!)"
                            viewController.product = catelogArray[(indexPath as NSIndexPath).row].products[0]
                            viewController.screenSubTitleText = "\((catelogArray[(indexPath as NSIndexPath).row].products[0].name)!)"
                            viewController.product = catelogArray[(indexPath as NSIndexPath).row].products[0]
                            viewController.isMpinSetLater = isMPIN
                            self.pushViewController(viewController)
                        } else if(productFID == Constants.FID.CASH_WITHDRAWAL) {
                            let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "CashWithdrawalVC") as! CashWithdrawalVC
                            viewController.screenTitleText = "\((catelogArray[(indexPath as NSIndexPath).row].products[0].name)!)"
                            viewController.product = catelogArray[(indexPath as NSIndexPath).row].products[0]
                            viewController.isMpinSetLater = isMPIN
                            self.pushViewController(viewController)
                        } else if(productFID == Constants.FID.RETAILPAYMENT_FID) {
                            let viewController = UIStoryboard(name: "FundTransfer", bundle: nil).instantiateViewController(withIdentifier: "FundTransferVC") as! FundTransferVC
                            viewController.screenTitleText = "\((catelogArray[(indexPath as NSIndexPath).row].products[0].name)!)"
                            viewController.product = catelogArray[(indexPath as NSIndexPath).row].products[0]
                            viewController.isMpinSetLater = isMPIN
                            self.pushViewController(viewController)
                        } else if(productFID == Constants.FID.CASH_WITHDRAWAL_LEG1_FID) {
                            
                            let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "CashWithdrawalLeg1InfoVC") as! CashWithdrawalLeg1InfoVC
                            viewController.product = catelogArray[(indexPath as NSIndexPath).row].products[0]
                            viewController.isMpinSetLater = isMPIN
                            self.pushViewController(viewController)
                        }
                        else if(productFID == Constants.FID.HRA_ACCCOUNT_OPENING) {
                            let viewController = UIStoryboard(name: "MyAccount", bundle: nil).instantiateViewController(withIdentifier: "HRAVC") as! HRARegistrationVC
                            viewController.screenTitleText = "\((catelogArray[(indexPath as NSIndexPath).row].products[0].name)!)"
                            viewController.product = catelogArray[(indexPath as NSIndexPath).row].products[0]
                            viewController.isMpinSetLater = isMPIN
                            self.pushViewController(viewController)
                        }
                        else if productFID == Constants.FID.DEBIT_CARD_ISSUANCE_INFO_UAT || productFID == Constants.FID.DEBIT_CARD_ISSUANCE_INFO_QA {
                            debitCardIssuancePostRequest()
                        }
                        else if productFID == Constants.FID.BOOKME_BUSES {
                            let viewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "CustomWebView") as! CustomWebView
                            viewController.screenTitleText = "\(catelogArray[(indexPath as NSIndexPath).row].name!)"
                            viewController.product = catelogArray[(indexPath as NSIndexPath).row].products[0]
                            viewController.imgString = catelogArray[indexPath.row].icon
                            viewController.urlString = Constants.BOOKME.BUSES_URL
                            viewController.isMpinSetLater = isMPIN
                            self.pushViewController(viewController)
                        }
                        
                    } else {
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
                else{
                    
                    let viewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "ProductListingVC") as! ProductListingVC
                    
                    if(catelogArray[(indexPath as NSIndexPath).row].products.count  == 0){
                        viewController.categoryArray = catelogArray[(indexPath as NSIndexPath).row].categories
                    }else if(catelogArray[(indexPath as NSIndexPath).row].categories.count != 0 && catelogArray[(indexPath as NSIndexPath).row].products.count != 0){
                        viewController.categoryArray = catelogArray[(indexPath as NSIndexPath).row].categories
                        viewController.productArray = catelogArray[(indexPath as NSIndexPath).row].products
                    }else{
                        //print(catelogArray[(indexPath as NSIndexPath).row])
                        viewController.productArray = catelogArray[(indexPath as NSIndexPath).row].products
                    }
                    viewController.mainCatID = catelogArray[(indexPath as NSIndexPath).row].cID
                    viewController.catLable = catelogArray[(indexPath as NSIndexPath).row].name
                    viewController.isMpinSetLater = isMPIN
                    self.pushViewController(viewController)
                }
            }
            
        }
        myCollectionView.deselectItem(at: indexPath, animated: true)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let numberOfColumns: CGFloat = 4
        let itemWidth : Int = Int((collectionView.frame.width - 2) / numberOfColumns as CGFloat)
        return CGSize(width: itemWidth , height: Int(Float(itemWidth) * 1.3) )
    }
    
    func balanceEnquiryPostRequest(){
        self.showLoadingView()
        
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                    xmlPath = Bundle.main.path(forResource: "Command-6-Balance Inquiry", ofType: "xml"),
                  let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
            
            else { return }
            
            //let newStr = String(data: data, encoding: NSUTF8StringEncoding)
            //print(newStr)
            response = XMLParser.balanceEnquiryXMLParsing(data)
            
            if(self.response.0.msg != nil){
                if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                }else{
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                }
            }else if(self.response.1.msg != nil){
                
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                
            }else{
                
                if(self.response.2["BALF"] != nil){
                    let balance = "\(self.response.2["BALF"]!)"
                    let balanceArr = balance.split{$0 == "."}.map(String.init)
                    self.customerBalance.text = balanceArr[0]
                    self.balanceDecimal.text = ".\(balanceArr[1])"
                }else{
                    self.customerBalance.text = "0"
                    self.balanceDecimal.text = ".00"
                }
            }
            
            self.hideLoadingView()
        }else{
            
            
            //let encryptedPin = try! inputText.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let myAccApi : MyAccountWebAPI = MyAccountWebAPI()
            
            myAccApi.balanceInqueryPostRequest(
                Constants.CommandId.BAL_INQUIRY,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                pin:"" ,
                ENCT: Constants.AppConfig.ENCT_KEY,
                ACCTYPE: "1",
                APID: "\(userDefault.object(forKey: "useracc")!)",
                BBACID: (Customer.sharedInstance.bank?.accounts[0].id)!,
                onSuccess:{(data) -> () in
                    //print(data)
                    self.response = XMLParser.balanceEnquiryXMLParsing(data)
                    //print(self.response.2[0]["BAL"])
                    
                    if(self.response.0.msg != nil){
                        if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                        }else{
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                        }
                    }else if(self.response.1.msg != nil){
                        
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                        
                    }else{
                        DispatchQueue.main.async(){
                            if(self.response.2["BALF"] != nil || self.response.2["BALF"] != ""){
                                
                                let balance = "\(self.response.2["BALF"]!)"
                                
                                self.userDefault.set(balance, forKey: "BAL")
                                
                                let balanceArr = balance.split{$0 == "."}.map(String.init)
                                self.customerBalance.text = balanceArr[0]
                                self.balanceDecimal.text = ".\(balanceArr[1])"
                            }else{
                                self.customerBalance.text = "0"
                                self.balanceDecimal.text = "00"
                            }
                        }
                    }
                    DispatchQueue.main.async(){
                        self.hideLoadingView()
                    }
                    
                },
                onFailure: {(reason) ->() in
                    //print("Failure")
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                    self.hideLoadingView()
                    
                })
            
        }
        
    }
    
    
    func debitCardIssuancePostRequest(){
        self.showLoadingView()
        
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                    xmlPath = Bundle.main.path(forResource: "Command-6-Balance Inquiry", ofType: "xml"),
                  let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
            
            else { return }
            
            response = XMLParser.paramTypeXMLParsing(data)
            
            if(self.response.0.msg != nil){
                if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                }else{
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                }
            }else if(self.response.1.msg != nil){
                
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                
            } else {
                
                
            }
            
            self.hideLoadingView()
        }else{
            
            
            //let encryptedPin = try! inputText.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let myAccApi : MyAccountWebAPI = MyAccountWebAPI()
            
            myAccApi.debitCardIssuanceInfo(
                Constants.CommandId.DEBIT_CARD_ISSUANCE_INFO,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                APPID: "2",
                
                onSuccess:{(data) -> () in
                    
                    self.response = XMLParser.paramTypeXMLParsing(data)
                    
                    
                    if(self.response.0.msg != nil){
                        if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED) {
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                        } else {
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                        }
                    }else if(self.response.1.msg != nil){
                        
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                        
                    } else {
                        
                        if self.response.2.count == 2 {
                            DispatchQueue.main.async {
                                let debitCardTermsAndConditionsVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "DebitCardTermsAndConditionsVC") as! DebitCardTermsAndConditionsVC
                                debitCardTermsAndConditionsVC.CNIC = self.response.2["CNIC"]!
                                debitCardTermsAndConditionsVC.cMob = self.response.2["MOBN"]!
                                self.pushViewController(debitCardTermsAndConditionsVC)
                            }
                        }
                        else {
                            DispatchQueue.main.async {
                                let debitCardTermsAndConditionsVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "DebitCardTermsAndConditionsVC") as! DebitCardTermsAndConditionsVC
                                debitCardTermsAndConditionsVC.fee = self.response.2["FEE"]!
                                self.pushViewController(debitCardTermsAndConditionsVC)
                            }
                        }
                        
                    }
                    DispatchQueue.main.async(){
                        self.hideLoadingView()
                    }
                    
                },
                onFailure: {(reason) ->() in
                    //print("Failure")
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                    self.hideLoadingView()
                    
                })
            
        }
        
    }
    
    
    func fetchBanks() {
        self.showLoadingView()
        
        if(Constants.AppConfig.IS_MOCK == 1) {
            guard let
                    xmlPath = Bundle.main.path(forResource: "Command-33-Login", ofType: "xml"),
                  let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
            else { return }
            
            self.banksList = XMLParser.mBanksXMLParsing(data)
            //print(response)
            
            if(self.response.0.msg != nil) {
                
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signInFailure", isCancelBtnHidden: true)
            }
            else if(self.response.1.msg != nil){
                
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "signInFailure", isCancelBtnHidden: true)
                
            }
            
            self.hideLoadingView()
        }
        else{
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let transcationApi : TransactionAPI = TransactionAPI()
            transcationApi.FetchBanksList(Constants.CommandId.MBANKS_LIST, reqTime: currentTime, DTID: "5", onSuccess: { data in
                self.banksList = XMLParser.mBanksXMLParsing(data)
                if self.banksList.0.msg != nil {
                    //Handle Server Error
                    
                }
                else if self.banksList.1.msg != nil {
                    ////Handle Server Messages
                    DispatchQueue.main.async(execute: {
                        self.alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: self.banksList.1.msg!, okButtonPressed: {
                            self.alertView.hide()
                        })
                        self.alertView.show(parentView: self.view)
                    })
                }
                else {
                    Customer.sharedInstance.mbanks = self.banksList.2
                }
            }, onFailure:{ reason in
                DispatchQueue.main.async {
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                }
                self.hideLoadingView()
                
            })
        }
        
        
    }
    
    
    func fetchPaymentPurpose() {
        self.showLoadingView()
        
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                    xmlPath = Bundle.main.path(forResource: "Command-33-Login", ofType: "xml"),
                  let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
            else { return }
            
            //print(response)
            self.paymentPurposeList = XMLParser.PaymentPurposeParsing(data)
            if(self.paymentPurposeList.0.msg != nil){
                //Handle Server Error
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.paymentPurposeList.0.msg!, actionType: "signInFailure", isCancelBtnHidden: true)
            }
            else if(self.paymentPurposeList.1.msg != nil){
                
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.paymentPurposeList.1.msg!, actionType: "signInFailure", isCancelBtnHidden: true)
                
            }
            
            self.hideLoadingView()
        }
        else {
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            let transcationApi : TransactionAPI = TransactionAPI()
            transcationApi.FetchPaymentPurposeList(Constants.CommandId.PAYMENT_PURPOSE, reqTime: currentTime, DTID: "5", onSuccess: { data in
                
                self.paymentPurposeList = XMLParser.PaymentPurposeParsing(data)
                if self.paymentPurposeList.0.msg != nil {
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.paymentPurposeList.0.msg!, actionType: "signInFailure", isCancelBtnHidden: true)
                }
                else if self.paymentPurposeList.1.msg != nil {
                    ////Handle Server Messages
                    DispatchQueue.main.async(execute: {
                        self.alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: self.paymentPurposeList.1.msg!, okButtonPressed: {
                            self.alertView.hide()
                        })
                        self.alertView.show(parentView: self.view)
                    })
                }
                else {
                    Customer.sharedInstance.tpurps = self.paymentPurposeList.2
                }
                self.hideLoadingView()
                
            }, onFailure: { reason in
                DispatchQueue.main.async {
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                }
                self.hideLoadingView()
                
            })
        }
        
        
    }
    
    func includeCustomCatelog(){
        if let decodedNSData = userDefault.object(forKey: "catelog") as? Data{
            catelogArray = (NSKeyedUnarchiver.unarchiveObject(with: decodedNSData) as? [Category])!
            
            //print(catelogArray)
            
            var isAccountAvailable: Bool = false
            
            for i in 0 ..< catelogArray.count {
                if(catelogArray[i].cID == Constants.CID.MY_ACCOUNT){
                    isAccountAvailable = true
                    for j in 0 ..< self.customeCatelog[0].categories.count{
                        catelogArray[i].categories.append(customeCatelog[0].categories[j])
                    }
                    break
                }
            }
            
            if(isAccountAvailable == false){
                //Append My Account
                catelogArray.append(customeCatelog[0])
                //Appned Cash Withdrawal
                //catelogArray.append(customeCatelog[1])
            }else{
                //Append Cash Withdrawal
                //catelogArray.append(customeCatelog[1])
                //Append Debit Card
                //catelogArray.append(customeCatelog[2])
            }
            
        }
        else {
            //print("Failed")
            catelogArray.append(customeCatelog[0])
            //Append Cash Withdrawal
            //catelogArray.append(customeCatelog[1])
            return
        }
        
        //        //print("Failed")
        //        catelogArray.append(customeCatelog[0])
        //        //Append Cash Withdrawal
        //        catelogArray.append(customeCatelog[1])
    }
    func okPressedChallanNo(EncMpin: String) {
        //
    }
    
    
}



