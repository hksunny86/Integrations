package com.inov8.microbank.webapp.action.portal.partnergroupmodule;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.apache.commons.validator.GenericValidator;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.model.PartnerGroupModel;
import com.inov8.microbank.common.model.PartnerModel;
import com.inov8.microbank.common.model.portal.partnergroupmodule.UserPermissionWrapper;
import com.inov8.microbank.common.model.usergroupmodule.CustomUserPermissionViewModel;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.portal.partnergroupmodule.PartnerGroupManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

public class PartnerTypeFormRefDataController  extends AjaxController{

	
	private ReferenceDataManager referenceDataManager;
	private PartnerGroupManager partnerGroupManager;

	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		String actionType = ServletRequestUtils.getRequiredStringParameter(
				request, "actionType");
		AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();

		if ("1".equals(actionType)) {
			PartnerModel partnerModel = new PartnerModel();
			partnerModel.setAppUserTypeId(ServletRequestUtils.getRequiredLongParameter(request,"appUserTypeId"));
			partnerModel.setActive(true);
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(partnerModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(partnerModel);

			referenceDataManager.getReferenceData(referenceDataWrapper);
			List<PartnerModel> partnerModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) {
				partnerModelList = referenceDataWrapper.getReferenceDataList();
			}

			if (partnerModelList.size() == 0)
				ajaxXmlBuilder.addItemAsCData("", "");
			ajaxXmlBuilder.addItems(partnerModelList, "name","partnerId").toString();

		}
		
		else if ("2".equals(actionType)) {
			
			String app= ServletRequestUtils.getRequiredStringParameter(
					request, "appUserTypeId");
			Long userTypeId=Long.valueOf(app);
			Long partnerId = 0L;
			String idStr= ServletRequestUtils.getRequiredStringParameter(request, "id");
			if(!idStr.equals(""))
			{
				partnerId = Long.valueOf(idStr);
			}
			PartnerModel partnerModel = new PartnerModel();
			partnerModel.setAppUserTypeId(userTypeId);
			
			if(userTypeId.equals(UserTypeConstantsInterface.SUPPLIER))
			{
				partnerModel.setSupplierId(partnerId);
			
			}
			else if(userTypeId.equals(UserTypeConstantsInterface.BANK))
			{
				partnerModel.setBankId(partnerId);
			}
			
			else if(userTypeId.equals(UserTypeConstantsInterface.MNO))
			{
				partnerModel.setMnoId(partnerId);
			}
			else if(userTypeId.equals(UserTypeConstantsInterface.INOV8))
			{
				partnerModel.setOperatorId(partnerId);
						
			}
			
			else if(userTypeId.equals(UserTypeConstantsInterface.RETAILER))
			{
				partnerModel.setRetailerId(partnerId);
			}
			
			else if(userTypeId.equals(UserTypeConstantsInterface.DISTRIBUTOR))
			{
				partnerModel.setDistributerId(partnerId);
			}
			partnerModel.setActive(true);
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
					partnerModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(partnerModel);
			try {
			referenceDataManager.getReferenceData(referenceDataWrapper);
			} catch (FrameworkCheckedException ex1) {
				ex1.getStackTrace();
			}
			List<PartnerModel> partnerModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) 
			{
				partnerModelList = referenceDataWrapper.getReferenceDataList();
			}
			
			PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
			partnerGroupModel.setActive(true);
			
			if(partnerModelList!=null && partnerModelList.size()>0)
			{
				partnerGroupModel.setPartnerId(partnerModelList.get(0).getPartnerId());			
			}
			
			List <PartnerGroupModel>list= null;
			if(userTypeId.equals(UserTypeConstantsInterface.INOV8))
			{
				Boolean admin= false;	
				Collection <CustomUserPermissionViewModel>userPermissionList = new ArrayList <CustomUserPermissionViewModel>();
					userPermissionList=UserUtils.getCurrentUser().getUserPermissionList();
					Iterator<CustomUserPermissionViewModel> customUserPermissionIter = userPermissionList.iterator();
						while(customUserPermissionIter.hasNext())
							{
								CustomUserPermissionViewModel  customUserPermissionViewModel =customUserPermissionIter.next();
								if(customUserPermissionViewModel.getAuthority().startsWith("ADM_GP"))
								{
									admin =true;				
						
								}
								
								
							}
								
					
			list =partnerGroupManager.getPartnerGroups(partnerGroupModel,admin);
			}
			else
			{
				if(partnerGroupModel.getPartnerId() != null)
				{
					list =partnerGroupManager.getPartnerGroups(partnerGroupModel,true);
				}
				
			}
			
			List<PartnerGroupModel> partnerGroupModelList = null;
			if (list != null) 
			{
				partnerGroupModelList = list;
			}					

