//
//  HeaderTitleView.swift
//  JSBL-BB
//
//  Created by Adnan Ahmed on 19/10/2017.
//  Copyright © 2017 Inov8. All rights reserved.
//

import UIKit

class HeaderTitleView: UIView {

    
    override init (frame: CGRect) {
        super.init(frame: frame)
        setBottomBorder()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setBottomBorder()
    }
    
    func setBottomBorder() {
        self.layer.backgroundColor = UIColor.white.cgColor
        self.layer.masksToBounds = false
        self.layer.shadowColor = UIColor(red:0.00, green:0.28, blue:0.56, alpha:1.0).cgColor
        self.layer.shadowOffset = CGSize(width: 0.0, height: 0.5)
        self.layer.shadowOpacity = 1.0
        self.layer.shadowRadius = 0.0
    }
    

}
