//
//  MasterPassSuccessVC.swift
//  JSBL-MB
//
//  Created by Maria Alvi on 11/20/17.
//  Copyright © 2017 inov8. All rights reserved.
//

import Foundation
import UIKit


class MasterPassSuccessVC: BaseViewController,UITableViewDelegate,UITableViewDataSource {
    @IBOutlet weak var tblSucessView: UITableView!
    @IBOutlet weak var lblAmount: UILabel!
    let arrHeaderTitles = ["Merchant Details","Payment Details"]
    var arrTitles = [[""]]
    var arrValues = [[""]]
    var responeDict = [String:String]()
    var dict = [String:String]()
    var arrCategory = [Category]()
    var subCategory =  [Category]()
    var amount = String()
    var QRResponseObj : retailMerchant = retailMerchant()
    var fromAccoutNo = String()
    var fromAccoutTitle = String()
    
    var accountTitle = String()
    var accountNumber = String()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.setupHeaderBarView("QR Payment", isBackButtonHidden: true, isHomeButtonHidden: false, isSignoutButtonHidden: false)
        
        lblAmount.text = "PKR \(QRResponseObj.totalAmountFormatted)"
        
        arrTitles = [["Merchant ID","Merchant Name"]]
        arrValues = [[QRResponseObj.merchantID,QRResponseObj.merchantName]]
        
        arrTitles.append(["Transaction Type","Amount","Transaction Fee"])
        arrValues.append(["QR Payment","PKR \(QRResponseObj.totalAmountFormatted)","PKR 0.0"])
        
    }
    
    //  MARK: UITableView
    func tableView(_ tableView: UITableView, viewForFooterInSection section: Int) -> UIView? {
        
        let view:UIView = UIView.init(frame: CGRect.init(x: 0, y: 0, width: self.tblSucessView.bounds.size.width, height: 10))
        view.backgroundColor = .clear
        return view
    }
    func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        return 10.0
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 30
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        
        let view = UIView(frame: CGRect(x: 0, y: 0, width: tblSucessView.frame.size.width, height:30))
        let label = UILabel(frame: view.bounds)
        label.text = arrHeaderTitles[section]
        label.textColor = UIColor.lightGray
        label.backgroundColor = self.view.backgroundColor
        if #available(iOS 8.2, *) {
            label.font = UIFont.systemFont(ofSize: 13, weight: .light)
        } else {
            label.font = UIFont.systemFont(ofSize: 13)
        }
        view.addSubview(label)
        view.backgroundColor = UIColor.clear
        return view
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 50
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return arrHeaderTitles.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        let arrTitle = arrTitles[section]
        return arrTitle.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let identifier = "Cell"
        let cell = UITableViewCell(style: UITableViewCell.CellStyle.value1, reuseIdentifier: identifier)
        
        //        let cell: UITableViewCell = tableView.dequeueReusableCell(withIdentifier: identifier)!
        
        cell.textLabel?.text = arrTitles[indexPath.section][indexPath.row]
        cell.detailTextLabel?.text = arrValues[indexPath.section][indexPath.row]
        
        if #available(iOS 8.2, *) {
            cell.textLabel?.font = UIFont.systemFont(ofSize: 14, weight: .light)
            cell.detailTextLabel?.font = UIFont.systemFont(ofSize: 14, weight: .light)
        } else {
            cell.textLabel?.font = UIFont.systemFont(ofSize: 14)
            cell.detailTextLabel?.font = UIFont.systemFont(ofSize: 14)
        }
        cell.textLabel?.textColor = UIColor.darkGray
        cell.detailTextLabel?.textColor = UIColor.lightGray
        cell.layer.borderColor = UIColor.init(red: 159.0/255.0, green: 159.0/255.0, blue: 159.0/255.0, alpha: 1.0).cgColor
        cell.layer.borderWidth = 0.2
        cell.preservesSuperviewLayoutMargins = false
        cell.separatorInset = UIEdgeInsets.zero
        cell.layoutMargins = UIEdgeInsets.zero
        cell.layer.cornerRadius = 2
        return cell
    }
    
    @IBAction func actOkay(_ sender: UIButton) {
        
        self.popViewControllerAndGotoStart()
    }
}