			if (partnerGroupModelList == null ||(partnerGroupModelList != null && partnerGroupModelList.size() == 0))
			{
				ajaxXmlBuilder.addItemAsCData("", "");
			}
			else
			{
				ajaxXmlBuilder.addItems(partnerGroupModelList, "name","partnerGroupId").toString();
			}

		}else if ("3".equals(actionType)) 
		{
			String partnerId = ServletRequestUtils.getRequiredStringParameter(
					request, "partnerId");
			
			List<UserPermissionWrapper> userPermissionWrapperList = this.partnerGroupManager.loadPartnerPermission(Long.valueOf(partnerId));
			
			Integer prevSectionId = 0;
			String disabled = "";
			StringBuilder tableHtml = new StringBuilder("<div class=\"eXtremeTable\" id=\"permissionList\">");
			String trClass = "odd";
			for(int i = 0; i < userPermissionWrapperList.size(); i++)
			{
				if(trClass.equals("odd"))
				{
					trClass = "even";
				}else if(trClass.equals("even"))
				{
					trClass = "odd";
				}
				UserPermissionWrapper up = userPermissionWrapperList.get(i);
				Integer currentSectionId = up.getUserPermissionSectionId();
				if( currentSectionId == null )
                {
				    currentSectionId = -1;
                } 

				if( prevSectionId.intValue() != currentSectionId.intValue() )
				{
				    if( prevSectionId.intValue() != 0 )
                    {
                        tableHtml.append( "</tbody></table></div>" );
                    }
                    tableHtml.append( "<h4>" );
                    if( GenericValidator.isBlankOrNull( up.getUserPermissionSectionName() ) )
                    {
                        tableHtml.append( "Misc." );
                    }
                    else
                    {
                        tableHtml.append( up.getUserPermissionSectionName() );
                    }
                    tableHtml.append( "</h4>" );

                    tableHtml.append( "<div>" );
                    tableHtml.append("<table id=\"permission_table\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"tableRegion\"  width=\"100%\" >" );
                    tableHtml.append("<thead>");
                    tableHtml.append("<tr>");
                    tableHtml.append("<td class=\"tableHeader\" id = \"permissionName\" >Permission Name</td>");
                    tableHtml.append("<td class=\"tableHeader\" id=\"create\" >Create</td>");
                    tableHtml.append("<td class=\"tableHeader\" id=\"read\" >Read</td>");
                    tableHtml.append("<td class=\"tableHeader\" id=\"update\" >Update</td>");
                    tableHtml.append("<td class=\"tableHeader\" id=\"delete\" >Delete</td>");
                    tableHtml.append("</tr>");
                    tableHtml.append("</thead>");
                    tableHtml.append("<tbody class=\"tableBody\" >");
				}

				tableHtml.append("<tr class=\""+trClass+"\"  onmouseover=\"this.className='highlight'\"  onmouseout=\"this.className='"+trClass+"'\" >");
				
//				tableHtml.append("<td>");
//				tableHtml.append("<input type=\"hidden\" name=\"userPermissionList["+i+"].permissionId\" id=\"userPermissionList["+i+"].permissionId\" value=\""+up.getPermissionId()+"\" />");
//				tableHtml.append(up.getPermissionName()+"	<input type=\"hidden\" name=\"userPermissionList["+i+"].permissionName\" value=\""+up.getPermissionName()+"\" />");
//				tableHtml.append("	<input type=\"hidden\" name=\"userPermissionList["+i+"].permissionShortName\" value=\""+up.getPermissionShortName()+"\" />");
//				tableHtml.append("</td>");

				tableHtml.append("<td>");
                tableHtml.append("<input type=\"hidden\" name=\"userPermissionList["+i+"].userPermissionSectionId\" value=\""+up.getUserPermissionSectionId()+"\" />");
                tableHtml.append("<input type=\"hidden\" name=\"userPermissionList["+i+"].userPermissionSectionName\" value=\""+up.getUserPermissionSectionName()+"\" />");
                tableHtml.append("<input type=\"hidden\" name=\"userPermissionList["+i+"].permissionId\" id=\"userPermissionList["+i+"].permissionId\" value=\""+up.getPermissionId()+"\" />");
                tableHtml.append("<input type=\"hidden\" name=\"userPermissionList["+i+"].permissionName\" value=\""+up.getPermissionName()+"\" />");
                tableHtml.append("<input type=\"hidden\" name=\"userPermissionList["+i+"].sequenceNo\" value=\""+up.getSequenceNo()+"\" />");
                tableHtml.append(up.getPermissionName());
                tableHtml.append("</td>");

				
				tableHtml.append("<td style=\"width:15px\" align=\"center\" class=\"table-autosort\">");
				tableHtml.append("<input type=\"hidden\"	name=\"_userPermissionList["+i+"].createAllowed\" />");
				tableHtml.append("<input type=\"checkbox\" class=\"perm_chkbox_"+up.getUserPermissionSectionName()+"\" name=\"userPermissionList["+i+"].createAllowed\" id=\"userPermissionList["+i+"].createAllowed\" value=\"true\" ");
				disabled = "";
				if(!up.isCreateAvailable())
				{
					disabled = "disabled";
				}
				tableHtml.append(" "+disabled+" onclick=\"adjust(this);\" />");
				tableHtml.append("<input type=\"hidden\" name=\"userPermissionList["+i+"].createAvailable\" value=\""+up.isCreateAvailable()+"\" />");
				tableHtml.append("</td>");
				
				tableHtml.append("<td style=\"width:15px\" align=\"center\" class=\"table-autosort\">");
				tableHtml.append("<input type=\"hidden\"	name=\"_userPermissionList["+i+"].readAllowed\" />");
				tableHtml.append("<input type=\"checkbox\" class=\"perm_chkbox_"+up.getUserPermissionSectionName()+"\" name=\"userPermissionList["+i+"].readAllowed\" id=\"userPermissionList["+i+"].readAllowed\"	value=\"true\" ");
				disabled = "";
				if(!up.isReadAvailable())
				{
					disabled = "disabled";
				}
				tableHtml.append(" "+disabled+" onclick=\"adjust(this);\" />");
				tableHtml.append("<input type=\"hidden\" name=\"userPermissionList["+i+"].readAvailable\" value=\""+up.isReadAvailable()+"\" />");
				tableHtml.append("</td>");
				
				tableHtml.append("<td style=\"width:15px\" align=\"center\" class=\"table-autosort\">");
				tableHtml.append("<input type=\"hidden\"	name=\"_userPermissionList["+i+"].updateAllowed\" />");
				tableHtml.append("<input type=\"checkbox\" class=\"perm_chkbox_"+up.getUserPermissionSectionName()+"\" name=\"userPermissionList["+i+"].updateAllowed\" id=\"userPermissionList["+i+"].updateAllowed\"	value=\"true\" ");
				disabled = "";
				if(!up.isUpdateAvailable())
				{
					disabled = "disabled";
				}
				tableHtml.append(" "+disabled+" onclick=\"adjust(this);\" />");
				tableHtml.append("<input type=\"hidden\" name=\"userPermissionList["+i+"].updateAvailable\" value=\""+up.isUpdateAvailable()+"\" />");
				tableHtml.append("</td>");
				
				tableHtml.append("<td style=\"width:15px\" align=\"center\" class=\"table-autosort\">");
				tableHtml.append("<input type=\"hidden\"	name=\"_userPermissionList["+i+"].deleteAllowed\" />");
				tableHtml.append("<input type=\"checkbox\" class=\"perm_chkbox_"+up.getUserPermissionSectionName()+"\" name=\"userPermissionList["+i+"].deleteAllowed\" id=\"userPermissionList["+i+"].deleteAllowed\"	value=\"true\" ");
				disabled = "";
				if(!up.isDeleteAvailable())
				{
					disabled = "disabled";
				}
				tableHtml.append(" "+" "+disabled+" onclick=\"adjust(this);\" />");
				tableHtml.append("<input type=\"hidden\" name=\"userPermissionList["+i+"].deleteAvailable\" value=\""+up.isDeleteAvailable()+"\" />");
				tableHtml.append("</td>");
				
				tableHtml.append("</tr>");
				prevSectionId = currentSectionId;
			}

			if( userPermissionWrapperList != null && !userPermissionWrapperList.isEmpty() )
            {
			    tableHtml.append( "</tbody></table></div>" );
            }

			tableHtml.append( "</div>" );//end starting div with id permissionList
			return tableHtml.toString();
		}
		
		
		else if ("5".equals(actionType)) 
		{
			String btnName = "";
			String status = "de-activated";
			String errorMessage = "";
			String expMsg = "";
			PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
			BaseWrapper baseWrapper = new  BaseWrapperImpl();
			Long id = ServletRequestUtils.getRequiredLongParameter(
					request, "id");
			
			btnName = ServletRequestUtils.getStringParameter(request, "btn");
			partnerGroupModel.setPartnerGroupId(id);
			baseWrapper.setBasePersistableModel(partnerGroupModel);
			try
			{
				baseWrapper=partnerGroupManager.activateDeactivatePartnerGroup(baseWrapper);
			}catch(FrameworkCheckedException fce)
			{
				status = "re-activated";
				ajaxXmlBuilder.addItem(btnName, "DeActivate");				
				errorMessage = getMessage(request, "partnerGroupModule.partnerGroupCantBeDeactive", new String[] { status });
				ajaxXmlBuilder.addItem("message", errorMessage);
				return ajaxXmlBuilder.toString();
			}
			Boolean isActive = (Boolean)baseWrapper.getObject(MfsAccountManager.KEY_IS_ACTIVE);
			
			if (isActive.booleanValue())
			{
				status = "re-activated";
				ajaxXmlBuilder.addItem(btnName, "DeActivate");
				
				errorMessage = getMessage(request, "partnerGroupModel.deactivate", new String[] { status });
			}
			else
			{
				status = "de-activated";
				ajaxXmlBuilder.addItem(btnName, "Activate");
				errorMessage = getMessage(request, "partnerGroupModel.activate", new String[] { status });
			}
			
			
			ajaxXmlBuilder.addItem("message", errorMessage);
		}
		
		
		else {
			ajaxXmlBuilder.addItemAsCData("", "");
		}

		return ajaxXmlBuilder.toString();

	}

	public void setPartnerGroupManager(PartnerGroupManager partnerGroupManager) {
		this.partnerGroupManager = partnerGroupManager;
	}

}
