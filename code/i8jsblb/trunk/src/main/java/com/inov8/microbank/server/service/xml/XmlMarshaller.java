/**
 * 
 */
package com.inov8.microbank.server.service.xml;

import java.io.IOException;

import org.springframework.oxm.XmlMappingException;


/**
 * @author NaseerUl
 *
 */
public interface XmlMarshaller<T extends Object>
{
	String marshal(T object) throws XmlMappingException, IOException;

    T unmarshal(String string) throws XmlMappingException, IOException;
}
