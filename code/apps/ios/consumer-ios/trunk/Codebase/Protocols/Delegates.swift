//
//  FinancialPinPopupDelegate.swift
//  Timepey
//
//  Created by Adnan Ahmed on 26/10/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation

 protocol FinancialPinPopupDelegate: class {
    func okPressedFP()
    func okPressedChallanNo(EncMpin : String)
    func canclePressedFP()
}


protocol AlertPopupDelegate: class {
    func okPressedAP()
    func canclePressedAP()
}

protocol OTPDelegate: class {
    func okPressedOTP(_ pinText: String)
    func canclePressedOTP()
    func resendOTP(_ pinText: String)
}

