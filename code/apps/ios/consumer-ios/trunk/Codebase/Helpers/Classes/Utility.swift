//
//  Utilities.swift
//  JSBL-BB
//
//  Created by Adnan Ahmed on 19/09/2017.
//  Copyright © 2017 Inov8. All rights reserved.
//

import Foundation
import UIKit
import IQKeyboardManagerSwift

class Utility{
    
    class func resize(image:UIImage) -> UIImage? {
        
        var actualHeight : CGFloat = image.size.height
        var actualWidth : CGFloat = image.size.width
        let maxHeight : CGFloat = 500
        let maxWidth : CGFloat = 500
        var imgRatio : CGFloat = actualWidth/actualHeight
        let maxRatio : CGFloat = maxWidth/maxHeight
        //        var compressionQuality : CGFloat = 0.5
        
        if (actualHeight > maxHeight || actualWidth > maxWidth){
            if(imgRatio < maxRatio){
                //adjust width according to maxHeight
                imgRatio = maxHeight / actualHeight
                actualWidth = imgRatio * actualWidth
                actualHeight = maxHeight
            }
            else if(imgRatio > maxRatio){
                //adjust height according to maxWidth
                imgRatio = maxWidth / actualWidth
                actualHeight = imgRatio * actualHeight
                actualWidth = maxWidth
            }
            else{
                actualHeight = maxHeight
                actualWidth = maxWidth
                
            }
        }
        
        let rect = CGRect(x: 0.0, y: 0.0, width: actualWidth, height: actualHeight)
        UIGraphicsBeginImageContext(rect.size)
        image.draw(in: rect)
        guard let img = UIGraphicsGetImageFromCurrentImageContext() else {
            return nil
        }
        UIGraphicsEndImageContext()
        return img
    }
    
    class func addShadowToView(view: UIView){
        view.layer.shadowOpacity = 0.5
        view.layer.shadowOffset = CGSize(width: 3.0, height: 2.0)
        view.layer.shadowRadius = 5.0
        view.layer.shadowColor = UIColor.black.cgColor
        view.layer.masksToBounds = false
    }
    
    class func roundButton(btn: UIButton){
        btn.layer.cornerRadius = Constants.UI.Button.CORNER_RADIUS
    }
    
    class func getDate(date: Date, withFormat format: String) -> String {
        let formatter: DateFormatter = DateFormatter()
        formatter.dateFormat = format
        let dateStr: String = formatter.string(from: date as Date)
        return dateStr
    }
    
    class func base64(of image: UIImage) -> String{
        let jpegCompressionQuality: CGFloat = 0.75 // Set this to whatever suits your purpose
        if let base64String = image.jpegData(compressionQuality: jpegCompressionQuality)?.base64EncodedString(options: []) {
            return base64String
        }
        return ""
    }
    class func dismissKeyboard(view:UIView) {
        IQKeyboardManager.shared.shouldResignOnTouchOutside = true
        view.endEditing(true)
    }
    
    class func showLoadingView(view:UIView)
    {
        DispatchQueue.main.async(execute: {
            let applicationDelegate : AppDelegate = UIApplication.shared.delegate as! AppDelegate
            view.addSubview(applicationDelegate.loadingView!)
            view.isUserInteractionEnabled = false
            applicationDelegate.loadingView!.show()
            
        })
    }
    
    class func hideLoadingView(view:UIView)
    {
        DispatchQueue.main.async(execute: {
            let applicationDelegate : AppDelegate = UIApplication.shared.delegate as! AppDelegate
            view.addSubview(applicationDelegate.loadingView!)
            view.isUserInteractionEnabled = true
            applicationDelegate.loadingView!.hide()
        })
    }
    
    class func pushViewController(_ viewController: UIViewController )
    {
        DispatchQueue.main.async(execute: {
            let applicationDelegate : AppDelegate = UIApplication.shared.delegate as! AppDelegate
            let mainViewController :UINavigationController = applicationDelegate.mainNavigationController!
            mainViewController.pushViewController(viewController, animated: true)
            
        })
    }
    
    class func popVCToCustomerLogin(){
        DispatchQueue.main.async(execute: {
            let applicationDelegate : AppDelegate = UIApplication.shared.delegate as! AppDelegate
            for vc: UIViewController in applicationDelegate.mainNavigationController!.viewControllers{
                if(vc is LoginCustomerVC){
                    applicationDelegate.mainNavigationController?.popToViewController(vc, animated: true)
                }
            }
        })
    }
    
