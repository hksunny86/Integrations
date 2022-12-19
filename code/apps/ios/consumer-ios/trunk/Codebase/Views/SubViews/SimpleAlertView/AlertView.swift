//
//  AlertView.swift
//  JSBL-MB
//
//  Created by Adnan Ahmed on 22/08/2017.
//  Copyright © 2017 inov8. All rights reserved.
//

import UIKit

class AlertView: UIView {
    
    var okCompletionHandler: (() -> ())? = nil
    var cancelCompletionHandler: (() -> ())? = nil
    
    @IBOutlet weak var headerImg: UIImageView!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var msgLabel: UILabel!
    @IBOutlet var view: UIView!
    @IBOutlet weak var okButton: UIButton!
    @IBOutlet weak var cancelButton: UIButton!
    @IBOutlet weak var popUpView: UIView!
    
    @IBOutlet weak var okButtonTrailingConstraint: NSLayoutConstraint!
    
    init() {
        super.init(frame: CGRect())
        loadFromXib()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        loadFromXib()
    }
    
    func loadFromXib(){
        DispatchQueue.main.async {
            Bundle.main.loadNibNamed("AlertView", owner: self, options: nil)
            self.popUpView.layer.cornerRadius = 2
            self.cancelButton.isHidden = false
            self.addSubview(self.view)
        }
    }
    
    
    func show(parentView:UIView){
        DispatchQueue.main.async(execute: {
            self.view.frame = parentView.frame
            parentView.addSubview(self.view)
            self.layoutSubviews()
        })
    }
    
    func initWithTitleAndMessage(title:String, message:String, okButtonPressed:(() -> ())?) {
        DispatchQueue.main.async {
            self.okCompletionHandler = okButtonPressed
            self.titleLabel.text = title
            self.msgLabel.text = message
            self.cancelButton.isHidden = true
        }
    }
    
    func initWithTitleAndMessage(title:String, message:String, actionType:String, okButtonPressed:(() -> ())?){
        if(actionType == "Success"){
            headerImg.image = UIImage(named: "popup_notification_success_message_icon")
        }
        self.okCompletionHandler = okButtonPressed
        titleLabel.text = title
        msgLabel.text = message
        cancelButton.isHidden = true
    }
    
    
    override func layoutSubviews() {
        super.layoutSubviews()
        //self.frame will be correct here
        if(cancelButton.isHidden == true) {
            switch UIDevice.current.userInterfaceIdiom {
            case .phone:
                okButtonTrailingConstraint.constant = -260
            case .pad:
                okButtonTrailingConstraint.constant = 10
            default:
                break
            }
        } else {
            okButtonTrailingConstraint.constant = 7
        }
    }
    
    
    func initWithTitleAndMessage(title:String, message:String, okButtonPressed:(() -> ())?, cancelButtonPressed:(() -> ())?){
        self.okCompletionHandler = okButtonPressed
        self.cancelCompletionHandler = cancelButtonPressed
        DispatchQueue.main.async {
            self.titleLabel.text = title
            self.msgLabel.text = message
        }
    }
    
    func initWithOkAndDownload(title:String, message:String, okButtonPressed:(() -> ())?, downloadButtonPressed:(() -> ())?){
        self.okCompletionHandler = okButtonPressed
        self.cancelCompletionHandler = downloadButtonPressed

        DispatchQueue.main.async {
            self.cancelButton.isHidden = false
            self.titleLabel.text = title
            self.msgLabel.text = message
            self.okButton.setTitle("OK", for: .normal)
            self.cancelButton.setTitle("DOWNLOAD", for: .normal)
        }

    }
    
    func initWithDownloadAndCancel(title:String, message:String, cancelButtonPressed:(() -> ())?, downloadButtonPressed:(() -> ())?){
        self.okCompletionHandler = cancelButtonPressed
        self.cancelCompletionHandler = downloadButtonPressed
        DispatchQueue.main.async {
            self.titleLabel.text = title
            self.msgLabel.text = message
            self.cancelButton.isHidden = false
            self.okButton.setTitle("CANCEL", for: .normal)
            self.cancelButton.setTitle("DOWNLOAD", for: .normal)
        }
    }
    
    func initWithTitleAndMessageWithCancelButton(title:String, message:String, okButtonPressed:(() -> ())?, cancelButtonPressed:(() -> ())?){
        
        self.okCompletionHandler = okButtonPressed
        self.cancelCompletionHandler = cancelButtonPressed
        DispatchQueue.main.async {
            self.okButton.setTitle("SETTING", for: .normal)
            self.titleLabel.text = title
            self.msgLabel.text = message
        }
    }
    
    
    func initWithTitleAndDownloadOnlyButton(title:String, message:String, okButtonPressed:(() -> ())?, cancelButtonPressed:(() -> ())?){
        
        self.okCompletionHandler = okButtonPressed
        self.cancelCompletionHandler = cancelButtonPressed
        DispatchQueue.main.async {
            self.okButton.setTitle("DOWNLOAD", for: .normal)
            self.titleLabel.text = title
            self.msgLabel.text = message
            self.cancelButton.isHidden = true
        }

    }
    
    func initWithResetLoginPINButton(title:String, message:String, okButtonPressed:(() -> ())?, cancelButtonPressed:(() -> ())?){
        
        self.okCompletionHandler = okButtonPressed
        self.cancelCompletionHandler = cancelButtonPressed
        DispatchQueue.main.async {
            self.okButton.setTitle("RESET LOGIN PIN", for: .normal)
            self.titleLabel.text = title
            self.msgLabel.text = message
            self.cancelButton.isHidden = true
        }

    }
    
    func initWithSetMPINNowWithSetMPINLater(title:String, message:String, SetMPINNowPressed:(() -> ())?, SetMPINLaterPressed:(() -> ())?){
        self.okCompletionHandler = SetMPINNowPressed
        self.cancelCompletionHandler = SetMPINLaterPressed

        DispatchQueue.main.async {
            self.cancelButton.isHidden = false
            self.titleLabel.text = title
            self.msgLabel.text = message
            self.okButton.setTitle("SET MPIN", for: .normal)
            self.cancelButton.setTitle("CANCEL", for: .normal)
        }

    }
    
    func hide(){
        self.view.removeFromSuperview()
    }
    
    @IBAction func okBtnPressed(_ sender: UIButton) {
        if(self.okCompletionHandler != nil){
            self.okCompletionHandler!()
        }
    }
    
    @IBAction func cancelBtnPressed(_ sender: UIButton) {
        if(self.cancelCompletionHandler != nil){
            self.cancelCompletionHandler!()
        }
    }
}
