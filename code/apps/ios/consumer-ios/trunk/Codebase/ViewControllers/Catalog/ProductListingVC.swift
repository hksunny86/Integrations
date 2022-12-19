//
//  ProductListingVC.swift
//  Timepey
//
//  Created by Adnan Ahmed on 09/06/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import UIKit


class ProductListingVC: BaseViewController, UITableViewDelegate, UITableViewDataSource, FinancialPinPopupDelegate {
    
    
    
    var productArray = [Product]()
    var categoryArray = [Category]()
    var catLable:String?
    var mainCatID: String?
    var webUrl: String?
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var catImage: UIImageView!
    @IBOutlet weak var catTitle: UILabel!
    @IBOutlet weak var topConstraintCategorylabelView: NSLayoutConstraint!
    @IBOutlet weak var lblCustomerName: UILabel!
    
    @IBOutlet weak var topConstraintTableView: NSLayoutConstraint!
    override func viewDidLoad() {
        super.viewDidLoad()
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: false, isSignoutButtonHidden: false)
        lblCustomerName.isHidden = true
        self.tableView.register(UINib(nibName: "productListingCell", bundle: nil), forCellReuseIdentifier: "listingCell")
        switch UIDevice.current.userInterfaceIdiom{
        case .pad:
            tableView.rowHeight = 90
        case .phone:
            tableView.rowHeight = 60
        default:
            break
        }
        
