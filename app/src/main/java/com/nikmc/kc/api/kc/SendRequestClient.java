package com.nikmc.kc.api.kc;

import android.content.Context;

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

    protected SendRequestClient(Context context, Bid bid) {
        super(context);
        this.bidrequest = bid;
    }

    @Override
    public String getSoapXml() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(XML_TITLE_OPEN);
        stringBuilder.append("<SendReq xmlns=\"http://tempuri.org/\">");

        stringBuilder.append("<req>");
        stringBuilder.append("<RefRequestCCTypeName>").append("интернет-приемная").append("</RefRequestCCTypeName>");

        stringBuilder.append("<Number>").append("</Number>");
        stringBuilder.append("<DateBegin>").append("</DateBegin>");

        stringBuilder.append("<Applicant>");
        stringBuilder.append("<LastName>").append(bidrequest.getCity()).append("</LastName>");
        stringBuilder.append("<FirstName>").append(bidrequest.getCity()).append("</FirstName>");
        stringBuilder.append("<MiddleName>").append(bidrequest.getCity()).append("</MiddleName>");
        stringBuilder.append("<Phone>").append(bidrequest.getPhone()).append("</Phone>");
        stringBuilder.append("</Applicant>");

        stringBuilder.append("<ApplicantAddress>");
        stringBuilder.append("<AddressId>").append("</AddressId>");
        stringBuilder.append("<Region>").append("Ульяновская область").append("</Region>");
        stringBuilder.append("<RegionType>").append("</RegionType>");
        stringBuilder.append("<City>").append(bidrequest.getCity()).append("</City>");
        stringBuilder.append("<CityType>").append("</CityType>");
        stringBuilder.append("<Locality>").append("</Locality>");
        stringBuilder.append("<LocalityType>").append("</LocalityType>");
        stringBuilder.append("<Street>").append(bidrequest.getStreet()).append("</Street>");
        stringBuilder.append("<StreetType>").append("</StreetType>");
        stringBuilder.append("<House>").append(bidrequest.getHouse()).append("</House>");
        stringBuilder.append("<Flat>").append("</Flat>");
        stringBuilder.append("</ApplicantAddress>");

        stringBuilder.append("<ShortContent>").append(bidrequest.getContent()).append("</ShortContent>");
        stringBuilder.append("<VisitDate>").append("</VisitDate>");

/*        <Documents>
        <Document>
        <Name>string</Name>
        <Data>base64Binary</Data>
        </Document>
        <Document>
        <Name>string</Name>
        <Data>base64Binary</Data>
        </Document>
        </Documents>
    */

        stringBuilder.append("</req>");
        stringBuilder.append("</SendReq>");
        stringBuilder.append(XML_TITLE_END);
        return stringBuilder.toString();
    }

    @Override
    public ArrayList getResponceObject(String xml) throws IOException, XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        String text = null;
        ArrayList<String> strings = new ArrayList<>();
        XmlPullParser parser = factory.newPullParser();
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
        return strings;

    }
}
