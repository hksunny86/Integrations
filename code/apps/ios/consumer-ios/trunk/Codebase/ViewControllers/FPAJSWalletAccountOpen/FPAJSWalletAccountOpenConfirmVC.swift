//
//  FPAJSWalletAccountOpenConfirmVC.swift
//  FalconApp
//
//  Created by M Zeshan Arif on 17/09/2017.
//  Copyright © 2017 Wateen. All rights reserved.
//

import UIKit

class FPAJSWalletAccountOpenConfirmVC: FPAJSWalletAccountOpenBaseVC {

    @IBOutlet weak var btnConfirm: FPAUIButton!
    @IBOutlet weak var btnCancel: UIButton!
    
    // MARK: - Overridden Methods -
    override func prepareDataSource() {
        super.prepareDataSource()
        
        let customerDocDetailSection = FPAJSWalletAccountOpenSection()
        customerDocDetailSection.title = "Customer Document Details"
        
        if let image =  account.customerImage{
            let customerPhotoRow = FPAJSWalletAccountOpenRow()
            customerPhotoRow.title = "Customer Photo"
            customerPhotoRow.image = image
            customerPhotoRow.rowType = .image
            customerDocDetailSection.rows.append(customerPhotoRow)
        }
        
        if let image =  account.cnicImage{
            let cnicPhotoRow = FPAJSWalletAccountOpenRow()
            cnicPhotoRow.title = "CNIC Front Photo"
            cnicPhotoRow.image = image
            cnicPhotoRow.rowType = .image
            customerDocDetailSection.rows.append(cnicPhotoRow)
        }
        
        accountOpenDataSource.sections.append(customerDocDetailSection)
        
        tableView.dataSource = self
        tableView.delegate = self
        tableView.rowHeight = UITableViewAutomaticDimension
        tableView.estimatedRowHeight = 100
        tableView.reloadData()
        self.view.layoutIfNeeded()
        self.view.layoutSubviews()
        tableViewHeight.constant = tableView.contentSize.height
        
        self.view.layoutIfNeeded()
        self.view.layoutSubviews()
        
    }
    
    override func setupView()  {
        super.setupView()
        
        Utility.roundButton(btn: btnConfirm)
        Utility.roundButton(btn: btnCancel)
    }
    
    override func initialization(){
        super.initialization()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Setup UI
        setupView()
        
        // Initialization
        initialization()
    }
    
    
    
//    @IBAction func btnConfirm_TouchUpInside(_ sender: FPAUIButton) {
//
//        self.showLoadingView()
//        DispatchQueue.global().async {
//            
//            
//            let dict: [String: String] = [
//                "CREATE_VCARD" : "0",
//                "CNIC" : self.account.cnic,
//                "MOBN" : self.account.accountNo!,
//                "DTID" : Constants.AppConfig.DTID_KEY,
//                "CNAME"  : self.account.name,
//                "DOB"    : self.account.dob,
//                "CNIC_EXP": self.account.cnicExpiryDate,
//                "ATYPE"   : MOJSWalletAccountType.L0.rawValue,
//                "IMG_PROFILE" : self.account.customerImageBase64,
//                "IMG_CNIC" : self.account.cnicImageBase64,
//                "DATE_TIME" : Utility.getDate(date: NSDate() as Date, withFormat: "yyyyMMddHHmmss")
//            ]
//            
//            
//            BankApiManager.accountOpening(dict, successBlock: { (response) in
//                
//                OnoXmlParser.parseAccountOpenResponse(response, successBlock: { (walletAccount, virtualCard, description) in
//                    
//                    DispatchQueue.main.async {
//                        
//                        let successPopUp = FPAJSWalletAccountOpenSuccessPopup(titleAndMessage: "Success", message: description, okMessage: "OK", cancelMessage: "VIEW VIRTUAL CARD", okButtonPressed: { (sender) in
//                            if let sender = sender as? BasePopup{
//                                sender.hide(completion: {})
//                            }
//                            
//                            // Navigate to Wallet
//                            
//                        }, cancelButtonPressed: { (sender) in
//                            if let sender = sender as? BasePopup{
//                                sender.hide(completion: {})
//                            }
//                            
//                            
//                            if let account = virtualCard{
//                                let vc = self.storyboard?.instantiateViewController(withIdentifier: "FPAVirtualCardDetailViewController") as! FPAVirtualCardDetailViewController
//                                vc.accounts = [account]
//                                vc.account = account
//                                vc.currentIndex = 0
//                                self.navigationController?.pushViewController(vc, animated: true)
//                            }
//                            
//                        })
//                        successPopUp?.show(self.view)
//                    }
//                    
//                }, failureBlock: { (error) in
//                    DispatchQueue.main.async {
//                        self.showMessage(error?.message!, completion: { (sender) in
//                            sender?.removeFromSuperview()
//                        })
//                    }
//                })
//                DispatchQueue.main.async {
//                    self.hideLoadingView()
//                }
//            }) { (error) in
//                DispatchQueue.main.async {
//                    self.showMessage(error?.localizedDescription, completion: { (sender) in
//                        sender?.removeFromSuperview()
//                    })
//                    self.hideLoadingView()
//                }
//            }
//        }
//    }
    
    @IBAction func btnCancel_TouchUpInside(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    
    
}




