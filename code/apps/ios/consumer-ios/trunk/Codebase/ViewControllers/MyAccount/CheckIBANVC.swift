//
//  CheckIBANVC.swift
//  JSBL-BB
//
//  Created by Inov8 on 4/17/21.
//  Copyright © 2021 Inov8. All rights reserved.
//

import UIKit

class CheckIBANVC: BaseViewController {

    @IBOutlet weak var lblIBAN: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: false, isSignoutButtonHidden: false)
        lblIBAN.text = Customer.sharedInstance.iBAN ?? ""
    }
    

    @IBAction func actOKPressed(_ sender: Any) {
        self.popViewController()
    }
}
