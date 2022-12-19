package com.inov8.agentmate.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;

import com.inov8.agentmate.activities.BaseActivity;
import com.inov8.agentmate.model.HttpResponseModel;
import com.inov8.agentmate.util.AesEncryptor;
import com.inov8.agentmate.util.AppLogger;
import com.inov8.agentmate.util.ApplicationData;
import com.inov8.agentmate.util.Constants;
import com.inov8.agentmate.util.DummyXmlMessages;

public class HttpUtils {
	private static boolean flag = false;

    public static HttpResponseModel sendHttpCall(String command,
                                                 HttpURLConnection httpUrlConnection, String requestString)
            throws Exception {
        HttpResponseModel response;

        if (ApplicationData.isDummyFlow) {
            response = sendDummyRequest(Integer.valueOf(command));
        } else {
            if(ApplicationData.nadraCall || ApplicationData.nadraOtcCall)
                response = sendRequest(httpUrlConnection, requestString);
            else
                response = sendRequest(httpUrlConnection, AesEncryptor.encrypt(Constants.HANDSHAKE_KEY, requestString));
        }
        return response;
    }

	public static HttpResponseModel sendDummyRequest(int command) {
		HttpResponseModel wrapper = new HttpResponseModel();
		String responseText = DummyXmlMessages.getDummyResponse(command);
		AppLogger.i("XML Response: " + responseText);
		wrapper.setXmlResponse(responseText);
		wrapper.setSessionId("233424343");
		return wrapper;
	}

    public static HttpResponseModel sendRequest(HttpURLConnection urlConnection, String requestString) throws Exception {
        HttpResponseModel response = new HttpResponseModel();
        String responseText = null;
        String session = "";
        InputStream inputStream = null;
        BufferedReader reader = null;

        try {
            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(requestString);
            writer.flush();
            writer.close();
            outputStream.close();

           // parseSessionID(urlConnection.getHeaderField("Set-Cookie"));

            int statusCode = urlConnection.getResponseCode();
            AppLogger.i("Status Code: " + statusCode);

            switch (statusCode) {
                case HttpURLConnection.HTTP_UNAVAILABLE:
                case HttpURLConnection.HTTP_NOT_FOUND:
                    throw new InvalidResponseException(
                            Constants.Messages.EXCEPTION_HTTP_UNAVAILABLE);
                case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
                    throw new TimeoutException(
                            Constants.Messages.EXCEPTION_TIME_OUT);
                case HttpURLConnection.HTTP_OK:
                    break;
                default:
                    AppLogger.i("------- Bad Response from server --------");
                    throw new InvalidResponseException(Constants.Messages.EXCEPTION);
            }

            inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            char charBuf[] = new char[20156];
            StringBuffer sb = new StringBuffer();
            int count;
            while ((count = reader.read(charBuf)) != -1) {
                sb.append(new String(charBuf, 0, count));
            }

            responseText = sb.toString();
            if(!ApplicationData.nadraCall && !ApplicationData.nadraOtcCall)
                responseText  = AesEncryptor.decryptWithAES(Constants.HANDSHAKE_KEY, responseText);
            AppLogger.i(urlConnection.getURL().getHost() + "\n" + responseText + "\nStatus : " + statusCode);

            urlConnection.disconnect();
        } catch (SocketException ex) {
            AppLogger.i("HTTPUtils sendHttpCall Exception Message: "
                    + ex.getMessage());
            responseText = getExceptionResponse(Constants.Messages.EXCEPTION);
        } catch (InvalidResponseException ex) {
            AppLogger.i("HTTPUtils sendHttpCall Exception Message: "
                    + ex.getMessage());
            responseText = getExceptionResponse(Constants.Messages.EXCEPTION);
        } catch (TimeoutException ex) {
            AppLogger.i("HTTPUtils sendHttpCall Exception Message: "
                    + ex.getMessage());
            responseText = getExceptionResponse(Constants.Messages.EXCEPTION);
        } catch (SSLHandshakeException ex) {
            AppLogger.i("SSL Handshake Exception");
            responseText = getExceptionResponse(Constants.Messages.EXCEPTION_SSL_HANDSHAKE);
        } catch (SSLException ex) {
            AppLogger.i("SSL Exception");
            responseText = getSSLExceptionResponse();
        } catch (Exception ex) {
            AppLogger.i("HTTPUtils sendHttpCall Exception Message: "
                    + ex.getMessage());
            responseText = getExceptionResponse(Constants.Messages.EXCEPTION);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (reader != null) {
                    reader.close();
                }

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        response.setXmlResponse(responseText);
        response.setSessionId(session);

        if(ApplicationData.nadraCall  || ApplicationData.nadraOtcCall) {
            ApplicationData.nadraCall = false;
            ApplicationData.nadraOtcCall = false;
     //       AppLogger.i("Json Response: " + BaseActivity.jsonPrettify(responseText) + "\n");
        }
        else
            AppLogger.i("XML response: " + responseText + "\n");
        return response;
    }

    private static String getExceptionResponse(String message) {
        String responseMessage = "<msg id=\"-1\"><errors><error code=\""
                + Constants.ErrorCodes.INTERNAL + "\" level=\"2\">" + message
                + "</error></errors></msg>";
        return responseMessage;
    }

	private static String getSSLExceptionResponse() {
		String responseMessage = "<msg id=\"-1\"><errors><error code=\""
				+ Constants.ErrorCodes.INTERNAL_SSL + "\" level=\"2\">"
				+ Constants.Messages.APPLICATION_SSL_EXPIRE
				+ "</error></errors></msg>";
		return responseMessage;
	}

	public static boolean haveInternet(final Context context) {
		Thread mThread = new Thread() {
			@Override
			public void run() {
				try {
					Looper.prepare();
					synchronized (this) {
						wait(1750);
					}
					ConnectivityManager connectivity = (ConnectivityManager) context
							.getSystemService(Context.CONNECTIVITY_SERVICE);
					if (connectivity != null) {
						NetworkInfo[] info = connectivity.getAllNetworkInfo();
						if (info != null) {
							for (int i = 0; i < info.length; i++) {
								if (info[i].getState() == NetworkInfo.State.CONNECTED) {
									flag = true;
								}
							}
						}
					}
				} catch (InterruptedException ex) {
				}
			}
		};
		mThread.start();
		return flag;

	}

    private static void parseSessionID(String value) {
        try {
            if (value.contains("JSESSIONID")) {
                int index = value.indexOf("JSESSIONID=");
                int endIndex = value.indexOf(";", index);
                ApplicationData.sessionId = value.substring(index + "JSESSIONID=".length(), endIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}