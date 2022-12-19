//
//  FPAJSWalletAccountOpenBaseVC.swift
//  FalconApp
//
//  Created by M Zeshan Arif on 17/09/2017.
//  Copyright © 2017 Wateen. All rights reserved.
//

import UIKit

class FPAJSWalletAccountOpenBaseVC: BaseViewController, UITableViewDataSource, UITableViewDelegate {

    // MARK: - Outlets -
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var tableViewHeight: NSLayoutConstraint!
    @IBOutlet weak var scrollView: UIScrollView!
    
    // MARK: - Variables -
    var account: JSAccount!
    var accountOpenDataSource = FPAJSWalletAccountOpenDataSource()
    
    
    func setupView()  {
        
        //topView.updateTitle("Account Opening")
        
        let summryNib = UINib(nibName: "FPAJSAccountOpenSummaryCell", bundle: nil)
        tableView.register(summryNib, forCellReuseIdentifier: "FPAJSAccountOpenSummaryCell")
        
        let imageCellNib = UINib(nibName: "FPAJSAccountOpenImageCell", bundle: nil)
        tableView.register(imageCellNib, forCellReuseIdentifier: "FPAJSAccountOpenImageCell")
        
    }
    
    func initialization(){
        
    }
    
//    func prepareDataSource(){
//        let customerDetailSection = FPAJSWalletAccountOpenSection()
//        customerDetailSection.title = "Customer Details"
//        
//        let mobileNoRow = FPAJSWalletAccountOpenRow()
//        mobileNoRow.title = "Mobile Number"
//        mobileNoRow.value = account.accountNo
//        mobileNoRow.rowType = .summary
//        customerDetailSection.rows.append(mobileNoRow)
//        
//        let nameRow = FPAJSWalletAccountOpenRow()
//        nameRow.title = "Name"
//        nameRow.value = account.name
//        nameRow.rowType = .summary
//        customerDetailSection.rows.append(nameRow)
//        
//        let cnicRow = FPAJSWalletAccountOpenRow()
//        cnicRow.title = "CNIC"
//        cnicRow.value = account.cnic
//        cnicRow.rowType = .summary
//        customerDetailSection.rows.append(cnicRow)
//        
////        let dobRow = FPAJSWalletAccountOpenRow()
////        dobRow.title = "Date of Birth"
////        dobRow.value = account.dob
////        dobRow.rowType = .summary
////        dobRow.dateFormat = "(dd-mm-yyyy)"
////        customerDetailSection.rows.append(dobRow)
////
////        let cnicExpiryDate = FPAJSWalletAccountOpenRow()
////        cnicExpiryDate.title = "CNIC Expiry Date"
////        cnicExpiryDate.value = account.cnicExpiryDate
////        cnicExpiryDate.rowType = .summary
////        cnicExpiryDate.dateFormat = "(dd-mm-yyyy)"
////        customerDetailSection.rows.append(cnicExpiryDate)
//        
//        accountOpenDataSource.sections.append(customerDetailSection)
//    }
    
    
    // MARK: -Table View Data Source-
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return accountOpenDataSource.sections.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        let accountSection = accountOpenDataSource.sections[section]
        return accountSection.rows.count
    }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        let accountSection = accountOpenDataSource.sections[section]
        return accountSection.title
    }
    
    
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerView = UIView()
        headerView.backgroundColor = self.view.backgroundColor
        
        let headerLabel = UILabel(frame: CGRect(x: 12, y: 4.5, width:
            tableView.bounds.size.width, height: tableView.bounds.size.height))
        headerLabel.font = UIFont(name: "Roboto-Light", size: 14)
        headerLabel.textColor = UIColor.lightGray
        headerLabel.text = self.tableView(self.tableView, titleForHeaderInSection: section)
        headerLabel.sizeToFit()
        headerView.addSubview(headerLabel)
        
        return headerView
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 30
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let accountSection = accountOpenDataSource.sections[indexPath.section]
        let accountRow = accountSection.rows[indexPath.row]
        
        switch accountRow.rowType {
        case .summary:
            if let cell = tableView.dequeueReusableCell(withIdentifier: "FPAJSAccountOpenSummaryCell") as? FPAJSAccountOpenSummaryCell{
                cell.accountRow = accountRow
                return cell
            }
            break
        case .image:
            if let cell = tableView.dequeueReusableCell(withIdentifier: "FPAJSAccountOpenImageCell") as? FPAJSAccountOpenImageCell{
                cell.accountRow = accountRow
                return cell
            }
            break
        }
        return UITableViewCell()
    }
    
    
}
