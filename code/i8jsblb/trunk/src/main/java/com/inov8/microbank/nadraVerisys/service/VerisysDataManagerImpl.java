package com.inov8.microbank.nadraVerisys.service;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.integration.middleware.controller.TransliterationController;
import com.inov8.integration.middleware.translitration.pdu.TransliterationVo;
import com.inov8.integration.vo.NadraIntegrationVO;
import com.inov8.microbank.common.util.AllPayWebUtil;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.fonepay.dao.hibernate.VirtualCardHibernateDAO;
import com.inov8.microbank.fonepay.vo.NADRADataVO;
import com.inov8.microbank.nadraVerisys.dao.VerisysDataDAO;
import com.inov8.microbank.nadraVerisys.model.VerisysDataModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by AtieqRe on 11/8/2016.
 */
public class VerisysDataManagerImpl implements VerisysDataManager {

    private final static Log logger = LogFactory.getLog(VerisysDataManagerImpl.class);
    private VerisysDataDAO verisysDataDAO;
    private VirtualCardHibernateDAO virtualCardHibernateDAO;

    @Override
    public VerisysDataModel saveOrUpdate(VerisysDataModel verisysDataModel) {
        return verisysDataDAO.saveOrUpdate(verisysDataModel);
    }

    @Override
    public VerisysDataModel loadVerisysDataModel(String cnic) {
        return verisysDataDAO.loadVerisysDataModel(cnic);
    }

    @Override
    public VerisysDataModel loadVerisysDataModel(Long appUserId) {
        return verisysDataDAO.loadVerisysDataModel(appUserId);
    }

    @Override
    public void markClosedByAppUserId(Long appUserId) throws FrameworkCheckedException
    {
        this.verisysDataDAO.markClosedByAppUserId(appUserId);
    }

    @Override
    public VerisysDataModel prepareVerisysDataModelByNadraIntegrationVO(NadraIntegrationVO nadraIntegrationVO){
        logger.info(">>>>>>>>>>> VerisysDataManagerImpl.prepareVerisysDataModelByNadraIntegrationVO() <<<<<<<<<<<<<");
        VerisysDataModel verisysDataModel = loadVerisysDataModel(nadraIntegrationVO.getCitizenNumber());
        if(null == verisysDataModel){
            verisysDataModel = new VerisysDataModel();
        }

        DateFormat df = new SimpleDateFormat(PortalDateUtils.SHORT_DATE_FORMAT_2);
        Date dob=null;
        try {
            dob = df.parse(nadraIntegrationVO.getDateOfBirth());
        }catch(Exception e){
            logger.error(e);
        }

        verisysDataModel.setName(nadraIntegrationVO.getFullName());
        verisysDataModel.setPlaceOfBirth(nadraIntegrationVO.getBirthPlace());
        verisysDataModel.setCnic(nadraIntegrationVO.getCitizenNumber());
        verisysDataModel.setCurrentAddress(nadraIntegrationVO.getPresentAddress());
        verisysDataModel.setPermanentAddress(nadraIntegrationVO.getPresentAddress());
        verisysDataModel.setMotherMaidenName(nadraIntegrationVO.getMotherName());
        verisysDataModel.setTranslated(Boolean.FALSE);
        return verisysDataModel;
    }

    @Override
    public List<VerisysDataModel> getVerisysDataForTransliteration() throws FrameworkCheckedException

    {
        return this.verisysDataDAO.getUrduDataForTransliteration();
    }

