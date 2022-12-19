//
//  AppDelegate.swift
//  Timepey
//
//  Created by Adnan Ahmed on 01/06/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//



import UIKit
import IQKeyboardManagerSwift
import GoogleMaps
import Firebase

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    
    var window: UIWindow?
    var mainNavigationController : UINavigationController?
    var loadingView : ActivityLoaderView?
    var appInActiveTime: Date?
    var snapshotImage : UIImageView?
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        GMSServices.provideAPIKey("AIzaSyCU226UeHsNc_zO1-jjVydQoaISZGfV6W0")
        
        // Override point for customization after application launch.
        let storyboard: UIStoryboard  = UIStoryboard(name:"Walkthrough", bundle:nil)
        let loginVC = storyboard.instantiateViewController(withIdentifier: "MainViewController")
        self.mainNavigationController = UINavigationController.init(rootViewController: loginVC)
        self.mainNavigationController?.navigationBar.isHidden = true
        
        self.mainNavigationController?.interactivePopGestureRecognizer?.isEnabled = false
        
        self.window!.rootViewController = self.mainNavigationController
        self.window!.makeKeyAndVisible()
        UINavigationBar.appearance().barStyle = .blackOpaque
        self.loadingView = ActivityLoaderView()
        self.loadingView!.xibSetup()
        IQKeyboardManager.shared.enable = true
        
        FirebaseApp.configure()
        
        return true
    }
    
    override func canPerformAction(_ action: Selector, withSender sender: Any?) -> Bool {
        OperationQueue.main.addOperation({() -> Void in
            UIMenuController.shared.setMenuVisible(false, animated: false)
        })
        return super.canPerformAction(action, withSender: sender)
    }
    
    
    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
        
        /// Security Fixes
        Utility.destroyNetworkCache()
        appInActiveTime = nil
        appInActiveTime = Date()
        
        
        snapshotImage = UIImageView.init(frame: (self.window?.bounds)!)
        snapshotImage?.image = UIImage.init(named:"splash_logo_bg")
        self.window?.addSubview(snapshotImage!)
    }
    
    
    func applicationDidEnterBackground(_ application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
        /// Security Fixes
        appInActiveTime = nil
        appInActiveTime = Date()
        Utility.destroyNetworkCache()
    }
    
    func applicationWillEnterForeground(_ application: UIApplication) {
        /// Security Fixes
        Utility.destroyNetworkCache()
        // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
    }
    
    func applicationDidBecomeActive(_ application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
        
        if(snapshotImage != nil) {
            snapshotImage?.removeFromSuperview()
            snapshotImage = nil;
        }
        //print("applicationDidBecomeActive")
        //print(Customer.sharedInstance.appV)
        if(Customer.sharedInstance.appV != nil){
            
            if(appInActiveTime != nil){
                //print(appInActiveTime)
                let appActiveTime = Date()
                let calendar = Calendar.current
                let components = (calendar as NSCalendar).components([.second, .day, .month, .year], from: appInActiveTime!, to: appActiveTime, options: [])
                
                if(components.second! > Constants.AppConfig.APP_SESSION){
                    let baseVC = BaseViewController()
                    if let navController = UIApplication.shared.keyWindow?.rootViewController {
                        let allViewControllers = navController.children
                        if let firstVC = navController.presentedViewController{
                            if let secVC = firstVC.presentedViewController{
                                secVC.dismiss(animated: false, completion: {
                                    firstVC.dismiss(animated: false, completion: {
                                        baseVC.appSessionExpired(allViewControllers[allViewControllers.count-1])
                                    })
                                })
                            }else{
                                firstVC.dismiss(animated: false, completion: {
                                    baseVC.appSessionExpired(allViewControllers[allViewControllers.count-1])
                                })
                            }
                        }else{
                            baseVC.appSessionExpired(allViewControllers[allViewControllers.count-1])
                        }
                    }
                }
            }
        }
    }
    
    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }
    
    func applicationDidReceiveMemoryWarning(_ application: UIApplication) {
        //print("yes")
    }
    
}

