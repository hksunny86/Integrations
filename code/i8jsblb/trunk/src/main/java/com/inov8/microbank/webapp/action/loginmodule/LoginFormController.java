package com.inov8.microbank.webapp.action.loginmodule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * <p>Title: Microbank Demo</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 */

public class LoginFormController
    implements Controller
{
  public LoginFormController()
  {
  }

  public ModelAndView handleRequest(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse) throws
      Exception
  {
    return new ModelAndView("login");
  }
}
