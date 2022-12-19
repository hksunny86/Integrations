//
//  LocatorVC.swift
//  Timepey
//
//  Created by Adnan Ahmed on 16/10/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import Foundation
import UIKit
import MapKit
import CoreLocation

class CustomPointAnnotation: MKPointAnnotation {
    var imageName: String!
}

class LocatorVC: BaseViewController, UITableViewDelegate, UITableViewDataSource, MKMapViewDelegate, CLLocationManagerDelegate, CustomPickerViewDelegate {
    
    
    @IBOutlet weak var myTableView: UITableView!
    @IBOutlet weak var myMapKit: MKMapView!
    @IBOutlet weak var btnRadius: UIButton!
    @IBOutlet weak var branchBtnTrailingMargin: NSLayoutConstraint!
    
    var response = (XMLError(), XMLMessage(), [BranchLocator](), String())
    var arrAnnotations = [AnyObject]()
    var locatorType: String = "Branch"
    let regionRadius: CLLocationDistance = 1000
    let locationManager = CLLocationManager()
    var isFullScreen: Bool = false
    var requestType: String?
    var radius = "5"
    let arrRadius = ["5","10","15","20"]
    let arrLocator = ["Branch","ATM", "Agent"]
    var selectedRadius: String?
    var selectedLocator: String?
    let pickerView = CustomPickerView(nibName: "CustomPickerView", bundle: nil)
    var isViewDidLoad: Bool = false
    //Pagging
    var pageNo = 1
    var pageSize = 8
    var totalCount = 0
    
    let confirmationAlertView = AlertView()
    
