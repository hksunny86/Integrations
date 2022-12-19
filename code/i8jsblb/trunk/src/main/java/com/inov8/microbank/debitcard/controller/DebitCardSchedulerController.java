package com.inov8.microbank.debitcard.controller;

import com.inov8.microbank.debitcard.service.DebitCardManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

@Controller
public class DebitCardSchedulerController {

    private static final Logger LOGGER = Logger.getLogger(DebitCardSchedulerController.class);

    private DebitCardManager debitCardManager;

    public void setDebitCardManager(DebitCardManager debitCardManager) {
        this.debitCardManager = debitCardManager;
    }

    @RequestMapping(value = "/debitCardSchedulersController")
    public ModelAndView runSchedulerManually(HttpServletRequest request,Model model)
    {
        LOGGER.info("*********** Starting Manually DebitCard Data Import Export ***********");
        try
        {
            debitCardManager.saveDebitCardImportExportSchedulerRequest();
        }
        catch(Exception ex)
        {
            model.addAttribute("messages", "Unable perform selected operation.");
            LOGGER.error("Unable perform selected operation." + ex.getMessage(),ex);
        }
        LOGGER.info("*********** End of Manually DebitCard Data Import Export ***********");
        request.setAttribute("isManual", "true");
        String view = "redirect:schedulersmanagement.html";
        ModelAndView modelAndView = new ModelAndView(view);
        modelAndView.addObject("isManual","true");
        return modelAndView;
    }

    /*@Override
    public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        StringBuffer buffer = new StringBuffer();
        String msg = "has been updated successfully.";
        LOGGER.info("*********** Starting Manually DebitCard Data Import Export ***********");
        try
        {
            debitCardManager.saveDebitCardImportExportSchedulerRequest();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        LOGGER.info("*********** End of Manually DebitCard Data Import Export ***********");
        buffer.append("Success");
        buffer.append(getMessageSourceAccessor().getMessage("issue.inov8.success", new String[] { msg },
                request.getLocale()));
        return buffer.toString();
    }*/
}