    @Override
    public List<TransliterationVo> translateUrduToEnglish(List<VerisysDataModel> vdmList) throws Exception {
        List<TransliterationVo> translatedVOs = new ArrayList<TransliterationVo>();
        TransliterationVo tVo;
        TransliterationController transliterationController = AllPayWebUtil.getTransliterationController();
        for(VerisysDataModel vdm : vdmList)
        {
            tVo = new TransliterationVo();
            tVo.setRequestParameter1(vdm.getName());
            tVo.setRequestParameter2(vdm.getMotherMaidenName());
            tVo.setRequestParameter3(vdm.getCurrentAddress());
            tVo.setRequestParameter4(vdm.getPermanentAddress());
            tVo.setRequestParameter5(vdm.getPlaceOfBirth());
            tVo = transliterationController.transliteration(tVo);
            if(tVo.getResponseCode() != null && tVo.getResponseCode().equals("100"))
            {
                Long appUserId=vdm.getAppUserId();
                Long customerId=this.verisysDataDAO.getCustomer(appUserId);
                Long addressId=this.verisysDataDAO.getAddressId(customerId);
                Long accountHolderId=this.verisysDataDAO.getAcoountHolderId(vdm.getCnic());
                Long accountInfo=this.verisysDataDAO.getAccountInfoId(customerId);
                tVo.setResponseParameter6(String.valueOf(addressId));
                tVo.setResponseParameter7(String.valueOf(customerId));
                tVo.setResponseParameter9(String.valueOf(accountHolderId));
                tVo.setResponseParameter10(String.valueOf(accountInfo));
                vdm.setTranslated(Boolean.TRUE);//translation is done; no need to treat it again when scheduler gets execute.
                tVo.setResponseParameter8(vdm.getAppUserId().toString());
                tVo.setCnic(vdm.getCnic());
                translatedVOs.add(tVo);
            }
            else{
                logger.info(">>>>>>> Error while translating from Urdu to English for cnic: " + vdm.getCnic() + "; response code received: " + tVo.getResponseCode() + " <<<<<<<<");
            }
        }


        return translatedVOs;
    }

    @Override
    public void updateTranslatedData(List<TransliterationVo> transliterationVoList) throws Exception {
        verisysDataDAO.initializeBatch();

        for(TransliterationVo vo :transliterationVoList) {
            String appUserId = vo.getResponseParameter8(); //mobileNo
            String addressId=vo.getResponseParameter6();
            String customerId = vo.getResponseParameter7(); //name
            String accountHolderId = vo.getResponseParameter9(); //name
            String accountInfoId = vo.getResponseParameter10(); //name
            String fullName = vo.getResponseParameter1(); //name
            String firstName = fullName == null ? "-" : fullName.split(" ")[0];
            String lastName = ""; //(fullName == null ? "" : fullName.split(" ").length > 1 ? fullName.split(" ")[1] : firstName) ;
            String secondNames[] =   fullName.split(" ");

            for(int i=1; i<secondNames.length; i++)
                lastName = lastName.concat(secondNames[i]).concat(" ");

            lastName = lastName.trim();
            if(StringUtil.isNullOrEmpty(lastName))
                lastName = firstName;

            String currentAddress = vo.getResponseParameter3(); //current address
            String permanentAddress = vo.getResponseParameter4(); //permanent address
            String motherMaidenName = vo.getResponseParameter2(); //mother maiden name
            String birthPlace = vo.getResponseParameter5(); // birthplace

            Object[] auObj = {appUserId, firstName, lastName, motherMaidenName};
            Object[] cuObj = {customerId, firstName + lastName, birthPlace};
            Object[] caObj = {addressId, currentAddress, permanentAddress};
            Object[] aiObj = {accountInfoId, firstName, lastName};
            Object[] ahObj = {accountHolderId, firstName, lastName};

            Object[] ntObj = {appUserId, firstName + lastName, currentAddress, permanentAddress, birthPlace, motherMaidenName};

            Map<String, Object[]> map = new HashMap<>(6);
            map.put("AU", auObj);
            map.put("CU", cuObj);
            map.put("CA", caObj);
            map.put("AI", aiObj);
            map.put("AH", ahObj);
            map.put("TD",ntObj);
            verisysDataDAO.addToBatch(map);
        }

        verisysDataDAO.closeBatch();
    }


    @Override
    public void saveNadraData(VerisysDataModel vModel, NADRADataVO nadraVO) throws Exception{
        this.verisysDataDAO.saveOrUpdate(vModel);
        this.virtualCardHibernateDAO.updateRegData(nadraVO);

    }


    public void setVerisysDataDAO(VerisysDataDAO verisysDataDAO) {
        this.verisysDataDAO = verisysDataDAO;
    }
    public void setVirtualCardHibernateDAO(VirtualCardHibernateDAO virtualCardHibernateDAO) {
        this.virtualCardHibernateDAO = virtualCardHibernateDAO;
    }
}
