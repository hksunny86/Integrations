//
//  FPAJSWalletAccountOpenImageUploadViewController.swift
//  FalconApp
//
//  Created by M Zeshan Arif on 06/09/2017.
//  Copyright © 2017 Wateen. All rights reserved.
//

import UIKit
import MobileCoreServices

class FPAJSWalletAccountOpenImageUploadViewController: BaseViewController, UINavigationControllerDelegate, UIImagePickerControllerDelegate {

    @IBOutlet weak var lblCustomerImage: UILabel!
    @IBOutlet weak var btnCustomerImage: UIButton!
    @IBOutlet weak var lblCNICFront: UILabel!
    @IBOutlet weak var btnCNICFront: UIButton!
    @IBOutlet weak var lblCNICBack: UILabel!
    @IBOutlet weak var btnCNICBack: UIButton!
    @IBOutlet weak var btnNext: FPAUIButton!
    
    var imagePickerType: ImagePickerType = .customerImage
    var customerImage: UIImage?
    var cnicFrontImage: UIImage?
    
     override func viewDidLoad() {
        super.viewDidLoad()
        
        // Setup UI
        setupView()
        
        // Initialization
        initialization()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
    }
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }

    func setupView()  {
        
    }
    
    func initialization(){
        
    }

    @IBAction func btnCustomerImage_TouchUpInside(_ sender: UIButton) {
        imagePickerType = .customerImage
        let zzMediaHelper = ZZMediaHelper(delegate: self)
        zzMediaHelper.getCameraOn(vc: self, canEdit: false)
        
    }
    
    @IBAction func btnCNICFront_TouchUpInside(_ sender: UIButton) {
        imagePickerType = .cnicFrontImage
        let zzMediaHelper = ZZMediaHelper(delegate: self)
        zzMediaHelper.getCameraOn(vc: self, canEdit: false)
    }
    
    @IBAction func btnCNICBack_TouchUpInside(_ sender: UIButton) {
        
    }
    
    @IBAction func btnNext_TouchUpInside(_ sender: FPAUIButton) {
    }

    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
// Local variable inserted by Swift 4.2 migrator.
let info = convertFromUIImagePickerControllerInfoKeyDictionary(info)

        
        let type: String = info[convertFromUIImagePickerControllerInfoKey(UIImagePickerController.InfoKey.mediaType)] as! String
//        var mediaURL: URL? = nil
//        var imageData: Data? = nil
        if type == (kUTTypeVideo as String)  || type == (kUTTypeMovie as String) {
            // movie != video
//            mediaURL = info[UIImagePickerControllerMediaURL] as? URL
//            imageData = try? Data(contentsOf: mediaURL!)
            //            self.mediaSelection!(mediaURL!, imageData!, 2, true)
        } else {
            
            var selectedImage: UIImage? = info[convertFromUIImagePickerControllerInfoKey(UIImagePickerController.InfoKey.editedImage)] as? UIImage
            if selectedImage == nil {
                selectedImage = info[convertFromUIImagePickerControllerInfoKey(UIImagePickerController.InfoKey.originalImage)] as? UIImage
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

// Helper function inserted by Swift 4.2 migrator.
fileprivate func convertFromUIImagePickerControllerInfoKeyDictionary(_ input: [UIImagePickerController.InfoKey: Any]) -> [String: Any] {
	return Dictionary(uniqueKeysWithValues: input.map {key, value in (key.rawValue, value)})
}

// Helper function inserted by Swift 4.2 migrator.
fileprivate func convertFromUIImagePickerControllerInfoKey(_ input: UIImagePickerController.InfoKey) -> String {
	return input.rawValue
}
