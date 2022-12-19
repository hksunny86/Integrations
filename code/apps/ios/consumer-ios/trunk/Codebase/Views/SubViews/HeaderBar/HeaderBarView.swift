//
//  HeaderBar.swift
//  Timepey
//
//  Created by Adnan Ahmed on 28/09/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import UIKit
import Foundation

enum HEADER_BAR_BUTTON_PRESSED : Int {
    case HEADER_BACK_BUTTON = 1
    case HEADER_HOME_BUTTON
    case HEADER_SIGNOUT_BUTTON
    case HEADER_BUTTON_NONE
}


protocol HeaderBarDelegate: class {
    func actHeaderBtn(_ buttonPressedType: HEADER_BAR_BUTTON_PRESSED)
}

class HeaderBarView: UIView {
    
    var view: UIView!
    
    @IBOutlet weak var backButton: UIButton!
    //@IBOutlet weak var homeButton: UIButton!
    @IBOutlet weak var signoutButton: UIButton!
    @IBOutlet weak var headerTitle: UILabel!
    @IBOutlet weak var headerLogoImg: UIImageView!
    @IBOutlet weak var headerLogoLeadingConstraint: NSLayoutConstraint!
    
    var isHomeButtonHidden: Bool?
    var isSignoutButtonHidden: Bool?
    
    var delegate: HeaderBarDelegate?
    
    
    
    func xibSetup(_ senderVC: HeaderBarDelegate, viewFrame: CGRect, _ isBackButtonHidden: Bool, isHomeButtonHidden: Bool, isSignoutButtonHidden: Bool, title:String) {
        
        view = loadViewFromNib()
        view.frame = viewFrame
        addSubview(view)
        
        if(title != ""){
            headerTitle.text = title
            headerLogoImg.isHidden = true
        }else{
            headerLogoImg.isHidden = false
        }
        
        
        if(isSignoutButtonHidden == true && isHomeButtonHidden == false){
            //homeButton.isHidden = true
            signoutButton.isHidden = false
            signoutButton.setImage(UIImage(named: "btn_home_normal"), for: .normal)
            self.isHomeButtonHidden = isHomeButtonHidden
            self.isSignoutButtonHidden = isSignoutButtonHidden
        }else{
            
            backButton.isHidden = isBackButtonHidden
            if(isBackButtonHidden == true){
                headerLogoLeadingConstraint.constant = -35
            }else{
                headerLogoLeadingConstraint.constant = 0
            }
            signoutButton.isHidden = isSignoutButtonHidden
            
            if(Customer.sharedInstance.appV == nil){
                signoutButton.isHidden = true
            }
        }
        
        self.delegate = senderVC
    
    }
    
    func loadViewFromNib() -> UIView
    {
        let bundle = Bundle(for: type(of: self))
        
        var nib = UINib()
        
        switch UIDevice.current.userInterfaceIdiom {
        case .pad:
            nib = UINib(nibName: "HeaderBarView_iPad", bundle: bundle)
            
        case .phone:
            nib = UINib(nibName: "HeaderBarView", bundle: bundle)
        default:
            break
        }

        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }
    
    // MARK: IBAction
    @IBAction func actHeaderBtn(_ sender: UIButton) {
        
        var buttonPressedType = HEADER_BAR_BUTTON_PRESSED.HEADER_BUTTON_NONE
        if sender.tag == 1 {
            buttonPressedType = HEADER_BAR_BUTTON_PRESSED.HEADER_BACK_BUTTON
        }
        else if sender.tag == 2 {
            buttonPressedType = HEADER_BAR_BUTTON_PRESSED.HEADER_HOME_BUTTON
        }
        else if sender.tag == 3 {
            if(isSignoutButtonHidden == true && isHomeButtonHidden == false){
                buttonPressedType = HEADER_BAR_BUTTON_PRESSED.HEADER_HOME_BUTTON
            }else{
                buttonPressedType = HEADER_BAR_BUTTON_PRESSED.HEADER_SIGNOUT_BUTTON
            }
            
        }
        delegate?.actHeaderBtn(buttonPressedType)
    }
    
}
