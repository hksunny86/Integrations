/**
 * 
 */
package com.inov8.microbank.server.service.xml;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;

/**
 * @author NaseerUl
 *
 */
public class XmlMarshallerImpl<T> implements XmlMarshaller<T>
{

	private Marshaller marshaller;
	private Unmarshaller unmarshaller;

	@Override
	public String marshal(T object) throws XmlMappingException, IOException
	{
		final StringWriter out = new StringWriter();
        marshaller.marshal(object, new StreamResult(out));
        return out.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T unmarshal(String string) throws XmlMappingException, IOException
	{
		return (T) unmarshaller.unmarshal(new StreamSource(new StringReader(string)));
	}

	public void setMarshaller(Marshaller marshaller)
	{
		this.marshaller = marshaller;
	}

	public void setUnmarshaller(Unmarshaller unmarshaller)
	{
		this.unmarshaller = unmarshaller;
	}

}
