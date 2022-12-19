//
//  FPAJSWalletAccountOpenDiscrepantVC.swift
//  FalconApp
//
//  Created by M Zeshan Arif on 18/09/2017.
//  Copyright © 2017 Wateen. All rights reserved.
//

import UIKit
import MobileCoreServices

class FPAJSWalletAccountOpenDiscrepantVC: FPAJSWalletAccountOpenBaseVC, UINavigationControllerDelegate, UIImagePickerControllerDelegate {

    // MARK: - Outlets -
    @IBOutlet weak var lblCustomerPhoto: UILabel!
    @IBOutlet weak var btnCustomerPhoto: UIButton!
    @IBOutlet weak var lblCNICPhoto: UILabel!
    @IBOutlet weak var btnCNICPhoto: UIButton!
    @IBOutlet weak var viewCustomerPhoto: UIView!
    @IBOutlet weak var viewCNICPhoto: UIView!
    @IBOutlet weak var viewCustomerPhotoHeight: NSLayoutConstraint!
    @IBOutlet weak var viewCNICPhotoHeight: NSLayoutConstraint!
    @IBOutlet weak var btnNext: FPAUIButton!
    
    @IBOutlet weak var imgCustomerPhoto: UIImageView!
    @IBOutlet weak var customerPhotoHeight: NSLayoutConstraint!
    @IBOutlet weak var imgCNICPhoto: UIImageView!
    @IBOutlet weak var cnicPhotoHeight: NSLayoutConstraint!
    
    // MARK: - Variables -
    var imagePickerType: ImagePickerType = .customerImage
    var _customerImage: UIImage?
    var _cnicFrontImage: UIImage?
    var customerImage: UIImage?{
        get{
            return _customerImage
        }
        set{
            _customerImage = newValue
            if let image = _customerImage {
                customerPhotoHeight.constant = 90
                self.viewCustomerPhotoHeight.constant = 143.5
                if let imageCompress = Utility.resize(image: image) {
                    imgCustomerPhoto.image = imageCompress
                    _customerImage = imageCompress
                }
                else{
                    imgCustomerPhoto.image = image
                }
            }
        }
    }
    
    var cnicFrontImage: UIImage?{
        get{
            return _cnicFrontImage
        }
        set{
            _cnicFrontImage = newValue
            if let image = _cnicFrontImage {
                cnicPhotoHeight.constant = 90
                self.viewCNICPhotoHeight.constant = 143.5
                if let imageCompress = Utility.resize(image: image) {
                    imgCNICPhoto.image = imageCompress
                    _cnicFrontImage = imageCompress
                }
                else{
                    imgCNICPhoto.image = image
                }
            }
        }
    }
    
    // MARK: - Overridden Methods -
    override func prepareDataSource() {
        super.prepareDataSource()
        
        tableView.dataSource = self
        tableView.delegate = self
        tableView.rowHeight = UITableViewAutomaticDimension
        tableView.estimatedRowHeight = 100
        tableView.reloadData()
        self.view.layoutIfNeeded()
        self.view.layoutSubviews()
        tableViewHeight.constant = tableView.contentSize.height
        
        self.view.layoutIfNeeded()
        self.view.layoutSubviews()
    }
    
    
    override func setupView()  {
        super.setupView()
        hideThumbnail()
        if let status = account.walletAccountStatus {
            switch status {
            case .DiscrepantWithProfilePhoto:
                hideCNICPhotoView()
                break
            case .DiscrepantWithCNICPhoto:
                hideCustomerPhotoView()
                break
            default:
                break
            }
        }
        
        Utility.roundButton(btn: btnNext)
        
    }
    
    override func initialization(){
        super.initialization()
    }
    
    func hideThumbnail(){
        self.customerPhotoHeight.constant = 0
        self.viewCustomerPhotoHeight.constant = 53.5
        self.cnicPhotoHeight.constant = 0
        self.viewCNICPhotoHeight.constant = 53.5
    }

    override func viewDidLoad() {
        //super.viewDidLoad()
        
        // Setup UI
        setupView()
        
        self.setupHeaderBarView(false, isHomeButtonHidden: true, isSignoutButtonHidden: true)
        
        // Initialization
        initialization()
    }
    
