//
//  JSAccount.swift
//  JSBL-BB
//
//  Created by Adnan Ahmed on 20/09/2017.
//  Copyright © 2017 Inov8. All rights reserved.
//

import UIKit

import UIKit


//Reference
//1.       Account type required in case account already existed. 01 = L0 account, 02 = L1 account
enum MOJSWalletAccountType : String{
    case L0 = "01"
    case L1 = "02"
}

// Reference
//On 18/09/2017, at 12:22 PM, Asad Ullah wrote:
//> 3. Following are the customer types that we can receive
//> a. 0 = Inactive
//> b. 1 = Active
//> c. 2 = Discrepant with Profile Picture
//> d. 3 = Discrepant with CNIC Picture
//> e. 4 Discrepant with both Picture


enum MOJSWalletAccountStatus : String{
    case New = "-1"
    case Inactive = "0"
    case Active = "1"
    case DiscrepantWithProfilePhoto = "2"
    case DiscrepantWithCNICPhoto = "3"
    case DiscrepantWithBothPhotos = "4"
    
}


class JSAccount: NSObject {
    
    fileprivate var _lastName: String = ""
    fileprivate var _name: String = ""
    fileprivate var _dob: String = ""
    fileprivate var _cnicExpiryDate: String = ""
    fileprivate var _cnicIssueDate: String = ""
    fileprivate var _customerEmail: String = ""
    fileprivate var _customerSimCarrier: String = ""
    fileprivate var _customerImage: UIImage?
    fileprivate var _cnicImage: UIImage?
    fileprivate var _accountStatus: MOJSWalletAccountStatus?
    fileprivate var _accountType: MOJSWalletAccountType?
    
    var _firstName: String = ""
    var isDiscrepant: String?
    var cnic: String = ""
    var accountNo: String = ""
    var cnicIssueDate: String = ""
    var customerSIMCarrier: String = ""
    var customerEmail:String = ""
    var cnicPhotoFlag: String?
    var customerPhotoFlag: String?
    var cnicPhotoPath: String?
    var customerPhotoPath: String?
    
    override init() {
        super.init()
        _firstName = ""
        _lastName = ""
        _dob = ""
        _cnicExpiryDate = ""
    }
    
    required init!(coder: NSCoder!) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func parsing(Dict: [String: String]){
        if((Dict["CNAME"]) != nil){
            isDiscrepant = "1"
            name = Dict["CNAME"]!
            dob = Dict["CDOB"]!
            cnicExpiryDate = Dict["CNIC_EXP"]!
            
            cnicPhotoFlag = Dict["CNIC_FRONT_PHOTO_FLAG"]
            
            if(cnicPhotoFlag == "1"){
                _accountStatus = .DiscrepantWithCNICPhoto
            }
            customerPhotoFlag = Dict["CUSTOMER_PHOTO_FLAG"]
            if(customerPhotoFlag == "1"){
                _accountStatus = .DiscrepantWithProfilePhoto
            }
            
            if(cnicPhotoFlag == "1" && customerPhotoFlag == "1"){
                _accountStatus = .DiscrepantWithBothPhotos
            }
        }else{
            isDiscrepant = "0"
            _accountStatus = .New
        }
    }
    
    
    func encode(with coder: NSCoder!) {
        
    }
    
    var firstName: String {
        get {
            return _firstName
        }
        set {
            _firstName = newValue
        }
    }
    
    var lastName: String {
        get {
            return _lastName
        }
        set {
            _lastName = newValue
        }
    }
    
    var name: String{
        get{
            if _firstName == "" && _lastName == "" {
                return _name
            }
            return "\(_firstName) \(_lastName)"
        }
        set{
            _name = newValue
        }
    }
    
    var dob: String {
        get {
            return _dob
        }
        set {
            _dob = newValue
        }
    }
    
    var cnicExpiryDate: String {
        get {
            return _cnicExpiryDate
        }
        set {
            _cnicExpiryDate = newValue
        }
    }
    
    var customerImage: UIImage? {
        get {
            return _customerImage
        }
        set {
            _customerImage = newValue
        }
    }
    
    var cnicImage: UIImage? {
        get {
            return _cnicImage
        }
        set {
            _cnicImage = newValue
        }
    }
    
    var customerImageBase64: String{
        get{
            if let image = _customerImage {
                return Utility.base64(of: image)
            }
            return ""
        }
    }
    
    var cnicImageBase64: String{
        get{
            if let image = _cnicImage {
                return Utility.base64(of: image)
            }
            return ""
        }
    }
    
    var walletAccountStatus: MOJSWalletAccountStatus? {
        get {
            return _accountStatus
        }
        set {
            _accountStatus = newValue
        }
    }
    
    var walletAccountStatusString: String? {
        get {
            if let status = _accountStatus {
                return status.rawValue
            }
            return nil
        }
        set {
            if newValue != nil {
                _accountStatus = MOJSWalletAccountStatus(rawValue: newValue!)
            }
        }
    }
    
    var walletAccountTypeString: String? {
        get {
            if let type = _accountType {
                return type.rawValue
            }
            return nil
        }
        set {
            if newValue != nil {
                _accountType = MOJSWalletAccountType(rawValue: newValue!)
            }
        }
    }
    
    var walletAccountType: MOJSWalletAccountType? {
        get {
            return _accountType
        }
        set {
            _accountType = newValue
        }
    }
    
}
