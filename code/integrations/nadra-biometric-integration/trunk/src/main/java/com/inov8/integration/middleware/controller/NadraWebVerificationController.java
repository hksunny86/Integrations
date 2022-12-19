package com.inov8.integration.middleware.controller;

import com.inov8.integration.middleware.util.ConfigReader;
import com.inov8.integration.vo.NadraIntegrationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.util.*;

@Controller
public class NadraWebVerificationController {


    private static String FRANCHISE_ID = ConfigReader.getInstance().getProperty("nadra.franchize.id", "");

    private NadraIntegrationController controller;

    @PostConstruct
    public void init() {

        try {
            getFromProxy("172.24.10.192");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(Model model) {
        populateDefaultModel(model, true);
        return "verify";
    }



    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    public ModelAndView verify(@ModelAttribute("verification") NadraIntegrationVO integrationVO, Model model) {

        integrationVO.setServiceProviderTransactionId(FRANCHISE_ID + getRandomNumberInRange());
        integrationVO.setSecondaryCitizenNumber("3520227440669");
        integrationVO.setSecondaryContactNo("03454677743");
        integrationVO.setRemittanceType("MONENY_TRANSFER_RECEIVE");

        try {
            integrationVO = controller.otcFingerPrintVerification(integrationVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ModelAndView modelAndView=new ModelAndView("result");
        modelAndView.addObject("verification",integrationVO);

        return modelAndView;
    }


    public void getFromProxy(String server) throws Exception {
        HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
        httpInvokerProxyFactoryBean.setServiceInterface(NadraIntegrationController.class);
        httpInvokerProxyFactoryBean.setServiceUrl("http://" + server + "/nadra-biometric-integration/ws/api");
        httpInvokerProxyFactoryBean.afterPropertiesSet();
        controller = (NadraIntegrationController) httpInvokerProxyFactoryBean.getObject();
    }


    private static long getRandomNumberInRange() {

        long LOWER_RANGE = 100000000000000L; //assign lower range value
        long UPPER_RANGE = 999999999999999L; //assign upper range value
        Random random = new Random();

        return LOWER_RANGE + (long)(random.nextDouble()*(UPPER_RANGE - LOWER_RANGE));

    }


    private void populateDefaultModel(Model model, boolean isNew) {

        Map<String, String> indexList = new LinkedHashMap<String, String>();
        indexList.put("1", "Right Thumb");
        indexList.put("2", "Right Index Finger");
        indexList.put("3", "Right Middle Finger");
        indexList.put("4", "Right Ring Finger");
        indexList.put("5", "Right Little Finger");
        indexList.put("6", "Left Thumb");
        indexList.put("7", "Left Index Finger");
        indexList.put("8", "Left Middle Finger");
        indexList.put("9", "Left Ring Finger");
        indexList.put("10", "Left Little Finger");
        model.addAttribute("indexList", indexList);

        Map<String, String> templateList = new LinkedHashMap<String, String>();
        templateList.put("ANSI", "ANSI");
        templateList.put("ISO_19794_2", "ISO_19794_2");
        templateList.put("RAW_IMAGE", "RAW_IMAGE");
        model.addAttribute("templateList", templateList);

        Map<String, String> areaList = new LinkedHashMap<String, String>();
        areaList.put("khyber-pakhtunkhwa", "Khyber-Pakhtunkhwa");
        areaList.put("fata", "Fata");
        areaList.put("punjab", "Punjab");
        areaList.put("sindh", "Sindh");
        areaList.put("baluchistan", "Baluchistan");
        areaList.put("islamabad", "Islamabad");
        areaList.put("gilgit-baltistan", "Gilgit-Baltistan");
        areaList.put("azad Kashmir", "Azad Kashmir");
        model.addAttribute("areaList", areaList);

        if(isNew) {
            model.addAttribute("verification", new NadraIntegrationVO());
        }

    }

}
