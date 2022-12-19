import Foundation
import UIKit


class CashWithdrawalLeg1InfoVC: BaseViewController, UITableViewDelegate, UITableViewDataSource, FinancialPinPopupDelegate, AlertPopupDelegate{
    
    func okPressedChallanNo(EncMpin: String) {
        //
    }
    
   
    
    
   
    
    
    @IBOutlet weak var myTableView: UITableView!
    
    @IBOutlet weak var okButton: UIButton!
    
    var responseDict = [String:String]()
    
    var response = (XMLError(), XMLMessage(), [String:String]())
    
    var product: Product?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.setupHeaderBarView("", isBackButtonHidden: false, isHomeButtonHidden: true, isSignoutButtonHidden: false)
        
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard))
        view.addGestureRecognizer(tap)

        switch UIDevice.current.userInterfaceIdiom {
        case .phone:
            myTableView.rowHeight = 38
        case .pad:
            myTableView.rowHeight = 60
        default:
            break
        }
        
        setupView()
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        let popupView = FinancialPinPopup(nibName: "FinancialPinPopup", bundle: nil)
        popupView.delegate = self
        popupView.product = product!
        popupView.modalPresentationStyle = .overCurrentContext
        popupView.isMpinSetLater = self.isMpinSetLater
        self.present(popupView, animated: false, completion: nil)
    }
    
    //MASK: Financial PIN Delegate
    func okPressedFP() {
        //print("pin verified")
        transactionInfoPostRequest("")
        
    }
    func canclePressedFP() {
        self.popViewController()
    }
    
    func okPressedAP() {
        self.popViewController()
    }
    
    func canclePressedAP() {
        
    }
    
    func setupView(){
        
        okButton.layer.cornerRadius = 2
        myTableView.layer.cornerRadius = 4
        okButton.isHidden = true
        myTableView.isHidden = true
        myTableView.layer.borderWidth = 0.6
    }

    // MARK: UITableViewDataSource
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
       return 3
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cellIdentifier = "Cell"
        let cell: UITableViewCell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier)!
        if((indexPath as NSIndexPath).row == 0){
            if let titleLabel = cell.viewWithTag(1) as? UILabel {
                titleLabel.text = "Transaction ID"
            }
            if let valueLabel = cell.viewWithTag(2) as? UILabel {
                valueLabel.text = responseDict["TRXID"]
            }
        }else if((indexPath as NSIndexPath).row == 1){
            if let titleLabel = cell.viewWithTag(1) as? UILabel {
                titleLabel.text = "Date & Time"
            }
            if let valueLabel = cell.viewWithTag(2) as? UILabel {
                valueLabel.text = responseDict["DATEF"]
            }
        }
        else if((indexPath as NSIndexPath).row == 2){
            if let titleLabel = cell.viewWithTag(1) as? UILabel {
                titleLabel.text = "Expiry"
            }
            if let valueLabel = cell.viewWithTag(2) as? UILabel {
                valueLabel.text = responseDict["EXPIRY"]
            }
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, viewForFooterInSection section: Int) -> UIView? {
        let footerView = UIView(frame: CGRect(x:0, y:0, width:tableView.frame.size.width, height: 5))
        footerView.backgroundColor = UIColor(red:0.00, green:0.28, blue:0.56, alpha:1.0)
        return footerView
    }
    func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        return 5.0
    }
    
    @IBAction func okButtonPressed(_ sender: UIButton) {
        self.popViewControllerAndGotoStart()
    }
    
    @IBAction func signOutPressed(_ sender: UIButton) {
        super.signoutCustomer()
    }
    
    @IBAction func backButtonPressed(_ sender: UIButton) {
        self.popViewController()
    }
    
    func transactionInfoPostRequest(_ tranxCode: String) {
        
        //var responseArray = [[String:String]]()
        
        self.showLoadingView()
        
        if(Constants.AppConfig.IS_MOCK == 1){
            guard let
                xmlPath = Bundle.main.path(forResource: "Command-195-CustomerCashWithdrawal", ofType: "xml"),
                let data = try? Data(contentsOf: URL(fileURLWithPath: xmlPath))
                
                else { return }
            
            //let newStr = String(data: data, encoding: NSUTF8StringEncoding)
            //print(newStr)
            self.response = XMLParser.cashWithdrawalInfoXMLParsing(data)
            //print(self.response)
            if(self.response.0.msg != nil){
                if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                    self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                }else{
                    //self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                    let popupView = AlertPopup(nibName: "AlertPopup", bundle: nil)
                    popupView.headerLabelText = Constants.Message.ALERT_NOTIFICATION_TITLE
                    popupView.msgLabelText = self.response.0.msg!
                    popupView.delegate = self
                    popupView.actionType = "goToMainMenu"
                    popupView.cancelButtonHidden = true
                    popupView.isMpinSetLater = self.isMpinSetLater
                    popupView.modalPresentationStyle = .overCurrentContext
                    self.present(popupView, animated: false, completion: nil)
                    
                }
            }else if(self.response.1.msg != nil){
                
                //self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                let popupView = AlertPopup(nibName: "AlertPopup", bundle: nil)
                popupView.headerLabelText = Constants.Message.ALERT_NOTIFICATION_TITLE
                popupView.msgLabelText = self.response.1.msg!
                popupView.delegate = self
                popupView.actionType = "goToMainMenu"
                popupView.cancelButtonHidden = true
                popupView.isMpinSetLater = self.isMpinSetLater
                popupView.modalPresentationStyle = .overCurrentContext
                self.present(popupView, animated: false, completion: nil)

            }else{
                DispatchQueue.main.async {
                    self.responseDict = self.response.2
                    self.okButton.isHidden = false
                    self.myTableView.isHidden = false
                    self.myTableView.reloadData()
                }
            }
            self.hideLoadingView()
            
        }else{
            
            let currentTime: String = "\(Int64(Date().timeIntervalSince1970 * 1000))"
            
            let webApi : CashWithdrawalWebAPI = CashWithdrawalWebAPI()
            
            var encryptedPin: String = ""
            
            if(tranxCode != ""){
                encryptedPin = try! tranxCode.aesEncrypt(Constants.AppConfig.ENCP_KEY, iv: "")
            }
            
            
            webApi.cashWithdrawalLeg1InfoPostRequest(
                Constants.CommandId.CASH_WITHDRAWAL_LEG1_INFO,
                reqTime: currentTime,
                DTID: Constants.AppConfig.DTID_KEY,
                PID: "", //(product?.id!)!,
                PIN: "",
                ENCT: Constants.AppConfig.ENCT_KEY,
                MANUAL_OTPIN:encryptedPin,
                
                onSuccess:{(data) -> () in
                    //print(data)
                    self.response = XMLParser.cashWithdrawalInfoXMLParsing(data)
                    //print(self.response)
                    if(self.response.0.msg != nil){
                        if(self.response.0.code == Constants.ErrorCode.SESSION_EXPIRED){
                            self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "signoutSuccess", isCancelBtnHidden: true)
                        }else{
                            //self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.0.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                            let popupView = AlertPopup(nibName: "AlertPopup", bundle: nil)
                            popupView.headerLabelText = Constants.Message.ALERT_NOTIFICATION_TITLE
                            popupView.msgLabelText = self.response.0.msg!
                            popupView.delegate = self
                            popupView.actionType = "goToMainMenu"
                            popupView.cancelButtonHidden = true
                            popupView.isMpinSetLater = self.isMpinSetLater
                            popupView.modalPresentationStyle = .overCurrentContext
                            self.present(popupView, animated: false, completion: nil)
                        }
                    }else if(self.response.1.msg != nil){
                        
                        
                        //self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: self.response.1.msg!, actionType: "serverFailure", isCancelBtnHidden: true)
                        
                        let popupView = AlertPopup(nibName: "AlertPopup", bundle: nil)
                        popupView.headerLabelText = Constants.Message.ALERT_NOTIFICATION_TITLE
                        popupView.msgLabelText = self.response.1.msg!
                        popupView.delegate = self
                        popupView.actionType = "goToMainMenu"
                        popupView.cancelButtonHidden = true
                        popupView.isMpinSetLater = self.isMpinSetLater
                        popupView.modalPresentationStyle = .overCurrentContext
                        self.present(popupView, animated: false, completion: nil)
                        
                    }else{
                        DispatchQueue.main.async {
                            self.responseDict = self.response.2
                            self.okButton.isHidden = false
                            self.myTableView.isHidden = false
                            self.myTableView.reloadData()
                        }
                    }
                    self.hideLoadingView()
            },
                onFailure: {(reason) ->() in
                    //print("Failure")
                    //self.showNotificationPopup(Constants.Message.ALERT_NOTIFICATION_TITLE, msgLabelText: reason, actionType: "serverFailure", isCancelBtnHidden: true)
                    DispatchQueue.main.async {
                        let popupView = AlertPopup(nibName: "AlertPopup", bundle: nil)
                        popupView.headerLabelText = Constants.Message.ALERT_NOTIFICATION_TITLE
                        popupView.msgLabelText = reason
                        popupView.delegate = self
                        popupView.actionType = "goToMainMenu"
                        popupView.cancelButtonHidden = true
                        popupView.isMpinSetLater = self.isMpinSetLater
                        popupView.modalPresentationStyle = .overCurrentContext
                        self.present(popupView, animated: false, completion: nil)
                        
                    }
                    self.hideLoadingView()
            })
        }
    }
    
}
 
