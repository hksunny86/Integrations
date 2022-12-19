//
//  ContactUSVC.swift
//  Timepey
//
//  Created by Adnan Ahmed on 23/09/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation
import UIKit

class ContactUSVC: BaseViewController {
    
    @IBOutlet weak var cellView: UIView!
    @IBOutlet weak var callButton: UIButton!
    @IBOutlet weak var otherCallButton: UIButton!
    @IBOutlet weak var webCallButton: UIButton!
    
    override func viewDidLoad() {
        //super.viewDidLoad()
        
        cellView.layer.cornerRadius = 4
        cellView.layer.borderWidth = 0.5
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: false)
        
        let attributedString = NSMutableAttributedString(string:"")
        let buttonTitleStr = NSMutableAttributedString(string:"jcash@jsbl.com", attributes:[NSAttributedString.Key.underlineStyle : 1])
        
        attributedString.append(buttonTitleStr)
       otherCallButton.setAttributedTitle(attributedString, for: .normal)
        
        let attributedString1 = NSMutableAttributedString(string:"")
        let buttonTitleStr1 = NSMutableAttributedString(string:"021 111 444 000", attributes:[NSAttributedString.Key.underlineStyle : 1])
        
        attributedString1.append(buttonTitleStr1)
        callButton.setAttributedTitle(attributedString1, for: .normal)
        
        
        let attributedString2 = NSMutableAttributedString(string:"")
        let buttonTitleStr2 = NSMutableAttributedString(string:"www.jsbl.com/jcash", attributes:[NSAttributedString.Key.underlineStyle : 1])
        
        attributedString2.append(buttonTitleStr2)
        webCallButton.setAttributedTitle(attributedString2, for: .normal)
        
    
    }
    @IBAction func OtherButtonPressed(_ sender: UIButton) {
        let email = "jcash@jsbl.com"
        if let url = URL(string: "mailto:\(email)") {
            UIApplication.shared.openURL(url as URL)
        }
    }
    
    @IBAction func callButtonPressed(_ sender: UIButton) {
        let url:NSURL = NSURL(string: "tel://021111444000")!
        UIApplication.shared.openURL(url as URL)
    }
    
    @IBAction func openWeb(_ sender: UIButton) {
        let url:NSURL = NSURL(string: "http://www.jsbl.com/jcash")!
        UIApplication.shared.openURL(url as URL)
    }
    
}
