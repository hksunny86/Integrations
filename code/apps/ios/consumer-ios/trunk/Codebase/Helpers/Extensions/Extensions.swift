//
//  Extensions.swift
//  JSBL-BB
//
//  Created by Adnan Ahmed on 19/09/2017.
//  Copyright © 2017 Inov8. All rights reserved.
//

import UIKit
import ObjectiveC

extension UIColor {
    static func colorWithRGB(rgbValue: UInt, alpha: CGFloat) -> UIColor {
        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: alpha
        )
    }
}

extension UIView {
    
    @IBInspectable var cornerRadius: CGFloat {
        get {
            return layer.cornerRadius
        }
        set {
            layer.cornerRadius = newValue
            layer.masksToBounds = newValue > 0
        }
    }
    
    @IBInspectable var borderWidth: CGFloat {
        get {
            return layer.borderWidth
        }
        set {
            layer.borderWidth = newValue
        }
    }
    
    @IBInspectable var borderColor: UIColor? {
        get {
            return UIColor(cgColor: layer.borderColor!)
        }
        set {
            layer.borderColor = newValue?.cgColor
        }
    }
}

// Declare a global var to produce a unique address as the assoc object handle
var disabledColorHandle: UInt8 = 0
var highlightedColorHandle: UInt8 = 0
var selectedColorHandle: UInt8 = 0

extension UIButton {
    // https://stackoverflow.com/questions/14523348/how-to-change-the-background-color-of-a-uibutton-while-its-highlighted
    private func image(withColor color: UIColor) -> UIImage? {
        let rect = CGRect(x: 0.0, y: 0.0, width: 1.0, height: 1.0)
        UIGraphicsBeginImageContext(rect.size)
        let context = UIGraphicsGetCurrentContext()

        context?.setFillColor(color.cgColor)
        context?.fill(rect)

        let image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()

        return image
    }

    func setBackgroundColor(_ color: UIColor, for state: UIControl.State) {
        self.setBackgroundImage(image(withColor: color), for: state)
    }

    @IBInspectable
    var disabledColor: UIColor? {
        get {
            if let color = objc_getAssociatedObject(self, &disabledColorHandle) as? UIColor {
                return color
            }
            return nil
        }
        set {
            if let color = newValue {
                self.setBackgroundColor(color, for: .disabled)
                objc_setAssociatedObject(self, &disabledColorHandle, color, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
            } else {
                self.setBackgroundImage(nil, for: .disabled)
                objc_setAssociatedObject(self, &disabledColorHandle, nil, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
            }
        }
    }

    @IBInspectable
    var highlightedColor: UIColor? {
        get {
            if let color = objc_getAssociatedObject(self, &highlightedColorHandle) as? UIColor {
                return color
            }
            return nil
        }
        set {
            if let color = newValue {
                self.setBackgroundColor(color, for: .highlighted)
                objc_setAssociatedObject(self, &highlightedColorHandle, color, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
            } else {
                self.setBackgroundImage(nil, for: .highlighted)
                objc_setAssociatedObject(self, &highlightedColorHandle, nil, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
            }
        }
    }

    @IBInspectable
    var selectedColor: UIColor? {
        get {
            if let color = objc_getAssociatedObject(self, &selectedColorHandle) as? UIColor {
                return color
            }
            return nil
        }
        set {
            if let color = newValue {
                self.setBackgroundColor(color, for: .selected)
                objc_setAssociatedObject(self, &selectedColorHandle, color, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
            } else {
                self.setBackgroundImage(nil, for: .selected)
                objc_setAssociatedObject(self, &selectedColorHandle, nil, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
            }
        }
    }
}
extension String {

    func stripOutHtml() -> String? {
        do {
            guard let data = self.data(using: .unicode) else {
                return nil
            }
            let attributed = try NSAttributedString(data: data, options: [.documentType: NSAttributedString.DocumentType.html, .characterEncoding: String.Encoding.utf8.rawValue], documentAttributes: nil)
            return attributed.string
        } catch {
            return nil
        }
    }
    var unescaped: String {
           let entities = ["\0", "\t", "\n", "\r", "\"", "\'", "\\"]
           var current = self
           for entity in entities {
               let descriptionCharacters = entity.debugDescription.dropFirst().dropLast()
               let description = String(descriptionCharacters)
               current = current.replacingOccurrences(of: description, with: entity)
           }
           return current
       }
}
