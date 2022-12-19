//
//  SuccessVC.swift
//  JSBL-BB
//
//  Created by Adnan Ahmed on 26/03/2018.
//  Copyright © 2018 Inov8. All rights reserved.
//

import Foundation
import UIKit

class SuccessVC: BaseViewController {
    
    @IBOutlet weak var headingText: UILabel!
    @IBOutlet weak var messageText: UILabel!
    var msg = ""
    var isComingFromDebitCardInfo = false
    
    override func viewDidLoad() {
        messageText.text = msg
    }
    
    
    @IBAction func okBtnPressed(_ sender: UIButton) {
        if isComingFromDebitCardInfo {
            self.popViewControllerAndGotoStart()
        } else {
            Utility.popVCToCustomerLogin()
        }
    }
    
    
}
