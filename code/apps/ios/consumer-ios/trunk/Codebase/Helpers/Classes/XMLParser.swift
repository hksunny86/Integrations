

//
//  XMLParser.swift
//  Timepey
//
//  Created by Adnan Ahmed on 21/06/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation
import UIKit
import AEXML

class XMLParser: NSObject {
    
    
    
    class func loginXMLParsing(_ data: Data)-> (XMLError, XMLMessage, LoginStatus: Bool){
        
        let userDefault = UserDefaults.standard
        let msgObj = XMLMessage()
        let errObj = XMLError()
        var LoginUser: Bool = false
        
        do {
            let xmlDoc = try AEXMLDocument(xml: data)
            if(xmlDoc.root.children.first?.name == "mesgs"){
                if let mesgs = xmlDoc.root["mesgs"]["mesg"].all {
                    for msg in mesgs {
                        msgObj.parseXML(msg)
                    }
                }
                
            }else if(xmlDoc.root.children.first?.name == "errors"){
                if let errors = xmlDoc.root["errors"]["error"].all {
                    for err in errors {
                        errObj.parseXML(err)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "params"){
                
                //Customer XML
                if let params = xmlDoc.root[Constants.ROOT_PARAM_KEY][Constants.CHILD_PARAM_KEY].all {
                    var dict = [String: String]()
                    for param in params {
                        
                        var attributeName: String = ""
                        var valueOfParam: String = ""
                        
                        if let name = param.attributes["name"] {
                            attributeName = name
                        }
                        if let paramValue = param.value {
                            valueOfParam = paramValue
                        }
                        
                        dict[attributeName] = valueOfParam
                        
                    }
                    
                    Customer.sharedInstance.addParseResults(dict)
                    LoginUser = true
                }
                
                var addsArray = [URL]()
                for ads in xmlDoc.root.children {
                    if ads.name == "ads" {
                        if let mesgs = xmlDoc.root["ads"]["ad"].all {
                            
                            for msg in mesgs {
                                
                                let imageNam = (msg.attributes["name"] != nil) ? msg.attributes["name"]! : ""
                                let imageName = "\(Constants.ServerConfig.BASE_URL)/images/ads/\(imageNam)"
                                let url = URL(string: imageName)
                                print(url ?? "")
                                addsArray.append(url!)
                            }
                            
                            let data = NSKeyedArchiver.archivedData(withRootObject: addsArray)
                            userDefault.set(data, forKey: "ads")
                            userDefault.synchronize()
                            //print(addsArray)
                        }
                    }
                }
                //Category XML
                if((xmlDoc.root["cat"].attributes["version"]) != nil){
                    let catVersion = xmlDoc.root["cat"].attributes["version"]
                    userDefault.removeObject(forKey: "catelog")
                    userDefault.set(catVersion, forKey: "catVersion")
                }
                
                if let categories = xmlDoc.root["cat"][Constants.CATEGORIES_KEY][Constants.CATEGORY_KEY].all {
                    
                    var catelogArray = [Category]()
                    for category in categories {
                        let categoryObj = Category()
                        categoryObj.parseCategoryXML(category)
                        catelogArray.append(categoryObj)
                    }
                    
                    let data = NSKeyedArchiver.archivedData(withRootObject: catelogArray)
                    userDefault.set(data, forKey: "catelog")
                    userDefault.synchronize()
                }else{
                    
                }
                //Banks XML
                if let banks = xmlDoc.root["banks"]["bank"].all {
                    for bank in banks {
                        let bankObj = Bank()
                        bankObj.parseBankXML(bank)
                        Customer.sharedInstance.bank = bankObj
                    }
                    userDefault.set(Customer.sharedInstance.balf!, forKey: "BAL")
                }
            }
            
            
            
        }
        catch {
            //print("\(error)")
            errObj.code = "9007"
            errObj.level = "3"
            errObj.msg = Constants.Message.UNKNOWN_SERVER_ERROR
        }
        
        return (errObj, msgObj, LoginUser)
    }
    
    class func PaymentPurposeParsing(_ data: Data) -> (XMLError, XMLMessage, [TPURPS]) {
        
        var transactionPurposeArray = [TPURPS]()
        let msgObj = XMLMessage()
        let errObj = XMLError()
        do {
            let xmlDoc = try AEXMLDocument(xml: data)
            
            if(xmlDoc.root.children.first?.name == "mesgs"){
                if let mesgs = xmlDoc.root["mesgs"]["mesg"].all {
                    for msg in mesgs {
                        msgObj.parseXML(msg)
                    }
                }
            } else if(xmlDoc.root.children.first?.name == "errors"){
                if let errors = xmlDoc.root["errors"]["error"].all {
                    for err in errors {
                        errObj.parseXML(err)
                    }
                }
            } else if let tpurps = xmlDoc.root["paymentreasons"]["paymentreason"].all {
                for tpurp in tpurps {
                    let obj = TPURPS()
                    obj.parseTpurpsXML(tpurp)
                    transactionPurposeArray.append(obj)
                }
                
            }
            
        }
        catch {
            errObj.code = "9007"
            errObj.level = "3"
            errObj.msg = Constants.Message.UNKNOWN_SERVER_ERROR
            
        }
        return (errObj, msgObj, transactionPurposeArray)
    }
    
    class func mBanksXMLParsing(_ data: Data) -> (XMLError, XMLMessage, [Bank]) {
        
        var mBanksArray = [Bank]()
        let msgObj = XMLMessage()
        let errObj = XMLError()
        
        do {
            let xmlDoc = try AEXMLDocument(xml: data)
            
            if(xmlDoc.root.children.first?.name == "mesgs"){
                if let mesgs = xmlDoc.root["mesgs"]["mesg"].all {
                    for msg in mesgs {
                        msgObj.parseXML(msg)
                    }
                }
            } else if(xmlDoc.root.children.first?.name == "errors"){
                if let errors = xmlDoc.root["errors"]["error"].all {
                    for err in errors {
                        errObj.parseXML(err)
                    }
                }
            } else if let mbanks = xmlDoc.root["memberbanks"]["bank"].all {
                for mbank in mbanks{
                    let bankObj = Bank()
                    bankObj.parseMemberBankXML(mbank)
                    mBanksArray.append(bankObj)
                }
            }
        }
        catch {
            //print("\(error)")
            errObj.code = "9007"
            errObj.level = "3"
            errObj.msg = Constants.Message.UNKNOWN_SERVER_ERROR
        }
        
        return (errObj, msgObj, mBanksArray)
        
        
    }
    class func customerSignoutXMLParsing(_ data: Data)-> (XMLError, XMLMessage, String){
        
        var responseString: String = ""
        let msgObj = XMLMessage()
        let errObj = XMLError()
        
        do {
            let xmlDoc = try AEXMLDocument(xml: data)
            //print(xmlDoc.root.children.first?.name)
            //Customer XML
            if(xmlDoc.root.children.first?.name == "mesgs"){
                if let mesgs = xmlDoc.root["mesgs"]["mesg"].all {
                    for msg in mesgs {
                        msgObj.parseXML(msg)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "errors"){
                if let errors = xmlDoc.root["errors"]["error"].all {
                    for err in errors {
                        errObj.parseXML(err)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "params"){
                if let params = xmlDoc.root["params"]["param"].all {
                    
                    for param in params {
                        
                        if let name = param.attributes["name"] {
                            responseString = name
                        }
                    }
                }
            }
        }
        catch {
            //print("\(error)")
            errObj.code = "9007"
            errObj.level = "3"
            errObj.msg = Constants.Message.UNKNOWN_SERVER_ERROR
        }
        
        return (errObj, msgObj, responseString)
    }
    
    class func miniStatementXMLParsing(_ data: Data)-> (XMLError, XMLMessage, [MiniStatement]){
        var statementArray = [MiniStatement]()
        let msgObj = XMLMessage()
        let errObj = XMLError()
        do {
            let xmlDoc = try AEXMLDocument(xml: data)
            //print(xmlDoc.root.children.first?.name)
            //Customer XML
            if(xmlDoc.root.children.first?.name == "mesgs"){
                if let mesgs = xmlDoc.root["mesgs"]["mesg"].all {
                    for msg in mesgs {
                        msgObj.parseXML(msg)
                    }
                }else if(xmlDoc.root.children.first?.name == "errors"){
                    if let errors = xmlDoc.root["errors"]["error"].all {
                        for err in errors {
                            errObj.parseXML(err)
                        }
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "errors"){
                if let errors = xmlDoc.root["errors"]["error"].all {
                    for err in errors {
                        errObj.parseXML(err)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "trans"){
                if let trans = xmlDoc.root["trans"]["trn"].all {
                    for trn in trans {
                        let statementObj = MiniStatement()
                        statementObj.parseXML(trn)
                        statementArray.append(statementObj)
                    }
                }
            }
        }
        catch {
            //print("\(error)")
            errObj.code = "9007"
            errObj.level = "3"
            errObj.msg = Constants.Message.UNKNOWN_SERVER_ERROR
        }
        
        return (errObj, msgObj, statementArray)
    }
    
    class func balanceEnquiryXMLParsing(_ data: Data)-> (XMLError, XMLMessage, [String:String]){
        
        var dict = [String: String]()
        let msgObj = XMLMessage()
        let errObj = XMLError()
        
        do {
            let xmlDoc = try AEXMLDocument(xml: data)
            //Customer XML
            
            if(xmlDoc.root.children.first?.name == "mesgs"){
                if let mesgs = xmlDoc.root["mesgs"]["mesg"].all {
                    for msg in mesgs {
                        msgObj.parseXML(msg)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "errors"){
                if let errors = xmlDoc.root["errors"]["error"].all {
                    for err in errors {
                        errObj.parseXML(err)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "params"){
                
                if let params = xmlDoc.root["params"]["param"].all {
                    
                    for param in params {
                        //var dict = [String: String]()
                        var attributeName: String = ""
                        var valueOfParam: String = ""
                        
                        if let name = param.attributes["name"] {
                            attributeName = name
                        }
                        if let paramValue = param.value {
                            valueOfParam = paramValue
                        }
                        
                        dict[attributeName] = valueOfParam
                        //array.append(dict)
                    }
                    
                }
            }
        }
        catch {
            //print("\(error)")
            errObj.code = "9007"
            errObj.level = "3"
            errObj.msg = Constants.Message.UNKNOWN_SERVER_ERROR
        }
        
        return (errObj, msgObj, dict)
    }
    
    class func changePINXMLParsing(_ data: Data)-> (XMLError, XMLMessage){
        let msgObj = XMLMessage()
        let errObj = XMLError()
        
        do {
            let xmlDoc = try AEXMLDocument(xml: data)
            //Customer XML
            
            if(xmlDoc.root.children.first?.name == "mesgs"){
                if let mesgs = xmlDoc.root["mesgs"]["mesg"].all {
                    for msg in mesgs {
                        msgObj.parseXML(msg)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "errors"){
                if let errors = xmlDoc.root["errors"]["error"].all {
                    for err in errors {
                        errObj.parseXML(err)
                    }
                }
            }
        }
        catch {
            //print("\(error)")
            errObj.code = "9007"
            errObj.level = "3"
            errObj.msg = Constants.Message.UNKNOWN_SERVER_ERROR
        }
        
        return (errObj, msgObj)
    }
    
    class func cashWithdrawalInfoXMLParsing(_ data: Data)-> (XMLError, XMLMessage, [String:String]){
        
        var dict = [String: String]()
        let msgObj = XMLMessage()
        let errObj = XMLError()
        
        do {
            let xmlDoc = try AEXMLDocument(xml: data)
            //print(xmlDoc.root.children.first?.name)
            //Customer XML
            if(xmlDoc.root.children.first?.name == "mesgs"){
                if let mesgs = xmlDoc.root["mesgs"]["mesg"].all {
                    for msg in mesgs {
                        msgObj.parseXML(msg)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "errors"){
                if let errors = xmlDoc.root["errors"]["error"].all {
                    for err in errors {
                        errObj.parseXML(err)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "params"){
                if let params = xmlDoc.root["params"]["param"].all {
                    
                    for param in params {
                        
                        var attributeName: String = ""
                        var valueOfParam: String = ""
                        
                        if let name = param.attributes["name"] {
                            attributeName = name
                            if let paramValue = param.value {
                                valueOfParam = paramValue
                            }
                        }
                        dict[attributeName] = valueOfParam
                    }
                }
            }
        }
        catch {
            //print("\(error)")
            errObj.code = "9007"
            errObj.level = "3"
            errObj.msg = Constants.Message.UNKNOWN_SERVER_ERROR
        }
        
        return (errObj, msgObj, dict)
    }
    
    class func cashWithdrawalCheckoutXMLParsing(_ data: Data)-> (XMLError, XMLMessage, [String:String]){
        
        var statementArray = [String:String]()
        let msgObj = XMLMessage()
        let errObj = XMLError()
        
        do {
            let xmlDoc = try AEXMLDocument(xml: data)
            //print(xmlDoc.root.children.first?.name)
            //Customer XML
            if(xmlDoc.root.children.first?.name == "mesgs"){
                if let mesgs = xmlDoc.root["mesgs"]["mesg"].all {
                    for msg in mesgs {
                        msgObj.parseXML(msg)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "errors"){
                if let errors = xmlDoc.root["errors"]["error"].all {
                    for err in errors {
                        errObj.parseXML(err)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "trans"){
                if let trans = xmlDoc.root["trans"]["trn"].all {
                    for trn in trans {
                        
                        statementArray = trn.attributes
                    }
                }
            }
        }
        catch {
            //print("\(error)")
            errObj.code = "9007"
            errObj.level = "3"
            errObj.msg = Constants.Message.UNKNOWN_SERVER_ERROR
        }
        
        return (errObj, msgObj, statementArray)
    }
    
    class func moneyTransferInfoXMLParsing(_ data: Data)-> (XMLError, XMLMessage, [String:String]){
        
        var receiptDict = [String:String]()
        let msgObj = XMLMessage()
        let errObj = XMLError()
        
        do {
            let xmlDoc = try AEXMLDocument(xml: data)
            //print(xmlDoc.root.children.first?.name)
            //Customer XML
            if(xmlDoc.root.children.first?.name == "mesgs"){
                if let mesgs = xmlDoc.root["mesgs"]["mesg"].all {
                    for msg in mesgs {
                        msgObj.parseXML(msg)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "errors"){
                if let errors = xmlDoc.root["errors"]["error"].all {
                    for err in errors {
                        errObj.parseXML(err)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "params"){
                if let params = xmlDoc.root["params"]["param"].all {
                    
                    for param in params {
                        
                        var attributeName: String = ""
                        var valueOfParam: String = ""
                        
                        if let name = param.attributes["name"] {
                            attributeName = name
                            if let paramValue = param.value {
                                valueOfParam = paramValue
                            }
                        }
                        receiptDict[attributeName] = valueOfParam
                    }
                }
            }
        }
        catch {
            //print("\(error)")
            errObj.code = "9007"
            errObj.level = "3"
            errObj.msg = Constants.Message.UNKNOWN_SERVER_ERROR
        }
        
        return (errObj, msgObj, receiptDict)
    }
    
    class func moneyTransferCheckoutXMLParsing(_ data: Data)-> (XMLError, XMLMessage, [String:String]){
        
        var statementArray = [String:String]()
        let msgObj = XMLMessage()
        let errObj = XMLError()
        
        do {
            let xmlDoc = try AEXMLDocument(xml: data)
            //print(xmlDoc.root.children.first?.name)
            //Customer XML
            if(xmlDoc.root.children.first?.name == "mesgs"){
                if let mesgs = xmlDoc.root["mesgs"]["mesg"].all {
                    for msg in mesgs {
                        msgObj.parseXML(msg)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "errors"){
                if let errors = xmlDoc.root["errors"]["error"].all {
                    for err in errors {
                        errObj.parseXML(err)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "trans"){
                if let trans = xmlDoc.root["trans"]["trn"].all {
                    for trn in trans {
                        
                        statementArray = trn.attributes
                    }
                }
            }
        }
        catch {
            //print("\(error)")
            errObj.code = "9007"
            errObj.level = "3"
            errObj.msg = Constants.Message.UNKNOWN_SERVER_ERROR
        }
        
        return (errObj, msgObj, statementArray)
    }
    
    class func retailPaymentInfoXMLParsing(_ data: Data)-> (XMLError, XMLMessage, [String:String]){
        
        var receiptDict = [String:String]()
        let msgObj = XMLMessage()
        let errObj = XMLError()
        
        do {
            let xmlDoc = try AEXMLDocument(xml: data)
            //print(xmlDoc.root.children.first?.name)
            //Customer XML
            if(xmlDoc.root.children.first?.name == "mesgs"){
                if let mesgs = xmlDoc.root["mesgs"]["mesg"].all {
                    for msg in mesgs {
                        msgObj.parseXML(msg)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "errors"){
                if let errors = xmlDoc.root["errors"]["error"].all {
                    for err in errors {
                        errObj.parseXML(err)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "params"){
                if let params = xmlDoc.root["params"]["param"].all {
                    
                    for param in params {
                        
                        var attributeName: String = ""
                        var valueOfParam: String = ""
                        
                        if let name = param.attributes["name"] {
                            attributeName = name
                            if let paramValue = param.value {
                                valueOfParam = paramValue
                            }
                        }
                        receiptDict[attributeName] = valueOfParam
                    }
                }
            }
        }
        catch {
            //print("\(error)")
            errObj.code = "9007"
            errObj.level = "3"
            errObj.msg = Constants.Message.UNKNOWN_SERVER_ERROR
        }
        
        return (errObj, msgObj, receiptDict)
    }
    
    class func retailPaymentCheckoutXMLParsing(_ data: Data)-> (XMLError, XMLMessage, [String:String]){
        
        var statementArray = [String:String]()
        let msgObj = XMLMessage()
        let errObj = XMLError()
        
        do {
            let xmlDoc = try AEXMLDocument(xml: data)
            //print(xmlDoc.root.children.first?.name)
            //Customer XML
            if(xmlDoc.root.children.first?.name == "mesgs"){
                if let mesgs = xmlDoc.root["mesgs"]["mesg"].all {
                    for msg in mesgs {
                        msgObj.parseXML(msg)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "errors"){
                if let errors = xmlDoc.root["errors"]["error"].all {
                    for err in errors {
                        errObj.parseXML(err)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "trans"){
                if let trans = xmlDoc.root["trans"]["trn"].all {
                    for trn in trans {
                        
                        statementArray = trn.attributes
                    }
                }
            }
        }
        catch {
            //print("\(error)")
            errObj.code = "9007"
            errObj.level = "3"
            errObj.msg = Constants.Message.UNKNOWN_SERVER_ERROR
        }
        
        return (errObj, msgObj, statementArray)
    }
    
    class func pinVerificationXMLParsing(_ data: Data)-> (XMLError, XMLMessage, [String:String]){
        
        var receiptDict = [String:String]()
        let msgObj = XMLMessage()
        let errObj = XMLError()
        
        do {
            let xmlDoc = try AEXMLDocument(xml: data)
            //print(xmlDoc.root.children.first?.name)
            //Customer XML
            if(xmlDoc.root.children.first?.name == "mesgs"){
                if let mesgs = xmlDoc.root["mesgs"]["mesg"].all {
                    for msg in mesgs {
                        msgObj.parseXML(msg)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "errors"){
                if let errors = xmlDoc.root["errors"]["error"].all {
                    for err in errors {
                        errObj.parseXML(err)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "params"){
                if let params = xmlDoc.root["params"]["param"].all {
                    
                    for param in params {
                        
                        var attributeName: String = ""
                        var valueOfParam: String = ""
                        
                        if let name = param.attributes["name"] {
                            attributeName = name
                            if let paramValue = param.value {
                                valueOfParam = paramValue
                            }
                        }
                        receiptDict[attributeName] = valueOfParam
                    }
                }
            }
        }
        catch {
            //print("\(error)")
            errObj.code = "9007"
            errObj.level = "3"
            errObj.msg = Constants.Message.UNKNOWN_SERVER_ERROR
        }
        
        return (errObj, msgObj, receiptDict)
    }
    
    class func paramTypeXMLParsing(_ data: Data)-> (XMLError, XMLMessage, [String:String]){
        
        var dict = [String: String]()
        let msgObj = XMLMessage()
        let errObj = XMLError()
        
        do {
            let xmlDoc = try AEXMLDocument(xml: data)
            //Customer XML
            
            if(xmlDoc.root.children.first?.name == "mesgs"){
                if let mesgs = xmlDoc.root["mesgs"]["mesg"].all {
                    for msg in mesgs {
                        msgObj.parseXML(msg)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "errors"){
                if let errors = xmlDoc.root["errors"]["error"].all {
                    for err in errors {
                        errObj.parseXML(err)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "params"){
                
                if let params = xmlDoc.root["params"]["param"].all {
                    
                    for param in params {
                        var attributeName: String = ""
                        var valueOfParam: String = ""
                        
                        if let name = param.attributes["name"] {
                            attributeName = name
                        }
                        if let paramValue = param.value {
                            valueOfParam = paramValue
                        }
                        
                        dict[attributeName] = valueOfParam
                    }
                    
                }
            }
        }
        catch {
            //print("\(error)")
            errObj.code = "9007"
            errObj.level = "3"
            errObj.msg = Constants.Message.UNKNOWN_SERVER_ERROR
        }
        
        return (errObj, msgObj, dict)
    }
    
    class func transTypeXMLParsing(_ data: Data)-> (XMLError, XMLMessage, [String:String]){
        
        var statementArray = [String:String]()
        let msgObj = XMLMessage()
        let errObj = XMLError()
        
        do {
            let xmlDoc = try AEXMLDocument(xml: data)
            //print(xmlDoc.root.children.first?.name)
            //Customer XML
            if(xmlDoc.root.children.first?.name == "mesgs"){
                if let mesgs = xmlDoc.root["mesgs"]["mesg"].all {
                    for msg in mesgs {
                        msgObj.parseXML(msg)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "errors"){
                if let errors = xmlDoc.root["errors"]["error"].all {
                    for err in errors {
                        errObj.parseXML(err)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "trans"){
                if let trans = xmlDoc.root["trans"]["trn"].all {
                    for trn in trans {
                        
                        statementArray = trn.attributes
                    }
                }
            }
        }
        catch {
            //print("\(error)")
            errObj.code = "9007"
            errObj.level = "3"
            errObj.msg = Constants.Message.UNKNOWN_SERVER_ERROR
        }
        
        return (errObj, msgObj, statementArray)
    }
    
    class func faqsXMLParsing(_ data: Data)-> (XMLError, XMLMessage, [[String:String]]){
        
        var array = [[String: String]]()
        let msgObj = XMLMessage()
        let errObj = XMLError()
        
        do {
            let xmlDoc = try AEXMLDocument(xml: data)
            //Customer XML
            
            if(xmlDoc.root.children.first?.name == "mesgs"){
                if let mesgs = xmlDoc.root["mesgs"]["mesg"].all {
                    for msg in mesgs {
                        msgObj.parseXML(msg)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "errors"){
                if let errors = xmlDoc.root["errors"]["error"].all {
                    for err in errors {
                        errObj.parseXML(err)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "faqs"){
                
                if let params = xmlDoc.root["faqs"]["faq"].all {
                    
                    for param in params {
                        var dict = [String: String]()
                        var attributeName: String = ""
                        var valueOfParam: String = ""
                        
                        if let name = param.attributes["question"] {
                            attributeName = name
                        }
                        if let paramValue = param.value {
                            valueOfParam = paramValue
                        }
                        
                        dict[attributeName] = valueOfParam
                        array.append(dict)
                    }
                    
                }
            }
        }
        catch {
            //print("\(error)")
            errObj.code = "9007"
            errObj.level = "3"
            errObj.msg = Constants.Message.UNKNOWN_SERVER_ERROR
        }
        
        return (errObj, msgObj, array)
    }
    
    class func branchATMLocatorXMLParsing(_ data: Data)-> (XMLError, XMLMessage, [BranchLocator], String){
        
        var array = [BranchLocator]()
        let msgObj = XMLMessage()
        let errObj = XMLError()
        var totalCount = String()
        
        do {
            let xmlDoc = try AEXMLDocument(xml: data)
            //Customer XML
            
            if(xmlDoc.root.children.first?.name == "mesgs"){
                if let mesgs = xmlDoc.root["mesgs"]["mesg"].all {
                    for msg in mesgs {
                        msgObj.parseXML(msg)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "errors"){
                if let errors = xmlDoc.root["errors"]["error"].all {
                    for err in errors {
                        errObj.parseXML(err)
                    }
                }
            }else{
                
                if let params = xmlDoc.root[Constants.ROOT_PARAM_KEY][Constants.CHILD_PARAM_KEY].all {
                    for param in params {
                        if let paramValue = param.value {
                            totalCount = paramValue
                        }else{
                            totalCount = "0"
                        }
                        
                    }
                }
                
                if let locations = xmlDoc.root["locations"]["location"].all {
                    
                    for location in locations {
                        let branchLocatorObj = BranchLocator()
                        branchLocatorObj.parseXML(location)
                        array.append(branchLocatorObj)
                    }
                }
            }
            
            
            
        }
        catch {
            //print("\(error)")
            errObj.code = "9007"
            errObj.level = "3"
            errObj.msg = Constants.Message.UNKNOWN_SERVER_ERROR
        }
        
        return (errObj, msgObj, array, totalCount)
    }
    
    class func merchantTranferQRXMLParsing(_ data: Data)-> (XMLError, XMLMessage, [retailMerchant]){
        
        var retailMerchantArray = [retailMerchant]()
        let msgObj = XMLMessage()
        let errObj = XMLError()
        
        do {
            let xmlDoc = try AEXMLDocument(xml: data)
            //print(xmlDoc.root.children.first?.name)
            //Customer XML
            if(xmlDoc.root.children.first?.name == "mesgs"){
                if let mesgs = xmlDoc.root["mesgs"]["mesg"].all {
                    for msg in mesgs {
                        msgObj.parseXML(msg)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "errors"){
                if let errors = xmlDoc.root["errors"]["error"].all {
                    for err in errors {
                        errObj.parseXML(err)
                    }
                }
            }else if(xmlDoc.root.children.first?.name == "trans"){
                if let trans = xmlDoc.root["trans"]["trn"].all {
                    
                    for trn in trans {
                        
                        let retailMerchantObj = retailMerchant()
                        retailMerchantObj.parseMerchantTransferResponse(trn)
                        retailMerchantArray.append(retailMerchantObj)
                    }
                }
            }
        }
        catch {
            //print("\(error)")
            errObj.code = "9007"
            errObj.level = "3"
            errObj.msg = Constants.Message.UNKNOWN_SERVER_ERROR
        }
        
        return (errObj, msgObj, retailMerchantArray)
    }
}
