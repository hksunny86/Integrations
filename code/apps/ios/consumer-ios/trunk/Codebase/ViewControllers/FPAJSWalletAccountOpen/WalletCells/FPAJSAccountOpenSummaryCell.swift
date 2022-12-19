//
//  FPAJSAccountOpenSummaryCell.swift
//  FalconApp
//
//  Created by M Zeshan Arif on 15/09/2017.
//  Copyright © 2017 Wateen. All rights reserved.
//

import UIKit

class FPAJSAccountOpenSummaryCell: UITableViewCell {
    
    // MARK: - Outlets -
    
    @IBOutlet weak var lblTitle: UILabel!
    @IBOutlet weak var lblValue: UILabel!
    @IBOutlet weak var lblDateFormat: UILabel!
    fileprivate var _accountRow : FPAJSWalletAccountOpenRow!
    
    
    var accountRow: FPAJSWalletAccountOpenRow{
        get{
            return _accountRow
        }
        set{
            _accountRow = newValue
            lblTitle.text = _accountRow.title
            lblValue.text = _accountRow.value
            lblDateFormat.text = _accountRow.dateFormat
            layoutIfNeeded()
        }
    }

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        // Configure the view for the selected state
    }

}
