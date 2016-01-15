package com.nikmc.kc.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.nikmc.kc.EnumType;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by NIKMC-I on 24.12.2015.
 */
public class WebServerClient extends AsyncTaskLoader<Request> {

    public String URL;
    protected String ServerURL;
    public Request request;

    protected String XML_TITLE_OPEN = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
            " xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\""+
            " xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
            "<soap:Body>";
    protected String XML_TITLE_END = "</soap:Body></soap:Envelope>";

    protected WebServerClient(Context context) {super(context);}

    @Override
    public Request loadInBackground() {
        request = new Request();
        if(isOnline()) {
            getDateFromServer();
        } else {
            request.enumType = EnumType.NOINTERNET;
        }
        return request;
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
    protected void getDateFromServer() {
        Log.d("SOAP","getDateFromServer start");
        HttpURLConnection connection = null;
        StringBuilder responseBuilder = new StringBuilder();
        try {
            URL url = new URL(ServerURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setReadTimeout(180000);
            connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
//            connection.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");

            Log.d("SOAP", "getDateFromServer getSoapXmlbytes l " + String.valueOf(getSoapXml().getBytes().length));
            connection.setRequestProperty("Content-Length", String.valueOf(getSoapXml().getBytes().length));
            connection.setRequestProperty("SOAPAction", URL);
            Log.d("SOAP", "getDateFromServer getSoapXml " + getSoapXml().toString());
            connection.getOutputStream().write(getSoapXml().getBytes());
/*
            OutputStreamWriter writer = new OutputStreamWriter(
                    connection.getOutputStream());
            writer.write(getSoapXml().getBytes());
*/

            // Closes this output stream and releases any system resources
            // associated with this stream. At this point, we've sent all the
            // data. Only the outputStream is closed at this point, not the
            // actual connection
//            writer.close();

            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            reader.close();
            request.arrayList = getResponceObject(responseBuilder.toString());

        } catch (java.io.IOException e) {
            e.printStackTrace();
            request.enumType = EnumType.ERROR;
            if (Build.VERSION.SDK_INT<=Build.VERSION_CODES.LOLLIPOP_MR1) {
                // call something for API Level 22-
                urlError(connection);
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            request.enumType = EnumType.ERROR;
        }
        Log.d("SOAP", "getDateFromServer finish");

    }

    private void urlError(HttpURLConnection connection){
        Log.e("SOAP", "urlError connection");
        String line;
        StringBuilder responseBuilder = new StringBuilder();
        InputStreamReader input = new InputStreamReader(connection.getErrorStream());
        try {
            BufferedReader reader = new BufferedReader(input);
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("SOAP", responseBuilder.toString());


    }

    protected static HostnameVerifier getHostnameVerifier()
    {
        HostnameVerifier hostnameVerifier = new HostnameVerifier()
        {
            @Override
            public boolean verify(String hostname, SSLSession session)
            {
                return true;
            }
        };
        return hostnameVerifier;
    }

    protected static SSLSocketFactory getValidateCertificates() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = new TrustManager[]
                {
                        new X509TrustManager(){

                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {

                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {

                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }
                        }
                };
        sslContext.init(null, trustManagers, null);
        return sslContext.getSocketFactory();
    }

    public ArrayList getResponceObject(String xml) throws IOException, XmlPullParserException {
        return null;
    }
    public String getSoapXml() {return null;}

}

