package com.inov8.microbank.webapp.action.mfs.jme;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class TestFalconController implements Controller
{
	  public TestFalconController()
	  {
	  }

	  public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception
	  {
		  return new ModelAndView("test-falcon");
	  }
}