package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.dao.geolocationmodule.GeoLocationDAO;
import com.inov8.microbank.server.dao.retailermodule.RetailerContactDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class BISPAgentVerificationCommand extends BaseCommand {
    private Log logger = LogFactory.getLog(BISPAgentVerificationCommand.class);
    private String latitude;
    private String longitude;
    private RetailerContactDAO retailerContactDAO;
    private AppUserModel appUserModel;
    private UserDeviceAccountsModel userDeviceAccountsModel;
    WorkFlowWrapper workFlowWrapper;
    private double radius= Double.parseDouble(MessageUtil.getMessage("BISP.Agent.Radius"));

    String Is_Location= String.valueOf(1);
    private String isBVSRequired= String.valueOf(1);



    @Override
    public void prepare(BaseWrapper baseWrapper) {
        userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
        appUserModel = ThreadLocalAppUser.getAppUserModel();
        latitude = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_LATITUDE);
        longitude = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_LONGITUDE);
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        ValidatorWrapper.doRequired(this.latitude, validationErrors, "Latitude");
        ValidatorWrapper.doRequired(this.longitude, validationErrors, "Longitude");
        if (validationErrors.hasValidationErrors())
            return validationErrors;
        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
         double latitudeD = Double.parseDouble(latitude);
        double longitudeD = Double.parseDouble(longitude);
        double currentLatitude = 0;
        double currentLongitude= 0;

        workFlowWrapper = new WorkFlowWrapperImpl();
        workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);
        RetailerContactModel retailerContactModel = new RetailerContactModel();
        retailerContactModel.setRetailerContactId(appUserModel.getRetailerContactId());

        BaseWrapper bWrapper = new BaseWrapperImpl();
        GeoLocationModel geoLocationModel = new GeoLocationModel();
        bWrapper.setBasePersistableModel(retailerContactModel);
        try {
            bWrapper = commonCommandManager.loadRetailerContact(bWrapper);
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
        }
        retailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();

        if(retailerContactModel.getGeoLocationId()!=null){
            geoLocationModel = commonCommandManager.getGeoLocationDao().findByPrimaryKey(retailerContactModel.getGeoLocationId());
        }
        if(geoLocationModel == null || (geoLocationModel != null && (geoLocationModel.getLatitude() == null || geoLocationModel.getLongitude() == null)))
            throw new CommandException("Agent Location is not configured.Please contact your service provider.", ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,null);
        currentLatitude = geoLocationModel.getLatitude();
        currentLongitude= geoLocationModel.getLongitude();
        double currentDistance=LocatorUtil.BISPCalculateDistance(currentLatitude,currentLongitude,latitudeD,longitudeD);
        //Saving agent location validation in table for verification in further transactions
        //Converting distance from miles to meters , as radius property is defined in meters
        if (currentDistance * 1.6* 1000 <= radius)
        this.updateAgentLocationValidation(1L);
        else
            this.updateAgentLocationValidation(0L);
        if (currentDistance <= radius) {
            Is_Location= String.valueOf(1);


            Long result = 0L;
            try {
                result = commonCommandManager.getBispCustNadraVerificationDAO().isBVSSuccessful(null,
                        userDeviceAccountsModel.getUserId(),null,1L,UserTypeConstantsInterface.RETAILER);
            } catch (FrameworkCheckedException e) {
                e.printStackTrace();
                result = 0L;
            }
            if(result >=1)
            {
                isBVSRequired= String.valueOf(0);
            }
            /*else
            {
                try
                {
                    result = commonCommandManager.getBispCustNadraVerificationDAO().isBVSSuccessful(null,
                            userDeviceAccountsModel.getUserId(),null,0L,UserTypeConstantsInterface.RETAILER);
                } catch (FrameworkCheckedException e) {
                    e.printStackTrace();
                    result = 16L;
                }
                if(result >= 16)
                {
                    isBVSRequired= String.valueOf(1);
                    throw new CommandException("Your Daily BVS Limit Exceeded.", ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,null);
                }
            }*/
        }
        else
        {
            Is_Location= String.valueOf(0);
            isBVSRequired= String.valueOf(0);

        }
    }

    private void updateAgentLocationValidation(long longValidated) {
        AgentLocationStatModel agentLocationStatModel = new AgentLocationStatModel();
        agentLocationStatModel.setAgentId(Long.parseLong(userDeviceAccountsModel.getUserId()));
        try {
            agentLocationStatModel = getCommonCommandManager().getAgentLocationStatManager().getAgentLocationStat(agentLocationStatModel);
            if(agentLocationStatModel!=null){
                agentLocationStatModel.setIsValidLocation(longValidated);
                agentLocationStatModel.setLastUpdate(new Date());
                this.getCommonCommandManager().getAgentLocationStatManager().saveOrUpdate(agentLocationStatModel);
            }
            else{
                AgentLocationStatModel agentLocationStatModel1 = new AgentLocationStatModel();
                agentLocationStatModel1.setAgentId(Long.parseLong(userDeviceAccountsModel.getUserId()));
                agentLocationStatModel1.setIsValidLocation(longValidated);
                agentLocationStatModel1.setLastUpdate(new Date());
                this.getCommonCommandManager().getAgentLocationStatManager().saveOrUpdate(agentLocationStatModel1);
            }



/*        agentLocationStatModel.setIsValidLocation(longValidated);
        agentLocationStatModel.setLastUpdate(new Date());

            this.getCommonCommandManager().getAgentLocationStatManager().saveOrUpdate(agentLocationStatModel);*/
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String response() {

//        params.add(new LabelValueBean(CommandFieldConstants.KEY_LATITUDE, latitude));
//        params.add(new LabelValueBean(CommandFieldConstants.KEY_LONGITUDE, longitude));
//        params.add(new LabelValueBean(CommandFieldConstants.KEY_IS_BVS_REQUIRED, isBVSRequired/*(String)workflowWrapper.getObject(CommandFieldConstants.IS_SENDER_BVS_REQUIRED)*/));
//        params.add(new LabelValueBean(CommandFieldConstants.KEY_LOCATION_TYPE))
//
        StringBuilder xml = new StringBuilder();
        xml.append("<");xml.append(XMLConstants.TAG_PARAMS);xml.append(">");
        xml.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_LATITUDE,latitude));
        xml.append("</");xml.append(XMLConstants.TAG_PARAMS);xml.append(">");
        xml.append("<");xml.append(XMLConstants.TAG_PARAMS);xml.append(">");
        xml.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_LONGITUDE,longitude));
        xml.append("</");xml.append(XMLConstants.TAG_PARAMS);xml.append(">");
        xml.append("<");xml.append(XMLConstants.TAG_PARAMS);xml.append(">");
        xml.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_IS_BVS_REQUIRED,isBVSRequired));
        xml.append("</");xml.append(XMLConstants.TAG_PARAMS);xml.append(">");
        xml.append("<");xml.append(XMLConstants.TAG_PARAMS);xml.append(">");
        xml.append(MiniXMLUtil.createXMLParameterTag("IS_VALID_LOCATION", Is_Location));
        xml.append("</");xml.append(XMLConstants.TAG_PARAMS);xml.append(">");
//        params.add(new LabelValueBean(ATTR_IS_BVS_REQUIRED,isBVSRequired));
//        params.add(new LabelValueBean(TAG_LOCATION,Boolean.toString(Is_Location)));

        return xml.toString();


    }
}
