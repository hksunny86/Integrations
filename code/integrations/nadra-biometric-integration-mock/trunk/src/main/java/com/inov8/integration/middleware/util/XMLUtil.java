package com.inov8.integration.middleware.util;

import com.inov8.integration.middleware.nadra.pdu.BiometricVerification;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringReader;
import java.io.StringWriter;

public class XMLUtil {


    public static String convertResponse(Object source) {
        String result;
        StringWriter sw = new StringWriter();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BiometricVerification.class);

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            jaxbMarshaller.marshal(source, sw);
            result = sw.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return result;
    }


    public static BiometricVerification convertRequest(String source) {

        return JAXB.unmarshal(new StringReader(source), BiometricVerification.class);
    }
}