    @IBAction func btnCustomerImage_TouchUpInside(_ sender: UIButton) {
        imagePickerType = .customerImage
        let zzMediaHelper = ZZMediaHelper(delegate: self)
        zzMediaHelper.getCameraOn(vc: self, canEdit: false)
//        zzMediaHelper.showActionSheetPicker(title: nil, message: nil, sender: sender, controller: self)
//        {
//            (url, data, type, success) in
//        }
        
    }
    
    @IBAction func btnCNICFront_TouchUpInside(_ sender: UIButton) {
        imagePickerType = .cnicFrontImage
        let zzMediaHelper = ZZMediaHelper(delegate: self)
        zzMediaHelper.getCameraOn(vc: self, canEdit: false)
//        zzMediaHelper.showActionSheetPicker(title: "", message: "", sender: sender, controller: self)
//        {
//            (url, data, type, success) in
//        }
    }
    
    @IBAction func btnNext_TouchUpInside(_ sender: FPAUIButton) {
        if validate() {
            // Proceed Flow
//            if let customerImage = customerImage {
//                account.customerImage = customerImage
//            }
//            if let cnicImage = cnicFrontImage {
//                account.cnicImage = cnicImage
//            }
            
//            if let vc = Utility.getViewController("FPAJSWalletAccountOpenTermsAndConditionVC", storyboardName: kST_ID_POPUP) as? FPAJSWalletAccountOpenTermsAndConditionVC {
//                vc.account = account
//                self.navigationController?.pushViewController(vc, animated: true)
//            }
            
            let viewController = UIStoryboard(name: "Popup_iPhone", bundle: nil).instantiateViewController(withIdentifier: "FPAJSWalletAccountOpenTermsAndConditionVC")
            
            
            self.pushViewController(viewController)
        }
    }

    func hideCustomerPhotoView(){
        viewCustomerPhoto.isHidden = true
        viewCustomerPhotoHeight.constant = 0
    }
    func hideCNICPhotoView(){
        viewCNICPhoto.isHidden = true
        viewCNICPhotoHeight.constant = 0
    }
    
    func validate() -> Bool{
        if let status = account.walletAccountStatus {
            var alertMessage = ""
            switch status {
            case .DiscrepantWithProfilePhoto:
                if customerImage == nil{
                    alertMessage = Constants.Message.kMESSAGE_CUSTOMER_IMAGE_ERROR
                }
                break
            case .DiscrepantWithCNICPhoto:
                if cnicFrontImage == nil{
                    alertMessage = Constants.Message.kMESSAGE_CNIC_FRONT_IMAGE_ERROR
                }
                break
            default:
                if customerImage == nil{
                    alertMessage = Constants.Message.kMESSAGE_CUSTOMER_IMAGE_ERROR
                }
                else if cnicFrontImage == nil{
                    alertMessage = Constants.Message.kMESSAGE_CNIC_FRONT_IMAGE_ERROR
                }
                break
            }
            if alertMessage != "" {
                self.showMessage("alertMessage")
                return false
            }
            return true
        }
        return true
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        
        let type: String = info[UIImagePickerControllerMediaType] as! String
        //        var mediaURL: URL? = nil
        //        var imageData: Data? = nil
        if type == (kUTTypeVideo as String)  || type == (kUTTypeMovie as String) {
            // movie != video
            //            mediaURL = info[UIImagePickerControllerMediaURL] as? URL
            //            imageData = try? Data(contentsOf: mediaURL!)
            //            self.mediaSelection!(mediaURL!, imageData!, 2, true)
        } else {
            
            var selectedImage: UIImage? = info[UIImagePickerControllerEditedImage] as? UIImage
            if selectedImage == nil {
                selectedImage = info[UIImagePickerControllerOriginalImage] as? UIImage
            }
            
            if let image = selectedImage {
                switch imagePickerType {
                case .customerImage:
                    self.customerImage = image
                    break
                case .cnicFrontImage:
                    self.cnicFrontImage = image
                    break
                }
                
            }
            else{
                // Invalid Image
            }
        }
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
            picker.dismiss(animated: false, completion: nil)
        }
        
    }
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true, completion: {
            //            self.mediaSelection!(nil, nil, 0, false)
        })
    }
}
