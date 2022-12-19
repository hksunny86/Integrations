//
//  HRARegistrationVC2.swift
//  JSBL-BB
//
//  Created by Uzair on 9/25/20.
//  Copyright © 2020 Inov8. All rights reserved.
//

import UIKit
import ActionSheetPicker_3_0

class HRARegistrationVC2: BaseViewController, UITextFieldDelegate {

    @IBOutlet weak var nameTextField: UITextField!
    @IBOutlet weak var mobileNoTextField: UITextField!
    @IBOutlet weak var cnicTextField: UITextField!
    @IBOutlet weak var relationshipButton: UIButton!
    
    @IBOutlet weak var btnNext: UIButton!
    @IBOutlet weak var btnCancel: UIButton!
    
    var customerCNIC = ""
    var customerName = ""
    var customerDOB = ""
    var customerFatherHusbandName = ""
    var occupation = ""
    var sourceOfIncome = ""
    var purposeOfAcct = ""
    var arrRelations = ["Spouse", "Children", "Father", "Mother", "Relative", "Friend", "Sister", "Brother", "Others"]
    var currentSelectedIndex = 0
    var selectedRelation = ""
    
    override func viewDidLoad() {
        
        super.viewDidLoad()
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: false)
        // Do any additional setup after loading the view.
        self.bottomBarView.isHidden = true
        setView()
        nameTextField.delegate = self
        mobileNoTextField.delegate = self
        cnicTextField.delegate = self
        
    }
    
    
    func setView(){
        
        let paddingForSec = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.nameTextField.frame.size.height))
        //Adding the padding to the second textField
        nameTextField.leftView = paddingForSec
        nameTextField.leftViewMode = UITextField.ViewMode.always
        
        let paddingForThird = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.mobileNoTextField.frame.size.height))
        //Adding the padding to the second textField
        mobileNoTextField.leftView = paddingForThird
        mobileNoTextField.leftViewMode = UITextField.ViewMode.always
        
        let paddingForFourth = UIView(frame: CGRect(x: 0, y: 0, width: 15, height: self.cnicTextField.frame.size.height))
        //Adding the padding to the second textField
        cnicTextField.leftView = paddingForFourth
        cnicTextField.leftViewMode = UITextField.ViewMode.always
        
        nameTextField.layer.cornerRadius = 2
        nameTextField.layer.borderWidth = 0.7
        nameTextField.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        
        mobileNoTextField.layer.cornerRadius = 2
        mobileNoTextField.layer.borderWidth = 0.7
        mobileNoTextField.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        
        cnicTextField.layer.cornerRadius = 2
        cnicTextField.layer.borderWidth = 0.7
        cnicTextField.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        
        relationshipButton.layer.cornerRadius = 2
        relationshipButton.layer.borderWidth = 0.7
        relationshipButton.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        relationshipButton.layer.cornerRadius = Constants.UI.Button.CORNER_RADIUS
        if #available(iOS 11.0, *) {
            relationshipButton.contentHorizontalAlignment = .left
        } else {
            // Fallback on earlier versions
            relationshipButton.contentHorizontalAlignment = .left
        }
        
        relationshipButton.contentEdgeInsets = UIEdgeInsets(top: 0, left: 10, bottom: 0, right: 0);
        
        btnNext.layer.cornerRadius = Constants.UI.Button.CORNER_RADIUS
        btnCancel.layer.cornerRadius = Constants.UI.Button.CORNER_RADIUS
        relationshipButton.setTitle("Select RelationShip", for: .normal)
        
    }
    
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if (textField.tag == 1) {
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_ALL, length: 25)
        } else if(textField.tag == 2) {
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 11)
            
        } else if(textField.tag == 3) {
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_NUMERIC, length: 13)
            
        }
        else {
            return true
        }
    }
    @IBAction func actRelationshipSelection(_ sender: Any) {
        ActionSheetStringPicker.show(withTitle: "Select Relation", rows: arrRelations, initialSelection: currentSelectedIndex, doneBlock: {(picker, index, value) in
            self.selectedRelation = self.arrRelations[index]
            self.relationshipButton.setTitle(self.selectedRelation, for: .normal)
            self.currentSelectedIndex = index
        }, cancel: {(success) in
            self.selectedRelation = self.selectedRelation == "" ? "Select Relation" : self.selectedRelation
        }, origin: sender)
        
    }
    
    
    
    
    @IBAction func actNext(_ sender: Any) {
        
        var errorMessage: String?
        
        let name = nameTextField.text
        let mobileNumText = mobileNoTextField.text!
        let cnic = cnicTextField.text
        let relation = selectedRelation
        
        if(mobileNumText == ""){
            errorMessage = "Mobile number field must not be empty."
        }else if(mobileNumText.count != 11){
            errorMessage = "Mobile number length should be of 11 digits."
        }else if(mobileNumText[mobileNumText.index(mobileNumText.startIndex, offsetBy: 0)] != "0" || mobileNumText[mobileNumText.index(mobileNumText.startIndex, offsetBy: 1)] != "3"){
            errorMessage = "Mobile number must start with 03."
        }
        else if (name == nil || name == "" ) {
            errorMessage = "Name field must not be empty."
        }
        else if (cnic == nil || cnic == "") {
            errorMessage = "CNIC field must not be empty."
        }
        else if (relation == "" || relation == "Select Relation") {
            errorMessage = "Relation field must not be empty."
        }
        if errorMessage != nil {
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: errorMessage!, actionType: "serverFailure", isCancelBtnHidden: true)
        }
        else {
            let hraVC3 = UIStoryboard(name: "MyAccount", bundle: nil).instantiateViewController(withIdentifier: "HRAVC3") as! HRARegistrationVC3ViewController
            hraVC3.customerCNIC = self.customerCNIC
            hraVC3.customerName = self.customerName
            hraVC3.customerDOB = self.customerDOB
            hraVC3.customerFatherHusbandName = self.customerFatherHusbandName
            hraVC3.occupation = self.occupation
            hraVC3.purposeOfAcct = self.purposeOfAcct
            hraVC3.sourceOfIncome = self.sourceOfIncome
            hraVC3.kinCNIC = cnic ?? ""
            hraVC3.kinName = name ?? ""
            hraVC3.kinMobileNo = mobileNumText 
            hraVC3.kinRelation = relation
            self.pushViewController(hraVC3)
        }
        
    }
    
    
    @IBAction func actCancel(_ sender: Any) {
        self.popViewController()
        
    }
    
    
    

}
