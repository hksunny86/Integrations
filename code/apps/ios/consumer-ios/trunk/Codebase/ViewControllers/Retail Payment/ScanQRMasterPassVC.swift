//
//  ScanQRMasterPassVC.swift
//  JSBL-MB
//
//  Created by Maria Alvi on 11/16/17.
//  Copyright © 2017 inov8. All rights reserved.
//

import Foundation
import UIKit
import  AVFoundation

class ScanQRMasterPassVC : BaseViewController , AVCaptureMetadataOutputObjectsDelegate {
    
    var arrCategory = [Category]()
    var subCategory =  [Category]()
    
    
    var captureSession: AVCaptureSession!
    var videoPreviewLayer: AVCaptureVideoPreviewLayer!
    var isReading: Bool = false
    let supportedCodeTypes = [AVMetadataObject.ObjectType.qr]
    var resultString = ""
    var QRResponseObj = QRResponse()
    var response = (XMLError(), XMLMessage(),[String:String]())
    
    @IBOutlet weak var qrCodeFrameView:UIView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        initialize()
    }
    
    func initialize(){
        self.setupHeaderBarView("QR Payment", isBackButtonHidden: false, isHomeButtonHidden: false, isSignoutButtonHidden: false)
        configureAndScan()
    }
    
    func configureAndScan(){
        
        guard let captureDevice = AVCaptureDevice.default(for: AVMediaType.video) else {return}
        
        do {
            let input = try AVCaptureDeviceInput(device: captureDevice)
            captureSession = AVCaptureSession()
            captureSession?.addInput(input)
            let captureMetadataOutput = AVCaptureMetadataOutput()
            captureSession?.addOutput(captureMetadataOutput)
            
            captureMetadataOutput.setMetadataObjectsDelegate(self, queue: DispatchQueue.main)
            captureMetadataOutput.metadataObjectTypes = supportedCodeTypes
            
            videoPreviewLayer = AVCaptureVideoPreviewLayer(session: captureSession!)
            videoPreviewLayer?.videoGravity = AVLayerVideoGravity.resizeAspectFill
            videoPreviewLayer?.frame = view.layer.bounds
            qrCodeFrameView.layer.addSublayer(videoPreviewLayer!)
            captureSession?.startRunning()
            
        } catch {
            // If any error occurs, simply print it out and don't continue any more.
            //print(error)
            return
        }
    }
    
    func failed() {
        let ac = UIAlertController(title: "Scanning not supported", message: "Your device does not support scanning a code from an item. Please use a device with a camera.", preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: "OK", style: .default))
        present(ac, animated: true)
        captureSession = nil
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        if (captureSession?.isRunning == false) {
            captureSession.startRunning();
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        
        if (captureSession?.isRunning == true) {
            captureSession.stopRunning();
        }
    }
    
    
    func metadataOutput(_ output: AVCaptureMetadataOutput, didOutput metadataObjects: [AVMetadataObject], from connection: AVCaptureConnection) {
        
        if metadataObjects.count == 0 {
            qrCodeFrameView?.frame = CGRect.zero
            //messageLabel.text = "No QR/barcode is detected"
            //print("No QR/barcode is detected")
            return
        }
        
        let metadataObj = metadataObjects[0] as! AVMetadataMachineReadableCodeObject
        
        if supportedCodeTypes.contains(metadataObj.type) {
            
            let barCodeObject = videoPreviewLayer?.transformedMetadataObject(for: metadataObj)
            qrCodeFrameView?.frame = barCodeObject!.bounds
            
            if metadataObj.stringValue != nil {
                
                //print(metadataObj.stringValue!)
                
                QRResponseObj = QRResponse.parseQrCode(metadataObj.stringValue!)
                videoPreviewLayer.removeFromSuperlayer()
                self.captureSession?.stopRunning()
                
                if QRResponseObj.amount == "" {
                    
                    let scanQRMasterPassInput = UIStoryboard(name: "RetailPayment", bundle: nil).instantiateViewController(withIdentifier: "MasterPassInputVC") as! MasterPassInputVC
                    scanQRMasterPassInput.merchantID = QRResponseObj.merchantID
                    scanQRMasterPassInput.QRResponseObj = QRResponseObj
//                    scanQRMasterPassConfrimVC.QRResponseObj = merchantResponse
                    self.pushViewController(scanQRMasterPassInput)
                
                } else {
                    // move to next controller
                    self.verifyMerchant(QRResponseObj: self.QRResponseObj)
                    
                }
            }
        }
    }
    
    
    func found(code: String) {
        //print(code)
    }
    
    override var prefersStatusBarHidden: Bool {
        return true
    }
    
    override var supportedInterfaceOrientations: UIInterfaceOrientationMask {
        return .portrait
    }
    
    func verifyMerchant(QRResponseObj:QRResponse){
        //print("verify Merchant")
        
        if(Constants.AppConfig.IS_MOCK == 1)  {
            guard let
                xmlPath = Bundle.main.path(forResource: "Command-208-RetailPayment", ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                else { return }
            
            response = XMLParser.paramTypeXMLParsing(data)
            
            if(response.0.msg != nil){
                
                self.alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: response.0.msg!, okButtonPressed: {
                    self.alertView.hide()
                })
                self.alertView.show(parentView: self.view)
            }
                
            else if(response.1.msg != nil){
                
                self.alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: response.1.msg!, okButtonPressed: {
                    self.alertView.hide()
                    self.popViewControllerAndGotoStart()
                })
                self.alertView.show(parentView: self.view)
            } else {
                let merchantResponse :  retailMerchant = retailMerchant()
                merchantResponse.merchantID = response.2["MRID"]!
                merchantResponse.totalAmount = response.2["TAMT"]!
                merchantResponse.totalAmountFormatted = response.2["TAMTF"]!
                merchantResponse.merchantName = response.2["MNAME"]!
                
                let scanQRMasterPassConfrimVC = UIStoryboard(name: "RetailPayment", bundle: nil).instantiateViewController(withIdentifier: "ScanQRMasterPassConfrimVC") as! ScanQRMasterPassConfrimVC
                scanQRMasterPassConfrimVC.arrCategory =  self.arrCategory
                scanQRMasterPassConfrimVC.subCategory =  self.subCategory
                scanQRMasterPassConfrimVC.QRResponseObj = merchantResponse
                scanQRMasterPassConfrimVC.QRString = ""
                self.pushViewController(scanQRMasterPassConfrimVC)
            }
        } else {
            
            self.showLoadingView()
            
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            var amount = ""
            if(QRResponseObj.amount != ""){
                amount = QRResponseObj.amount
            }
            
            var merchantID = ""
            if(QRResponseObj.merchantID != "" ){
                merchantID = QRResponseObj.merchantID
            }
            
            
            let webApi : TransactionAPI = TransactionAPI()
            
            webApi.retailPaymentInfoPostRequest(
                Constants.CommandId.RETAILPAYMENT_INFO,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                MRID: merchantID,
                TAMT: amount,
                QRString: QRResponseObj.qrString,
                onSuccess:{(data) -> () in
                    //print(data)
                    self.response = XMLParser.moneyTransferInfoXMLParsing(data)
                    //print(self.response)
                    if(self.response.0.msg != nil){
                        if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                        }else{
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                        }
                    }else if(self.response.1.msg != nil){
                        
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                        
                    }else{
                        let merchantResponse :  retailMerchant = retailMerchant()
                        merchantResponse.merchantID = self.response.2["MRID"]!
                        merchantResponse.totalAmount = self.response.2["TAMT"]!
                        merchantResponse.totalAmountFormatted = self.response.2["TAMTF"]!
                        merchantResponse.merchantName = self.response.2["MNAME"]!
                        
                        let scanQRMasterPassConfrimVC = UIStoryboard(name: "RetailPayment", bundle: nil).instantiateViewController(withIdentifier: "ScanQRMasterPassConfrimVC") as! ScanQRMasterPassConfrimVC
                        scanQRMasterPassConfrimVC.arrCategory =  self.arrCategory
                        scanQRMasterPassConfrimVC.subCategory =  self.subCategory
                        scanQRMasterPassConfrimVC.QRResponseObj = merchantResponse
                        self.pushViewController(scanQRMasterPassConfrimVC)
                    }
                    self.hideLoadingView()
            },
                onFailure: {(reason) ->() in
                    //print("Failure")
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                    self.hideLoadingView()
            })
        }
    }
         
}


