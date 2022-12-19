package com.inov8.integration.middleware.controller;

import com.inov8.integration.middleware.mock.MockService;
import com.inov8.integration.middleware.mock.MockTransactionDAO;
import com.inov8.integration.vo.NadraIntegrationVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller("nadraMockController")
public class nadraMockController {

    @Autowired
    private MockTransactionDAO transactionDAO;
    @Autowired
    private MockService mockService;
    private static Logger logger = LoggerFactory.getLogger(nadraMockController.class.getSimpleName());

    @RequestMapping(value = "/nadra-biometric-integration-mock", method = RequestMethod.GET)
    public ModelAndView homePage(@ModelAttribute("command") NadraIntegrationVO nadraIntegrationVO) {
        return new ModelAndView("index");


    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public ModelAndView nadraPage(@ModelAttribute("command") NadraIntegrationVO nadraIntegrationVO, @RequestParam("fingerPrintTemplate") String fingerPrintTemplate) {
        if (nadraIntegrationVO.getCitizenNumber().equals(""))

        {
            return new ModelAndView("index");
        } else {
            boolean result = mockService.saveResponse(nadraIntegrationVO, fingerPrintTemplate);
            return new ModelAndView("index");
        }


    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ModelAndView search(@ModelAttribute("command") NadraIntegrationVO nadraIntegrationVO, @RequestParam("searchCnic") String searchCnic) {
        ModelAndView modelAndView = new ModelAndView("index");
        nadraIntegrationVO = mockService.getCustomerData(searchCnic);
        if (nadraIntegrationVO != null) {
            modelAndView.addObject("nadraIntegrationVO", nadraIntegrationVO);
            return modelAndView;
        } else
            return new ModelAndView("index");


    }

}
