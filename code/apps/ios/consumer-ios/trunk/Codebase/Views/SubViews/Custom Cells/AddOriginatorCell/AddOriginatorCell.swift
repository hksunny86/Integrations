//
//  AddOriginatorCell.swift
//  JSBL-BB
//
//  Created by Uzair on 9/25/20.
//  Copyright © 2020 Inov8. All rights reserved.
//

import UIKit
import ActionSheetPicker_3_0



class AddOriginatorCell: UITableViewCell, UITextFieldDelegate {
    
    @IBOutlet weak var lblOriginatorNO: UILabel!
    @IBOutlet weak var locationTextField: UITextField!
    @IBOutlet weak var btnRelationWithOriginator: UIButton!
    @IBOutlet weak var btnAddOriginator: UIButton!
    @IBOutlet weak var btnDelete: UIButton!
    var delegate:addOriginatorDelegate?
    var indexPath:IndexPath!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        btnRelationWithOriginator.layer.borderColor = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
        btnRelationWithOriginator.layer.borderWidth = 0.7
        locationTextField.delegate = self
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
    @IBAction func actAddOriginator(_ sender: Any) {
        delegate?.addOriginator(at: indexPath)
        
    }
    
    @IBAction func actChooseRelation(_ sender: Any) {
        delegate?.displayRelationShipPickerCell1()
        
    }
    
    @IBAction func actDeleteSelf(_ sender: Any) {
        delegate?.deleteSelf(at: indexPath)
    }
    
    // UITextField Defaults delegates
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        
        locationTextField.resignFirstResponder()
        return true
    }
    func textFieldDidEndEditing(_ textField: UITextField) {
        
        locationTextField = textField
        delegate?.afterClickingReturnInTextFieldCell1(cell: self)
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        
        return Utility.textfieldInputandLengthCheck(textField, range: range, string: string, characters: Constants.UI.TextField.ACCEPTABLE_ALPHABETS, length: 250)
        
    }
}
