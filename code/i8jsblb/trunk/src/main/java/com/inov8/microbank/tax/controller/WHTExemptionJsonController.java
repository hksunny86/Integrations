package com.inov8.microbank.tax.controller;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.server.facade.taxregimemodule.TaxRegimeFacade;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.tax.model.WHTExemptionModel;
import com.inov8.microbank.tax.vo.WHTExemptionVO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Zeeshan on 6/29/2016.
 */
@Controller
public class WHTExemptionJsonController {

    private final static Log logger = LogFactory.getLog(WHTExemptionJsonController.class);

    @Autowired
    private CommonCommandManager commonCommandManager;

    @Autowired
    private TaxRegimeFacade taxRegimeFacade;


    @RequestMapping(value = "/p_loadWhtExemptionmapping", method = RequestMethod.GET)
    public @ResponseBody List<WHTExemptionVO> LoadWhtExemptionmapping (@RequestParam Long userId)
    {
        Long startTime = System.currentTimeMillis();
        List<WHTExemptionModel> whtExemptionModelList = null;
        List<WHTExemptionVO> whtExemptionVOList = null;
        try
        {
            UserDeviceAccountsModel udaModel=commonCommandManager.loadUserDeviceAccountByUserId(userId.toString());

            if(udaModel!=null) {
                whtExemptionModelList = taxRegimeFacade.loadWHTExemptionByAppUserId(udaModel.getAppUserId());
                whtExemptionVOList = extractWhtExemptionVoList(whtExemptionModelList, udaModel);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        Long endTime = System.currentTimeMillis();
        logger.info("p_loadWhtExemptionmapping - loading WHT Exemption for selcted criteria. Time elapsed: " + ( TimeUnit.MILLISECONDS.toSeconds( (endTime - startTime)) ) );

        return whtExemptionVOList;
    }

    public List<WHTExemptionVO> extractWhtExemptionVoList(List<WHTExemptionModel> whtExemptionModelList , UserDeviceAccountsModel udaModel) throws Exception{
            List<WHTExemptionVO> whtExemptionVOList = new ArrayList<WHTExemptionVO>(whtExemptionModelList.size());

            for(WHTExemptionModel whtExemptionModel :whtExemptionModelList)
            {
                WHTExemptionVO whtExemptionVO=new WHTExemptionVO();
                whtExemptionVO.setUserId(udaModel.getUserId());
                whtExemptionVO.setStartDate(whtExemptionModel.getStartDate());
                whtExemptionVO.setEndDate(whtExemptionModel.getEndDate());
                
                SimpleDateFormat sdf  = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false);
                
                whtExemptionVO.setStartDateStr(sdf.format(whtExemptionModel.getStartDate()));
                whtExemptionVO.setEndDateStr(sdf.format(whtExemptionModel.getEndDate()));
                
                whtExemptionVO.setActive(whtExemptionModel.getActive());
                whtExemptionVO.setWhtExemptionId(whtExemptionModel.getWhtExemptionId());


               if(whtExemptionModel.getEndDate().after(new Date())){
                   whtExemptionVO.setEditAllowed(true);
               }
                else{
                   whtExemptionVO.setEditAllowed(false);
               }
                AppUserModel appUserModel = new AppUserModel();
                BaseWrapper baseWrapper = new BaseWrapperImpl();
                appUserModel.setAppUserId(udaModel.getAppUserId());
                baseWrapper.setBasePersistableModel(appUserModel);
                baseWrapper= commonCommandManager.loadAppUser(baseWrapper);
                appUserModel=(AppUserModel) baseWrapper.getBasePersistableModel();

                whtExemptionVO.setAgentName(appUserModel.getFullName());
                whtExemptionVO.setAgentCnic(appUserModel.getNic());
                whtExemptionVO.setMobile(appUserModel.getMobileNo());


                whtExemptionVOList.add(whtExemptionVO);
            }
            return whtExemptionVOList;

    }

}
