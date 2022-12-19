//
//  Encryption.swift
//  Timepey
//
//  Created by Adnan Ahmed on 30/06/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation
import CryptoSwift
import Security


struct RSA {

static func encrypt(string: String, publicKey: String?) -> String? {
    guard let publicKey = publicKey else { return nil }

    let keyString = publicKey.replacingOccurrences(of: "-----BEGIN RSA PUBLIC KEY-----\n", with: "").replacingOccurrences(of: "\n-----END RSA PUBLIC KEY-----", with: "")
    guard let data = Data(base64Encoded: keyString) else { return nil }

    var attributes: CFDictionary {
        return [kSecAttrKeyType         : kSecAttrKeyTypeRSA,
                kSecAttrKeyClass        : kSecAttrKeyClassPublic,
                kSecAttrKeySizeInBits   : 2048,
                kSecReturnPersistentRef : true] as CFDictionary
    }

    var error: Unmanaged<CFError>? = nil
    if #available(iOS 10.0, *) {
        guard let secKey = SecKeyCreateWithData(data as CFData, attributes, &error) else {
            print(error.debugDescription)
            return nil
        }
        return encrypt(string: string, publicKey: secKey)
    } else {
        return encrypt(string: string, publicKey: "")
    }

}

static func encrypt(string: String, publicKey: SecKey) -> String? {
    let buffer = [UInt8](string.utf8)

    var keySize   = SecKeyGetBlockSize(publicKey)
    var keyBuffer = [UInt8](repeating: 0, count: keySize)

    // Encrypto  should less than key length
    guard SecKeyEncrypt(publicKey, SecPadding.PKCS1, buffer, buffer.count, &keyBuffer, &keySize) == errSecSuccess else { return nil }
    return Data(bytes: keyBuffer, count: keySize).base64EncodedString()
   }
}


extension String {
    
    func escape(string: String) -> String {
        let allowedCharacters = string.addingPercentEncoding(withAllowedCharacters: CharacterSet(charactersIn: "+:=\"#%/<>?@\\^`{|}").inverted) ?? ""
        return allowedCharacters
    }
    
    func Unescape(string: String) -> String {
        let decodedString = string.removingPercentEncoding
        return decodedString ?? ""
    }
    
    func aesEncrypt(_ val: [UInt8], iv: String) throws -> String {
        
        let value = SSCheck().reveal(key: val)
        
        let data = self.data(using: String.Encoding.utf8)
        let enc = try AES(key: value, iv: iv, blockMode:.ECB).encrypt(ArraySlice(data!))
        let encData = Data(bytes: UnsafePointer<UInt8>(enc), count: Int(enc.count))
        let base64String: String = encData.base64EncodedString(options: NSData.Base64EncodingOptions(rawValue: 0));
        let result = String(base64String)
        return result
    }
    
    func aesDecrypt(_ val: [UInt8], iv: String) throws -> String {
        
        
        let value = SSCheck().reveal(key: val)
        guard let data = Data(base64Encoded: self, options: NSData.Base64DecodingOptions(rawValue: 0)) else {
            return Constants.Message.GENERAL_SERVER_ERROR    }
        
        let  dec = try AES(key: value, iv: iv, blockMode:.ECB).decrypt(ArraySlice(data))
        let decData = Data(bytes: UnsafePointer<UInt8>(dec), count: Int(dec.count))
        let result = NSString(data: decData, encoding: String.Encoding.utf8.rawValue)
        return String(result!)
    }
    
    
}

