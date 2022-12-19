//
//  HRARegistrationVC.swift
//  JSBL-BB
//
//  Created by Uzair on 9/21/20.
//  Copyright © 2020 Inov8. All rights reserved.
//

import UIKit
import ActionSheetPicker_3_0


class HRARegistrationVC: BaseViewController, UITextFieldDelegate, UITableViewDataSource, UITableViewDelegate {
    
    
    @IBOutlet weak var cnicTextFieldFirst: FPMBottomBorderTextField!
    @IBOutlet weak var cnicTextFieldSecond: FPMBottomBorderTextField!
    @IBOutlet weak var cnicTextFieldThird: FPMBottomBorderTextField!
    @IBOutlet weak var lblScreenTitle: UILabel!
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var textFieldSourceOfIncome: UITextField!
    @IBOutlet weak var textFieldOccupation: UITextField!
    @IBOutlet weak var stackView: UIStackView!
    @IBOutlet weak var tableViewHC: NSLayoutConstraint!
    @IBOutlet weak var btnAccountPurpose: UIButton!
    @IBOutlet weak var btnNext: UIButton!
    @IBOutlet weak var btnCancel2: UIButton!
    
    @IBOutlet weak var btnCancel: UIButton!
    @IBOutlet weak var btnSearch: UIButton!
    
    let arrPurposeAccount = ["Personal", "Buisness"]
    let arrTitles = ["Name", "Father/Spouse Name", "Date of Birth"]
    let arrValues = ["CNAME", "FATHER_NAME", "CDOB"]
    var customerCNIC = ""
    var product: Product?
    var screenTitleText: String?
    var response = (XMLError(), XMLMessage(), [String:String]())
    var currentSelectedIndex = 0
    var selectedPurposeAccount = ""
    
    override func viewDidLayoutSubviews() {
        adjustScrollViewContent()
    }
    
    func adjustScrollViewContent() {
        tableViewHC.constant = tableView.contentSize.height
        var size: CGSize = scrollView.contentSize
        size.height = tableView.contentSize.height + btnNext.frame.size.height + 420
        scrollView.contentSize = size
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: false)
        self.bottomBarView.isHidden = true
    
        lblScreenTitle.text = screenTitleText
        setView()
        
        cnicTextFieldFirst.isEnabled = true
        cnicTextFieldSecond.isEnabled = false
        cnicTextFieldThird.isEnabled = false
        
        // Do any additional setup after loading the view.
        
        
        cnicTextFieldFirst.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        cnicTextFieldSecond.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        cnicTextFieldThird.addTarget(self, action: #selector(editingChanged), for: .editingChanged)
        
 
        textFieldSourceOfIncome.layer.cornerRadius = 2
        textFieldOccupation.layer.cornerRadius = 2
        
        textFieldSourceOfIncome.layer.borderWidth = 0.7
        textFieldOccupation.layer.borderWidth = 0.7
        btnAccountPurpose.layer.borderWidth = 0.7
        
        textFieldSourceOfIncome.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        textFieldOccupation.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        btnAccountPurpose.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor


        tableView.delegate = self
        tableView.dataSource = self
        
        // This is for rounded corners

        self.tableView.layer.shadowColor = UIColor.lightGray.cgColor
        self.tableView.layer.shadowOffset = CGSize(width: 1, height: 1)
        self.tableView.layer.shadowRadius = 3
        self.tableView.layer.shadowOpacity = 0.5
        self.tableView.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        self.tableView.layer.borderWidth = 0.7
        self.tableView.layer.cornerRadius = 10
        
        self.tableView.clipsToBounds = true
        
        
        cnicTextFieldFirst.delegate = self
        cnicTextFieldSecond.delegate = self
        cnicTextFieldThird.delegate = self
        textFieldOccupation.delegate = self
        textFieldSourceOfIncome.delegate = self
        
        
    }
    
    @objc func editingChanged(_ textField: UITextField) {
        
        let text = textField.text
        
        if textField.tag == 1 {
            if text?.utf16.count == 5 {
                cnicTextFieldSecond.isEnabled = true
                cnicTextFieldSecond.becomeFirstResponder()
            }
        }
        
        if textField.tag == 2 {
            if text?.utf16.count == 7 {
                cnicTextFieldThird.isEnabled = true
                cnicTextFieldThird.becomeFirstResponder()
            }
        }
        if textField.tag == 3 {
            if text?.utf16.count == 1 {
                cnicTextFieldThird.resignFirstResponder()
            }
        }
        
    }
    
