//
//  RetailPaymentQRScanVC.swift
//  Timepey
//
//  Created by Adnan Ahmed on 09/09/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import UIKit
import Foundation
import AVFoundation



class RetailPaymentQRScanVC: BaseViewController, AVCaptureMetadataOutputObjectsDelegate{
    
    var product: Product?
    
    
    var captureSession: AVCaptureSession!
    var previewLayer: AVCaptureVideoPreviewLayer!
    
    override func viewDidLoad() {
        //super.viewDidLoad()
        
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: false, isSignoutButtonHidden: false)
        
        captureSession = AVCaptureSession()
        
        
        
        let videoCaptureDevice = AVCaptureDevice.default(for: AVMediaType.video)
        
        let videoInput: AVCaptureDeviceInput
        
        do {
            videoInput = try AVCaptureDeviceInput(device: videoCaptureDevice!)
        } catch {
            return
        }
        
        if (captureSession.canAddInput(videoInput)) {
            captureSession.addInput(videoInput)
        } else {
            failed();
            return;
        }
        
        let metadataOutput = AVCaptureMetadataOutput()
        
        if (captureSession.canAddOutput(metadataOutput)) {
            captureSession.addOutput(metadataOutput)
            
            metadataOutput.setMetadataObjectsDelegate(self, queue: DispatchQueue.main)
            metadataOutput.metadataObjectTypes = [AVMetadataObject.ObjectType.qr]
        } else {
            failed()
            return
        }
        
        previewLayer = AVCaptureVideoPreviewLayer(session: captureSession);
        //previewLayer.frame = view.layer.bounds;
        previewLayer.frame = CGRect(x: 0,y: 60,width: view.frame.size.width,height: view.frame.size.height)
        previewLayer.videoGravity = AVLayerVideoGravity.resizeAspectFill
        view.layer.addSublayer(previewLayer)
        
    }
    
    func failed() {
        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: "Your device does not support scanning a code from an item. Please use a device with a camera.", actionType: "serverFailure", isCancelBtnHidden: true)
        //captureSession = nil
        captureSession.stopRunning();
    }
    
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        
        if (captureSession?.isRunning == true) {
            captureSession.stopRunning();
        }
        //previewLayer.removeFromSuperlayer()
    }
    
    func captureOutput(_ captureOutput: AVCaptureOutput!, didOutputMetadataObjects metadataObjects: [Any]!, from connection: AVCaptureConnection!) {
        captureSession.stopRunning()
        
        if let metadataObject = metadataObjects.first {
            let readableObject = metadataObject as! AVMetadataMachineReadableCodeObject;
            
            AudioServicesPlaySystemSound(SystemSoundID(kSystemSoundID_Vibrate))
            foundCode(readableObject.stringValue!);
        }
        
        //dismiss(animated: true, completion: nil)
    }
    
    func foundCode(_ code: String) {
        //        000710027750A11038541658480B08afdsfdaf
        //
        //        QR Code Data
        //
        //        Agent ID                     00 071002775
        //        Agent Mobile Number 0A 1103854165848
        //        Agent Name 0B 08afdsfdaf
        
        //print(code)
        
        var agentMobileNumber: String?
        
        
        if (code.contains("0A11") && code.contains("0B")){
            if let firstStr = code.components(separatedBy: "0A11").last {
                if let secStr = firstStr.components(separatedBy: "0B").first{
                    agentMobileNumber = secStr
                }
            }
        }
        
        if(agentMobileNumber != nil && agentMobileNumber?.count == 11){
            
            let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "RetailPaymentInputAmountVC") as! RetailPaymentInputAmountVC
            viewController.product = product
            viewController.agentMobileNumber = agentMobileNumber!
            self.pushViewController(viewController)
            
            previewLayer.removeFromSuperlayer()
            
        }else{
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: "Invalid QR code!", actionType: "serverFailure", isCancelBtnHidden: true)
            previewLayer.removeFromSuperlayer()
        }
    }
    
    @IBAction func scanQRCodePressed(_ sender: UIButton) {
        let cameraMediaType = AVMediaType.video
        let cameraAuthorizationStatus = AVCaptureDevice.authorizationStatus(for: cameraMediaType)
        
        switch cameraAuthorizationStatus {
        case .denied:
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: "Please allow us to access your Camera", actionType: "", isCancelBtnHidden: true)
        case .authorized:
            
            captureSession.startRunning()
            view.layer.addSublayer(previewLayer)
            
            //            let viewController = UIStoryboard(name: "Transaction", bundle: nil).instantiateViewController(withIdentifier: "RetailPaymentInputAmountVC") as! RetailPaymentInputAmountVC
            //            viewController.product = product
            //            self.pushViewController(viewController)
            
        case .restricted:
            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: "Please allow us to access your current Camera", actionType: "", isCancelBtnHidden: true)
            
        case .notDetermined:
            // Prompting user for the permission to use the camera.
            AVCaptureDevice.requestAccess(for: cameraMediaType) { granted in
                if granted {
                    //print("Granted access to \(cameraMediaType)")
                    
                } else {
                    //print("Denied access to \(cameraMediaType)")
                }
            }
        }
    }
}
