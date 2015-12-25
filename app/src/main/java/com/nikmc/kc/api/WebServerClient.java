package com.nikmc.kc.api;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.ConnectivityManager;

import com.nikmc.kc.EnumType;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by NIKMC-I on 24.12.2015.
 */
public class WebServerClient extends AsyncTaskLoader<Request>{

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
        HttpURLConnection connection = null;
        StringBuilder responseBuilder = new StringBuilder();
        try {
            URL url = new URL(ServerURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setReadTimeout(180000);
            connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            connection.setRequestProperty("Context-Length", String.valueOf(getSoapXml().getBytes().length));
            connection.setRequestProperty("SOAPAction", URL);

            connection.getOutputStream().write(getSoapXml().getBytes());
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
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            request.enumType = EnumType.ERROR;
        }

    }

    public ArrayList getResponceObject(String xml) throws IOException, XmlPullParserException {
        return null;
    }
    public String getSoapXml() {return null;}

}

