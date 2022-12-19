//
//  UserVerficationVC.swift
//  JSBL-BB
//
//  Created by Adnan Ahmed on 18/09/2017.
//  Copyright © 2017 Inov8. All rights reserved.
//



import UIKit
import Foundation
import ActionSheetPicker_3_0
import MobileCoreServices

class UserVerficationVC: FPAJSWalletAccountOpenBaseVC, UITextFieldDelegate,UINavigationControllerDelegate, UIImagePickerControllerDelegate {
    // MARK: - Outlets -
    @IBOutlet weak var lblFirstName: UILabel!
    @IBOutlet weak var txtFirstName: UITextField!
    @IBOutlet weak var lblLastName: UILabel!
    @IBOutlet weak var txtLastName: UITextField!
    @IBOutlet weak var lblDateOfBirth: UILabel!
    @IBOutlet weak var btnDateOfBirth: FPADropDownButton!
    @IBOutlet weak var lblDateOfBirthFormat: UILabel!
    @IBOutlet weak var lblCNICExpiryDate: UILabel!
    @IBOutlet weak var btnCNICExpiryDate: FPADropDownButton!
    @IBOutlet weak var lblCNICExpiryFormat: UILabel!
    @IBOutlet weak var lblCustomerImage: UILabel!
    @IBOutlet weak var btnCustomerImage: UIButton!
    @IBOutlet weak var lblCNICFront: UILabel!
    @IBOutlet weak var btnCNICFront: UIButton!
    @IBOutlet weak var btnNext: UIButton!
    
    @IBOutlet weak var imgCustomerPhoto: UIImageView!
    @IBOutlet weak var customerPhotoHeight: NSLayoutConstraint!
    @IBOutlet weak var imgCNICPhoto: UIImageView!
    @IBOutlet weak var cnicPhotoHeight: NSLayoutConstraint!
    
    @IBOutlet weak var customerPhotoBtn: UIButton!
    @IBOutlet weak var cnicPhotoBtn: UIButton!
    
    
    var account1 = JSAccount()
    var imagePickerType: ImagePickerType = .customerImage
    var _customerImage: UIImage?
    var _cnicFrontImage: UIImage?
    var dob: String = ""
    var cnicExpiry: String = ""
    var cnic: String?
    var cmob: String?
    