    class func serverErrorHandling(response:(XMLError, XMLMessage, [String:String]), parentView:UIView) -> Bool{
        let alertView = AlertView()
        if(response.0.msg != nil){
            if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: response.0.msg!, okButtonPressed: {
                    alertView.hide()
                    Utility.popVCToCustomerLogin()
                })
            }else{
                alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: response.0.msg!, okButtonPressed: {
                    alertView.hide()
                })
            }
            DispatchQueue.main.async(execute: {
                alertView.show(parentView: parentView)
            })
            return false
        }else if(response.1.msg != nil){
            alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: response.1.msg!, okButtonPressed: {
                alertView.hide()
            })
            DispatchQueue.main.async(execute: {
                alertView.show(parentView: parentView)
            })
            return false
        }else{
            return true
        }
    }
    
    class func convertDateFormater(_ date: String) -> String
    {
        //print(date)
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        //print(dateFormatter)
        let date = dateFormatter.date(from: date)
        dateFormatter.dateFormat = "dd-MM-yyyy"
        //print(dateFormatter)
        return  dateFormatter.string(from: date!)
        
    }
    
    class func convertDateFormater2(_ date: String) -> String
    {
        //print(date)
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "dd-MM-yyyy"
        //print(dateFormatter)
        let date = dateFormatter.date(from: date)
        dateFormatter.dateFormat = "yyyy-MM-dd"
        //print(dateFormatter)
        return  dateFormatter.string(from: date!)
        
    }
    
    class func isAlphaNumaric(_ string: String)-> Bool{
        let letters = CharacterSet(charactersIn: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz")
        let numbers = CharacterSet(charactersIn: "0123456789")
        var lettersResult: Bool?
        var numbersResult: Bool?
        
        for i in  String(string).utf16  {
            if !letters.contains(UnicodeScalar(i)!) {
                lettersResult = false
            }else{
                lettersResult = true
                break
            }
        }
        for i in  String(string).utf16  {
            if !numbers.contains(UnicodeScalar(i)!) {
                numbersResult = false
            }else{
                numbersResult = true
                break
            }
        }
        
        if(lettersResult == true && numbersResult == true){
            return true
        }else{
            return false
        }
    }
    
    class func isValidPhone(_ email: String) -> Bool {
        
        let emailRegEx = "^((\\+92)|(0092))-{0,1}3{1}\\d{2}-{0,1}\\d{7}$|^0{0,1}3{1}\\d{10}$|^0{0,1}3{1}\\d{2}-\\d{7}$"
        let emailPred = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        return emailPred.evaluate(with: email)
    }
    
    class func addPhoneNoCountryCode(cc: String    = "0", phoneNo: String?) -> String? {
            guard let number = phoneNo else  {
                return nil
            }
            var startString                                = ""
            if (number.starts(with: "0092")) {
                startString                                    = "0092"
            } else if (number.starts(with: "92")) {
                startString                                    = "92"
            } else if (number.starts(with: "+92")) {
                startString                                    = "+92"
            }
        let range = NSRange(location: 0, length: startString.count)
        
        if number.starts(with: "03") {
            return (number as NSString).replacingCharacters(in: range, with: "")
        } else {
        
            return cc + (number as NSString).replacingCharacters(in: range, with: "")
        }
        
    }
    
    class func textfieldInputandLengthCheck(_ text:UITextField,range: NSRange,string: String,characters:String,length:Int)->Bool  {
        
        if range.location == 0 && string == " "
        {
            return false
        }
        
        guard let text = text.text else { return true }
        let newLength = text.count + string.count - range.length
        if newLength > length {
            return false
        }
        
        let cs = NSCharacterSet(charactersIn:characters).inverted
        let filtered = string.components(separatedBy: cs).joined(separator: "")
        return (string == filtered)
        
    }
    
    class func destroyNetworkCache() {
        
        let sharedCache = URLCache(memoryCapacity: 0, diskCapacity: 0, diskPath: nil)
        URLCache.shared = sharedCache
        URLCache.shared.removeAllCachedResponses()
        let caches = NSSearchPathForDirectoriesInDomains(.cachesDirectory, .userDomainMask, true)[0]
        let appID = Bundle.main.infoDictionary!["CFBundleIdentifier"] as? String
        var path = "\(caches)/\(appID ?? "")/Cache.db-wal"
        try? FileManager.default.removeItem(atPath: path)
        path = "\(caches)/\(appID ?? "")/Cache.db"
        try? FileManager.default.removeItem(atPath: path)
        path = "\(caches)/\(appID ?? "")/Cache.db-shm"
        try? FileManager.default.removeItem(atPath: path)
    }
}
