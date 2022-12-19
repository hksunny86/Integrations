//
//  FPAJSWalletAccountOpenDataSource.swift
//  FalconApp
//
//  Created by M Zeshan Arif on 17/09/2017.
//  Copyright © 2017 Wateen. All rights reserved.
//

import UIKit

class FPAJSWalletAccountOpenDataSource: NSObject {
    var sections: [FPAJSWalletAccountOpenSection] = []
}


enum FPAJSWalletAccountOpenRowType {
    case summary
    case image
}
