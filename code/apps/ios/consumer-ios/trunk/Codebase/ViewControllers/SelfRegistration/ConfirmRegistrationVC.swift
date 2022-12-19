//
//  ConfirmRegistrationVC.swift
//  JSBL-BB
//
//  Created by Adnan Ahmed on 25/09/2017.
//  Copyright © 2017 Inov8. All rights reserved.
//

import UIKit


class ConfirmRegistrationVC: FPAJSWalletAccountOpenBaseVC {

    @IBOutlet weak var btnConfirm: FPAUIButton!
    
    // MARK: - Overridden Methods -
    func prepareDataSource() {
        
        //super.prepareDataSource()
        
        let customerDetailSection = FPAJSWalletAccountOpenSection()
        customerDetailSection.title = "Customer Details"
        
        let mobileNoRow = FPAJSWalletAccountOpenRow()
        mobileNoRow.title = "Mobile Number"
        mobileNoRow.value = account.accountNo
        mobileNoRow.rowType = .summary
        customerDetailSection.rows.append(mobileNoRow)
        
        let cnicRow = FPAJSWalletAccountOpenRow()
        cnicRow.title = "CNIC"
        cnicRow.value = account.cnic
        cnicRow.rowType = .summary
        customerDetailSection.rows.append(cnicRow)
        
        
//        let dobRow = FPAJSWalletAccountOpenRow()
//        dobRow.title = "Date of Birth"
//        if(account.isDiscrepant == "1"){
//            dobRow.value = Utility.convertDateFormater(account.dob)
//        }else{
//            dobRow.value = account.dob
//        }
//        dobRow.rowType = .summary
//        dobRow.dateFormat = "(dd-mm-yyyy)"
//        customerDetailSection.rows.append(dobRow)
        
        let cnicExpiryDate = FPAJSWalletAccountOpenRow()
        cnicExpiryDate.title = "CNIC Issue Date"
//        if(account.isDiscrepant == "1"){
//            cnicExpiryDate.value = Utility.convertDateFormater(account.cnicExpiryDate)
//        }else{
//            cnicExpiryDate.value = account.cnicExpiryDate
//        }
        cnicExpiryDate.value = account.cnicIssueDate
        
        cnicExpiryDate.rowType = .summary
        cnicExpiryDate.dateFormat = "(dd-mm-yyyy)"
        customerDetailSection.rows.append(cnicExpiryDate)
        
        let nameRow = FPAJSWalletAccountOpenRow()
        nameRow.title = "Email Address"
        nameRow.value = account.customerEmail
        nameRow.rowType = .summary
        customerDetailSection.rows.append(nameRow)
        
        let mobileNetworkRow = FPAJSWalletAccountOpenRow()
        mobileNetworkRow.title = "Customer Mobile Network"
        mobileNetworkRow.value = account.customerEmail
        mobileNetworkRow.rowType = .summary
        customerDetailSection.rows.append(mobileNoRow)
        
        
        
        accountOpenDataSource.sections.append(customerDetailSection)
        
//        let customerDocDetailSection = FPAJSWalletAccountOpenSection()
//        customerDocDetailSection.title = "Customer Document Details"
//
//        if let image =  account.customerImage{
//            let customerPhotoRow = FPAJSWalletAccountOpenRow()
//            customerPhotoRow.title = "Customer Photo"
//            customerPhotoRow.image = image
//            customerPhotoRow.rowType = .image
//            customerDocDetailSection.rows.append(customerPhotoRow)
//        }
//
//        if let image =  account.cnicImage{
//            let cnicPhotoRow = FPAJSWalletAccountOpenRow()
//            cnicPhotoRow.title = "CNIC Front Photo"
//            cnicPhotoRow.image = image
//            cnicPhotoRow.rowType = .image
//            customerDocDetailSection.rows.append(cnicPhotoRow)
//        }
        
//        accountOpenDataSource.sections.append(customerDocDetailSection)
        
        tableView.dataSource = self
        tableView.delegate = self
        tableView.rowHeight = UITableView.automaticDimension
        tableView.estimatedRowHeight = 100
        tableView.reloadData()
        self.view.layoutIfNeeded()
        self.view.layoutSubviews()
        tableViewHeight.constant = tableView.contentSize.height
        
//        self.view.layoutIfNeeded()
//        self.view.layoutSubviews()
        
    }
    
    override func setupView()  {
        super.setupView()
        prepareDataSource()
        
        btnConfirm.layer.cornerRadius = Constants.UI.Button.CORNER_RADIUS
    }
    
    override func initialization(){
        super.initialization()
    }
    
    override func viewDidLoad() {
        //super.viewDidLoad()
        
        // Setup UI
        setupView()
        
        self.setupHeaderBarView("Open Account", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: true)
        
        // Initialization
        initialization()
    }
    
    
    
        @IBAction func btnConfirm_TouchUpInside(_ sender: FPAUIButton) {
            self.showLoadingView()
        }
}




