//
//  CustomPickerView.swift
//  Timepey
//
//  Created by Adnan Ahmed on 17/10/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation
import UIKit


protocol CustomPickerViewDelegate: class {
    func pickerViewSelected(isRadius:Bool, selectedValue: String)
}


class CustomPickerView: BaseViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var transparentView: UIView!
    @IBOutlet weak var parentView: UIView!
    @IBOutlet weak var pickerViewLabel: UILabel!
    @IBOutlet weak var myTableView: UITableView!
    @IBOutlet weak var parentViewHeightConstraint: NSLayoutConstraint!
    
    var selectedRowIndex = IndexPath()
    
    var isRadius: Bool?
    var screenTitle: String?
    var dataSource = [String]()
    
    var selectedRadius: String?
    var selectedLocator: String?
    
    
    weak var delegate: CustomPickerViewDelegate?
    
    
    override func viewDidLoad() {
        
        
        parentView.layer.cornerRadius = 5
        
        
        myTableView.register(UINib(nibName: "PickerViewCell", bundle: nil), forCellReuseIdentifier: "PickerViewCell")
        myTableView.rowHeight = 44
        
        if(screenTitle != nil){
            pickerViewLabel.text = screenTitle
        }
        
        myTableView.tableFooterView = UIView()
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        //print("yes")
        transparentView.backgroundColor = UIColor.black
        DispatchQueue.main.async{
            self.selectedRowIndex = IndexPath()
            self.myTableView.reloadData()
        }
    }
    
    @IBAction func cancelButtonPressed(_ sender: UIButton) {
        transparentView.backgroundColor = UIColor.clear
        DispatchQueue.main.async {
            self.dismiss(animated: true, completion: nil)
        }

    }
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return dataSource.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cellIdentifier = "PickerViewCell"
        let cell: PickerViewCell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier)! as! PickerViewCell
        if(isRadius == true){
            cell.lblTitle.text = "\(dataSource[indexPath.row]) KM"
            if(selectedRadius == dataSource[indexPath.row]){
                cell.imgPicker.image = UIImage(named: "btn_circle_pressed")!
            }else{
                cell.imgPicker.image = UIImage(named: "btn_circle_normal")!
            }
        }else{
            cell.lblTitle.text = dataSource[indexPath.row]
            if(selectedLocator == dataSource[indexPath.row]){
                cell.imgPicker.image = UIImage(named: "btn_circle_pressed")!
            }else{
                cell.imgPicker.image = UIImage(named: "btn_circle_normal")!
            }
        }
        
        
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        transparentView.backgroundColor = UIColor.clear
        
        let value = dataSource[indexPath.row]
        
        var isRadiusScreen = Bool()
        
        if(isRadius == true){
            isRadiusScreen = true
        }else{
            isRadiusScreen = false
        }
        DispatchQueue.main.async {
            self.dismiss(animated: true, completion: nil)
        }
        delegate?.pickerViewSelected(isRadius: isRadiusScreen, selectedValue: value)
        
        
        
        if(selectedRowIndex == indexPath){
            selectedRowIndex = IndexPath()
        }else{
            selectedRowIndex = indexPath
        }
        
        
        tableView.deselectRow(at: indexPath, animated: true)
        
        myTableView.beginUpdates()
        let selectedIndexPath = IndexPath(row: indexPath.row, section: 0)
        myTableView.reloadRows(at: [selectedIndexPath], with: UITableView.RowAnimation.none)
        myTableView.endUpdates()
        
        
    }
    
    
    
}
