//
//  RouteVC.swift
//  Timepey
//
//  Created by Adnan Ahmed on 16/10/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//
import Foundation
import CoreLocation
import MapKit
import GoogleMaps
import SwiftyJSON

class RouteVC: BaseViewController, GMSMapViewDelegate {
    
    @IBOutlet weak var myMapView: GMSMapView!
    
    
    var locatorType: String?
    var userLocation: CLLocation?
    var branch: BranchLocator?
    
    override func viewDidLoad() {
        
        self.showLoadingView()
        
        myMapView.delegate = self
        self.myMapView?.isMyLocationEnabled = false
        
        self.myMapView.camera = GMSCameraPosition.camera(withTarget: (self.userLocation?.coordinate)!, zoom: 5)
        
        if(Customer.sharedInstance.appV == nil){
            self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: true)
        }else{
            self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: false, isSignoutButtonHidden: false)
        }
        calculateRoutesSource((userLocation?.coordinate)!, destination: (branch?.location)!)
    }
    
    
    func calculateRoutesSource(_ source: CLLocationCoordinate2D, destination desitnation: CLLocationCoordinate2D) {
        
        let saddr = "\(source.latitude),\(source.longitude)"
        let daddr = "\(desitnation.latitude),\(desitnation.longitude)"
        let apiUrlStr = "https://maps.googleapis.com/maps/api/directions/json?origin=\(saddr)&destination=\(daddr)&mode=driving&sensor=false"
        let apiUrl = NSURL(string: apiUrlStr)!
        var request = URLRequest(url: apiUrl as URL)
        request.httpMethod = "GET"
        
        let reachabilityManager = Reachability.isConnectedToNetwork()
        
        if(reachabilityManager != true){
            self.showNoInternetPopup()
            self.hideLoadingView()
        }else{
            let defaultSession = URLSession(configuration: URLSessionConfiguration.default)
            
            defaultSession.dataTask(with: request){
                data, response, error in
                //print(response!)
                if let httpResponse = response as? HTTPURLResponse {
                    if(httpResponse.statusCode == Constants.HTTPStatusCode.SUCCESS){
                        
                        let result = try! JSON(data: data!)
                        //print(result["routes"][0]["overview_polyline"]["points"])
                        DispatchQueue.main.async {
                        
                            //Add Marker
                            let sourceMarker = GMSMarker(position: (self.userLocation?.coordinate)!)
                            //sourceMarker.groundAnchor = CGPoint.init(x: 1, y: 1)
                            sourceMarker.title = "Current Location"
                            sourceMarker.icon = UIImage(named: "current_pin.png")
                            sourceMarker.map = self.myMapView
                            
                            let destinationMarker = GMSMarker(position: (self.branch?.location)!)
                            //destinationMarker.groundAnchor = CGPoint.init(x: 0.5, y: 0.5)
                            destinationMarker.title = (self.branch?.name!)!
                            if(self.locatorType == "Branch"){
                                destinationMarker.icon = UIImage(named: "bank_icon.png")
                            }else if(self.locatorType == "ATM"){
                                destinationMarker.icon = UIImage(named: "atm_icon.png")
                            }else{
                                destinationMarker.icon = UIImage(named: "agent_icon.png")
                            }
                            destinationMarker.map = self.myMapView
                            
                            //Add PolyLine
                            let route = (result["routes"][0]["overview_polyline"]["points"]).stringValue
                            
                            let path: GMSPath = GMSPath(fromEncodedPath: route)!
                            
                            let rectangle = GMSPolyline(path: path)
                            rectangle.strokeWidth = 2.0
                            rectangle.map = self.myMapView

                            self.myMapView.animate(to: GMSCameraPosition.camera(withTarget: (self.userLocation?.coordinate)!, zoom: 12))
                            
                        }
                    }
                    
                }
                self.hideLoadingView()
                }.resume()
        }
    }
    
    
}
