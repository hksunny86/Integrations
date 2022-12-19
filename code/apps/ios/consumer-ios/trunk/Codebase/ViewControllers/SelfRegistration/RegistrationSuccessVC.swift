//
//  RegistrationSuccessVC.swift
//  Timepey
//
//  Created by Adnan Ahmed on 17/10/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation
import UIKit

class RegistrationSuccessVC: BaseViewController {
    
    @IBOutlet weak var messageLabel: UILabel!
    @IBOutlet weak var okButton: UIButton!
    
    var messageText: String?
    
    override func viewDidLoad() {
        
        okButton.layer.cornerRadius = Constants.UI.Button.CORNER_RADIUS
        
        if(messageText != nil){
            messageLabel.text = messageText
        }
        
    }
    
    @IBAction func okButtonPressed(_ sender: UIButton) {
        self.goToCustomerLogin()
    }
    
}
