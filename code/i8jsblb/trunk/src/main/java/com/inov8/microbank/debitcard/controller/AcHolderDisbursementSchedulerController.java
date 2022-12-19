package com.inov8.microbank.debitcard.controller;

import com.inov8.microbank.disbursement.job.AcHolderDisbursementScheduler;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AcHolderDisbursementSchedulerController {
    private static final Logger LOGGER = Logger.getLogger(AcHolderDisbursementSchedulerController.class);

    private AcHolderDisbursementScheduler acHolderDisbursementScheduler;


    @RequestMapping(value = "/acHolderDisbursementSchedulersController")
    public ModelAndView runSchedulerManually(HttpServletRequest request, Model model)
    {
        LOGGER.info("*********** Starting Manually AC Holder Disbursment Scheduler ***********");
        try
        {
            acHolderDisbursementScheduler.startExecution();
        }
        catch(Exception ex)
        {
            model.addAttribute("messages", "Unable perform selected operation.");
            LOGGER.error("Unable perform selected operation." + ex.getMessage(),ex);
        }
        LOGGER.info("*********** End of Manually AC Holder Disbursment Scheduler ***********");
        request.setAttribute("isManual", "true");
        String view = "redirect:schedulersmanagement.html";
        ModelAndView modelAndView = new ModelAndView(view);
        modelAndView.addObject("isManual","true");
        return modelAndView;
    }

    public void setAcHolderDisbursementScheduler(AcHolderDisbursementScheduler acHolderDisbursementScheduler) {
        this.acHolderDisbursementScheduler = acHolderDisbursementScheduler;
    }
}
