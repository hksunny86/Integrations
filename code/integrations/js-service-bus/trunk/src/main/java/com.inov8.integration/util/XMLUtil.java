package com.inov8.integration.util;

//import com.inov8.integration.channel.rdv.mb.request.PanPinVerificationRequest;
import com.thoughtworks.xstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;


import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;

public class XMLUtil {
    private static Logger logger = LoggerFactory.getLogger(XMLUtil.class.getSimpleName());

    public static Object converXMLtoObj(String xml, Object obj) {
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            StringReader reader = new StringReader(xml);
            obj = unmarshaller.unmarshal(reader);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return obj;
    }

    public static String convertToXMLWithoutXMLTag(Object obj) {
        String result;
        StringWriter sw = new StringWriter();
        try {
            JAXBContext jc = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            marshaller.marshal(obj, sw);
            result = sw.toString();

        } catch (JAXBException e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
        return result;
    }

    public static String convertToXML(Object obj) {
        String result;
        StringWriter sw = new StringWriter();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            jaxbMarshaller.marshal(obj, sw);
            result = sw.toString();
          result =   result.replace(" standalone=\"yes\"","");
        } catch (JAXBException e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }

        return result;
    }

    public static String convertToXstreamXML(Object obj) {
        String result;
        try {
            XStream xStream = new XStream();
            xStream.autodetectAnnotations(true);
            String xml = xStream.toXML(obj);
            result = xml;
        } catch (Exception e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }

        return result;
    }

    public static Document convertToDoc(String requestXML) {
        Document doc;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(requestXML));
            doc = db.parse(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return doc;
    }
    public static String convertRequest(Object source) {
        String result;
        StringWriter sw = new StringWriter();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(source.getClass());

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            jaxbMarshaller.marshal(new JAXBElement<Object>(new QName("uri","local"), Object.class, source), sw);
            result = sw.toString();
            result.replace(" standalone=\"yes\"","");
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
    public static String maskPassword(String requestXML) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(requestXML));
            Document doc = db.parse(is);
            Node username = doc.getElementsByTagName("CNIC").item(0);
            username.setTextContent("**********");
            Node password = doc.getElementsByTagName("MobileNo").item(0);
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

    public static void main(String[] args) {
//
//        PanPinVerificationRequest panPinVerificationRequest = new PanPinVerificationRequest();
//        panPinVerificationRequest.setCNIC("35487777887878 888");
//        panPinVerificationRequest.setDateOfBirth("someBirth");
//        String output = convertToXML(panPinVerificationRequest);
//        System.out.println(output);
    }
}
