package com.inov8.microbank.webapp.action.allpayweb;

import com.inov8.microbank.webapp.action.allpayweb.formbean.Category;

import javax.xml.xpath.XPathExpressionException;
import java.util.List;

/**
 * Created by Yasir Shabbir on 9/7/2016.
 */
public interface ParserService {
    List<Category> parseCateogry(String xmlResponse) throws XPathExpressionException;

    boolean isPinChangeRequired(String responseXml, AllPayRequestWrapper requestWrapper) throws XPathExpressionException;


}
