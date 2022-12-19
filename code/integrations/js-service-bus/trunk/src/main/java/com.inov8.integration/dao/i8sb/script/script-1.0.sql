  CREATE TABLE LINKED_REQUEST
   (
    CLIENT_ID      VARCHAR2(20)  NOT NULL
  , TERMINAL_ID    VARCHAR2(20)  NOT NULL
  , CHANNEL_ID     VARCHAR2(20)  NOT NULL
  , REQUEST_TYPE   VARCHAR2(100) NOT NULL
  , LINKED_REQUEST VARCHAR2(500) NOT NULL
    ,
    DEPENDENCY     VARCHAR2(500) NOT NULL
  );

  CREATE TABLE TRANSACTION_LOG
(
  ID NUMBER NOT NULL
, GATEWAY   VARCHAR2(20)
, CLIENT_ID VARCHAR2(20)
, TERMINAL_ID VARCHAR2(20)
, CHANNEL_ID VARCHAR2(20)
, RRN VARCHAR2(30)
, REQUEST_TYPE VARCHAR2(100)
, PARENT_REQUEST_RRN VARCHAR2(30)
, I8SB_REQUEST CLOB
, I8SB_RESPONSE CLOB
, CHANNEL_REQUEST CLOB
, CHANNEL_RESPONSE CLOB
, RESPONSE_CODE VARCHAR2(20)
, STATUS VARCHAR2(30)
, ERROR VARCHAR2(255)
, REQUEST_DATE_TIME DATE
, RESPONSE_DATE_TIME DATE
);


  CREATE SEQUENCE  "I8SB_DEV"."TRANSACTION_LOG_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999
  INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE ;


  INSERT INTO linked_request (client_id, terminal_id, channel_id, request_type, linked_request, dependency)
  VALUES ('JSBL','MB', 'RDV_MB', 'CustomerValidation', 'PanPinVerification|CNICValidation', '0|1');
  INSERT INTO linked_request (client_id, terminal_id, channel_id, request_type, linked_request, dependency)
  VALUES ('JSBL', 'MB', 'RDV_MB', 'GetMyAccounts', 'CheckingAccountSummary|LoanAccountSummary|TDRAccountSummary', '0|0|0');
  INSERT INTO linked_request (client_id, channel_id, request_type, linked_request, dependency, terminal_id)
  VALUES ('MBL', 'RDV_WS', 'AuthenticateCustomer', 'AuthenticateCustomer|IVRPasscode', '0|1', 'MB');
  INSERT INTO linked_request (client_id, channel_id, request_type, linked_request, dependency, terminal_id)
  VALUES ('MBL', 'RDV_WS', 'RegisterUser', 'AuthenticateOTP|RegisterUser', '0|1', 'MB');
  INSERT INTO linked_request (client_id, channel_id, request_type, linked_request, dependency, terminal_id)
  VALUES ('MBL', 'RDV_WS', 'PanPinVerification', 'PanPinVerification|GenerateOTP', '0|1', 'MB');
  INSERT INTO linked_request (client_id, channel_id, request_type, linked_request, dependency, terminal_id)
  VALUES ('MBL', 'RDV_WS', 'ForgetCredentials', 'AuthenticateOTP|ChangePassword', '0|1', 'MB');
  INSERT INTO linked_request (client_id, channel_id, request_type, linked_request, dependency, terminal_id)
  VALUES ('JSBL', 'APIGEE', 'AccountBalanceInquiry', 'AccessToken|AccountBalanceInquiry', '0|1', 'BLB');
  INSERT INTO linked_request (client_id, channel_id, request_type, linked_request, dependency, terminal_id)
  VALUES ('JSBL', 'APIGEE', 'CashWithdrawal', 'AccessToken|CashWithdrawal', '0|1', 'BLB');
    INSERT INTO linked_request (client_id, channel_id, request_type, linked_request, dependency, terminal_id)
  VALUES ('JSBL', 'APIGEE', 'PayMTCN', 'PayMtcnAccessToken|PayMTCN', '0|1', 'BLB');
  INSERT INTO linked_request (client_id, channel_id, request_type, linked_request, dependency, terminal_id)
  VALUES ('JSBL', 'APIGEE', 'CashWithdrawalReversal', 'AccessToken|CashWithdrawalReversal', '0|1', 'BLB');
    INSERT INTO linked_request (client_id, channel_id, request_type, linked_request, dependency, terminal_id)
  VALUES ('JSBL', 'APIGEE', 'EOBITitleFetch', 'EOBIAccessToken|EOBITitleFetch', '0|1', 'BLB');
  INSERT INTO linked_request (client_id, channel_id, request_type, linked_request, dependency, terminal_id)
  VALUES ('JSBL', 'APIGEE', 'EOBICashWithdrawal', 'EOBIAccessToken|EOBICashWithdrawal', '0|1', 'BLB');
  INSERT INTO linked_request (client_id, channel_id, request_type, linked_request, dependency, terminal_id)
  VALUES ('MBL', 'RDV_WS', 'ForgetUsername', 'AuthenticateOTP|Email', '0|1', 'MB');
  INSERT INTO linked_request (client_id, channel_id, request_type, linked_request, dependency, terminal_id)
  VALUES ('MBL', 'RDV_WS', 'AddEForm', 'AddEForm|DoActivity', '0|1', 'MB');
  Insert into linked_request (CLIENT_ID,CHANNEL_ID,REQUEST_TYPE,LINKED_REQUEST,DEPENDENCY,TERMINAL_ID) values ('JSBL','JSDEBITCARDAPI','CardReissuance','GetCardDetails|CardReissuance','0|1','BLB');


  Commit;