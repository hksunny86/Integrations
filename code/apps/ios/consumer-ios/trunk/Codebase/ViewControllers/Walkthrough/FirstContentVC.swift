//
//  FirstContentVC.swift
//  JSBL-BB
//
//  Created by Adnan Ahmed on 13/12/2017.
//  Copyright © 2017 Inov8. All rights reserved.
//

import UIKit
import AVFoundation

class FirstContentVC: UIViewController {

    var player = AVPlayer()
    var paused: Bool = false
    
    override func viewDidLoad() {
        super.viewDidLoad()

        let path = Bundle.main.path(forResource: "video", ofType: "mp4")
        player = AVPlayer(url: URL(fileURLWithPath: path!))
        let playerLayer = AVPlayerLayer(player: player)
        playerLayer.frame = CGRect(x: 0, y: 0, width: self.view.frame.width, height: self.view.frame.height)
        playerLayer.videoGravity = AVLayerVideoGravity.resizeAspectFill
        playerLayer.opacity = 1.0
        self.view.layer.addSublayer(playerLayer)
        
        
        
        player.seek(to: CMTime.zero); player.play()
        NotificationCenter.default.addObserver(self,
                                               selector: #selector(playerItemDidReachEnd(notification:)),
                                               name: NSNotification.Name.AVPlayerItemDidPlayToEndTime,
                                               object: player.currentItem)
        
    }

    @objc func playerItemDidReachEnd(notification: Notification) {
        let p: AVPlayerItem = notification.object as! AVPlayerItem
        p.seek(to: CMTime.zero)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        player.play()
        paused = false
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        player.pause()
        paused = true
    }

}
