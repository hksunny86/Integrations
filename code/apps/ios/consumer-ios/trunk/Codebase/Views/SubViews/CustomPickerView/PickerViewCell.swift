//
//  PickerViewCell.swift
//  Timepey
//
//  Created by Adnan Ahmed on 17/10/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation
import UIKit

class PickerViewCell: UITableViewCell {
    
    @IBOutlet weak var imgPicker: UIImageView!
    @IBOutlet weak var lblTitle: UILabel!
    
    override func awakeFromNib() {
        // Initialization code
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        // Configure the view for the selected state
    }
    
}
