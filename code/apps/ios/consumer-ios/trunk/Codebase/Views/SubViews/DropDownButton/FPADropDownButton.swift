//
//  FPADropDownButton.swift
//  FalconApp
//
//  Created by Maria Alvi on 4/27/17.
//  Copyright © 2017 Wateen. All rights reserved.
//

import Foundation

import UIKit



@objc class FPADropDownButton: UIButton {

    //var borderColor = UIColor(red: 0.0/255.0, green: 0.0/255.0, blue: 0.0/255.0, alpha: 0.3)
    var textColor = UIColor(red: 100.0/255.0, green: 100.0/255.0, blue: 100.0/255.0, alpha: 1000)
    let paddingValue :CGFloat = 4
    var imageViewWidth : CGFloat = 0
    
    
    override func awakeFromNib() {
        
        super.awakeFromNib()
        
        self.layoutIfNeeded()
        self.layer.cornerRadius = 5
        self.layer.borderWidth = 1
        self.layer.backgroundColor = UIColor.colorWithRGB(rgbValue: 0xFFFFFF, alpha: 1).cgColor
        self.layer.borderColor = UIColor.colorWithRGB(rgbValue: 0x000000, alpha: 0.3).cgColor
        self.setTitleColor(UIColor.colorWithRGB(rgbValue: 0x000000, alpha: 1), for: .normal)
        self.setTitleColor(UIColor.colorWithRGB(rgbValue: 0x000000, alpha: 0.5), for: .highlighted)
        self.titleLabel?.lineBreakMode = .byTruncatingTail
        self.isOpaque = true
        self.imageView?.contentMode = .scaleAspectFit
        
    }
    
    //    required init?(coder aDecoder: NSCoder) {
    //        super.init(coder: aDecoder)
    //
    //    }
    //
    //    override init(frame: CGRect) {
    //
    //        super.init(frame: frame)
    //    }
    
    
    override func titleRect(forContentRect contentRect: CGRect) -> CGRect {
        var rect = super.titleRect(forContentRect: contentRect)
        debugPrint(imageViewWidth)
        rect.origin.x = 0 + paddingValue
        rect.size.width = contentRect.maxX - imageViewWidth - paddingValue * 2
        
        return rect
        
    }
    
    override func imageRect(forContentRect contentRect: CGRect) -> CGRect {
        var rect = super.imageRect(forContentRect: contentRect)
        
        
        imageViewWidth = rect.size.width
        if imageViewWidth > self.bounds.size.height {
            imageViewWidth = self.bounds.size.height
        }
        
        rect.origin.x = contentRect.maxX - imageViewWidth  - paddingValue
        rect.size.width = imageViewWidth
        
        
        
        return rect
    }
    
    
    func setTitle(titleString: String){
       
        self.setTitle(titleString ,for: .normal)
    
    }
    
    func  setImage(image: UIImage)  {

        self.setImage( image, for: .normal)
    }
    
}
