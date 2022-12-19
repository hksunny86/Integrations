//
//  BottomBarView.swift
//  Timepey
//
//  Created by Adnan Ahmed on 19/09/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//
import UIKit
import Foundation

enum BOTTOM_BAR_BUTTON_PRESSED : Int {
    case bottom_BAR_LOCATOR = 1
    case bottom_BAR_MYACCOUNT
    case bottom_BAR_QRPAY
    case bottom_BAR_CONTACT_US
    case bottom_BAR_FAQ
    case bottom_BAR_NONE
}


protocol BottomBarDelegate: class {
    func actBottomBtn(_ buttonPressedType: BOTTOM_BAR_BUTTON_PRESSED)
}

class BottomBarView: UIView {
    
    
    var view: UIView!
    
    @IBOutlet weak var btnContactUs: UIButton!
    @IBOutlet weak var btnFaq: UIButton!
    @IBOutlet weak var btnSecurityGuide: UIButton!
    @IBOutlet weak var btnUserGuide: UIButton!
    @IBOutlet weak var btnTerms: UIButton!
    
    var delegate: BottomBarDelegate?
    
    
    func xibSetup(_ senderVC: BottomBarDelegate, viewFrame: CGRect) {
        self.view = self.loadViewFromNib()
        self.view.frame = viewFrame
        Utility.addShadowToView(view: self.view)
        
        self.btnTerms.setBackgroundColor(UIColor(red:0.99, green:0.71, blue:0.08, alpha:1.0), for: .highlighted)
        self.btnUserGuide.setBackgroundColor(UIColor(red:0.99, green:0.71, blue:0.08, alpha:1.0), for: .highlighted)
        //btnSecurityGuide.setBackgroundColor(color: UIColor(red:0.99, green:0.71, blue:0.08, alpha:1.0), forState: .highlighted)
        self.btnFaq.setBackgroundColor(UIColor(red:0.99, green:0.71, blue:0.08, alpha:1.0), for: .highlighted)
        self.btnContactUs.setBackgroundColor(UIColor(red:0.99, green:0.71, blue:0.08, alpha:1.0), for: .highlighted)
        self.addSubview(self.view)
        
        self.delegate = senderVC
        
    }
    
    func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "BottomBarView", bundle: bundle)
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }
    
    // MARK: IBAction
    @IBAction func actBottomBtn(_ sender: UIButton) {
        
        var buttonPressedType = BOTTOM_BAR_BUTTON_PRESSED.bottom_BAR_NONE
        if sender.tag == 1 {
            buttonPressedType = BOTTOM_BAR_BUTTON_PRESSED.bottom_BAR_LOCATOR
        }
        else if sender.tag == 2 {
            buttonPressedType = BOTTOM_BAR_BUTTON_PRESSED.bottom_BAR_MYACCOUNT
        }
        else if sender.tag == 3 {
            buttonPressedType = BOTTOM_BAR_BUTTON_PRESSED.bottom_BAR_QRPAY
        }
        else if sender.tag == 4 {
            buttonPressedType = BOTTOM_BAR_BUTTON_PRESSED.bottom_BAR_CONTACT_US
        }
        else {
            buttonPressedType = BOTTOM_BAR_BUTTON_PRESSED.bottom_BAR_FAQ
        }
        delegate?.actBottomBtn(buttonPressedType)
    }
    
}
