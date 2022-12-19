//
//  APIRequest.swift
//  Timepey
//
//  Created by Adnan Ahmed on 04/10/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation
import UIKit
//import Alamofire

public class APIManager:NSObject, URLSessionDelegate {
    
    class func urlRequest(requestXML: String, onSuccess:@escaping (_ data: Data) ->(),onFailure:@escaping (_ reason: String) ->()){
        
        let eRequest = try! requestXML.aesEncrypt(Constants.AppConfig.M_KEY, iv: "")
        
        let url = URL(string:Constants.ServerConfig.SERVER_URL)
        var urlrequest = URLRequest(url:url!)
        urlrequest.cachePolicy = .reloadIgnoringLocalCacheData
        urlrequest.addValue("text/xml", forHTTPHeaderField: "Content-Type")
        //let requestXMLLength = String(describing: requestXML.count)
        let requestXMLLength = String(describing: eRequest.count)
        urlrequest.addValue(requestXMLLength , forHTTPHeaderField: "Content-Length")
        urlrequest.httpMethod = "POST"
        urlrequest.timeoutInterval = Constants.AppConfig.HTTP_REQUEST_TIMEOUT
        //urlrequest.httpBody = requestXML.data(using: String.Encoding.utf8)
        urlrequest.httpBody = eRequest.data(using: String.Encoding.utf8)
    
        
        let reachabilityManager = Reachability.isConnectedToNetwork()
        
        if(reachabilityManager != true){
            onFailure(Constants.Message.CONNECTIVITY_ISSUE)
        }else{
            
            let defaultSession = URLSession(
                configuration: URLSessionConfiguration.default,
                delegate: APIManager(),
                delegateQueue: nil)
            
            defaultSession.dataTask(with: urlrequest){
                data, response, error in
                //print(data)
                //print(error)
                //print(response)
                if let httpResponse = response as? HTTPURLResponse {
                    if httpResponse.statusCode == Constants.HTTPStatusCode.SUCCESS {
                        
                        let returnData = String(data: data!, encoding: .utf8)
                        let encryptedRequest = try! returnData?.aesDecrypt(Constants.AppConfig.M_KEY, iv: "")
                        onSuccess((encryptedRequest?.data(using: String.Encoding.utf8)!)!)
                        //onSuccess(data!)
                    }else if(httpResponse.statusCode == Constants.HTTPStatusCode.NOT_FOUND || httpResponse.statusCode == Constants.HTTPStatusCode.SERVICE_UNAVAILABLE){
                        onFailure(Constants.Message.EXCEPTION_HTTP_UNAVAILABLE)
                    }else if(httpResponse.statusCode == Constants.HTTPStatusCode.GATEWAY_TIMEOUT){
                        onFailure(Constants.Message.EXCEPTION_TIME_OUT)
                    }
                }else{
                    onFailure(Constants.Message.GENERAL_SERVER_ERROR)
                }
            }.resume()
        }
    }
    
    public func urlSession(_ session: URLSession, didReceive challenge: URLAuthenticationChallenge, completionHandler: @escaping (URLSession.AuthChallengeDisposition, URLCredential?) -> Swift.Void) {
        
        // Adapted from OWASP https://www.owasp.org/index.php/Certificate_and_Public_Key_Pinning#iOS
        
        if (challenge.protectionSpace.authenticationMethod == NSURLAuthenticationMethodServerTrust) {
            if let serverTrust = challenge.protectionSpace.serverTrust {
                var secresult = SecTrustResultType.invalid
                let status = SecTrustEvaluate(serverTrust, &secresult)
                
                if(errSecSuccess == status) {
                    if let serverCertificate = SecTrustGetCertificateAtIndex(serverTrust, 0) {
                        let serverCertificateData = SecCertificateCopyData(serverCertificate)
                        let data = CFDataGetBytePtr(serverCertificateData);
                        let size = CFDataGetLength(serverCertificateData);
                        let cert1 = NSData(bytes: data, length: size)
                        let file_der = Bundle.main.path(forResource: "jsbl_SSL", ofType: "cer")
                        
                        if let file = file_der {
                            if let cert2 = NSData(contentsOfFile: file) {
                                if cert1.isEqual(to: cert2 as Data) {
                                    completionHandler(URLSession.AuthChallengeDisposition.useCredential, URLCredential(trust:serverTrust))
                                    return
                                }
                            }
                        }
                    }
                }
            }
        }
        // Pinning failed
        completionHandler(URLSession.AuthChallengeDisposition.cancelAuthenticationChallenge, nil)
    }
}