        if(mainCatID == "7"){
            self.isMyAccount = false
        }else if(mainCatID == "420"){
            self.isMore = false
        }
        
    }
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        if Constants.DeviceType.IS_IPHONE_X {
            topConstraintCategorylabelView.constant = topConstraintCategorylabelView.constant 
        }
        if(mainCatID == Constants.CID.MY_ACCOUNT) {
            topConstraintTableView.constant = 30
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        if(catLable != nil){
            catTitle.text = catLable
        }
        
        
        if(mainCatID == Constants.CID.PAY_BILL) {
            catImage.image = UIImage(named: "heading_icon_pay_bill.png")
        }else if(mainCatID == Constants.CID.MONEY_TRANSFER) {
            catImage.image = UIImage(named: "heading_icon_money_transfer")
        }else if(mainCatID == Constants.CID.PAYMENTS) {
            catImage.image = UIImage(named: "heading_icon_payment.png")
        }else if(mainCatID == Constants.CID.ZONG_SERVICES) {
            catImage.image = UIImage(named: "heading_icon_zong_services.png")
        }else if(mainCatID == Constants.CID.MY_ACCOUNT) {
            catImage.image = UIImage(named: "heading_icon_my_account.png")
            lblCustomerName.isHidden = false
            lblCustomerName.text = Customer.sharedInstance.fName! + " " + Customer.sharedInstance.lName!
            
        }else if mainCatID == Constants.CID.MOBILELOAD {
        }
        
        tableView.tableFooterView = UIView()
        
    }
    
    
    // MARK: Financial Pin Popup implementation
    
    func okPressedFP() {
        //print("Ok Pressed")
    }
    
    func canclePressedFP() {
        //print("Cancle Pressed")
    }
    
    // MARK: TableViewDelegate implementation
    
    func numberOfSections(in tableView: UITableView) -> Int {
        if(categoryArray.count > 0 && productArray.count == 0){
            return categoryArray.count
        }else if(categoryArray.count > 0 && productArray.count > 0){
            return (categoryArray.count + productArray.count)
        }
        else{
            return productArray.count
        }
    }
    
    func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        return 8.0
    }
    
    func tableView(_ tableView: UITableView, viewForFooterInSection section: Int) -> UIView? {
        let headerView = UIView()
        headerView.backgroundColor = UIColor.clear
        return headerView
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell:productListingCell = tableView.dequeueReusableCell(withIdentifier: "listingCell", for: indexPath) as! productListingCell
        
        
        cell.layer.cornerRadius = 2
        cell.layer.shadowColor = UIColor.lightGray.cgColor
        cell.layer.shadowOffset = CGSize(width: 0.5, height: 0.5)
        cell.layer.borderWidth = 0.2
        cell.layer.shadowRadius = 2
        cell.layer.shadowOpacity = 0.4
        cell.prodImage.contentMode = .scaleAspectFit
        
        if(categoryArray.count > 0 && productArray.count == 0) {
            cell.titleLabel.text = "\(categoryArray[(indexPath as NSIndexPath).section].name!)"
            if categoryArray[indexPath.section].url != nil && categoryArray[indexPath.section].url != "null" {
                
                DispatchQueue.main.async {
                    self.webUrl = "\(Constants.ServerConfig.BASE_URL)/images/utilities/\(self.categoryArray[indexPath.section].url!)"
                    let url = URL(string: self.webUrl!)
                    cell.prodImage.kf.setImage(with: url, placeholder: UIImage(named: "ads_placeholder"), options: nil, progressBlock: nil, completionHandler: nil)
                    //                    cell.prodImage.kf.setImage(with:url)
                    cell.setNeedsLayout()
                }
            } else if categoryArray[indexPath.section].icon != nil && categoryArray[indexPath.section].icon != "null" {
                
                DispatchQueue.main.async {
                    self.webUrl = "\(Constants.ServerConfig.BASE_URL)/images/utilities/\(self.categoryArray[indexPath.section].icon!)"
                    let url2 = URL(string: self.webUrl!)
                    cell.prodImage.kf.setImage(with: url2, placeholder: UIImage(named: "ads_placeholder"), options: nil, progressBlock: nil, completionHandler: nil)
                    //                    cell.prodImage.kf.setImage(with:url2)
                    cell.setNeedsLayout()
                }
            }
            else {
                cell.leadingConstraintTitlelabel.constant =  -cell.prodImage.bounds.width + 20
                cell.prodImage.isHidden = true
                
            }
        }else if(categoryArray.count > 0 && productArray.count > 0) {
            
            if(categoryArray.count > indexPath.section){
                cell.titleLabel.text = "\(categoryArray[(indexPath as NSIndexPath).section].name!)"
                if categoryArray[indexPath.section].url != nil && categoryArray[indexPath.section].url != "null" {
                    DispatchQueue.main.async {
                        self.webUrl = "\(Constants.ServerConfig.BASE_URL)/images/utilities/\(self.categoryArray[indexPath.section].url!)"
                        let url3 = URL(string: self.webUrl!)
                        cell.prodImage.kf.setImage(with: url3, placeholder: UIImage(named: "ads_placeholder"), options: nil, progressBlock: nil, completionHandler: nil)
                        //                        cell.prodImage.kf.setImage(with:url3)
                        cell.setNeedsLayout()
                    }
                }
                else if categoryArray[indexPath.section].icon != nil && categoryArray[indexPath.section].icon != "null" {
                    DispatchQueue.main.async {
                        self.webUrl = "\(Constants.ServerConfig.BASE_URL)/images/utilities/\(self.categoryArray[indexPath.section].icon!)"
                        let url4 = URL(string: self.webUrl!)
                        cell.prodImage.kf.setImage(with: url4, placeholder: UIImage(named: "ads_placeholder"), options: nil, progressBlock: nil, completionHandler: nil)
                        //                        cell.prodImage.kf.setImage(with:url4)
                        cell.setNeedsLayout()
                    }
                }
                else {
                    cell.leadingConstraintTitlelabel.constant =  -cell.prodImage.bounds.width + 20
                    cell.prodImage.isHidden = true
                    
                }
            }
            else{
                
                let ip =  indexPath.section - categoryArray.count
                cell.titleLabel.text = "\(productArray[ip].name!)"
                if productArray[ip].url != nil && productArray[ip].url != "null" {
                    
                    DispatchQueue.main.async {
                        self.webUrl = "\(Constants.ServerConfig.BASE_URL)/images/utilities/\(self.productArray[ip].url!)"
                        let url5 = URL(string: self.webUrl!)
                        cell.prodImage.kf.setImage(with: url5, placeholder: UIImage(named: "ads_placeholder"), options: nil, progressBlock: nil, completionHandler: nil)
                        //                        cell.prodImage.kf.setImage(with:url5)
                        cell.setNeedsLayout()
                    }
                }
                else {
                    cell.leadingConstraintTitlelabel.constant =  -cell.prodImage.bounds.width + 20
                    cell.prodImage.isHidden = true
                }
            }
        }
        
        else {
            cell.titleLabel.text = "\(productArray[(indexPath as NSIndexPath).section].name!)"
            if productArray[indexPath.section].url != nil && productArray[indexPath.section].url != "null" {
                
                DispatchQueue.main.async {
                    self.webUrl = "\(Constants.ServerConfig.BASE_URL)/images/utilities/\(self.productArray[indexPath.section].url!)"
                    let url6 = URL(string: self.webUrl!)
                    cell.prodImage.kf.setImage(with: url6, placeholder: UIImage(named: "ads_placeholder"), options: nil, progressBlock: nil, completionHandler: nil)
                    //                    cell.prodImage.kf.setImage(with:url6)
                    cell.setNeedsLayout()
                }
            }
            else {
                cell.leadingConstraintTitlelabel.constant =  -cell.prodImage.bounds.width + 20
                cell.prodImage.isHidden = true
                
            }
        }
        cell.titleLabel.sizeToFit()
        return cell
    }
    
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        if(categoryArray.count > 0 && productArray.count == 0) {
            
            if(categoryArray[(indexPath as NSIndexPath).section].isProduct == "0") {
                
                let viewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "ProductListingVC") as! ProductListingVC
                if(categoryArray.count > 0) {
                    if(categoryArray[(indexPath as NSIndexPath).section].products.count  == 0){
                        viewController.categoryArray = categoryArray[(indexPath as NSIndexPath).section].categories
                    } else if categoryArray[(indexPath as NSIndexPath).section].cID == "277" {
                        viewController.categoryArray = categoryArray[(indexPath as NSIndexPath).section].categories
                        viewController.productArray = categoryArray[(indexPath as NSIndexPath).section].products
                    } else{
                        viewController.productArray = categoryArray[(indexPath as NSIndexPath).section].products
                    }
                    viewController.isMpinSetLater = self.isMpinSetLater
                    viewController.catLable = categoryArray[(indexPath as NSIndexPath).section].name
                }
                self.pushViewController(viewController)
                
            }
            else{
                if(categoryArray[(indexPath as NSIndexPath).section].products.count > 0) {
                    
                    if(categoryArray[(indexPath as NSIndexPath).section].products[0].fID == Constants.FID.RETAILPAYMENT_FID){
                        let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "RetailPaymentQRScanVC") as! RetailPaymentQRScanVC
                        viewController.product = categoryArray[(indexPath as NSIndexPath).section].products[0]
                        self.pushViewController(viewController)
                    }
                    
                    else if(categoryArray[(indexPath as NSIndexPath).section].products[0].fID == Constants.FID.HRA_ACCCOUNT_OPENING) {
                        let viewController = UIStoryboard(name: "MyAccount", bundle: nil).instantiateViewController(withIdentifier: "HRAVC") as! HRARegistrationVC
                        viewController.screenTitleText = categoryArray[(indexPath as NSIndexPath).section].products[0].name
                        viewController.product = categoryArray[(indexPath as NSIndexPath).section].products[0]
                        self.pushViewController(viewController)
                    }
                    else if(categoryArray[(indexPath as NSIndexPath).section].products[0].fID == Constants.FID.MINISTATEMENT_FID) {
                        let viewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "ProductListingVC") as! ProductListingVC
                        if(categoryArray.count > 0){
                            if(categoryArray[(indexPath as NSIndexPath).section].products.count  == 0){
                                viewController.categoryArray = categoryArray[(indexPath as NSIndexPath).section].categories
                            }else{
                                viewController.productArray = categoryArray[(indexPath as NSIndexPath).section].products
                            }
                            viewController.catLable = categoryArray[(indexPath as NSIndexPath).section].name
                        }
                        viewController.isMpinSetLater = self.isMpinSetLater
                        self.pushViewController(viewController)
                    }
                    else if(categoryArray[(indexPath as NSIndexPath).section].products[0].fID == Constants.FID.TRANSFER_IN_FID || categoryArray[(indexPath as NSIndexPath).section].products[0].fID == Constants.FID.TRANSFER_OUT_FID){
                        
                        let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "CashWithdrawalVC") as! CashWithdrawalVC
                        viewController.screenTitleText = categoryArray[(indexPath as NSIndexPath).section].products[0].name
                        viewController.product = categoryArray[(indexPath as NSIndexPath).section].products[0]
                        viewController.isMpinSetLater = self.isMpinSetLater
                        self.pushViewController(viewController)
                        
                    }
                    else if(categoryArray[(indexPath as NSIndexPath).section].products[0].fID == Constants.FID.MY_LIMITS_FID){
                        let viewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "MyLimitsVC") as! MyLimitsVC
                        self.pushViewController(viewController)
                    }
                    else if(categoryArray[(indexPath as NSIndexPath).section].products[0].fID == Constants.FID.CHECKIBAN){
                        let viewController = UIStoryboard(name: "MyAccount", bundle: nil).instantiateViewController(withIdentifier: "CheckIBANVC") as! CheckIBANVC
                        self.pushViewController(viewController)
                    }
                    else if(categoryArray[(indexPath as NSIndexPath).section].products[0].fID == Constants.FID.BALANCE_INQUERY_FID) {
                        if isMpinSetLater == false {
                            let viewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "ProductListingVC") as! ProductListingVC
                            if(categoryArray.count > 0) {
                                
                                if(categoryArray[(indexPath as NSIndexPath).section].products.count  == 0) {
                                    viewController.categoryArray = categoryArray[(indexPath as NSIndexPath).section].categories
                                }else{
                                    viewController.productArray = categoryArray[(indexPath as NSIndexPath).section].products
                                }
                                viewController.catLable = categoryArray[(indexPath as NSIndexPath).section].name
                            }
                            viewController.isMpinSetLater = self.isMpinSetLater

                            self.pushViewController(viewController)
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
                    else if(categoryArray[indexPath.section].products[0].fID == Constants.FID.CONTACT_US) {
                        let viewController:UIViewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "ContactUSVC") as UIViewController
                        self.pushViewController(viewController)
                        
                    }
                    else if (categoryArray[indexPath.section].products[0].fID == Constants.FID.TERMSANDCONDITIONS) {
                        
                        let viewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "CustomWebView") as! CustomWebView
                        viewController.screenTitleText = "Terms And Conditions"
                        viewController.urlString = "\(Constants.ServerConfig.TERMS_AND_CONDITION_URL)"
                        viewController.imgString = "heading_icon_terms"
                        self.pushViewController(viewController)
                        
                    }
                    else if(categoryArray[indexPath.section].products[0].fID == Constants.FID.FAQ) {
                        
                        if(Reachability.isConnectedToNetwork() == true) {
                            let viewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "FAQsVC") as! FAQsVC
                            self.pushViewController(viewController)
                        } else {
                            self.showNoInternetPopup()
                        }
                        
                    }
                    else if(categoryArray[indexPath.section].products[0].fID == Constants.FID.CHALLAN_NUMBER) {
                        
                        if(Reachability.isConnectedToNetwork() == true) {
                            if isMpinSetLater == false {
                                let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "TransDataEntryVC") as! TransDataEntryVC
                                if(catLable != nil){
                                    viewController.screenTitleText = categoryArray[(indexPath as NSIndexPath).section].products[0].name
                                }
                                
                                viewController.screenSubTitleText = "\((categoryArray[(indexPath as NSIndexPath).section].products[0].name)!)"
                                
                                viewController.product = categoryArray[indexPath.section].products[0]
                                viewController.isMpinSetLater = self.isMpinSetLater
                                self.pushViewController(viewController)
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
                        }else{
                            self.showNoInternetPopup()
                        }
                    }
                }
            }
        }
        else if(categoryArray.count > 0 && productArray.count > 0) {
            
            if(categoryArray.count > indexPath.section) {
                
                if(categoryArray[(indexPath as NSIndexPath).section].isProduct == "0") {
                    
                    let viewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "ProductListingVC") as! ProductListingVC
                    if(categoryArray.count > 0){
                        if(categoryArray[(indexPath as NSIndexPath).section].products.count  == 0){
                            viewController.categoryArray = categoryArray[(indexPath as NSIndexPath).section].categories
                        }else{
                            viewController.productArray = categoryArray[(indexPath as NSIndexPath).section].products
                        }
                        viewController.catLable = categoryArray[(indexPath as NSIndexPath).section].name
                    }
                    viewController.isMpinSetLater = self.isMpinSetLater
                    self.pushViewController(viewController)
                    
                }
                else {
                    if(categoryArray[(indexPath as NSIndexPath).section].products.count > 0) {
                        
                        if(categoryArray[(indexPath as NSIndexPath).section].products[0].fID == Constants.FID.RETAILPAYMENT_FID){
                            let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "RetailPaymentQRScanVC") as! RetailPaymentQRScanVC
                            viewController.product = categoryArray[(indexPath as NSIndexPath).section].products[0]
                            self.pushViewController(viewController)
                        }
                        else if(categoryArray[(indexPath as NSIndexPath).section].products[0].fID == Constants.FID.CHANGELOGINPIN){
                            
                            let viewController = UIStoryboard(name: "MyAccount", bundle: nil).instantiateViewController(withIdentifier: "ChangePinVC") as! ChangePin
                            viewController.screenTitle = "\((categoryArray[(indexPath as NSIndexPath).section].products[0].name)!)"
                            viewController.productId = categoryArray[(indexPath as NSIndexPath).section].products[0].id
                            self.pushViewController(viewController)
                            
                        }
                        else if(categoryArray[(indexPath as NSIndexPath).section].products[0].fID == Constants.FID.MINISTATEMENT_FID) {
                            
                            showFinancialPinPopup(categoryArray[(indexPath as NSIndexPath).section].products[0].name!, requiredAction: "", delegate: self, productFlowID: categoryArray[(indexPath as NSIndexPath).section].products[0].fID!, productId: "")
                            
                        }
                        else if(categoryArray[indexPath.section].products[0].fID == Constants.FID.CHALLAN_NUMBER) {
                            if(Reachability.isConnectedToNetwork() == true){
                                let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "TransDataEntryVC") as! TransDataEntryVC
                                if(catLable != nil){
                                    viewController.screenTitleText = categoryArray[(indexPath as NSIndexPath).section].products[0].name
                                }
                                
                                viewController.screenSubTitleText = "\((categoryArray[(indexPath as NSIndexPath).section].products[0].name)!)"
                                
                                viewController.product = categoryArray[indexPath.section].products[0]
                                viewController.isMpinSetLater = self.isMpinSetLater
                                self.pushViewController(viewController)
                            }else{
                                self.showNoInternetPopup()
                            }
                        }
                        else if(categoryArray[(indexPath as NSIndexPath).section].products[0].fID == Constants.FID.TRANSFER_IN_FID || categoryArray[(indexPath as NSIndexPath).section].products[0].fID == Constants.FID.TRANSFER_OUT_FID){
                            
                            let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "CashWithdrawalVC") as! CashWithdrawalVC
                            viewController.screenTitleText = categoryArray[(indexPath as NSIndexPath).section].products[0].name
                            viewController.product = categoryArray[(indexPath as NSIndexPath).section].products[0]
                            self.pushViewController(viewController)
                            
                        }
                        else if(categoryArray[(indexPath as NSIndexPath).section].products[0].fID == Constants.FID.MY_LIMITS_FID){
                            let viewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "MyLimitsVC") as! MyLimitsVC
                            viewController.isMpinSetLater = self.isMpinSetLater
                            self.pushViewController(viewController)
                        }
                        else if(categoryArray[(indexPath as NSIndexPath).section].products[0].fID == Constants.FID.BALANCE_INQUERY_FID) {
                            
                            if isMpinSetLater == false {
                                let viewController = UIStoryboard(name: "MyAccount", bundle: nil).instantiateViewController(withIdentifier: "BalanceInquiry") as! BalanceInquiry
                                viewController.productID = categoryArray[(indexPath as NSIndexPath).section].products[0].id!
                                viewController.isMpinSetLater = self.isMpinSetLater
                                self.pushViewController(viewController)
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
                        else if(categoryArray[(indexPath as NSIndexPath).section].fID == Constants.FID.ACCTOCASH_FID || categoryArray[(indexPath as NSIndexPath).section].fID == Constants.FID.BBTOCORE_FID) {
                            if isMpinSetLater == false {
                                let viewController = UIStoryboard(name: "FundTransfer", bundle: nil).instantiateViewController(withIdentifier: "FundTransferVC") as! FundTransferVC
                                viewController.screenTitleText = "\((categoryArray[(indexPath as NSIndexPath).section].name)!)"
                                viewController.product = productArray[(indexPath as NSIndexPath).section]
                                viewController.isMpinSetLater = self.isMpinSetLater
                                self.pushViewController(viewController)
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
                    }
                }
                
            }else{
                let ip =  indexPath.section - categoryArray.count
                //titleLabel!.text = "\(productArray[ip].name!)"
                if(productArray[ip].fID == Constants.FID.ACCTOCASH_FID || productArray[ip].fID == Constants.FID.BBTOCORE_FID) {
                    if isMpinSetLater == false {
                        let viewController = UIStoryboard(name: "FundTransfer", bundle: nil).instantiateViewController(withIdentifier: "FundTransferVC") as! FundTransferVC
                        viewController.screenTitleText = "\((productArray[ip].name)!)"
                        viewController.product = productArray[ip]
                        viewController.isMpinSetLater = self.isMpinSetLater
                        self.pushViewController(viewController)
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
                else if(productArray[ip].fID == Constants.FID.ACCTOACC_FID) {
                    if isMpinSetLater == false {
                        let viewController = UIStoryboard(name: "FundTransfer", bundle: nil).instantiateViewController(withIdentifier: "FundTransferVC") as! FundTransferVC
                        viewController.screenTitleText = "\((productArray[ip].name)!)"
                        viewController.product = productArray[ip]
                        viewController.isMpinSetLater = self.isMpinSetLater
                        self.pushViewController(viewController)
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
                }else if(productArray[ip].fID == Constants.FID.TRANSFER_IN_FID || productArray[ip].fID == Constants.FID.TRANSFER_OUT_FID) {
                    if isMpinSetLater == false {
                        let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "CashWithdrawalVC") as! CashWithdrawalVC
                        viewController.screenTitleText = productArray[ip].name
                        viewController.product = productArray[ip]
                        self.pushViewController(viewController)
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
                else if (productArray[ip].fID == Constants.FID.BILL_PAYMENT) {
                    if isMpinSetLater == false {
                        let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "TransDataEntryVC") as! TransDataEntryVC
                        viewController.screenTitleText = productArray[ip].name
                        viewController.screenSubTitleText = productArray[ip].name
                        viewController.product = productArray[ip]
                        viewController.isMpinSetLater = self.isMpinSetLater
                        self.pushViewController(viewController)
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
            }
        }
        else{
            
            if(productArray[(indexPath as NSIndexPath).section].fID == Constants.FID.BALANCE_INQUERY_FID) {
                if isMpinSetLater == false {
                    let viewController = UIStoryboard(name: "MyAccount", bundle: nil).instantiateViewController(withIdentifier: "BalanceInquiry") as! BalanceInquiry
                    viewController.productID = productArray[(indexPath as NSIndexPath).section].id!
                    viewController.isMpinSetLater = self.isMpinSetLater
                    self.pushViewController(viewController)
                    
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
            else if(productArray[(indexPath as NSIndexPath).section].fID == Constants.FID.CHANGEMPIN) {
                if isMpinSetLater == false {
                    let viewController = UIStoryboard(name: "MyAccount", bundle: nil).instantiateViewController(withIdentifier: "ChangePinVC") as! ChangePin
                    viewController.screenTitle = "\((productArray[(indexPath as NSIndexPath).section].name)!)"
                    viewController.productId = productArray[(indexPath as NSIndexPath).section].id
                    self.pushViewController(viewController)
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
            else if (productArray[(indexPath as NSIndexPath).section].fID == Constants.FID.CHANGELOGINPIN) {
                let viewController = UIStoryboard(name: "MyAccount", bundle: nil).instantiateViewController(withIdentifier: "ChangePinVC") as! ChangePin
                viewController.screenTitle = "\((productArray[(indexPath as NSIndexPath).section].name)!)"
                viewController.productId = productArray[(indexPath as NSIndexPath).section].id
                self.pushViewController(viewController)
            }
            else if(productArray[(indexPath as NSIndexPath).section].fID == Constants.FID.REGENRATE_MPIN) {
                let viewController = UIStoryboard(name: "MyAccount", bundle: nil).instantiateViewController(withIdentifier: "SetMPINVC") as! SetMPINVC
                viewController.isMpinSetLater = self.isMpinSetLater
                self.pushViewController(viewController)
            }
            else if(productArray[(indexPath as NSIndexPath).section].fID == Constants.FID.FORGOT_MPIN) {
                
                if isMpinSetLater == false {
                    let viewController = UIStoryboard(name: "MyAccount", bundle: nil).instantiateViewController(withIdentifier: "ForgotLoginPINVC") as! ForgotLoginPINVC
                    viewController.isComingFromForgotMPIN = true
                    self.pushViewController(viewController)
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
            else if (productArray[(indexPath as NSIndexPath).section].fID == Constants.FID.HRA_BALANCE_INQUIRY_FID) {
                if isMpinSetLater == false {
                    let viewController = UIStoryboard(name: "MyAccount", bundle: nil).instantiateViewController(withIdentifier: "BalanceInquiry") as! BalanceInquiry
                    viewController.productID = productArray[(indexPath as NSIndexPath).section].id!
                    viewController.isMpinSetLater = self.isMpinSetLater
                    self.pushViewController(viewController)
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
            else if(productArray[(indexPath as NSIndexPath).section].fID == Constants.FID.MINISTATEMENT_FID) {
                if isMpinSetLater == false {
                    self.showFinancialPinPopup(productArray[(indexPath as NSIndexPath).section].name!, requiredAction: "", delegate: self, productFlowID: productArray[(indexPath as NSIndexPath).section].fID!, productId: "")
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
            else if (productArray[(indexPath as NSIndexPath).section].fID == Constants.FID.HRA_MINI_STATEMENT_FID) {
                
                if isMpinSetLater == false {
                    self.showFinancialPinPopup(productArray[(indexPath as NSIndexPath).section].name!, requiredAction: "", delegate: self, productFlowID: productArray[(indexPath as NSIndexPath).section].fID!, productId: "")
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
            else if(productArray[(indexPath as NSIndexPath).section].fID == Constants.FID.ACCTOCASH_FID || productArray[(indexPath as NSIndexPath).section].fID == Constants.FID.BBTOCORE_FID || productArray[(indexPath as NSIndexPath).section].fID == Constants.FID.TRANSFER_HRA_TO_WALLET) {
                if isMpinSetLater == false {
                    let viewController = UIStoryboard(name: "FundTransfer", bundle: nil).instantiateViewController(withIdentifier: "FundTransferVC") as! FundTransferVC
                    viewController.screenTitleText = "\((productArray[(indexPath as NSIndexPath).section].name)!)"
                    viewController.product = productArray[(indexPath as NSIndexPath).section]
                    viewController.isMpinSetLater = self.isMpinSetLater
                    self.pushViewController(viewController)
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
            else if(productArray[(indexPath as NSIndexPath).section].fID == Constants.FID.ACCTOACC_FID) {
                if isMpinSetLater == false {
                    let viewController = UIStoryboard(name: "FundTransfer", bundle: nil).instantiateViewController(withIdentifier: "FundTransferVC") as! FundTransferVC
                    viewController.screenTitleText = "\((productArray[(indexPath as NSIndexPath).section].name)!)"
                    viewController.product = productArray[(indexPath as NSIndexPath).section]
                    viewController.isMpinSetLater = self.isMpinSetLater
                    self.pushViewController(viewController)
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
            else if(productArray[(indexPath as NSIndexPath).section].fID == Constants.FID.BBToIBFT_FID) {
                if isMpinSetLater == false {
                    let viewController = UIStoryboard(name: "FundTransfer", bundle: nil).instantiateViewController(withIdentifier: "BBToIBFTVC") as! BBToIBFTVC
                    viewController.screenTitleText = "\((productArray[(indexPath as NSIndexPath).section].name)!)"
                    viewController.product = productArray[(indexPath as NSIndexPath).section]
                    viewController.isMpinSetLater = self.isMpinSetLater
                    self.pushViewController(viewController)
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
            else if(productArray[(indexPath as NSIndexPath).section].fID == Constants.FID.BILL_PAYMENT || productArray[(indexPath as NSIndexPath).section].fID == Constants.FID.ZONG_MINILOAD_FID || productArray[(indexPath as NSIndexPath).section].fID == Constants.FID.COLLECTION_PAYMENT || productArray[(indexPath as NSIndexPath).section].fID == Constants.FID.CHALLAN_NUMBER) {
                if isMpinSetLater == false {
                    let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "TransDataEntryVC") as! TransDataEntryVC
                    
                    if(catLable != nil){
                        viewController.screenTitleText = catLable
                    }
                    
                    viewController.screenSubTitleText = "\((productArray[(indexPath as NSIndexPath).section].name)!)"
                    viewController.product = productArray[(indexPath as NSIndexPath).section]
                    viewController.isMpinSetLater = self.isMpinSetLater
                    self.pushViewController(viewController)
                    
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
            else if(productArray[(indexPath as NSIndexPath).section].fID == Constants.FID.CASH_WITHDRAWAL) {
                let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "CashWithdrawalVC") as! CashWithdrawalVC
                viewController.screenTitleText = productArray[(indexPath as NSIndexPath).section].name
                viewController.product = productArray[(indexPath as NSIndexPath).section]
                self.pushViewController(viewController)
            }
            else if(productArray[(indexPath as NSIndexPath).section].fID == Constants.FID.CHANGE_ATM_PIN || productArray[(indexPath as NSIndexPath).section].fID == Constants.FID.GENRATE_ATM_PIN || productArray[(indexPath as NSIndexPath).section].fID == Constants.FID.ATM_CARD_BLOCK || productArray[(indexPath as NSIndexPath).section].fID == Constants.FID.ATM_CARD_ACTIVATION) {
                
                let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "ConfirmationVC") as! ConfirmationVC
                viewController.product = productArray[(indexPath as NSIndexPath).section]
                viewController.isMpinSetLater = self.isMpinSetLater
                self.pushViewController(viewController)
            }
            else if (productArray[indexPath.section].fID == Constants.FID.BOOKME_AIR || productArray[indexPath.section].fID == Constants.FID.BOOKME_HOTEL || productArray[indexPath.section].fID == Constants.FID.BOOKME_CINEMA || productArray[indexPath.section].fID == Constants.FID.BOOKME_EVENTS) {
                if isMpinSetLater == false {
                    let viewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "CustomWebView") as! CustomWebView
                    viewController.screenTitleText = "\((productArray[indexPath.section].name)!)"
                    viewController.product = productArray[indexPath.section]
                    if productArray[indexPath.section].fID == Constants.FID.BOOKME_AIR {
                        viewController.urlString = Constants.BOOKME.AIRLINES_URL
                    }else if productArray[indexPath.section].fID == Constants.FID.BOOKME_HOTEL {
                        viewController.urlString = Constants.BOOKME.HOTELS_URL
                    }else if productArray[indexPath.section].fID == Constants.FID.BOOKME_CINEMA {
                        viewController.urlString = Constants.BOOKME.CINEMAS_URL
                    } else {
                        viewController.urlString = Constants.BOOKME.EVENTS_URL
                    }
                    viewController.isMpinSetLater = self.isMpinSetLater
                    self.pushViewController(viewController)
                    
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
        }
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    func okPressedChallanNo(EncMpin: String) {
        //
    }
}
