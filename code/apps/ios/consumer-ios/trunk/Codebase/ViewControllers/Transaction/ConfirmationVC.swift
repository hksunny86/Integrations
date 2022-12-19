//
//  ConfirmationVC.swift
//  Timepey
//
//  Created by Adnan Ahmed on 28/07/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import UIKit
import Foundation


class ConfirmationVC: BaseViewController, UITableViewDelegate, UITableViewDataSource, UITextFieldDelegate, UIScrollViewDelegate, FinancialPinPopupDelegate {
    
    
    
    @IBOutlet weak var myScrollView: UIScrollView!
    @IBOutlet weak var screenTitleLabel: UILabel!
    @IBOutlet weak var screenSubTitleLabel: UILabel!
    @IBOutlet weak var myTableView: UITableView!
    @IBOutlet weak var amountParentView: UIView!
    @IBOutlet weak var amountLabel: UILabel!
    @IBOutlet weak var amountTextField: UITextField!
    @IBOutlet weak var amountHintLabel: UILabel!
    @IBOutlet weak var confirmButton: UIButton!
    @IBOutlet weak var cancelButton: UIButton!
    @IBOutlet weak var hintLabel: UILabel!
    @IBOutlet weak var myTableViewTopConstraint: NSLayoutConstraint!
    @IBOutlet weak var myTableViewHightConstraint: NSLayoutConstraint!
    
    
    var CNSMRNO: String?
    var MPIN: String?
    var CSCD: String?
    var product: Product?
    var selectedPrpTranx: String?
    var selectedPrpTranxCode: String?
    var bankID: String?
    var bankName: String?
    var responseDict = [String:String]()
    var orderDetails = [String:String]()
    var screenTitleText: String?
    var receiverName = "0.0"
    var departureCity = "0.0"
    var arrivalCity = "0.0"
    let userDefault = UserDefaults.standard
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: false, isSignoutButtonHidden: false)
        
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard))
        view.addGestureRecognizer(tap)
        
        myScrollView.delegate = self
        
        if(product?.name != nil) {
            screenTitleLabel.text = product?.name!
        }
        
        if(product?.fID == Constants.FID.CASH_WITHDRAWAL_LEG1_FID && screenTitleText != nil){
            screenTitleLabel.text = screenTitleText!
        }
        
        if(product?.inrequired == "1" && product?.pprequired == "1") {
            amountTextField.delegate = self
            amountTextField.keyboardType = UIKeyboardType.numberPad;
            amountTextField.layer.cornerRadius = 2
            amountTextField.layer.borderWidth = 0.7
            amountTextField.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
            let paddingForFirst = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.amountTextField.frame.size.height))
            amountTextField.leftView = paddingForFirst
            amountTextField.leftViewMode = UITextField.ViewMode .always
            
            if(product?.minamtf != nil && product?.maxamtf != nil){
                amountHintLabel.text = "Enter an amount of PKR \((product?.minamtf)!) to PKR \((product?.maxamtf)!)"
            }
        } else {
            amountParentView.isHidden = true
        }
        
        switch UIDevice.current.userInterfaceIdiom {
        case .phone:
            self.view.frame.size.height = 670
            myScrollView.frame.size.height = 487
            myTableView.frame.size.height = 280
            myTableView.rowHeight = 50
        case .pad:
            myTableView.rowHeight = 60
        default:
            break
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        if(product?.fID == Constants.FID.CHANGE_ATM_PIN || product?.fID == Constants.FID.GENRATE_ATM_PIN || product?.fID == Constants.FID.ATM_CARD_BLOCK || product?.fID == Constants.FID.ATM_CARD_ACTIVATION){
            hideSubViews()
            cardServicesPinRequestInfo()
        }
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        setupView()
        
    }
    
    func hideSubViews(){
        screenSubTitleLabel.isHidden = true
        myTableView.isHidden = true
        hintLabel.isHidden = true
        confirmButton.isHidden = true
        cancelButton.isHidden = true
    }
    
    func showSubViews(){
        myTableView.isHidden = false
        hintLabel.isHidden = false
        confirmButton.isHidden = false
        cancelButton.isHidden = false
    }
    
    func setupView(){
        
        confirmButton.layer.cornerRadius = 2
        cancelButton.layer.cornerRadius = 2
        myTableView.layer.borderWidth = 0.6
        
        var numOfRows = 0
        if(product?.fID == Constants.FID.CASH_WITHDRAWAL_LEG1_FID || product?.fID == Constants.FID.ZONG_MINILOAD_FID){
            numOfRows = 2
        }
        else if(product?.fID == Constants.FID.CASH_WITHDRAWAL){
            numOfRows = 3
        }
        else if(product?.fID == Constants.FID.TRANSFER_HRA_TO_WALLET){
            numOfRows = 3
        }
        else if(product?.fID == Constants.FID.RETAILPAYMENT_FID || product?.fID == Constants.FID.LOAN_PAYMENTS_FID){
            numOfRows = 5
        }else if(product?.fID == Constants.FID.ACCTOACC_FID){
            numOfRows = 5
        }else if(product?.fID == Constants.FID.BBToIBFT_FID){
            numOfRows = 7
        }else if(product?.fID == Constants.FID.BBTOCORE_FID || product?.fID == Constants.FID.COLLECTION_PAYMENT){
            numOfRows = 6
        }else if (product?.fID == Constants.FID.CHALLAN_NUMBER) {
            numOfRows = 8
        }
        else if (product?.fID == Constants.FID.BOOKME_BUSES || product?.fID == Constants.FID.BOOKME_HOTEL || product?.fID == Constants.FID.BOOKME_AIR) {
            numOfRows = 12
            isMore = true
        }
        else if product?.fID == Constants.FID.BOOKME_EVENTS || product?.fID == Constants.FID.BOOKME_CINEMA {
            numOfRows = 13
        }
        else if(product?.fID == Constants.FID.TRANSFER_IN_FID || product?.fID == Constants.FID.TRANSFER_OUT_FID || product?.fID == Constants.FID.CHANGE_ATM_PIN || product?.fID == Constants.FID.GENRATE_ATM_PIN || product?.fID == Constants.FID.ATM_CARD_BLOCK || product?.fID == Constants.FID.ATM_CARD_ACTIVATION || product?.fID == Constants.FID.ACCTOCASH_FID){
            numOfRows = 5
        }
        else{
            //Bill Payment
            if(product?.inrequired == "0"){
                numOfRows = 3
            } else if product?.denomFlag == "1" {
                numOfRows = 3
            } else {
                numOfRows = 7
            }
        }
        myTableView.layer.cornerRadius = 4
        
        let tableHight = Int(myTableView.rowHeight) * numOfRows
        if(amountParentView.isHidden){
            var frameSize = myTableView.frame
            frameSize.size.height = CGFloat(tableHight + 5)
            frameSize.origin.y = amountParentView.frame.origin.y
            myTableView.frame = frameSize
            
            switch UIDevice.current.userInterfaceIdiom {
            case .phone:
                myTableViewTopConstraint.constant = -112
            case .pad:
                myTableViewTopConstraint.constant = -178
            default:
                break
            }
            
        }else{
            var frameSize = myTableView.frame.size
            frameSize.height = CGFloat(tableHight + 5)
            myTableView.frame.size = frameSize
            myTableViewTopConstraint.constant = -1
        }
        
        myTableViewHightConstraint.constant = CGFloat(myTableView.frame.size.height)
        
        var hintLabelFrame = hintLabel.frame
        
        switch UIDevice.current.userInterfaceIdiom{
        case .phone:
            hintLabelFrame.origin.y = myTableView.frame.origin.y + myTableView.frame.height + 5
        case .pad:
            hintLabelFrame.origin.y = myTableView.frame.origin.y + myTableView.frame.height + 10
        default:
            break
        }
        
        hintLabel.frame = hintLabelFrame
        
        
        var confrBtnFrame = confirmButton.frame
        var cancelBtnFrame = cancelButton.frame
        
        switch UIDevice.current.userInterfaceIdiom {
        case .phone:
            confrBtnFrame.origin.y = (myTableView.frame.size.height + myTableView.frame.origin.y) + 10 + hintLabelFrame.height
            cancelBtnFrame.origin.y = (myTableView.frame.size.height + myTableView.frame.origin.y) + 10 + hintLabelFrame.height
        case .pad:
            confrBtnFrame.origin.y = (myTableView.frame.size.height + myTableView.frame.origin.y) + 50 + hintLabelFrame.height
            cancelBtnFrame.origin.y = (myTableView.frame.size.height + myTableView.frame.origin.y) + 50 + hintLabelFrame.height
        default:
            break
        }
        
        confirmButton.frame = confrBtnFrame
        cancelButton.frame = cancelBtnFrame
        
        
        switch UIDevice.current.userInterfaceIdiom {
        case .phone:
            var size  = myScrollView.contentSize
            size.height = confirmButton.frame.origin.y + confirmButton.frame.size.height + 20
            myScrollView.contentSize = size
        case .pad:
            var size  = myScrollView.contentSize
            size.height = confirmButton.frame.origin.y + confirmButton.frame.size.height + 40
            myScrollView.contentSize = size
        default:
            break
        }
        
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        guard let text = amountTextField.text else { return true }
        let newLength = text.count + string.count - range.length
        return newLength <= 7
    }
    
    // MARK: UITableViewDataSource
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        if(!(responseDict.isEmpty)){
            if(product?.fID == Constants.FID.CASH_WITHDRAWAL_LEG1_FID || product?.fID == Constants.FID.ZONG_MINILOAD_FID){
                return 2
            }
            else if(product?.fID == Constants.FID.CASH_WITHDRAWAL){
                return 3
            }else if(product?.fID == Constants.FID.TRANSFER_HRA_TO_WALLET){
                return 3
            }else if(product?.fID == Constants.FID.RETAILPAYMENT_FID || product?.fID == Constants.FID.LOAN_PAYMENTS_FID){
                return 5
            }else if(product?.fID == Constants.FID.ACCTOACC_FID){
                return 5
            }else if(product?.fID == Constants.FID.BBToIBFT_FID){
                return 7
            }else if(product?.fID == Constants.FID.BBTOCORE_FID || product?.fID == Constants.FID.COLLECTION_PAYMENT || product?.fID == Constants.FID.CHALLAN_NUMBER) {
                return 8
            }else if(product?.fID == Constants.FID.TRANSFER_IN_FID || product?.fID == Constants.FID.TRANSFER_OUT_FID || product?.fID == Constants.FID.GENRATE_ATM_PIN || product?.fID == Constants.FID.CHANGE_ATM_PIN || product?.fID == Constants.FID.ATM_CARD_BLOCK || product?.fID == Constants.FID.ATM_CARD_ACTIVATION || product?.fID == Constants.FID.ACCTOCASH_FID){
                return 5
            }
            else if product?.fID == Constants.FID.BOOKME_HOTEL || product?.fID == Constants.FID.BOOKME_BUSES || product?.fID == Constants.FID.BOOKME_HOTEL || product?.fID == Constants.FID.BOOKME_AIR {
                return 12
            }
            else if product?.fID == Constants.FID.BOOKME_EVENTS || product?.fID == Constants.FID.BOOKME_CINEMA {
                return 13
            }
            else{
                //Bill Payment
                if(product?.inrequired == "0") {
                    return 3
                } else if product?.denomFlag == "1" {
                    return 3
                }
                else {
                    return 8
                }
            }
        }else{
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cellIdentifier = "Cell"
        let cell: UITableViewCell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier)!
        if(product?.fID == Constants.FID.CASH_WITHDRAWAL_LEG1_FID) {
            
            if((indexPath as NSIndexPath).row == 0){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Transaction ID"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["TRXID"]
                }
            }else if((indexPath as NSIndexPath).row == 1){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Date & Time"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["DATEF"]
                }
            }
        }
        else if(product?.fID == Constants.FID.ZONG_MINILOAD_FID){
            
            if((indexPath as NSIndexPath).row == 0){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Mobile No."
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["TMOB"]
                }
            }else if((indexPath as NSIndexPath).row == 1){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Topup Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TAMTF"]!)"
                }
            }
        }
        else if(product?.fID == Constants.FID.CASH_WITHDRAWAL){
            if((indexPath as NSIndexPath).row == 0) {
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TXAMF"]!)"
                }
            }else if((indexPath as NSIndexPath).row == 1){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Charges"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TPAMF"]!)"
                }
            }else if((indexPath as NSIndexPath).row == 2){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Total Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TAMTF"]!)"
                }
            }
        }
        else if (product?.fID == Constants.FID.TRANSFER_HRA_TO_WALLET) {
            if((indexPath as NSIndexPath).row == 0) {
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TXAMF"]!)"
                }
            }else if((indexPath as NSIndexPath).row == 1){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Charges"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TPAMF"]!)"
                }
            }else if((indexPath as NSIndexPath).row == 2){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Total Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TAMTF"]!)"
                }
            }
            
        }
        else if(product?.fID == Constants.FID.ACCTOACC_FID){
            if((indexPath as NSIndexPath).row == 0){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Receiver Mobile No."
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["RCMOB"]
                }
            }else if((indexPath as NSIndexPath).row == 1){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Receiver A/C Title"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["RECACCTITLE"]
                    self.receiverName = responseDict["RECACCTITLE"] ?? ""
                }
            }else if((indexPath as NSIndexPath).row == 2){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TXAMF"]!)"
                }
            }else if((indexPath as NSIndexPath).row == 3){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Charges"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TPAMF"]!)"
                }
            }else if((indexPath as NSIndexPath).row == 4){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Total Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TAMTF"]!)"
                }
            }
            
        }
        else if(product?.fID == Constants.FID.RETAILPAYMENT_FID){
            if((indexPath as NSIndexPath).row == 0){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Merchant"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["RANAME"]
                }
            }else if((indexPath as NSIndexPath).row == 1){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Merchant Mobile No."
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["AMOB"]
                }
            }else if((indexPath as NSIndexPath).row == 2){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TXAMF"]!)"
                }
            }else if((indexPath as NSIndexPath).row == 3){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Charges"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TPAMF"]!)"
                }
            }else if((indexPath as NSIndexPath).row == 4){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Total Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TAMTF"]!)"
                }
            }
            
        }
        else if(product?.fID == Constants.FID.BBTOCORE_FID){
            //print(responseDict)
            if((indexPath as NSIndexPath).row == 0){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Receiver Mobile No."
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["RCMOB"]
                }
            }else if((indexPath as NSIndexPath).row == 1){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Receiver A/C No."
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["COREACID"]
                }
            }else if((indexPath as NSIndexPath).row == 2){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Receiver A/C Title"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["COREACTL"]
                }
            }else if((indexPath as NSIndexPath).row == 3){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TXAMF"]!)"
                }
            }else if((indexPath as NSIndexPath).row == 4){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Charges"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TPAMF"]!)"
                }
            }else if((indexPath as NSIndexPath).row == 5){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Total Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TAMTF"]!)"
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
                    valueLabel.text = responseDict["RCMOB"]
                }
            }else if((indexPath as NSIndexPath).row == 2){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Receiver A/C No."
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["COREACID"]
                }
            }else if((indexPath as NSIndexPath).row == 3){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Receiver A/C Title"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["COREACTL"]
                }
            }else if((indexPath as NSIndexPath).row == 4){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TXAMF"]!)"
                }
            }else if((indexPath as NSIndexPath).row == 5){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Charges"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TPAMF"]!)"
                }
            }else if((indexPath as NSIndexPath).row == 6){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Total Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TAMTF"]!)"
                }
            }
        }
        else if(product?.fID == Constants.FID.ACCTOCASH_FID) {
            if((indexPath as NSIndexPath).row == 0){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Receiver Mobile No."
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["RCMOB"]
                }
            }else if((indexPath as NSIndexPath).row == 1){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Receiver CNIC"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["RWCNIC"]
                }
            }else if((indexPath as NSIndexPath).row == 2){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TXAMF"]!)"
                }
            }else if((indexPath as NSIndexPath).row == 3){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Charges"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TPAMF"]!)"
                }
            }else if((indexPath as NSIndexPath).row == 4){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Total Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TAMTF"]!)"
                }
            }
            
        }
        else if(product?.fID == Constants.FID.TRANSFER_IN_FID){
            if((indexPath as NSIndexPath).row == 0){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "From Account"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["COREACID"]
                }
            }else if((indexPath as NSIndexPath).row == 1){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "To Account"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["BBACID"]
                }
            }else if((indexPath as NSIndexPath).row == 2){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TXAMF"]!)"
                }
            }else if((indexPath as NSIndexPath).row == 3){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Charges"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TPAMF"]!)"
                }
            }else if((indexPath as NSIndexPath).row == 4){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Total Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TAMTF"]!)"
                }
            }
        }
        else if(product?.fID == Constants.FID.TRANSFER_OUT_FID){
            if((indexPath as NSIndexPath).row == 0){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "From Account"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["BBACID"]
                }
            }else if((indexPath as NSIndexPath).row == 1){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "To Account"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["COREACID"]
                }
            }else if((indexPath as NSIndexPath).row == 2){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TXAMF"]!)"
                }
            }else if((indexPath as NSIndexPath).row == 3){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Charges"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TPAMF"]!)"
                }
            }else if((indexPath as NSIndexPath).row == 4){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Total Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TAMTF"]!)"
                }
            }
        }
        else if(product?.fID == Constants.FID.CHANGE_ATM_PIN || product?.fID == Constants.FID.GENRATE_ATM_PIN || product?.fID == Constants.FID.ATM_CARD_BLOCK || product?.fID == Constants.FID.ATM_CARD_ACTIVATION){
            
            if((indexPath as NSIndexPath).row == 0){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Mobile No."
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["MOBN"]
                }
            }else if((indexPath as NSIndexPath).row == 1){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "CNIC No."
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["CNIC"]
                }
            }else if((indexPath as NSIndexPath).row == 2){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Name"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["ACTITLE"]
                }
            }else if((indexPath as NSIndexPath).row == 3){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Card No."
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["CARDNO"]
                }
            }else if((indexPath as NSIndexPath).row == 4){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Card Program"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["CPNAME"]
                }
            }
        }
        else if(product?.fID == Constants.FID.COLLECTION_PAYMENT) {
            
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
                    valueLabel.text = responseDict["MOBN"]
                }
            }else if((indexPath as NSIndexPath).row == 2){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "CNIC"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["CNIC"]
                }
            }else if((indexPath as NSIndexPath).row == 3){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TXAMF"]!)"
                }
            }else if((indexPath as NSIndexPath).row == 4){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Charges"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TPAMF"]!)"
                }
            }else if((indexPath as NSIndexPath).row == 5){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Total Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TAMTF"]!)"
                }
            }
        } else if(product?.fID == Constants.FID.CHALLAN_NUMBER){
            
            if((indexPath as NSIndexPath).row == 0){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Transaction Type"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["PNAME"]
                }
            }else  if((indexPath as NSIndexPath).row == 1){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Challan No."
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["CONSUMER"]
                }
            }  else  if((indexPath as NSIndexPath).row == 2){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Mobile No."
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["CMOB"]
                }
            }
            else  if((indexPath as NSIndexPath).row == 3){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Status"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "Un-Paid"
                }
            }
            
            else if((indexPath as NSIndexPath).row == 4){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Due Date"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["DUEDATE"]
                }
            }else if((indexPath as NSIndexPath).row == 5){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["BAMTF"]!)"
                }
            }else if((indexPath as NSIndexPath).row == 6){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Charges"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TPAMF"]!)"
                }
            }else if((indexPath as NSIndexPath).row == 7){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Total Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TAMTF"]!)"
                }
            }
        } else if (product?.fID == Constants.FID.LOAN_PAYMENTS_FID) {
            if((indexPath as NSIndexPath).row == 0){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "CNIC"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["CUSTOMERCNIC"]
                }
            }else  if((indexPath as NSIndexPath).row == 1){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Mobile No."
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["CMOB"]
                }
            }  else  if((indexPath as NSIndexPath).row == 2){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TXAM"]!)"
                }
            }
            else  if((indexPath as NSIndexPath).row == 3){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Charges"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TPAMF"]!)"
                }
            }
            
            else if((indexPath as NSIndexPath).row == 4){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Total Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TAMTF"]!)"
                }
                
            }
        } else if (product?.fID == Constants.FID.BOOKME_BUSES) {
            
            if((indexPath as NSIndexPath).row == 0){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Order Ref ID"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["ORDERID"]
                }
            }else  if((indexPath as NSIndexPath).row == 1){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Mobile No."
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = Customer.sharedInstance.cMob!
                }
            }  else  if((indexPath as NSIndexPath).row == 2){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "CNIC"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = Customer.sharedInstance.cnic!
                }
            }else  if((indexPath as NSIndexPath).row == 3){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "BookMe Mobile No."
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = orderDetails["contact_no"]
                }
            }  else  if((indexPath as NSIndexPath).row == 4){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "BookMe CNIC"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = orderDetails["cnic"]
                }
            }
            else  if((indexPath as NSIndexPath).row == 5){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Service Provider"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["SPNAME"]
                }
            }
            else  if((indexPath as NSIndexPath).row == 6){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Departure"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = departureCity
                }
            }
            else  if((indexPath as NSIndexPath).row == 7){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Arrival"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = arrivalCity
                }
            }
            else  if((indexPath as NSIndexPath).row == 8){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Departure Date & Time"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = orderDetails["dep_date"]! + orderDetails["dep_time"]!
                }
            }
            else if((indexPath as NSIndexPath).row == 9){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["BAMT"]!)"
                }
                
            }
            else if((indexPath as NSIndexPath).row == 10){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Charges"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TPAM"]!)"
                }
                
            }
            
            else if((indexPath as NSIndexPath).row == 11){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Total Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TAMT"]!)"
                }
                
            }
        } else if (product?.fID == Constants.FID.BOOKME_AIR) {
            
            if((indexPath as NSIndexPath).row == 0){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Order Ref ID"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["ORDERID"]
                }
            }else  if((indexPath as NSIndexPath).row == 1){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Mobile No."
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = Customer.sharedInstance.cMob!
                }
            }  else  if((indexPath as NSIndexPath).row == 2){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "CNIC"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = Customer.sharedInstance.cnic!
                }
            }else  if((indexPath as NSIndexPath).row == 3){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "BookMe Mobile No."
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = orderDetails["phone_number"]
                }
            }  else  if((indexPath as NSIndexPath).row == 4){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "BookMe CNIC"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = orderDetails["cnic"]
                }
            }
            else  if((indexPath as NSIndexPath).row == 5){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Service Provider"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["SPNAME"]
                }
            }
            else  if((indexPath as NSIndexPath).row == 6){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Departure"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = departureCity
                }
            }
            else  if((indexPath as NSIndexPath).row == 7){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Arrival"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = arrivalCity
                }
            }
            else  if((indexPath as NSIndexPath).row == 8){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Departure Date & Time"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = orderDetails["initial_departure_datetime"]!
                }
            }
            
            else if((indexPath as NSIndexPath).row == 9){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["BAMT"]!)"
                }
                
            }
            else if((indexPath as NSIndexPath).row == 10){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Charges"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TPAM"]!)"
                }
                
            }
            else if((indexPath as NSIndexPath).row == 11){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Total Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TAMT"]!)"
                }
                
            }
            
        } else if (product?.fID == Constants.FID.BOOKME_HOTEL) {
            if((indexPath as NSIndexPath).row == 0){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Order Ref ID"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["ORDERID"]
                }
            }else  if((indexPath as NSIndexPath).row == 1){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Mobile No."
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = Customer.sharedInstance.cMob!
                }
            }  else  if((indexPath as NSIndexPath).row == 2){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "CNIC"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = Customer.sharedInstance.cnic!
                }
            }else  if((indexPath as NSIndexPath).row == 3){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "BookMe Mobile No."
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = orderDetails["phone_number"]
                }
            }else  if((indexPath as NSIndexPath).row == 4){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "BookMe CNIC"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = orderDetails["cnic"]
                }
            }else  if((indexPath as NSIndexPath).row == 5){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Service Provider"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["SPNAME"]
                }
            }
            else  if((indexPath as NSIndexPath).row == 6){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "CheckIn Date & Time"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = orderDetails["checkin_datetime"]!
                }
            }
            else  if((indexPath as NSIndexPath).row == 7){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "CheckOut Date & Time"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = orderDetails["checkout_datetime"]!
                }
            }
            
            else  if((indexPath as NSIndexPath).row == 8){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Hotel Name"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = orderDetails["hotel_name"]!
                }
            }
            else if((indexPath as NSIndexPath).row == 9){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["BAMT"]!)"
                }
                
            }
            else if((indexPath as NSIndexPath).row == 10){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Charges"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TPAM"]!)"
                }
                
            }
            else if((indexPath as NSIndexPath).row == 11) {
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Total Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TAMT"]!)"
                }
                
            }
        } else if (product?.fID == Constants.FID.BOOKME_EVENTS) {
            
            if((indexPath as NSIndexPath).row == 0) {
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Order Ref ID"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["ORDERID"]
                }
            }else  if((indexPath as NSIndexPath).row == 1){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Mobile No."
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = Customer.sharedInstance.cMob!
                }
            }  else  if((indexPath as NSIndexPath).row == 2){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "CNIC"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = Customer.sharedInstance.cnic!
                }
            }else  if((indexPath as NSIndexPath).row == 3){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "BookMe Mobile No."
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = orderDetails["contact_no"]
                }
            }  else  if((indexPath as NSIndexPath).row == 4){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "BookMe CNIC"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = orderDetails["cnic"]
                }
            }
            else  if((indexPath as NSIndexPath).row == 5){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Service Provider"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["SPNAME"]
                }
            }
            else  if((indexPath as NSIndexPath).row == 6){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Event Name"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = orderDetails["name"]!
                }
            }
            else  if((indexPath as NSIndexPath).row == 7){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Event Date"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = orderDetails["event_date"]!
                }
            }
            else  if((indexPath as NSIndexPath).row == 8){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Event Duration"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = self.departureCity
                }
            }
            else  if((indexPath as NSIndexPath).row == 9){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Event Venue"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = self.arrivalCity
                }
            }
            
            else if((indexPath as NSIndexPath).row == 10){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["BAMT"]!)"
                }
                
            }
            else if((indexPath as NSIndexPath).row == 11){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Charges"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TPAM"]!)"
                }
                
            }
            else if((indexPath as NSIndexPath).row == 12){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Total Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TAMT"]!)"
                }
                
            }
        }else if (product?.fID == Constants.FID.BOOKME_CINEMA) {
            
            if((indexPath as NSIndexPath).row == 0) {
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Order Ref ID"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["ORDERID"]
                }
            }else  if((indexPath as NSIndexPath).row == 1){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Mobile No."
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = Customer.sharedInstance.cMob!
                }
            }  else  if((indexPath as NSIndexPath).row == 2){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "CNIC"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = Customer.sharedInstance.cnic!
                }
            }else  if((indexPath as NSIndexPath).row == 3){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "BookMe Mobile No."
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = orderDetails["contact_no"]
                }
            }  else  if((indexPath as NSIndexPath).row == 4){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "BookMe CNIC"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = orderDetails["cnic"]
                }
            }
            else  if((indexPath as NSIndexPath).row == 5){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Service Provider"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = responseDict["SPNAME"]
                }
            }
            else  if((indexPath as NSIndexPath).row == 6){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Show Time"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = orderDetails["show_time"]!
                }
            }
            else  if((indexPath as NSIndexPath).row == 7){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Movie Title"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = departureCity
                }
            }
            else  if((indexPath as NSIndexPath).row == 8){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Cinema Name"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = arrivalCity
                }
            }
            else  if((indexPath as NSIndexPath).row == 9){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Seat Numbers"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = orderDetails["seat_numbers"]!
                }
            }
            
            else if((indexPath as NSIndexPath).row == 10){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["BAMT"]!)"
                }
                
            }
            else if((indexPath as NSIndexPath).row == 11){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Charges"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TPAM"]!)"
                }
                
            }
            else if((indexPath as NSIndexPath).row == 12){
                if let titleLabel = cell.viewWithTag(1) as? UILabel {
                    titleLabel.text = "Total Amount"
                }
                if let valueLabel = cell.viewWithTag(2) as? UILabel {
                    valueLabel.text = "PKR \(responseDict["TAMT"]!)"
                }
                
            }
        }
        else {
            if(product?.inrequired == "0" || product?.denomFlag == "1"){
                
                if((indexPath as NSIndexPath).row == 0) {
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Mobile No."
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = responseDict["CONSUMER"]
                    }
                } else if((indexPath as NSIndexPath).row == 1){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Topup Amount"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(responseDict["BAMTF"]!)"
                    }
                } else if((indexPath as NSIndexPath).row == 2) {
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Charges"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(responseDict["TPAM"] ?? "0.0")"
                    }
                }
            }else{
                if((indexPath as NSIndexPath).row == 0){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Billing Company"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = responseDict["PNAME"]
                    }
                }else if((indexPath as NSIndexPath).row == 1){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = product?.label!
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = responseDict["CONSUMER"]
                    }
                }else if((indexPath as NSIndexPath).row == 2){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Due Date"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = responseDict["DUEDATE"]
                    }

                }else if((indexPath as NSIndexPath).row == 3) {
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Charges"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(responseDict["TPAM"] ?? "0.0")"
                    }
                }
                else if((indexPath as NSIndexPath).row == 4){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Bill Amount"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(responseDict["BAMTF"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 5){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Late Bill Amount"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = "PKR \(responseDict["LBAMTF"]!)"
                    }
                }else if((indexPath as NSIndexPath).row == 6){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Bill Paid Status"
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        if(responseDict["BPAID"] == "0"){
                            valueLabel.text = "Unpaid"
                        }else if(responseDict["BPAID"] == "1"){
                            valueLabel.text = "Paid"
                        }
                    }
                }else if((indexPath as NSIndexPath).row == 7){
                    if let titleLabel = cell.viewWithTag(1) as? UILabel {
                        titleLabel.text = "Customer Mobile No."
                    }
                    if let valueLabel = cell.viewWithTag(2) as? UILabel {
                        valueLabel.text = responseDict["CMOB"]
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
    
    @IBAction func confirmButtonPressed(_ sender: UIButton) {
        
        if(product?.inrequired == "1" && product?.pprequired == "1"){
            
            self.dismissKeyboard()
            
            var errorMessage: String?
            
            let amtText = amountTextField.text
            
            var minAmount = 0
            var maxAmount = 0
            
            if(product?.minamt != nil && product?.minamt != "" && product?.maxamt != nil && product?.maxamt != ""){
                
                minAmount = ((NumberFormatter().number(from: (product?.minamt)!))?.intValue)!
                maxAmount = ((NumberFormatter().number(from: (product?.maxamt)!))?.intValue)!
            }else{
                minAmount = Constants.Validation.TextField.AMOUNT_MIN
                maxAmount = Constants.Validation.TextField.AMOUNT_MAX
            }
            
            
            if(amtText != nil && amtText != ""){
                
                if(Int((amountTextField?.text)!)! < minAmount || Int((amountTextField?.text)!)! > maxAmount){
                    errorMessage = "Invalid amount entered."
                }
                
            }
            
            
            if(errorMessage != nil){
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: errorMessage!, actionType: "serverFailure", isCancelBtnHidden: true)
            }else{
                
                let popupView = FinancialPinPopup(nibName: "FinancialPinPopup", bundle: nil)
                popupView.delegate = self
                popupView.responseDict = responseDict
                if(bankID != nil){
                    popupView.BAMID = bankID!
                }
                popupView.product = product!
                if(amountTextField.text != nil){
                    popupView.partialBillAmount = amountTextField.text
                }
                if(bankName != nil){
                    popupView.bankName = bankName!
                }
                popupView.receiverName = self.receiverName
                popupView.isMpinSetLater = self.isMpinSetLater
                popupView.modalPresentationStyle = .overCurrentContext
                self.present(popupView, animated: false, completion: nil)
            }
            
        }else{
            
            self.dismissKeyboard()
            
            let popupView = FinancialPinPopup(nibName: "FinancialPinPopup", bundle: nil)
            popupView.delegate = self
            popupView.responseDict = responseDict
            if(bankID != nil) {
                popupView.BAMID = bankID!
            }
            popupView.product = product!
            
            if(amountTextField.text != nil) {
                popupView.partialBillAmount = amountTextField.text
            }
            
            if(bankName != nil){
                popupView.bankName = bankName!
            }
            
            if selectedPrpTranxCode != nil {
                popupView.tranxCode = self.selectedPrpTranxCode
            }
            
            if product?.fID == Constants.FID.BOOKME_AIR {
                popupView.bFare = orderDetails["approx_base_price"] ?? "0.0"
                popupView.approxAmt = orderDetails["approx_total_price"] ?? "0.0"
                popupView.discountAmt = orderDetails["ep_discount"] ?? "0.0"
            }
            if product?.fID == Constants.FID.BOOKME_AIR || product?.fID == Constants.FID.BOOKME_HOTEL {
                popupView.bNumber = orderDetails["phone_number"] ?? ""
            } else {
                popupView.bNumber = orderDetails["contact_no"] ?? ""
            }
            popupView.bCnic = orderDetails["cnic"] ?? ""
            popupView.bName = orderDetails["name"] ?? ""
            popupView.bEmail = orderDetails["email"] ?? ""
            popupView.receiverName = self.receiverName
            popupView.isMpinSetLater = self.isMpinSetLater
            popupView.modalPresentationStyle = .overCurrentContext
            self.present(popupView, animated: false, completion: nil)
        }
    }
    
    @IBAction func cancelButtonPressed(_ sender: UIButton) {
        
        let popupView = AlertPopup(nibName: "AlertPopup", bundle: nil)
        popupView.headerLabelText = Constants.Message.ALERT_NOTIFICATION_TITLE
        popupView.isYesNoButtons = true
        popupView.msgLabelText = "Are you sure to cancel?"
        popupView.cancelButtonHidden = false
        popupView.isMpinSetLater = self.isMpinSetLater
        popupView.modalPresentationStyle = .overCurrentContext
        self.present(popupView, animated: false, completion: nil)
    }
    
    func okPressedFP() {
        if(product?.fID == Constants.FID.CHANGE_ATM_PIN || product?.fID == Constants.FID.GENRATE_ATM_PIN ){
            let viewController = UIStoryboard(name: "CardServices", bundle: nil).instantiateViewController(withIdentifier: "ChangeATMPINVC") as! ChangeATMPINVC
            viewController.product = product
            self.pushViewController(viewController)
        }else if(product?.fID == Constants.FID.ATM_CARD_BLOCK || product?.fID == Constants.FID.ATM_CARD_ACTIVATION){
            cardServicesFinalPostRequest()
        }else if(product?.fID == Constants.FID.COLLECTION_PAYMENT || product?.fID == Constants.FID.CHALLAN_NUMBER){
            collectionPaymentConfirmation()
        }
    }
    
    func canclePressedFP() {
        //print("cancel")
    }
    
    func collectionPaymentConfirmation(){
        
        var response = (XMLError(), XMLMessage(), [String:String]())
        self.showLoadingView()
        var fileName: String = ""
        if(product?.fID == Constants.FID.COLLECTION_PAYMENT ){
            fileName = "Command-210- Collection Payment Checkout"
        } else if ( product?.fID == Constants.FID.CHALLAN_NUMBER){
            fileName = "Command 210 Payment Response"
        }
        
        var tamt = ""
        //print(amountTextField.text)
        if(amountTextField.text != nil && amountTextField.text != ""){
            tamt = amountTextField.text!
        }else{
            tamt = responseDict["BAMT"]!
        }
        
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                    xmlPath = Bundle.main.path(forResource: fileName, ofType: "xml"),
                  let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
            
            else { return }
            
            response = XMLParser.transTypeXMLParsing(data)
            //print(response)
            if(response.0.msg != nil){
                if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                }else{
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "", isCancelBtnHidden: true)
                }
            }else if(response.1.msg != nil){
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.1.msg!, actionType: "requestDenied", isCancelBtnHidden: true)
            }else{
                DispatchQueue.main.async {
                    self.dismiss(animated: false, completion: nil)
                }
                let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "NotificationVC") as! NotificationVC
                //print(response)
                viewController.checkOutResponse = response
                viewController.product = self.product
                viewController.totalAmount = tamt
                viewController.product = self.product
                viewController.isMpinSetLater = self.isMpinSetLater
                self.pushViewController(viewController)
            }
            self.hideLoadingView()
        }else{
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let webApi : TransactionAPI = TransactionAPI()
            
            if(product?.fID == Constants.FID.COLLECTION_PAYMENT ){
                webApi.CollectionPaymentCheckoutPostRequest(
                    Constants.CommandId.COLLECTION_PAYMENT_CHECKOUT,
                    reqTime: currentTime,
                    DTID: Constants.AppConfig.DTID_KEY,
                    PID: (product?.id!)!,
                    CNSMRNO: CNSMRNO!,
                    TXAM: tamt,
                    TPAM: responseDict["TPAM"]!,
                    TAMT: responseDict["TAMT"]!,
                    onSuccess:{(data) -> () in
                        //print(data)
                        response = XMLParser.cashWithdrawalCheckoutXMLParsing(data)
                        //print(self.response)
                        if(response.0.msg != nil){
                            if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                            }else{
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            }
                        }else if(response.1.msg != nil){
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.1.msg!, actionType:"serverFailure", isCancelBtnHidden: true)
                        }else{
                            DispatchQueue.main.async {
                                self.dismiss(animated: false, completion: nil)
                            }
                            
                            let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "NotificationVC") as! NotificationVC
                            viewController.checkOutResponse = response
                            viewController.product = self.product
                            
                            var tamt1 = ""
                            if(self.amountTextField.text != nil && self.amountTextField.text != ""){
                                tamt1 = self.amountTextField.text!
                            }else{
                                tamt1 = self.responseDict["TAMT"]!
                            }
                            
                            viewController.totalAmount = tamt1
                            viewController.isMpinSetLater = self.isMpinSetLater
                            self.pushViewController(viewController)
                            if(response.2["BALF"] != nil){
                                self.userDefault.set(response.2["BALF"], forKey: "BAL")
                            }
                        }
                        
                        self.hideLoadingView()
                    },
                    onFailure: {(reason) ->() in
                        //print("Failure")
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                        DispatchQueue.main.async {
                            self.dismiss(animated: false, completion: nil)
                        }
                        self.hideLoadingView()
                    })
            } else if(product?.fID == Constants.FID.CHALLAN_NUMBER) {
                
                webApi.ChallanPaymentCheckoutPostRequest(
                    Constants.CommandId.COLLECTION_PAYMENT_CHECKOUT,
                    reqTime: currentTime,
                    DTID: Constants.AppConfig.DTID_KEY,
                    PIN: MPIN!,
                    PID: (product?.id!)!,
                    ENCT: "1",
                    CMOB: responseDict["CMOB"]!,
                    CSCD: responseDict["CONSUMER"]!,
                    BAMT: responseDict["BAMT"]!,
                    TPAM: responseDict["TPAM"]!,
                    TAMT: responseDict["TAMT"]!,
                    onSuccess:{(data) -> () in
                        //print(data)
                        response = XMLParser.cashWithdrawalCheckoutXMLParsing(data)
                        //print(self.response)
                        if(response.0.msg != nil){
                            if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                            }else{
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            }
                        }else if(response.1.msg != nil){
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.1.msg!, actionType:"serverFailure", isCancelBtnHidden: true)
                        }else{
                            DispatchQueue.main.async {
                                self.dismiss(animated: false, completion: nil)
                            }
                            let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "NotificationVC") as! NotificationVC
                            viewController.checkOutResponse = response
                            viewController.product = self.product
                            
                            var tamt1 = ""
                            if(self.amountTextField.text != nil && self.amountTextField.text != ""){
                                tamt1 = self.amountTextField.text!
                            }else{
                                tamt1 = self.responseDict["TAMT"]!
                            }
                            viewController.totalAmount = tamt1
                            viewController.mobileNo = self.responseDict["CMOB"]!
                            viewController.isMpinSetLater = self.isMpinSetLater
                            self.pushViewController(viewController)
                            if(response.2["BALF"] != nil){
                                self.userDefault.set(response.2["BALF"], forKey: "BAL")
                            }
                        }
                        
                        self.hideLoadingView()
                    },
                    onFailure: {(reason) ->() in
                        //print("Failure")
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                        DispatchQueue.main.async {
                            self.dismiss(animated: false, completion: nil)
                        }
                        self.hideLoadingView()
                    })
            }
        }
    }
    
    func cardServicesPinRequestInfo() {
        
        var response = (XMLError(), XMLMessage(), [String:String]())
        self.showLoadingView()
        
        var fileName: String?
        if(product?.fID == Constants.FID.GENRATE_ATM_PIN || product?.fID == Constants.FID.CHANGE_ATM_PIN){
            fileName = "Command-157-ATM PIN Change & Generation Info"
        }else if(product?.fID == Constants.FID.ATM_CARD_BLOCK){
            fileName = "Command-155-Debit Card Block Info"
        }else if(product?.fID == Constants.FID.ATM_CARD_ACTIVATION){
            fileName = "Command-153-Debit Card Activation Info"
        }
        
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                    xmlPath = Bundle.main.path(forResource: fileName!, ofType: "xml"),
                  let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
            
            else { return }
            
            response = XMLParser.paramTypeXMLParsing(data)
            //print(response)
            if(response.0.msg != nil){
                if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                }else{
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "", isCancelBtnHidden: true)
                }
            }else if(response.1.msg != nil){
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.1.msg!, actionType: "requestDenied", isCancelBtnHidden: true)
            }else{
                DispatchQueue.main.async {
                    self.responseDict = response.2
                    self.myTableView.reloadData()
                    self.showSubViews()
                }
            }
            self.hideLoadingView()
            
        }else{
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            let webApi : CardServicesAPI = CardServicesAPI()
            
            if(product?.fID == Constants.FID.ATM_CARD_ACTIVATION){
                webApi.cardActivationInfoPostRequest(
                    Constants.CommandId.CARD_ACTIVATION_INFO,
                    reqTime: currentTime,
                    DTID: Constants.AppConfig.DTID_KEY,
                    CMOB: Customer.sharedInstance.cMob!,
                    OCA: "1",
                    onSuccess:{(data) -> () in
                        //print(data)
                        response = XMLParser.paramTypeXMLParsing(data)
                        //print(response)
                        if(response.0.msg != nil){
                            if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                            }else{
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "", isCancelBtnHidden: true)
                            }
                        }else if(response.1.msg != nil){
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.1.msg!, actionType: "requestDenied", isCancelBtnHidden: true)
                        }else{
                            DispatchQueue.main.async {
                                self.responseDict = response.2
                                self.myTableView.reloadData()
                                self.showSubViews()
                            }
                        }
                        self.hideLoadingView()
                    },
                    onFailure: {(reason) ->() in
                        //print("Failure")
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                        self.hideLoadingView()
                    })
            }
            else if(product?.fID == Constants.FID.ATM_CARD_BLOCK){            
                webApi.cardBlockInfoPostRequest(
                    Constants.CommandId.CARD_BLOCK_INFO,
                    reqTime: currentTime,
                    DTID: Constants.AppConfig.DTID_KEY,
                    onSuccess:{(data) -> () in
                        //print(data)
                        response = XMLParser.paramTypeXMLParsing(data)
                        //print(response)
                        if(response.0.msg != nil){
                            if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                            }else{
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "", isCancelBtnHidden: true)
                            }
                        }else if(response.1.msg != nil){
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.1.msg!, actionType: "requestDenied", isCancelBtnHidden: true)
                        }else{
                            DispatchQueue.main.async {
                                self.responseDict = response.2
                                self.myTableView.reloadData()
                                self.showSubViews()
                            }
                        }
                        self.hideLoadingView()
                    },
                    onFailure: {(reason) ->() in
                        //print("Failure")
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                        self.hideLoadingView()
                    })
                
                
            }
            else if(product?.fID == Constants.FID.GENRATE_ATM_PIN || product?.fID == Constants.FID.CHANGE_ATM_PIN){
                
                var APING: String?
                if(product?.fID == Constants.FID.GENRATE_ATM_PIN){
                    APING = "true"
                }else{
                    APING = "false"
                }
                
                webApi.genrateATMPINInfoPostRequest(
                    Constants.CommandId.CARD_ATM_PIN_GEN_CHANAGE_INFO,
                    reqTime: currentTime,
                    aPING: APING!,
                    DTID: Constants.AppConfig.DTID_KEY,
                    onSuccess:{(data) -> () in
                        
                        response = XMLParser.paramTypeXMLParsing(data)
                        //print(response)
                        if(response.0.msg != nil){
                            if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                            }else{
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "", isCancelBtnHidden: true)
                            }
                        }else if(response.1.msg != nil){
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.1.msg!, actionType: "requestDenied", isCancelBtnHidden: true)
                        }else{
                            DispatchQueue.main.async {
                                self.responseDict = response.2
                                self.myTableView.reloadData()
                                self.showSubViews()
                            }
                        }
                        self.hideLoadingView()
                    },
                    onFailure: {(reason) ->() in
                        //print("Failure")
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                        self.hideLoadingView()
                    })
            }
        }
    }
    
    func cardServicesFinalPostRequest() {
        
        self.showLoadingView()
        
        var response = (XMLError(), XMLMessage(), [String:String]())
        
        if(Constants.AppConfig.IS_MOCK == 1){
            var xmlFileName: String?
            if(product?.fID == Constants.FID.ATM_CARD_BLOCK){
                
                xmlFileName = "Command-156-Debit Card Block"
                guard let
                        xmlPath = Bundle.main.path(forResource: xmlFileName, ofType: "xml"),
                      let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                
                else { return }
                //let newStr = String(data: data, encoding: NSUTF8StringEncoding)
                //print(newStr)
                response = XMLParser.paramTypeXMLParsing(data)
                if(response.0.msg != nil){
                    if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                    }else{
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "requestDenied", isCancelBtnHidden: true)
                    }
                }else if(response.1.msg != nil){
                    
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE_CARD_BLOCK_SUCCESS, msgLabelText: response.1.msg!, actionType: "", isCancelBtnHidden: true)
                    
                }
                self.hideLoadingView()
                
            }
            else if(product?.fID == Constants.FID.ATM_CARD_ACTIVATION){
                xmlFileName = "Command-154-Debit Card Activation"
                guard let
                        xmlPath = Bundle.main.path(forResource: xmlFileName, ofType: "xml"),
                      let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                
                else { return }
                //let newStr = String(data: data, encoding: NSUTF8StringEncoding)
                //print(newStr)
                response = XMLParser.paramTypeXMLParsing(data)
                
                if(response.0.msg != nil){
                    if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                    }else{
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                    }
                }else if(response.1.msg != nil){
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                }else{
                    let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "NotificationVC") as! NotificationVC
                    //print(response)
                    viewController.checkOutResponse = response
                    viewController.product = product
                    viewController.isMpinSetLater = self.isMpinSetLater
                    self.pushViewController(viewController)
                }
                self.hideLoadingView()
            }
            
        }else{
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            let webApi : CardServicesAPI = CardServicesAPI()
            
            if(product?.fID == Constants.FID.ATM_CARD_ACTIVATION){
                webApi.cardActivationFinalPostRequest(
                    Constants.CommandId.CARD_ACTIVATION_CHECKOUT,
                    reqTime: currentTime,
                    DTID: Constants.AppConfig.DTID_KEY,
                    ENCT: Constants.AppConfig.ENCT_KEY,
                    CMOB: Customer.sharedInstance.cMob!,
                    TRXID: "",// responseDict["TRXID"]!,
                    OCA: "1",
                    onSuccess:{(data) -> () in
                        //print(data)
                        response = XMLParser.paramTypeXMLParsing(data)
                        //print(self.response)
                        if(response.0.msg != nil){
                            if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                            }else{
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            }
                        }else if(response.1.msg != nil){
                            
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            
                        }else{
                            let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "NotificationVC") as! NotificationVC
                            viewController.checkOutResponse = response
                            viewController.product = self.product
                            viewController.totalAmount = response.2["TAMTF"]
                            viewController.isMpinSetLater = self.isMpinSetLater
                            self.pushViewController(viewController)
                        }
                        self.hideLoadingView()
                    },
                    onFailure: {(reason) ->() in
                        //print("Failure")
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                        self.hideLoadingView()
                    })
            }
            else if(product?.fID == Constants.FID.ATM_CARD_BLOCK){
                webApi.cardBlockFinalPostRequest(
                    Constants.CommandId.CARD_BLOCK_CHECKOUT,
                    reqTime: currentTime,
                    DTID: Constants.AppConfig.DTID_KEY,
                    onSuccess:{(data) -> () in
                        
                        response = XMLParser.paramTypeXMLParsing(data)
                        if(response.0.msg != nil){
                            if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                            }else{
                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "requestDenied", isCancelBtnHidden: true)
                            }
                        }else if(response.1.msg != nil){
                            
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE_CARD_BLOCK_SUCCESS, msgLabelText: response.1.msg!, actionType: "", isCancelBtnHidden: true)
                            
                        }
                        self.hideLoadingView()
                        
                    },
                    onFailure: {(reason) ->() in
                        //print("Failure")
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                        self.hideLoadingView()
                    })
            }
        }
    }
    func okPressedChallanNo(EncMpin: String) {
        
        MPIN = EncMpin
        if(product?.fID == Constants.FID.CHALLAN_NUMBER){
            collectionPaymentConfirmation()
        }
    }
    
}


