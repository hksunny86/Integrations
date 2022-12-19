package com.inov8.microbank.webapp.action.ajax;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.apache.commons.validator.GenericValidator;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.TaxRegimeModel;
import com.inov8.microbank.server.dao.portal.taxregimemodule.TaxRegimeDAO;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Feb 17, 2013 11:57:20 AM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class LoadFEDAjaxController extends AjaxController
{
    //Autowired
	TaxRegimeDAO taxRegimeDAO;
	
    @SuppressWarnings( "unchecked" )
    @Override
    public String getResponseContent( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        if( logger.isDebugEnabled() )
        {
            logger.debug("Start of getResponseContent of LoadFEDAjaxController");
        }
        AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
        String taxRegimeIdStr = request.getParameter( "taxRegimeId" );
        if( !GenericValidator.isBlankOrNull( taxRegimeIdStr ) )
        {
            long taxRegimeId = Long.parseLong( taxRegimeIdStr );
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            TaxRegimeModel taxRegimeModel = new TaxRegimeModel();
            taxRegimeModel = taxRegimeDAO.findByPrimaryKey(taxRegimeId);
            String fed = taxRegimeModel.getFed().toString();          
            ajaxXmlBuilder.addItem("fed",fed);
        }

        if( logger.isDebugEnabled() )
        {
            logger.debug("End of getResponseContent of LoadFEDAjaxController");
        }
        return ajaxXmlBuilder.toString();
    }

	public void setTaxRegimeDAO(TaxRegimeDAO taxRegimeDAO) {
		if (taxRegimeDAO != null) {
			this.taxRegimeDAO = taxRegimeDAO;
		}
	}
}
