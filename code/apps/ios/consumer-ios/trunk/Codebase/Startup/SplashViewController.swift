//
//  SplashViewController.swift
//  Timepey
//
//  Created by Adnan Ahmed on 21/11/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation
import UIKit

class SplashViewController:BaseViewController {
    
    override func viewDidLoad() {
        //super.viewDidLoad()
    }

    override func viewWillAppear(_ animated: Bool) {
        self.perform(#selector(self.loadLoginView), with: nil, afterDelay: 0.5)
    }
    
    @objc func loadLoginView() {
        let loginVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "LoginCustomerVC") as! LoginCustomerVC
        self.pushViewController(loginVC)
    }
}
