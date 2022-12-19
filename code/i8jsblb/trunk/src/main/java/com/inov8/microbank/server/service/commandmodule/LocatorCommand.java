package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.vo.RetailerContactDetailVO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

public class LocatorCommand extends BaseCommand {

    private Log logger = LogFactory.getLog(LocatorCommand.class);
    private String city;
    private String radius;
    private String locationType;
    private String latitude;
    private String longitude;
    private String retailer;

    private List<RetailerContactDetailVO> rcsvModels;
    private int locationTypeId;
    private String natureOfBussiness;
    private int totalRecods;
    private int pageNo;
	private int pageSize;

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
     //   natureOfBussiness = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CAT);
       // city = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CITY);
//        retailer = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MERCHANT_ID);
        radius = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_RADIUS);
        locationType = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_LOCATION_TYPE);
        latitude = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_LATITUDE);
        longitude = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_LONGITUDE);
        
        String pageNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PAGE_NO);
		String pageSize = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PAGE_SIZE);
		this.pageNo = StringUtil.isNullOrEmpty(pageNo) ? 1 : Integer.parseInt(pageNo);
		this.pageSize = StringUtil.isNullOrEmpty(pageSize) ? 1000 : Integer.parseInt(pageSize);
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        ValidatorWrapper.doRequired(this.radius, validationErrors, "Raduis");
        ValidatorWrapper.doNumeric(this.radius, validationErrors, "Raduis");
        ValidatorWrapper.doRequired(this.locationType, validationErrors, "Location Type");
        ValidatorWrapper.doNumeric(this.locationType, validationErrors, "Location Type");

        if (validationErrors.hasValidationErrors())
            return validationErrors;

        ValidatorWrapper.doRequired(this.latitude, validationErrors, "Latitude");
        ValidatorWrapper.doRequired(this.longitude, validationErrors, "Longitude");

        return validationErrors;
    }

    @Override
    public void doValidate() throws CommandException {

        validationErrors = new ValidationErrors();
        validate(validationErrors);
        if (validationErrors.hasValidationErrors()) {
            throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH, new Throwable());
        }
    }

    @Override
    public void execute() throws CommandException {

        try {
            // my business here
            double radiusD = Double.parseDouble(radius);
            double latitudeD = Double.parseDouble(latitude);
            double longitudeD = Double.parseDouble(longitude);
            locationTypeId = Integer.parseInt(locationType);

            if (LocatorConstants.BRANCHES == locationTypeId) { // branches
                /*BranchModel branchModel = new BranchModel();

                if (!StringUtil.isNullOrEmpty(city)) {
                    branchModel.setCityName(city);
                }

                this.branches = commonCommandManager.getBranchManager().loadBranchModel(branchModel);

                this.branches = LocatorUtil.getShortestDistanceByBranch(this.branches, radiusD, latitudeD, longitudeD);
*/
            }

            else if (LocatorConstants.ATM == locationTypeId) {// atms
               /* ATMModel atmModel = new ATMModel();

                if (!StringUtil.isNullOrEmpty(city)) {
                    atmModel.setCityName(city);
                }

                this.atms = commonCommandManager.getAtmManager().loadATMModel(atmModel);

                this.atms = LocatorUtil.getShortestDistanceByATM(this.atms, radiusD, latitudeD, longitudeD);*/
            }

            else if (LocatorConstants.AGENTS == locationTypeId) {
               Long retailerKey = !StringUtil.isNullOrEmpty(retailer) ? Long.parseLong(retailer) : null;
               Long catatoryKey = !StringUtil.isNullOrEmpty(natureOfBussiness) ? Long.parseLong(natureOfBussiness) : null;
				
                rcsvModels = null;
                BaseWrapper baseWrapper = LocatorUtil.filterMerchantsByDistance(LocatorCacheUtil.getRetailerModels(retailerKey, catatoryKey),
						radiusD, latitudeD, longitudeD, pageSize, pageNo);

				rcsvModels = (List<RetailerContactDetailVO>) baseWrapper.getObject(CommandFieldConstants.KEY_RETAILER_CONTACT_VO);

               // Collections.sort();
				totalRecods =  (Integer) baseWrapper.getObject(CommandFieldConstants.KEY_TOTAL_COUNT);
            }
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    @Override
    public String response() {

        String sb = null;

        if (LocatorConstants.ATM == locationTypeId) { // atm
           // sb = prepareATMLocationXml();
        }

        else if (LocatorConstants.BRANCHES == locationTypeId) { // branches
          //  sb = prepraeBranchLocationXml();
        }

        else if (LocatorConstants.AGENTS == locationTypeId) { // Agents
            sb = prepareMerchantLocationsXml();
        }

        else {
            sb = MiniXMLUtil.createErrorXml(ErrorCodes.INVALID_INPUT, ErrorLevel.HIGH.level(), "Invalid Locator Type");
        }

        return sb;
    }

    private String prepraeBranchLocationXml() {

        StringBuilder xml = new StringBuilder();
        /*xml.append(MiniXMLUtil.createXMLTag(XMLConstants.TAG_LOCATIONS, true));
        for (BranchModel branch : this.branches) {
            Map<String, Object> attributes = new LinkedHashMap<>();
            attributes.put(XMLConstants.ATTR_NAME_CAP_ID, branch.getName());
            attributes.put(XMLConstants.ATTR_DISTANCE_ID, String.valueOf(branch.getDistanceDisplacement()) + " KM");
            attributes.put(XMLConstants.ATTR_ADDRESS_ID, branch.getAddress());
            attributes.put(XMLConstants.ATTR_CONTACT_ID, branch.getContactNo() != null ? branch.getContactNo() : "");
            attributes.put(XMLConstants.ATTR_LATITUDE_ID, branch.getGeoLocationModel().getLatitude().toString());
            attributes.put(XMLConstants.ATTR_LONGITUDE_ID, branch.getGeoLocationModel().getLongitude().toString());

            xml.append(MiniXMLUtil.createXMLTag(XMLConstants.TAG_LOCATION, attributes));
        }
        xml.append(MiniXMLUtil.createXMLTag(XMLConstants.TAG_LOCATIONS, false));*/
        return xml.toString();
    }

    private String prepareATMLocationXml() {

        StringBuilder xml = new StringBuilder();
       /* xml.append(MiniXMLUtil.createXMLTag(XMLConstants.TAG_LOCATIONS, true));
        for (ATMModel atm : this.atms) {
            Map<String, Object> attributes = new LinkedHashMap<>();
            attributes.put(XMLConstants.ATTR_NAME_CAP_ID, atm.getName());
            attributes.put(XMLConstants.ATTR_DISTANCE_ID, String.valueOf(atm.getDistanceDisplacement()) + "KM");
            attributes.put(XMLConstants.ATTR_ADDRESS_ID, atm.getAddress());
            attributes.put(XMLConstants.ATTR_CONTACT_ID, "");
            attributes.put(XMLConstants.ATTR_LATITUDE_ID, atm.getGeoLocationModel().getLatitude().toString());
            attributes.put(XMLConstants.ATTR_LONGITUDE_ID, atm.getGeoLocationModel().getLongitude().toString());

            xml.append(MiniXMLUtil.createXMLTag(XMLConstants.TAG_LOCATION, attributes));

        }
        xml.append(MiniXMLUtil.createXMLTag(XMLConstants.TAG_LOCATIONS, false));*/
        return xml.toString();
    }

    private String prepareMerchantLocationsXml() {

        if(rcsvModels==null || rcsvModels.size()==0 ){
            String message="No nearby agent found in "+this.radius+" KM";
           return MiniXMLUtil.createMessageXML(message);
        }

        StringBuilder xml = new StringBuilder();
        xml.append("<");xml.append(XMLConstants.TAG_PARAMS);xml.append(">");
        xml.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TOTAL_COUNT, String.valueOf(totalRecods) ));
        xml.append("</");xml.append(XMLConstants.TAG_PARAMS);xml.append(">");
        xml.append(MiniXMLUtil.createXMLTag(XMLConstants.TAG_LOCATIONS, true));
        
        for (RetailerContactDetailVO model : this.rcsvModels) {
            Map<String, Object> attributes = new LinkedHashMap<>();
            attributes.put(XMLConstants.ATTR_NAME_CAP_ID, StringUtils.join(new String[]{model.getFirstName()," ", model.getLastName()}));
            attributes.put(XMLConstants.ATTR_DISTANCE_ID, String.valueOf(model.getDistanceDisplacement()) + " KM");
            attributes.put(XMLConstants.ATTR_LATITUDE_ID, model.getLatitude() != null ? model.getLatitude().toString() : "");
            attributes.put(XMLConstants.ATTR_LONGITUDE_ID, model.getLongitude() != null ? model.getLongitude().toString() : "");
            attributes.put(XMLConstants.ATTR_ADDRESS_ID, model.getAddress());
            attributes.put(XMLConstants.ATTR_CONTACT_ID, model.getContactNo());
            xml.append(MiniXMLUtil.createXMLTag(XMLConstants.TAG_LOCATION, attributes, true));
        }
        
        xml.append(MiniXMLUtil.createXMLTag(XMLConstants.TAG_LOCATIONS, false));
        return xml.toString();
   }

    /*private String prepareAdsXml(Long retailerId, Long adTypeId) {
        StringBuilder xml = new StringBuilder();
        xml.append(MiniXMLUtil.createXMLTag(XMLConstants.TAG_ADS, true));

        if (CollectionUtils.isEmpty(ads)) {
            return "";
        }

        for (AdsModel model : this.ads) {

            if (model.getRetailerId().longValue() != retailerId)
                continue;


            if (model.getAdTypeId().longValue() != adTypeId)
                continue;


            Map<String, String> attributes = new LinkedHashMap<>();

            attributes.put(XMLConstants.ATTR_NAME, model.getImageName());
            xml.append(MiniXMLUtil.createXMLTag(XMLConstants.TAG_AD, attributes));
        }

        xml.append(MiniXMLUtil.createXMLTag(XMLConstants.TAG_ADS, false));
        return xml.toString();
    }*/


}
