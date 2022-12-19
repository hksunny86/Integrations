//
//  ViewController.swift
//  Walkthrough
//
//  Created by Adnan Ahmed on 11/12/2017.
//  Copyright © 2017 inov8. All rights reserved.
//
import Foundation
import UIKit

class MainViewController: UIViewController, UIPageViewControllerDataSource, UIPageViewControllerDelegate {

    @IBOutlet weak var continueBtn: UIButton!
    @IBOutlet weak var loginBtn: UIButton!
    @IBOutlet weak var continueBtnBottom: NSLayoutConstraint!
    
    var pageViewController = UIPageViewController()
    //Total Number of Content Pages
    // Track the current index
    var currentIndex: Int?
    private var pendingIndex: Int?
    let defaults = UserDefaults.standard
    
    private lazy var viewControllersArray: [UIViewController] = {
        return [self.getviewController(at: 0)!,self.getviewController(at: 1)!,self.getviewController(at: 2)!,self.getviewController(at: 3)!]
    }()
    
    override func viewDidLoad() {
        
        super.viewDidLoad()
        // Create page view controller
        pageViewController = UIPageViewController(transitionStyle: .scroll, navigationOrientation: .horizontal, options: nil)
        pageViewController.dataSource = self
        pageViewController.delegate = self
        let firstVC = viewControllersArray[0]
        pageViewController.setViewControllers([firstVC], direction: .forward, animated: false) { _ in }
        addChild(pageViewController)
        view.insertSubview(pageViewController.view, at: 0)
        pageViewController.didMove(toParent: self)
        
        continueBtn.layer.cornerRadius = Constants.UI.Button.CORNER_RADIUS
        
    }
    
    private func isJailbroken() -> Bool {
        
        guard let cydiaUrlScheme = NSURL(string: "cydia://package/com.example.package") else { return false }
        if UIApplication.shared.canOpenURL(cydiaUrlScheme as URL) {
            return false
        }
        
        #if arch(i386) || arch(x86_64)
        // This is a Simulator not an idevice
        return false
        #endif
        
        let fileManager = FileManager.default
        if fileManager.fileExists(atPath: "/Applications/Cydia.app") ||
            fileManager.fileExists(atPath: "/Library/MobileSubstrate/MobileSubstrate.dylib") ||
            fileManager.fileExists(atPath: "/bin/bash") ||
            fileManager.fileExists(atPath: "/usr/sbin/sshd") ||
            fileManager.fileExists(atPath: "/etc/apt") ||
            fileManager.fileExists(atPath: "/usr/bin/ssh") ||
            fileManager.fileExists(atPath: "/private/var/lib/apt") {
            return true
        }
        
        if canOpen(path: "/Applications/Cydia.app") ||
            canOpen(path: "/Library/MobileSubstrate/MobileSubstrate.dylib") ||
            canOpen(path: "/bin/bash") ||
            canOpen(path: "/usr/sbin/sshd") ||
            canOpen(path: "/etc/apt") ||
            canOpen(path: "/usr/bin/ssh") {
            return true
        }
        
        let path = "/private/" + NSUUID().uuidString
        do {
            try "anyString".write(toFile: path, atomically: true, encoding: String.Encoding.utf8)
            try fileManager.removeItem(atPath: path)
            return true
        } catch {
            return false
        }
    }
    
    private func canOpen(path: String) -> Bool {
        let file = fopen(path, "r")
        guard file != nil else { return false }
        fclose(file)
        return true
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        if (self.isJailbroken()) {
            
            let alertView = AlertView()
            alertView.initWithTitleAndMessage(title: Constants.Message.ALERT_NOTIFICATION_TITLE, message: "Device Rooted/Jail Broken!! Data Security Compromised. Any/all actions using this application can lose private data, and therefore result in the possibility of fraudulent activity on your account.", okButtonPressed: {
                alertView.hide()
            })
            alertView.show(parentView: self.view)
            continueBtn.isHidden = true
            loginBtn.isHidden = true
            
        } else {
            continueBtn.isHidden = false
            loginBtn.isHidden = false
            let skipTutorial = defaults.bool(forKey: "skipTutorialPages")
            if(skipTutorial == true){
                pageViewController.setViewControllers([viewControllersArray[0]],
                                                      direction: UIPageViewController.NavigationDirection.forward,
                                                      animated: true,
                                                      completion: {(success) -> Void in
                })
                self.pageViewController.dataSource = nil
                continueBtn.setTitle("OPEN ACCOUNT", for: .normal)
                loginBtn.isHidden = false
                continueBtnBottom.constant = 8
            }else{
                loginBtn.isHidden = true
                continueBtnBottom.constant = -10
            }
        }
    }

