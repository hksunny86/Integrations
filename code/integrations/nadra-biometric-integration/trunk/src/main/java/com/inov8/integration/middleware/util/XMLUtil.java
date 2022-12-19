package com.inov8.integration.middleware.util;

import com.inov8.integration.middleware.nadra.pdu.BiometricVerification;
import com.inov8.integration.middleware.nadra.pdu.UserVerification;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;

import javax.xml.bind.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;

public class XMLUtil {


    public static String convertRequest(Object source) {
        String result;

        StringWriter sw = new StringWriter();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BiometricVerification.class);

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

            jaxbMarshaller.marshal(source, sw);
            result = sw.toString();


        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return result;
    }


    public static BiometricVerification convertResponse(String source) {
        return JAXB.unmarshal(new StringReader(source), BiometricVerification.class);
    }

    public static String maskPasswordAndFingerTempalte(String requestXML) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(requestXML));
            Document doc = db.parse(is);
            Node username = doc.getElementsByTagName("USERNAME").item(0);
            username.setTextContent("**********");
            Node password = doc.getElementsByTagName("PASSWORD").item(0);
            password.setTextContent("**********");
            Node fingerTemplate = doc.getElementsByTagName("FINGER_TEMPLATE").item(0);
            fingerTemplate.setTextContent("**********");

            // Converting document back to String
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            //transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            //transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            requestXML = sw.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return requestXML;
    }


    public static String maskPassword(String requestXML) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(requestXML));
            Document doc = db.parse(is);
            Node username = doc.getElementsByTagName("USERNAME").item(0);
            username.setTextContent("**********");
            Node password = doc.getElementsByTagName("PASSWORD").item(0);
            password.setTextContent("**********");
            // Converting document back to String
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            //transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            //transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            requestXML = sw.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return requestXML;
    }


}
