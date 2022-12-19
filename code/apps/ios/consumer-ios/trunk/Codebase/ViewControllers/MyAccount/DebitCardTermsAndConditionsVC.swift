//
//  DebitCardTermsAndConditionsVC.swift
//  JSBL-BB
//
//  Created by Hassan Masood on 12/8/20.
//  Copyright © 2020 Inov8. All rights reserved.
//

import UIKit
import WebKit

class DebitCardTermsAndConditionsVC: BaseViewController, WKUIDelegate {
    
    @IBOutlet weak var btnAccept: UIButton!
    @IBOutlet weak var btnCancel: UIButton!
    @IBOutlet weak var webViewContainer: UIView!
    var fee = ""
    var cMob = ""
    var CNIC = ""
    var webView: WKWebView!
    
    override func viewDidLoad() {
        isDebitCardTermsVC = true
        super.viewDidLoad()
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: true)
        // Do any additional setup after loading the view.
        let webConfiguration = WKWebViewConfiguration()

        let customFrame = CGRect.init(origin: CGPoint.zero, size: CGSize.init(width: 0.0, height: self.webViewContainer.frame.size.height))
        self.webView = WKWebView (frame: customFrame , configuration: webConfiguration)
        self.webViewContainer.addSubview(webView)
        webView.translatesAutoresizingMaskIntoConstraints = false
        webView.topAnchor.constraint(equalTo: webViewContainer.topAnchor).isActive = true
        webView.rightAnchor.constraint(equalTo: webViewContainer.rightAnchor).isActive = true
        webView.leftAnchor.constraint(equalTo: webViewContainer.leftAnchor).isActive = true
        webView.bottomAnchor.constraint(equalTo: webViewContainer.bottomAnchor).isActive = true
        webView.heightAnchor.constraint(equalTo: webViewContainer.heightAnchor).isActive = true
        webView.uiDelegate = self
        webView.isOpaque = false
        webView.backgroundColor = UIColor.clear

        let url = URL(string: Constants.ServerConfig.TERMS_AND_CONDITION_URL)!
        webView.load(URLRequest(url: url))
    }
    
    
    @IBAction func actAccept(_ sender: UIButton) {
        if sender.isSelected == true {
            sender.isSelected = false
            sender.setImage(UIImage(named : "unselectedImage"), for: .normal)
          }else {
            sender.isSelected = true
            sender.setImage(UIImage(named : "selectedImage"), for: .normal)
            let addressVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "DebitCardMailingInfo") as! DebitCardMailingInfoVC
            addressVC.fee = fee
            self.pushViewController(addressVC)
          }
    }
    
    
    @IBAction func actCancel(_ sender: Any) {
        self.popViewController()
        
    }
    
    
    
}
