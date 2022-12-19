package com.inov8.microbank.webapp.action.loginmodule;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author Abu Turab 25-10-2017
 */
public class AgentWebLoginFormController
    implements Controller
{
  public AgentWebLoginFormController()
  {
  }

  public ModelAndView handleRequest(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse) throws
      Exception
  {

    return new ModelAndView("awlogin");
  }
}
