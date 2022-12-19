//
//  FPAJSWalletAccountOpenTermsAndConditionVC.swift
//  FalconApp
//
//  Created by M Zeshan Arif on 18/09/2017.
//  Copyright © 2017 Wateen. All rights reserved.
//

import UIKit

class FPAJSWalletAccountOpenTermsAndConditionVC: BaseViewController {

    
    @IBOutlet weak var webView: UIWebView!
    @IBOutlet weak var btnAccept: FPAUIButton!
    @IBOutlet weak var btnCancel: UIButton!
    
    //var account: MOJSWalletAccount!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //topView.updateTitle("Account Opening")
        
        Utility.roundButton(btn: btnAccept)
        Utility.roundButton(btn: btnCancel)
        // Load Web View
        let urlString = "\(Constants.ServerConfig.BASE_URL)/terms-and-conditions.jsp"
        if let url = URL(string: urlString)
        {
            let request = URLRequest(url: url)
            webView.loadRequest(request)
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        
    }
    

    @IBAction func btnAccept_TouchUpInside(_ sender: FPAUIButton) {
        
        let viewController = UIStoryboard(name: "Popup_iPhone", bundle: nil).instantiateViewController(withIdentifier: "FPAJSWalletAccountOpenConfirmVC")
        self.pushViewController(viewController)
    }
    
    
    @IBAction func btnCancel_TouchUpInside(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }

}
