//
//  FPMBottomBorderTextField.swift
//  JSBL-MB
//
//  Created by Uzair on 8/29/17.
//  Copyright © 2017 inov8. All rights reserved.
//

import Foundation
import UIKit


@IBDesignable class FPMBottomBorderTextField : UITextField {
    var bottomStokeView : UIView = UIView()
    @IBInspectable var bottomBarColor: UIColor = UIColor.lightGray
    fileprivate var bottomStokeViewHeight: NSLayoutConstraint?
    
    override func awakeFromNib() {
        bottomStokeView.translatesAutoresizingMaskIntoConstraints = false
        bottomStokeView.backgroundColor = bottomBarColor
        self.addSubview(bottomStokeView)
        
        if #available(iOS 9.0, *) {
            // use the feature only available in iOS 9
            // Configure Constraints
            bottomStokeView.bottomAnchor.constraint(equalTo: self.bottomAnchor, constant: 0.0).isActive = true
            bottomStokeView.leadingAnchor.constraint(equalTo: self.leadingAnchor, constant: 0.0).isActive = true
            bottomStokeView.trailingAnchor.constraint(equalTo: self.trailingAnchor, constant: 0.0).isActive = true
            bottomStokeViewHeight = bottomStokeView.heightAnchor.constraint(equalToConstant: 1)
            bottomStokeViewHeight?.isActive = true
        }
    }
    override func prepareForInterfaceBuilder() {
        super.prepareForInterfaceBuilder()
        self.layoutIfNeeded()
    }
    
    func focusedHeight() {
        if isEditing {
            if #available(iOS 9.0, *) {
                // Configure Constraints
                UIView.animate(withDuration: 0.5, delay: 0, usingSpringWithDamping: 0.5, initialSpringVelocity: 0.5, options: .curveEaseOut, animations: {
                    self.bottomStokeViewHeight?.constant = 2
                    self.bottomStokeView.layoutIfNeeded()
                }, completion: nil)
                
            }
        }
    }
    
    func normalHeight() {
        if #available(iOS 9.0, *) {
            // Configure Constraints
            UIView.animate(withDuration: 0.5, delay: 0, usingSpringWithDamping: 0.5, initialSpringVelocity: 0.5, options: .curveEaseIn, animations: {
                self.bottomStokeViewHeight?.constant = 1
                self.bottomStokeView.layoutIfNeeded()
            }, completion: nil)
        }
    }
}