    var customerImage: UIImage?{
        get{
            return _customerImage
        }
        set{
            _customerImage = newValue
            if let image = _customerImage {
                customerPhotoHeight.constant = 90
                self.customerPhotoBtn.isHidden = false
                self.customerPhotoBtn.setTitle("Upload", for: .normal)
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
                self.cnicPhotoBtn.isHidden = false
                self.cnicPhotoBtn.setTitle("Upload", for: .normal)
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
    
    override func viewDidLoad() {
        //super.viewDidLoad()
        
        self.setupHeaderBarView("Open Account", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: true)
        // Setup UI
        setupView()
        
        // Initialization
        initialization()
    }
    
    
    override func setupView()  {
        //        self.topView.updateTitle("Account Opening")
        //        Utility.roundButton(btnNext)
        
        //let customer = Customer.sharedInstance()
        btnNext.layer.cornerRadius = Constants.UI.Button.CORNER_RADIUS
        txtFirstName.text = ""
        txtLastName.text = ""
        txtFirstName.delegate = self
        txtLastName.delegate = self
        
        
        hideThumbnail()
    }
    
    override func initialization(){
        
    }
    
    func hideThumbnail(){
        self.customerPhotoHeight.constant = 0
        self.cnicPhotoHeight.constant = 0
        self.customerPhotoBtn.isHidden = true
        self.cnicPhotoBtn.isHidden = true
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        let cs = CharacterSet(charactersIn: Constants.Validation.ALPHABETS_CHARACTERS).inverted
        let filtered: String = (string.components(separatedBy: cs) as NSArray).componentsJoined(by: "")
        if(string == filtered){
            if (textField.tag == 1){
                guard let text = txtFirstName.text else { return true }
                let newLength = text.count + string.count - range.length
                return newLength <= 20
            }
            else if(textField.tag == 2){
                guard let text = txtLastName.text else { return true }
                let newLength = text.count + string.count - range.length
                return newLength <= 20
            }else{
                return true
            }
        }else{
            return false
        }
    }
    
    func validate() -> Bool{
        
        var alertMessage = ""
        let firstName = txtFirstName.text!.trimmed
        let lastName = txtLastName.text!.trimmed
        
        
        if firstName == "" || lastName == "" {
            alertMessage = Constants.Message.kMESSAGE_EMPTY_FIELD_ERROR
        }
        else if dob == "" {
            alertMessage = Constants.Message.kMESSAGE_EMPTY_DOB_ERROR
        }
        else if cnicExpiry == "" {
            alertMessage = Constants.Message.kMESSAGE_EMPTY_CNIC_EXPIRY_ERROR
        }
        else if customerImage == nil {
            alertMessage = Constants.Message.kMESSAGE_CUSTOMER_IMAGE_ERROR
        }
        else if cnicFrontImage == nil {
            alertMessage = Constants.Message.kMESSAGE_CNIC_FRONT_IMAGE_ERROR
        }
        else if (customerPhotoBtn.currentTitle != "Uploaded") {
            alertMessage = "Please upload Customer Photo"
        }
        else if (cnicPhotoBtn.currentTitle != "Uploaded") {
            alertMessage = "Please upload CNIC front Photo"
        }
        
        
        if alertMessage != "" {
            self.showMessage(alertMessage)
            return false
        }
        return true
    }
    
    
    @IBAction func btnDateOfBirth_TouchUpInside(_ sender: FPADropDownButton) {
        self.view.endEditing(true)
        let minDate: Date =  Calendar.current.date(byAdding: .year, value: -118, to: Date())!
        let maxDate: Date =  Calendar.current.date(byAdding: .year, value: -18, to: Date())!
        
        
        
        ActionSheetDatePicker.show(withTitle: "Date of Birth", datePickerMode: .date, selectedDate:maxDate, minimumDate: minDate, maximumDate: maxDate, doneBlock: { (picker, userSelectedDate, origin) in
            self.dob = Utility.getDate(date: userSelectedDate as! Date, withFormat: "dd-MM-yyyy")
            self.btnDateOfBirth.setTitle(titleString: self.dob)
            
        },cancel: { (success) in
            self.btnDateOfBirth.setTitle(titleString: self.dob == "" ? "Select Date" : self.dob)
            
        }, origin: btnDateOfBirth)
    }
    @IBAction func btnCNICExpiryDate_TouchUpInside(_ sender: FPADropDownButton) {
        self.view.endEditing(true)
        let minDate: Date = Date()
        let maxDate: Date =  Calendar.current.date(byAdding: .year, value: 10, to: minDate)!
        
        ActionSheetDatePicker.show(withTitle: "CNIC Expiry Date", datePickerMode: .date, selectedDate:maxDate, minimumDate: minDate, maximumDate: maxDate, doneBlock: { (picker, userSelectedDate, origin) in
            self.cnicExpiry = Utility.getDate(date: userSelectedDate as! Date, withFormat: "dd-MM-yyyy")
            self.btnCNICExpiryDate.setTitle(titleString: self.cnicExpiry)
            
        },cancel: { (success) in
            self.btnCNICExpiryDate.setTitle(titleString: self.cnicExpiry == "" ? "Select Date" : self.cnicExpiry)
            
        }, origin: btnCNICExpiryDate)
    }
    @IBAction func btnCustomerImage_TouchUpInside(_ sender: UIButton) {
        self.view.endEditing(true)
        imagePickerType = .customerImage
        let zzMediaHelper = ZZMediaHelper(delegate: self)
        zzMediaHelper.getCameraOnFront(vc: self, canEdit: false)
        //        zzMediaHelper.showActionSheetPicker(title: nil, message: nil, sender: sender, controller: self)
        //        {
        //            (url, data, type, success) in
        //        }
        
    }
    
    @IBAction func btnCNICFront_TouchUpInside(_ sender: UIButton) {
        self.view.endEditing(true)
        imagePickerType = .cnicFrontImage
        let zzMediaHelper = ZZMediaHelper(delegate: self)
        zzMediaHelper.getCameraOn(vc: self, canEdit: false)
        //        zzMediaHelper.showActionSheetPicker(title: "", message: "", sender: sender, controller: self)
        //        {
        //            (url, data, type, success) in
        //        }
    }
    
    
    @IBAction func btnNext_TouchUpInside(_ sender: UIButton) {
        self.view.endEditing(true)
        if validate() {
            let firstName = txtFirstName.text!.trimmed
            let lastName = txtLastName.text!.trimmed
            account1.name = "\(firstName) \(lastName)"
            account1.cnic = cnic!
            account1.dob = dob
            account1.cnicExpiryDate = cnicExpiry
            account1.isDiscrepant = "0"
            account1.accountNo = cmob!
            
            if let customerImage = customerImage {
                account1.customerImage = customerImage
            }
            if let cnicImage = cnicFrontImage {
                account1.cnicImage = cnicImage
            }
            //
            
            let viewController = UIStoryboard(name: "SelfRegistration", bundle: nil).instantiateViewController(withIdentifier: "ConfirmRegistrationVC") as! ConfirmRegistrationVC
            viewController.account = account1
            self.pushViewController(viewController)
        }
        
    }
    
    @IBAction func uploadCustomerImage(_ sender: UIButton) {
        if(customerPhotoBtn.currentTitle == "Upload"){
            let reachabilityManager = Reachability.isConnectedToNetwork()
            if(reachabilityManager == true){
                imageUploadRequest(imgageType: true, image: _customerImage!, uploadUrl: URL(string:"\(Constants.ServerConfig.BASE_URL)/upload")!, param: nil)
            }else{
               self.showMessage(Constants.Message.CONNECTIVITY_ISSUE)
            }
        }
    }
    @IBAction func uploadCNICPhoto(_ sender: UIButton) {
        if(cnicPhotoBtn.currentTitle == "Upload"){
            let reachabilityManager = Reachability.isConnectedToNetwork()
            if(reachabilityManager == true){
                imageUploadRequest(imgageType: false, image: _cnicFrontImage!, uploadUrl: URL(string:"\(Constants.ServerConfig.BASE_URL)/upload")!, param: nil)
            }else{
                self.showMessage(Constants.Message.CONNECTIVITY_ISSUE)
            }
        }
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

    
    
    func imageUploadRequest(imgageType: Bool, image: UIImage, uploadUrl: URL, param: [String:String]?) {
        
        Utility.showLoadingView(view: self.view)
        
        let request = NSMutableURLRequest(url:uploadUrl);
        request.httpMethod = "POST"
        
        let boundary = generateBoundaryString()
        
        request.setValue("multipart/form-data; boundary=\(boundary)", forHTTPHeaderField: "Content-Type")
        
        let imageData = image.jpegData(compressionQuality: 1)
        
        if(imageData==nil)  { return; }
        
        request.httpBody = createBodyWithParameters(imgageType: imgageType, parameters: param, filePathKey: "file", imageDataKey: imageData! as NSData, boundary: boundary) as Data
        
        //myActivityIndicator.startAnimating();
        
        let defaultSession = URLSession(configuration: URLSessionConfiguration.default)
        defaultSession.dataTask(with: request as URLRequest){
            data, response, error in
            
            if let data = data {
                
                // You can print out response object
                //print("******* response = \(String(describing: response))")
                //print(data.count)
                // you can use data here
                
                // Print out reponse body
                //let responseString = NSString(data: data, encoding: String.Encoding.utf8.rawValue)
                //print("****** response data = \(responseString!)")
                
                
                let response = XMLParser.paramTypeXMLParsing(data)
                
                if((response.1.msg) != nil){
                    
                    if(imgageType == true){
                        self.customerPhotoBtn.setTitle("Uploaded", for: .normal)
                    }else{
                        self.cnicPhotoBtn.setTitle("Uploaded", for: .normal)
                    }
                    
                    self.showMessage(response.1.msg!)
                }
            } else if error != nil {
                //error.localizedDescription
                self.showMessage("Image can not be uploaded at this time")
            }
            
            DispatchQueue.main.async(execute: {
                Utility.hideLoadingView(view: self.view)
            })
            
        }.resume()
    }
    
    func createBodyWithParameters(imgageType: Bool, parameters: [String: String]?, filePathKey: String?, imageDataKey: NSData, boundary: String) -> NSData {
        
        //imgageType = true ---> Customer Photo else CNIC_FRONT
        
        let body = NSMutableData();
        
        if parameters != nil {
            for (key, value) in parameters! {
                body.appendString(string: "--\(boundary)\r\n")
                body.appendString(string: "Content-Disposition: form-data; name=\"\(key)\"\r\n\r\n")
                body.appendString(string: "\(value)\r\n")
            }
        }
        
        var imgName: String = ""
        if(imgageType == true){
            imgName = "Customer_Photo"
        }else{
            imgName = "CNIC_Front_Photo"
        }
        
//        CustApp_03014341523_Customer_Photo.png
//        CustApp_03014341523_CNIC_Front_Photo.png
        
        let filename = "CustApp_\(String(describing: self.cmob!))_\(imgName).png"
        
        if(imgageType == true){
            account1.customerPhotoPath = filename
        }else{
            account1.cnicPhotoPath = filename
        }
        
        let mimetype = "image/png"
        
        body.appendString(string: "--\(boundary)\r\n")
        body.appendString(string: "Content-Disposition: form-data; name=\"\(filePathKey!)\"; filename=\"\(filename)\"\r\n")
        body.appendString(string: "Content-Type: \(mimetype)\r\n\r\n")
        body.append(imageDataKey as Data)
        body.appendString(string: "\r\n")
        
        body.appendString(string: "--\(boundary)--\r\n")
        
        return body
    }
    
    func generateBoundaryString() -> String {
        return "Boundary-\(NSUUID().uuidString)"
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
