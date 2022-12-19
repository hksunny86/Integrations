package com.inov8.jsblconsumer.net;

import android.content.Context;
import android.net.http.X509TrustManagerExtensions;
import android.util.Base64;

import com.inov8.jsblconsumer.R;
import com.inov8.jsblconsumer.model.HttpResponseModel;
import com.inov8.jsblconsumer.util.AppLogger;
import com.inov8.jsblconsumer.util.ApplicationData;
import com.inov8.jsblconsumer.util.BuildConstants;
import com.inov8.jsblconsumer.util.Constants;
import com.inov8.jsblconsumer.util.PreferenceConnector;

import org.apache.commons.lang3.StringEscapeUtils;

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
    private X509TrustManagerExtensions trustManagerExt = null;

    private HttpCalls(Context appContext) {
        context = appContext;
    }

    public static HttpCalls getInstance(Context appContext) {
        instance = new HttpCalls(appContext);
        instance.init();
        return instance;
    }

    private URL getUrl() {
        String appUrl = Constants.APPURL;

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
            urlConnection = (HttpURLConnection) getUrl().openConnection();
            urlConnection.setConnectTimeout(Constants.CONNECTION_TIMEOUT * 1000);
            urlConnection.setReadTimeout(Constants.CONNECTION_TIMEOUT * 1000);

            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");
            urlConnection.setRequestProperty("Content-Language", "en-US");
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("POST");


            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    HostnameVerifier hv =
                            HttpsURLConnection.getDefaultHostnameVerifier();
                    return hv.verify(BuildConstants.HOST_NAME, session);
                }
            };


            if (Constants.PROTOCOL.equals("https")) {
                SSLSocketFactory sslSocketFactory = getSSLSocketFactory();
                if (sslSocketFactory != null) {
                    ((HttpsURLConnection) urlConnection).setSSLSocketFactory(sslSocketFactory);
                    ((HttpsURLConnection) urlConnection).setHostnameVerifier(hostnameVerifier);
                    ((HttpsURLConnection) urlConnection).connect();

                    Set<String> validPins = Collections.singleton(Constants.SECURITY_KEY);
                    instance.validatePinning(trustManagerExt, ((HttpsURLConnection) urlConnection), validPins, this);

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
            HttpsURLConnection conn, Set<String> validPins, HttpCalls httpCalls)
            throws SSLException {
        String certChainMsg = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            List<X509Certificate> trustedChain = httpCalls.trustedChain(trustManagerExt, conn);

            for (Certificate cert : trustedChain) {
                byte[] publicKey = cert.getPublicKey().getEncoded();
                md.update(publicKey, 0, publicKey.length);
                String pin = Base64.encodeToString(md.digest(),
                        Base64.NO_WRAP);
                if (validPins.contains(pin)) {
                    return;
                }
            }
        } catch (NoSuchAlgorithmException e) {
            throw new SSLException(e);
        } catch (SSLException ex) {
            ex.printStackTrace();
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

            // Find first X509TrustManager in the TrustManagerFactory
            X509TrustManager x509TrustManager = null;
            for (TrustManager trustManager : tmf.getTrustManagers()) {
                if (trustManager instanceof X509TrustManager) {
                    x509TrustManager = (X509TrustManager) trustManager;
                    break;
                }
            }
            trustManagerExt = new X509TrustManagerExtensions(x509TrustManager);

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

        return PreferenceConnector.readString(context,
                PreferenceConnector.CUSTOM_IP, Constants.APPURL);

    }


    public HttpResponseModel customerRegister(int command, String MOB, String CNIC, String isHRA)
            throws Exception {
        Long time = new Date().getTime();

        String requestString =
                "<msg id=\"" + command + "\" reqTime=\""
                        + time.toString() + "\">" + "" + "<params>"
                        + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                        + "<param name=\"MOBN\">" + MOB + "</param>"
                        + "<param name=\"CNIC\">" + CNIC + "</param>"
                        + "<param name=\"IS_UPGRADE\">" + isHRA + "</param>"
                        + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel hraToWalletInfo(int command, String pid, String amount)
            throws Exception {
        Long time = new Date().getTime();

        String requestString =
                "<msg id=\"" + command + "\" reqTime=\""
                        + time.toString() + "\">" + "" + "<params>"
                        + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                        + "<param name=\"TXAM\">" + amount + "</param>"
                        + "<param name=\"PID\">" + pid + "</param>"
                        + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel transPurposeCode(int command) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">5</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel hraToWallet(int command, String pid, String txam, String tamt, String camt, String tpam)
            throws Exception {
        Long time = new Date().getTime();

        String requestString =
                "<msg id=\"" + command + "\" reqTime=\""
                        + time.toString() + "\">" + "" + "<params>"
                        + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                        + "<param name=\"PID\">" + pid + "</param>"
                        + "<param name=\"TXAM\">" + txam + "</param>"
                        + "<param name=\"TAMT\">" + tamt + "</param>"
                        + "<param name=\"CAMT\">" + camt + "</param>"
                        + "<param name=\"TPAM\">" + tpam + "</param>"
                        + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel generateMPIN(int command, String MOB)
            throws Exception {
        Long time = new Date().getTime();

        String requestString =
                "<msg id=\"" + command + "\" reqTime=\""
                        + time.toString() + "\">" + "" + "<params>"
                        + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                        + "<param name=\"MOBN\">" + MOB + "</param>"
                        + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel resetLoginPin(int command, String MOB, String CNIC)
            throws Exception {
        Long time = new Date().getTime();

        String requestString =
                "<msg id=\"" + command + "\" reqTime=\""
                        + time.toString() + "\">" + "" + "<params>"
                        + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                        + "<param name=\"CMOB\">" + MOB + "</param>"
                        + "<param name=\"CNIC\">" + CNIC + "</param>"
                        + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }
    public HttpResponseModel forgotMpin(int command, String CNIC, String MOB)
            throws Exception {
        Long time = new Date().getTime();

        String requestString =
                "<msg id=\"" + command + "\" reqTime=\""
                        + time.toString() + "\">" + "" + "<params>"
                        + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                        + "<param name=\"CNIC\">" + CNIC + "</param>"
                        + "<param name=\"CMOB\">" + MOB + "</param>"
                        + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }
    public HttpResponseModel setMpinLater(int command, String MOB, String CNIC, String ISLATER)
            throws Exception {
        Long time = new Date().getTime();

        String requestString =
                "<msg id=\"" + command + "\" reqTime=\""
                        + time.toString() + "\">" + "" + "<params>"
                        + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                        + "<param name=\"CNIC\">" + CNIC + "</param>"
                        + "<param name=\"CMOB\">" + MOB + "</param>"
                        + "<param name=\"IS_SET_MPIN_LATER\">" + ISLATER + "</param>"
                        + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel bookmeInquiry(int command, String PID, String MOB, String BAMT, String PMTTYPE, String ORDERID, String STYPE, String SPNAME, String BFARE,String TAPAMT,String DAMT,String TAX,String FEE,String BNAME,String BPHONE,String BCNIC,String BEMAIL)
            throws Exception {
        Long time = new Date().getTime();

        String requestString =
                "<msg id=\"" + command + "\" reqTime=\""
                        + time.toString() + "\">" + "" + "<params>"
                        + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                        + "<param name=\"PID\">" + PID + "</param>"
                        + "<param name=\"CMOB\">" + MOB + "</param>"
                        + "<param name=\"BAMT\">" + BAMT + "</param>"
                        + "<param name=\"PMTTYPE\">" + PMTTYPE + "</param>"
                        + "<param name=\"ORDERID\">" + ORDERID + "</param>"
                        + "<param name=\"STYPE\">" + STYPE + "</param>"
                        + "<param name=\"SPNAME\">" + SPNAME + "</param>"
                        + "<param name=\"BFARE\">" + BFARE + "</param>"
                        + "<param name=\"TAPAMT\">" + TAPAMT + "</param>"
                        + "<param name=\"DAMT\">" + DAMT + "</param>"
                        + "<param name=\"TAX\">" + TAX + "</param>"
                        + "<param name=\"FEE\">" + FEE + "</param>"
                        + "<param name=\"BNAME\">" + BNAME + "</param>"
                        + "<param name=\"BCNIC\">" + BCNIC + "</param>"
                        + "<param name=\"BMOB\">" + BPHONE + "</param>"
                        + "<param name=\"BEMAIL\">" + BEMAIL + "</param>"
                        + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }
    public HttpResponseModel bookmePayment(int command, String PID, String MOB, String BAMT, String PMTTYPE, String ORDERID, String STYPE, String TPAM, String TAMT, String CAMT, String SPNAME, String BFARE,String TAPAMT,String DAMT,String TAX,String FEE,String BNAME,String BPHONE,String BCNIC,String BEMAIL)
            throws Exception {
        Long time = new Date().getTime();

        String requestString =
                "<msg id=\"" + command + "\" reqTime=\""
                        + time.toString() + "\">" + "" + "<params>"
                        + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                        + "<param name=\"PID\">" + PID + "</param>"
                        + "<param name=\"CMOB\">" + MOB + "</param>"
                        + "<param name=\"BAMT\">" + BAMT + "</param>"
                        + "<param name=\"PMTTYPE\">" + PMTTYPE + "</param>"
                        + "<param name=\"ORDERID\">" + ORDERID + "</param>"
                        + "<param name=\"STYPE\">" + STYPE + "</param>"
                        + "<param name=\"TPAM\">" + TPAM + "</param>"
                        + "<param name=\"TAMT\">" + TAMT + "</param>"
                        + "<param name=\"CAMT\">" + CAMT + "</param>"
                        + "<param name=\"SPNAME\">" + SPNAME + "</param>"
                        + "<param name=\"BFARE\">" + BFARE + "</param>"
                        + "<param name=\"TAPAMT\">" + TAPAMT + "</param>"
                        + "<param name=\"DAMT\">" + DAMT + "</param>"
                        + "<param name=\"TAX\">" + TAX + "</param>"
                        + "<param name=\"FEE\">" + FEE + "</param>"
                        + "<param name=\"BNAME\">" + BNAME + "</param>"
                        + "<param name=\"BCNIC\">" + BCNIC + "</param>"
                        + "<param name=\"BMOB\">" + BPHONE + "</param>"
                        + "<param name=\"BEMAIL\">" + BEMAIL + "</param>"
                        + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel debitCardBlockType(int command, String PID, String DBTYPE)
            throws Exception {
        Long time = new Date().getTime();

        String requestString =
                "<msg id=\"" + command + "\" reqTime=\""
                        + time.toString() + "\">" + "" + "<params>"
                        + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                        + "<param name=\"PID\">" + PID + "</param>"
                        + "<param name=\"DBTYPE\">" + DBTYPE + "</param>"
                        + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }
    public HttpResponseModel debitCardActivationAndChangePin(int command, String PID, String NPIN, String CPIN)
            throws Exception {
        Long time = new Date().getTime();

        String requestString =
                "<msg id=\"" + command + "\" reqTime=\""
                        + time.toString() + "\">" + "" + "<params>"
                        + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                        + "<param name=\"PID\">" + PID + "</param>"
                        + "<param name=\"NPIN\">" + NPIN + "</param>"
                        + "<param name=\"CPIN\">" + CPIN + "</param>"
                        + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }
    public HttpResponseModel debitCardInquiry(int command, String PID, String MOB)
            throws Exception {
        Long time = new Date().getTime();

        String requestString =
                "<msg id=\"" + command + "\" reqTime=\""
                        + time.toString() + "\">" + "" + "<params>"
                        + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                        + "<param name=\"PID\">" + PID + "</param>"
                        + "<param name=\"CMOB\">" + MOB + "</param>"
                        + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel forgotPassword(int command, String MOB)
            throws Exception {
        Long time = new Date().getTime();

        String requestString =
                "<msg id=\"" + command + "\" reqTime=\""
                        + time.toString() + "\">" + "" + "<params>"
                        + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                        + "<param name=\"MOBN\">" + MOB + "</param>"
                        + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
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
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"APPID\">" + Constants.APP_ID + "</param>"
                + "<param name=\"CNIC\">" + cnic + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel forgotPasswordConfirm(int command, String MOB, String OTP, String NMPIN, String CMPIN)
            throws Exception {
        Long time = new Date().getTime();

        String requestString =
                "<msg id=\"" + command + "\" reqTime=\""
                        + time.toString() + "\">" + "" + "<params>"
                        + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                        + "<param name=\"MOBN\">" + MOB + "</param>"
//                        + "<param name=\"OTP\">" + OTP + "</param>"
                        + "<param name=\"NMPIN\">" + NMPIN + "</param>"
                        + "<param name=\"CMPIN\">" + CMPIN + "</param>"
                        + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel debitCardRequest(int command) throws Exception {

        String requestString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><msg id=\""
                + command
                + "\" reqTime=\""
                + new Date().getTime()
                + "\">" +
                "    <params>\n" +
//                "        <param name=\"CNIC\">" + cnic + "</param>\n" +
//                "        <param name=\"CMOB\">" + mobNO + "</param>\n" +
//                "        <param name=\"CARD_DESCRIPTION\">" + embossingName + "</param>\n" +
//                "        <param name=\"MAILING_ADDRESS\">" + mailingAddress + "</param>\n" +
                "        <param name=\"APPID\">" + Constants.APP_ID + "</param>\n" +
                "        <param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>\n" +
                "    </params>\n" +
                "</msg>";
        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel advanceLoanInfo(int command, String pid, String mobileNo, String cnic, String amount) throws Exception {

        String requestString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><msg id=\""
                + command
                + "\" reqTime=\""
                + new Date().getTime()
                + "\">" +
                "    <params>\n" +
                "        <param name=\"PID\">" + pid + "</param>\n" +
                "        <param name=\"CNIC\">" + cnic + "</param>\n" +
                "        <param name=\"CMOB\">" + mobileNo + "</param>\n" +
                "        <param name=\"TXAM\">" + amount + "</param>\n" +
                "        <param name=\"APPID\">" + Constants.APP_ID + "</param>\n" +
                "        <param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>\n" +
                "    </params>\n" +
                "</msg>";
        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel debitCardRequestConfirmation(int command,String embossingName, String mailingAddress) throws Exception {

        String requestString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><msg id=\""
                + command
                + "\" reqTime=\""
                + new Date().getTime()
                + "\">" +
                "    <params>\n" +
//                "        <param name=\"CNIC\">" + cnic + "</param>\n" +
//                "        <param name=\"CMOB\">" + mobNO + "</param>\n" +
                "        <param name=\"CARD_DESCRIPTION\">" + embossingName + "</param>\n" +
                "        <param name=\"MAILING_ADDRESS\">" + mailingAddress + "</param>\n" +
                "        <param name=\"APPID\">" + Constants.APP_ID + "</param>\n" +
                "        <param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>\n" +
                "    </params>\n" +
                "</msg>";
        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }
    public HttpResponseModel debitCardRequestConfirmation(int command, String mobNO,
                                                          String cnic, String embossingName, String mailingAddress) throws Exception {

        String requestString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><msg id=\""
                + command
                + "\" reqTime=\""
                + new Date().getTime()
                + "\">" +
                "    <params>\n" +
                "        <param name=\"CNIC\">" + cnic + "</param>\n" +
                "        <param name=\"CMOB\">" + mobNO + "</param>\n" +
                "        <param name=\"CARD_DESCRIPTION\">" + embossingName + "</param>\n" +
                "        <param name=\"MAILING_ADDRESS\">" + mailingAddress + "</param>\n" +
                "        <param name=\"APPID\">" + Constants.APP_ID + "</param>\n" +
                "        <param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>\n" +
                "    </params>\n" +
                "</msg>";
        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }


    public HttpResponseModel openAccountNew(int command, String cnic, String cmob,
                                          String suAccType, String cnicExp,
                                         String mobileNetwork,String email
    )
            throws Exception {

        Long time = new Date().getTime();
        String requestString = "";


            requestString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><msg id=\"" + command + "\" reqTime=\""
                    + new Date().getTime() + "\"><params>"
                    + "<param name=\"DATETIME\">" + new Date().getTime() + "</param>"
                    + "<param name=\"CNIC\">" + cnic + "</param>"
                    + "<param name=\"CMOB\">" + cmob + "</param>"
                    + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                    + "<param name=\"CNIC_ISSUE_DATE\">" + cnicExp + "</param>"
                    + "<param name=\"CUST_MOB_NETWORK\">" + mobileNetwork + "</param>"
                    + "<param name=\"EMAIL_ADDRESS\">" + email + "</param>"
                    + "<param name=\"IS_NEW_ACCOUNT\">" + "1" + "</param>"
                    + "<param name=\"CUST_ACC_TYPE\">" + suAccType + "</param>"
                    + "</params></msg> ";


        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }



    public HttpResponseModel openAccount(int command, String customerPhoto, String cnicFrontPhoto, String cname, String cnic, String cmob,
                                         String cdob, String suAccType, String cnicExp, String isCnicSeen, String birthPlace,
                                         String presentAddress, String cardExpired, String fatherHusbandName, String motherName, String gender,
                                         String discrepant, String isHRA, String mobileNetwork
    )
            throws Exception {

        Long time = new Date().getTime();
        String requestString = "";

        if (suAccType.equals("1")) {

            requestString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><msg id=\""
                    + command
                    + "\" reqTime=\""
                    + new Date().getTime()
                    + "\">"
                    + "<params>"
                    + "<param name=\"DTID\">5</param>"
                    + "<param name=\"CUSTOMER_PHOTO\">" + customerPhoto + "</param>"
                    + "<param name=\"CNIC_FRONT_PHOTO\">"
                    + cnicFrontPhoto
                    + "</param>"
                    + "<param name=\"CNAME\">"
                    + StringEscapeUtils.escapeXml(cname)
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
//                + "<param name=\"IS_CNIC_SEEN\">"
//                + isCnicSeen
//                + "</param>"
                    + "<param name=\"CDOB\">"
                    + cdob
                    + "</param>"
                    + "<param name=\"CUST_ACC_TYPE\">"
                    + suAccType
                    + "</param>"
                    + "<param name=\"IS_DISCREPANT\">"
                    + discrepant
                    + "</param>"
                    + "<param name=\"CNIC_EXP\">"
                    + cnicExp
                    + "</param>"
                    + "<param name=\"IS_UPGRADE\">" + isHRA + "</param>"
                    + "</params></msg> ";
        } else {

            requestString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><msg id=\"" + command + "\" reqTime=\""
                    + new Date().getTime() + "\"><params>"
                    + "<param name=\"DATETIME\">" + new Date().getTime() + "</param>"
                    + "<param name=\"CNIC\">" + cnic + "</param>"
                    + "<param name=\"CMOB\">" + cmob + "</param>"
                    + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"

                    + "<param name=\"CNAME\">" + cname + "</param>"
//                + "<param name=\"ACCTITLE\">" + name1 + "</param>"
                    + "<param name=\"BIRTH_PLACE\">" + birthPlace + "</param>"
                    + "<param name=\"PRESENT_ADDR\">" + presentAddress + "</param>"
                    + "<param name=\"IS_DISCREPANT\">"
                    + discrepant
                    + "</param>"

                    + "<param name=\"CNIC_STATUS\">" + cardExpired + "</param>"
                    + "<param name=\"CNIC_EXP\">" + cnicExp + "</param>"
                    + "<param name=\"CDOB\">" + cdob + "</param>"
                    + "<param name=\"FATHER_NAME\">" + fatherHusbandName + "</param>"
                    + "<param name=\"CUST_MOB_NETWORK\">" + mobileNetwork + "</param>"
                    + "<param name=\"MOTHER_MAIDEN\">" + motherName + "</param>"
                    + "<param name=\"GENDER\">" + gender + "</param>"
                    + "<param name=\"IS_UPGRADE\">" + isHRA + "</param>"
                    + "<param name=\"CUST_ACC_TYPE\">"
                    + suAccType
                    + "</param>"
                    + "</params></msg> ";
        }


        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }


    public HttpResponseModel customerRegisterOTP(int command, String CNIC, String MOB, String otp)
            throws Exception {

        Long time = new Date().getTime();

        String requestString =
                "<msg id=\"" + command + "\" reqTime=\""
                        + time.toString() + "\">" + "" + "<params>"
                        + "<param name=\"CMDID\">" + command + "</param>"
                        + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                        + "<param name=\"MOBN\">" + MOB + "</param>"
                        + "<param name=\"CNIC\">" + CNIC + "</param>"
                        + "<param name=\"PIN\">" + otp + "</param>"
                        + "<param name=\"ACTION\">" + "0" + "</param>"
                        + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }


    public HttpResponseModel customerTermsCondition(int command, String zongSubs, String bvsVerf, String cnic,
                                                    String mobileNum, String regCnic, String strUniqueDeivceID,
                                                    String strAndroidVersion, String strDeviceVendor,
                                                    String strDeviceNetwork, String strDeviceModel)
            throws Exception {

        Long time = new Date().getTime();

        String requestString =
                "<msg id=\"" + command + "\" reqTime=\""
                        + time.toString() + "\">" + ""
                        + "<params>"
                        + "<param name=\"IS_ZONG_SUBS\">" + zongSubs + "</param>"
                        + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                        + "<param name=\"IS_BVS_VERF\">" + bvsVerf + "</param>"
                        + "<param name=\"CNIC\">" + cnic + "</param>"
                        + "<param name=\"MOBN\">" + mobileNum + "</param>"
                        + "<param name=\"IS_CNIC_REG\">" + regCnic + "</param>"
                        + "<param name=\"UDID\">" + strUniqueDeivceID + "</param>"
                        + "<param name=\"OS\">Android</param>"
                        + "<param name=\"OSVERSION\">" + strAndroidVersion + "</param>"
                        + "<param name=\"VENDOR\">" + strDeviceVendor + "</param>"
                        + "<param name=\"NETWORK\">" + strDeviceNetwork + "</param>"
                        + "<param name=\"MODEL\">" + strDeviceModel + "</param>"
                        + "</params>"
                        + "</msg>";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel changePin(int command, String oldPIN,
                                       String newPIN, String confirmPIN) throws Exception {

        Long time = new Date().getTime();

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + time.toString() + "\">" + ""
                + "<params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PIN\">" + oldPIN + "</param>"
                + "<param name=\"NPIN\">" + newPIN + "</param>"
                + "<param name=\"CPIN\">" + confirmPIN + "</param>"
                + "<param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    //    public HttpResponseModel changePin(int command, String newPIN) throws Exception {
//
//        Long time = new Date().getTime();
//
//        String requestString = "<msg id=\"" + command + "\" reqTime=\""
//                + time.toString() + "\">" + ""
//                + "<params>"
//                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
//                + "<param name=\"PGR\">" + newPIN + "</param>"
//                + "</params></msg> ";
//
//        AppLogger.i("XML Request: " + requestString + "\n");
//
//
//        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
//        return serverResponse;
//    }
    public HttpResponseModel changePin(int command, String newPIN, String confirmPIN) throws Exception {

        Long time = new Date().getTime();

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + time.toString() + "\">" + ""
                + "<params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"NPIN\">" + newPIN + "</param>"
                + "<param name=\"CPIN\">" + confirmPIN + "</param>"
                + "<param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel openAccountL0ToL1(int command, String mpin, String mobileNumber,
                                               String cnic, String cname, String dob, String fatherHusbandMane,
                                               String agentMobile, String productId, String trnPur, String strOccupation,
                                               String strOrgLoc1, String strOrgLoc2, String strOrgLoc3, String strOrgLoc4, String strOrgLoc5,
                                               String strOrgRel1, String strOrgRel2, String strOrgRel3, String strOrgRel4, String strOrgRel5,
                                               String SOI, String kinName, String kinMobileNo, String kinCNIC, String kinRel


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
//                + "<param name=\"PID\">" + productId + "</param>"
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

                + "<param name=\"TRX_PUR\">" + trnPur + "</param>"
                + "<param name=\"SOI\">" + SOI + "</param>"
                + "<param name=\"KIN_NAME\">" + kinName + "</param>"
                + "<param name=\"Kin_MOB_NO\">" + kinMobileNo + "</param>"
                + "<param name=\"KIN_CNIC\">" + kinCNIC + "</param>"
                + "<param name=\"KIN_RELATIONSHIP\">" + kinRel + "</param>"

                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel checkBalance(int command, String pin,
                                          String acctType, String apid, String bbacid, String isHRA) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PIN\">" + pin + "</param>"
                + "<param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE + "</param>"
                + "<param name=\"ACCTYPE\">"
                + StringEscapeUtils.escapeXml(acctType) + "</param>"
                + "<param name=\"APID\">" + apid + "</param>"
                + "<param name=\"APPID\">" + "2" + "</param>"
                + "<param name=\"PAYMENT_MODE\">" + isHRA + "</param>"
                + "<param name=\"BBACID\">"
                + StringEscapeUtils.escapeXml(bbacid)
                + "</param></params></msg>";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel myLimits(int command, String pin) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\"" + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PIN\">" + pin + "</param>"
                + "<param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE + "</param>"
                + "</params></msg>";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel login(int command, String username,
                                   String password, String cvno, String isRooted, String osVer,
                                   String model, String vendor, String network, String udid) throws Exception {

        AppLogger.i(Constants.APPURL);

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\">"
                + "<params>"
                + "<param name=\"PIN\">" + password + "</param>"
                + "<param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE + "</param>"
                + "<param name=\"UID\">" + StringEscapeUtils.escapeXml(username) + "</param>"
                + "<param name=\"CVNO\">" + cvno + "</param>"
                + "<param name=\"APPV\">" + Constants.APPLICATION_VERSION + "</param>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"ISROOTED\">" + isRooted + "</param>"
                + "<param name=\"OS\">Android</param>"
                + "<param name=\"OSVERSION\">" + osVer + "</param>"
                + "<param name=\"MODEL\">" + model + "</param>"
                + "<param name=\"VENDOR\">" + vendor + "</param>"
                + "<param name=\"NETWORK\">" + network + "</param>"
                + "<param name=\"UDID\">" + udid + "</param>"
                + "<param name=\"USTY\">" + Constants.USER_TYPE + "</param>"
                + "<param name=\"APPID\">" + Constants.APP_ID + "</param>"
                + "</params>"
                + "</msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);

        return serverResponse;
    }

//    public HttpResponseModel cashWithdrawal(int command) throws Exception {
//        request = getRequest();
//        Long time = new Date().getTime();
//
//        String requestString = "<msg id=\"" + command + "\" reqTime=\""
//                + time.toString() + "\">" + "" + "<params>"
//                + "<param name=\"DTID\">5</param>"
//                + "<param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE + "</param>"
//                + "</params></msg> ";
//
//        AppLogger.i("XML Request: " + requestString + "\n");
//        List<NameValuePair> params = new LinkedList<NameValuePair>();
//        params.add(new BasicNameValuePair("message", requestString));
//        if (!params.isEmpty()) {
//            request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
//        }
//
//        serverResponse = HttpUtils.sendHttpCall(command, httpClient, request);
//
//        return serverResponse;
//    }

    public HttpResponseModel otpVerification(int command, String action, String udid,
                                             String mPin, String uid, String cvno) throws Exception {

        Long time = new Date().getTime();

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + time.toString() + "\">" + "" + "<params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"APPID\">" + Constants.APP_ID + "</param>"
                + "<param name=\"PIN\">" + mPin + "</param>"
                + "<param name=\"UDID\">" + udid + "</param>"
                + "<param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE + "</param>"
                + "<param name=\"UID\">" + uid + "</param>"
                + "<param name=\"USTY\">" + Constants.USER_TYPE + "</param>"
                + "<param name=\"CVNO\">" + cvno + "</param>"
                + "<param name=\"APPV\">" + Constants.APPLICATION_VERSION + "</param>"
                + "<param name=\"ACTION\">" + action + "</param>"
                + "<param name=\"OS\">Android</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel otpVerification(int command, String action, String udid, String uid, String cvno) throws Exception {

        Long time = new Date().getTime();

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + time.toString() + "\">" + "" + "<params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"APPID\">" + Constants.APP_ID + "</param>"
                + "<param name=\"UDID\">" + udid + "</param>"
                + "<param name=\"USTY\">" + Constants.USER_TYPE + "</param>"
                + "<param name=\"UID\">" + uid + "</param>"
                + "<param name=\"USTY\">" + Constants.USER_TYPE + "</param>"
                + "<param name=\"CVNO\">" + cvno + "</param>"
                + "<param name=\"APPV\">" + Constants.APPLICATION_VERSION + "</param>"
                + "<param name=\"ACTION\">" + action + "</param>"
                + "<param name=\"OS\">Android</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel miniLoadInfo(int command, String PID, String TMOB, String TXAM) throws Exception {

        String requestString = null;

        requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PID\">" + PID + "</param>"
                + "<param name=\"TMOB\">" + TMOB + "</param>"
                + "<param name=\"TXAM\">" + TXAM + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel schedulePayment(int command, String PID) throws Exception {

        String requestString = null;

        requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PID\">" + PID + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel miniLoad(int command, String pin, String PID, String TMOB, String TXAM,
                                      String CAMT, String TPAM, String TAMT) throws Exception {

        String requestString = null;

        requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PID\">" + PID + "</param>"
                + "<param name=\"PIN\">" + pin + "</param>"
                + "<param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE + "</param>"
                + "<param name=\"TMOB\">" + TMOB + "</param>"
                + "<param name=\"TXAM\">" + TXAM + "</param>"
                + "<param name=\"CAMT\">" + CAMT + "</param>"
                + "<param name=\"TPAM\">" + TPAM + "</param>"
                + "<param name=\"TAMT\">" + TAMT + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }


    public HttpResponseModel retailPaymentMerchantVerification(int command, String MRID,
                                                               String TAMT, String qrString)
            throws Exception {

        String requestString = null;

        requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"MRID\">" + MRID + "</param>"
                + "<param name=\"TAMT\">" + TAMT + "</param>"
                + "<param name=\"QR_STRING\">" + qrString + "</param>"
                + "</params></msg> ";


        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }


    public HttpResponseModel retailPaymenyMPass(int command, String MRID,
                                                String TAMT, String MNAME, String qrString)
            throws Exception {

        String requestString = null;

        requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"MRID\">" + MRID + "</param>"
                + "<param name=\"TAMT\">" + TAMT + "</param>"
                + "<param name=\"MNAME\">" + MNAME + "</param>"
                + "<param name=\"QR_STRING\">" + qrString + "</param>"
                + "</params></msg> ";


        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }


    public HttpResponseModel collectionPaymentInfo(int command, String PID,
                                                   String CONSUMER, String TXAM)
            throws Exception {

        String requestString = null;

        requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PID\">" + PID + "</param>"
                + "<param name=\"CMOB\">" + ApplicationData.userId + "</param>"
                + "<param name=\"CSCD\">" + StringEscapeUtils.escapeXml(CONSUMER) + "</param>"
                + "</params></msg> ";


        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }


    public HttpResponseModel collectionPaymentConfirmation(int command, String PID,
                                                           String CONSUMER, String MOBN, String CNIC, String TXAM, String TPAM, String TAMT)
            throws Exception {

        String requestString = null;

        requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PID\">" + PID + "</param>"
                + "<param name=\"CSCD\">" + StringEscapeUtils.escapeXml(CONSUMER) + "</param>"
                + "<param name=\"CMOB\">" + MOBN + "</param>"
                + "<param name=\"BAMT\">" + TXAM + "</param>"
                + "<param name=\"TPAM\">" + TPAM + "</param>"
                + "<param name=\"TAMT\">" + TAMT + "</param>"
                + "</params></msg> ";


        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel billInquiry(int command, String PID, String AMOB,
                                         String CMOB, String CONSUMER, String BAMOUNT, String BTYPE, String PMTTYPE, String BAID)
            throws Exception {

        String requestString = null;

        requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PID\">" + PID + "</param><param name=\"AMOB\">" + AMOB + "</param>"
                + "<param name=\"CMOB\">" + CMOB + "</param>"
                + "<param name=\"BAMT\">" + BAMOUNT + "</param>"
                + "<param name=\"CSCD\">"
                + StringEscapeUtils.escapeXml(CONSUMER) + "</param>"
                + "<param name=\"PMTTYPE\">" + PMTTYPE + "</param>"
                + "<param name=\"BAID\">" + StringEscapeUtils.escapeXml(BAID)
                + "</param>" + "</params></msg> ";
        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel billPayment(int command, String pin, String pid,
                                         String cmob, String consumer, String pmttype, String baid,
                                         String txam, String amob) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PID\">" + pid + "</param>"
                + "<param name=\"PIN\">" + pin + "</param>"
                + "<param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE + "</param>"
                + "<param name=\"CMOB\">" + cmob + "</param>"
                + "<param name=\"AMOB\">" + amob + "</param>"
                + "<param name=\"CSCD\">"
                + StringEscapeUtils.escapeXml(consumer) + "</param>"
                + "<param name=\"PMTTYPE\">" + pmttype + "</param>"
                + "<param name=\"BAID\">" + StringEscapeUtils.escapeXml(baid)
                + "</param>" + "<param name=\"BAMT\">" + txam + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel fetchMiniStatement(int command, String pin,
                                                String ACCTYPE, String APID, String BBACID, String isHRA) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PIN\">" + pin + "</param>"
                + "<param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE + "</param>"
                + "<param name=\"ACCTYPE\">"
                + StringEscapeUtils.escapeXml(ACCTYPE) + "</param>"
                + "<param name=\"STNO\">1</param>"
                + "<param name=\"ETNO\">5</param>" + "<param name=\"APID\">"
                + APID + "</param>" + "<param name=\"BBACID\">"

                + StringEscapeUtils.escapeXml(BBACID) + "</param>"
                + "<param name=\"PAYMENT_MODE\">" + isHRA + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }


    public HttpResponseModel VerifyAccountRequst(int command, String cnic, String mob, String tType)
            throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DATETIME\">" + new Date().getTime() + "</param>"
                + "<param name=\"CNIC\">" + cnic + "</param>"
                + "<param name=\"MOBN\">" + mob + "</param>"
                + "<param name=\"TTYPE\">" + tType + "</param>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }


    public HttpResponseModel accountOpeningRequest(int command, String mobileNumber,
                                                   String cnic, String birthPlace
            , String name, String motherName
            , String dob, String cardExpired
            , String presentAddress, String permanentAddress
            , String name1, String gender
            , String fatherHusbandName, String expiryDate,
                                                   String accountType
    )
            throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DATETIME\">" + new Date().getTime() + "</param>"
                + "<param name=\"CNIC\">" + cnic + "</param>"
                + "<param name=\"MOBN\">" + mobileNumber + "</param>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"

                + "<param name=\"CNAME\">" + name + "</param>"
                + "<param name=\"ACCTITLE\">" + name1 + "</param>"
                + "<param name=\"BIRTH_PLACE\">" + birthPlace + "</param>"
                + "<param name=\"PRESENT_ADDR\">" + presentAddress + "</param>"

                + "<param name=\"CNIC_STATUS\">" + cardExpired + "</param>"
                + "<param name=\"CNIC_EXP\">" + expiryDate + "</param>"
                + "<param name=\"DOB\">" + dob + "</param>"
                + "<param name=\"FATHER_NAME\">" + fatherHusbandName + "</param>"

                + "<param name=\"MOTHER_MAIDEN\">" + motherName + "</param>"
                + "<param name=\"GENDER\">" + gender + "</param>"

                //  + "<param name=\"CREATE_VCARD\">" + accountType + "</param>"

                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }


    public HttpResponseModel ftBlbToCnicInfo(int command, String pid,
                                             String rcmob, String rwcnic,
                                             String txam)
            throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PID\">" + pid + "</param>"
                + "<param name=\"RCMOB\">" + rcmob + "</param>"
                + "<param name=\"RWCNIC\">" + rwcnic + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "<param name=\"CMOB\">" + ApplicationData.customerMobileNumber + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel ftBlbToCnic(int command, String pin, String pid,
                                         String rwmob, String rwcnic, String txam,
                                         String camt, String tpam, String tamt, String tpurps, String purposeCode) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PID\">" + pid + "</param>"
                + "<param name=\"PIN\">" + pin + "</param>"
                + "<param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE + "</param>"
                + "<param name=\"RCMOB\">" + rwmob + "</param>"
                + "<param name=\"CMOB\">" + ApplicationData.customerMobileNumber + "</param>"
                + "<param name=\"RWCNIC\">" + rwcnic + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "<param name=\"CAMT\">" + camt + "</param>"
                + "<param name=\"TPAM\">" + tpam + "</param>"
                + "<param name=\"TAMT\">" + tamt + "</param>"
                + "<param name=\"TRX_PUR\">" + tpurps + "</param>"
                + "<param name=\"TRANS_PURPOSE_CODE\">" + purposeCode + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
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
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE + "</param>"
                + "<param name=\"PIN\">"
                + pin
                + "</param>"
                + "<param name=\"PIN_RETRY_COUNT\">"
                + retryCount + "</param>" + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel ftBlbToBlb(int command, String pin, String pid,
                                        String rcmob, String txam, String camt,
                                        String tpam, String tamt) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PID\">" + pid + "</param><param name=\"PIN\">" + pin + "</param>"
                + "<param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE + "</param>"
                + "<param name=\"RCMOB\">" + rcmob + "</param>"
                + "<param name=\"CMOB\">" + ApplicationData.customerMobileNumber + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "<param name=\"CAMT\">" + camt + "</param>"
                + "<param name=\"TPAM\">" + tpam + "</param>"
                + "<param name=\"TAMT\">" + tamt + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel advanceLoan(int command, String pin, String pid,
                                             String cnic, String cmob, String txam, String camt,
                                        String tpam, String tamt, String thirdPartyRRN) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PID\">" + pid + "</param>"
                + "<param name=\"PIN\">" + pin + "</param>"
                + "<param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE + "</param>"
                + "<param name=\"CMOB\">" + cmob + "</param>"
                + "<param name=\"CNIC\">" + cnic + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "<param name=\"CAMT\">" + camt + "</param>"
                + "<param name=\"TPAM\">" + tpam + "</param>"
                + "<param name=\"TAMT\">" + tamt + "</param>"
                + "<param name=\"THIRD_PARTY_RRN\">" + thirdPartyRRN + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel ftBlbToBlbInfo(int command, String PID,
                                            String rcmob, String txam)
            throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PID\">" + PID + "</param>"
                + "<param name=\"RCMOB\">" + rcmob + "</param>"
                + "<param name=\"CMOB\">" + ApplicationData.customerMobileNumber + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel transferInInfo(int command, String PID, String txam) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PID\">" + PID + "</param><param name=\"TXAM\">" + txam + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel transferIn(int command, String pid,
                                        String txam, String camt, String tpam, String tamt)
            throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PID\">" + pid + "</param><param name=\"TXAM\">" + txam + "</param>"
                + "<param name=\"CAMT\">" + camt + "</param>"
                + "<param name=\"TPAM\">" + tpam + "</param>"
                + "<param name=\"TAMT\">" + tamt + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel transferOutInfo(int command, String PID, String txam) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PID\">" + PID + "</param>" + "<param name=\"TXAM\">" + txam + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel transferOut(int command, String pid,
                                         String bbacid, String coreacid, String coreactl, String baid,
                                         String txam, String camt, String tpam, String tamt)
            throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PID\">" + pid + "</param>"
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


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel ftBlbToCoreInfo(int command, String pid,
                                             String rcmob, String coreacid, String txam)
            throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PID\">" + pid + "</param>"
                + "<param name=\"RCMOB\">" + rcmob + "</param>"
                + "<param name=\"CMOB\">" + ApplicationData.customerMobileNumber + "</param>"
                + "<param name=\"COREACID\">"
                + StringEscapeUtils.escapeXml(coreacid) + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel ftBlbToCore(int command, String pin, String pid, String rcmob, String coreacid,
                                         String coreactl, String txam, String camt, String tpam, String tamt)
            throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PID\">" + pid + "</param><param name=\"PIN\">" + pin
                + "</param><param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE + "</param>"
                + "<param name=\"RCMOB\">" + rcmob + "</param>"
                + "<param name=\"CMOB\">" + ApplicationData.customerMobileNumber + "</param>"
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


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel ftBlbToIbftInfo(int command, String pid,
                                             String rcmob, String coreacid,
                                             String txam, String baimd,String bankName, String paymentPurposeCode) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PID\">" + pid + "</param>"
                + "<param name=\"RCMOB\">" + rcmob + "</param>"
                + "<param name=\"CMOB\">" + ApplicationData.mobileNo + "</param>"
                + "<param name=\"COREACID\">" + StringEscapeUtils.escapeXml(coreacid) + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "<param name=\"BAIMD\">" + StringEscapeUtils.escapeXml(baimd) + "</param>"
                + "<param name=\"PMTTYPE\"> 0 </param>"
                + "<param name=\"BENE_BANK_NAME\">" +bankName +"</param>"
                + "<param name=\"TRANS_PURPOSE_CODE\">" +paymentPurposeCode +"</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel ftBlbToIbft (int command, String pin, String pid,
                                         String rcmob, String coreacid, String coreactl,
                                         String baimd, String camt, String camtf,
                                         String tpam, String tpamf, String tamt,
                                         String tamtf, String txam, String txamf,
                                         String bankName, String branchName, String iban, String crDr, String accTitle
            , String paymentPurposeCode) throws Exception {

        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PID\">" + pid + "</param>"
                + "<param name=\"PIN\">" + pin + "</param>"
                + "<param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE + "</param>"
                + "<param name=\"RCMOB\">" + rcmob + "</param>"
                + "<param name=\"CMOB\">" + ApplicationData.mobileNo + "</param>"
                + "<param name=\"COREACID\">" + StringEscapeUtils.escapeXml(coreacid) + "</param>"
                + "<param name=\"COREACTL\">" + StringEscapeUtils.escapeXml(coreactl) + "</param>"
                + "<param name=\"BAIMD\">" + baimd + "</param>"
                + "<param name=\"PMTTYPE\"> 0 </param>"
                + "<param name=\"CAMT\">" + camt + "</param>"
                + "<param name=\"CAMTF\">" + camtf + "</param>"
                + "<param name=\"TPAM\">" + tpam + "</param>"
                + "<param name=\"TPAMF\">" + tpamf + "</param>"
                + "<param name=\"TAMT\">" + tamt + "</param>"
                + "<param name=\"TAMTF\">" + tamtf + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "<param name=\"TXAMF\">" + txamf + "</param>"
                + "<param name=\"BENE_BANK_NAME\">" + bankName + "</param>"
                + "<param name=\"BENE_BRANCH_NAME\">" + branchName + "</param>"
                + "<param name=\"BENE_IBAN\">" + iban + "</param>"
                + "<param name=\"CR_DR\">" + crDr + "</param>"
                + "<param name=\"COREACTITLE\">" + accTitle + "</param>"
                + "<param name=\"TRANS_PURPOSE_CODE\">" + paymentPurposeCode + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel retailPaymentInfo(int command, String PID,
                                               String amob, String txam) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PID\">" + PID + "</param>"
                + "<param name=\"AMOB\">" + amob + "</param>"
                + "<param name=\"CMOB\">" + ApplicationData.customerMobileNumber + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel retailPayment(int command, String pin, String pid,
                                           String amob, String txam, String enct, String camt, String tpam,
                                           String tamt) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PID\">" + pid + "</param>"
                + "<param name=\"PIN\">" + pin + "</param>"
                + "<param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE + "</param>"
                + "<param name=\"AMOB\">" + amob + "</param>"
                + "<param name=\"CMOB\">" + ApplicationData.customerMobileNumber + "</param>"
                + "<param name=\"TXAM\">" + txam + "</param>"
                + "<param name=\"CAMT\">" + camt + "</param>"
                + "<param name=\"TPAM\">" + tpam + "</param>"
                + "<param name=\"TAMT\">" + tamt + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel debitCardIssuanceInfo(int command, String pid,
                                                   String strCategory, String strType, String strRank,
                                                   String strApplicantType, String strMobileNumber,
                                                   String strCardNumber, String strBranchCode) {
        HttpResponseModel serverResponse = null;

        String cardOrBranch = "";
        int categoryId = Integer.parseInt(strCategory);
        switch (categoryId) {
            case Constants.DEBIT_CARD_CATEGORY_NONPERSONALIZED:
                cardOrBranch = "<param name=\"CARDNO\">"
                        + StringEscapeUtils.escapeXml(strCardNumber) + "</param>";
                break;
            case Constants.DEBIT_CARD_CATEGORY_PERSONALIZED:
                cardOrBranch = "<param name=\"BRANCH\">"
                        + StringEscapeUtils.escapeXml(strBranchCode) + "</param>";
                break;
        }

        String requestString = "<msg id=\""
                + command
                + "\" reqTime=\""
                + new Date().getTime()
                + "\">"
                + "<params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PID\">"
                + StringEscapeUtils.escapeXml(pid)
                + "</param>"
                + "<param name=\"CAT\">"
                + StringEscapeUtils.escapeXml(strCategory)
                + "</param>"
                + "<param name=\"CTYPE\">"
                + StringEscapeUtils.escapeXml(strType)
                + "</param>"
                // + "<param name=\"RANK\">"
                // + StringEscapeUtils.escapeXml(strRank) + "</param>"
                + "<param name=\"MOBN\">"
                + StringEscapeUtils.escapeXml(strMobileNumber) + "</param>"
                + "<param name=\"USTY\">"
                + StringEscapeUtils.escapeXml(strApplicantType) + "</param>"
                + cardOrBranch + "</params></msg>";

        AppLogger.i("XML Request: " + requestString + "\n");


        try {

            serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);


            AppLogger.i("XML Response: " + serverResponse.getXmlResponse()
                    + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverResponse;
    }

    public HttpResponseModel debitCardIssuance(int command, String mpin, String productId,
                                               String cardCategoryId, String cardTypeId, String cardRankId,
                                               String applicantTypeId, String mobileNumber, String cardNumber,
                                               String otp, String branchCode, String amount, String commission,
                                               String charges, String totalAmount, String trxid) {
        HttpResponseModel serverResponse = null;


        String cardOrBranch = "";
        int categoryId = Integer.parseInt(cardCategoryId);
        switch (categoryId) {
            case Constants.DEBIT_CARD_CATEGORY_NONPERSONALIZED:
                cardOrBranch = "<param name=\"CARDNO\">"
                        + StringEscapeUtils.escapeXml(cardNumber) + "</param>";
                cardOrBranch = cardOrBranch + "<param name=\"OTPIN\">"
                        + StringEscapeUtils.escapeXml(otp) + "</param>"
                        + "<param name=\"TRXID\">" + trxid + "</param>";
                break;
            case Constants.DEBIT_CARD_CATEGORY_PERSONALIZED:
                cardOrBranch = "<param name=\"BRANCH\">"
                        + StringEscapeUtils.escapeXml(branchCode) + "</param>";
                break;
        }

        String requestString = "<msg id=\""
                + command
                + "\" reqTime=\""
                + new Date().getTime()
                + "\">"
                + "<params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"PID\">"
                + productId
                + "</param>"
                + "<param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE + "</param>"
                + "<param name=\"PIN\">"
                + StringEscapeUtils.escapeXml(mpin)
                + "</param>"
                + "<param name=\"CAT\">"
                + StringEscapeUtils.escapeXml(cardCategoryId)
                + "</param>"
                + "<param name=\"CTYPE\">"
                + StringEscapeUtils.escapeXml(cardTypeId)
                + "</param>"
                // + "<param name=\"RANK\">"
                // + StringEscapeUtils.escapeXml(cardRankId) + "</param>"
                + "<param name=\"MOBN\">"
                + StringEscapeUtils.escapeXml(mobileNumber) + "</param>"
                + "<param name=\"USTY\">"
                + StringEscapeUtils.escapeXml(applicantTypeId) + "</param>"
                + cardOrBranch + "<param name=\"TXAM\">"
                + StringEscapeUtils.escapeXml(amount) + "</param>"
                + "<param name=\"CAMT\">"
                + StringEscapeUtils.escapeXml(commission) + "</param>"
                + "<param name=\"TPAM\">"
                + StringEscapeUtils.escapeXml(charges) + "</param>"
                + "<param name=\"TAMT\">"
                + StringEscapeUtils.escapeXml(totalAmount) + "</param>"
                + "</params></msg>";

        AppLogger.i("XML Request: " + requestString + "\n");


        try {
            serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);


            AppLogger.i("XML Response: " + serverResponse.getXmlResponse()
                    + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverResponse;
    }

    public HttpResponseModel debitCardActivationInfo(int command, String mobileNumber,
                                                     String activationType) {
        HttpResponseModel serverResponse = null;


        String requestString = "<msg id=\""
                + command + "\" reqTime=\""
                + new Date().getTime() + "\">" + "<params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"CMOB\">" + StringEscapeUtils.escapeXml(mobileNumber) + "</param>"
                + "<param name=\"OCA\">" + StringEscapeUtils.escapeXml(activationType) + "</param>"
                + "</params></msg>";

        AppLogger.i("XML Request: " + requestString + "\n");


        try {
            serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);

            AppLogger.i("XML Response: " + serverResponse.getXmlResponse()
                    + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverResponse;
    }

    public HttpResponseModel debitCardActivation(int command, String mobileNumber,
                                                 String transactionId, String activationType) {
        HttpResponseModel serverResponse = null;


        String requestString = "<msg id=\""
                + command + "\" reqTime=\""
                + new Date().getTime() + "\">" + "<params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE + "</param>"
                + "<param name=\"CMOB\">" + StringEscapeUtils.escapeXml(mobileNumber) + "</param>"
                + "<param name=\"TRXID\">" + transactionId + "</param>"
                + "<param name=\"OCA\">" + StringEscapeUtils.escapeXml(activationType) + "</param>"
                + "</params></msg>";

        AppLogger.i("XML Request: " + requestString + "\n");


        try {
            serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);

            AppLogger.i("XML Response: " + serverResponse.getXmlResponse()
                    + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverResponse;
    }

//    public HttpResponseModel signOut(int command) throws Exception {
//        request = getRequest();
//
//        String requestString = "<msg id=\"" + command + "\" reqTime=\""
//                + new Date().getTime() + "\"><params>"
//                + "<param name=\"DTID\">5</param>" + "</params></msg> ";
//
//        AppLogger.i("XML Request: " + requestString + "\n");
//
//        List<NameValuePair> params = new LinkedList<NameValuePair>();
//        params.add(new BasicNameValuePair("message", requestString));
//        if (!params.isEmpty()) {
//            request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
//        }
//
//        serverResponse = HttpUtils.sendHttpCall(command, httpClient, request);
//        return serverResponse;
//    }

    public HttpResponseModel simpleRequest(int command) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }



    public HttpResponseModel getBanks(int command) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel faqs(int command, String version) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"FVNO\">" + version + "</param>"
                + "</params></msg> ";


        AppLogger.i("XML Request: " + requestString + "\n");

        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel locator(int command, String radius,
                                     String latitude, String longitude, String type, String pageNo, String pageSize) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"RADIUS\">" + radius + "</param>"
                + "<param name=\"LATITUDE\">" + latitude + "</param>"
                + "<param name=\"LONGITUDE\">" + longitude + "</param>"

                + "<param name=\"PAGE_NO\">" + pageNo + "</param>"
                + "<param name=\"PAGE_SIZE\">" + pageSize + "</param>"

                + "<param name=\"TYPE\">" + type + "</param>"
                + "</params></msg> ";


        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;


    }

    public HttpResponseModel changeAndGeneratePinInfo(int command, String aping) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"APING\">" + aping + "</param>"
                + "</params></msg> ";

        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel changeAtmPin(int command, String oldPin, String newPin,
                                          String confirmPin, String aping) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"OPIN\">" + oldPin + "</param>"
                + "<param name=\"NPIN\">" + newPin + "</param>"
                + "<param name=\"CPIN\">" + confirmPin + "</param>"
                + "<param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE + "</param>"
                + "<param name=\"APING\">" + aping + "</param>"
                + "</params></msg> ";


        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }

    public HttpResponseModel blockDebitCard(int command, String CSID,
                                            String APIN) throws Exception {


        String requestString = "<msg id=\"" + command + "\" reqTime=\""
                + new Date().getTime() + "\"><params>"
                + "<param name=\"DTID\">" + Constants.DEVICE_TYPE_ID + "</param>"
                + "<param name=\"CSID\">" + CSID + "</param>"
                + "<param name=\"APIN\">" + APIN + "</param>"
                + "<param name=\"ENCT\">" + Constants.ENCRYPTION_TYPE + "</param>"
                + "</params></msg> ";


        AppLogger.i("XML Request: " + requestString + "\n");


        serverResponse = HttpUtils.sendHttpCall(command, urlConnection, requestString);
        return serverResponse;
    }


}
