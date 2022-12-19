//
//  FPAJSWalletAccountOpenViewController.swift
//  FalconApp
//
//  Created by M Zeshan Arif on 06/09/2017.
//  Copyright © 2017 Wateen. All rights reserved.
//

import UIKit


class FPAJSWalletAccountOpenViewController: BaseViewController, UITextFieldDelegate {
    // MARK: - Outlets -
    @IBOutlet weak var imgCard: UIImageView!
    @IBOutlet weak var lblMobileNumber: UILabel!
    @IBOutlet weak var txtMobileNo: UITextField!
    @IBOutlet weak var lblCNIC: UILabel!
    @IBOutlet weak var viewCNIC: CNICTextFieldView!
    @IBOutlet weak var btnNext: FPAUIButton!
    
    // MARK: - Variables -
    var bank: Bank!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Setup UI
        setupView()
        
        // Initialization
        initialization()
    }
    

    func setupView()  {
        //topView.updateTitle("Account Opening")
        
        //let imgUrlStr = "\(IMAGE_URL_SWIFT)\(BANKS_IMAGE_PATH)\(Utility.addImageExtension(bank!.layout!)!)"
        
        //imgCard.sd_setImage(with: URL(string: imgUrlStr), placeholderImage: UIImage(named: "card_visa_bg-1"), options: SDWebImageOptions.refreshCached)
        Utility.roundButton(btn: btnNext)
        txtMobileNo.delegate = self
    }
    
    func initialization(){
//        if let mobNo =  Customer.sharedInstance().mobileNo{
//            txtMobileNo.text = mobNo
////            txtMobileNo.isEnabled = false
////            txtMobileNo.isUserInteractionEnabled = false
////            txtMobileNo.backgroundColor = Utility.disabledColor()
//        }
//        if let cnic = Customer.sharedInstance().cnic{
//            viewCNIC.setCustomerCNIC(cnic)
//        }
    }
    
    func validate() -> Bool{
        
        var alertMessage = ""
        if txtMobileNo.text == "" || viewCNIC.cnicString == "" {
            alertMessage = Constants.Message.kMESSAGE_EMPTY_FIELD_ERROR
        }
            
        else if txtMobileNo.text?.characters.count != 11{
            alertMessage = Constants.Message.kMESSAGE_INVALID_MOBILE_NO
        }
        else if !viewCNIC.isValidCNIC() {
            alertMessage = Constants.Message.kMESSAGE_cnicErrorMsg
        }
        
        
        
        if alertMessage != "" {
            self.showMessage(alertMessage)
            return false
        }
        return true
    }
    
    
    func showOtpPopup(_ account: JSAccount)
    {
        
//        
//        let otpVerifyPopup = IMEIUpdatePopup(titleAndMessage: "OTP Verification", message: "Enter OTP", successBlock: { (sender, responseMessage) in
//            
//            if let sender = sender as? BasePopup{
//                sender.hide(completion: {})
//            }
//            
//            self.showMessage(responseMessage!, completion: { (sender) in
//                sender?.removeFromSuperview()
//                
//                if let accountStatus = account.walletAccountStatus{
//                    switch accountStatus {
//                    case .New:
//                        if let vc = Utility.getViewController("FPAJSWalletAccountOpenDetailViewController", storyboardName: kST_ID_POPUP) as? FPAJSWalletAccountOpenDetailViewController
//                        {
//                            vc.account = account
//                            self.navigationController?.pushViewController(vc, animated: true)
//                        }
//                        break
//                    case .DiscrepantWithProfilePhoto,
//                         .DiscrepantWithCNICPhoto,
//                         .DiscrepantWithBothPhotos:
//                        
//                        if let vc = Utility.getViewController("FPAJSWalletAccountOpenDiscrepantVC", storyboardName: kST_ID_POPUP) as? FPAJSWalletAccountOpenDiscrepantVC{
//                            vc.account = account
//                            self.navigationController?.pushViewController(vc, animated: true)
//                        }
//                        break
//                    default:
//                        break
//                    }
//                }
//                
//                
//            })
//            
//        }, failureBlock: { (sender, errorMessage) in
//            if let sender = sender as? BasePopup{
//                sender.hide(completion: {})
//            }
//            self.showMessage(errorMessage!, completion: { (sender) in
//                sender?.removeFromSuperview()
//            })
//        }, cancel: { (sender) in
//            if let sender = sender as? BasePopup{
//                sender.hide(completion: {})
//            }
//        })
//        otpVerifyPopup?.otpVerificationDict = [
//            "CMOB": self.txtMobileNo.text!,
//            "BAID": self.bank.bankId!,
//            "DATETIME": Utility.getDateAndTime(),
//            "CNIC": self.viewCNIC.getCNIC(),
//            "TTYPE": TType.AccountOpen.rawValue,
//            "PAYMENT_MODE_ID": "11"
//        ]
//        otpVerifyPopup?.otpMaxLength = 5
//        otpVerifyPopup?.show(self.view)
    }

    @IBAction func btnNext_TouchUpInside(_ sender: UIButton) {
        self.view.endEditing(true)
        if validate(){
            
            // #TESTING:
//            if let vc = Utility.getViewController("FPAJSWalletAccountOpenDetailViewController", storyboardName: kST_ID_POPUP) as? FPAJSWalletAccountOpenDetailViewController
//            {
//                let account = MOJSWalletAccount()
//                account.cnic = self.viewCNIC.getCNIC()!
//                account.accountNo = self.txtMobileNo.text!
//                account.walletAccountStatus = .New
//                account.walletAccountType = .L0
//                vc.account = account
//                self.navigationController?.pushViewController(vc, animated: true)
//            }
//            return;
            let dicParams:[String:String] = ["DTID": Constants.AppConfig.DTID_KEY,
                                             "CNIC": viewCNIC.getCNIC(),
                                             "MOBN": txtMobileNo.text!,
                                             "DATETIME" : Utility.getDate(date: Date(), withFormat: "yyyyddmmhhmm"),
                                             "TTYPE": TType.AccountOpen.rawValue
                                             ]
            
            
//            self.showLoadingView()
//            BankApiManager.verifyJSAccount(dicParams, successBlock: { (response) in
//                
//                OnoXmlParser.parseJSWalletAccountOpenVerificationResponse(response, successBlock: { (account) in
//                    if let jsWalletAccount = account as? MOJSWalletAccount{
//                        
//                        // #TESTING:
//                        jsWalletAccount.walletAccountStatus = .New
//                        jsWalletAccount.walletAccountType = .L0
//                        
//                        if  let _ =  jsWalletAccount.walletAccountStatus,
//                            let type = jsWalletAccount.walletAccountType,
//                            type == .L0
//                        {
//                            self.showOtpPopup(jsWalletAccount)
//                        }
//                        else{
//                            self.showMessage(kEXCEPTION_GENERAL_SERVER_ERROR, completion: { (sender) in
//                                sender?.removeFromSuperview()
//                            })
//                        }
//                    }
//                }, failureBlock: { (error) in
//                    self.showMessage(error?.message!, completion: { (sender) in
//                        sender?.removeFromSuperview()
//                    })
//                })
//                
//                self.hideLoadingView()
//                
//            }, failureBlock: { (error) in
//                Utility.hideLoadingView(in: self.view)
//                self.showMessage(error?.localizedDescription, completion: { (sender) in
//                    sender?.removeFromSuperview()
//                })
//            })
            
            
           
        }
    }

    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        
        if string == "" {
            return true
        }
        let set = CharacterSet(charactersIn: Constants.Validation.kNUMERIC)
        if(string.rangeOfCharacter(from: set.inverted) != nil ){
            return false
        }
        else if textField.text?.characters.count == Constants.Validation.kMAX_PHONE_CHARACTERS{
            return false
        }
        
        
        return true
    }

}