    func pageViewController(_ pageViewController: UIPageViewController, viewControllerBefore viewController: UIViewController) -> UIViewController? {
        
        guard let viewControllerIndex = viewControllersArray.firstIndex(of: viewController) else {
            return nil
        }
        
        let previousIndex = viewControllerIndex - 1
        
        guard previousIndex >= 0 else {
            return nil
        }
        
        guard viewControllersArray.count > previousIndex else {
            return nil
        }
        if(currentIndex == nil){
            currentIndex = 0
        }
        return viewControllersArray[previousIndex]

    }
    
    func pageViewController(_ pageViewController: UIPageViewController, viewControllerAfter viewController: UIViewController) -> UIViewController? {
        
        guard let viewControllerIndex = viewControllersArray.firstIndex(of: viewController) else {
            return nil
        }
        
        let nextIndex = viewControllerIndex + 1
        
        let viewControllersArrayCount = viewControllersArray.count
        
        guard viewControllersArrayCount != nextIndex else {
            return nil
        }
        
        guard viewControllersArrayCount > nextIndex else {
            return nil
        }
        if(currentIndex == nil){
            currentIndex = 0
        }
        return viewControllersArray[nextIndex]
        
    }
    
    private func getviewController(at index: Int) -> UIViewController? {
        // Create a new view controller and pass suitable data.
        if(index == 0){
            return storyboard?.instantiateViewController(withIdentifier: "FirstContentVC")
        }else if(index == 1){
            return storyboard?.instantiateViewController(withIdentifier: "SecondContentVC")
        }else if(index == 2){
            return storyboard?.instantiateViewController(withIdentifier: "ThirdViewController")
        }else if(index == 3){
            return storyboard?.instantiateViewController(withIdentifier: "FourthViewController")
        }else{
            return storyboard?.instantiateViewController(withIdentifier: "ThirdViewController")
        }
    }
    
    func continueBtnProcess(currentIndex:Int){
        if(currentIndex == viewControllersArray.count-1){
            continueBtn.setTitle("OPEN ACCOUNT", for: .normal)
            loginBtn.isHidden = false
            continueBtnBottom.constant = 8
        }else{
            continueBtn.setTitle("CONTINUE", for: .normal)
            loginBtn.isHidden = true
            continueBtnBottom.constant = -10
        }
    }
    
    func pageViewController(_ pageViewController: UIPageViewController, willTransitionTo pendingViewControllers: [UIViewController]) {
        pendingIndex = viewControllersArray.firstIndex(of: pendingViewControllers.first!)
    }
    
    func pageViewController(_ pageViewController: UIPageViewController, didFinishAnimating finished: Bool, previousViewControllers: [UIViewController], transitionCompleted completed: Bool) {
        if completed {
            currentIndex = pendingIndex
            self.continueBtnProcess(currentIndex: currentIndex!)
        }
    }
    
    @IBAction func continuePressed(_ sender: UIButton) {
        let skipTutorial = defaults.bool(forKey: "skipTutorialPages")
        if(skipTutorial == true){
            let viewController = UIStoryboard(name: "SelfRegistration", bundle: nil).instantiateViewController(withIdentifier: "SelfRegistrationVC")
            Utility.pushViewController(viewController)
            return
        }else{
            if(currentIndex == nil){
                currentIndex = 0
            }
            let nextIndex = currentIndex! + 1
            let viewControllersArrayCount = viewControllersArray.count
            
            guard viewControllersArrayCount != nextIndex else {
                defaults.setValue(true, forKey:"skipTutorialPages")
                defaults.synchronize()
                let viewController = UIStoryboard(name: "SelfRegistration", bundle: nil).instantiateViewController(withIdentifier: "SelfRegistrationVC")
                Utility.pushViewController(viewController)
                return
            }
            
            guard viewControllersArrayCount > nextIndex else {
                return
            }
            
            pageViewController.setViewControllers([viewControllersArray[nextIndex]],
                                                  direction: UIPageViewController.NavigationDirection.forward,
                                                  animated: true,
                                                  completion: {(success) -> Void in
                                                    self.currentIndex = self.currentIndex! + 1
                                                    self.continueBtnProcess(currentIndex: self.currentIndex!)
            })
        }
        
        
    }
    
    @IBAction func loginPressed(_ sender: UIButton) {
        defaults.setValue(true, forKey:"skipTutorialPages")
        defaults.synchronize()
        let loginVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "LoginCustomerVC") as! LoginCustomerVC
         Utility.pushViewController(loginVC)
    }
    
}

