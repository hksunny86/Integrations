//
//  FPAJSWalletAccountOpenSection.swift
//  FalconApp
//
//  Created by M Zeshan Arif on 17/09/2017.
//  Copyright © 2017 Wateen. All rights reserved.
//

import UIKit

class FPAJSWalletAccountOpenSection: NSObject {
    
    fileprivate var _title : String = ""
    fileprivate var _rows : [FPAJSWalletAccountOpenRow] = []
    
    var title: String {
        get {
            return _title
        }
        set {
            _title = newValue
        }
    }
    
    var rows: [FPAJSWalletAccountOpenRow] {
        get {
            return _rows
        }
        set {
            _rows = newValue
        }
    }
    
}
