package com.inov8.microbank.webapp.filter;

import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.UserUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.owasp.esapi.ESAPI;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.regex.Pattern;

public class XSSCleanFilter implements Filter {
	protected final Log logger = LogFactory.getLog(getClass());
	private FilterConfig filterConfig;
	private final String sqlRegex = MessageUtil.getMessage("xsscleanfilter.sqlRegex");

	static class FilteredRequest extends HttpServletRequestWrapper {

		/* These are the characters allowed by the Javascript validation */
		static String allowedChars = "+-0123456789#*&_";

		public FilteredRequest(ServletRequest request) {
			super((HttpServletRequest) request);
		}

		public String sanitize(String input) {
			String result = "";

			result = ESAPI.encoder().encodeForHTML(input);
			return result;
		}

		public String getParameter(String paramName) {
//			StringBuilder sb = new StringBuilder();
//			for(int i = 0; i < paramName.length(); ++i) {
//				char c = paramName.charAt(i);
//				if(c!='\r' && c!='\n' && c!='%' && c!='&' && c!='!' && c!='@' && c!='$' && c!='|' && c!='/' && c!='\\' && c!='#' && c!='^' && c!='~' && c!='*' && c!='(' && c!=')' && c!='"' && c!=':' && c!='?' && c!='=' && c!='+' && c!='[' && c!=']' && c!='{' && c!='}'){
//					sb.append("" + Character.valueOf(c));
//				}else{
//					getRequest().setAttribute("parameterCompromized", "true");
//					break;
//				}
//			}
			String value = super.getParameter(paramName);
			if(!StringUtil.isNullOrEmpty(value) && !value.contains("<msg")){
				value = sanitize(value);
			}
			return value;
		}


		public String[] getParameterValues(String paramName) {
			String values[] = super.getParameterValues(paramName);

			for (int index = 0; index < values.length; index++) {
				if (!StringUtil.isNullOrEmpty(values[index])
						&& !values[index].startsWith("<msg")) {

					values[index] = ESAPI.encoder()
							.encodeForHTML(values[index]);
				}
			}

			return values;
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	public void destroy() {
		this.filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		try {
			Enumeration<String> enumeration = request.getParameterNames();

			HttpServletRequest httpRequest = (HttpServletRequest) request;
			String requesterIp = UserUtils.getClientIpAddress(httpRequest);
			String requesterSessionId = httpRequest.getSession().getId();
			String ipAddressInSession = (String) httpRequest.getSession().getAttribute(requesterSessionId);

			while (enumeration.hasMoreElements()) {
				String paramName = enumeration.nextElement();
				String value = (String) request.getParameter(paramName);

				if (value != null && (Pattern.compile(sqlRegex).matcher(value.toUpperCase()).find())) {
					logger.error("\n\n\n***************************************************************************\n"
							+ "SQL Injection Detected... paramName=" + paramName + " , value=" + value +
							"\n***************************************************************************\n\n");

					HttpServletResponse resp = (HttpServletResponse) response;
					resp.sendRedirect("/error_.html");
				}

			}

			//Turab:Security:Duplicate Cookies issue handled
			if (null != ipAddressInSession && !requesterIp.equals(ipAddressInSession)) {
				logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				logger.info("Malicious attempt to access application is received!");
				logger.info("I.P. address " + requesterIp + " has tried to access the session of I.P. address " + ipAddressInSession + " 's session (" + requesterSessionId + ") ");
				logger.info("<< Throwing back to login page >>");
				logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				httpResponse.sendRedirect("logout.jsp?error=true");
			}

			chain.doFilter(new FilteredRequest(request), response);
		}
		catch (Exception e){
			logger.error("", e);
			e.printStackTrace();
			throw new RuntimeException();
		}
		
	}

}
