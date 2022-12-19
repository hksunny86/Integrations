//
//  HRARegistrationVC3ViewController.swift
//  JSBL-BB
//
//  Created by Uzair on 9/25/20.
//  Copyright © 2020 Inov8. All rights reserved.
//

import UIKit
import ActionSheetPicker_3_0


protocol addOriginatorDelegate {
    func addOriginator(at index:IndexPath)
    func displayRelationShipPicker()
    func displayRelationShipPickerCell1()
    func deleteSelf(at index:IndexPath)
    func afterClickingReturnInTextField(cell: AddOriginatorCell2)
    func afterClickingReturnInTextFieldCell1(cell: AddOriginatorCell)
}



class HRARegistrationVC3ViewController: BaseViewController, UITableViewDelegate, UITableViewDataSource, addOriginatorDelegate, FinancialPinPopupDelegate {
    
    
    
    
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var btnNext: UIButton!
    @IBOutlet weak var btnCancel: UIButton!
    var customerCNIC = ""
    var customerName = ""
    var customerDOB = ""
    var customerFatherHusbandName = ""
    var occupation = ""
    var sourceOfIncome = ""
    var purposeOfAcct = ""
    var kinCNIC = ""
    var kinName = ""
    var kinRelation = ""
    var kinMobileNo = ""
    var encryptedPIN = ""
    var count = 1
    var currentSelectedIndex = 0
    var arrRelations = ["Spouse", "Children", "Father", "Mother", "Relative", "Friend", "Sister", "Brother", "Others"]
    var arrOriginator = ["1", "2", "3", "4", "5"]
    var org_locations = ["", "", "", "" ,""]
    var org_Relations = ["", "", "", "", ""]
    var currentCell:AddOriginatorCell2?
    var currentCell1:AddOriginatorCell?
    var selectedRelation = ""
    var response = (XMLError(), XMLMessage())
    var pinValue = String()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: false)
        // Do any additional setup after loading the view.
        self.bottomBarView.isHidden = true
        tableView.register(UINib(nibName: "AddOriginatorCell", bundle: nil), forCellReuseIdentifier: "originatorCell")
        tableView.register(UINib(nibName: "AddOriginatorCell2", bundle: nil), forCellReuseIdentifier: "originatorCell2")
        tableView.delegate = self
        tableView.dataSource = self
        btnNext.layer.cornerRadius = Constants.UI.Button.CORNER_RADIUS
        btnCancel.layer.cornerRadius = Constants.UI.Button.CORNER_RADIUS
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 270
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.row == 0 {
            let cell:AddOriginatorCell = tableView.dequeueReusableCell(withIdentifier: "originatorCell", for: indexPath) as! AddOriginatorCell
            
            cell.layer.shadowColor = UIColor.lightGray.cgColor
            cell.layer.shadowOffset = CGSize(width: 0.5, height: 0.5)
            cell.layer.borderWidth = 0.1
            cell.layer.shadowRadius = 2
            cell.layer.shadowOpacity = 0.4
            
            cell.lblOriginatorNO.text = "Originator " + "\(arrOriginator[indexPath.row])"
            cell.delegate = self
            cell.indexPath = indexPath
            currentCell1 = cell

            return cell
        }
        else {
            let cell2:AddOriginatorCell2 = tableView.dequeueReusableCell(withIdentifier: "originatorCell2", for: indexPath) as! AddOriginatorCell2
            cell2.layer.shadowColor = UIColor.lightGray.cgColor
            cell2.layer.shadowOffset = CGSize(width: 0.5, height: 0.5)
            cell2.layer.borderWidth = 0.1
            cell2.layer.shadowRadius = 2
            cell2.layer.shadowOpacity = 0.4
            
            
            cell2.lblOriginatorNO.text = "Originator " + "\(arrOriginator[indexPath.row])"
            cell2.delegate = self
            cell2.indexPath = indexPath
            currentCell = cell2
            
            return cell2
            
        }
    }
    
    func addOriginator(at index:IndexPath) {
        if count < 5 {
            count = count + 1
            currentSelectedIndex = 0
            DispatchQueue.main.async {
                self.tableView.reloadData()
            }
        }
    }
    
    func displayRelationShipPicker() {
        ActionSheetStringPicker.show(withTitle: "Select Relation", rows: arrRelations, initialSelection: currentSelectedIndex, doneBlock: {(picker, index, value) in
            self.selectedRelation = self.arrRelations[index]
            self.currentCell?.btnRelationWithOriginator.setTitle(self.selectedRelation, for: .normal)
            self.org_Relations[(self.currentCell?.indexPath.row)!] = self.selectedRelation
            self.currentSelectedIndex = index
        }, cancel: {(success) in
            self.selectedRelation = self.selectedRelation == "" ? "Select Relation" : self.selectedRelation
        }, origin: currentCell?.btnRelationWithOriginator)
        
    }
    
    func displayRelationShipPickerCell1() {
        ActionSheetStringPicker.show(withTitle: "Select Relation", rows: arrRelations, initialSelection: currentSelectedIndex, doneBlock: {(picker, index, value) in
            self.selectedRelation = self.arrRelations[index]
            self.currentCell1?.btnRelationWithOriginator.setTitle(self.selectedRelation, for: .normal)
            self.org_Relations[(self.currentCell1?.indexPath.row)!] = self.selectedRelation
            self.currentSelectedIndex = index
        }, cancel: {(success) in
            self.selectedRelation = self.selectedRelation == "" ? "Select Relation" : self.selectedRelation
        }, origin: currentCell1?.btnRelationWithOriginator)
    }
    
    func deleteSelf(at index: IndexPath) {
        count = count - 1
        tableView.beginUpdates()
        self.tableView.deleteRows(at: [index], with: .automatic)
        tableView.endUpdates()
    }
    
    func afterClickingReturnInTextField(cell: AddOriginatorCell2) {
        self.org_locations[cell.indexPath.row] = cell.locationTextField.text ?? ""
    }
    
    func afterClickingReturnInTextFieldCell1(cell: AddOriginatorCell) {
        self.org_locations[cell.indexPath.row] = cell.locationTextField.text ?? ""
    }
    
    
    func okPressedFP() {
        
    }
    
    func okPressedChallanNo(EncMpin: String) {
        self.encryptedPIN = EncMpin
        HRAAccountOpeningRequest()
    }
    
    func canclePressedFP() {
        
    }
    
    
    
    @IBAction func actNext(_ sender: Any) {
        self.showFinancialPinPopup("", requiredAction: "", delegate: self, productFlowID: Constants.FID.HRA_ACCCOUNT_OPENING, productId: "")
        
    }
    
    @IBAction func actCancel(_ sender: Any) {
        self.popViewController()
    }
    
    
    func HRAAccountOpeningRequest() {
        self.showLoadingView()
        
        
        
        if(Constants.AppConfig.IS_MOCK == 1) {
            guard let
                xmlPath = Bundle.main.path(forResource: "Command-6-Balance Inquiry", ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                
                else { return }
            
            response = XMLParser.changePINXMLParsing(data)
            if(response.0.msg != nil){
                if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                }else{
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "requestDenied", isCancelBtnHidden: true)
                    
                }
            }else if(response.1.msg != nil){
                
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE_IMPCR_SUCCESS, msgLabelText: response.1.msg!, actionType: "", isCancelBtnHidden: true)
                
            }
            
            self.hideLoadingView()
        } else {
            
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            let myAccApi : MyAccountWebAPI = MyAccountWebAPI()
            myAccApi.HRAAccountOpeningRequest(Constants.CommandId.HRA_ACCOUNT_OPENING, reqTime: currentTime, DTID: Constants.AppConfig.DTID_KEY, ENCT: "1", CMOB: (Customer.sharedInstance.cMob)!, PIN: encryptedPIN, CNIC: customerCNIC, CNAME: customerName, CDOB: customerDOB, FATHER_HUSBND_NAME: customerFatherHusbandName, AMOB: "NULL", OCCUPATION: occupation, ORG_LOC1:org_locations[0] , ORG_LOC2: org_locations[1], ORG_LOC3:org_locations[2] , ORG_LOC4: org_locations[3], ORG_LOC5: org_locations[4], ORG_REL1: org_Relations[0], ORG_REL2: org_Relations[1], ORG_REL3: org_Relations[2], ORG_REL4: org_Relations[3], ORG_REL5: org_Relations[4], TRX_PUR: purposeOfAcct, SOI: sourceOfIncome, KIN_NAME: kinName, KIN_MOB_NO: kinMobileNo, KIN_CNIC: kinCNIC, KIN_RELATIONSHIP: kinRelation, onSuccess:{ (data) -> () in
                //print(data)
                self.response = XMLParser.changePINXMLParsing(data)
                self.hideLoadingView()
                if(self.response.0.msg != nil){
                    if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "delegate", isCancelBtnHidden: true)
                    }else{
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "delegate", isCancelBtnHidden: true)
                        
                    }
                }else if(self.response.1.msg != nil) {
                    
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE_HRA_SUCCESS, msgLabelText: self.response.1.msg!, actionType: "goToMainMenu", isCancelBtnHidden: true)
                    
                }
                
                
            },
                                              onFailure: {(reason) ->() in
                                                //print("Failure")
                                                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                                                self.hideLoadingView()
                                                
            })
            
        }
        
    }
    
    
}
