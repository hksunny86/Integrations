package com.inov8.microbank.nadraVerisys.controller;

import com.inov8.microbank.nadraVerisys.model.VerisysDataModel;
import com.inov8.microbank.nadraVerisys.service.VerisysDataManager;
import com.inov8.microbank.server.dao.customermodule.RegistrationStateDAO;
import com.inov8.microbank.server.dao.portal.citymodule.CityDAO;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bkr on 11/9/2016.
 */
public class VerisysDataViewController extends AbstractController {

    private VerisysDataManager verisysDataManager;
    private RegistrationStateDAO registrationStateDAO;
    private CityDAO cityDAO;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String nic = ServletRequestUtils.getStringParameter(request,"id");
        Map<String,Object> dataMap = new HashMap<>();
        VerisysDataModel verisysDataModel = verisysDataManager.loadVerisysDataModel(nic);
        if(null == verisysDataModel){
            dataMap.put("showErrorMsg",true);
            dataMap.put("errorMsg","Nadra Verisys Data Not Found against this CNIC.");
            return new ModelAndView("p_verisysdata",dataMap);
        }
//        RegistrationStateModel registrationStateModel = registrationStateDAO.getRegistrationStateById(verisysDataModel.getRegistrationStateId());
//        if(null == registrationStateModel){
//            registrationStateModel = new RegistrationStateModel();
//            registrationStateModel.setName("Invalid Registration State Id");
//        }
        dataMap.put("verisysDataModel",verisysDataModel);
//        dataMap.put("registrationStateModel",registrationStateModel);
        return new ModelAndView("p_verisysdata",dataMap);
    }

    public void setVerisysDataManager(VerisysDataManager verisysDataManager) {
        this.verisysDataManager = verisysDataManager;
    }

    public void setRegistrationStateDAO(RegistrationStateDAO registrationStateDAO) {
        this.registrationStateDAO = registrationStateDAO;
    }

    public void setCityDAO(CityDAO cityDAO) {
        this.cityDAO = cityDAO;
    }
}
