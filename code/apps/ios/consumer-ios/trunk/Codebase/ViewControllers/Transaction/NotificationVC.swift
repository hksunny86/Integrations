//
//  NotificationVC.swift
//  Timepey
//
//  Created by Adnan Ahmed on 28/07/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import UIKit
import Foundation


class NotificationVC: BaseViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var descTableView: UITableView!
    @IBOutlet weak var screenTitleLabel: UILabel!
    @IBOutlet weak var amountLabel: UILabel!
    @IBOutlet weak var okButton: UIButton!
    @IBOutlet weak var myTableView: UITableView!
    @IBOutlet weak var myTableViewHightConstraint: NSLayoutConstraint!
    @IBOutlet weak var myScrollView: UIScrollView!
    
    var mobileNo = String()
    var PMTTYPE: String?
    
    var finalResults = [String]()
    var checkOutResponse = (XMLError(), XMLMessage(), [String:String]())
    var totalAmount: String?
    var bankName: String?
    var receiverName: String?
    let userDefault = UserDefaults.standard
    var product: Product?
    var bCnic = ""
    var bMobileNumber = ""
    
    override func viewDidLoad() {
        
        super.viewDidLoad()
        
        myScrollView.delegate = self
        
        self.setupHeaderBarView("", isBackButtonHidden: true, isHomeButtonHidden: false, isSignoutButtonHidden: false)
        
        if(totalAmount != nil){
            amountLabel.text = "PKR \(totalAmount!)"
        }
        
        if(product?.fID == Constants.FID.ZONG_MINILOAD_FID){
            screenTitleLabel.text = Constants.Message.MINI_LOAD_SUCCESS
        }else if(product?.fID == Constants.FID.CASH_WITHDRAWAL){
            screenTitleLabel.text = "Cash Out Successful"
        }
        else if(product?.fID == Constants.FID.RETAILPAYMENT_FID){
            screenTitleLabel.text = Constants.Message.RETAIL_PAY_SUCCESS
        }else if(product?.fID == Constants.FID.BBTOCORE_FID || product?.fID == Constants.FID.ACCTOACC_FID || product?.fID == Constants.FID.ACCTOCASH_FID || product?.fID == Constants.FID.TRANSFER_HRA_TO_WALLET) {
            screenTitleLabel.text = "Money Transfer Successful"
        }else if(product?.fID == Constants.FID.TRANSFER_IN_FID){
            screenTitleLabel.text = "Transfer In Successful"
        }else if(product?.fID == Constants.FID.TRANSFER_OUT_FID){
            screenTitleLabel.text = "Transfer Out Successful"
        }
        else if(product?.fID == Constants.FID.ATM_CARD_ACTIVATION) {
            screenTitleLabel.text = "Card Activation Successful"
            amountLabel.isHidden = true
        }else if(product?.fID == Constants.FID.COLLECTION_PAYMENT) {
            screenTitleLabel.text = "\(checkOutResponse.2["PROD"] ?? "") Successful"
        }
        else {
            
            if(product?.name != nil || product?.name != "") {
                screenTitleLabel.text = "\(checkOutResponse.2["PROD"] ?? "" + " ") \(Constants.Message.TRANSACTION_SUCCESS)"
            }else{
                screenTitleLabel.text = Constants.Message.BILLPAYMENT_SUCCESS
            }
        }
        
        if(product?.name != nil || product?.name != "") {
            screenTitleLabel.text = "\(checkOutResponse.2["PROD"] ?? "" + " ") \(Constants.Message.TRANSACTION_SUCCESS)"
            if product?.fID == Constants.FID.BBToIBFT_FID {
                screenTitleLabel.text = (product?.name)! + Constants.Message.TRANSACTION_SUCCESS
            }
        } else {
            screenTitleLabel.text = Constants.Message.BILLPAYMENT_SUCCESS
        }
        
        switch UIDevice.current.userInterfaceIdiom {
        case .phone:
            myScrollView.frame.size.height = 487
            myTableView.rowHeight = 50
        case .pad:
            myTableView.rowHeight = 60
        default:
            break
        }
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        setupView()
        
    }
    
    
    func setupView(){
        okButton.layer.cornerRadius = 2
        myTableView.layer.cornerRadius = 4
        myTableView.layer.borderWidth = 0.6
        
        var numOfRows = 0
        if(product?.fID == Constants.FID.BBTOCORE_FID || product?.fID == Constants.FID.CHALLAN_NUMBER){
            numOfRows = 7
        }else if(product?.fID == Constants.FID.TRANSFER_IN_FID || product?.fID == Constants.FID.TRANSFER_OUT_FID || product?.fID == Constants.FID.ACCTOCASH_FID || product?.fID == Constants.FID.RETAILPAYMENT_FID || product?.fID == Constants.FID.ATM_CARD_ACTIVATION || product?.fID == Constants.FID.COLLECTION_PAYMENT || product?.fID == Constants.FID.ACCTOACC_FID){
            numOfRows = 6
        }else if(product?.fID == Constants.FID.CASH_WITHDRAWAL || product?.fID == Constants.FID.TRANSFER_HRA_TO_WALLET || product?.fID == Constants.FID.LOAN_PAYMENTS_FID){
            numOfRows = 5
        }else if(product?.fID == Constants.FID.ZONG_MINILOAD_FID){
            numOfRows = 3
        }else if (product?.fID == Constants.FID.BOOKME_BUSES || product?.fID == Constants.FID.BOOKME_AIR || product?.fID == Constants.FID.BOOKME_HOTEL || product?.fID == Constants.FID.BOOKME_EVENTS || product?.fID == Constants.FID.BOOKME_CINEMA){
            numOfRows = 10
        }else if(product?.fID == Constants.FID.BBToIBFT_FID){
            numOfRows = 8
            amountLabel.isHidden = true
        }
        else{
            numOfRows = 5
        }
        let tableHight = Int(myTableView.rowHeight) * numOfRows
        
        var frameSize = myTableView.frame.size
        frameSize.height = CGFloat(tableHight + 5)
        myTableView.frame.size = frameSize
        
        myTableViewHightConstraint.constant = CGFloat(myTableView.frame.size.height)
        
        var okBtnFrame = okButton.frame
        
        switch UIDevice.current.userInterfaceIdiom {
        case .pad:
            okBtnFrame.origin.y = myTableView.frame.origin.y + myTableView.frame.height + 50
        case .phone:
            okBtnFrame.origin.y = myTableView.frame.origin.y + myTableView.frame.height + 10
        default:
            break
        }
        
        
        okButton.frame = okBtnFrame
        
        switch UIDevice.current.userInterfaceIdiom {
        case .phone:
            var size  = myScrollView.contentSize
            size.height = okButton.frame.origin.y + okButton.frame.size.height + 20
            myScrollView.contentSize = size
        case .pad:
            var size  = myScrollView.contentSize
            size.height = okButton.frame.origin.y + okButton.frame.size.height + 40
            myScrollView.contentSize = size
        default:
            break
        }
        
    }
    
    // MARK: UITableViewDataSource
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(product?.fID == Constants.FID.BBToIBFT_FID) {
            return 8
        } else if product?.fID == Constants.FID.BBTOCORE_FID || product?.fID == Constants.FID.CHALLAN_NUMBER {
            return 7
        } else if (product?.fID == Constants.FID.BOOKME_BUSES || product?.fID == Constants.FID.BOOKME_AIR || product?.fID == Constants.FID.BOOKME_HOTEL || product?.fID == Constants.FID.BOOKME_EVENTS || product?.fID == Constants.FID.BOOKME_CINEMA){
            return 10
        } else if(product?.fID == Constants.FID.TRANSFER_IN_FID || product?.fID == Constants.FID.TRANSFER_OUT_FID || product?.fID == Constants.FID.ACCTOCASH_FID || product?.fID == Constants.FID.RETAILPAYMENT_FID || product?.fID == Constants.FID.ATM_CARD_ACTIVATION || product?.fID == Constants.FID.COLLECTION_PAYMENT || product?.fID == Constants.FID.ACCTOACC_FID){
            return 6
        } else if(product?.fID == Constants.FID.CASH_WITHDRAWAL || product?.fID == Constants.FID.TRANSFER_HRA_TO_WALLET || product?.fID == Constants.FID.LOAN_PAYMENTS_FID){
            return 5
        } else if(product?.fID == Constants.FID.ZONG_MINILOAD_FID){
            return 3
        } else {
            return 5
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cellIdentifier = "Cell"
        let cell: UITableViewCell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier)!
        
        if(!(checkOutResponse.2.isEmpty)){
            if(product?.fID == Constants.FID.ZONG_MINILOAD_FID) {
                if((indexPath as NSIndexPath).row == 0){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Consumer No."
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["TMOB"]
                    }
                }else if((indexPath as NSIndexPath).row == 1){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Transaction ID"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["TRXID"]
                    }
                }else if((indexPath as NSIndexPath).row == 3) {
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Charges"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TPAM"] ?? "0.0")"
                    }
                }else if((indexPath as NSIndexPath).row == 2){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Date & Time"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        if(checkOutResponse.2["DATEF"] != nil || checkOutResponse.2["TIMEF"] != nil){
                            valueLabel.text = "\(checkOutResponse.2["DATEF"]!)"
                        }
                    }
                }
            }
            else if(product?.fID == Constants.FID.CASH_WITHDRAWAL){
                if((indexPath as NSIndexPath).row == 0){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Transaction ID"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["TRXID"]
                    }
                }else if((indexPath as NSIndexPath).row == 1){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Amount"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TXAMF"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 2){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Charges"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TPAMF"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 3){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Total Amount"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TAMTF"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 4){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Date & Time"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        if(checkOutResponse.2["DATEF"] != nil || checkOutResponse.2["TIMEF"] != nil){
                            valueLabel.text = "\(checkOutResponse.2["DATEF"]!)"
                        }
                    }
                }
            }
            else if(product?.fID == Constants.FID.ACCTOACC_FID){
                if((indexPath as NSIndexPath).row == 0){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Receiver Mobile No."
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["RCMOB"]
                    }
                }else if((indexPath as NSIndexPath).row == 1){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Receiver A/C Title"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        if checkOutResponse.2["RCNAME"] == "" || checkOutResponse.2["RCNAME"] == nil {
                            valueLabel.text = receiverName
                        }
                        else {
                            valueLabel.text = checkOutResponse.2["RCNAME"]
                        }
                    }
                }
                else if((indexPath as NSIndexPath).row == 2){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Amount"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TXAMF"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 3){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Charges"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TPAMF"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 4) {
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Transaction ID"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["TRXID"]
                    }
                }else if((indexPath as NSIndexPath).row == 5) {
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Date & Time"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        if(checkOutResponse.2["DATEF"] != nil || checkOutResponse.2["TIMEF"] != nil){
                            valueLabel.text = "\(checkOutResponse.2["DATEF"]!)"
                        }
                    }
                }
            }
            else if(product?.fID == Constants.FID.ACCTOCASH_FID){
                if((indexPath as NSIndexPath).row == 0){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Receiver Mobile No."
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["RCMOB"]
                    }
                }else if((indexPath as NSIndexPath).row == 1){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Receiver CNIC"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["RWCNIC"]
                    }
                }else if((indexPath as NSIndexPath).row == 2){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Amount"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TXAMF"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 3){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Charges"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TPAMF"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 4){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Transaction ID"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["TRXID"]
                    }
                }else if((indexPath as NSIndexPath).row == 5){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Date & Time"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        if(checkOutResponse.2["DATEF"] != nil || checkOutResponse.2["TIMEF"] != nil){
                            valueLabel.text = "\(checkOutResponse.2["DATEF"]!)"
                        }
                    }
                }
                
            }
            else if(product?.fID == Constants.FID.BBTOCORE_FID) {
                if((indexPath as NSIndexPath).row == 0){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Receiver Mobile No."
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["RCMOB"]
                    }
                }else if((indexPath as NSIndexPath).row == 1){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Receiver A/C No."
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["COREACID"]
                    }
                }else if((indexPath as NSIndexPath).row == 2){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Receiver A/C Title"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["COREACTL"]
                    }
                }else if((indexPath as NSIndexPath).row == 3){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Amount"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TXAMF"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 4){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Charges"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TPAMF"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 5){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Transaction ID"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["TRXID"]
                    }
                }else if((indexPath as NSIndexPath).row == 6){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Date & Time"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        if(checkOutResponse.2["DATEF"] != nil || checkOutResponse.2["TIMEF"] != nil){
                            valueLabel.text = "\(checkOutResponse.2["DATEF"]!)"
                        }
                    }
                }
                
            }
            else if(product?.fID == Constants.FID.BBToIBFT_FID){
                if((indexPath as NSIndexPath).row == 0){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Receiver Bank"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = bankName!
                    }
                }else if((indexPath as NSIndexPath).row == 1){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Receiver Mobile No."
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["RCMOB"]
                    }
                }else if((indexPath as NSIndexPath).row == 2){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Receiver A/C No."
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["COREACID"]
                    }
                }else if((indexPath as NSIndexPath).row == 3){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Receiver A/C Title"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["COREACTL"]
                    }
                }else if((indexPath as NSIndexPath).row == 4){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Amount"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TXAMF"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 5){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Charges"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TPAMF"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 6){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Transaction ID"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["TRXID"]
                    }
                }else if((indexPath as NSIndexPath).row == 7){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Date & Time"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        if(checkOutResponse.2["DATEF"] != nil || checkOutResponse.2["TIMEF"] != nil){
                            valueLabel.text = "\(checkOutResponse.2["DATEF"]!)"
                        }
                    }
                }
            }
            else if(product?.fID == Constants.FID.TRANSFER_IN_FID){
                if((indexPath as NSIndexPath).row == 0){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "From Account"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["COREACID"]
                    }
                }else if((indexPath as NSIndexPath).row == 1){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "To Account"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["BBACID"]
                    }
                }else if((indexPath as NSIndexPath).row == 2){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Amount"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TXAMF"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 3){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Charges"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TPAMF"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 4){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Transaction ID"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["TRXID"]!
                    }
                }else if((indexPath as NSIndexPath).row == 5){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Date & Time"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        if(checkOutResponse.2["DATEF"] != nil || checkOutResponse.2["TIMEF"] != nil){
                            valueLabel.text = "\(checkOutResponse.2["DATEF"]!)"
                        }
                    }
                }
            }
            else if(product?.fID == Constants.FID.TRANSFER_OUT_FID){
                //print(checkOutResponse.2)
                if((indexPath as NSIndexPath).row == 0){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "From Account"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["BBACID"]
                    }
                }else if((indexPath as NSIndexPath).row == 1){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "To Account"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["COREACID"]
                    }
                }else if((indexPath as NSIndexPath).row == 2){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Amount"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TXAMF"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 3){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Charges"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TPAMF"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 4){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Transaction ID"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["TRXID"]!
                    }
                }else if((indexPath as NSIndexPath).row == 5){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Date & Time"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        if(checkOutResponse.2["DATEF"] != nil || checkOutResponse.2["TIMEF"] != nil){
                            valueLabel.text = "\(checkOutResponse.2["DATEF"]!)"
                        }
                    }
                }
            }
            else if(product?.fID == Constants.FID.RETAILPAYMENT_FID){
                if((indexPath as NSIndexPath).row == 0){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Merchant"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["RANAME"]
                    }
                }
                else if((indexPath as NSIndexPath).row == 1){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Merchant Mobile No."
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["AMOB"]
                    }
                }
                else if((indexPath as NSIndexPath).row == 2){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Amount"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TXAMF"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 3){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Charges"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TPAMF"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 4){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Transaction ID"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["TRXID"]
                    }
                }
                else if((indexPath as NSIndexPath).row == 5){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Date & Time"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        if(checkOutResponse.2["DATEF"] != nil || checkOutResponse.2["TIMEF"] != nil){
                            valueLabel.text = "\(checkOutResponse.2["DATEF"]!)"
                        }
                    }
                }
            }
            else if(product?.fID == Constants.FID.ATM_CARD_ACTIVATION){
                
                if((indexPath as NSIndexPath).row == 0){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Mobile No."
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["MOBN"]
                    }
                }
                else if((indexPath as NSIndexPath).row == 1){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "CNIC No."
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["CNIC"]
                    }
                }
                else if((indexPath as NSIndexPath).row == 2){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Name"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["ACTITLE"]
                    }
                }else if((indexPath as NSIndexPath).row == 3){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Card No."
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["CARDNO"]
                    }
                }else if((indexPath as NSIndexPath).row == 4){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Card Program"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["CPNAME"]
                    }
                }
                else if((indexPath as NSIndexPath).row == 5){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Date & Time"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        if(checkOutResponse.2["DATEF"] != nil || checkOutResponse.2["TIMEF"] != nil){
                            valueLabel.text = checkOutResponse.2["DATEF"]
                        }
                    }
                }
            }
            else if(product?.fID == Constants.FID.COLLECTION_PAYMENT){
                if((indexPath as NSIndexPath).row == 0){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Transaction Type"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = product?.name
                    }
                }else if((indexPath as NSIndexPath).row == 1){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Mobile No."
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["MOBN"]
                    }
                }else if((indexPath as NSIndexPath).row == 2){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "CNIC"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["CNIC"]
                    }
                }else if((indexPath as NSIndexPath).row == 3){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Charges"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TPAMF"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 4){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Transaction ID"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["TRXID"]
                    }
                }else if((indexPath as NSIndexPath).row == 5){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Date & Time"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        if(checkOutResponse.2["DATEF"] != nil || checkOutResponse.2["TIMEF"] != nil){
                            valueLabel.text = "\(checkOutResponse.2["DATEF"]!)"
                        }
                    }
                }
            }
            else if(product?.fID == Constants.FID.CHALLAN_NUMBER){
                
                if((indexPath as NSIndexPath).row == 0){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Mobile No."
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = mobileNo
                    }
                } else if((indexPath as NSIndexPath).row == 1){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Challan No."
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["CONSUMER"]
                    }
                } else if((indexPath as NSIndexPath).row == 2){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Transaction ID"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["TRXID"]!
                    }
                }
                else if((indexPath as NSIndexPath).row == 3){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Amount"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TXAMF"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 4){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Charges"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["CAMTF"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 5){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Total Amount"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TAMTF"]!)"
                    }
                } else if((indexPath as NSIndexPath).row == 6){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Date & Time"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["DATEF"]!
                    }
                }
                
            }
            else if (product?.fID == Constants.FID.TRANSFER_HRA_TO_WALLET) {
                
                if((indexPath as NSIndexPath).row == 0){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Transaction ID"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["TRXID"]
                    }
                }else if((indexPath as NSIndexPath).row == 1){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Amount"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TXAM"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 2){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Charges"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TPAMF"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 3){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Total Amount"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TAMT"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 4){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Date & Time"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        if(checkOutResponse.2["DATE"] != nil){
                            valueLabel.text = "\(checkOutResponse.2["DATE"]!)"
                        }
                    }
                }
            }
            else if (product?.fID == Constants.FID.LOAN_PAYMENTS_FID) {
                if((indexPath as NSIndexPath).row == 0){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Transaction ID"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["TRXID"]
                    }
                }else if((indexPath as NSIndexPath).row == 1){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Amount"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TXAM"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 2){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Charges"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TPAMF"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 3){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Total Amount"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TAMT"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 4){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Date & Time"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        if(checkOutResponse.2["DATEF"] != nil || checkOutResponse.2["TIMEF"] != nil){
                            valueLabel.text = "\(checkOutResponse.2["DATEF"]!)"
                        }
                    }
                }
            }
            else if (product?.fID == Constants.FID.BOOKME_BUSES || product?.fID == Constants.FID.BOOKME_AIR || product?.fID == Constants.FID.BOOKME_HOTEL || product?.fID == Constants.FID.BOOKME_EVENTS || product?.fID == Constants.FID.BOOKME_CINEMA) {
                if((indexPath as NSIndexPath).row == 0) {
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Transaction ID"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["TRXID"]
                    }
                }else if((indexPath as NSIndexPath).row == 1) {
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Transaction Type"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = product?.name
                    }
                }else if((indexPath as NSIndexPath).row == 2){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Mobile Number"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["CMOB"]
                    }
                }else if((indexPath as NSIndexPath).row == 3){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "CNIC"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = Customer.sharedInstance.cnic!
                    }
                }else if((indexPath as NSIndexPath).row == 4){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "BookMe Mobile No."
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "\(bMobileNumber)"
                    }
                    
                }else if((indexPath as NSIndexPath).row == 5){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "BookMe CNIC"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "\(bCnic)"
                    }
                    
                }
                else if((indexPath as NSIndexPath).row == 6){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Amount"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TXAM"]!)"
                    }
                    
                }
                else if((indexPath as NSIndexPath).row == 7) {
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Charges"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TPAM"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 8){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Total Amount"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TAMT"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 9){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Date & Time"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        if(checkOutResponse.2["DATEF"] != nil || checkOutResponse.2["TIMEF"] != nil){
                            valueLabel.text = "\(checkOutResponse.2["DATEF"]!)"
                        }
                    }
                }
            }
            else{
                if((indexPath as NSIndexPath).row == 0){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Billing Company"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["PNAME"]
                    }
                }else if((indexPath as NSIndexPath).row == 1){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        //print(product)
                        titleLabel.text = product?.label!
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["CONSUMER"]
                    }
                }else if((indexPath as NSIndexPath).row == 2) {
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Charges"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(checkOutResponse.2["TPAM"] ?? "0.0")"
                    }
                }
                else if((indexPath as NSIndexPath).row == 3){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Transaction ID"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = checkOutResponse.2["TRXID"]
                    }
                }else if((indexPath as NSIndexPath).row == 4){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Date & Time"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        if(checkOutResponse.2["DATEF"] != nil || checkOutResponse.2["TIMEF"] != nil){
                            valueLabel.text = "\(checkOutResponse.2["DATEF"]!)"
                        }
                    }
                }
            }
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, viewForFooterInSection section: Int) -> UIView? {
        let footerView = UIView(frame: CGRect(x:0, y:0, width:tableView.frame.size.width, height: 5))
        footerView.backgroundColor = UIColor(red:0.00, green:0.28, blue:0.56, alpha:1.0)
        return footerView
    }
    
    func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        return 5.0
    }
    
    @IBAction func okButtonPressed(_ sender: UIButton) {
        self.popViewControllerAndGotoStart()
    }
}

