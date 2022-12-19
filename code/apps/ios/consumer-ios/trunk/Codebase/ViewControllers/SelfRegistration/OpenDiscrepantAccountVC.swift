//
//  OpenDiscrepantAccountVC.swift
//  JSBL-BB
//
//  Created by Adnan Ahmed on 22/09/2017.
//  Copyright © 2017 Inov8. All rights reserved.
//

import UIKit
import MobileCoreServices

class OpenDiscrepantAccountVC: FPAJSWalletAccountOpenBaseVC, UINavigationControllerDelegate, UIImagePickerControllerDelegate {

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
    
    @IBOutlet weak var customerPhotoBtn: UIButton!
    @IBOutlet weak var cnicPhotoBtn: UIButton!
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
                self.viewCNICPhotoHeight.constant = 143.5
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
    
    // MARK: - Overridden Methods -
 func prepareDataSource() {
        //super.prepareDataSource()
        
        let customerDetailSection = FPAJSWalletAccountOpenSection()
        customerDetailSection.title = "Customer Details"
        
        let mobileNoRow = FPAJSWalletAccountOpenRow()
        mobileNoRow.title = "Mobile Number"
        mobileNoRow.value = account.accountNo
        mobileNoRow.rowType = .summary
        customerDetailSection.rows.append(mobileNoRow)
        
        let nameRow = FPAJSWalletAccountOpenRow()
        nameRow.title = "Name"
        nameRow.value = account.name
        nameRow.rowType = .summary
        customerDetailSection.rows.append(nameRow)
        
        let cnicRow = FPAJSWalletAccountOpenRow()
        cnicRow.title = "CNIC"
        cnicRow.value = account.cnic
        cnicRow.rowType = .summary
        customerDetailSection.rows.append(cnicRow)
        let dobRow = FPAJSWalletAccountOpenRow()
        dobRow.title = "Date of Birth"
        
        dobRow.value = Utility.convertDateFormater(account.dob)
        dobRow.rowType = .summary
        dobRow.dateFormat = "(dd-mm-yyyy)"
        customerDetailSection.rows.append(dobRow)
        
        let cnicExpiryDate = FPAJSWalletAccountOpenRow()
        cnicExpiryDate.title = "CNIC Expiry Date"
        cnicExpiryDate.value = Utility.convertDateFormater(account.cnicExpiryDate)
        cnicExpiryDate.rowType = .summary
        cnicExpiryDate.dateFormat = "(dd-mm-yyyy)"
        customerDetailSection.rows.append(cnicExpiryDate)
        
        accountOpenDataSource.sections.append(customerDetailSection)
        
        
        tableView.dataSource = self
        tableView.delegate = self
        tableView.rowHeight = UITableView.automaticDimension
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
        prepareDataSource()
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
        
        btnNext.layer.cornerRadius = Constants.UI.Button.CORNER_RADIUS
        
    }
    
    override func initialization(){
        super.initialization()
    }
    
    func hideThumbnail(){
        self.customerPhotoHeight.constant = 0
        self.viewCustomerPhotoHeight.constant = 53.5
        self.cnicPhotoHeight.constant = 0
        self.viewCNICPhotoHeight.constant = 53.5
        self.customerPhotoBtn.isHidden = true
        self.cnicPhotoBtn.isHidden = true
    }
    
    override func viewDidLoad() {
        //super.viewDidLoad()
        
        // Setup UI
        setupView()
        
        self.setupHeaderBarView("Open Account", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: true)
        
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
            //Proceed Flow
            if let customerImage = customerImage {
                    account.customerImage = customerImage
            }
            if let cnicImage = cnicFrontImage {
                        account.cnicImage = cnicImage
            }
            
            let viewController = UIStoryboard(name: "SelfRegistration", bundle: nil).instantiateViewController(withIdentifier: "ConfirmRegistrationVC") as! ConfirmRegistrationVC
            viewController.account = account
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
                }else if (customerPhotoBtn.currentTitle != "Uploaded"){
                    alertMessage = "Please upload Customer Photo"
                }
                break
            case .DiscrepantWithCNICPhoto:
                if cnicFrontImage == nil{
                    alertMessage = Constants.Message.kMESSAGE_CNIC_FRONT_IMAGE_ERROR
                }else if (cnicPhotoBtn.currentTitle != "Uploaded"){
                    alertMessage = "Please upload CNIC front Photo"
                }
                break
            default:
                if customerImage == nil{
                    alertMessage = Constants.Message.kMESSAGE_CUSTOMER_IMAGE_ERROR
                }
                else if cnicFrontImage == nil{
                    alertMessage = Constants.Message.kMESSAGE_CNIC_FRONT_IMAGE_ERROR
                }else if (customerPhotoBtn.currentTitle != "Uploaded"){
                    alertMessage = "Please upload Customer Photo"
                }
                else if (cnicPhotoBtn.currentTitle != "Uploaded"){
                    alertMessage = "Please upload CNIC front Photo"
                }
                break
            }
            if alertMessage != "" {
                self.showMessage(alertMessage)
                return false
            }
            return true
        }
        return true
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
            } else if let error = error {
                self.showMessage(error.localizedDescription)
            }
            
            DispatchQueue.main.async(execute: {
                Utility.hideLoadingView(view: self.view)
            });
            
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
        
        let filename = "CustApp_\(account.accountNo)_\(imgName).png"
        
        if(imgageType == true){
            account.customerPhotoPath = filename
        }else{
            account.cnicPhotoPath = filename
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

extension NSMutableData {
    
    func appendString(string: String) {
        let data = string.data(using: String.Encoding.utf8, allowLossyConversion: true)
        append(data!)
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
