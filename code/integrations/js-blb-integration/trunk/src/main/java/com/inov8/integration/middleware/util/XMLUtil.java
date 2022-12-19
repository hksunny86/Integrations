package com.inov8.integration.middleware.util;

import com.inov8.integration.middleware.constants.EnumHelper;
import com.inov8.integration.middleware.exceptions.MappingException;
import com.inov8.integration.middleware.pdu.request.AccountOpeningRequest;
import com.inov8.integration.middleware.pdu.request.BalanceInquiryRequest;
import org.apache.commons.lang.reflect.FieldUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 9/26/2017.
 */
public class XMLUtil {


    public static <T> String convertToXML(Object obj) {
        String result;
        StringWriter sw = new StringWriter();
        JAXBContext jaxbContext = null;

        try {
            jaxbContext = JAXBContext.newInstance(obj.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            jaxbMarshaller.marshal(obj, sw);
            result = sw.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return result;
    }


    /**
     * Takes xml as {@code String} and mapping information as {@code Enum} .According to
     * these informations populates the {@code Object}
     *
     * @param object The {@code Object} to be populated
     * @param xml    The {@code String} to be parsed
     * @param values The {@code Array<Enum>} to extract mapping information
     * @return populated {@code Object}
     */


    public static Object populateFromResponse(Object object, String xml, EnumHelper[] values) {
        XPath xpath = XPathFactory.newInstance().newXPath();

        for (EnumHelper en : values) {
            try {
                if (en.isList())
                    FieldUtils.writeField(object, en.getName(), iterativeNodes(en.getListClass(), en.getPath(), xml, (EnumHelper[]) en.getListEnum().getEnumConstants()), true);
                else {

                    String value = ((String) xpath.evaluate(en.getPath(), new InputSource(new StringReader(xml)), XPathConstants.STRING)).trim();
                    FieldUtils.writeField(object, en.getName(), value, true);
                    System.err.append(en.getName() + " for path  " + en.getPath() + " with value " + value + "\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MappingException("Please Review your Mapping Information regarding field  " + en.getName());
            }
        }

        return object;
    }


    public static <T> List<T> iterativeNodes(Class<T> klass, String path, String xml, EnumHelper[] values) {

        List<T> list = new ArrayList<>();

        try {

            XPath xPath = XPathFactory.newInstance().newXPath();
            InputSource inputSource = new InputSource(new StringReader(xml));

            XPathExpression exp = xPath.compile(path);
            NodeList nl = (NodeList) exp.evaluate(inputSource, XPathConstants.NODESET);

            for (int i = 0; i < nl.getLength(); i++) {
                T iterative = klass.newInstance();
                NamedNodeMap node = nl.item(i).getAttributes();

                for (EnumHelper en : values) {
                    if (en.isList()) {

                        FieldUtils.writeField(iterative, en.getName(), iterativeNodes(en.getListClass(), path + "[" + (i + 1) + "]" + en.getPath(), xml, (EnumHelper[]) en.getListEnum().getEnumConstants()), true);
                    } else {
                        String val = node.getNamedItem(en.getPath()).getNodeValue().trim();
                        System.err.append("     " + en.getName() + "  value " + val + " for path  " + en.getPath() + "\n");
                        FieldUtils.writeField(iterative, en.getName(), val, true);
                    }
                }
                list.add(iterative);
            }


        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MappingException("Please Review your Mapping Information");
        }


        return list;
    }
    public static String convertRequest(Object source) {
        String result;
        StringWriter sw = new StringWriter();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(source.getClass());

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            jaxbMarshaller.marshal(source, sw);
            result = sw.toString();
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
            Node username = doc.getElementsByTagName("UserName").item(0);
            username.setTextContent("**********");
            Node password = doc.getElementsByTagName("Password").item(0);
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
