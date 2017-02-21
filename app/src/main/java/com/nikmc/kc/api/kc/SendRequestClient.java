package com.nikmc.kc.api.kc;

import android.content.Context;
import android.util.Log;

import com.nikmc.kc.logic.CheckValid;
import com.nikmc.kc.model.Bid;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by NIKMC-I on 25.12.2015.
 */
public class SendRequestClient extends LKWebServerClient {

    Bid bidrequest;
    ArrayList<String> imageCode;
    public SendRequestClient(Context context, String url, Bid bid, ArrayList<String> image) {
        super(context);
        this.URL = url;
        this.bidrequest = bid;
        this.imageCode = image;
    }

    @Override
    public String getSoapXml() {
        Log.d("SOAP", "getSoapXml start");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(XML_TITLE_OPEN);
        stringBuilder.append("<SendReq xmlns=\"http://tempuri.org/\">");
        stringBuilder.append("<req>");
        stringBuilder.append("<RefRequestCCTypeName>").append("интернет-приёмная").append("</RefRequestCCTypeName>");
//        stringBuilder.append("<Number>").append("null").append("</Number>");
//        stringBuilder.append("<DateBegin>").append("null").append("</DateBegin>");
        stringBuilder.append("<Applicant>");
        if(bidrequest.getFIO().trim().contains(" ")){
            if(CheckValid.isFIO_valid(bidrequest.getFIO().trim())){
                stringBuilder.append("<LastName>").append(bidrequest.getFIO().substring(0, bidrequest.getFIO().indexOf(" ")).trim()).append("</LastName>");
                stringBuilder.append("<FirstName>").append(bidrequest.getFIO().substring(bidrequest.getFIO().indexOf(" ") + 1, bidrequest.getFIO().lastIndexOf(" ")).trim()).append("</FirstName>");
                stringBuilder.append("<MiddleName>").append(bidrequest.getFIO().substring(bidrequest.getFIO().lastIndexOf(" ")+ 1).trim()).append("</MiddleName>");

            } else {
                stringBuilder.append("<LastName>").append(bidrequest.getFIO().substring(0, bidrequest.getFIO().indexOf(" ")).trim()).append("</LastName>");
                stringBuilder.append("<FirstName>").append(bidrequest.getFIO().substring(bidrequest.getFIO().indexOf(" ") + 1).trim()).append("</FirstName>");
                stringBuilder.append("<MiddleName>").append("").append("</MiddleName>");
            }
        }else {
            stringBuilder.append("<LastName>").append("").append("</LastName>");
            stringBuilder.append("<FirstName>").append(bidrequest.getFIO().trim()).append("</FirstName>");
            stringBuilder.append("<MiddleName>").append("").append("</MiddleName>");
        }

        stringBuilder.append("<Phone>").append("+7" + bidrequest.getPhone()).append("</Phone>");
        stringBuilder.append("</Applicant>");
        stringBuilder.append("<ApplicantAddress>");
//        stringBuilder.append("<AddressId>").append("null").append("</AddressId>");
        stringBuilder.append("<Region>").append("Ульяновская область").append("</Region>");
//        stringBuilder.append("<RegionType>").append("null").append("</RegionType>");
        stringBuilder.append("<City>").append(bidrequest.getCity()).append("</City>");
//        stringBuilder.append("<CityType>").append("null").append("</CityType>");
//        stringBuilder.append("<Locality>").append("null").append("</Locality>");
//        stringBuilder.append("<LocalityType>").append("null").append("</LocalityType>");
        stringBuilder.append("<Street>").append(bidrequest.getStreet()).append("</Street>");
//        stringBuilder.append("<StreetType>").append("null").append("</StreetType>");
        stringBuilder.append("<House>").append(bidrequest.getHouse()).append("</House>");
        //stringBuilder.append("<Flat>").append("null").append("</Flat>");
        stringBuilder.append("</ApplicantAddress>");
        stringBuilder.append("<ShortContent>")/*.append("test")*/.append(bidrequest.getContent()).append("</ShortContent>");
//        stringBuilder.append("<VisitDate>").append("null").append("</VisitDate>");
        stringBuilder.append("<Documents>");
        Log.d("SOAP", "imagecode size = " + imageCode.size());
        for(int i=0; i< imageCode.size(); i++) {
            Log.d("SOAP", "imagecode =" + imageCode.get(i));
            stringBuilder.append("<Document>");
            stringBuilder.append("<Name>").append("image"+i +".JPEG").append("</Name>");
            stringBuilder.append("<Data>").append(imageCode.get(i)).append("</Data>");
            stringBuilder.append("</Document>");
        }
        stringBuilder.append("</Documents>");
        stringBuilder.append("</req>");
        stringBuilder.append("</SendReq>");
        stringBuilder.append(XML_TITLE_END);
        Log.d("SOAP", "getSoapXml finish");
        return stringBuilder.toString();
    }

    @Override
    public ArrayList getResponceObject(String xml) throws IOException, XmlPullParserException {
        Log.d("SOAP", "getResponceObject start");
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        String text = null;
        ArrayList<String> strings = new ArrayList<>();
        XmlPullParser parser = factory.newPullParser();
        Log.d("SOAP", "soaprequest" + xml);
        parser.setInput(new StringReader(xml));
        int eventType = parser.getEventType();
        while(eventType != XmlPullParser.END_DOCUMENT) {
            String tagname = parser.getName();
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;
                case XmlPullParser.END_TAG:
                    if(tagname.equalsIgnoreCase("RequestNumber")) {
                        strings.add(text);
                    }
                    break;
                default:
                    break;
            }
            eventType = parser.next();
        }
        Log.d("SOAP", "getResponceObject finish");

        return strings;

    }
}
