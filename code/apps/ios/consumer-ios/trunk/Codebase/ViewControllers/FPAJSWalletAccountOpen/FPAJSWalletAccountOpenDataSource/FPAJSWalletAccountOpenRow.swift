//
//  FPAJSWalletAccountOpenRow.swift
//  FalconApp
//
//  Created by M Zeshan Arif on 17/09/2017.
//  Copyright © 2017 Wateen. All rights reserved.
//

import UIKit

class FPAJSWalletAccountOpenRow: NSObject {

    fileprivate var _title : String = ""
    var title: String {
        get {
            return _title
        }
        set {
            _title = newValue
        }
    }
    
    fileprivate var _value : String = ""
    var value: String {
        get {
            return _value
        }
        set {
            _value = newValue
        }
    }
    
    fileprivate var _dateFormat : String = ""
    var dateFormat: String {
        get {
            return _dateFormat
        }
        set {
            _dateFormat = newValue
        }
    }
    
    
    fileprivate var _image : UIImage?
    var image: UIImage? {
        get {
            return _image
        }
        set {
            _image = newValue
        }
    }
    
    fileprivate var _rowType : FPAJSWalletAccountOpenRowType = .summary
    var rowType: FPAJSWalletAccountOpenRowType {
        get {
            return _rowType
        }
        set {
            _rowType = newValue
        }
    }
}
