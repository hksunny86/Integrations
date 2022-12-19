//
//  productListingCell.swift
//  JSBL-BB
//
//  Created by Hassan Masood on 10/5/20.
//  Copyright © 2020 Inov8. All rights reserved.
//

import UIKit

class productListingCell: UITableViewCell {

    
    @IBOutlet weak var prodImage: UIImageView!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var leadingConstraintTitlelabel: NSLayoutConstraint!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
