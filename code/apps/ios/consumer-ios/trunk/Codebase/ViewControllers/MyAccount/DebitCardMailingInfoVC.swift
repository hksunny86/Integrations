//
//  DebitCardMailingInfoVC.swift
//  JSBL-BB
//
//  Created by Hassan Masood on 12/8/20.
//  Copyright © 2020 Inov8. All rights reserved.
//

import UIKit

class DebitCardMailingInfoVC: BaseViewController, UITextFieldDelegate, FinancialPinPopupDelegate {
    
    

    @IBOutlet weak var lblMobileNumber: UILabel!
    @IBOutlet weak var lblCnicNumber: UILabel!
    @IBOutlet weak var lblCardFee: UILabel!
    @IBOutlet weak var txtFieldEmbossingName: FPMBottomBorderTextField!
    @IBOutlet weak var txtFieldMailingAddress: FPMBottomBorderTextField!
    @IBOutlet weak var btnNext: UIButton!
    
    @IBOutlet weak var btnAgree: UIButton!
    var fee = ""
    var cMob = ""
    var CNIC = ""
    var isAgreeToTerms: Bool = Bool()
    
    var response = (XMLError(), XMLMessage(), [String:String]())
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: true)
        // Do any additional setup after loading the view.
        lblMobileNumber.text = Customer.sharedInstance.cMob!
        lblCnicNumber.text = Customer.sharedInstance.cnic!
        if fee != "" {
            lblCardFee.text = "On debit card issuance \(fee) PKR fee will be applied."
        } else {
            lblCardFee.isHidden = true
            btnAgree.isHidden = true
        }
        txtFieldEmbossingName.delegate = self
        txtFieldMailingAddress.delegate = self
        
    }
    
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        txtFieldMailingAddress.focusedHeight()
        txtFieldEmbossingName.focusedHeight()
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        txtFieldMailingAddress.normalHeight()
        txtFieldEmbossingName.normalHeight()
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        
        if (textField.tag == 1) {
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_ALPHABETS_CARD_NAME, length: 50)
        } else if(textField.tag == 2) {
            return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_ALPHANUMERIC_ADDRESS, length: 250)
            
        } else{
            return true
        }
    }
    
    @IBAction func btnAgreeTap(_ sender: UIButton) {
        isAgreeToTerms = !isAgreeToTerms
        let imageName = isAgreeToTerms ? "btn_check_box_pressed" : ""
        sender.setImage(UIImage(named:imageName), for: UIControl.State())
    }
    
    @IBAction func btnNextTap(_ sender: Any) {
    
        var errorMessage: String?
        
        
        if txtFieldMailingAddress.text == "" {
            errorMessage = "Field must not be empty."
            
        } else if txtFieldEmbossingName.text == "" {
            errorMessage = "Field must not be empty."
        }
        else if fee != "" {
            if isAgreeToTerms == false {
                errorMessage = "Your consent is required for debit card issuance fee deduction.Kindly check the check box."
            }
        }
        if(errorMessage != nil) {
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: errorMessage!, actionType: "serverFailure", isCancelBtnHidden: true)
        }else{
            self.showFinancialPinPopup("", requiredAction: "", delegate: self, productFlowID: Constants.FID.DEBIT_CARD_ISSUANCE_INFO_UAT, productId: "")
        }
        
    }
    
    func okPressedFP() {
        cardIssuanceRequest()
    }
    
    func okPressedChallanNo(EncMpin: String) {
        
    }
    
    func canclePressedFP() {
        self.popViewController()
    }
    
    
    
    func cardIssuanceRequest() {
        
        self.showLoadingView()
        
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                xmlPath = Bundle.main.path(forResource: "Command-33-Login", ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                else { return }
            
            self.response = XMLParser.paramTypeXMLParsing(data)
            
            if(self.response.0.msg != nil){
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
            }
            else if(self.response.1.msg != nil){
                
                self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                
            }
            else {
                
            }
            self.hideLoadingView()
        }
        else {
            
                //let encryptedPin = try! inputText.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
                let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
                
                let myAccApi : MyAccountWebAPI = MyAccountWebAPI()
                
                myAccApi.debitCardIssuanceRequest(
                    Constants.CommandId.DEBIT_CARD_ISSUANCE,
                    reqTime: currentTime,
                    DTID: Constants.AppConfig.DTID_KEY,
                    APPID: "2",
                    cardDescription: txtFieldEmbossingName.text!,   //MARK: Set this PARAM in CAPITAL
                    mailingAddress: txtFieldMailingAddress.text!,
                    
                    onSuccess:{(data) -> () in
                        
                        self.response = XMLParser.paramTypeXMLParsing(data)
                        
                        
                        if(self.response.0.msg != nil) {
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            
                        }else if(self.response.1.msg != nil){
                            
                                let successVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "SuccessVC") as! SuccessVC
                            successVC.msg = self.response.1.msg!
                            successVC.isComingFromDebitCardInfo = true
                                self.pushViewController(successVC)
                            
                        }
                        DispatchQueue.main.async(){
                            self.hideLoadingView()
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
