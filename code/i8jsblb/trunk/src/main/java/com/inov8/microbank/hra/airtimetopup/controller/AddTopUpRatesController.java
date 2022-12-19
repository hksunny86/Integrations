package com.inov8.microbank.hra.airtimetopup.controller;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.hra.airtimetopup.model.ConversionRateModel;
import com.inov8.microbank.hra.service.HRAManager;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AddTopUpRatesController extends AdvanceFormController {

    private HRAManager hraManager;

    public AddTopUpRatesController() {
        setCommandName("conversionRateModel");
        setCommandClass(ConversionRateModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        return null;
    }

    @Override
    protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception {
        return new ConversionRateModel();
    }

    @Override
    protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {
        ConversionRateModel conversionRateModel = (ConversionRateModel) o;

        ConversionRateModel dollarConversionRate = new ConversionRateModel();
        ConversionRateModel topUpRate = new ConversionRateModel();

        dollarConversionRate.setRateTypeId(Long.valueOf(1));
        dollarConversionRate.setRate(conversionRateModel.getDollarRate());
        dollarConversionRate.setStartDate(new Timestamp(System.currentTimeMillis()));
        dollarConversionRate.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
        //dollarConversionRate.setVersion(conversionRateModel.getVersion());
        dollarConversionRate.setUpdatedOn(new Date());
        dollarConversionRate.setCreatedOn(new Date());

        topUpRate.setRateTypeId(Long.valueOf(2));
        topUpRate.setRate(conversionRateModel.getRate());
        topUpRate.setStartDate(new Timestamp(System.currentTimeMillis()));
        topUpRate.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
        topUpRate.setCreatedOn(new Date());
        topUpRate.setUpdatedOn(new Date());
        //topUpRate.setVersion(conversionRateModel.getVersion());

        ConversionRateModel openModel = new ConversionRateModel();
        openModel.setEndDate(null);
//        openModel.setRateTypeId(conversionRateModel.getRateTypeId());
        List<ConversionRateModel> list = hraManager.findAllOpenConversionRates(openModel);
        if(list!=null && !list.isEmpty())
        {
            for(ConversionRateModel model:list)
            {
                model.setEndDate(new Timestamp(System.currentTimeMillis()));
                model.setUpdatedOn(new Date());
            }
            hraManager.saveOrUpdateOpenConversionRates(list);
        }

        List<ConversionRateModel> list1 = new ArrayList<>();
        list1.add(dollarConversionRate);
        list1.add(topUpRate);

        hraManager.saveOrUpdateCollection(list1);
//
//        conversionRateModel.setStartDate(new Timestamp(System.currentTimeMillis()));
//        conversionRateModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
//        conversionRateModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
//        conversionRateModel.setCreatedOn(new Date());
//        conversionRateModel.setUpdatedOn(new Date());
//        hraManager.saveOrUpdateConversionRateModel(conversionRateModel);
        ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
        super.saveMessage(httpServletRequest, "Record saved successfully.");

//         return new ModelAndView(getSuccessView(),"conversionRateList",list);
        return new ModelAndView("redirect:conversionRate.html");

    }

    @Override
    protected ModelAndView onUpdate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {
        return null;
    }

    public void setHraManager(HRAManager hraManager) {
        this.hraManager = hraManager;
    }
}
