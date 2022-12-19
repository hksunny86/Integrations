//
//  Z2MediaHelper.swift
//  FonePayMerchant
//
//  Created by Zeeshan Haider on 11/07/2017.
//  Copyright © 2017 XYZco. All rights reserved.
//

import Foundation
import Foundation
import MobileCoreServices
import UIKit

enum VCType : Int {
    case image = 0
    case video
    case none
}

typealias MediaSelectionHandler = (_ mediaURL: URL?, _ data: Data?, _ type: Int, _ success: Bool) -> Void
typealias AlertViewDismissHandler = () -> Void
typealias AlertViewButtonClickedAtIndexHandler = (_ selectedOption: UIImagePickerController.SourceType) -> Void

class ZZMediaHelper : NSObject {
    
    private let imagePicker = UIImagePickerController()
    private let isPhotoLibraryAvailable = UIImagePickerController.isSourceTypeAvailable(.photoLibrary)
    private let isSavedPhotoAlbumAvailable = UIImagePickerController.isSourceTypeAvailable(.savedPhotosAlbum)
    private let isCameraAvailable = UIImagePickerController.isSourceTypeAvailable(.camera)
    private let isRearCameraAvailable = UIImagePickerController.isCameraDeviceAvailable(.rear)
    private let isFrontCameraAvailable = UIImagePickerController.isCameraDeviceAvailable(.front)
    private let sourceTypeCamera = UIImagePickerController.SourceType.camera
    private let rearCamera = UIImagePickerController.CameraDevice.rear
    private let frontCamera = UIImagePickerController.CameraDevice.front
    private var _viewController : UIViewController!
    
    var viewController: UIViewController {
        get {
            return _viewController
        }
        set {
            _viewController = newValue
        }
    }
    
    var mediaSelection : MediaSelectionHandler?
    
    
    var delegate: UINavigationControllerDelegate  & UIImagePickerControllerDelegate
    init (delegate: UINavigationControllerDelegate  & UIImagePickerControllerDelegate) {
        self.delegate = delegate
    }
    
    
    func getCameraOn(vc: UIViewController, canEdit: Bool) {
        _viewController = vc
        if !isCameraAvailable { return }
        let type1 = kUTTypeImage as String
        
        if isCameraAvailable {
            if let availableTypes = UIImagePickerController.availableMediaTypes(for: .camera) {
                if availableTypes.contains(type1) {
                    imagePicker.mediaTypes = [type1]
                    imagePicker.sourceType = sourceTypeCamera
                }
            }
            
            if isRearCameraAvailable {
                imagePicker.cameraDevice = rearCamera
            } else if isFrontCameraAvailable {
                imagePicker.cameraDevice = frontCamera
            }
        } else {
            return
        }
        
        imagePicker.allowsEditing = canEdit
        imagePicker.allowsEditing = false
        imagePicker.showsCameraControls = true
        imagePicker.delegate = delegate
        
         self.viewController.present(imagePicker, animated: true, completion: nil)
//        onVC.present(imagePicker, animated: true, completion: nil)
    }

    func getCameraOnFront(vc: UIViewController, canEdit: Bool) {
        _viewController = vc
        if !isCameraAvailable { return }
        let type1 = kUTTypeImage as String
        
        if isCameraAvailable {
            if let availableTypes = UIImagePickerController.availableMediaTypes(for: .camera) {
                if availableTypes.contains(type1) {
                    imagePicker.mediaTypes = [type1]
                    imagePicker.sourceType = sourceTypeCamera
                }
            }
            
            if  isFrontCameraAvailable{
                imagePicker.cameraDevice = frontCamera
            } else if isRearCameraAvailable {
                imagePicker.cameraDevice = rearCamera
            }
        } else {
            return
        }
        
        imagePicker.allowsEditing = canEdit
        imagePicker.allowsEditing = false
        imagePicker.showsCameraControls = true
        imagePicker.delegate = delegate
        
        self.viewController.present(imagePicker, animated: true, completion: nil)
        //        onVC.present(imagePicker, animated: true, completion: nil)
    }
    

}

