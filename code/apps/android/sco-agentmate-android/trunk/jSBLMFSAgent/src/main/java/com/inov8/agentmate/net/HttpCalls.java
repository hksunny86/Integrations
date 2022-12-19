package com.inov8.agentmate.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;

import android.app.Application;
import android.content.Context;
import android.net.http.X509TrustManagerExtensions;
import android.util.Base64;

import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.util.AppLogger;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.PreferenceConnector;
import com.inov8.agentmate.util.Utility;
import com.inov8.jsbl.sco.BuildConstants;
import com.inov8.jsbl.sco.R;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class HttpCalls {
    private HttpResponseModel serverResponse = null;
    private static HttpCalls instance = null;
    private HttpURLConnection urlConnection;
    private final Context context;

    private HttpCalls(Context appContext) {
        context = appContext;
    }

    public static HttpCalls getInstance(Context appContext) {
        instance = new HttpCalls(appContext);
        instance.init();
        return instance;
    }

    private URL getUrl() {
        String appUrl = null;

        if (ApplicationData.nadraCall)
            appUrl = Constants.NADRA_VERIFICATION_URL;

        else if (ApplicationData.nadraOtcCall)
            appUrl = Constants.NADRA_OTC_URL;

        else
            appUrl = Constants.APPURL;

        if (ApplicationData.isCustomIP) {
            appUrl = setCustomIP();
        }

        String path = String.format(appUrl);
        AppLogger.i("Server IP: " + path);

        URL url = null;
        try {
            url = new URL(appUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private void init() {
        try {
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    HostnameVerifier hv =
                            HttpsURLConnection.getDefaultHostnameVerifier();
                    return hv.verify(BuildConstants.HOSTNAME, session);
                }
            };

            if (Constants.PROTOCOL == "https://") {
                urlConnection = (HttpsURLConnection) getUrl().openConnection();
            }
            else {
                urlConnection = (HttpURLConnection) getUrl().openConnection();
            }
            urlConnection.setConnectTimeout(Constants.CONNECTION_TIMEOUT * 1000);
            urlConnection.setReadTimeout(Constants.CONNECTION_TIMEOUT * 1000);
            urlConnection.setDoOutput(true);

            if (ApplicationData.nadraCall || ApplicationData.nadraOtcCall)
                urlConnection.setRequestProperty("Content-Type", "application/json");
            else
                urlConnection.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");
            urlConnection.setRequestProperty("Content-Language", "en-US");

            if (Constants.PROTOCOL.equals("https://")) {
                SSLSocketFactory sslSocketFactory = getSSLSocketFactory();
                if (sslSocketFactory != null) {
                    ((HttpsURLConnection) urlConnection).setSSLSocketFactory(sslSocketFactory);
                    ((HttpsURLConnection) urlConnection).setHostnameVerifier(hostnameVerifier);
                    ((HttpsURLConnection) urlConnection).connect();

                    Set<String> validPins = Collections.singleton(Constants.SECURITY_KEY);
                    validatePinning(getX509TrustManagerExt(), ((HttpsURLConnection) urlConnection), validPins);

                } else {
                    urlConnection.disconnect();
                    instance = null;
                }
            }
        } catch (SSLPeerUnverifiedException puex) {
            puex.printStackTrace();
            urlConnection.disconnect();
            instance = null;
        } catch (SSLException ex) {
            ex.printStackTrace();
            urlConnection.disconnect();
            instance = null;
        } catch (IOException ioex) {
            ioex.printStackTrace();
            urlConnection.disconnect();
            instance = null;
        } catch (Exception e) {
            e.printStackTrace();
            urlConnection.disconnect();
            instance = null;
        }
    }

    private X509TrustManagerExtensions getX509TrustManagerExt() {
        X509TrustManagerExtensions trustManagerExt = null;
        try {
            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(
                            TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            // Find first X509TrustManager in the TrustManagerFactory
            X509TrustManager x509TrustManager = null;
            for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
                if (trustManager instanceof X509TrustManager) {
                    x509TrustManager = (X509TrustManager) trustManager;
                    break;
                }
            }
            trustManagerExt = new X509TrustManagerExtensions(x509TrustManager);

        } catch (NoSuchAlgorithmException e) {
            AppLogger.i("NoSuchAlgorithmException: " + e.getMessage());
        } catch (KeyStoreException e) {
            AppLogger.i("KeyStoreException: " + e.getMessage());
        }

        return trustManagerExt;
    }

    private void validatePinning(
            X509TrustManagerExtensions trustManagerExt,
            HttpsURLConnection conn, Set<String> validPins)
            throws SSLException {
        String certChainMsg = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            List<X509Certificate> trustedChain = trustedChain(trustManagerExt, conn);

            for (Certificate cert : trustedChain) {
                byte[] publicKey = cert.getPublicKey().getEncoded();
                md.update(publicKey, 0, publicKey.length);
                String pin = Base64.encodeToString(md.digest(), Base64.NO_WRAP);
                if (validPins.contains(pin)) {
                    return;
                }
            }
        } catch (NoSuchAlgorithmException e) {
            throw new SSLException(e);
        }
        throw new SSLPeerUnverifiedException("Certificate pinning " +
                "failure\n  Peer certificate chain:\n" + certChainMsg);
    }

    private List<X509Certificate> trustedChain(X509TrustManagerExtensions trustManagerExt,
                                               HttpsURLConnection conn) throws SSLException {
        Certificate[] serverCerts = conn.getServerCertificates();
        X509Certificate[] untrustedCerts = Arrays.copyOf(serverCerts,
                serverCerts.length, X509Certificate[].class);
        String host = conn.getURL().getHost();
        try {
            return trustManagerExt.checkServerTrusted(untrustedCerts,
                    "RSA", host);
        } catch (CertificateException e) {
            throw new SSLException(e);
        }
    }

    private SSLSocketFactory getSSLSocketFactory() {
        InputStream caInput = null;
        Certificate ca = null;
        SSLContext sslContext = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            caInput = context.getResources().openRawResource(BuildConstants.cer);

            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
        } catch (KeyStoreException e) {
            AppLogger.i("Exception: " + e.getMessage());
        } catch (CertificateException ce) {
            AppLogger.i("CertificateException: " + ce.getMessage());
        } catch (NoSuchAlgorithmException e) {
            AppLogger.i("Exception: " + e.getMessage());
        } catch (KeyManagementException e) {
            AppLogger.i("Exception: " + e.getMessage());
        } catch (IOException e) {
            AppLogger.i("Exception: " + e.getMessage());
        } finally {
            if (caInput != null) {
                try {
                    caInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sslContext.getSocketFactory();
    }

    private String setCustomIP() {

        String ip = null;

        if (ApplicationData.nadraCall)
            ip = Constants.NADRA_VERIFICATION_URL;
        else if (ApplicationData.nadraOtcCall)
            ip = Constants.NADRA_OTC_URL;
        else
            ip = PreferenceConnector.readString(context,
                    PreferenceConnector.CUSTOM_IP, Constants.APPURL);
        return ip;
    }

    public HttpResponseModel changePin(int command, String oldPIN,
                                       String newPIN, String confirmPIN) throws Exception {
        Long time = new Date().getTime();

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + time.toString() + "\">" + "" + "<params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PIN\">"
                + oldPIN + "</param>" + "<param name=\"NPIN\">" + newPIN
                + "</param>" + "<param name=\"CPIN\">" + confirmPIN
                + "</param>" + "<param name=\"ENCT\">"
                + Constants.ENCRYPTION_TYPE + "</param>" + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }
    public HttpResponseModel setPin(int command, String newPIN,String confirmPIN) throws Exception {

        Long time = new Date().getTime();

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + time.toString() + "\">" + ""
                + "<params>"
                + "<param name=\"DTID\">" + "5" + "</param>"
                + "<param name=\"NPIN\">" + newPIN + "</param>"
                + "<param name=\"CPIN\">" + confirmPIN + "</param>"
                + "<param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel checkBalance(int command, String pin,
                                          String acctType, String apid, String bbacid) throws Exception {
        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PIN\">"
                + pin + "</param>" + "<param name=\"ENCT\">"
                + Constants.ENCRYPTION_TYPE + "</param>"
                + "<param name=\"ACCTYPE\">"
                + StringEscapeUtils.escapeXml(acctType) + "</param>"
                + "<param name=\"APID\">" + apid + "</param>"
                + "<param name=\"BBACID\">"
                + StringEscapeUtils.escapeXml(bbacid) + "</param>"
                + "<param name=\"APPID\">" + Constants.APPID + "</param>"
                + "</params></msg>";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel agentToAgentTransfer(int command, String pin,
                                                  String pid, String ramob, String amob, String txam, String camt,
                                                  String tpam, String tamt) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + pid + "</param>" + "<param name=\"PIN\">" + pin
                + "</param><param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE
                + "</param>" + "<param name=\"RAMOB\">" + ramob + "</param>"
                + "<param name=\"AMOB\">" + amob + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "<param name=\"CAMT\">" + camt + "</param>"
                + "<param name=\"TPAM\">" + tpam + "</param>"
                + "<param name=\"TAMT\">" + tamt + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel login(int command, String username,
                                   String password, String cvno, String isRooted, String osVer,
                                   String model, String vendor, String network, String udid) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\">"
                + "<params>"
                + "<param name=\"PIN\">" + password + "</param>"
                + "<param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE + "</param>"
                + "<param name=\"UID\">" + StringEscapeUtils.escapeXml(username) + "</param>"
                + "<param name=\"CVNO\">" + cvno + "</param>"
                + "<param name=\"APPV\">" + Constants.APPLICATION_VERSION + "</param>"
                + "<param name=\"DTID\">5</param>"
                + "<param name=\"ISROOTED\">" + isRooted + "</param>"
                + "<param name=\"OS\">Android</param>"
                + "<param name=\"OSVERSION\">" + osVer + "</param>"
                + "<param name=\"MODEL\">" + model + "</param>"
                + "<param name=\"VENDOR\">" + vendor + "</param>"
                + "<param name=\"NETWORK\">" + network + "</param>"
                + "<param name=\"UDID\">" + udid + "</param>"
                + "<param name=\"USTY\">" + Constants.USER_TYPE + "</param>"
                + "<param name=\"APPID\">" + Constants.APPID + "</param>"
                + "</params>"
                + "</msg> ";
        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel billInquiry(int command, String PID, String AMOB,
                                         String CMOB, String CONSUMER, String PMTTYPE, String BAID)
            throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + PID + "</param><param name=\"AMOB\">" + AMOB + "</param>"
                + "<param name=\"CMOB\">" + CMOB + "</param>"
                + "<param name=\"CONSUMER\">"
                + StringEscapeUtils.escapeXml(CONSUMER) + "</param>"
                + "<param name=\"PMTTYPE\">" + PMTTYPE + "</param>"
                + "<param name=\"BAID\">" + StringEscapeUtils.escapeXml(BAID)
                + "</param>" + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel billPayment(int command, String pin, String pid,
                                         String cmob, String consumer, String pmttype, String baid,
                                         String txam, String amob) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + pid + "</param>" + "<param name=\"PIN\">" + pin
                + "</param><param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE
                + "</param>" + "<param name=\"CMOB\">" + cmob + "</param>"
                + "<param name=\"AMOB\">" + amob + "</param>"
                + "<param name=\"CONSUMER\">"
                + StringEscapeUtils.escapeXml(consumer) + "</param>"
                + "<param name=\"PMTTYPE\">" + pmttype + "</param>"
                + "<param name=\"BAID\">" + StringEscapeUtils.escapeXml(baid)
                + "</param>" + "<param name=\"TXAM\">" + txam + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }


    public HttpResponseModel checkBVS(int command, String latitude,
                                      String longitude) throws Exception {
        String xml = null;

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>"
                + "<param name=\"LATITUDE\">" + latitude + "</param>"
                + "<param name=\"LONGITUDE\">" + longitude + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }


    public HttpResponseModel agentBVS(int command,
                                      String cnic, String pid,
                                      String nadraSessionId, String fingerIndex,
                                      String templateType, String template, String NFIQuality, String minutiaeCount,String IMEI) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>"
                + "<param name=\"PID\">" + pid + "</param>"
                + "<param name=\"CNIC\">" + cnic + "</param>"
                + "<param name=\"NADRA_SESSION_ID\">" + nadraSessionId + "</param>"
                + "<param name=\"FINGER_INDEX\">" + fingerIndex + "</param>"
                + "<param name=\"TEMPLATE_TYPE\">" + templateType + "</param>"
                + "<param name=\"FINGER_TEMPLATE\">" + template + "</param>"
                + "<param name=\"NADRA_NIFQ\">" + NFIQuality + "</param>"
                + "<param name=\"MINUTIAE_COUNT\">" + minutiaeCount + "</param>"
                + "<param name=\"ERROR_COUNTER\">" + ApplicationData.errorCount + "</param>"
                + "<param name=\"AMOB\">" + ApplicationData.agentMobileNumber + "</param>"
                + "<param name=\"AGENT_ID\">" + ApplicationData.userId + "</param>"
                + "<param name=\"TERMINAL_ID\">" + ApplicationData.deviceId + "</param>"
                + "<param name=\"IMEI_NUMBER\">" + IMEI + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }
    public HttpResponseModel debitCardRequest(int command, String mobNO,String cnic) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>" +
                "        <param name=\"CNIC\">"+cnic+"</param>\n" +
                "        <param name=\"CMOB\">"+mobNO+"</param>\n" +
//                "        <param name=\"CARD_DESCRIPTION\">"+embossingName+"</param>\n" +
//                "        <param name=\"MAILING_ADDRESS\">"+mailingAddress+"</param>\n" +
                "        <param name=\"APPID\">"+Constants.APPID+"</param>\n" +
                "        <param name=\"DTID\">"+Constants.DTID+"</param>\n" +
                "    </params>\n" +
                "</msg>";
        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }
    public HttpResponseModel debitCardRequestConfirmation(int command, String mobNO,
                                                          String cnic, String embossingName,String mailingAddress) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>" +
                "        <param name=\"CNIC\">"+cnic+"</param>\n" +
                "        <param name=\"CMOB\">"+mobNO+"</param>\n" +
                "        <param name=\"CARD_DESCRIPTION\">"+embossingName+"</param>\n" +
                "        <param name=\"MAILING_ADDRESS\">"+mailingAddress+"</param>\n" +
                "        <param name=\"APPID\">"+Constants.APPID+"</param>\n" +
                "        <param name=\"DTID\">"+Constants.DTID+"</param>\n" +
                "    </params>\n" +
                "</msg>";
        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }
    public HttpResponseModel fetchMiniStatement(int command, String pin,
                                                String ACCTYPE, String APID, String BBACID) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PIN\">"
                + pin + "</param><param name=\"ENCT\">"
                + Constants.ENCRYPTION_TYPE + "</param>"
                + "<param name=\"ACCTYPE\">"
                + StringEscapeUtils.escapeXml(ACCTYPE) + "</param>"
                + "<param name=\"STNO\">1</param>"
                + "<param name=\"ETNO\">5</param>" + "<param name=\"APID\">"
                + APID + "</param>" + "<param name=\"BBACID\">"
                + StringEscapeUtils.escapeXml(BBACID) + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel thirdPartyCashOutInfo(int command, String pid,
                                                   String cnicOrMobile, String amount) throws Exception {
        String xml = null;
        if(cnicOrMobile.length()==11){
            xml = "<param name=\"CMOB\">" + cnicOrMobile + "</param>";
        }
        else{
            xml = "<param name=\"CNIC\">" + cnicOrMobile + "</param>";
        }

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>"
                + "<param name=\"PID\">" + pid + "</param>"
                + xml
                + "<param name=\"AMOUNT\">" + amount + "</param>"
                + "<param name=\"AMOB\">" + ApplicationData.agentMobileNumber + "</param>"
                + "<param name=\"AGENT_ID\">" + ApplicationData.userId + "</param>"
                + "<param name=\"TERMINAL_ID\">" + ApplicationData.deviceId + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel thirdPartyCashOut(int command, String pin, String pid,
                                               String cnic, String cmob, String accId,
                                               String accTitle, String amount, String thirdPartySessionId,
                                               String nadraSessionId, String isBvsReq, String fingerIndex,
                                               String templateType, String template,
                                               String camt, String tpam, String tamt, String otp
            , String NFIQuality, String minutiaeCount, String thirdPartyTransactionId,
                                               String isWalletExist,
                                               String walletNumber,
                                               String walletBal, String isWalletTran) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>"
                + "<param name=\"PID\">" + pid + "</param>"
                + "<param name=\"PIN\">" + pin + "</param>"
                + "<param name=\"OTP\">" + otp + "</param>"
                + "<param name=\"CNIC\">" + cnic + "</param>"
                + "<param name=\"AMT\">" + amount + "</param>"
                + "<param name=\"CAMT\">" + camt + "</param>"
                + "<param name=\"TPAM\">" + tpam + "</param>"
                + "<param name=\"TAMT\">" + tamt + "</param>"
                + "<param name=\"CMOB\">" + cmob + "</param>"
                + "<param name=\"COREACTL\">" + accTitle + "</param>"
                + "<param name=\"ACCNO\">" + accId + "</param>"
                + "<param name=\"THIRD_PARTY_SESSION_ID\">" + thirdPartySessionId + "</param>"
                + "<param name=\"NADRA_SESSION_ID\">" + nadraSessionId + "</param>"
                + "<param name=\"IS_BVS_REQ\">" + isBvsReq + "</param>"
                + "<param name=\"FINGER_INDEX\">" + fingerIndex + "</param>"
                + "<param name=\"TEMPLATE_TYPE\">" + templateType + "</param>"
                + "<param name=\"FINGER_TEMPLATE\">" + template + "</param>"
                + "<param name=\"NADRA_NIFQ\">" + NFIQuality + "</param>"
                + "<param name=\"MINUTIAE_COUNT\">" + minutiaeCount + "</param>"
                + "<param name=\"THIRD_PARTY_TRANSACTION_ID\">" + thirdPartyTransactionId + "</param>"
                + "<param name=\"ERROR_COUNTER\">" + ApplicationData.errorCount + "</param>"
                + "<param name=\"AMOB\">" + ApplicationData.agentMobileNumber + "</param>"
                + "<param name=\"AGENT_ID\">" + ApplicationData.userId + "</param>"
                + "<param name=\"TERMINAL_ID\">" + ApplicationData.deviceId + "</param>"
                + "<param name=\"IS_BAFL_WALLET_EXISTS\">" + isWalletExist + "</param>"
                + "<param name=\"BAFL_WALLET_ACCOUNT_ID\">" + walletNumber + "</param>"
                + "<param name=\"BAFL_WALLET_BALANCE\">" + walletBal + "</param>"
                + "<param name=\"IS_WALLET_TRANSFER\">" + isWalletTran + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel agentIBFTInfo(int command,
                                           String pid, String mobNum, String bankIMD,
                                           String accNum, String amount, String paymentPurpose,String bankName) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>"
                + "<param name=\"PID\">" + pid + "</param>"
                + "<param name=\"CMOB\">" + mobNum + "</param>"
                + "<param name=\"COREACID\">" + accNum + "</param>"
                + "<param name=\"TO_BANK_IMD\">" + bankIMD + "</param>"
                + "<param name=\"TRANS_PURPOSE_CODE\">" + paymentPurpose + "</param>"
                + "<param name=\"TXAM\">" + amount + "</param>"
                + "<param name=\"BENE_BANK_NAME\">" + bankName + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel agentIBFT(int command,
                                       String pid, String coreAcId, String toBankIMD,
                                       String paymentPurpose, String txamt,
                                       String bankName,String branchName,String iban,String crDr,String accTitle ) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>"
                + "<param name=\"PID\">" + pid + "</param>"
                + "<param name=\"COREACID\">" + coreAcId + "</param>"
                + "<param name=\"TO_BANK_IMD\">" + toBankIMD + "</param>"
                + "<param name=\"TRANS_PURPOSE_CODE\">" + paymentPurpose + "</param>"
                + "<param name=\"TXAM\">" + txamt + "</param>"
                + "<param name=\"BENE_BANK_NAME\">" + bankName + "</param>"
                + "<param name=\"BENE_BRANCH_NAME\">" + branchName + "</param>"
                + "<param name=\"BENE_IBAN\">" + iban + "</param>"
                + "<param name=\"CR_DR\">" + crDr + "</param>"
                + "<param name=\"COREACTITLE\">" + accTitle + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel bankList(int command) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>"
                + "<param name=\"IS_IBFT\">1</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel cashIn(int command, String pin, String pid,
                                    String cmob, String amob, String cnic, String txam, String camt,
                                    String tpam, String tamt) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + pid + "</param>" + "<param name=\"PIN\">" + pin
                + "</param><param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE
                + "</param>" + "<param name=\"CMOB\">" + cmob + "</param>"
                + "<param name=\"AMOB\">" + amob + "</param>"
                + "<param name=\"CNIC\">" + cnic + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "<param name=\"CAMT\">" + camt + "</param>"
                + "<param name=\"TPAM\">" + tpam + "</param>"
                + "<param name=\"TAMT\">" + tamt + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel ftBlbToCnicInfo(int command, String pid,
                                             String amob, String cmob, String rcmob, String rwcnic, String txam)
            throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + pid + "</param><param name=\"AMOB\">" + amob + "</param>"
                + "<param name=\"CMOB\">" + cmob + "</param>"
                + "<param name=\"RCMOB\">" + rcmob + "</param>"
                + "<param name=\"RWCNIC\">" + rwcnic + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel ftBlbToCnic(int command, String pin, String pid,
                                         String amob, String cmob, String rwmob, String rwcnic, String txam,
                                         String camt, String tpam, String tamt, String purposeCode) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + pid + "</param><param name=\"PIN\">" + pin
                + "</param><param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE
                + "</param>" + "<param name=\"AMOB\">" + amob + "</param>"
                + "<param name=\"CMOB\">" + cmob + "</param>"
                + "<param name=\"RCMOB\">" + rwmob + "</param>"
                + "<param name=\"RWCNIC\">" + rwcnic + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "<param name=\"CAMT\">" + camt + "</param>"
                + "<param name=\"TPAM\">" + tpam + "</param>"
                + "<param name=\"TAMT\">" + tamt + "</param>"
                + "<param name=\"TRANS_PURPOSE_CODE\">" + purposeCode + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel ftCnicToCnicInfo(int command, String pid,
                                              String amob, String swmob, String swcnic, String rwmob,
                                              String rwcnic, String txam) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + pid + "</param><param name=\"AMOB\">" + amob + "</param>"
                + "<param name=\"SWMOB\">" + swmob + "</param>"
                + "<param name=\"SWCNIC\">" + swcnic + "</param>"
                + "<param name=\"RWMOB\">" + rwmob + "</param>"
                + "<param name=\"RWCNIC\">" + rwcnic + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel ftCnicToCnic(int command, String pin, String pid,
                                          String amob, String swcnic, String swmob, String rwcnic,
                                          String rwmob, String txam, String camt, String tpam, String tamt,
                                          String bvsFlag, String senderCity, String receiverCity, String purposeCode)
            throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + pid + "</param><param name=\"PIN\">" + pin
                + "</param><param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE
                + "</param>" + "<param name=\"AMOB\">" + amob + "</param>"
                + "<param name=\"IS_BVS_REQ\">" + bvsFlag + "</param>"
                + "<param name=\"SENDER_CITY\">" + senderCity + "</param>"
                + "<param name=\"RECEIVER_CITY\">" + receiverCity + "</param>"
                + "<param name=\"SWMOB\">" + swmob + "</param>"
                + "<param name=\"RWMOB\">" + rwmob + "</param>"
                + "<param name=\"SWCNIC\">" + swcnic + "</param>"
                + "<param name=\"RWCNIC\">" + rwcnic + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "<param name=\"CAMT\">" + camt + "</param>"
                + "<param name=\"TPAM\">" + tpam + "</param>"
                + "<param name=\"TAMT\">" + tamt + "</param>"
                + "<param name=\"TRANS_PURPOSE_CODE\">" + purposeCode + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel verifyPin(int command, String pin, int retryCount)
            throws Exception {

        String requestString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><msg id=\""
                + command
                + "\" reqTime=\""
                + new Date().getTime()
                + "\">"
                + ""
                + "<params>"
                + "<param name=\"DTID\">5</param>"
                + "<param name=\"ENCT\">1</param>"
                + "<param name=\"PIN\">"
                + pin
                + "</param>"
                + "<param name=\"PIN_RETRY_COUNT\">"
                + retryCount + "</param>" + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel agentToAgentTransferInfo(int command, String PID,
                                                      String RAMOB, String AMOB, String TXAM) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + PID + "</param><param name=\"AMOB\">" + AMOB + "</param>"
                + "<param name=\"RAMOB\">" + RAMOB + "</param>"
                + "<param name=\"TXAM\">" + TXAM + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel cashInInfo(int command, String PID, String CMOB,
                                        String AMOB, String TXAM) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + PID + "</param><param name=\"AMOB\">" + AMOB + "</param>"
                + "<param name=\"CMOB\">" + CMOB + "</param>"
                + "<param name=\"TXAM\">" + TXAM + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel ftBlbToBlb(int command, String pin, String pid,
                                        String amob, String cmob, String rcmob, String txam, String camt,
                                        String tpam, String tamt) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + pid + "</param><param name=\"PIN\">" + pin
                + "</param><param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE
                + "</param>" + "<param name=\"AMOB\">" + amob + "</param>"
                + "<param name=\"CMOB\">" + cmob + "</param>"
                + "<param name=\"RCMOB\">" + rcmob + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "<param name=\"CAMT\">" + camt + "</param>"
                + "<param name=\"TPAM\">" + tpam + "</param>"
                + "<param name=\"TAMT\">" + tamt + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel transPurposeCode(int command) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel ftBlbToBlbInfo(int command, String PID,
                                            String amob, String cmob, String rcmob, String txam)
            throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + PID + "</param><param name=\"AMOB\">" + amob + "</param>"
                + "<param name=\"CMOB\">" + cmob + "</param>"
                + "<param name=\"RCMOB\">" + rcmob + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel transferInInfo(int command, String PID,
                                            String amob, String txam, String baid) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + PID + "</param><param name=\"AMOB\">" + amob
                + "</param><param name=\"TXAM\">" + txam + "</param>"
                + "<param name=\"BAID\">" + StringEscapeUtils.escapeXml(baid)
                + "</param>" + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel transferIn(int command, String pin, String pid,
                                        String amob, String bbacid, String coreacid, String baid,
                                        String txam, String camt, String tpam, String tamt)
            throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + pid + "</param><param name=\"PIN\">" + pin
                + "</param><param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE
                + "</param>" + "<param name=\"AMOB\">" + amob + "</param>"
                + "<param name=\"BBACID\">"
                + StringEscapeUtils.escapeXml(bbacid) + "</param>"
                + "<param name=\"COREACID\">"
                + StringEscapeUtils.escapeXml(coreacid) + "</param>"
                + "<param name=\"BAID\">" + StringEscapeUtils.escapeXml(baid)
                + "</param>" + "<param name=\"TXAM\">" + txam + "</param>"
                + "<param name=\"CAMT\">" + camt + "</param>"
                + "<param name=\"TPAM\">" + tpam + "</param>"
                + "<param name=\"TAMT\">" + tamt + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel transferOutInfo(int command, String PID,
                                             String Pin, String amob, String txam, String baid) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + PID + "</param>" + "<param name=\"AMOB\">" + amob
                + "</param><param name=\"TXAM\">" + txam + "</param>"
                + "<param name=\"BAID\">" + StringEscapeUtils.escapeXml(baid)
                + "</param>" + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel transferOut(int command, String pin, String pid,
                                         String amob, String bbacid, String coreacid, String baid,
                                         String txam, String camt, String tpam, String tamt, String coreactl)
            throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + pid + "</param><param name=\"PIN\">" + pin
                + "</param><param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE
                + "</param>" + "<param name=\"AMOB\">" + amob + "</param>"
                + "<param name=\"BBACID\">"
                + StringEscapeUtils.escapeXml(bbacid) + "</param>"
                + "<param name=\"COREACID\">"
                + StringEscapeUtils.escapeXml(coreacid) + "</param>"
                + "<param name=\"COREACTL\">"
                + StringEscapeUtils.escapeXml(coreactl) + "</param>"
                + "<param name=\"BAID\">" + StringEscapeUtils.escapeXml(baid)
                + "</param>" + "<param name=\"TXAM\">" + txam + "</param>"
                + "<param name=\"CAMT\">" + camt + "</param>"
                + "<param name=\"TPAM\">" + tpam + "</param>"
                + "<param name=\"TAMT\">" + tamt + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel ftBlbToCoreInfo(int command, String pid,
                                             String amob, String cmob, String coreacid, String txam)
            throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + pid + "</param><param name=\"AMOB\">" + amob + "</param>"
                + "<param name=\"CMOB\">" + cmob + "</param>"
                + "<param name=\"COREACID\">"
                + StringEscapeUtils.escapeXml(coreacid) + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel ftBlbToCore(int command, String pin, String pid,
                                         String amob, String cmob, String rcmob, String coreacid,
                                         String coreactl, String txam, String camt, String tpam, String tamt)
            throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + pid + "</param><param name=\"PIN\">" + pin
                + "</param><param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE
                + "</param>" + "<param name=\"AMOB\">" + amob + "</param>"
                + "<param name=\"CMOB\">" + cmob + "</param>"
                + "<param name=\"RCMOB\">" + rcmob + "</param>"
                + "<param name=\"COREACID\">"
                + StringEscapeUtils.escapeXml(coreacid) + "</param>"
                + "<param name=\"COREACTL\">"
                + StringEscapeUtils.escapeXml(coreactl) + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "<param name=\"CAMT\">" + camt + "</param>"
                + "<param name=\"TPAM\">" + tpam + "</param>"
                + "<param name=\"TAMT\">" + tamt + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel ftCnicToCoreInfo(int command, String pid,
                                              String amob, String swmob, String swcnic, String coreacid,
                                              String txam) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + pid + "</param><param name=\"AMOB\">" + amob + "</param>"
                + "<param name=\"SWMOB\">" + swmob + "</param>"
                + "<param name=\"SWCNIC\">" + swcnic + "</param>"
                + "<param name=\"COREACID\">"
                + StringEscapeUtils.escapeXml(coreacid) + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel ftCnicToCore(int command, String pin, String pid,
                                          String amob, String swcnic, String swmob, String rcmob,
                                          String coreacid, String coreactl, String txam, String camt,
                                          String tpam, String tamt, String bvsFlag) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + pid + "</param><param name=\"PIN\">" + pin
                + "</param><param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE
                + "</param>" + "<param name=\"AMOB\">" + amob + "</param>"
                + "<param name=\"IS_BVS_REQ\">" + bvsFlag + "</param>"
                + "<param name=\"SWMOB\">" + swmob + "</param>"
                + "<param name=\"RCMOB\">" + rcmob + "</param>"
                + "<param name=\"COREACID\">"
                + StringEscapeUtils.escapeXml(coreacid) + "</param>"
                + "<param name=\"COREACTL\">"
                + StringEscapeUtils.escapeXml(coreactl) + "</param>"
                + "<param name=\"SWCNIC\">" + swcnic + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "<param name=\"CAMT\">" + camt + "</param>"
                + "<param name=\"TPAM\">" + tpam + "</param>"
                + "<param name=\"TAMT\">" + tamt + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel cash_withdrawal_by_trxid_info(int command, String PID, String TXID,
                                                           String TXAM) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\"" + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>"
                + "<param name=\"PID\">"
                + StringEscapeUtils.escapeXml(PID) + "</param>"
                + "<param name=\"TXAM\">"
                + StringEscapeUtils.escapeXml(TXAM) + "</param>"
                + "<param name=\"TXID\">"
                + StringEscapeUtils.escapeXml(TXID) + "</param>"
                + "<param name=\"AMOB\">"
                + ApplicationData.agentMobileNumber + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel cash_withdrawal_by_trxid(int command, String TRXCODE, String PID,
                                                      String TXAM, String TRXID, String CAMT,
                                                      String TPAM, String TAMT, String CMOB) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\"" + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>"
                + "<param name=\"ENCT\">1</param>"
                + "<param name=\"OTPIN\">" + StringEscapeUtils.escapeXml(TRXCODE) + "</param>"
                + "<param name=\"PID\">" + StringEscapeUtils.escapeXml(PID) + "</param>"
                + "<param name=\"TXAM\">" + StringEscapeUtils.escapeXml(TXAM) + "</param>"
                + "<param name=\"CAMT\">" + StringEscapeUtils.escapeXml(CAMT) + "</param>"
                + "<param name=\"TPAM\">" + StringEscapeUtils.escapeXml(TPAM) + "</param>"
                + "<param name=\"TAMT\">" + StringEscapeUtils.escapeXml(TAMT) + "</param>"
                + "<param name=\"TXID\">" + StringEscapeUtils.escapeXml(TRXID) + "</param>"
                + "<param name=\"CMOB\">" + CMOB + "</param>"
                + "<param name=\"AMOB\">" + ApplicationData.agentMobileNumber + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel cashOutInfo(int command, String PID, String CMOB,
                                         String AMOB, String TXAM, String isHRA, String CNIC, String hraLinkedRequest) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + PID + "</param><param name=\"AMOB\">" + AMOB + "</param>"
                + "<param name=\"CMOB\">" + CMOB + "</param>"
                + "<param name=\"TXAM\">" + TXAM + "</param>"
                + "<param name=\"CNIC\">" + CNIC + "</param>"
                + "<param name=\"PAYMENT_MODE\">" + isHRA + "</param>"
                + "<param name=\"HRA_LINKED_REQUEST\">" + hraLinkedRequest + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel cashOut(int command, String pin, String pid,
                                     String cmob, String cnic, String amob, String txam, String camt,
                                     String tpam, String tamt,String isHRA, String trxId, String otp) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + pid + "</param>" + "<param name=\"PIN\">" + pin
                + "</param><param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE
                + "</param>" + "<param name=\"CMOB\">" + cmob + "</param>"
                + "<param name=\"CNIC\">" + cnic + "</param>"
                + "<param name=\"AMOB\">" + amob + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "<param name=\"CAMT\">" + camt + "</param>"
                + "<param name=\"TPAM\">" + tpam + "</param>"
                + "<param name=\"TAMT\">" + tamt + "</param>"
                + "<param name=\"OTPIN\">" + otp + "</param>"
                + "<param name=\"TXID\">" + trxId + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel ftCnicToBlbInfo(int command, String pid,
                                             String amob, String swmob, String swcnic, String rcmob, String txam)
            throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + pid + "</param><param name=\"AMOB\">" + amob + "</param>"
                + "<param name=\"SWMOB\">" + swmob + "</param>"
                + "<param name=\"SWCNIC\">" + swcnic + "</param>"
                + "<param name=\"RCMOB\">" + rcmob + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel ftCnicToBlb(int command, String pin, String pid,
                                         String amob, String rcmob, String swmob, String swcnic,
                                         String txam, String camt, String tpam, String tamt, String bvsFlag,
                                         String purposeCode)
            throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + pid + "</param><param name=\"PIN\">" + pin
                + "</param><param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE
                + "</param>" + "<param name=\"AMOB\">" + amob + "</param>"
                + "<param name=\"IS_BVS_REQ\">" + bvsFlag + "</param>"
                + "<param name=\"RCMOB\">" + rcmob + "</param>"
                + "<param name=\"SWMOB\">" + swmob + "</param>"
                + "<param name=\"SWCNIC\">" + swcnic + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "<param name=\"CAMT\">" + camt + "</param>"
                + "<param name=\"TPAM\">" + tpam + "</param>"
                + "<param name=\"TAMT\">" + tamt + "</param>"
                + "<param name=\"TRANS_PURPOSE_CODE\">" + purposeCode + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel collectionPaymentInfo(int command, String PID,
                                                   String cmob, String consumerNo) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>"
                + "<param name=\"PID\">" + PID + "</param>"
                + "<param name=\"CMOB\">" + cmob + "</param>"
                + "<param name=\"CONSUMER\">" + consumerNo + "</param>"
                + "<param name=\"AMOB\">" + ApplicationData.agentMobileNumber + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel collectionPayment(int command, String PId, String PName,
                                               String consumerNo, String status, String dueDate,
                                               String cmob, String strAmount, String charges,
                                               String totalAmount) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>"
                + "<param name=\"PID\">" + PId + "</param>"
                + "<param name=\"PNAME\">" + PName + "</param>"
                + "<param name=\"CMOB\">" + cmob + "</param>"
                + "<param name=\"CONSUMER\">" + consumerNo + "</param>"
                + "<param name=\"STATUS\">" + status + "</param>"
                + "<param name=\"DUEDATE\">" + dueDate + "</param>"
                + "<param name=\"TXAM\">" + strAmount + "</param>"
                + "<param name=\"TPAM\">" + charges + "</param>"
                + "<param name=\"TAMT\">" + totalAmount + "</param>"
                + "<param name=\"AMOB\">" + ApplicationData.agentMobileNumber + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel retailPaymentInfo(int command, String PID,
                                               String cmob, String txam) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + PID + "</param><param name=\"CMOB\">" + cmob
                + "</param><param name=\"TXAM\">" + txam + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel retailPayment(int command, String pin, String pid,
                                           String cmob, String txam, String enct, String camt, String tpam,
                                           String tamt) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + pid + "</param><param name=\"PIN\">" + pin
                + "</param><param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE
                + "</param>" + "<param name=\"CMOB\">" + cmob + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "<param name=\"CAMT\">" + camt + "</param>"
                + "<param name=\"TPAM\">" + tpam + "</param>"
                + "<param name=\"TAMT\">" + tamt + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel openAccountVerifyCustomerRegistration(int command, String msisdn,
                                                                   String cnic, String trxid,
                                                                   String isReceiveCash, String isHra, String isUpgrad)
            throws Exception {

        String requestString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><msg id=\""
                + command
                + "\" reqTime=\""
                + new Date().getTime()
                + "\">"
                + "<params>"
                + "<param name=\"DTID\">5</param>"
                + "<param name=\"CMOB\">"
                + msisdn
                + "</param>"
                + "<param name=\"CNIC\">"
                + cnic
                + "</param>"
                + "<param name=\"IS_RECEIVE_CASH\">" + isReceiveCash + "</param>"
                + "<param name=\"IS_HRA\">" + isHra + "</param>"
                + "<param name=\"IS_UPGRADE\">" + isUpgrad + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }


    public HttpResponseModel openAccountVerifyL1TOHRACustomerRegistration(int command, String msisdn,
                                                                          String cnic)
            throws Exception {

        String requestString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><msg id=\""
                + command
                + "\" reqTime=\""
                + new Date().getTime()
                + "\">"
                + "<params>"
                + "<param name=\"DTID\">5</param>"
                + "<param name=\"CMOB\">"
                + msisdn
                + "</param>"
                + "<param name=\"CNIC\">"
                + cnic
                + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }


    public HttpResponseModel openAccountBvs(int command, String mpin, String mobileNumber,
                                            String cnic, String birthPlace, String resp,
                                            String cname, String mothermaiden,
                                            String dob, String cnicStatus, String presentAddress, String presentCity,
                                            String permanentAddress, String permanentCity, String accountTitle,
                                            String gender, String fatherHusbandMane, String cnicSeen,
                                            String cnicExp, String initialDeposit,
                                            String state, String accountType, String agentMobile,
                                            String productId, String isbvsAccount,
                                            String custRegState, String custRegStateId, String trxId, String depositAmountFlag,
                                            String strIsHr, String strNextKin, String purposeOfAccount, String strOccupation,
                                            String strOrgLoc1, String strOrgLoc2, String strOrgLoc3, String strOrgLoc4, String strOrgLoc5,
                                            String strOrgRel1, String strOrgRel2, String strOrgRel3, String strOrgRel4, String strOrgRel5,
                                            String strMobileNetwork
    ) throws Exception {


        String requestString = "";
        requestString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><msg id=\"121\" reqTime=\""
                + new Date().getTime()
                + "\">"
                + "<params>"
                + "<param name=\"DTID\">5</param>"
                + "<param name=\"ENCT\">1</param>"
                + "<param name=\"CREG_STATE\">" + custRegState + "</param>"
                + "<param name=\"CREG_STATE_ID\">" + custRegStateId + "</param>"
                + "<param name=\"CMOB\">" + mobileNumber + "</param>"
                + "<param name=\"PIN\">" + mpin + "</param>"
                + "<param name=\"CNIC\">" + cnic + "</param>"
                + "<param name=\"BIRTH_PLACE\">" + birthPlace + "</param>"
                + "<param name=\"RESP\">" + resp + "</param>"
                + "<param name=\"CNAME\">" + cname + "</param>"
                + "<param name=\"MOTHER_MAIDEN\">" + mothermaiden + "</param>"
                + "<param name=\"CDOB\">" + dob + "</param>"
                + "<param name=\"CUST_MOB_NETWORK\">" + strMobileNetwork + "</param>"
                + "<param name=\"CNIC_STATUS\">" + cnicStatus + "</param>"
                + "<param name=\"CNIC_EXP\">" + cnicExp + "</param>"
                + "<param name=\"PRESENT_ADDR\">" + presentAddress + "</param>"
                + "<param name=\"PRESENT_CITY\">" + presentCity + "</param>"
                + "<param name=\"PERMANENT_ADDR\">" + permanentAddress + "</param>"
                + "<param name=\"PERMANENT_CITY\">" + permanentCity + "</param>"
                + "<param name=\"ACTITLE\">" + accountTitle + "</param>"
                + "<param name=\"GENDER\">" + gender + "</param>"
                + "<param name=\"FATHER_HUSBND_NAME\">" + fatherHusbandMane + "</param>"
                + "<param name=\"IS_CNIC_SEEN\">" + cnicSeen + "</param>"
                + "<param name=\"DEPOSIT_AMT_FLAG\">" + depositAmountFlag + "</param>"
                + "<param name=\"DEPOSIT_AMT\">" + initialDeposit + "</param>"
                + "<param name=\"AMOB\">" + agentMobile + "</param>"
                + "<param name=\"PID\">" + productId + "</param>"
                + "<param name=\"IS_BVS_ACCOUNT\">" + isbvsAccount + "</param>"
                + "<param name=\"CUST_ACC_TYPE\">" + accountType + "</param>"
                + "<param name=\"TRXID\">" + trxId + "</param>"

                + "<param name=\"IS_HRA\">" + strIsHr + "</param>"
                + "<param name=\"NOKMOB\">" + strNextKin + "</param>"
                + "<param name=\"TRX_PUR\">" + purposeOfAccount + "</param>"
                + "<param name=\"OCCUPATION\">" + strOccupation + "</param>"

                + "<param name=\"ORG_LOC1\">" + strOrgLoc1 + "</param>"
                + "<param name=\"ORG_LOC2\">" + strOrgLoc2 + "</param>"
                + "<param name=\"ORG_LOC3\">" + strOrgLoc3 + "</param>"
                + "<param name=\"ORG_LOC4\">" + strOrgLoc4 + "</param>"
                + "<param name=\"ORG_LOC5\">" + strOrgLoc5 + "</param>"

                + "<param name=\"ORG_REL1\">" + strOrgRel1 + "</param>"
                + "<param name=\"ORG_REL2\">" + strOrgRel2 + "</param>"
                + "<param name=\"ORG_REL3\">" + strOrgRel3 + "</param>"
                + "<param name=\"ORG_REL4\">" + strOrgRel4 + "</param>"
                + "<param name=\"ORG_REL5\">" + strOrgRel5 + "</param>"

                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }


    public HttpResponseModel openAccountL0ToL1(int command, String mpin, String mobileNumber,
                                               String cnic, String cname, String dob, String fatherHusbandMane,
                                               String agentMobile, String productId, String trnPur, String strOccupation,
                                               String strOrgLoc1, String strOrgLoc2, String strOrgLoc3, String strOrgLoc4, String strOrgLoc5,
                                               String strOrgRel1, String strOrgRel2, String strOrgRel3, String strOrgRel4, String strOrgRel5,
                                               String SOI, String kinName, String kinMobileNo, String kinCNIC, String kinRel, String mobileNetwork


    ) throws Exception {


        String requestString = "";
        requestString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><msg id=\"226\" reqTime=\""
                + new Date().getTime()
                + "\">"
                + "<params>"
                + "<param name=\"DTID\">5</param>"
                + "<param name=\"ENCT\">1</param>"

                + "<param name=\"CMOB\">" + mobileNumber + "</param>"
                + "<param name=\"PIN\">" + mpin + "</param>"
                + "<param name=\"CNIC\">" + cnic + "</param>"
                + "<param name=\"CNAME\">" + cname + "</param>"
                + "<param name=\"CDOB\">" + dob + "</param>"
                + "<param name=\"FATHER_HUSBND_NAME\">" + fatherHusbandMane + "</param>"
                + "<param name=\"AMOB\">" + agentMobile + "</param>"
                + "<param name=\"PID\">" + productId + "</param>"
                + "<param name=\"OCCUPATION\">" + strOccupation + "</param>"
                + "<param name=\"CUST_MOB_NETWORK\">" + mobileNetwork + "</param>"

                + "<param name=\"ORG_LOC1\">" + strOrgLoc1 + "</param>"
                + "<param name=\"ORG_LOC2\">" + strOrgLoc2 + "</param>"
                + "<param name=\"ORG_LOC3\">" + strOrgLoc3 + "</param>"
                + "<param name=\"ORG_LOC4\">" + strOrgLoc4 + "</param>"
                + "<param name=\"ORG_LOC5\">" + strOrgLoc5 + "</param>"

                + "<param name=\"ORG_REL1\">" + strOrgRel1 + "</param>"
                + "<param name=\"ORG_REL2\">" + strOrgRel2 + "</param>"
                + "<param name=\"ORG_REL3\">" + strOrgRel3 + "</param>"
                + "<param name=\"ORG_REL4\">" + strOrgRel4 + "</param>"
                + "<param name=\"ORG_REL5\">" + strOrgRel5 + "</param>"

                + "<param name=\"TRX_PUR\">" + trnPur + "</param>"
                + "<param name=\"SOI\">" + SOI + "</param>"
                + "<param name=\"KIN_NAME\">" + kinName + "</param>"
                + "<param name=\"Kin_MOB_NO\">" + kinMobileNo + "</param>"
                + "<param name=\"KIN_CNIC\">" + kinCNIC + "</param>"
                + "<param name=\"KIN_RELATIONSHIP\">" + kinRel + "</param>"

                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel openAccount(int command, String pin,
                                         String termsPhoto, String signaturePhoto, String customerPhoto,
                                         String cnicFrontPhoto, String cnicBackPhoto, String l1FormPhoto,
                                         String cname, String cnic, String cmob, String cregState,
                                         String cregStateId, String cdob, String suAccType, String cnicExp,
                                         String initialDeposit, String isCnicSeen, String amob,
                                         String trxId, String depositAmountFlag, String mobileNetwork)
            throws Exception {

        String xml = "";
        if (trxId != null) {
            xml = "<param name=\"TRXID\">" + trxId + "</param>";
        } else {
            xml = "<param name=\"DEPOSIT_AMT\">" + initialDeposit + "</param>" +
                    "<param name=\"DEPOSIT_AMT_FLAG\">" + depositAmountFlag + "</param>";
        }

        String requestString = "";
        if (suAccType.equals("1")) {
            requestString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><msg id=\""
                    + command
                    + "\" reqTime=\""
                    + new Date().getTime()
                    + "\">"
                    + "<params>"
                    + "<param name=\"DTID\">5</param>"
                    + "<param name=\"PIN\">"
                    + pin
                    + "</param>"
                    + "<param name=\"ENCT\">"
                    + Constants.ENCRYPTION_TYPE
                    + "</param>"
                    + "<param name=\"AMOB\">"
                    + amob
                    + "</param>"
                    + "<param name=\"CNAME\">"
                    + cname
                    + "</param>"
                    + "<param name=\"CNIC\">"
                    + cnic
                    + "</param>"
                    + "<param name=\"CUST_MOB_NETWORK\">"
                    + mobileNetwork
                    + "</param>"
                    + "<param name=\"CMOB\">"
                    + cmob
                    + "</param>"
                    + "<param name=\"IS_CNIC_SEEN\">"
                    + isCnicSeen
                    + "</param>"
                    + "<param name=\"CDOB\">"
                    + cdob
                    + "</param>"
                    + xml
                    + "<param name=\"CUST_ACC_TYPE\">"
                    + suAccType
                    + "</param>"
                    + "<param name=\"CNIC_EXP\">"
                    + cnicExp
                    + "</param>"
                    + "<param name=\"CREG_STATE\">"
                    + cregState
                    + "</param>"
                    + "<param name=\"CREG_STATE_ID\">"
                    + cregStateId
                    + "</param>"
                    + "<param name=\"CUSTOMER_PHOTO\">"
                    + customerPhoto
                    + "</param>"
                    + "<param name=\"CNIC_FRONT_PHOTO\">"
                    + cnicFrontPhoto
                    + "</param>"
                    + "</params></msg> ";
        } else {
            requestString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><msg id=\""
                    + command
                    + "\" reqTime=\""
                    + new Date().getTime()
                    + "\">"
                    + "<params>"
                    + "<param name=\"DTID\">5</param>"
                    + "<param name=\"PIN\">"
                    + pin
                    + "</param>"
                    + "<param name=\"ENCT\">"
                    + Constants.ENCRYPTION_TYPE
                    + "</param>"
                    + "<param name=\"AMOB\">"
                    + amob
                    + "</param>"
                    + "<param name=\"CUSTOMER_PHOTO\">"
                    + customerPhoto
                    + "</param>"
                    + "<param name=\"CNIC_FRONT_PHOTO\">"
                    + cnicFrontPhoto
                    + "</param>"
                    + "<param name=\"CNAME\">"
                    + StringEscapeUtils.escapeXml(cname)
                    + "</param>"
                    + "<param name=\"CNIC\">"
                    + cnic
                    + "</param>"
                    + "<param name=\"CMOB\">"
                    + cmob
                    + "</param>"
                    + "<param name=\"IS_CNIC_SEEN\">"
                    + isCnicSeen
                    + "</param>"
                    + "<param name=\"CDOB\">"
                    + cdob
                    + "</param>"
                    + xml
                    + "<param name=\"CUST_ACC_TYPE\">"
                    + suAccType
                    + "</param>"
                    + "<param name=\"CNIC_EXP\">"
                    + cnicExp
                    + "</param>"
                    + "<param name=\"CREG_STATE\">"
                    + StringEscapeUtils.escapeXml(cregState)
                    + "</param>"
                    + "<param name=\"CREG_STATE_ID\">"
                    + cregStateId
                    + "</param>" + "</params></msg> ";
        }

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel receiveMoneySenderRedeemInfo(int command,
                                                          String pid, String amob, String trxId, String senderCnic,
                                                          String senderMobileNo) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + pid + "</param>" + "<param name=\"AMOB\">" + amob
                + "</param>" + "<param name=\"TRXID\">" + trxId + "</param>"
                + "<param name=\"SWMOB\">" + senderMobileNo + "</param>"
                + "<param name=\"SWCNIC\">" + senderCnic + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel receiveMoneySenderRedeem(int command, String pin,
                                                      String pid, String amob, String senderCnic, String senderMobileNo,
                                                      String receiverCnic, String receiverMobileNo, String trxCode,
                                                      String trxId, String strTotalAmount, String bvsFlag) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + pid + "</param>" + "<param name=\"AMOB\">" + amob
                + "</param>" + "<param name=\"ENCT\">1</param>"
                + "<param name=\"PIN\">" + pin + "</param>"
                + "<param name=\"OTPIN\">" + trxCode + "</param>"
                + "<param name=\"TRXID\">" + trxId + "</param>"
                + "<param name=\"SWCNIC\">" + senderCnic + "</param>"
                + "<param name=\"SWMOB\">" + senderMobileNo + "</param>"
                + "<param name=\"RWCNIC\">" + receiverCnic + "</param>"
                + "<param name=\"RWMOB\">" + receiverMobileNo + "</param>"
                + "<param name=\"TAMT\">" + strTotalAmount + "</param>"
                + "<param name=\"IS_BVS_REQ\">" + bvsFlag + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel receiveMoneyReceiveCashInfo(int command,
                                                         String pid, String amob, String trxId, String receiverCnic,
                                                         String receiverMobileNo) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + pid + "</param>" + "<param name=\"AMOB\">" + amob
                + "</param>" + "<param name=\"TRXID\">" + trxId + "</param>"
                + "<param name=\"RWMOB\">" + receiverMobileNo + "</param>"
                + "<param name=\"RWCNIC\">" + receiverCnic + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel receiveMoneyPendingTrxPayment(int command,
                                                           String pin, String pid, String amob, String senderCnic,
                                                           String senderMobileNo, String receiverCnic,
                                                           String receiverMobileNo, String trxCode, String trxId,
                                                           String strAmount, String strCommissonAmount, String strCharges,
                                                           String strTotalAmount) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + pid + "</param>" + "<param name=\"AMOB\">" + amob
                + "</param>" + "<param name=\"ENCT\">1</param>"
                + "<param name=\"PIN\">" + pin + "</param>"
                + "<param name=\"OTPIN\">" + trxCode + "</param>"
                + "<param name=\"TRXID\">" + trxId + "</param>"
                + "<param name=\"SWCNIC\">" + senderCnic + "</param>"
                + "<param name=\"SWMOB\">" + senderMobileNo + "</param>"
                + "<param name=\"RWCNIC\">" + receiverCnic + "</param>"
                + "<param name=\"RWMOB\">" + receiverMobileNo + "</param>"
                + "<param name=\"TXAM\">" + strAmount + "</param>"
                + "<param name=\"CAMT\">" + strCommissonAmount + "</param>"
                + "<param name=\"TPAM\">" + strCharges + "</param>"
                + "<param name=\"TAMT\">" + strTotalAmount + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel receiveMoneyReceiveCash(int command, String pin,
                                                     String pid, String amob, String senderCnic, String senderMobileNo,
                                                     String receiverCnic, String receiverMobileNo, String trxCode,
                                                     String trxId, String strAmount, String strCommissonAmount,
                                                     String strCharges, String strTotalAmount, String bvsFlag) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "<param name=\"PID\">"
                + pid + "</param>" + "<param name=\"ENCT\">1</param>"
                + "<param name=\"PIN\">" + pin + "</param>"
                + "<param name=\"OTPIN\">" + trxCode + "</param>"
                + "<param name=\"TRXID\">" + trxId + "</param>"
                + "<param name=\"SWCNIC\">" + senderCnic + "</param>"
                + "<param name=\"SWMOB\">" + senderMobileNo + "</param>"
                + "<param name=\"TXAM\">" + strAmount + "</param>"
                + "<param name=\"CAMT\">" + strCommissonAmount + "</param>"
                + "<param name=\"TPAM\">" + strCharges + "</param>"
                + "<param name=\"TAMT\">" + strTotalAmount + "</param>"
                + "<param name=\"IS_BVS_REQ\">" + bvsFlag + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel verifyMsisdnCnicImage(int command, String MSISDN, String CNIC,
                                                   String IMAGE, String FINGER_INDEX, String TEMPLATE_TYPE, String TEMPLATE)
            throws Exception {

        Long time = new Date().getTime();
        HttpResponseModel serverResponse = null;
        String requestString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><msg id=\""
                + Constants.CMD_OPEN_ACCOUNT_NADRA_VERIFICATION
                + "\" reqTime=\""
                + time.toString()
                + "\">"
                + "<params>"
                + "<param name=\"DTID\">5</param>"
                + "<param name=\"CMOB\">" + StringEscapeUtils.escapeXml(MSISDN) + "</param>"
                + "<param name=\"CNIC\">" + StringEscapeUtils.escapeXml(CNIC) + "</param>"
                + "<param name=\"THUMB_IMPRESSION\">" + StringEscapeUtils.escapeXml(IMAGE) + "</param>"
                + "<param name=\"FINGER_INDEX\">" + StringEscapeUtils.escapeXml(FINGER_INDEX) + "</param>"
                + "<param name=\"TEMPLATE_TYPE\">" + StringEscapeUtils.escapeXml(TEMPLATE_TYPE) + "</param>"
                + "<param name=\"FINGER_TEMPLATE\">" + TEMPLATE + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }


    public HttpResponseModel otpVerification(int command, String action,
                                             String udid, String mPin, String uid) throws Exception {

        Long time = new Date().getTime();
        String requestString;
        if (action != null && (action.equals(Constants.ACTION_VERIFY))) {
            requestString = "<msg id=\"" + command + "\" reqTime=\""
                    + time.toString() + "\">" + "" + "<params>"
                    + "<param name=\"DTID\">5</param>" + "<param name=\"PIN\">"
                    + mPin + "</param>" + "<param name=\"UDID\">" + udid
                    + "</param>" + "<param name=\"ENCT\">"
                    + Constants.ENCRYPTION_TYPE + "</param>"
                    + "<param name=\"UID\">" + uid + "</param>"
                    + "<param name=\"USTY\">" + Constants.USER_TYPE
                    + "</param>" + "<param name=\"ACTION\">" + action
                    + "</param>" + "</params></msg> ";
        } else {
            requestString = "<msg id=\"" + command + "\" reqTime=\""
                    + time.toString() + "\">" + "" + "<params>"
                    + "<param name=\"DTID\">5</param>"
                    + "<param name=\"UDID\">" + udid + "</param>"
                    + "<param name=\"USTY\">" + Constants.USER_TYPE
                    + "</param>" + "<param name=\"UID\">" + uid + "</param>"
                    + "<param name=\"USTY\">" + Constants.USER_TYPE
                    + "</param>" + "<param name=\"ACTION\">" + action
                    + "</param>" + "</params></msg> ";
        }

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel openAccountOtpVerification(int command, String mob,
                                                        String cnic, String action, String pin) throws Exception {
        Long time = new Date().getTime();

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + time.toString() + "\">" + ""
                + "<params>"
                + "<param name=\"CMDID\">" + command + "</param>"
                + "<param name=\"MOBN\">" + mob + "</param>"
                + "<param name=\"CNIC\">" + cnic + "</param>"
                + "<param name=\"DTID\">5</param>"
                + "<param name=\"ACTION\">" + action + "</param>"
                + "<param name=\"PIN\">" + pin + "</param>"
                + "<param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel signOut(int command) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>" + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel verifyNadraFingerPrint(int command, String requestString) throws Exception {

        AppLogger.i("JSON Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(String.valueOf(command), urlConnection, requestString);
        return serverResponse;
    }
}