    override func viewDidLoad() {
        self.isLocator = false
        isViewDidLoad = true
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: false)
        
//        btnLocator.setTitle(arrLocator.first!, for: .normal)
//        self.view.bringSubview(toFront: btnLocator)
        btnRadius.setTitle("\(arrRadius.first!) KM", for: .normal)
        self.view.bringSubviewToFront(btnRadius)
        
        
        myTableView.delegate = self
        locationManager.delegate = self
        locationManager.distanceFilter = kCLDistanceFilterNone
        locationManager.desiredAccuracy = kCLLocationAccuracyBestForNavigation
        locationManager.requestWhenInUseAuthorization()
        locationManager.startUpdatingLocation()
        if CLLocationManager.locationServicesEnabled(){
            if (CLLocationManager.authorizationStatus() == .authorizedWhenInUse) {
                if(locationManager.location != nil){
                    locatorPostRequest(false, locator: "", userLocation: locationManager.location!, pageNo: self.pageNo, pageSize: self.pageSize)
                }
            }else{
                
                confirmationAlertView.initWithTitleAndMessageWithCancelButton(title:"LOCATION SERVICE", message: "Please enable Location Services from Settings.", okButtonPressed: {
                    if (CLLocationManager.authorizationStatus() == .authorizedWhenInUse) {
                        if(self.locationManager.location != nil) {
                            self.confirmationAlertView.hide()
                        }
                    } else {
                        
                        UIApplication.shared.openURL(URL(string:UIApplication.openSettingsURLString)!)
                    }
                }, cancelButtonPressed: {
                    self.confirmationAlertView.hide()
                    self.popViewController()
                    
                })
                confirmationAlertView.show(parentView: self.view)
            }
        }
        else {
            confirmationAlertView.initWithTitleAndMessageWithCancelButton(title:"LOCATION SERVICE", message: "Please enable Location Services from Settings.", okButtonPressed: {
                if (CLLocationManager.authorizationStatus() == .authorizedWhenInUse) {
                    if(self.locationManager.location != nil) {
                        self.confirmationAlertView.hide()
                    }
                } else {
                    
                    UIApplication.shared.openURL(URL(string:UIApplication.openSettingsURLString)!)
                }
            }, cancelButtonPressed: {
                self.confirmationAlertView.hide()
                self.popViewController()
                
            })
            confirmationAlertView.show(parentView: self.view)
        }
    }
    
    override func viewWillLayoutSubviews() {
        if(Customer.sharedInstance.appV == nil){
            branchBtnTrailingMargin.constant = 0
        }
    }
    
    
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        
        if(CLLocationManager.locationServicesEnabled()){
            switch(CLLocationManager.authorizationStatus()) {
            case .notDetermined:
                break
            case .restricted, .denied:
                break
            case .authorizedWhenInUse:
                self.confirmationAlertView.hide()
                locationManager.startUpdatingLocation()
                if(locationManager.location != nil){
                    locatorPostRequest(false, locator: "Branch", userLocation: locationManager.location!, pageNo: self.pageNo, pageSize: self.pageSize)
                }
            default:
                break
            }
            
        }else{
            
//            DispatchQueue.main.async(execute: {
//                
//                let confirmationAlertView = AlertView()
//                confirmationAlertView.initWithTitleAndMessageWithCancelButton(title:"LOCATION SERVICE", message: "Please enable Location Services from Settings.", okButtonPressed: {
//                    
//                    if (CLLocationManager.authorizationStatus() == .authorizedWhenInUse) {
//                        if(self.locationManager.location != nil) {
//                            confirmationAlertView.hide()
//                        }
//                    } else {
//                        
//                        UIApplication.shared.openURL(URL(string:UIApplicationOpenSettingsURLString)!)
//                    }
//                    
//                }, cancelButtonPressed: {
//                    confirmationAlertView.hide()
//                    self.popViewController()
//                    
//                })
//                confirmationAlertView.show(parentView: self.view)
//            })
        }
        
    }
    
    func locationUpdated(_ locationManager: CLLocationManager, failWithError error: Error?, authorizationStatus: CLAuthorizationStatus) {
        
        DispatchQueue.main.async(execute: {
            
            self.confirmationAlertView.initWithTitleAndMessageWithCancelButton(title:"LOCATION SERVICE", message: "Please enable Location Services from Settings.", okButtonPressed: {
                
                if (CLLocationManager.authorizationStatus() == .authorizedWhenInUse) {
                    if(self.locationManager.location != nil) {
                        self.confirmationAlertView.hide()
                    }
                } else {
                    UIApplication.shared.openURL(URL(string:UIApplication.openSettingsURLString)!)
                }
                
            }, cancelButtonPressed: {
                self.confirmationAlertView.hide()
                self.popViewController()
                
            })
            self.confirmationAlertView.show(parentView: self.view)
        })
    }
    
    
    // MARK: CLLocationManagerDelegate
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        
        let location = locations.last! as CLLocation
        
        let center = CLLocationCoordinate2D(latitude: location.coordinate.latitude, longitude: location.coordinate.longitude)
        let region = MKCoordinateRegion(center: center, span: MKCoordinateSpan(latitudeDelta: 0.1, longitudeDelta: 0.1))
        
        self.myMapKit.setRegion(region, animated: true)
        
        myMapKit.showsUserLocation = false
        
        
        locationManager.stopUpdatingLocation()
    }
    
    
   
    
    // MARK: MKMapViewDelegate
    func mapView(_ mapView: MKMapView, viewFor annotation: MKAnnotation) -> MKAnnotationView? {
        if !(annotation is CustomPointAnnotation) {
            return nil
        }
        
        let reuseId = "custom"
        
        var anView = mapView.dequeueReusableAnnotationView(withIdentifier: reuseId)
        if anView == nil {
            anView = MKAnnotationView(annotation: annotation, reuseIdentifier: reuseId)
            anView?.canShowCallout = true
        }
        else {
            anView?.annotation = annotation
        }
        
        let cpa = annotation as! CustomPointAnnotation
        anView?.image = UIImage(named:cpa.imageName)
        
        return anView
    }
    
    // MARK: TableView-delegate-methods
    func numberOfSections(in tableView: UITableView) -> Int {
        return response.2.count
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        return 10.0
    }
    
    func tableView(_ tableView: UITableView, viewForFooterInSection section: Int) -> UIView? {
        let headerView = UIView()
        headerView.backgroundColor = UIColor.clear
        return headerView
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cellIdentifier = "Cell"
        let cell: UITableViewCell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier)!
        
        let view = cell.viewWithTag(22)
        
        view?.layer.cornerRadius = 2
        view?.layer.shadowColor = UIColor.lightGray.cgColor
        view?.layer.shadowOffset = CGSize(width: 0, height: 0)
        view?.layer.borderWidth = 0
        view?.layer.shadowRadius = 2
        view?.layer.shadowOpacity = 0.4
        view?.layer.cornerRadius = 2
        
        let nameLabel = cell.viewWithTag(11) as? UILabel
        if let name = response.2[indexPath.section].name {
                nameLabel?.text = name
        }
        
        let addressLabel = cell.viewWithTag(12) as? UILabel
        
        if let address = response.2[indexPath.section].address {
            addressLabel?.text = address
        }
        
        let distanceLabel = cell.viewWithTag(13) as? UILabel
        
        if let distance = response.2[indexPath.section].distance {
            distanceLabel?.text = distance
        }
        
        if indexPath.section == response.2.count - 1 { // last cell
            if totalCount > response.2.count { //removing totalItems for always service call
                locatorPostRequest(false, locator: "", userLocation: locationManager.location!, pageNo: self.pageNo, pageSize: self.pageSize)
            }
        }
        
        return cell
    }

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        if(Reachability.isConnectedToNetwork() == true){
            let viewController = UIStoryboard(name: "Locator", bundle: nil).instantiateViewController(withIdentifier: "RouteVC") as! RouteVC
            
            viewController.locatorType = locatorType
            
            viewController.userLocation = locationManager.location
            viewController.branch = response.2[indexPath.section]
            self.pushViewController(viewController)
            
            tableView.deselectRow(at: indexPath, animated: true)
        }else{
            self.showNoInternetPopup()
        }
        
    }
    
    @IBAction func radiusButtonPressed(_ sender: UIButton) {
        pickerView.screenTitle = "Choose Radius"
        pickerView.isRadius = true
        pageNo = 1
        response.2 = [BranchLocator]()
        if(selectedRadius == nil){
            pickerView.selectedRadius = arrRadius.first
        }else{
            pickerView.selectedRadius = selectedRadius
        }
        
        pickerView.dataSource = arrRadius
        pickerView.delegate = self
        pickerView.modalPresentationStyle = .overCurrentContext
        self.present(pickerView, animated: false, completion: nil)
    }

    @IBAction func locatorButtonPressed(_ sender: UIButton) {
        pickerView.screenTitle = "Select Locator"
        pickerView.isRadius = false
        if(selectedLocator == nil){
            pickerView.selectedLocator = arrLocator.first
        }else{
            pickerView.selectedLocator = selectedLocator
        }
        pickerView.dataSource = arrLocator
        pickerView.delegate = self
        pickerView.modalPresentationStyle = .overCurrentContext
        self.present(pickerView, animated: false, completion: nil)
    }
    
    func pickerViewSelected(isRadius:Bool, selectedValue: String){
        if (CLLocationManager.authorizationStatus() == .authorizedWhenInUse) {
            if(locationManager.location != nil){
                locatorPostRequest(isRadius, locator: selectedValue, userLocation: locationManager.location!, pageNo: self.pageNo, pageSize: self.pageSize)
            }
        }
    }
    
    func locatorPostRequest(_ isRadius: Bool, locator: String, userLocation: CLLocation, pageNo:Int, pageSize:Int) {
        self.showLoadingView()
        
        var response = (XMLError(), XMLMessage(), [BranchLocator](), String())
        
        if(isRadius == true){
            radius = locator
        }else{
            //requestType = "0"
            if(locator == "Branch"){
                requestType = "0"
                locatorType = "Branch"
            }else if(locator == "ATM"){
                requestType = "1"
                locatorType = "ATM"
            }else{
                requestType = "2"
                locatorType = "Agent"
            }
        }
        
        
        if(Constants.AppConfig.IS_MOCK == 1) {
            
            
            
            guard let
                xmlPath = Bundle.main.path(forResource: "Command-182-branch-locator", ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                
                else { return }
            response = XMLParser.branchATMLocatorXMLParsing(data)
            
            if(isRadius == true){
                btnRadius.setTitle("\(locator) KM", for: .normal)
            }else{
                
            }
            
            DispatchQueue.main.async {
                self.myTableView.reloadData()
            }
            addPinsOnMap(isBranch: true)
            self.hideLoadingView()
        }else{
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let myAccApi : MyAccountWebAPI = MyAccountWebAPI()
            
            
            
            myAccApi.locatorPostRequest(
                Constants.CommandId.LOCATOR,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                CITY: "",
                RADIUS: radius,
                LATITUDE: "\(userLocation.coordinate.latitude)",
                LONGITUDE:  "\(userLocation.coordinate.longitude)",
                PAGENO: "\(pageNo)",
                PAGESIZE: "\(pageSize)",
                TYPE: "2",//requestType!,
                onSuccess:{(data) -> () in
                    //let newStr = String(data: data, encoding: String.Encoding.utf8)
                    //print(newStr)
                    response = XMLParser.branchATMLocatorXMLParsing(data)
                    //print(self.response)
                    if(response.0.msg != nil){
                        if(response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                        }else{
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                        }
                    }else if(response.1.msg != nil){
                        self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                        
                    }else{
                        DispatchQueue.main.async {
                            if(isRadius == true){
                                self.btnRadius.setTitle("\(locator) KM", for: .normal)
                                self.selectedRadius = locator
                            }else{
                                self.selectedLocator = locator
                            }
                            self.response.2 += response.2
                            self.myTableView.reloadData()
                            
                            self.pageNo += 1
                            if let count = Int(response.3){
                                self.totalCount = count
                            }
                            if(self.requestType == "1"){
                                self.addPinsOnMap(isBranch: true)
                            }else{
                                self.addPinsOnMap(isBranch: false)
                            }
                            
                        }
                    }
                    DispatchQueue.main.async {
                        self.hideLoadingView()
                    }
                    
            },
                onFailure: {(reason) ->() in
                    //print("Failure")
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                    self.hideLoadingView()
            })
        }
    }
    
    func addPinsOnMap(isBranch: Bool) {
        myMapKit.removeAnnotations(arrAnnotations as! [MKAnnotation])
        arrAnnotations.removeAll()
        for i in 0..<response.2.count {
            let branch = response.2[i]
            
            let pointAnnotation = CustomPointAnnotation()
            pointAnnotation.title = branch.name
            pointAnnotation.coordinate = branch.location!
            if(requestType == "0"){
                pointAnnotation.imageName = "bank_icon"
            }else if(requestType == "1"){
                pointAnnotation.imageName = "atm_icon"
            }else if(requestType == "2"){
                pointAnnotation.imageName = "agent_icon"
            }
            
            myMapKit.addAnnotation(pointAnnotation)
            arrAnnotations.append(pointAnnotation)
        }
        myTableView.reloadData()
    }
    
    @IBAction func fullScreenButtonPressed(_ sender: UIButton) {
        UIView.animate(withDuration: 0.8, animations: {
            if(self.isFullScreen == false){
                var frame: CGRect = self.myTableView.frame
                frame.origin.y += frame.size.height
                self.myTableView.frame = frame
                frame = self.myMapKit.frame
                frame.size.height *= 2
                self.myMapKit.frame = frame
            }else{
                var frame: CGRect = self.myTableView.frame
                frame.origin.y -= frame.size.height
                self.myTableView.frame = frame
                frame = self.myMapKit.frame
                frame.size.height /= 2
                self.myMapKit.frame = frame
            }
            //self.myTableView.isHidden = !(self.isFullScreen)
            self.isFullScreen = !(self.isFullScreen)
        })
    }
    
}

