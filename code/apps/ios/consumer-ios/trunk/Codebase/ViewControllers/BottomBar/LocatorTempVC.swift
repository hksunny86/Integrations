//
//  LocatorTempVC.swift
//  Timepey
//
//  Created by Uzair on 5/11/17.
//  Copyright © 2017 Inov8. All rights reserved.
//

import UIKit

class LocatorTempVC: BaseViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        self.isLocator = false
          self.setupHeaderBarView(false, isHomeButtonHidden: true, isSignoutButtonHidden: false)
        // Do any additional setup after loading the view.
    }


}