    func setView(){
        
        let paddingForSec = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.cnicTextFieldFirst.frame.size.height))
        //Adding the padding to the second textField
        cnicTextFieldFirst.leftView = paddingForSec
        cnicTextFieldFirst.leftViewMode = UITextField.ViewMode.always
        
        let paddingForThird = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.cnicTextFieldSecond.frame.size.height))
        //Adding the padding to the second textField
        cnicTextFieldSecond.leftView = paddingForThird
        cnicTextFieldSecond.leftViewMode = UITextField.ViewMode.always
        
        let paddingForFourth = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.cnicTextFieldThird.frame.size.height))
        //Adding the padding to the second textField
        cnicTextFieldThird.leftView = paddingForFourth
        cnicTextFieldThird.leftViewMode = UITextField.ViewMode.always
        
        let paddingForFifth = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.textFieldSourceOfIncome.frame.size.height))
        //Adding the padding to the second textField
        textFieldSourceOfIncome.leftView = paddingForFifth
        textFieldSourceOfIncome.leftViewMode = UITextField.ViewMode.always
        
        let paddingForSixth = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.textFieldOccupation.frame.size.height))
        //Adding the padding to the second textField
        textFieldOccupation.leftView = paddingForSixth
        textFieldOccupation.leftViewMode = UITextField.ViewMode.always
        
        self.btnAccountPurpose.setTitle("Select Account Purpose", for: .normal)
        if #available(iOS 11.0, *) {
            btnAccountPurpose.contentHorizontalAlignment = .leading
        } else {
            // Fallback on earlier versions
            btnAccountPurpose.contentHorizontalAlignment = .left
        }
        btnAccountPurpose.contentEdgeInsets = UIEdgeInsets(top: 0, left: 10, bottom: 0, right: 0);
        btnAccountPurpose.layer.cornerRadius = Constants.UI.Button.CORNER_RADIUS
        btnNext.layer.cornerRadius = Constants.UI.Button.CORNER_RADIUS
        btnCancel2.layer.cornerRadius = Constants.UI.Button.CORNER_RADIUS
        
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        cnicTextFieldFirst.focusedHeight()
        cnicTextFieldSecond.focusedHeight()
        cnicTextFieldThird.focusedHeight()
        
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        
        cnicTextFieldFirst.normalHeight()
        cnicTextFieldSecond.normalHeight()
        cnicTextFieldThird.normalHeight()
        
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if (textField.tag == 1) {
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 5)
        } else if(textField.tag == 2) {
            guard let text = cnicTextFieldSecond.text else { return true }
            let newLength  = text.count + string.count - range.length
            if newLength == 0 {
                cnicTextFieldFirst.becomeFirstResponder()
                cnicTextFieldSecond.text = ""
                return false
            }
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 7)
            
        } else if(textField.tag == 3) {
            guard let text = cnicTextFieldThird.text else { return true }
            let newLength  = text.count + string.count - range.length
            if newLength == 0 {
                cnicTextFieldSecond.becomeFirstResponder()
                cnicTextFieldThird.text = ""
                return false
            }
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 1)
            
        }
        else if(textField.tag == 4) {
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_ALL, length: 25)
            
        }
        else if(textField.tag == 5) {
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_ALL, length: 25)
            
        }
            
        else {
            return true
        }
    }

    
    func numberOfSections(in tableView: UITableView) -> Int {
        return arrValues.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 50
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cellIdentifier = "Cell"
        let cell: UITableViewCell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier)!

        if(response.2["CNIC"] != nil) {
            
            if let descLabel = cell.viewWithTag(1) as? UILabel {
                descLabel.text = arrTitles[indexPath.section]
            }
            if let amountLabel = cell.viewWithTag(3) as? UILabel {
                amountLabel.text = response.2[arrValues[indexPath.section]]
            }
        }
        
        
        return cell
    }

    
    @IBAction func actSearch(_ sender: Any) {
        
        var errorMessage: String?
        
        let cnicFirstText = cnicTextFieldFirst.text
        let cnicSecText = cnicTextFieldSecond.text
        let cnicThirdText = cnicTextFieldThird.text
        

        
        if((cnicFirstText == nil && cnicSecText == nil && cnicThirdText == nil) || (cnicFirstText! == "" && cnicSecText! == "" && cnicThirdText! == "")){
            errorMessage = "CNIC field must not be empty."
        }
        else if((cnicFirstText?.count)! != 5 || (cnicSecText?.count)! != 7 || (cnicThirdText?.count)! != 1){
            errorMessage = "CNIC length should be of 13 digits."
        }
        if(errorMessage != nil) {
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: errorMessage!, actionType: "serverFailure", isCancelBtnHidden: true)
        }
        else {
            HRAAccountOpeningInfoRequest()
        }
        
    }
    
    
    @IBAction func actCancel(_ sender: Any) {
        
        self.popViewController()
        
    }
    
    @IBAction func actPurposeOfAccount(_ sender: Any) {
        
        ActionSheetStringPicker.show(withTitle: "Select Account Purpose", rows: arrPurposeAccount, initialSelection: currentSelectedIndex, doneBlock: {(picker, index, value) in
            self.selectedPurposeAccount = self.arrPurposeAccount[index]
            self.btnAccountPurpose.setTitle(self.selectedPurposeAccount, for: .normal)
            self.currentSelectedIndex = index
        }, cancel: {(success) in
            self.selectedPurposeAccount = self.selectedPurposeAccount == "" ? "Select Account Purpose" : self.selectedPurposeAccount
        }, origin: btnAccountPurpose)
        
        
    }
    
    @IBAction func actNextTap(_ sender: Any) {
        var errorMessage: String?
        
        let sourceOfIncome = textFieldSourceOfIncome.text
        let occupation = textFieldOccupation.text
        if (sourceOfIncome == nil || sourceOfIncome == "") {
            errorMessage = "Source of Income field must not be empty."
        }
        else if (occupation == nil || occupation == "" ) {
            errorMessage = "Occupation field must not be empty."
        }
        else if selectedPurposeAccount == "" {
            errorMessage = "Purpose of Account field must not be empty."
        }
        if(errorMessage != nil) {
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: errorMessage!, actionType: "serverFailure", isCancelBtnHidden: true)
        }
        else {
            let hraVC2 = UIStoryboard(name: "MyAccount", bundle: nil).instantiateViewController(withIdentifier: "HRAVC2") as! HRARegistrationVC2
            hraVC2.customerCNIC = self.customerCNIC
            hraVC2.customerName = self.response.2["CNAME"]!
            hraVC2.customerDOB = self.response.2["CDOB"]!
            hraVC2.customerFatherHusbandName = self.response.2["FATHER_NAME"]!
            hraVC2.occupation = occupation ?? ""
            hraVC2.sourceOfIncome = sourceOfIncome ?? ""
            hraVC2.purposeOfAcct = selectedPurposeAccount
            
            self.pushViewController(hraVC2)
        }

        
    }
    
    
    @IBAction func actCancel2(_ sender: Any) {
        self.popViewController()
    }
    
    
    func HRAAccountOpeningInfoRequest() {
        self.showLoadingView()
        
        let cnicNumber = cnicTextFieldFirst.text!+cnicTextFieldSecond.text!+cnicTextFieldThird.text!
        
        if(Constants.AppConfig.IS_MOCK == 1) {
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
            }else if(response.1.msg != nil){
                
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE_IMPCR_SUCCESS, msgLabelText: response.1.msg!, actionType: "", isCancelBtnHidden: true)
                
            }
            
            self.hideLoadingView()
        } else {
            
            
            //let encryptedPin = try! inputText.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let myAccApi : MyAccountWebAPI = MyAccountWebAPI()
            
            myAccApi.HRAAccountOpeningInfo(
                Constants.CommandId.HRA_ACCOUNT_OPENINGINFO,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                APPID: "2",
                CNIC: cnicNumber,
                onSuccess:{(data) -> () in
                    //print(data)
                    self.response = XMLParser.balanceEnquiryXMLParsing(data)
                    
                    if(self.response.0.msg != nil) {
                        if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "delegate", isCancelBtnHidden: true)
                        }
                        else{
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "delegate", isCancelBtnHidden: true)
                        }
                    }
                    else if(self.response.1.msg != nil) {
                        
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "", isCancelBtnHidden: true)
                        
                    }
                    else {
                        DispatchQueue.main.async() {
                            self.scrollView.isHidden = false
                            self.cnicTextFieldFirst.isUserInteractionEnabled = false
                            self.cnicTextFieldSecond.isUserInteractionEnabled = false
                            self.cnicTextFieldThird.isUserInteractionEnabled = false
                            self.tableView.reloadData()
                            self.btnSearch.isHidden = true
                            self.btnCancel.isHidden = true
                            self.customerCNIC = cnicNumber
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
